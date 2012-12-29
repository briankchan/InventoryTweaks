package invtweaks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

public class InvTweaksItemTree
{
    public static final int MAX_CATEGORY_RANGE = 1000;
    public static final String UNKNOWN_ITEM = "unknown";
    private static final Logger log = Logger.getLogger("InvTweaks");
    private Map categories = new HashMap();
    private Map itemsById = new HashMap(500);
    private static Vector defaultItems = null;
    private Map itemsByName = new HashMap(500);
    private String rootCategory;

    public InvTweaksItemTree()
    {
        this.reset();
    }

    public void reset()
    {
        if (defaultItems == null)
        {
            defaultItems = new Vector();
            defaultItems.add(new InvTweaksItemTreeItem("unknown", -1, -1, Integer.MAX_VALUE));
        }

        this.categories.clear();
        this.itemsByName.clear();
        this.itemsById.clear();
    }

    public boolean matches(List var1, String var2)
    {
        if (var1 == null)
        {
            return false;
        }
        else
        {
            Iterator var3 = var1.iterator();

            while (var3.hasNext())
            {
                InvTweaksItemTreeItem var4 = (InvTweaksItemTreeItem)var3.next();

                if (var4.getName().equals(var2))
                {
                    return true;
                }
            }

            InvTweaksItemTreeCategory var6 = this.getCategory(var2);

            if (var6 != null)
            {
                Iterator var7 = var1.iterator();

                while (var7.hasNext())
                {
                    InvTweaksItemTreeItem var5 = (InvTweaksItemTreeItem)var7.next();

                    if (var6.contains(var5))
                    {
                        return true;
                    }
                }
            }

            return var2.equals(this.rootCategory);
        }
    }

    public int getKeywordDepth(String var1)
    {
        try
        {
            return this.getRootCategory().findKeywordDepth(var1);
        }
        catch (NullPointerException var3)
        {
            log.severe("The root category is missing: " + var3.getMessage());
            return 0;
        }
    }

    public int getKeywordOrder(String var1)
    {
        List var2 = this.getItems(var1);

        if (var2 != null && var2.size() != 0)
        {
            return ((InvTweaksItemTreeItem)var2.get(0)).getOrder();
        }
        else
        {
            try
            {
                return this.getRootCategory().findCategoryOrder(var1);
            }
            catch (NullPointerException var4)
            {
                log.severe("The root category is missing: " + var4.getMessage());
                return -1;
            }
        }
    }

    public boolean isKeywordValid(String var1)
    {
        if (this.containsItem(var1))
        {
            return true;
        }
        else
        {
            InvTweaksItemTreeCategory var2 = this.getCategory(var1);
            return var2 != null;
        }
    }

    public Collection getAllCategories()
    {
        return this.categories.values();
    }

    public InvTweaksItemTreeCategory getRootCategory()
    {
        return (InvTweaksItemTreeCategory)this.categories.get(this.rootCategory);
    }

    public InvTweaksItemTreeCategory getCategory(String var1)
    {
        return (InvTweaksItemTreeCategory)this.categories.get(var1);
    }

    public boolean isItemUnknown(int var1, int var2)
    {
        return this.itemsById.get(Integer.valueOf(var1)) == null;
    }

    public List getItems(int var1, int var2)
    {
        List var3 = (List)this.itemsById.get(Integer.valueOf(var1));
        ArrayList var4 = new ArrayList();

        if (var3 != null)
        {
            var4.addAll(var3);
        }

        InvTweaksItemTreeItem var6;

        if (var3 != null && !var3.isEmpty())
        {
            Iterator var5 = var3.iterator();

            while (var5.hasNext())
            {
                var6 = (InvTweaksItemTreeItem)var5.next();

                if (var6.getDamage() != -1 && var6.getDamage() != var2)
                {
                    var4.remove(var6);
                }
            }
        }

        if (var4.isEmpty())
        {
            InvTweaksItemTreeItem var7 = new InvTweaksItemTreeItem(String.format("%d-%d", new Object[] {Integer.valueOf(var1), Integer.valueOf(var2)}), var1, var2, 5000 + var1 * 16 + var2);
            var6 = new InvTweaksItemTreeItem(Integer.toString(var1), var1, -1, 5000 + var1 * 16);
            this.addItem(this.getRootCategory().getName(), var7);
            this.addItem(this.getRootCategory().getName(), var6);
            var4.add(var7);
            var4.add(var6);
        }

        return var4;
    }

    public List getItems(String var1)
    {
        return (List)this.itemsByName.get(var1);
    }

    public InvTweaksItemTreeItem getRandomItem(Random var1)
    {
        return (InvTweaksItemTreeItem)this.itemsByName.values().toArray()[var1.nextInt(this.itemsByName.size())];
    }

    public boolean containsItem(String var1)
    {
        return this.itemsByName.containsKey(var1);
    }

    public boolean containsCategory(String var1)
    {
        return this.categories.containsKey(var1);
    }

    public void setRootCategory(InvTweaksItemTreeCategory var1)
    {
        this.rootCategory = var1.getName();
        this.categories.put(this.rootCategory, var1);
    }

    public void addCategory(String var1, InvTweaksItemTreeCategory var2) throws NullPointerException
    {
        ((InvTweaksItemTreeCategory)this.categories.get(var1.toLowerCase())).addCategory(var2);
        this.categories.put(var2.getName(), var2);
    }

    public void addItem(String var1, InvTweaksItemTreeItem var2) throws NullPointerException
    {
        ((InvTweaksItemTreeCategory)this.categories.get(var1.toLowerCase())).addItem(var2);
        Vector var3;

        if (this.itemsByName.containsKey(var2.getName()))
        {
            ((Vector)this.itemsByName.get(var2.getName())).add(var2);
        }
        else
        {
            var3 = new Vector();
            var3.add(var2);
            this.itemsByName.put(var2.getName(), var3);
        }

        if (this.itemsById.containsKey(Integer.valueOf(var2.getId())))
        {
            ((Vector)this.itemsById.get(Integer.valueOf(var2.getId()))).add(var2);
        }
        else
        {
            var3 = new Vector();
            var3.add(var2);
            this.itemsById.put(Integer.valueOf(var2.getId()), var3);
        }
    }

    private void log(InvTweaksItemTreeCategory var1, int var2)
    {
        String var3 = "";

        for (int var4 = 0; var4 < var2; ++var4)
        {
            var3 = var3 + "  ";
        }

        log.info(var3 + var1.getName());
        Iterator var8 = var1.getSubCategories().iterator();

        while (var8.hasNext())
        {
            InvTweaksItemTreeCategory var5 = (InvTweaksItemTreeCategory)var8.next();
            this.log(var5, var2 + 1);
        }

        var8 = var1.getItems().iterator();

        while (var8.hasNext())
        {
            List var9 = (List)var8.next();
            Iterator var6 = var9.iterator();

            while (var6.hasNext())
            {
                InvTweaksItemTreeItem var7 = (InvTweaksItemTreeItem)var6.next();
                log.info(var3 + "  " + var7 + " " + var7.getId() + " " + var7.getDamage());
            }
        }
    }
}
