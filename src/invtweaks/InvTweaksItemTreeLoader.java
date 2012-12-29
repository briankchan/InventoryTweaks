package invtweaks;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class InvTweaksItemTreeLoader extends DefaultHandler
{
    private static final String ATTR_ID = "id";
    private static final String ATTR_DAMAGE = "damage";
    private static final String ATTR_RANGE_MIN = "min";
    private static final String ATTR_RANGE_MAX = "max";
    private static final String ATTR_RANGE_DMIN = "dmin";
    private static final String ATTR_RANGE_DMAX = "dmax";
    private static final String ATTR_TREE_VERSION = "treeVersion";
    private static InvTweaksItemTree tree;
    private static String treeVersion;
    private static int itemOrder;
    private static LinkedList categoryStack;
    private static Boolean treeLoaded;
    private static List onLoadListeners = new ArrayList();

    private static void init()
    {
        treeLoaded = Boolean.valueOf(false);
        treeVersion = null;
        tree = new InvTweaksItemTree();
        itemOrder = 0;
        categoryStack = new LinkedList();
    }

    public static synchronized InvTweaksItemTree load(String var0) throws Exception
    {
        init();
        SAXParserFactory var1 = SAXParserFactory.newInstance();
        SAXParser var2 = var1.newSAXParser();
        var2.parse(new File(var0), new InvTweaksItemTreeLoader());
        List var3 = onLoadListeners;

        synchronized (onLoadListeners)
        {
            treeLoaded = Boolean.valueOf(true);
            Iterator var4 = onLoadListeners.iterator();

            while (var4.hasNext())
            {
                InvTweaksItemTreeListener var5 = (InvTweaksItemTreeListener)var4.next();
                var5.onTreeLoaded(tree);
            }

            return tree;
        }
    }

    public static synchronized boolean isValidVersion(String var0) throws Exception
    {
        init();
        File var1 = new File(var0);

        if (var1.exists())
        {
            treeVersion = null;
            SAXParserFactory var2 = SAXParserFactory.newInstance();
            SAXParser var3 = var2.newSAXParser();
            var3.parse(var1, new InvTweaksItemTreeLoader());
            return "1.4.0".equals(treeVersion);
        }
        else
        {
            return false;
        }
    }

    public static synchronized void addOnLoadListener(InvTweaksItemTreeListener var0)
    {
        onLoadListeners.add(var0);

        if (treeLoaded.booleanValue())
        {
            var0.onTreeLoaded(tree);
        }
    }

    public static synchronized boolean removeOnLoadListener(InvTweaksItemTreeListener var0)
    {
        return onLoadListeners.remove(var0);
    }

    public synchronized void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException
    {
        String var5 = var4.getValue("min");
        String var6 = var4.getValue("dmin");
        String var7 = var4.getValue("treeVersion");
        int var8;
        int var9;

        if (var4.getLength() != 0 && treeVersion != null && var5 == null && var6 == null)
        {
            if (var4.getValue("id") != null)
            {
                var8 = Integer.parseInt(var4.getValue("id"));
                var9 = -1;

                if (var4.getValue("damage") != null)
                {
                    var9 = Integer.parseInt(var4.getValue("damage"));
                }

                tree.addItem((String)categoryStack.getLast(), new InvTweaksItemTreeItem(var3.toLowerCase(), var8, var9, itemOrder++));
            }
        }
        else
        {
            if (treeVersion == null)
            {
                treeVersion = var7;
            }

            if (categoryStack.isEmpty())
            {
                tree.setRootCategory(new InvTweaksItemTreeCategory(var3));
            }
            else
            {
                tree.addCategory((String)categoryStack.getLast(), new InvTweaksItemTreeCategory(var3));
            }

            int var10;

            if (var5 != null)
            {
                var8 = Integer.parseInt(var5);
                var9 = Integer.parseInt(var4.getValue("max"));

                for (var10 = var8; var10 <= var9; ++var10)
                {
                    tree.addItem(var3, new InvTweaksItemTreeItem((var3 + var10).toLowerCase(), var10, -1, itemOrder++));
                }
            }
            else if (var6 != null)
            {
                var8 = Integer.parseInt(var4.getValue("id"));
                var9 = Integer.parseInt(var6);
                var10 = Integer.parseInt(var4.getValue("dmax"));

                for (int var11 = var9; var11 <= var10; ++var11)
                {
                    tree.addItem(var3, new InvTweaksItemTreeItem((var3 + var8 + "-" + var11).toLowerCase(), var8, var11, itemOrder++));
                }
            }

            categoryStack.add(var3);
        }
    }

    public synchronized void endElement(String var1, String var2, String var3) throws SAXException
    {
        if (!categoryStack.isEmpty() && var3.equals(categoryStack.getLast()))
        {
            categoryStack.removeLast();
        }
    }
}
