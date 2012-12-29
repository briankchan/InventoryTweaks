package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

import net.minecraft.src.*;

public class InvTweaksContainerManager extends InvTweaksObfuscation
{
    public static final int DROP_SLOT = -999;
    public static final int INVENTORY_SIZE = 36;
    public static final int HOTBAR_SIZE = 9;
    public static final int ACTION_TIMEOUT = 500;
    public static final int POLLING_DELAY = 3;
    private GuiContainer guiContainer;
    private Container container;
    private Map slotRefs = new HashMap();
    private int clickDelay = 0;

    public InvTweaksContainerManager(Minecraft var1)
    {
        super(var1);
        GuiScreen var2 = this.getCurrentScreen();

        if (this.isGuiContainer(var2))
        {
            this.guiContainer = this.asGuiContainer(var2);
            this.container = this.getContainer(this.guiContainer);
        }
        else
        {
            this.container = this.getPlayerContainer();
        }

        List var3 = this.getSlots(this.container);
        int var4 = var3.size();
        boolean var5 = true;

        if (this.isContainerPlayer(this.container))
        {
            this.slotRefs.put(InvTweaksContainerSection.CRAFTING_OUT, var3.subList(0, 1));
            this.slotRefs.put(InvTweaksContainerSection.CRAFTING_IN, var3.subList(1, 5));
            this.slotRefs.put(InvTweaksContainerSection.ARMOR, var3.subList(5, 9));
        }
        else if (this.isContainerCreative(this.container))
        {
            this.slotRefs.put(InvTweaksContainerSection.ARMOR, var3.subList(5, 9));
            --var4;
        }
        else if (!this.isContainerChest(this.container) && !this.isContainerDispenser(this.container))
        {
            if (this.isContainerFurnace(this.container))
            {
                this.slotRefs.put(InvTweaksContainerSection.FURNACE_IN, var3.subList(0, 1));
                this.slotRefs.put(InvTweaksContainerSection.FURNACE_FUEL, var3.subList(1, 2));
                this.slotRefs.put(InvTweaksContainerSection.FURNACE_OUT, var3.subList(2, 3));
            }
            else if (this.isContainerWorkbench(this.container))
            {
                this.slotRefs.put(InvTweaksContainerSection.CRAFTING_OUT, var3.subList(0, 1));
                this.slotRefs.put(InvTweaksContainerSection.CRAFTING_IN, var3.subList(1, 10));
            }
            else if (this.isContainerEnchantmentTable(this.container))
            {
                this.slotRefs.put(InvTweaksContainerSection.ENCHANTMENT, var3.subList(0, 1));
            }
            else if (this.isContainerBrewingStand(this.container))
            {
                this.slotRefs.put(InvTweaksContainerSection.BREWING_BOTTLES, var3.subList(0, 3));
                this.slotRefs.put(InvTweaksContainerSection.BREWING_INGREDIENT, var3.subList(3, 4));
            }
            else
            {
                this.slotRefs = this.mods.getSpecialContainerSlots(var2, this.container);

                if (this.slotRefs.isEmpty())
                {
                    if (var4 >= 36)
                    {
                        this.slotRefs.put(InvTweaksContainerSection.CHEST, var3.subList(0, var4 - 36));
                    }
                    else
                    {
                        var5 = false;
                        this.slotRefs.put(InvTweaksContainerSection.CHEST, var3.subList(0, var4));
                    }
                }
            }
        }
        else
        {
            this.slotRefs.put(InvTweaksContainerSection.CHEST, var3.subList(0, var4 - 36));
        }

        if (var5 && !this.slotRefs.containsKey(InvTweaksContainerSection.INVENTORY))
        {
            this.slotRefs.put(InvTweaksContainerSection.INVENTORY, var3.subList(var4 - 36, var4));
            this.slotRefs.put(InvTweaksContainerSection.INVENTORY_NOT_HOTBAR, var3.subList(var4 - 36, var4 - 9));
            this.slotRefs.put(InvTweaksContainerSection.INVENTORY_HOTBAR, var3.subList(var4 - 9, var4));
        }
    }

