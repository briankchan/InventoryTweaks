package net.minecraft.src;

public enum InvTweaksConfigSortingRuleType
{
    RECTANGLE(1),
    ROW(2),
    COLUMN(3),
    SLOT(4);
    private int lowestPriority;
    private int highestPriority;

    private InvTweaksConfigSortingRuleType(int var3)
    {
        this.lowestPriority = var3 * 1000000;
        this.highestPriority = (var3 + 1) * 1000000 - 1;
    }

    public int getLowestPriority()
    {
        return this.lowestPriority;
    }

    public int getHighestPriority()
    {
        return this.highestPriority;
    }
}
