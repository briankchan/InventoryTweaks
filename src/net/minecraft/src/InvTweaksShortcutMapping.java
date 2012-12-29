package net.minecraft.src;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;

public class InvTweaksShortcutMapping
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private List keysToHold;

    public InvTweaksShortcutMapping(int var1)
    {
        this.keysToHold = new LinkedList();
        this.keysToHold.add(Integer.valueOf(var1));
    }

    public InvTweaksShortcutMapping(int ... var1)
    {
        this.keysToHold = new LinkedList();
        int[] var2 = var1;
        int var3 = var1.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            int var5 = var2[var4];
            this.keysToHold.add(Integer.valueOf(var5));
        }
    }

    public InvTweaksShortcutMapping(String var1)
    {
        this(new String[] {var1});
    }

    public InvTweaksShortcutMapping(String ... var1)
    {
        this.keysToHold = new LinkedList();
        String[] var2 = var1;
        int var3 = var1.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            String var5 = var2[var4];
            var5 = var5.trim().replace("KEY_", "").replace("ALT", "MENU");
            this.keysToHold.add(Integer.valueOf(Keyboard.getKeyIndex(var5)));
        }
    }

    public boolean isTriggered(Map var1)
    {
        Iterator var2 = this.keysToHold.iterator();
        Integer var3;
        label26:

        do
        {
            do
            {
                if (!var2.hasNext())
                {
                    return true;
                }

                var3 = (Integer)var2.next();

                if (var3.intValue() == 29)
                {
                    continue label26;
                }
            }
            while (((Boolean)var1.get(var3)).booleanValue());

            return false;
        }
        while (((Boolean)var1.get(var3)).booleanValue() && !Keyboard.isKeyDown(184));

        return false;
    }

    public List getKeyCodes()
    {
        return this.keysToHold;
    }
}
