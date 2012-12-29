package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;

public class InvTweaksObfuscation
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    protected Minecraft mc;
    protected InvTweaksModCompatibility mods;
    private static Map fieldsMap = new HashMap();

    public InvTweaksObfuscation(Minecraft var1)
    {
        this.mc = var1;
        this.mods = new InvTweaksModCompatibility(this);
    }

    protected void addChatMessage(String var1)
    {
        if (this.mc.ingameGUI != null)
        {
            this.mc.ingameGUI.getChatGUI().printChatMessage(var1);
        }
    }

    protected EntityClientPlayerMP getThePlayer()
    {
        return this.mc.thePlayer;
    }

    protected WorldClient getTheWorld()
    {
        return this.mc.theWorld;
    }

    protected PlayerControllerMP getPlayerController()
    {
        return this.mc.playerController;
    }

    protected GuiScreen getCurrentScreen()
    {
        return this.mc.currentScreen;
    }

    protected FontRenderer getFontRenderer()
    {
        return this.mc.fontRenderer;
    }

    protected void displayGuiScreen(GuiScreen var1)
    {
        this.mc.displayGuiScreen(var1);
    }

    protected int getDisplayWidth()
    {
        return this.mc.displayWidth;
    }

    protected int getDisplayHeight()
    {
        return this.mc.displayHeight;
    }

    protected GameSettings getGameSettings()
    {
        return this.mc.gameSettings;
    }

    public KeyBinding[] getRegisteredBindings()
    {
        return this.getGameSettings().keyBindings;
    }

    public void setRegisteredBindings(KeyBinding[] var1)
    {
        this.getGameSettings().keyBindings = var1;
    }

    protected int getKeyBindingForwardKeyCode()
    {
        return this.getKeyCode(this.getGameSettings().keyBindForward);
    }

    protected int getKeyBindingBackKeyCode()
    {
        return this.getKeyCode(this.getGameSettings().keyBindBack);
    }

    protected InventoryPlayer getInventoryPlayer()
    {
        return this.getThePlayer().inventory;
    }

    protected ItemStack getCurrentEquippedItem()
    {
        return this.getThePlayer().getCurrentEquippedItem();
    }

    protected Container getCraftingInventory()
    {
        return this.getThePlayer().inventoryContainer;
    }

    protected Container getPlayerContainer()
    {
        return this.getThePlayer().inventoryContainer;
    }

    protected ItemStack[] getMainInventory()
    {
        return this.getInventoryPlayer().mainInventory;
    }

    protected void setMainInventory(ItemStack[] var1)
    {
        this.getInventoryPlayer().mainInventory = var1;
    }

    protected void setHasInventoryChanged(boolean var1)
    {
        this.getInventoryPlayer().inventoryChanged = var1;
    }

    protected void setHeldStack(ItemStack var1)
    {
        this.getInventoryPlayer().setItemStack(var1);
    }

    protected boolean hasInventoryChanged()
    {
        return this.getInventoryPlayer().inventoryChanged;
    }

    protected ItemStack getHeldStack()
    {
        return this.getInventoryPlayer().getItemStack();
    }

    protected ItemStack getFocusedStack()
    {
        return this.getInventoryPlayer().getCurrentItem();
    }

    protected int getFocusedSlot()
    {
        return this.getInventoryPlayer().currentItem;
    }

    protected int getWindowWidth(GuiScreen var1)
    {
        return var1.width;
    }

    protected int getWindowHeight(GuiScreen var1)
    {
        return var1.height;
    }

    protected int getGuiX(GuiContainer var1)
    {
        return var1.guiLeft;
    }

    protected int getGuiY(GuiContainer var1)
    {
        return var1.guiTop;
    }

    protected int getGuiWidth(GuiContainer var1)
    {
        return var1.xSize;
    }

    protected int getGuiHeight(GuiContainer var1)
    {
        return var1.ySize;
    }

    protected List getControlList(GuiScreen var1)
    {
        return var1.controlList;
    }

    protected void setControlList(GuiScreen var1, List var2)
    {
        var1.controlList = var2;
    }

    public GuiContainer asGuiContainer(GuiScreen var1)
    {
        return (GuiContainer)var1;
    }

    protected int getStringWidth(FontRenderer var1, String var2)
    {
        return var1.getStringWidth(var2);
    }

    protected void drawStringWithShadow(FontRenderer var1, String var2, int var3, int var4, int var5)
    {
        var1.drawStringWithShadow(var2, var3, var4, var5);
    }

    protected ItemStack createItemStack(int var1, int var2, int var3)
    {
        return new ItemStack(var1, var2, var3);
    }

    protected ItemStack copy(ItemStack var1)
    {
        return var1.copy();
    }

    protected int getItemDamage(ItemStack var1)
    {
        return var1.getItemDamage();
    }

    protected int getMaxStackSize(ItemStack var1)
    {
        return var1.getMaxStackSize();
    }

    protected boolean hasDataTags(ItemStack var1)
    {
        return var1.hasTagCompound();
    }

    protected int getStackSize(ItemStack var1)
    {
        return var1.stackSize;
    }

    protected int getItemID(ItemStack var1)
    {
        return var1.itemID;
    }

    protected boolean areItemStacksEqual(ItemStack var1, ItemStack var2)
    {
        return var1.isItemEqual(var2) && this.getStackSize(var1) == this.getStackSize(var2);
    }

    protected boolean isItemStackDamageable(ItemStack var1)
    {
        return var1.isItemStackDamageable();
    }

    protected boolean areSameItemType(ItemStack var1, ItemStack var2)
    {
        return this.areItemsEqual(var1, var2) || this.isItemStackDamageable(var1) && this.getItemID(var1) == this.getItemID(var2);
    }

    protected boolean areItemsEqual(ItemStack var1, ItemStack var2)
    {
        return var1.isItemEqual(var2);
    }

    protected int getAnimationsToGo(ItemStack var1)
    {
        return var1.animationsToGo;
    }

    protected Item getItem(ItemStack var1)
    {
        return var1.getItem();
    }

    protected boolean isDamageable(Item var1)
    {
        return var1.isDamageable();
    }

    protected int getMaxDamage(Item var1)
    {
        return var1.getMaxDamage();
    }

    protected int getArmorLevel(ItemArmor var1)
    {
        return var1.damageReduceAmount;
    }

    protected ItemArmor asItemArmor(Item var1)
    {
        return (ItemArmor)var1;
    }

    protected ItemStack clickInventory(PlayerControllerMP var1, int var2, int var3, int var4, boolean var5, EntityClientPlayerMP var6)
    {
        return var1.windowClick(var2, var3, var4, var5 ? 1 : 0, var6);
    }

    public int getWindowId(Container var1)
    {
        return var1.windowId;
    }

    public List getSlots(Container var1)
    {
        return var1.inventorySlots;
    }

    public Slot getSlot(Container var1, int var2)
    {
        return (Slot)((Slot)this.getSlots(var1).get(var2));
    }

    public ItemStack getSlotStack(Container var1, int var2)
    {
        Slot var3 = this.getSlot(var1, var2);
        return var3 == null ? null : this.getStack(var3);
    }

    protected void setSlotStack(Container var1, int var2, ItemStack var3)
    {
        var1.putStackInSlot(var2, var3);
    }

    protected boolean hasStack(Slot var1)
    {
        return var1.getHasStack();
    }

    protected int getSlotNumber(Slot var1)
    {
        try
        {
            if (var1 instanceof SlotCreativeInventory)
            {
                Slot var2 = (Slot)getThroughReflection(SlotCreativeInventory.class, "b", var1);

                if (var2 != null)
                {
                    return var2.slotNumber;
                }
            }
        }
        catch (Exception var3)
        {
            log.warning("Failed to access creative slot number");
        }

        return var1.slotNumber;
    }

    public ItemStack getStack(Slot var1)
    {
        return var1.getStack();
    }

    protected int getXDisplayPosition(Slot var1)
    {
        return var1.xDisplayPosition;
    }

    protected int getYDisplayPosition(Slot var1)
    {
        return var1.yDisplayPosition;
    }

    protected boolean areSlotAndStackCompatible(Slot var1, ItemStack var2)
    {
        return var1.isItemValid(var2);
    }

    public Container getContainer(GuiContainer var1)
    {
        return var1.inventorySlots;
    }

    protected GuiButton asGuiButton(Object var1)
    {
        return (GuiButton)var1;
    }

    protected void setEnabled(GuiButton var1, boolean var2)
    {
        var1.enabled = var2;
    }

    protected int getId(GuiButton var1)
    {
        return var1.id;
    }

    protected void setDisplayString(GuiButton var1, String var2)
    {
        var1.displayString = var2;
    }

    protected String getDisplayString(GuiButton var1)
    {
        return var1.displayString;
    }

    protected void playSound(String var1, float var2, float var3)
    {
        this.mc.sndManager.playSoundFX(var1, var2, var3);
    }

    protected long getCurrentTime()
    {
        return this.getTheWorld().getSeed();
    }

    protected int getKeyCode(KeyBinding var1)
    {
        return var1.keyCode;
    }

    protected int getSpecialChestRowSize(GuiContainer var1, int var2)
    {
        return this.mods.getSpecialChestRowSize(var1, var2);
    }

    protected boolean hasTexture(String var1)
    {
        TexturePackList var2 = (TexturePackList)getThroughReflection(RenderEngine.class, "k", this.mc.renderEngine);
        return var2 != null && var2.getSelectedTexturePack().getResourceAsStream(var1) != null;
    }

    public static StringTranslate getLocalizationService()
    {
        return StringTranslate.getInstance();
    }

    public static String getCurrentLanguage()
    {
        return getLocalizationService().getCurrentLanguage();
    }

    public static String getLocalizedString(String var0)
    {
        return getLocalizationService().translateKey(var0);
    }

    public static ItemStack getHoldStackStatic(Minecraft var0)
    {
        return (new InvTweaksObfuscation(var0)).getHeldStack();
    }

    public static GuiScreen getCurrentScreenStatic(Minecraft var0)
    {
        return (new InvTweaksObfuscation(var0)).getCurrentScreen();
    }

    protected boolean isValidChest(GuiScreen var1)
    {
        return var1 != null && (this.isGuiChest(var1) || this.isGuiDispenser(var1) || this.mods.isSpecialChest(var1));
    }

    protected boolean isValidInventory(GuiScreen var1)
    {
        return this.isStandardInventory(var1) || this.mods.isSpecialInventory(var1);
    }

    protected boolean isStandardInventory(GuiScreen var1)
    {
        return this.isGuiInventory(var1) || this.isGuiWorkbench(var1) || this.isGuiFurnace(var1) || this.isGuiBrewingStand(var1) || this.isGuiEnchantmentTable(var1) || this.isGuiTrading(var1) || this.isGuiAnvil(var1) || this.isGuiBeacon(var1) || this.isGuiInventoryCreative(var1) && this.getSlots(this.getContainer(this.asGuiContainer(var1))).size() == 46;
    }

    protected boolean isGuiContainer(Object var1)
    {
        return var1 != null && var1 instanceof GuiContainer;
    }

    protected boolean isGuiBeacon(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiBeacon.class);
    }

    protected boolean isGuiBrewingStand(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiBrewingStand.class);
    }

    protected boolean isGuiChest(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiChest.class);
    }

    protected boolean isGuiWorkbench(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiCrafting.class);
    }

    public boolean isGuiInventoryCreative(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiContainerCreative.class);
    }

    protected boolean isGuiEnchantmentTable(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiEnchantment.class);
    }

    protected boolean isGuiFurnace(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiFurnace.class);
    }

    protected boolean isGuiInventory(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiInventory.class);
    }

    protected boolean isGuiTrading(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiMerchant.class);
    }

    protected boolean isGuiAnvil(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiRepair.class);
    }

    protected boolean isGuiDispenser(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiDispenser.class);
    }

    protected boolean isGuiButton(Object var1)
    {
        return var1 != null && var1 instanceof GuiButton;
    }

    protected boolean isGuiEditSign(Object var1)
    {
        return var1 != null && var1.getClass().equals(GuiEditSign.class);
    }

    protected boolean isContainerBeacon(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerBeacon.class);
    }

    protected boolean isContainerBrewingStand(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerBrewingStand.class);
    }

    protected boolean isContainerChest(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerChest.class);
    }

    protected boolean isContainerWorkbench(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerWorkbench.class);
    }

    protected boolean isContainerEnchantmentTable(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerEnchantment.class);
    }

    protected boolean isContainerFurnace(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerFurnace.class);
    }

    protected boolean isContainerPlayer(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerPlayer.class);
    }

    protected boolean isContainerTrading(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerMerchant.class);
    }

    protected boolean isContainerAnvil(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerRepair.class);
    }

    protected boolean isContainerDispenser(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerDispenser.class);
    }

    protected boolean isContainerCreative(Object var1)
    {
        return var1 != null && var1.getClass().equals(ContainerCreative.class);
    }

    protected boolean isItemArmor(Object var1)
    {
        return var1 != null && var1 instanceof ItemArmor;
    }

    protected boolean isBasicSlot(Object var1)
    {
        return var1 != null && var1.getClass().equals(Slot.class);
    }

    protected static void makeFieldPublic(Class var0, String var1)
    {
        try
        {
            Field var2 = var0.getDeclaredField(var1);
            var2.setAccessible(true);
            Field var3 = Field.class.getDeclaredField("modifiers");
            var3.setAccessible(true);
            var3.setInt(var2, 1);
            fieldsMap.put(var0.getName() + var1, var2);
        }
        catch (Exception var4)
        {
            log.severe("Failed to make " + var0.getName() + "." + var1 + " accessible: " + var4.getMessage());
        }
    }

    protected static Object getThroughReflection(Class var0, String var1, Object var2)
    {
        try
        {
            return ((Field)fieldsMap.get(var0.getName() + var1)).get(var2);
        }
        catch (Exception var4)
        {
            return null;
        }
    }

    static
    {
        makeFieldPublic(SlotCreativeInventory.class, "b");
        makeFieldPublic(RenderEngine.class, "k");
    }
}