    public boolean move(InvTweaksContainerSection var1, int var2, InvTweaksContainerSection var3, int var4)
    {
        ItemStack var5 = this.getItemStack(var1, var2);
        ItemStack var6 = this.getItemStack(var3, var4);

        if (var5 == null && var4 != -999)
        {
            return false;
        }
        else if (var1 == var3 && var2 == var4)
        {
            return true;
        }
        else
        {
            int var7;

            if (this.getHeldStack() != null)
            {
                var7 = this.getFirstEmptyIndex(InvTweaksContainerSection.INVENTORY);

                if (var7 == -1)
                {
                    return false;
                }

                this.leftClick(InvTweaksContainerSection.INVENTORY, var7);
            }

            if (var6 != null && this.getItemID(var5) == this.getItemID(var6) && (this.getMaxStackSize(var5) == 1 || this.hasDataTags(var5) || this.hasDataTags(var6)))
            {
                var7 = this.getFirstEmptyUsableSlotNumber();
                InvTweaksContainerSection var8 = this.getSlotSection(var7);
                int var9 = this.getSlotIndex(var7);

                if (var9 == -1)
                {
                    return false;
                }

                this.leftClick(var3, var4);
                this.leftClick(var8, var9);
                this.leftClick(var1, var2);
                this.leftClick(var3, var4);
                this.leftClick(var8, var9);
                this.leftClick(var1, var2);
            }
            else
            {
                this.leftClick(var1, var2);
                this.leftClick(var3, var4);

                if (this.getHeldStack() != null)
                {
                    this.leftClick(var1, var2);
                }
            }

            return true;
        }
    }

