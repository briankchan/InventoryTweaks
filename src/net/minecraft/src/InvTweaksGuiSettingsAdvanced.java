package net.minecraft.src;

import invtweaks.InvTweaksConst;
import java.awt.Desktop;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import org.lwjgl.util.Point;

import net.minecraft.src.*;

public class InvTweaksGuiSettingsAdvanced extends InvTweaksGuiSettingsAbstract
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private static final int ID_SORT_ON_PICKUP = 1;
    private static final int ID_AUTO_EQUIP_ARMOR = 2;
    private static final int ID_ENABLE_SOUNDS = 3;
    private static final int ID_CHESTS_BUTTONS = 4;
    private static final int ID_SLOW_SORTING = 5;
    private static final int ID_EDITSHORTCUTS = 100;
    private static String labelChestButtons;
    private static String labelSortOnPickup;
    private static String labelEquipArmor;
    private static String labelEnableSounds;
    private static String labelSlowSorting;

    public InvTweaksGuiSettingsAdvanced(Minecraft var1, GuiScreen var2, InvTweaksConfig var3)
    {
        super(var1, var2, var3);
        labelSortOnPickup = InvTweaksLocalization.get("invtweaks.settings.advanced.sortonpickup");
        labelEquipArmor = InvTweaksLocalization.get("invtweaks.settings.advanced.autoequip");
        labelEnableSounds = InvTweaksLocalization.get("invtweaks.settings.advanced.sounds");
        labelChestButtons = InvTweaksLocalization.get("invtweaks.settings.chestbuttons");
        labelSlowSorting = InvTweaksLocalization.get("invtweaks.settings.slowsorting");
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
        var1.add(new GuiButton(100, var2.getX() + 55, this.obf.getWindowHeight(this) / 6 + 144, InvTweaksLocalization.get("invtweaks.settings.advanced.mappingsfile")));
        int var11 = var3 + 2;
        this.moveToButtonCoords(var11++, var2);
        InvTweaksGuiTooltipButton var4 = new InvTweaksGuiTooltipButton(1, var2.getX(), var2.getY(), this.computeBooleanButtonLabel("enableSortingOnPickup", labelSortOnPickup), InvTweaksLocalization.get("invtweaks.settings.advanced.sortonpickup.tooltip"));
        var1.add(var4);
        this.moveToButtonCoords(var11++, var2);
        InvTweaksGuiTooltipButton var5 = new InvTweaksGuiTooltipButton(3, var2.getX(), var2.getY(), this.computeBooleanButtonLabel("enableSounds", labelEnableSounds), InvTweaksLocalization.get("invtweaks.settings.advanced.sounds.tooltip"));
        var1.add(var5);
        this.moveToButtonCoords(var11++, var2);
        var1.add(new InvTweaksGuiTooltipButton(4, var2.getX(), var2.getY(), this.computeBooleanButtonLabel("showChestButtons", labelChestButtons), InvTweaksLocalization.get("invtweaks.settings.chestbuttons.tooltip")));
        this.moveToButtonCoords(var11++, var2);
        InvTweaksGuiTooltipButton var6 = new InvTweaksGuiTooltipButton(2, var2.getX(), var2.getY(), this.computeBooleanButtonLabel("enableAutoEquipArmor", labelEquipArmor), InvTweaksLocalization.get("invtweaks.settings.advanced.autoequip.tooltip"));
        var1.add(var6);
        var11 += 3;
        this.moveToButtonCoords(var11++, var2);
        InvTweaksGuiTooltipButton var7 = new InvTweaksGuiTooltipButton(5, var2.getX(), var2.getY() + 10, this.computeBooleanButtonLabel("slowSorting", labelSlowSorting), (String)null);
        var1.add(var7);

        if (!Desktop.isDesktopSupported())
        {
            Iterator var8 = var1.iterator();

            while (var8.hasNext())
            {
                Object var9 = var8.next();

                if (this.obf.isGuiButton(var9))
                {
                    GuiButton var10 = this.obf.asGuiButton(var9);

                    if (this.obf.getId(var10) == 100)
                    {
                        this.obf.setEnabled(var10, false);
                    }
                }
            }
        }

        this.obf.setControlList(this, var1);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        super.drawScreen(var1, var2, var3);
        Point var4 = new Point();
        this.moveToButtonCoords(1, var4);
        this.drawString(this.obf.getFontRenderer(), InvTweaksLocalization.get("invtweaks.settings.pvpwarning.pt1"), var4.getX(), 40, 10066329);
        this.drawString(this.obf.getFontRenderer(), InvTweaksLocalization.get("invtweaks.settings.pvpwarning.pt2"), var4.getX(), 50, 10066329);
        this.drawString(this.obf.getFontRenderer(), InvTweaksLocalization.get("invtweaks.settings.slowsorting.pt1"), var4.getX(), 115, 10066329);
        this.drawString(this.obf.getFontRenderer(), InvTweaksLocalization.get("invtweaks.settings.slowsorting.pt2"), var4.getX(), 125, 10066329);
        this.drawString(this.obf.getFontRenderer(), InvTweaksLocalization.get("invtweaks.settings.slowsorting.pt3"), var4.getX(), 135, 10066329);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        switch (this.obf.getId(var1))
        {
            case 1:
                this.toggleBooleanButton(var1, "enableSortingOnPickup", labelSortOnPickup);
                break;

            case 2:
                this.toggleBooleanButton(var1, "enableAutoEquipArmor", labelEquipArmor);
                break;

            case 3:
                this.toggleBooleanButton(var1, "enableSounds", labelEnableSounds);
                break;

            case 4:
                this.toggleBooleanButton(var1, "showChestButtons", labelChestButtons);
                break;

            case 5:
                this.toggleBooleanButton(var1, "slowSorting", labelSlowSorting);
                break;

            case 100:
                try
                {
                    Desktop.getDesktop().open(new File(InvTweaksConst.CONFIG_PROPS_FILE));
                }
                catch (Exception var3)
                {
                    InvTweaks.logInGameErrorStatic("invtweaks.settings.advanced.mappingsfile.error", var3);
                }

                break;

            case 200:
                this.obf.displayGuiScreen(new InvTweaksGuiSettings(this.mc, this.parentScreen, this.config));
        }
    }
}
