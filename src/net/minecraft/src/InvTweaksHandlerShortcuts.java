package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.src.InvTweaksObfuscation;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InvTweaksHandlerShortcuts extends InvTweaksObfuscation
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private static final int DROP_SLOT = -999;
    private InvTweaksConfig config;
    private InvTweaksContainerManager container;
    private Map pressedKeys;
    private Map shortcuts;

    public InvTweaksHandlerShortcuts(Minecraft var1, InvTweaksConfig var2)
    {
        super(var1);
        this.config = var2;
        this.pressedKeys = new HashMap();
        this.shortcuts = new HashMap();
    }

    public void loadShortcuts()
    {
        this.pressedKeys.clear();
        this.shortcuts.clear();
        Map var1 = this.config.getProperties("shortcutKey");
        Iterator var2 = var1.keySet().iterator();
        int var7;
        int var8;

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            String[] var4 = ((String)var1.get(var3)).split("[ ]*,[ ]*");
            InvTweaksShortcutType var5 = InvTweaksShortcutType.fromConfigKey(var3);

            if (var5 != null)
            {
                String[] var6 = var4;
                var7 = var4.length;

                for (var8 = 0; var8 < var7; ++var8)
                {
                    String var9 = var6[var8];
                    String[] var10 = var9.split("\\+");
                    this.registerShortcutMapping(var5, new InvTweaksShortcutMapping(var10));
                }
            }
        }

        int var11 = this.getKeyBindingForwardKeyCode();
        int var12 = this.getKeyBindingBackKeyCode();
        this.registerShortcutMapping(InvTweaksShortcutType.MOVE_UP, new InvTweaksShortcutMapping(var11));
        this.registerShortcutMapping(InvTweaksShortcutType.MOVE_DOWN, new InvTweaksShortcutMapping(var12));
        int[] var13 = new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 79, 80, 81, 75, 76, 77, 71, 72, 73};
        int[] var14 = var13;
        int var15 = var13.length;

        for (var7 = 0; var7 < var15; ++var7)
        {
            var8 = var14[var7];
            this.registerShortcutMapping(InvTweaksShortcutType.MOVE_TO_SPECIFIC_HOTBAR_SLOT, new InvTweaksShortcutMapping(var8));
        }

        this.pressedKeys.put(Integer.valueOf(42), Boolean.valueOf(false));
        this.pressedKeys.put(Integer.valueOf(54), Boolean.valueOf(false));
    }

    private void registerShortcutMapping(InvTweaksShortcutType var1, InvTweaksShortcutMapping var2)
    {
        if (this.shortcuts.containsKey(var1))
        {
            ((List)this.shortcuts.get(var1)).add(var2);
        }
        else
        {
            LinkedList var3 = new LinkedList();
            var3.add(var2);
            this.shortcuts.put(var1, var3);
        }

        Iterator var5 = var2.getKeyCodes().iterator();

        while (var5.hasNext())
        {
            int var4 = ((Integer)var5.next()).intValue();
            this.pressedKeys.put(Integer.valueOf(var4), Boolean.valueOf(false));
        }
    }

    public void handleShortcut()
    {
        try
        {
            InvTweaksHandlerShortcuts$ShortcutConfig var1 = this.computeShortcutToTrigger();

            if (var1 != null)
            {
                int var2 = Mouse.getEventX();
                int var3 = Mouse.getEventY();
                this.runShortcut(var1);
                Mouse.destroy();
                Mouse.create();
                Mouse.setCursorPosition(var2, var3);
            }
        }
        catch (Exception var4)
        {
            InvTweaks.logInGameErrorStatic("invtweaks.shortcut.error", var4);
        }
    }

    public InvTweaksHandlerShortcuts$ShortcutConfig computeShortcutToTrigger()
    {
        this.updatePressedKeys();
        this.container = new InvTweaksContainerManager(this.mc);
        this.container.setClickDelay(this.config.getClickDelay());
        Slot var1 = this.container.getSlotAtMousePosition();
        InvTweaksHandlerShortcuts$ShortcutConfig var2 = new InvTweaksHandlerShortcuts$ShortcutConfig(this, (InvTweaksHandlerShortcuts$1)null);

        if (var1 != null && (this.hasStack(var1) || this.getHeldStack() != null))
        {
            int var3 = this.getSlotNumber(var1);
            var2.fromSection = this.container.getSlotSection(var3);
            var2.fromIndex = this.container.getSlotIndex(var3);
            var2.fromStack = this.hasStack(var1) ? this.copy(this.getStack(var1)) : this.copy(this.getHeldStack());

            if (this.isShortcutDown(InvTweaksShortcutType.MOVE_TO_SPECIFIC_HOTBAR_SLOT) != null)
            {
                var2.type = InvTweaksShortcutType.MOVE_TO_SPECIFIC_HOTBAR_SLOT;
            }
            else if (this.isShortcutDown(InvTweaksShortcutType.MOVE_ALL_ITEMS) != null)
            {
                var2.type = InvTweaksShortcutType.MOVE_ALL_ITEMS;
            }
            else if (this.isShortcutDown(InvTweaksShortcutType.MOVE_EVERYTHING) != null)
            {
                var2.type = InvTweaksShortcutType.MOVE_EVERYTHING;
            }
            else if (this.isShortcutDown(InvTweaksShortcutType.MOVE_ONE_ITEM) != null)
            {
                var2.type = InvTweaksShortcutType.MOVE_ONE_ITEM;
            }
            else if (var2.type == null && (this.isShortcutDown(InvTweaksShortcutType.MOVE_UP) != null || this.isShortcutDown(InvTweaksShortcutType.MOVE_DOWN) != null || this.isShortcutDown(InvTweaksShortcutType.DROP) != null))
            {
                var2.type = InvTweaksShortcutType.MOVE_ONE_STACK;
            }

            if (var2.fromSection == InvTweaksContainerSection.CRAFTING_OUT && var2.type == InvTweaksShortcutType.MOVE_ONE_ITEM)
            {
                var2.type = InvTweaksShortcutType.MOVE_ONE_STACK;
            }

            if (var2.fromSection != null && var2.fromIndex != -1 && var2.type != null)
            {
                if (var2.type == InvTweaksShortcutType.MOVE_TO_SPECIFIC_HOTBAR_SLOT)
                {
                    var2.toSection = InvTweaksContainerSection.INVENTORY_HOTBAR;
                    InvTweaksShortcutMapping var4 = this.isShortcutDown(InvTweaksShortcutType.MOVE_TO_SPECIFIC_HOTBAR_SLOT);

                    if (var4 != null && !var4.getKeyCodes().isEmpty())
                    {
                        String var5 = Keyboard.getKeyName(((Integer)var4.getKeyCodes().get(0)).intValue());
                        var2.toIndex = -1 + Integer.parseInt(var5.replace("NUMPAD", ""));
                    }
                }
                else
                {
                    Vector var9 = new Vector();

                    if (this.container.hasSection(InvTweaksContainerSection.CHEST))
                    {
                        var9.add(InvTweaksContainerSection.CHEST);
                    }
                    else if (this.container.hasSection(InvTweaksContainerSection.CRAFTING_IN))
                    {
                        var9.add(InvTweaksContainerSection.CRAFTING_IN);
                    }
                    else if (this.container.hasSection(InvTweaksContainerSection.FURNACE_IN))
                    {
                        var9.add(InvTweaksContainerSection.FURNACE_IN);
                    }
                    else if (this.container.hasSection(InvTweaksContainerSection.BREWING_INGREDIENT))
                    {
                        ItemStack var10 = this.container.getStack(var1);

                        if (var10 != null)
                        {
                            if (this.getItemID(var10) == 373)
                            {
                                var9.add(InvTweaksContainerSection.BREWING_BOTTLES);
                            }
                            else
                            {
                                var9.add(InvTweaksContainerSection.BREWING_INGREDIENT);
                            }
                        }
                    }
                    else if (this.container.hasSection(InvTweaksContainerSection.ENCHANTMENT))
                    {
                        var9.add(InvTweaksContainerSection.ENCHANTMENT);
                    }

                    var9.add(InvTweaksContainerSection.INVENTORY_NOT_HOTBAR);
                    var9.add(InvTweaksContainerSection.INVENTORY_HOTBAR);
                    boolean var11 = this.isShortcutDown(InvTweaksShortcutType.MOVE_UP) != null;
                    boolean var6 = this.isShortcutDown(InvTweaksShortcutType.MOVE_DOWN) != null;

                    if (!var11 && !var6)
                    {
                        switch (InvTweaksHandlerShortcuts$1.$SwitchMap$InvTweaksContainerSection[var2.fromSection.ordinal()])
                        {
                            case 1:
                                var2.toSection = InvTweaksContainerSection.INVENTORY;
                                break;

                            case 2:
                                if (var9.contains(InvTweaksContainerSection.CHEST))
                                {
                                    var2.toSection = InvTweaksContainerSection.CHEST;
                                }
                                else
                                {
                                    var2.toSection = InvTweaksContainerSection.INVENTORY_NOT_HOTBAR;
                                }

                                break;

                            case 3:
                            case 4:
                                var2.toSection = InvTweaksContainerSection.INVENTORY_NOT_HOTBAR;
                                break;

                            default:
                                if (var9.contains(InvTweaksContainerSection.CHEST))
                                {
                                    var2.toSection = InvTweaksContainerSection.CHEST;
                                }
                                else
                                {
                                    var2.toSection = InvTweaksContainerSection.INVENTORY_HOTBAR;
                                }
                        }
                    }
                    else
                    {
                        int var7 = 0;

                        if (var11)
                        {
                            --var7;
                        }

                        if (var6)
                        {
                            ++var7;
                        }

                        int var8 = var9.indexOf(var2.fromSection);

                        if (var8 != -1)
                        {
                            var2.toSection = (InvTweaksContainerSection)var9.get((var9.size() + var8 + var7) % var9.size());
                        }
                        else
                        {
                            var2.toSection = InvTweaksContainerSection.INVENTORY;
                        }
                    }
                }

                var2.forceEmptySlot = Mouse.isButtonDown(1);
                var2.drop = this.isShortcutDown(InvTweaksShortcutType.DROP) != null;
                return var2;
            }
        }

        return null;
    }

    public void updatePressedKeys()
    {
        if (this.haveControlsChanged())
        {
            this.loadShortcuts();
        }

        Iterator var1 = this.pressedKeys.keySet().iterator();

        while (var1.hasNext())
        {
            int var2 = ((Integer)var1.next()).intValue();

            if (var2 > 0 && Keyboard.isKeyDown(var2))
            {
                if (!((Boolean)this.pressedKeys.get(Integer.valueOf(var2))).booleanValue())
                {
                    this.pressedKeys.put(Integer.valueOf(var2), Boolean.valueOf(true));
                }
            }
            else
            {
                this.pressedKeys.put(Integer.valueOf(var2), Boolean.valueOf(false));
            }
        }
    }

    private boolean haveControlsChanged()
    {
        return !this.pressedKeys.containsKey(Integer.valueOf(this.getKeyBindingForwardKeyCode())) || !this.pressedKeys.containsKey(Integer.valueOf(this.getKeyBindingBackKeyCode()));
    }

    private void runShortcut(InvTweaksHandlerShortcuts$ShortcutConfig var1) throws TimeoutException
    {
        int var3;

        if (this.getHeldStack() != null)
        {
            Slot var2 = this.container.getSlotAtMousePosition();

            if (var2 == null)
            {
                return;
            }

            var3 = this.getSlotNumber(var2);
            this.container.putHoldItemDown(this.container.getSlotSection(var3), this.container.getSlotIndex(var3));

            if (this.getHeldStack() != null)
            {
                return;
            }
        }

        synchronized (this)
        {
            if (var1.type == InvTweaksShortcutType.MOVE_TO_SPECIFIC_HOTBAR_SLOT)
            {
                this.container.move(var1.fromSection, var1.fromIndex, var1.toSection, var1.toIndex);
            }
            else
            {
                var3 = this.getNextTargetIndex(var1);

                if (var3 != -1)
                {
                    switch (InvTweaksHandlerShortcuts$1.$SwitchMap$InvTweaksShortcutType[var1.type.ordinal()])
                    {
                        case 1:
                            Slot var6 = this.container.getSlot(var1.fromSection, var1.fromIndex);

                            if (var1.fromSection != InvTweaksContainerSection.CRAFTING_OUT && var1.toSection != InvTweaksContainerSection.ENCHANTMENT)
                            {
                                while (this.hasStack(var6) && var3 != -1)
                                {
                                    boolean var4 = this.container.move(var1.fromSection, var1.fromIndex, var1.toSection, var3);
                                    int var5 = this.getNextTargetIndex(var1);
                                    var3 = !var4 && !var1.drop && var5 == var3 ? -1 : var5;
                                }

                                return;
                            }
                            else
                            {
                                this.container.move(var1.fromSection, var1.fromIndex, var1.toSection, var3);
                                break;
                            }

                        case 2:
                            this.container.moveSome(var1.fromSection, var1.fromIndex, var1.toSection, var3, 1);
                            break;

                        case 3:
                            this.moveAll(var1, var1.fromStack);

                            if (var1.fromSection == InvTweaksContainerSection.INVENTORY_NOT_HOTBAR && var1.toSection == InvTweaksContainerSection.CHEST)
                            {
                                var1.fromSection = InvTweaksContainerSection.INVENTORY_HOTBAR;
                                this.moveAll(var1, var1.fromStack);
                            }

                            break;

                        case 4:
                            this.moveAll(var1, (ItemStack)null);

                            if (var1.fromSection == InvTweaksContainerSection.INVENTORY_HOTBAR && var1.toSection == InvTweaksContainerSection.CHEST)
                            {
                                var1.fromSection = InvTweaksContainerSection.INVENTORY_HOTBAR;
                                this.moveAll(var1, (ItemStack)null);
                            }
                    }
                }
            }
        }
    }

    private void moveAll(InvTweaksHandlerShortcuts$ShortcutConfig var1, ItemStack var2) throws TimeoutException
    {
        int var3 = this.getNextTargetIndex(var1);
        Iterator var6 = this.container.getSlots(var1.fromSection).iterator();

        while (var6.hasNext())
        {
            Slot var7 = (Slot)var6.next();
            int var4;
            boolean var5;

            if (this.hasStack(var7) && (var2 == null || this.areSameItemType(var2, this.getStack(var7))))
            {
                for (int var8 = this.container.getSlotIndex(this.getSlotNumber(var7)); this.hasStack(var7) && var3 != -1 && (var1.fromSection != var1.toSection || var8 != var3); var3 = !var5 && !var1.drop && var4 == var3 ? -1 : var4)
                {
                    var5 = this.container.move(var1.fromSection, var8, var1.toSection, var3);
                    var4 = this.getNextTargetIndex(var1);
                }
            }

            if (var3 == -1)
            {
                break;
            }
        }
    }

    private int getNextTargetIndex(InvTweaksHandlerShortcuts$ShortcutConfig var1)
    {
        if (var1.drop)
        {
            return -999;
        }
        else
        {
            int var2 = -1;

            if (!var1.forceEmptySlot)
            {
                int var3 = 0;

                for (Iterator var4 = this.container.getSlots(var1.toSection).iterator(); var4.hasNext(); ++var3)
                {
                    Slot var5 = (Slot)var4.next();

                    if (this.hasStack(var5))
                    {
                        ItemStack var6 = this.getStack(var5);

                        if (!this.hasDataTags(var6) && this.areItemsEqual(var6, var1.fromStack) && this.getStackSize(var6) < this.getMaxStackSize(var6))
                        {
                            var2 = var3;
                            break;
                        }
                    }
                }
            }

            if (var2 == -1)
            {
                var2 = this.container.getFirstEmptyIndex(var1.toSection);
            }

            if (var2 == -1 && var1.toSection == InvTweaksContainerSection.FURNACE_IN)
            {
                var1.toSection = InvTweaksContainerSection.FURNACE_FUEL;
                var2 = this.container.getFirstEmptyIndex(var1.toSection);
            }

            return var2;
        }
    }

    public InvTweaksShortcutMapping isShortcutDown(InvTweaksShortcutType var1)
    {
        List var2 = (List)this.shortcuts.get(var1);

        if (var2 != null)
        {
            Iterator var3 = var2.iterator();

            while (var3.hasNext())
            {
                InvTweaksShortcutMapping var4 = (InvTweaksShortcutMapping)var3.next();

                if (var4.isTriggered(this.pressedKeys))
                {
                    return var4;
                }
            }
        }

        return null;
    }
}