    public boolean moveSome(InvTweaksContainerSection var1, int var2, InvTweaksContainerSection var3, int var4, int var5)
    {
        ItemStack var6 = this.getItemStack(var1, var2);

        if (var6 != null && (var1 != var3 || var2 != var4))
        {
            ItemStack var7 = this.getItemStack(var1, var2);
            int var8 = this.getStackSize(var6);
            int var9 = Math.min(var5, var8);

            if (var6 != null && (var7 == null || this.areItemStacksEqual(var6, var7)))
            {
                this.leftClick(var1, var2);

                for (int var10 = 0; var10 < var9; ++var10)
                {
                    this.rightClick(var3, var4);
                }

                if (var9 < var8)
                {
                    this.leftClick(var1, var2);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public boolean drop(InvTweaksContainerSection var1, int var2)
    {
        return this.move(var1, var2, (InvTweaksContainerSection)null, -999);
    }

    public boolean dropSome(InvTweaksContainerSection var1, int var2, int var3)
    {
        return this.moveSome(var1, var2, (InvTweaksContainerSection)null, -999, var3);
    }

    public boolean putHoldItemDown(InvTweaksContainerSection var1, int var2)
    {
        ItemStack var3 = this.getHeldStack();

        if (var3 != null)
        {
            if (this.getItemStack(var1, var2) == null)
            {
                this.click(var1, var2, false);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public void leftClick(InvTweaksContainerSection var1, int var2)
    {
        this.click(var1, var2, false);
    }

    public void rightClick(InvTweaksContainerSection var1, int var2)
    {
        this.click(var1, var2, true);
    }

    public void click(InvTweaksContainerSection var1, int var2, boolean var3)
    {
        int var4 = this.indexToSlot(var1, var2);

        if (var4 != -1)
        {
            this.clickInventory(this.getPlayerController(), this.getWindowId(this.container), var4, var3 ? 1 : 0, false, this.getThePlayer());
        }

        if (this.clickDelay > 0)
        {
            try
            {
                Thread.sleep((long)this.clickDelay);
            }
            catch (InterruptedException var6)
            {
                var6.printStackTrace();
            }
        }
    }

    public Slot getSlotAtMousePosition()
    {
        if (this.guiContainer != null)
        {
            int var1 = this.getMouseX();
            int var2 = this.getMouseY();

            for (int var3 = 0; var3 < this.getSlots(this.getContainer(this.guiContainer)).size(); ++var3)
            {
                Slot var4 = (Slot)this.getSlots(this.getContainer(this.guiContainer)).get(var3);

                if (this.getIsMouseOverSlot(var4, var1, var2))
                {
                    return var4;
                }
            }

            return null;
        }
        else
        {
            return null;
        }
    }

    public boolean getIsMouseOverSlot(Slot var1)
    {
        return this.getIsMouseOverSlot(var1, this.getMouseX(), this.getMouseY());
    }

    private boolean getIsMouseOverSlot(Slot var1, int var2, int var3)
    {
        if (this.guiContainer == null)
        {
            return false;
        }
        else
        {
            int var4 = this.guiContainer.guiLeft;
            int var5 = this.guiContainer.guiTop;
            var2 -= var4;
            var3 -= var5;
            return var2 >= this.getXDisplayPosition(var1) - 1 && var2 < this.getXDisplayPosition(var1) + 16 + 1 && var3 >= this.getYDisplayPosition(var1) - 1 && var3 < this.getYDisplayPosition(var1) + 16 + 1;
        }
    }

    private int getMouseX()
    {
        return Mouse.getEventX() * this.getWindowWidth(this.guiContainer) / this.getDisplayWidth();
    }

    private int getMouseY()
    {
        return this.getWindowHeight(this.guiContainer) - Mouse.getEventY() * this.getWindowHeight(this.guiContainer) / this.getDisplayHeight() - 1;
    }

    public boolean hasSection(InvTweaksContainerSection var1)
    {
        return this.slotRefs.containsKey(var1);
    }

    public List getSlots(InvTweaksContainerSection var1)
    {
        return (List)this.slotRefs.get(var1);
    }

    public int getSize()
    {
        int var1 = 0;
        List var3;

        for (Iterator var2 = this.slotRefs.values().iterator(); var2.hasNext(); var1 += var3.size())
        {
            var3 = (List)var2.next();
        }

        return var1;
    }

    public int getSize(InvTweaksContainerSection var1)
    {
        return this.hasSection(var1) ? ((List)this.slotRefs.get(var1)).size() : 0;
    }

    public int getFirstEmptyIndex(InvTweaksContainerSection var1)
    {
        int var2 = 0;

        for (Iterator var3 = ((List)this.slotRefs.get(var1)).iterator(); var3.hasNext(); ++var2)
        {
            Slot var4 = (Slot)var3.next();

            if (!this.hasStack(var4))
            {
                return var2;
            }
        }

        return -1;
    }

    public boolean isSlotEmpty(InvTweaksContainerSection var1, int var2)
    {
        return this.hasSection(var1) ? this.getItemStack(var1, var2) == null : false;
    }

    public Slot getSlot(InvTweaksContainerSection var1, int var2)
    {
        List var3 = (List)this.slotRefs.get(var1);
        return var3 != null ? (Slot)var3.get(var2) : null;
    }

    public int getSlotIndex(int var1)
    {
        return this.getSlotIndex(var1, false);
    }

    public int getSlotIndex(int var1, boolean var2)
    {
        Iterator var3 = this.slotRefs.keySet().iterator();

        while (var3.hasNext())
        {
            InvTweaksContainerSection var4 = (InvTweaksContainerSection)var3.next();

            if (!var2 && var4 != InvTweaksContainerSection.INVENTORY || var2 && var4 != InvTweaksContainerSection.INVENTORY_NOT_HOTBAR && var4 != InvTweaksContainerSection.INVENTORY_HOTBAR)
            {
                int var5 = 0;

                for (Iterator var6 = ((List)this.slotRefs.get(var4)).iterator(); var6.hasNext(); ++var5)
                {
                    Slot var7 = (Slot)var6.next();

                    if (this.getSlotNumber(var7) == var1)
                    {
                        return var5;
                    }
                }
            }
        }

        return -1;
    }

    public InvTweaksContainerSection getSlotSection(int var1)
    {
        Iterator var2 = this.slotRefs.keySet().iterator();

        while (var2.hasNext())
        {
            InvTweaksContainerSection var3 = (InvTweaksContainerSection)var2.next();

            if (var3 != InvTweaksContainerSection.INVENTORY)
            {
                Iterator var4 = ((List)this.slotRefs.get(var3)).iterator();

                while (var4.hasNext())
                {
                    Slot var5 = (Slot)var4.next();

                    if (this.getSlotNumber(var5) == var1)
                    {
                        return var3;
                    }
                }
            }
        }

        return null;
    }

    public ItemStack getItemStack(InvTweaksContainerSection var1, int var2) throws NullPointerException, IndexOutOfBoundsException
    {
        int var3 = this.indexToSlot(var1, var2);
        return var3 >= 0 && var3 < this.getSlots(this.container).size() ? this.getSlotStack(this.container, var3) : null;
    }

    public Container getContainer()
    {
        return this.container;
    }

    private int getFirstEmptyUsableSlotNumber()
    {
        Iterator var1 = this.slotRefs.keySet().iterator();

        while (var1.hasNext())
        {
            InvTweaksContainerSection var2 = (InvTweaksContainerSection)var1.next();
            Iterator var3 = ((List)this.slotRefs.get(var2)).iterator();

            while (var3.hasNext())
            {
                Slot var4 = (Slot)var3.next();

                if (this.isBasicSlot(var4) && !this.hasStack(var4))
                {
                    return this.getSlotNumber(var4);
                }
            }
        }

        return -1;
    }

    private int indexToSlot(InvTweaksContainerSection var1, int var2)
    {
        if (var2 == -999)
        {
            return -999;
        }
        else if (this.hasSection(var1))
        {
            Slot var3 = (Slot)((List)this.slotRefs.get(var1)).get(var2);
            return var3 != null ? this.getSlotNumber(var3) : -1;
        }
        else
        {
            return -1;
        }
    }

    public void setClickDelay(int var1)
    {
        this.clickDelay = var1;
    }
}
