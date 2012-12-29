package net.minecraft.src;

import invtweaks.InvTweaksItemTree;
import java.awt.Point;
import java.util.logging.Logger;

public class InvTweaksConfigSortingRule implements Comparable
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private String constraint;
    private int[] preferredPositions;
    private String keyword;
    private InvTweaksConfigSortingRuleType type;
    private int priority;
    private int containerSize;
    private int containerRowSize;

    public InvTweaksConfigSortingRule(InvTweaksItemTree var1, String var2, String var3, int var4, int var5)
    {
        this.keyword = var3;
        this.constraint = var2;
        this.containerSize = var4;
        this.containerRowSize = var5;
        this.type = getRuleType(var2, var5);
        this.preferredPositions = this.getRulePreferredPositions(var2);
        this.priority = this.type.getLowestPriority() + 100000 + var1.getKeywordDepth(var3) * 1000 - var1.getKeywordOrder(var3);
    }

    public InvTweaksConfigSortingRuleType getType()
    {
        return this.type;
    }

    public int[] getPreferredSlots()
    {
        return this.preferredPositions;
    }

    public String getKeyword()
    {
        return this.keyword;
    }

    public String getRawConstraint()
    {
        return this.constraint;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public int compareTo(InvTweaksConfigSortingRule var1)
    {
        return this.getPriority() - var1.getPriority();
    }

    public int[] getRulePreferredPositions(String var1)
    {
        return getRulePreferredPositions(var1, this.containerSize, this.containerRowSize);
    }

    public static int[] getRulePreferredPositions(String var0, int var1, int var2)
    {
        int[] var3 = null;
        int var4 = var1 / var2;

        if (var0.length() >= 5)
        {
            boolean var5 = false;

            if (var0.contains("v"))
            {
                var5 = true;
                var0 = var0.replaceAll("v", "");
            }

            String[] var6 = var0.split("-");

            if (var6.length == 2)
            {
                int[] var7 = getRulePreferredPositions(var6[0], var1, var2);
                int[] var8 = getRulePreferredPositions(var6[1], var1, var2);

                if (var7.length == 1 && var8.length == 1)
                {
                    int var9 = var7[0];
                    int var10 = var8[0];
                    Point var11 = new Point(var9 % var2, var9 / var2);
                    Point var12 = new Point(var10 % var2, var10 / var2);
                    var3 = new int[(Math.abs(var12.y - var11.y) + 1) * (Math.abs(var12.x - var11.x) + 1)];
                    int var13 = 0;
                    int var15;

                    if (var5)
                    {
                        Point[] var14 = new Point[] {var11, var12};
                        var15 = var14.length;

                        for (int var16 = 0; var16 < var15; ++var16)
                        {
                            Point var17 = var14[var16];
                            int var18 = var17.x;
                            var17.x = var17.y;
                            var17.y = var18;
                        }
                    }

                    int var24 = var11.y;

                    while (true)
                    {
                        if (var11.y < var12.y)
                        {
                            if (var24 > var12.y)
                            {
                                break;
                            }
                        }
                        else if (var24 < var12.y)
                        {
                            break;
                        }

                        var15 = var11.x;

                        while (true)
                        {
                            if (var11.x < var12.x)
                            {
                                if (var15 > var12.x)
                                {
                                    break;
                                }
                            }
                            else if (var15 < var12.x)
                            {
                                break;
                            }

                            var3[var13++] = var5 ? index(var2, var15, var24) : index(var2, var24, var15);
                            var15 += var11.x < var12.x ? 1 : -1;
                        }

                        var24 += var11.y < var12.y ? 1 : -1;
                    }

                    if (var0.contains("r"))
                    {
                        reverseArray(var3);
                    }
                }
            }
        }
        else
        {
            int var19 = -1;
            int var20 = -1;
            boolean var21 = false;
            int var22;

            for (var22 = 0; var22 < var0.length(); ++var22)
            {
                char var23 = var0.charAt(var22);

                if (var23 >= 49 && var23 - 49 <= var2)
                {
                    var19 = var23 - 49;
                }
                else if (var23 >= 97 && var23 - 97 <= var4)
                {
                    var20 = var23 - 97;
                }
                else if (var23 == 114)
                {
                    var21 = true;
                }
            }

            if (var19 != -1 && var20 != -1)
            {
                var3 = new int[] {index(var2, var20, var19)};
            }
            else if (var20 != -1)
            {
                var3 = new int[var2];

                for (var22 = 0; var22 < var2; ++var22)
                {
                    var3[var22] = index(var2, var20, var21 ? var2 - 1 - var22 : var22);
                }
            }
            else
            {
                var3 = new int[var4];

                for (var22 = 0; var22 < var4; ++var22)
                {
                    var3[var22] = index(var2, var21 ? var22 : var4 - 1 - var22, var19);
                }
            }
        }

        return var3;
    }

    public static InvTweaksConfigSortingRuleType getRuleType(String var0, int var1)
    {
        InvTweaksConfigSortingRuleType var2 = InvTweaksConfigSortingRuleType.SLOT;

        if (var0.length() != 1 && (var0.length() != 2 || !var0.contains("r")))
        {
            if (var0.length() > 4)
            {
                if (var0.charAt(1) == var0.charAt(4))
                {
                    var2 = InvTweaksConfigSortingRuleType.COLUMN;
                }
                else if (var0.charAt(0) == var0.charAt(3))
                {
                    var2 = InvTweaksConfigSortingRuleType.ROW;
                }
                else
                {
                    var2 = InvTweaksConfigSortingRuleType.RECTANGLE;
                }
            }
        }
        else
        {
            var0 = var0.replace("r", "");

            if (var0.charAt(0) - 49 <= var1 && var0.charAt(0) >= 49)
            {
                var2 = InvTweaksConfigSortingRuleType.COLUMN;
            }
            else
            {
                var2 = InvTweaksConfigSortingRuleType.ROW;
            }
        }

        return var2;
    }

    public String toString()
    {
        return this.constraint + " " + this.keyword;
    }

    private static int index(int var0, int var1, int var2)
    {
        return var1 * var0 + var2;
    }

    private static void reverseArray(int[] var0)
    {
        int var1 = 0;

        for (int var2 = var0.length - 1; var1 < var2; --var2)
        {
            int var3 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var3;
            ++var1;
        }
    }

    public int compareTo(Object var1)
    {
        return this.compareTo((InvTweaksConfigSortingRule)var1);
    }
}
