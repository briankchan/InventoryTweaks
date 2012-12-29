package invtweaks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

public class InvTweaksItemTreeCategory
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private final Map items = new HashMap();
    private final Vector matchingItems = new Vector();
    private final Vector subCategories = new Vector();
    private String name;
    private int order = -1;

    public InvTweaksItemTreeCategory(String var1)
    {
        this.name = var1 != null ? var1.toLowerCase() : null;
    }

    public boolean contains(InvTweaksItemTreeItem var1)
    {
        List var2 = (List)this.items.get(Integer.valueOf(var1.getId()));
        Iterator var3;

        if (var2 != null)
        {
            var3 = var2.iterator();

            while (var3.hasNext())
            {
                InvTweaksItemTreeItem var4 = (InvTweaksItemTreeItem)var3.next();

                if (var4.equals(var1))
                {
                    return true;
                }
            }
        }

        var3 = this.subCategories.iterator();
        InvTweaksItemTreeCategory var5;

        do
        {
            if (!var3.hasNext())
            {
                return false;
            }

            var5 = (InvTweaksItemTreeCategory)var3.next();
        }
        while (!var5.contains(var1));

        return true;
    }

    public void addCategory(InvTweaksItemTreeCategory var1)
    {
        this.subCategories.add(var1);
    }

    public void addItem(InvTweaksItemTreeItem var1)
    {
        if (this.items.get(Integer.valueOf(var1.getId())) == null)
        {
            ArrayList var2 = new ArrayList();
            var2.add(var1);
            this.items.put(Integer.valueOf(var1.getId()), var2);
        }
        else
        {
            ((List)this.items.get(Integer.valueOf(var1.getId()))).add(var1);
        }

        this.matchingItems.add(var1.getName());

        if (this.order == -1 || this.order > var1.getOrder())
        {
            this.order = var1.getOrder();
        }
    }

    public int getCategoryOrder()
    {
        if (this.order != -1)
        {
            return this.order;
        }
        else
        {
            Iterator var2 = this.subCategories.iterator();
            int var1;

            do
            {
                if (!var2.hasNext())
                {
                    return -1;
                }

                InvTweaksItemTreeCategory var3 = (InvTweaksItemTreeCategory)var2.next();
                var1 = var3.getCategoryOrder();
            }
            while (var1 == -1);

            return var1;
        }
    }

    public int findCategoryOrder(String var1)
    {
        if (var1.equals(this.name))
        {
            return this.getCategoryOrder();
        }
        else
        {
            Iterator var3 = this.subCategories.iterator();
            int var2;

            do
            {
                if (!var3.hasNext())
                {
                    return -1;
                }

                InvTweaksItemTreeCategory var4 = (InvTweaksItemTreeCategory)var3.next();
                var2 = var4.findCategoryOrder(var1);
            }
            while (var2 == -1);

            return var2;
        }
    }

    public int findKeywordDepth(String var1)
    {
        if (this.name.equals(var1))
        {
            return 0;
        }
        else if (this.matchingItems.contains(var1))
        {
            return 1;
        }
        else
        {
            Iterator var3 = this.subCategories.iterator();
            int var2;

            do
            {
                if (!var3.hasNext())
                {
                    return -1;
                }

                InvTweaksItemTreeCategory var4 = (InvTweaksItemTreeCategory)var3.next();
                var2 = var4.findKeywordDepth(var1);
            }
            while (var2 == -1);

            return var2 + 1;
        }
    }

    public Collection getSubCategories()
    {
        return this.subCategories;
    }

    public Collection getItems()
    {
        return this.items.values();
    }

    public String getName()
    {
        return this.name;
    }

    public String toString()
    {
        return this.name + " (" + this.subCategories.size() + " cats, " + this.items.size() + " items)";
    }
}
