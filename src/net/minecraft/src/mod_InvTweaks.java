package net.minecraft.src;

import java.util.logging.Logger;
import net.minecraft.client.Minecraft;

public class mod_InvTweaks extends BaseMod
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private InvTweaks instance;
    private InvTweaksObfuscation obf;

    public String getName()
    {
        return "Inventory Tweaks";
    }

    public String getVersion()
    {
        return "1.47 (1.4.6)";
    }

    public void load()
    {
        Minecraft var1 = ModLoader.getMinecraftInstance();
        this.obf = new InvTweaksObfuscation(var1);
        ModLoader.setInGameHook(this, true, true);
        this.instance = new InvTweaks(var1);
    }

    public boolean onTickInGame(float var1, Minecraft var2)
    {
        if (this.obf.getCurrentScreen() != null)
        {
            this.instance.onTickInGUI(this.obf.getCurrentScreen());
        }
        else
        {
            this.instance.onTickInGame();
        }

        return true;
    }

    public void onItemPickup(EntityPlayer var1, ItemStack var2)
    {
        this.instance.setItemPickupPending(true);
    }
}
