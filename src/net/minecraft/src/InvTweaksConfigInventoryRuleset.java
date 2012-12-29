package net.minecraft.src;

import invtweaks.InvTweaksItemTree;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

public class InvTweaksConfigInventoryRuleset
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private String name;
    private int[] lockPriorities;
    private boolean[] frozenSlots;
    private Vector lockedSlots;
    private Vector rules;
    private Vector autoReplaceRules;
    private boolean debugEnabled;
    private InvTweaksItemTree tree;

    public InvTweaksConfigInventoryRuleset(InvTweaksItemTree var1, String var2)
    {
        this.tree = var1;
        this.name = var2.trim();
        this.lockPriorities = new int[36];
        int var3;

        for (var3 = 0; var3 < this.lockPriorities.length; ++var3)
        {
            this.lockPriorities[var3] = 0;
        }

        this.frozenSlots = new boolean[36];

        for (var3 = 0; var3 < this.frozenSlots.length; ++var3)
        {
            this.frozenSlots[var3] = false;
        }

        this.lockedSlots = new Vector();
        this.rules = new Vector();
        this.autoReplaceRules = new Vector();
        this.debugEnabled = false;
    }

    public String registerLine(String var1) throws InvalidParameterException
    {
        InvTweaksConfigSortingRule var2 = null;
        String var3 = var1.replaceAll("[\\s]+", " ").toLowerCase();
        String[] var4 = var3.split(" ");

        if (var4.length == 2)
        {
            if (var3.matches("^([a-d]|[1-9]|[r]){1,2} [\\w]*$") || var3.matches("^[a-d][1-9]-[a-d][1-9][rv]?[rv]? [\\w]*$"))
            {
                var4[0] = var4[0].toLowerCase();
                var4[1] = var4[1];
                int[] var11;
                int var17;
                int var16;

                if (var4[1].equals("locked"))
                {
                    var11 = InvTweaksConfigSortingRule.getRulePreferredPositions(var4[0], 36, 9);
                    int var15 = InvTweaksConfigSortingRule.getRuleType(var4[0], 9).getLowestPriority() - 1;
                    int[] var14 = var11;
                    var16 = var11.length;

                    for (var17 = 0; var17 < var16; ++var17)
                    {
                        int var10 = var14[var17];
                        this.lockPriorities[var10] = var15;
                    }

                    return null;
                }

                if (var4[1].equals("frozen"))
                {
                    var11 = InvTweaksConfigSortingRule.getRulePreferredPositions(var4[0], 36, 9);
                    int[] var13 = var11;
                    int var12 = var11.length;

                    for (var16 = 0; var16 < var12; ++var16)
                    {
                        var17 = var13[var16];
                        this.frozenSlots[var17] = true;
                    }

                    return null;
                }

                String var5 = var4[1].toLowerCase();
                boolean var6 = this.tree.isKeywordValid(var5);

                if (!var6)
                {
                    if (var5.matches("^[0-9-]*$"))
                    {
                        var6 = true;
                    }
                    else
                    {
                        Vector var7 = this.getKeywordVariants(var5);
                        Iterator var8 = var7.iterator();

                        while (var8.hasNext())
                        {
                            String var9 = (String)var8.next();

                            if (this.tree.isKeywordValid(var9.toLowerCase()))
                            {
                                var6 = true;
                                var5 = var9;
                                break;
                            }
                        }
                    }
                }

                if (var6)
                {
                    var2 = new InvTweaksConfigSortingRule(this.tree, var4[0], var5.toLowerCase(), 36, 9);
                    this.rules.add(var2);
                    return null;
                }

                return var5.toLowerCase();
            }

            if (var4[0].equals("autorefill") || var4[0].equals("autoreplace"))
            {
                var4[1] = var4[1].toLowerCase();

                if (this.tree.isKeywordValid(var4[1]) || var4[1].equals("nothing"))
                {
                    this.autoReplaceRules.add(var4[1]);
                }

                return null;
            }
        }
        else if (var4.length == 1 && var4[0].equals("debug"))
        {
            this.debugEnabled = true;
            return null;
        }

        throw new InvalidParameterException();
    }

    public void finalize()
    {
        if (this.autoReplaceRules.isEmpty())
        {
            try
            {
                this.autoReplaceRules.add(this.tree.getRootCategory().getName());
            }
            catch (NullPointerException var2)
            {
                throw new NullPointerException("No root category is defined.");
            }
        }

        Collections.sort(this.rules, Collections.reverseOrder());

        for (int var1 = 0; var1 < this.lockPriorities.length; ++var1)
        {
            if (this.lockPriorities[var1] > 0)
            {
                this.lockedSlots.add(Integer.valueOf(var1));
            }
        }
    }

    public String getName()
    {
        return this.name;
    }

    public int[] getLockPriorities()
    {
        return this.lockPriorities;
    }

    public boolean[] getFrozenSlots()
    {
        return this.frozenSlots;
    }

    public Vector getLockedSlots()
    {
        return this.lockedSlots;
    }

    public Vector getRules()
    {
        return this.rules;
    }

    public Vector getAutoReplaceRules()
    {
        return this.autoReplaceRules;
    }

    public boolean isDebugEnabled()
    {
        return this.debugEnabled;
    }

    private Vector getKeywordVariants(String var1)
    {
        Vector var2 = new Vector();

        if (var1.endsWith("es"))
        {
            var2.add(var1.substring(0, var1.length() - 2));
        }

        if (var1.endsWith("s"))
        {
            var2.add(var1.substring(0, var1.length() - 1));
        }

        if (var1.contains("en"))
        {
            var2.add(var1.replaceAll("en", ""));
        }
        else
        {
            if (var1.contains("wood"))
            {
                var2.add(var1.replaceAll("wood", "wooden"));
            }

            if (var1.contains("gold"))
            {
                var2.add(var1.replaceAll("gold", "golden"));
            }
        }

        if (var1.matches("\\w*[A-Z]\\w*"))
        {
            byte[] var3 = var1.getBytes();

            for (int var4 = 0; var4 < var3.length; ++var4)
            {
                if (var3[var4] >= 65 && var3[var4] <= 90)
                {
                    String var5 = (var1.substring(var4) + var1.substring(0, var4)).toLowerCase();
                    var2.add(var5);
                    var2.addAll(this.getKeywordVariants(var5));
                }
            }
        }

        return var2;
    }
}
