package invtweaks;

public class InvTweaksItemTreeItem implements Comparable
{
    private String name;
    private int id;
    private int damage;
    private int order;

    public InvTweaksItemTreeItem(String var1, int var2, int var3, int var4)
    {
        this.name = var1;
        this.id = var2;
        this.damage = var3;
        this.order = var4;
    }

    public String getName()
    {
        return this.name;
    }

    public int getId()
    {
        return this.id;
    }

    public int getDamage()
    {
        return this.damage;
    }

    public int getOrder()
    {
        return this.order;
    }

    public boolean equals(Object var1)
    {
        if (var1 != null && var1 instanceof InvTweaksItemTreeItem)
        {
            InvTweaksItemTreeItem var2 = (InvTweaksItemTreeItem)var1;
            return this.id == var2.getId() && (this.damage == -1 || this.damage == var2.getDamage());
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        return this.name;
    }

    public int compareTo(InvTweaksItemTreeItem var1)
    {
        return var1.order - this.order;
    }

    public int compareTo(Object var1)
    {
        return this.compareTo((InvTweaksItemTreeItem)var1);
    }
}
