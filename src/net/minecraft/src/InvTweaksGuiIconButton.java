package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class InvTweaksGuiIconButton extends InvTweaksGuiTooltipButton
{
    protected InvTweaksConfigManager cfgManager;
    private boolean useCustomTexture;

    public InvTweaksGuiIconButton(InvTweaksConfigManager var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, boolean var9)
    {
        super(var2, var3, var4, var5, var6, var7, var8);
        this.cfgManager = var1;
        this.useCustomTexture = var9;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        super.drawButton(var1, var2, var3);

        if (this.isEnabled2())
        {
            int var4 = this.getHoverState(this.isMouseOverButton(var2, var3));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.useCustomTexture)
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getTexture(var1, "/gui/button10px.png"));
                this.drawTexturedModalRect(this.getXPosition(), this.getYPosition(), (var4 - 1) * 10, 0, this.getWidth(), this.getHeight());
            }
            else
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getTexture(var1, "/gui/gui.png"));
                this.drawTexturedModalRect(this.getXPosition(), this.getYPosition(), 1, 46 + var4 * 20 + 1, this.getWidth() / 2, this.getHeight() / 2);
                this.drawTexturedModalRect(this.getXPosition(), this.getYPosition() + this.getHeight() / 2, 1, 46 + var4 * 20 + 20 - this.getHeight() / 2 - 1, this.getWidth() / 2, this.getHeight() / 2);
                this.drawTexturedModalRect(this.getXPosition() + this.getWidth() / 2, this.getYPosition(), 200 - this.getWidth() / 2 - 1, 46 + var4 * 20 + 1, this.getWidth() / 2, this.getHeight() / 2);
                this.drawTexturedModalRect(this.getXPosition() + this.getWidth() / 2, this.getYPosition() + this.getHeight() / 2, 200 - this.getWidth() / 2 - 1, 46 + var4 * 20 + 19 - this.getHeight() / 2, this.getWidth() / 2, this.getHeight() / 2);
            }
        }
    }
}
