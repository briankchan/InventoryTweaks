package net.minecraft.src;

import java.util.concurrent.TimeoutException;
import net.minecraft.client.Minecraft;

class InvTweaksHandlerAutoRefill$1 implements Runnable
{
    private InvTweaksContainerSectionManager containerMgr;
    private int targetedSlot;
    private int i;
    private int expectedItemId;
    private boolean refillBeforeBreak;

    final InvTweaksHandlerAutoRefill this$0;

    InvTweaksHandlerAutoRefill$1(InvTweaksHandlerAutoRefill var1)
    {
        this.this$0 = var1;
    }

    public Runnable init(Minecraft var1, int var2, int var3, boolean var4) throws Exception
    {
        this.containerMgr = new InvTweaksContainerSectionManager(var1, InvTweaksContainerSection.INVENTORY);
        this.targetedSlot = var3;

        if (var2 != -1)
        {
            this.i = var2;
            this.expectedItemId = this.this$0.getItemID(this.containerMgr.getItemStack(var2));
        }
        else
        {
            this.i = this.containerMgr.getFirstEmptyIndex();
            this.expectedItemId = -1;
        }

        this.refillBeforeBreak = var4;
        return this;
    }

    public void run()
    {
        byte var1 = 0;
        this.this$0.setHasInventoryChanged(false);

        while (this.this$0.getThePlayer() != null && !this.this$0.hasInventoryChanged() && var1 < 1500)
        {
            InvTweaksHandlerAutoRefill.access$000(3);
        }

        if (this.this$0.getThePlayer() != null)
        {
            if (var1 < 200)
            {
                InvTweaksHandlerAutoRefill.access$000(200 - var1);
            }

            if (var1 >= 1500)
            {
                InvTweaksHandlerAutoRefill.access$100().warning("Autoreplace timout");
            }

            try
            {
                ItemStack var2 = this.containerMgr.getItemStack(this.i);

                if (var2 != null && this.this$0.getItemID(var2) == this.expectedItemId || this.refillBeforeBreak)
                {
                    if (!this.containerMgr.move(this.targetedSlot, this.i) && !this.containerMgr.move(this.i, this.targetedSlot))
                    {
                        InvTweaksHandlerAutoRefill.access$100().warning("Failed to move stack for autoreplace, despite of prior tests.");
                    }
                    else
                    {
                        if (!InvTweaksHandlerAutoRefill.access$200(this.this$0).getProperty("enableSounds").equals("false"))
                        {
                            this.this$0.playSound("mob.chickenplop", 1.4F, 0.5F);
                        }

                        if (this.containerMgr.getItemStack(this.i) != null && this.i >= 27)
                        {
                            for (int var3 = 0; var3 < 36; ++var3)
                            {
                                if (this.containerMgr.getItemStack(var3) == null)
                                {
                                    this.containerMgr.move(this.i, var3);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            catch (NullPointerException var4)
            {
                ;
            }
            catch (TimeoutException var5)
            {
                InvTweaksHandlerAutoRefill.access$100().severe("Failed to trigger autoreplace: " + var5.getMessage());
            }
        }
    }
}
