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
    
    public boolean isSpecialChest(GuiScreen guiScreen) {
        return is_GuiScreen(guiScreen, "GuiAlchChest") // Equivalent Exchange
            || is_GuiScreen(guiScreen, "GuiCondenser") // Equivalent Exchange
        	|| is_GuiScreen(guiScreen, "GUIChest") // Iron chests (formerly IC2)
                || is_GuiScreen(guiScreen, "GuiMultiPageChest") // Multi Page chest
                || is_GuiScreen(guiScreen, "GuiGoldSafe") // More Storage
                || is_GuiScreen(guiScreen, "GuiLocker")
                || is_GuiScreen(guiScreen, "GuiDualLocker")
                || is_GuiScreen(guiScreen, "GuiSafe") 
                || is_GuiScreen(guiScreen, "GuiCabinet") 
                || is_GuiScreen(guiScreen, "GuiTower")
                || is_GuiScreen(guiScreen, "GuiBufferChest") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiRetriever") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiItemDetect") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiAlloyFurnace") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiDeploy") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiSorter") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiFilter") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiAdvBench") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiEject") // Red Power 2
                || is_GuiScreen(guiScreen, "GuiPersonalChest")
                || is_GuiScreen(guiScreen, "GuiNuclearReactor") // IC2
                //|| is_GuiScreen(guiScreen, "GuiEnderChest") // EnderChest Old
                || is_GuiScreen(guiScreen, "GuiColorBox")
                || is_GuiScreen(guiScreen, "GuiLinkedColorBox") // ColorBox
                || is_GuiScreen(guiScreen, "FC_GuiChest") || is_GuiScreen(guiScreen, "FM_GuiMintStorage") // Metallurgy
                || is_GuiScreen(guiScreen, "GuiChestTFC") // TerraFirmaCraft
                
                //Added by Lordmau5
                || is_GuiScreen(guiScreen, "GuiBackpack") // Backpack Mod or Forestry Tier 1
                || is_GuiScreen(guiScreen, "GuiBag") // Red Power 2 Canvas Bag
                || is_GuiScreen(guiScreen, "GuiReinforcedChest") // Better Storage Mod
                || is_GuiScreen(guiScreen, "GuiEnderStorage") // Ender Storage / Ender Chest
                || is_GuiScreen(guiScreen, "GuiBackpackT2") || is_GuiScreen(guiScreen, "GuiForestry") // Forestry
          ;
    }

    public int getSpecialChestRowSize(GuiContainer var1, int var2)
    {
        if (!is_GuiScreen(var1, "GuiAlchChest") && !is_GuiScreen(var1, "GuiCondenser"))
        {
            if (is_GuiScreen(var1, "GUIChest"))
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
                if (is_GuiScreen(var1, "GuiMultiPageChest"))
                {
                    return 13;
                }

                if (is_GuiScreen(var1, "GuiLocker") || is_GuiScreen(var1, "GuiDualLocker") || is_GuiScreen(var1, "GuiTower"))
                {
                    return 8;
                }

                if (is_GuiScreen(var1, "GuiBufferChest"))
                {
                    return 4;
                }

                if (is_GuiScreen(var1, "GuiSorter"))
                {
                    return 8;
                }

                if (is_GuiScreen(var1, "GuiRetriever") || is_GuiScreen(var1, "GuiItemDetect") || is_GuiScreen(var1, "GuiAlloyFurnace") || is_GuiScreen(var1, "GuiDeploy") || is_GuiScreen(var1, "GuiFilter") || is_GuiScreen(var1, "GuiEject"))
                {
                    return 3;
                }

                if (is_GuiScreen(var1, "GuiNuclearReactor"))
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
        return is_GuiScreen(var1, "GuiAlchChest") || is_GuiScreen(var1, "GuiMultiPageChest") || is_GuiScreen(var1, "GUIChest") || is_GuiScreen(var1, "FC_GuiChest") || is_GuiScreen(var1, "GuiReinforcedChest") || is_GuiScreen(var1, "GuiBackpackT2");
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

        if (is_GuiScreen(var1, "GuiCondenser"))
        {
            var3.put(InvTweaksContainerSection.CHEST, var4.subList(1, var4.size() - 36));
        }
        else if (is_GuiScreen(var1, "GuiAdvBench"))
        {
            var3.put(InvTweaksContainerSection.CRAFTING_IN, var4.subList(0, 9));
            var3.put(InvTweaksContainerSection.CRAFTING_OUT, var4.subList(9, 10));
            var3.put(InvTweaksContainerSection.CHEST, var4.subList(10, 28));
        }

        return var3;
    }

    private static final boolean is_GuiScreen(GuiScreen var0, String var1)
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
