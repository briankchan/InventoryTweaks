package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.InvTweaksObfuscation;

public class InvTweaksGuiTooltipButton extends InvTweaksObfuscationGuiButton
{
    public static final int DEFAULT_BUTTON_WIDTH = 200;
    public static final int LINE_HEIGHT = 11;
    private int hoverTime;
    private long prevSystemTime;
    private String tooltip;
    private String[] tooltipLines;
    private int tooltipWidth;

    public InvTweaksGuiTooltipButton(int var1, int var2, int var3, String var4)
    {
        this(var1, var2, var3, 150, 20, var4, (String)null);
    }

    public InvTweaksGuiTooltipButton(int var1, int var2, int var3, String var4, String var5)
    {
        this(var1, var2, var3, 150, 20, var4, var5);
    }

    public InvTweaksGuiTooltipButton(int var1, int var2, int var3, int var4, int var5, String var6)
    {
        this(var1, var2, var3, var4, var5, var6, (String)null);
    }

    public InvTweaksGuiTooltipButton(int var1, int var2, int var3, int var4, int var5, String var6, String var7)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.hoverTime = 0;
        this.prevSystemTime = 0L;
        this.tooltip = null;
        this.tooltipLines = null;
        this.tooltipWidth = -1;

        if (var7 != null)
        {
            this.setTooltip(var7);
        }
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        super.drawButton(var1, var2, var3);
        InvTweaksObfuscation var4 = new InvTweaksObfuscation(var1);

        if (this.isEnabled2())
        {
            if (this.tooltipLines != null)
            {
                if (this.isMouseOverButton(var2, var3))
                {
                    long var5 = System.currentTimeMillis();

                    if (this.prevSystemTime != 0L)
                    {
                        this.hoverTime = (int)((long)this.hoverTime + (var5 - this.prevSystemTime));
                    }

                    this.prevSystemTime = var5;
                }
                else
                {
                    this.hoverTime = 0;
                    this.prevSystemTime = 0L;
                }

                if (this.hoverTime > 800 && this.tooltipLines != null)
                {
                    FontRenderer var13 = var4.getFontRenderer();
                    int var6 = var2 + 12;
                    int var7 = var3 - 11 * this.tooltipLines.length;
                    int var10;

                    if (this.tooltipWidth == -1)
                    {
                        String[] var8 = this.tooltipLines;
                        int var9 = var8.length;

                        for (var10 = 0; var10 < var9; ++var10)
                        {
                            String var11 = var8[var10];
                            this.tooltipWidth = Math.max(var4.getStringWidth(var13, var11), this.tooltipWidth);
                        }
                    }

                    if (var6 + this.tooltipWidth > var4.getWindowWidth(var4.getCurrentScreen()))
                    {
                        var6 = var4.getWindowWidth(var4.getCurrentScreen()) - this.tooltipWidth;
                    }

                    this.drawGradientRect(var6 - 3, var7 - 3, var6 + this.tooltipWidth + 3, var7 + 11 * this.tooltipLines.length, -1073741824, -1073741824);
                    int var14 = 0;
                    String[] var15 = this.tooltipLines;
                    var10 = var15.length;

                    for (int var16 = 0; var16 < var10; ++var16)
                    {
                        String var12 = var15[var16];
                        var4.drawStringWithShadow(var13, var12, var6, var7 + var14++ * 11, -1);
                    }
                }
            }
        }
    }

    protected boolean isMouseOverButton(int var1, int var2)
    {
        return var1 >= this.getXPosition() && var2 >= this.getYPosition() && var1 < this.getXPosition() + this.getWidth() && var2 < this.getYPosition() + this.getHeight();
    }

    protected int getTextColor(int var1, int var2)
    {
        int var3 = -2039584;

        if (!this.isEnabled())
        {
            var3 = -6250336;
        }
        else if (this.isMouseOverButton(var1, var2))
        {
            var3 = -96;
        }

        return var3;
    }

    public void setTooltip(String var1)
    {
        this.tooltip = var1;
        this.tooltipLines = var1.split("\n");
    }

    public String getTooltip()
    {
        return this.tooltip;
    }
}
