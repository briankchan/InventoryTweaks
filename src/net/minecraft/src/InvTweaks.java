package net.minecraft.src;

import invtweaks.InvTweaksConst;
import invtweaks.InvTweaksItemTree;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class InvTweaks extends InvTweaksObfuscation
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private static InvTweaks instance;
    private InvTweaksConfigManager cfgManager = null;
    private int chestAlgorithm = 0;
    private long chestAlgorithmClickTimestamp = 0L;
    private boolean chestAlgorithmButtonDown = false;
    private int storedStackId = 0;
    private int storedStackDamage = -1;
    private int storedFocusedSlot = -1;
    private ItemStack[] hotbarClone = new ItemStack[9];
    private boolean hadFocus = true;
    private boolean mouseWasDown = false;
    private int tickNumber = 0;
    private int lastPollingTickNumber = -3;
    private long sortingKeyPressedDate = 0L;
    private boolean sortKeyDown = false;
    private boolean itemPickupPending = false;

    public InvTweaks(Minecraft var1)
    {
        super(var1);
        log.setLevel(InvTweaksConst.DEFAULT_LOG_LEVEL);
        instance = this;
        this.cfgManager = new InvTweaksConfigManager(var1);

        if (this.cfgManager.makeSureConfigurationIsLoaded())
        {
            log.info("Mod initialized");
        }
        else
        {
            log.severe("Mod failed to initialize!");
        }
    }

    public void onTickInGame()
    {
        synchronized (this)
        {
            if (this.onTick())
            {
                this.handleAutoRefill();
            }
        }
    }

    public void onTickInGUI(GuiScreen var1)
    {
        synchronized (this)
        {
            this.handleMiddleClick(var1);

            if (this.onTick())
            {
                if (this.isTimeForPolling())
                {
                    this.unlockKeysIfNecessary();
                }

                this.handleGUILayout(var1);
                this.handleShortcuts(var1);
                ItemStack var3 = this.getFocusedStack();
                this.storedStackId = var3 == null ? 0 : this.getItemID(var3);
                this.storedStackDamage = var3 == null ? 0 : this.getItemDamage(var3);
            }
        }
    }

    public final void onSortingKeyPressed()
    {
        synchronized (this)
        {
            if (this.cfgManager.makeSureConfigurationIsLoaded())
            {
                GuiScreen var2 = this.getCurrentScreen();

                if (var2 == null || this.isValidChest(var2) || this.isValidInventory(var2))
                {
                    this.handleSorting(var2);
                }
            }
        }
    }

    public void onItemPickup()
    {
        if (this.cfgManager.makeSureConfigurationIsLoaded())
        {
            InvTweaksConfig var1 = this.cfgManager.getConfig();

            if (!this.cfgManager.getConfig().getProperty("enableSortingOnPickup").equals("false"))
            {
                try
                {
                    InvTweaksContainerSectionManager var2 = new InvTweaksContainerSectionManager(this.mc, InvTweaksContainerSection.INVENTORY);
                    var2.setClickDelay(var1.getClickDelay());
                    int var3 = -1;

                    for (int var4 = 0; var4 < 9; ++var4)
                    {
                        ItemStack var5 = var2.getItemStack(var4 + 27);

                        if (var5 != null && this.getAnimationsToGo(var5) == 5 && this.hotbarClone[var4] == null)
                        {
                            var3 = var4 + 27;
                        }
                    }

                    if (var3 != -1)
                    {
                        this.itemPickupPending = false;
                        LinkedList var16 = new LinkedList();
                        InvTweaksItemTree var17 = var1.getTree();
                        ItemStack var6 = var2.getItemStack(var3);
                        List var7 = var17.getItems(this.getItemID(var6), this.getItemDamage(var6));
                        Iterator var8 = var1.getRules().iterator();

                        while (var8.hasNext())
                        {
                            InvTweaksConfigSortingRule var9 = (InvTweaksConfigSortingRule)var8.next();

                            if (var17.matches(var7, var9.getKeyword()))
                            {
                                int[] var10 = var9.getPreferredSlots();
                                int var11 = var10.length;

                                for (int var12 = 0; var12 < var11; ++var12)
                                {
                                    int var13 = var10[var12];
                                    var16.add(Integer.valueOf(var13));
                                }
                            }
                        }

                        boolean var18 = true;

                        if (var16 != null)
                        {
                            Iterator var19 = var16.iterator();

                            while (var19.hasNext())
                            {
                                int var21 = ((Integer)var19.next()).intValue();

                                try
                                {
                                    if (var21 == var3)
                                    {
                                        var18 = false;
                                        break;
                                    }

                                    if (var2.getItemStack(var21) == null && var2.move(var3, var21))
                                    {
                                        break;
                                    }
                                }
                                catch (TimeoutException var14)
                                {
                                    this.logInGameError("Failed to move picked up stack", var14);
                                }
                            }
                        }

                        if (var18)
                        {
                            for (int var20 = 0; var20 < var2.getSize() && (var2.getItemStack(var20) != null || !var2.move(var3, var20)); ++var20)
                            {
                                ;
                            }
                        }
                    }
                }
                catch (Exception var15)
                {
                    this.logInGameError("Failed to move picked up stack", var15);
                }
            }
        }
    }

    public void setItemPickupPending(boolean var1)
    {
        this.itemPickupPending = var1;
    }

    public void logInGame(String var1)
    {
        this.logInGame(var1, false);
    }

    public void logInGame(String var1, boolean var2)
    {
        String var3 = this.buildlogString(Level.INFO, var2 ? var1 : InvTweaksLocalization.get(var1));
        this.addChatMessage(var3);
        log.info(var3);
    }

    public void logInGameError(String var1, Exception var2)
    {
        String var3 = this.buildlogString(Level.SEVERE, InvTweaksLocalization.get(var1), var2);
        this.addChatMessage(var3);
        log.severe(var3);
    }

    public static void logInGameStatic(String var0)
    {
        getInstance().logInGame(var0);
    }

    public static void logInGameErrorStatic(String var0, Exception var1)
    {
        getInstance().logInGameError(var0, var1);
    }

    public static InvTweaks getInstance()
    {
        return instance;
    }

    public static Minecraft getMinecraftInstance()
    {
        return instance.mc;
    }

    public static InvTweaksConfigManager getConfigManager()
    {
        return instance.cfgManager;
    }

    public static boolean classExists(String var0)
    {
        try
        {
            return Class.forName(var0) != null;
        }
        catch (ClassNotFoundException var2)
        {
            return false;
        }
    }

    private boolean onTick()
    {
        ++this.tickNumber;
        InvTweaksConfig var1 = this.cfgManager.getConfig();

        if (var1 == null)
        {
            return false;
        }
        else
        {
            if (this.itemPickupPending)
            {
                this.onItemPickup();
            }

            GuiScreen var2 = this.getCurrentScreen();

            if (var2 == null || this.isGuiInventory(var2))
            {
                this.cloneHotbar();
            }

            if (Keyboard.isKeyDown(var1.getSortKeyCode()))
            {
                if (!this.sortKeyDown)
                {
                    this.sortKeyDown = true;
                    this.onSortingKeyPressed();
                }
            }
            else
            {
                this.sortKeyDown = false;
            }

            this.handleConfigSwitch();
            return true;
        }
    }

    private void handleConfigSwitch()
    {
        InvTweaksConfig var1 = this.cfgManager.getConfig();
        GuiScreen var2 = this.getCurrentScreen();
        this.cfgManager.getShortcutsHandler().updatePressedKeys();
        InvTweaksShortcutMapping var3 = this.cfgManager.getShortcutsHandler().isShortcutDown(InvTweaksShortcutType.MOVE_TO_SPECIFIC_HOTBAR_SLOT);

        if (this.isSortingShortcutDown() && var3 != null)
        {
            String var4 = null;
            int var5 = ((Integer)var3.getKeyCodes().get(0)).intValue();

            if (var5 >= 2 && var5 <= 10)
            {
                var4 = var1.switchConfig(var5 - 2);
            }
            else
            {
                switch (var5)
                {
                    case 71:
                        var4 = var1.switchConfig(6);
                        break;

                    case 72:
                        var4 = var1.switchConfig(7);
                        break;

                    case 73:
                        var4 = var1.switchConfig(8);

                    case 74:
                    case 78:
                    default:
                        break;

                    case 75:
                        var4 = var1.switchConfig(3);
                        break;

                    case 76:
                        var4 = var1.switchConfig(4);
                        break;

                    case 77:
                        var4 = var1.switchConfig(5);
                        break;

                    case 79:
                        var4 = var1.switchConfig(0);
                        break;

                    case 80:
                        var4 = var1.switchConfig(1);
                        break;

                    case 81:
                        var4 = var1.switchConfig(2);
                }
            }

            if (var4 != null)
            {
                this.logInGame(String.format(InvTweaksLocalization.get("invtweaks.loadconfig.enabled"), new Object[] {var4}), true);
                this.sortingKeyPressedDate = 2147483647L;
            }
        }

        if (this.isSortingShortcutDown())
        {
            long var8 = System.currentTimeMillis();

            if (this.sortingKeyPressedDate == 0L)
            {
                this.sortingKeyPressedDate = var8;
            }
            else if (var8 - this.sortingKeyPressedDate > 1000L && this.sortingKeyPressedDate != 2147483647L)
            {
                String var6 = var1.getCurrentRulesetName();
                String var7 = var1.switchConfig();

                if (var6 != null && var7 != null && !var6.equals(var7))
                {
                    this.logInGame(String.format(InvTweaksLocalization.get("invtweaks.loadconfig.enabled"), new Object[] {var7}), true);
                    this.handleSorting(var2);
                }

                this.sortingKeyPressedDate = var8;
            }
        }
        else
        {
            this.sortingKeyPressedDate = 0L;
        }
    }

    private void handleSorting(GuiScreen var1)
    {
        ItemStack var2 = null;
        int var3 = this.getFocusedSlot();
        ItemStack[] var4 = this.getMainInventory();

        if (var3 < var4.length && var3 >= 0)
        {
            var2 = var4[var3];
        }

        try
        {
            (new InvTweaksHandlerSorting(this.mc, this.cfgManager.getConfig(), InvTweaksContainerSection.INVENTORY, 3, 9)).sort();
        }
        catch (Exception var6)
        {
            this.logInGameError("invtweaks.sort.inventory.error", var6);
            var6.printStackTrace();
        }

        this.playClick();

        if (var2 != null && var4[var3] == null)
        {
            this.storedStackId = 0;
        }
    }

    private void handleAutoRefill()
    {
        ItemStack var1 = this.getFocusedStack();
        int var2 = var1 == null ? 0 : this.getItemID(var1);
        int var3 = var1 == null ? 0 : this.getItemDamage(var1);
        int var4 = this.getFocusedSlot() + 27;
        InvTweaksConfig var5 = this.cfgManager.getConfig();

        if (var2 != this.storedStackId || var3 != this.storedStackDamage)
        {
            if (this.storedFocusedSlot != var4)
            {
                this.storedFocusedSlot = var4;
            }
            else if ((var1 == null || this.getItemID(var1) == 281 && this.storedStackId == 282) && (this.getCurrentScreen() == null || this.isGuiEditSign(this.getCurrentScreen())))
            {
                if (var5.isAutoRefillEnabled(this.storedStackId, this.storedStackId))
                {
                    try
                    {
                        this.cfgManager.getAutoRefillHandler().autoRefillSlot(var4, this.storedStackId, this.storedStackDamage);
                    }
                    catch (Exception var9)
                    {
                        this.logInGameError("invtweaks.sort.autorefill.error", var9);
                    }
                }
            }
            else
            {
                int var6 = this.getMaxDamage(this.getItem(var1));

                if (var6 != 0 && var6 - var3 < 5 && var6 - this.storedStackDamage >= 5 && var5.getProperty("autoRefillBeforeBreak").equals("true") && var5.isAutoRefillEnabled(this.storedStackId, this.storedStackId))
                {
                    try
                    {
                        this.cfgManager.getAutoRefillHandler().autoRefillSlot(var4, this.storedStackId, this.storedStackDamage);
                    }
                    catch (Exception var8)
                    {
                        this.logInGameError("invtweaks.sort.autorefill.error", var8);
                    }
                }
            }
        }

        this.storedStackId = var2;
        this.storedStackDamage = var3;
    }

    private void handleMiddleClick(GuiScreen var1)
    {
        if (Mouse.isButtonDown(2))
        {
            if (!this.cfgManager.makeSureConfigurationIsLoaded())
            {
                return;
            }

            InvTweaksConfig var2 = this.cfgManager.getConfig();

            if (var2.getProperty("enableMiddleClick").equals("true") && !this.chestAlgorithmButtonDown)
            {
                this.chestAlgorithmButtonDown = true;
                InvTweaksContainerManager var3 = new InvTweaksContainerManager(this.mc);
                var3.setClickDelay(var2.getClickDelay());
                Slot var4 = var3.getSlotAtMousePosition();
                InvTweaksContainerSection var5 = null;

                if (var4 != null)
                {
                    var5 = var3.getSlotSection(this.getSlotNumber(var4));
                }

                if (this.isValidChest(var1))
                {
                    GuiContainer var6 = this.asGuiContainer(var1);

                    if (InvTweaksContainerSection.CHEST.equals(var5))
                    {
                        this.playClick();
                        long var7 = System.currentTimeMillis();

                        if (var7 - this.chestAlgorithmClickTimestamp > 2000L)
                        {
                            this.chestAlgorithm = 0;
                        }

                        try
                        {
                            (new InvTweaksHandlerSorting(this.mc, this.cfgManager.getConfig(), InvTweaksContainerSection.CHEST, this.chestAlgorithm, this.getContainerRowSize(var6))).sort();
                        }
                        catch (Exception var10)
                        {
                            this.logInGameError("invtweaks.sort.chest.error", var10);
                            var10.printStackTrace();
                        }

                        this.chestAlgorithm = (this.chestAlgorithm + 1) % 3;
                        this.chestAlgorithmClickTimestamp = var7;
                    }
                    else if (InvTweaksContainerSection.INVENTORY_HOTBAR.equals(var5) || InvTweaksContainerSection.INVENTORY_NOT_HOTBAR.equals(var5))
                    {
                        this.handleSorting(var1);
                    }
                }
                else if (this.isValidInventory(var1))
                {
										// Crafting stacks evening
										if(InvTweaksContainerSection.CRAFTING_IN.equals(var5))
										{
												try
												{
														new InvTweaksHandlerSorting(this.mc, this.cfgManager.getConfig(), InvTweaksContainerSection.CRAFTING_IN, InvTweaksHandlerSorting.ALGORITHM_EVEN_STACKS, (var3.getSize(var5) == 9) ? 3 : 2).sort();
                        }
												catch(Exception e)
												{
														logInGameError("invtweaks.sort.crafting.error", e);
														 e.printStackTrace();
												}
										}
										else
										{
												this.handleSorting(var1);
										}
                }
            }
        }
        else
        {
            this.chestAlgorithmButtonDown = false;
        }
    }

    private void handleGUILayout(GuiScreen var1)
    {
        InvTweaksConfig var2 = this.cfgManager.getConfig();
        boolean var3 = this.isValidChest(var1);

        if (!var3 && (!this.isStandardInventory(var1) || this.isGuiEnchantmentTable(var1)))
        {
            if (this.isGuiInventoryCreative(var1))
            {
                List var16 = this.getControlList(var1);
                GuiButton var17 = null;
                Iterator var18 = var16.iterator();

                while (var18.hasNext())
                {
                    Object var19 = var18.next();

                    if (this.isGuiButton(var19) && this.getId(this.asGuiButton(var19)) == 54696386)
                    {
                        var17 = this.asGuiButton(var19);
                        break;
                    }
                }

                if (var17 != null)
                {
                    var16.remove(var17);
                }
            }
        }
        else
        {
            GuiContainer var4 = this.asGuiContainer(var1);
            byte var5 = 10;
            byte var6 = 10;
            boolean var7 = false;
            List var8 = this.getControlList(var1);
            Iterator var9 = var8.iterator();

            while (var9.hasNext())
            {
                Object var10 = var9.next();

                if (this.isGuiButton(var10))
                {
                    GuiButton var11 = this.asGuiButton(var10);

                    if (this.getId(var11) == 54696386)
                    {
                        var7 = true;
                        break;
                    }
                }
            }

            if (!var7)
            {
                boolean var20 = this.hasTexture("/gui/button10px.png");

                if (!var3)
                {
                    var8.add(new InvTweaksGuiSettingsButton(this.cfgManager, 54696386, this.getGuiX(var4) + this.getGuiWidth(var4) - 15, this.getGuiY(var4) + 5, var5, var6, "...", InvTweaksLocalization.get("invtweaks.button.settings.tooltip"), var20));
                }
                else
                {
                    this.chestAlgorithmClickTimestamp = 0L;
                    int var22 = 54696386;
                    int var21 = this.getGuiX(var4) + this.getGuiWidth(var4) - 16;
                    int var12 = this.getGuiY(var4) + 5;
                    boolean var13 = this.mods.isChestWayTooBig(var1);

                    if (var13 && classExists("mod_NotEnoughItems") && this.isNotEnoughItemsEnabled())
                    {
                        var21 = this.getGuiX(var4) + this.getGuiWidth(var4) - 35;
                        var12 += 50;
                    }

                    var8.add(new InvTweaksGuiSettingsButton(this.cfgManager, var22++, var13 ? var21 + 22 : var21 - 1, var13 ? var12 - 3 : var12, var5, var6, "...", InvTweaksLocalization.get("invtweaks.button.settings.tooltip"), var20));

                    if (!var2.getProperty("showChestButtons").equals("false"))
                    {
                        int var14 = this.getContainerRowSize(var4);
                        InvTweaksGuiSortingButton var15 = new InvTweaksGuiSortingButton(this.cfgManager, var22++, var13 ? var21 + 22 : var21 - 13, var13 ? var12 + 12 : var12, var5, var6, "h", InvTweaksLocalization.get("invtweaks.button.chest3.tooltip"), 2, var14, var20);
                        var8.add(var15);
                        var15 = new InvTweaksGuiSortingButton(this.cfgManager, var22++, var13 ? var21 + 22 : var21 - 25, var13 ? var12 + 25 : var12, var5, var6, "v", InvTweaksLocalization.get("invtweaks.button.chest2.tooltip"), 1, var14, var20);
                        var8.add(var15);
                        var15 = new InvTweaksGuiSortingButton(this.cfgManager, var22++, var13 ? var21 + 22 : var21 - 37, var13 ? var12 + 38 : var12, var5, var6, "s", InvTweaksLocalization.get("invtweaks.button.chest1.tooltip"), 0, var14, var20);
                        var8.add(var15);
                    }
                }
            }
        }
    }

    private boolean isNotEnoughItemsEnabled()
    {
        BufferedReader var1 = null;
        boolean var3;

        try
        {
            var1 = new BufferedReader(new FileReader(new File(InvTweaksConst.MINECRAFT_CONFIG_DIR + "NEI.cfg")));
            String var2;

            while ((var2 = var1.readLine()) != null)
            {
                if (var2.contains("enable=true"))
                {
                    var3 = true;
                    return var3;
                }
            }

            var3 = false;
            return var3;
        }
        catch (IOException var14)
        {
            var3 = false;
        }
        finally
        {
            if (var1 != null)
            {
                try
                {
                    var1.close();
                }
                catch (IOException var13)
                {
                    var13.printStackTrace();
                }
            }
        }

        return var3;
    }

    private void handleShortcuts(GuiScreen var1)
    {
        if (this.isValidChest(var1) || this.isStandardInventory(var1))
        {
            if (!this.isGuiInventoryCreative(var1))
            {
                if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1))
                {
                    this.mouseWasDown = false;
                }
                else if (!this.mouseWasDown)
                {
                    this.mouseWasDown = true;

                    if (this.cfgManager.getConfig().getProperty("enableShortcuts").equals("true"))
                    {
                        this.cfgManager.getShortcutsHandler().handleShortcut();
                    }
                }
            }
        }
    }

    private int getContainerRowSize(GuiContainer var1)
    {
        return this.isGuiChest(var1) ? 9 : (this.isGuiDispenser(var1) ? 3 : this.getSpecialChestRowSize(var1, 9));
    }

    private boolean isSortingShortcutDown()
    {
        int var1 = this.cfgManager.getConfig().getSortKeyCode();
        return var1 > 0 ? Keyboard.isKeyDown(var1) : Mouse.isButtonDown(100 + var1);
    }

    private boolean isTimeForPolling()
    {
        if (this.tickNumber - this.lastPollingTickNumber >= 3)
        {
            this.lastPollingTickNumber = this.tickNumber;
        }

        return this.tickNumber - this.lastPollingTickNumber == 0;
    }

    private void unlockKeysIfNecessary()
    {
        boolean var1 = Display.isActive();

        if (!this.hadFocus && var1)
        {
            Keyboard.destroy();
            boolean var2 = true;

            while (!Keyboard.isCreated())
            {
                try
                {
                    Keyboard.create();
                }
                catch (LWJGLException var4)
                {
                    if (var2)
                    {
                        this.logInGameError("invtweaks.keyboardfix.error", var4);
                        var2 = false;
                    }
                }
            }

            if (!var2)
            {
                this.logInGame("invtweaks.keyboardfix.recover");
            }
        }

        this.hadFocus = var1;
    }

    private void cloneHotbar()
    {
        ItemStack[] var1 = this.getMainInventory();

        for (int var2 = 0; var2 < 9; ++var2)
        {
            if (var1[var2] != null)
            {
                this.hotbarClone[var2] = this.copy(var1[var2]);
            }
            else
            {
                this.hotbarClone[var2] = null;
            }
        }
    }

    private void playClick()
    {
        if (!this.cfgManager.getConfig().getProperty("enableSounds").equals("false"))
        {
            this.playSound("random.click", 0.6F, 1.8F);
        }
    }

    private String buildlogString(Level var1, String var2, Exception var3)
    {
        if (var3 != null)
        {
            StackTraceElement var4 = var3.getStackTrace()[0];
            return this.buildlogString(var1, var2) + ": " + var3.getMessage() + " (l" + var4.getLineNumber() + " in " + var4.getFileName().replace("InvTweaks", "") + ")";
        }
        else
        {
            return this.buildlogString(var1, var2);
        }
    }

    private String buildlogString(Level var1, String var2)
    {
        return "InvTweaks: " + (var1.equals(Level.SEVERE) ? "[ERROR] " : "") + var2;
    }
}
