package net.minecraft.src;

import invtweaks.InvTweaksItemTree;
import invtweaks.InvTweaksItemTreeItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;

public class InvTweaksHandlerAutoRefill extends InvTweaksObfuscation
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private InvTweaksConfig config = null;

    public InvTweaksHandlerAutoRefill(Minecraft var1, InvTweaksConfig var2)
    {
        super(var1);
        this.setConfig(var2);
    }

    public void setConfig(InvTweaksConfig var1)
    {
        this.config = var1;
    }

    public void autoRefillSlot(int var1, int var2, int var3) throws Exception
    {
        InvTweaksContainerSectionManager var4 = new InvTweaksContainerSectionManager(this.mc, InvTweaksContainerSection.INVENTORY);
        var4.setClickDelay(this.config.getClickDelay());
        ItemStack var6 = null;
        int var7 = -1;
        boolean var8 = this.config.getProperty("autoRefillBeforeBreak").equals("true");
        ArrayList var9 = new ArrayList();
        Vector var10 = this.config.getRules();
        InvTweaksItemTree var11 = this.config.getTree();
        ItemStack var5;

        if (!var11.isItemUnknown(var2, var3))
        {
            List var12 = var11.getItems(var2, var3);
            Iterator var13 = var12.iterator();

            while (var13.hasNext())
            {
                InvTweaksItemTreeItem var14 = (InvTweaksItemTreeItem)var13.next();
                var9.add(new InvTweaksConfigSortingRule(var11, "D" + (var1 - 27), var14.getName(), 36, 9));
            }

            var13 = var10.iterator();
            InvTweaksConfigSortingRule var21;

            while (var13.hasNext())
            {
                var21 = (InvTweaksConfigSortingRule)var13.next();

                if (var21.getType() == InvTweaksConfigSortingRuleType.SLOT || var21.getType() == InvTweaksConfigSortingRuleType.COLUMN)
                {
                    int[] var15 = var21.getPreferredSlots();
                    int var16 = var15.length;

                    for (int var17 = 0; var17 < var16; ++var17)
                    {
                        int var18 = var15[var17];

                        if (var1 == var18)
                        {
                            var9.add(var21);
                            break;
                        }
                    }
                }
            }

            var13 = var9.iterator();

            while (var13.hasNext())
            {
                var21 = (InvTweaksConfigSortingRule)var13.next();

                for (int var20 = 0; var20 < 36; ++var20)
                {
                    var5 = var4.getItemStack(var20);

                    if (var5 != null)
                    {
                        List var22 = var11.getItems(this.getItemID(var5), this.getItemDamage(var5));

                        if (var11.matches(var22, var21.getKeyword()))
                        {
                            if (this.getMaxStackSize(var5) == 1)
                            {
                                if ((var6 == null || this.getItemDamage(var5) > this.getItemDamage(var6)) && (!var8 || this.getMaxDamage(this.getItem(var5)) - this.getItemDamage(var5) > 5))
                                {
                                    var6 = var5;
                                    var7 = var20;
                                }
                            }
                            else if (var6 == null || this.getStackSize(var5) < this.getStackSize(var6))
                            {
                                var6 = var5;
                                var7 = var20;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            for (int var19 = 0; var19 < 36; ++var19)
            {
                var5 = var4.getItemStack(var19);

                if (var5 != null && this.getItemID(var5) == var2 && this.getItemDamage(var5) == var3)
                {
                    var6 = var5;
                    var7 = var19;
                    break;
                }
            }
        }

        if (var6 != null || var8 && this.getStack(var4.getSlot(var1)) != null)
        {
            log.info("Automatic stack replacement.");
            (new Thread((new InvTweaksHandlerAutoRefill$1(this)).init(this.mc, var7, var1, var8))).start();
        }
    }

    private static void trySleep(int var0)
    {
        try
        {
            Thread.sleep((long)var0);
        }
        catch (InterruptedException var2)
        {
            ;
        }
    }

    static void access$000(int var0)
    {
        trySleep(var0);
    }

    static Logger access$100()
    {
        return log;
    }

    static InvTweaksConfig access$200(InvTweaksHandlerAutoRefill var0)
    {
        return var0.config;
    }
}
