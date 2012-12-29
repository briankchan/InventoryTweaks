package net.minecraft.src;

import invtweaks.InvTweaksConst;
import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;

public class InvTweaksGuiSettings extends InvTweaksGuiSettingsAbstract
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private static final int ID_MIDDLE_CLICK = 1;
    private static final int ID_BEFORE_BREAK = 2;
    private static final int ID_SHORTCUTS = 3;
    private static final int ID_SHORTCUTS_HELP = 4;
    private static final int ID_AUTO_REFILL = 5;
    private static final int ID_MORE_OPTIONS = 6;
    private static final int ID_SORTING_KEY = 7;
    private static final int ID_EDITRULES = 100;
    private static final int ID_EDITTREE = 101;
    private static final int ID_HELP = 102;
    private static String labelMiddleClick;
    private static String labelShortcuts;
    private static String labelAutoRefill;
    private static String labelAutoRefillBeforeBreak;
    private static String labelMoreOptions;
    private InvTweaksGuiTooltipButton sortMappingButton;
    private boolean sortMappingEdition = false;

    public InvTweaksGuiSettings(Minecraft var1, GuiScreen var2, InvTweaksConfig var3)
    {
        super(var1, var2, var3);
        labelMiddleClick = InvTweaksLocalization.get("invtweaks.settings.middleclick");
        labelShortcuts = InvTweaksLocalization.get("invtweaks.settings.shortcuts");
        labelAutoRefill = InvTweaksLocalization.get("invtweaks.settings.autorefill");
        labelAutoRefillBeforeBreak = InvTweaksLocalization.get("invtweaks.settings.beforebreak");
        labelMoreOptions = InvTweaksLocalization.get("invtweaks.settings.moreoptions");
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        List var1 = this.obf.getControlList(this);
        Point var2 = new Point();
        byte var3 = 0;
        this.moveToButtonCoords(1, var2);
        var1.add(new GuiButton(100, var2.getX() + 55, this.obf.getWindowHeight(this) / 6 + 96, InvTweaksLocalization.get("invtweaks.settings.rulesfile")));
        var1.add(new GuiButton(101, var2.getX() + 55, this.obf.getWindowHeight(this) / 6 + 120, InvTweaksLocalization.get("invtweaks.settings.treefile")));
        var1.add(new GuiButton(102, var2.getX() + 55, this.obf.getWindowHeight(this) / 6 + 144, InvTweaksLocalization.get("invtweaks.settings.onlinehelp")));
        int var14 = var3 + 1;
        this.moveToButtonCoords(var3, var2);
        var1.add(new InvTweaksGuiTooltipButton(4, var2.getX() + 130, var2.getY(), 20, 20, "?", "Shortcuts help"));
        String var4 = this.config.getProperty("enableShortcuts");
        InvTweaksGuiTooltipButton var5 = new InvTweaksGuiTooltipButton(3, var2.getX(), var2.getY(), 130, 20, this.computeBooleanButtonLabel("enableShortcuts", labelShortcuts), InvTweaksLocalization.get("invtweaks.settings.shortcuts.tooltip"));
        var1.add(var5);

        if (var4.equals("convenientInventoryCompatibility"))
        {
            this.obf.setEnabled(var5, false);
            var5.setTooltip(var5.getTooltip() + "\n(" + InvTweaksLocalization.get("invtweaks.settings.disableci.tooltip") + ")");
        }

        this.moveToButtonCoords(var14++, var2);
        this.sortMappingButton = new InvTweaksGuiTooltipButton(7, var2.getX(), var2.getY(), InvTweaksLocalization.get("invtweaks.settings.key") + " " + this.config.getProperty("keySortInventory"));
        var1.add(this.sortMappingButton);
        this.moveToButtonCoords(var14++, var2);
        InvTweaksGuiTooltipButton var6 = new InvTweaksGuiTooltipButton(2, var2.getX(), var2.getY(), this.computeBooleanButtonLabel("autoRefillBeforeBreak", labelAutoRefillBeforeBreak), InvTweaksLocalization.get("invtweaks.settings.beforebreak.tooltip"));
        var1.add(var6);
        this.moveToButtonCoords(var14++, var2);
        InvTweaksGuiTooltipButton var7 = new InvTweaksGuiTooltipButton(5, var2.getX(), var2.getY(), this.computeBooleanButtonLabel("enableAutoRefill", labelAutoRefill), InvTweaksLocalization.get("invtweaks.settings.autorefill.tooltip"));
        var1.add(var7);
        this.moveToButtonCoords(var14++, var2);
        var1.add(new InvTweaksGuiTooltipButton(6, var2.getX(), var2.getY(), labelMoreOptions, InvTweaksLocalization.get("invtweaks.settings.moreoptions.tooltip")));
        String var8 = this.config.getProperty("enableMiddleClick");
        this.moveToButtonCoords(var14++, var2);
        InvTweaksGuiTooltipButton var9 = new InvTweaksGuiTooltipButton(1, var2.getX(), var2.getY(), this.computeBooleanButtonLabel("enableMiddleClick", labelMiddleClick), InvTweaksLocalization.get("invtweaks.settings.middleclick.tooltip"));
        var1.add(var9);

        if (var8.equals("convenientInventoryCompatibility"))
        {
            this.obf.setEnabled(var9, false);
            var9.setTooltip(var9.getTooltip() + "\n(" + InvTweaksLocalization.get("invtweaks.settings.disableci.tooltip"));
        }

        if (!Desktop.isDesktopSupported())
        {
            Iterator var10 = var1.iterator();

            while (var10.hasNext())
            {
                Object var11 = var10.next();

                if (this.obf.isGuiButton(var11))
                {
                    GuiButton var12 = this.obf.asGuiButton(var11);

                    if (this.obf.getId(var12) >= 100 && this.obf.getId(var12) <= 102)
                    {
                        this.obf.setEnabled(var12, false);
                    }
                }
            }
        }

        this.obf.setControlList(this, var1);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        super.actionPerformed(var1);

        switch (this.obf.getId(var1))
        {
            case 1:
                this.toggleBooleanButton(var1, "enableMiddleClick", labelMiddleClick);
                break;

            case 2:
                this.toggleBooleanButton(var1, "autoRefillBeforeBreak", labelAutoRefillBeforeBreak);
                break;

            case 3:
                this.toggleBooleanButton(var1, "enableShortcuts", labelShortcuts);
                break;

            case 4:
                this.obf.displayGuiScreen(new InvTweaksGuiShortcutsHelp(this.mc, this, this.config));
                break;

            case 5:
                this.toggleBooleanButton(var1, "enableAutoRefill", labelAutoRefill);
                break;

            case 6:
                this.obf.displayGuiScreen(new InvTweaksGuiSettingsAdvanced(this.mc, this.parentScreen, this.config));
                break;

            case 7:
                this.sortMappingButton.displayString = InvTweaksLocalization.get("invtweaks.settings.key") + " > ??? <";
                this.sortMappingEdition = true;
                break;

            case 100:
                try
                {
                    Desktop.getDesktop().open(new File(InvTweaksConst.CONFIG_RULES_FILE));
                }
                catch (Exception var5)
                {
                    InvTweaks.logInGameErrorStatic("invtweaks.settings.rulesfile.error", var5);
                }

                break;

            case 101:
                try
                {
                    Desktop.getDesktop().open(new File(InvTweaksConst.CONFIG_TREE_FILE));
                }
                catch (Exception var4)
                {
                    InvTweaks.logInGameErrorStatic("invtweaks.settings.treefile.error", var4);
                }

                break;

            case 102:
                try
                {
                    Desktop.getDesktop().browse((new URL("http://modding.kalam-alami.net/invtweaks")).toURI());
                }
                catch (Exception var3)
                {
                    InvTweaks.logInGameErrorStatic("invtweaks.settings.onlinehelp.error", var3);
                }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        if (this.sortMappingEdition)
        {
            String var3 = Keyboard.getKeyName(var2);
            this.config.setProperty("keySortInventory", var3);
            this.sortMappingButton.displayString = InvTweaksLocalization.get("invtweaks.settings.key") + " " + var3;
        }
    }
}
