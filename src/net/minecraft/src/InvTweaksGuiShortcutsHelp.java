package net.minecraft.src;

import java.util.LinkedList;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import net.minecraft.src.*;

public class InvTweaksGuiShortcutsHelp extends GuiScreen
{
    private static final int ID_DONE = 0;
    private InvTweaksObfuscation obf;
    private GuiScreen parentScreen;
    private InvTweaksConfig config;

    public InvTweaksGuiShortcutsHelp(Minecraft var1, GuiScreen var2, InvTweaksConfig var3)
    {
        this.obf = new InvTweaksObfuscation(var1);
        this.parentScreen = var2;
        this.config = var3;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        LinkedList var1 = new LinkedList();
        var1.add(new GuiButton(0, this.obf.getWindowWidth(this) / 2 - 100, this.obf.getWindowHeight(this) / 6 + 168, "Done"));
        this.obf.setControlList(this, var1);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.obf.getFontRenderer(), "WARNING: Since 1.3.1, shortcuts won\'t work as expected. Looking for a workaround...", this.obf.getWindowWidth(this) / 2, 5, 16711680);
        this.drawCenteredString(this.obf.getFontRenderer(), InvTweaksLocalization.get("invtweaks.help.shortcuts.title"), this.obf.getWindowWidth(this) / 2, 20, 16777215);
        String var4 = InvTweaksLocalization.get("invtweaks.help.shortcuts.click");
        int var5 = this.obf.getWindowHeight(this) / 6 - 2;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.onestack"), "LSHIFT " + InvTweaksLocalization.get("invtweaks.help.shortcuts.or") + " RSHIFT + " + var4, 16776960, var5);
        var5 += 12;
        this.drawShortcutLine("", this.buildUpOrDownLabel("shortcutKeyToUpperSection", this.obf.getKeyBindingForwardKeyCode(), InvTweaksLocalization.get("invtweaks.help.shortcuts.forward")) + " + " + var4, 16776960, var5);
        var5 += 12;
        this.drawShortcutLine("", this.buildUpOrDownLabel("shortcutKeyToLowerSection", this.obf.getKeyBindingBackKeyCode(), InvTweaksLocalization.get("invtweaks.help.shortcuts.backwards")) + " + " + var4, 16776960, var5);
        var5 += 12;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.oneitem"), this.config.getProperty("shortcutKeyOneItem") + " + " + var4, 16776960, var5);
        var5 += 12;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.allitems"), this.config.getProperty("shortcutKeyAllItems") + " + " + var4, 16776960, var5);
        var5 += 12;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.everything"), this.config.getProperty("shortcutKeyEverything") + " + " + var4, 16776960, var5);
        var5 += 19;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.hotbar"), "0-9 + " + var4, 65331, var5);
        var5 += 12;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.emptyslot"), InvTweaksLocalization.get("invtweaks.help.shortcuts.rightclick"), 65331, var5);
        var5 += 12;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.drop"), this.config.getProperty("shortcutKeyDrop") + " + " + var4, 65331, var5);
        var5 += 19;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.craftall"), "LSHIFT, RSHIFT + " + var4, 16746496, var5);
        var5 += 12;
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.craftone"), this.config.getProperty("shortcutKeyOneItem") + " + " + var4, 16746496, var5);
        var5 += 19;
        String var6 = this.getKeyName(this.config.getSortKeyCode(), "(Sort Key)");
        this.drawShortcutLine(InvTweaksLocalization.get("invtweaks.help.shortcuts.selectconfig"), "0-9 + " + var6, 8978431, var5);
        super.drawScreen(var1, var2, var3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        switch (this.obf.getId(var1))
        {
            case 0:
                this.obf.displayGuiScreen(this.parentScreen);

            default:
        }
    }

    private String buildUpOrDownLabel(String var1, int var2, String var3)
    {
        String var4 = this.config.getProperty(var1);
        String var5 = this.getKeyName(var2, var3);
        return var5.equals(var4) ? var5 : var5 + "/" + var4;
    }

    protected String getKeyName(int var1, String var2)
    {
        try
        {
            return Keyboard.getKeyName(var1);
        }
        catch (Exception var4)
        {
            return var2;
        }
    }

    private void drawShortcutLine(String var1, String var2, int var3, int var4)
    {
        this.drawString(this.obf.getFontRenderer(), var1, 30, var4, -1);

        if (var2 != null)
        {
            this.drawString(this.obf.getFontRenderer(), var2.contains("DEFAULT") ? "-" : var2.replaceAll(", ", " " + InvTweaksLocalization.get("invtweaks.help.shortcuts.or") + " "), this.obf.getWindowWidth(this) / 2 - 30, var4, var3);
        }
    }
}
