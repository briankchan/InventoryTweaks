package net.minecraft.src;

import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;

import net.minecraft.src.*;

public class InvTweaksGuiSettingsButton extends InvTweaksGuiIconButton
{
    private static final Logger log = Logger.getLogger("InvTweaks");

    public InvTweaksGuiSettingsButton(InvTweaksConfigManager var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, boolean var9)
    {
        super(var1, var2, var3, var4, var5, var6, var7, var8, var9);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        super.drawButton(var1, var2, var3);

        if (this.isEnabled2())
        {
            InvTweaksObfuscation var4 = new InvTweaksObfuscation(var1);
            this.drawCenteredString(var4.getFontRenderer(), this.getDisplayString(), this.getXPosition() + 5, this.getYPosition() - 1, this.getTextColor(var2, var3));
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        InvTweaksObfuscation var4 = new InvTweaksObfuscation(var1);
        InvTweaksConfig var5 = this.cfgManager.getConfig();

        if (!super.mousePressed(var1, var2, var3))
        {
            return false;
        }
        else
        {
            try
            {
                InvTweaksContainerSectionManager var6 = new InvTweaksContainerSectionManager(var1, InvTweaksContainerSection.INVENTORY);
                var6.setClickDelay(var5.getClickDelay());

                if (var4.getHeldStack() != null)
                {
                    try
                    {
                        for (int var7 = var6.getSize() - 1; var7 >= 0; --var7)
                        {
                            if (var6.getItemStack(var7) == null)
                            {
                                var6.leftClick(var7);
                                break;
                            }
                        }
                    }
                    catch (TimeoutException var8)
                    {
                        InvTweaks.logInGameErrorStatic("invtweaks.sort.releaseitem.error", var8);
                    }
                }
            }
            catch (Exception var9)
            {
                log.severe(var9.getMessage());
            }

            this.cfgManager.makeSureConfigurationIsLoaded();
            var4.displayGuiScreen(new InvTweaksGuiSettings(var1, var4.getCurrentScreen(), var5));
            return true;
        }
    }
}
