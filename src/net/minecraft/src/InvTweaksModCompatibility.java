package net.minecraft.src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.InvTweaksObfuscation;

public class InvTweaksModCompatibility
{
    private InvTweaksObfuscation obf;

    public InvTweaksModCompatibility(InvTweaksObfuscation var1)
    {
        this.obf = var1;
    }

    public boolean isSpecialChest(GuiScreen var1)
    {
        return is(var1, "GuiAlchChest") || is(var1, "GuiCondenser") || is(var1, "GUIChest") || is(var1, "GuiMultiPageChest") || is(var1, "GuiGoldSafe") || is(var1, "GuiLocker") || is(var1, "GuiDualLocker") || is(var1, "GuiSafe") || is(var1, "GuiCabinet") || is(var1, "GuiTower") || is(var1, "GuiBufferChest") || is(var1, "GuiRetriever") || is(var1, "GuiItemDetect") || is(var1, "GuiAlloyFurnace") || is(var1, "GuiDeploy") || is(var1, "GuiSorter") || is(var1, "GuiFilter") || is(var1, "GuiAdvBench") || is(var1, "GuiEject") || is(var1, "GuiPersonalChest") || is(var1, "GuiNuclearReactor") || is(var1, "GuiEnderChest") || is(var1, "GuiColorBox") || is(var1, "GuiLinkedColorBox") || is(var1, "FC_GuiChest") || is(var1, "FM_GuiMintStorage") || is(var1, "GuiChestTFC") || is(var1, "GuiBackpack") || is(var1, "GuiBag");
    }

    public int getSpecialChestRowSize(GuiContainer var1, int var2)
    {
        if (!is(var1, "GuiAlchChest") && !is(var1, "GuiCondenser"))
        {
            if (is(var1, "GUIChest"))
            {
                try
                {
                    return ((Integer)var1.getClass().getMethod("getRowLength", new Class[0]).invoke(var1, new Object[0])).intValue();
                }
                catch (Exception var4)
                {
                    ;
                }
            }
            else
            {
                if (is(var1, "GuiMultiPageChest"))
                {
                    return 13;
                }

                if (is(var1, "GuiLocker") || is(var1, "GuiDualLocker") || is(var1, "GuiTower"))
                {
                    return 8;
                }

                if (is(var1, "GuiBufferChest"))
                {
                    return 4;
                }

                if (is(var1, "GuiSorter"))
                {
                    return 8;
                }

                if (is(var1, "GuiRetriever") || is(var1, "GuiItemDetect") || is(var1, "GuiAlloyFurnace") || is(var1, "GuiDeploy") || is(var1, "GuiFilter") || is(var1, "GuiEject"))
                {
                    return 3;
                }

                if (is(var1, "GuiNuclearReactor"))
                {
                    return (this.obf.getSlots(this.obf.getContainer(var1)).size() - 36) / 6;
                }
            }

            return var2;
        }
        else
        {
            return 13;
        }
    }

    public boolean isChestWayTooBig(GuiScreen var1)
    {
        return is(var1, "GuiAlchChest") || is(var1, "GuiMultiPageChest") || is(var1, "GUIChest") || is(var1, "FC_GuiChest");
    }

    public boolean isSpecialInventory(GuiScreen var1)
    {
        try
        {
            return this.obf.getSlots(this.obf.getContainer(this.obf.asGuiContainer(var1))).size() > 36 && !this.obf.isGuiInventoryCreative(var1);
        }
        catch (Exception var3)
        {
            return false;
        }
    }

    public Map getSpecialContainerSlots(GuiScreen var1, Container var2)
    {
        HashMap var3 = new HashMap();
        List var4 = this.obf.getSlots(var2);

        if (is(var1, "GuiCondenser"))
        {
            var3.put(InvTweaksContainerSection.CHEST, var4.subList(1, var4.size() - 36));
        }
        else if (is(var1, "GuiAdvBench"))
        {
            var3.put(InvTweaksContainerSection.CRAFTING_IN, var4.subList(0, 9));
            var3.put(InvTweaksContainerSection.CRAFTING_OUT, var4.subList(9, 10));
            var3.put(InvTweaksContainerSection.CHEST, var4.subList(10, 28));
        }

        return var3;
    }

    private static final boolean is(GuiScreen var0, String var1)
    {
        try
        {
            return var0.getClass().getSimpleName().contains(var1);
        }
        catch (Exception var3)
        {
            return false;
        }
    }
}
