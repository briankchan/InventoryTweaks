package net.minecraft.src;

import java.util.List;
import java.util.concurrent.TimeoutException;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Container;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class InvTweaksContainerSectionManager
{
    private InvTweaksContainerManager containerMgr;
    private InvTweaksContainerSection section;

    public InvTweaksContainerSectionManager(Minecraft var1, InvTweaksContainerSection var2) throws Exception
    {
        this(new InvTweaksContainerManager(var1), var2);
    }

    public void setClickDelay(int var1)
    {
        this.containerMgr.setClickDelay(var1);
    }

    public InvTweaksContainerSectionManager(InvTweaksContainerManager var1, InvTweaksContainerSection var2) throws Exception
    {
        this.containerMgr = var1;
        this.section = var2;

        if (!this.containerMgr.hasSection(var2))
        {
            throw new Exception("Section not available");
        }
    }

    public boolean move(int var1, int var2) throws TimeoutException
    {
        return this.containerMgr.move(this.section, var1, this.section, var2);
    }

    public boolean moveSome(int var1, int var2, int var3) throws TimeoutException
    {
        return this.containerMgr.moveSome(this.section, var1, this.section, var2, var3);
    }

    public boolean drop(int var1) throws TimeoutException
    {
        return this.containerMgr.drop(this.section, var1);
    }

    public boolean dropSome(int var1, int var2) throws TimeoutException
    {
        return this.containerMgr.dropSome(this.section, var1, var2);
    }

    public boolean putHoldItemDown(int var1) throws TimeoutException
    {
        return this.containerMgr.putHoldItemDown(this.section, var1);
    }

    public void leftClick(int var1) throws TimeoutException
    {
        this.containerMgr.leftClick(this.section, var1);
    }

    public void rightClick(int var1) throws TimeoutException
    {
        this.containerMgr.rightClick(this.section, var1);
    }

    public void click(int var1, boolean var2) throws TimeoutException
    {
        this.containerMgr.click(this.section, var1, var2);
    }

    public List getSlots()
    {
        return this.containerMgr.getSlots(this.section);
    }

    public int getSize()
    {
        return this.containerMgr.getSize(this.section);
    }

    public int getFirstEmptyIndex()
    {
        return this.containerMgr.getFirstEmptyIndex(this.section);
    }

    public boolean isSlotEmpty(int var1)
    {
        return this.containerMgr.isSlotEmpty(this.section, var1);
    }

    public Slot getSlot(int var1)
    {
        return this.containerMgr.getSlot(this.section, var1);
    }

    public int getSlotIndex(int var1)
    {
        return this.isSlotInSection(var1) ? this.containerMgr.getSlotIndex(var1) : -1;
    }

    public boolean isSlotInSection(int var1)
    {
        return this.containerMgr.getSlotSection(var1) == this.section;
    }

    public ItemStack getItemStack(int var1) throws NullPointerException, IndexOutOfBoundsException
    {
        return this.containerMgr.getItemStack(this.section, var1);
    }

    public Container getContainer()
    {
        return this.containerMgr.getContainer();
    }
}
