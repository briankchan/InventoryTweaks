package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class InvTweaksGuiSortingButton extends InvTweaksGuiIconButton
{
    private final InvTweaksContainerSection section;
    private int algorithm;
    private int rowSize;

    public InvTweaksGuiSortingButton(InvTweaksConfigManager var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8, int var9, int var10, boolean var11)
    {
        super(var1, var2, var3, var4, var5, var6, var7, var8, var11);
        this.section = InvTweaksContainerSection.CHEST;
        this.algorithm = var9;
        this.rowSize = var10;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        super.drawButton(var1, var2, var3);

        if (this.isEnabled2())
        {
            int var4 = this.getTextColor(var2, var3);

            if (this.getDisplayString().equals("h"))
            {
                this.drawRect(this.getXPosition() + 3, this.getYPosition() + 3, this.getXPosition() + this.getWidth() - 3, this.getYPosition() + 4, var4);
                this.drawRect(this.getXPosition() + 3, this.getYPosition() + 6, this.getXPosition() + this.getWidth() - 3, this.getYPosition() + 7, var4);
            }
            else if (this.getDisplayString().equals("v"))
            {
                this.drawRect(this.getXPosition() + 3, this.getYPosition() + 3, this.getXPosition() + 4, this.getYPosition() + this.getHeight() - 3, var4);
                this.drawRect(this.getXPosition() + 6, this.getYPosition() + 3, this.getXPosition() + 7, this.getYPosition() + this.getHeight() - 3, var4);
            }
            else
            {
                this.drawRect(this.getXPosition() + 3, this.getYPosition() + 3, this.getXPosition() + this.getWidth() - 3, this.getYPosition() + 4, var4);
                this.drawRect(this.getXPosition() + 5, this.getYPosition() + 4, this.getXPosition() + 6, this.getYPosition() + 5, var4);
                this.drawRect(this.getXPosition() + 4, this.getYPosition() + 5, this.getXPosition() + 5, this.getYPosition() + 6, var4);
                this.drawRect(this.getXPosition() + 3, this.getYPosition() + 6, this.getXPosition() + this.getWidth() - 3, this.getYPosition() + 7, var4);
            }
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        if (super.mousePressed(var1, var2, var3))
        {
            try
            {
                (new InvTweaksHandlerSorting(var1, this.cfgManager.getConfig(), this.section, this.algorithm, this.rowSize)).sort();
            }
            catch (Exception var5)
            {
                InvTweaks.logInGameErrorStatic("invtweaks.sort.chest.error", var5);
                var5.printStackTrace();
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
