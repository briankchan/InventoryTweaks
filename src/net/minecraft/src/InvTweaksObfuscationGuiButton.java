package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

public class InvTweaksObfuscationGuiButton extends GuiButton
{
    public InvTweaksObfuscationGuiButton(int var1, int var2, int var3, int var4, int var5, String var6)
    {
        super(var1, var2, var3, var4, var5, var6);
    }

    /*protected void drawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6)
    {
        this.drawGradientRect(var1, var2, var3, var4, var5, var6);
    }

    protected void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6)
    {
        this.drawTexturedModalRect(var1, var2, var3, var4, var5, var6);
    }

    protected void drawRect(int var1, int var2, int var3, int var4, int var5)
    {
        drawRect(var1, var2, var3, var4, var5);
    }*/

    protected String getDisplayString()
    {
        return this.displayString;
    }

    protected int getTexture(Minecraft var1, String var2)
    {
        return var1.renderEngine.getTexture(var2);
    }

    /*protected int getHoverState(boolean var1)
    {
        return this.;
    }*/

    protected boolean isEnabled2()
    {
        return this.drawButton;
    }

    protected boolean isEnabled()
    {
        return this.enabled;
    }

    protected int getXPosition()
    {
        return this.xPosition;
    }

    protected int getYPosition()
    {
        return this.yPosition;
    }

    protected int getWidth()
    {
        return this.width;
    }

    protected int getHeight()
    {
        return this.height;
    }
}
