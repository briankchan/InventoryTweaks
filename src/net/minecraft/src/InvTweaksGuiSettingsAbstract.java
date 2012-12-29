package net.minecraft.src;

import java.util.List;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import org.lwjgl.util.Point;

import net.minecraft.src.*;

public abstract class InvTweaksGuiSettingsAbstract extends GuiScreen
{
    protected static final Logger log = Logger.getLogger("InvTweaks");
    protected static String ON;
    protected static String OFF;
    protected static String DISABLE_CI;
    protected Minecraft mc;
    protected InvTweaksObfuscation obf;
    protected InvTweaksConfig config;
    protected GuiScreen parentScreen;
    protected static String LABEL_DONE;
    protected static final int ID_DONE = 200;

    public InvTweaksGuiSettingsAbstract(Minecraft var1, GuiScreen var2, InvTweaksConfig var3)
    {
        LABEL_DONE = InvTweaksLocalization.get("invtweaks.settings.exit");
        ON = ": " + InvTweaksLocalization.get("invtweaks.settings.on");
        OFF = ": " + InvTweaksLocalization.get("invtweaks.settings.off");
        DISABLE_CI = ": " + InvTweaksLocalization.get("invtweaks.settings.disableci");
        this.mc = var1;
        this.obf = new InvTweaksObfuscation(var1);
        this.parentScreen = var2;
        this.config = var3;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        List var1 = this.obf.getControlList(this);
        Point var2 = new Point();
        this.moveToButtonCoords(1, var2);
        var1.add(new GuiButton(200, var2.getX() + 55, this.obf.getWindowHeight(this) / 6 + 168, LABEL_DONE));
        this.obf.setControlList(this, var1);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.obf.getFontRenderer(), InvTweaksLocalization.get("invtweaks.settings.title"), this.obf.getWindowWidth(this) / 2, 20, 16777215);
        super.drawScreen(var1, var2, var3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (this.obf.getId(var1) == 200)
        {
            this.obf.displayGuiScreen(this.parentScreen);
        }
    }

    protected void moveToButtonCoords(int var1, Point var2)
    {
        var2.setX(this.obf.getWindowWidth(this) / 2 - 155 + (var1 + 1) % 2 * 160);
        var2.setY(this.obf.getWindowHeight(this) / 6 + var1 / 2 * 24);
    }

    protected void toggleBooleanButton(GuiButton var1, String var2, String var3)
    {
        Boolean var4 = Boolean.valueOf(!(new Boolean(this.config.getProperty(var2))).booleanValue());
        this.config.setProperty(var2, var4.toString());
        this.obf.setDisplayString(var1, this.computeBooleanButtonLabel(var2, var3));
    }

    protected String computeBooleanButtonLabel(String var1, String var2)
    {
        String var3 = this.config.getProperty(var1);

        if (var3.equals("convenientInventoryCompatibility"))
        {
            return var2 + DISABLE_CI;
        }
        else
        {
            Boolean var4 = new Boolean(var3);
            return var2 + (var4.booleanValue() ? ON : OFF);
        }
    }
}
