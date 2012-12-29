package net.minecraft.src;

import invtweaks.InvTweaksConst;
import invtweaks.InvTweaksItemTree;
import invtweaks.InvTweaksItemTreeLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;

public class InvTweaksConfig
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    public static final String PROP_VERSION = "version";
    public static final String PROP_ENABLE_MIDDLE_CLICK = "enableMiddleClick";
    public static final String PROP_SHOW_CHEST_BUTTONS = "showChestButtons";
    public static final String PROP_ENABLE_SORTING_ON_PICKUP = "enableSortingOnPickup";
    public static final String PROP_ENABLE_AUTO_EQUIP_ARMOR = "enableAutoEquipArmor";
    public static final String PROP_ENABLE_AUTO_REFILL = "enableAutoRefill";
    public static final String PROP_AUTO_REFILL_BEFORE_BREAK = "autoRefillBeforeBreak";
    public static final String PROP_KEY_SORT_INVENTORY = "keySortInventory";
    public static final String PROP_ENABLE_SHORTCUTS = "enableShortcuts";
    public static final String PROP_SHORTCUT_PREFIX = "shortcutKey";
    public static final String PROP_SHORTCUT_ONE_ITEM = "shortcutKeyOneItem";
    public static final String PROP_OBSOLETE_SHORTCUT_ONE_STACK = "shortcutKeyOneStack";
    public static final String PROP_SHORTCUT_ALL_ITEMS = "shortcutKeyAllItems";
    public static final String PROP_SHORTCUT_EVERYTHING = "shortcutKeyEverything";
    public static final String PROP_SHORTCUT_DROP = "shortcutKeyDrop";
    public static final String PROP_SHORTCUT_UP = "shortcutKeyToUpperSection";
    public static final String PROP_SHORTCUT_DOWN = "shortcutKeyToLowerSection";
    public static final String PROP_SLOW_SORTING = "slowSorting";
    public static final String PROP_SLOW_SORTING_DELAY = "slowsortingdelay";
    public static final String PROP_ENABLE_SOUNDS = "enableSounds";
    public static final String PROP_OBSOLETE_ENABLE_SORTING_SOUND = "enableSortingSound";
    public static final String PROP_OBSOLETE_ENABLE_AUTO_REFILL_SOUND = "enableAutoRefillSound";
    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";
    public static final String VALUE_CI_COMPATIBILITY = "convenientInventoryCompatibility";
    public static final String LOCKED = "locked";
    public static final String FROZEN = "frozen";
    public static final String AUTOREFILL = "autorefill";
    public static final String AUTOREFILL_NOTHING = "nothing";
    public static final String DEBUG = "debug";
    public static final boolean DEFAULT_AUTO_REFILL_BEHAVIOUR = true;
    private String rulesFile;
    private String treeFile;
    private InvTweaksConfigProperties properties;
    private InvTweaksItemTree tree;
    private Vector rulesets;
    private int currentRuleset = 0;
    private String currentRulesetName = null;
    private Vector invalidKeywords;
    private int sortKeyCode;
    private long storedConfigLastModified;

    public InvTweaksConfig(String var1, String var2)
    {
        this.rulesFile = var1;
        this.treeFile = var2;
        this.reset();
    }

    public void load() throws Exception
    {
        synchronized (this)
        {
            this.reset();
            this.loadProperties();
            this.saveProperties();
            this.tree = InvTweaksItemTreeLoader.load(this.treeFile);
            File var2 = new File(this.rulesFile);
            char[] var3 = new char[(int)var2.length()];
            FileReader var4 = null;

            try
            {
                var4 = new FileReader(var2);
                var4.read(var3);
            }
            finally
            {
                if (var4 != null)
                {
                    var4.close();
                }
            }

            String[] var5 = String.valueOf(var3).replace("\r\n", "\n").replace('\r', '\n').split("\n");
            InvTweaksConfigInventoryRuleset var6 = new InvTweaksConfigInventoryRuleset(this.tree, "Default");
            boolean var7 = true;
            boolean var8 = true;
            String[] var10 = var5;
            int var11 = var5.length;

            for (int var12 = 0; var12 < var11; ++var12)
            {
                String var13 = var10[var12];
                String var14 = var13.trim();

                if (!var14.isEmpty())
                {
                    if (var14.matches("^[\\w]*[\\s]*\\:$"))
                    {
                        if (!var7 || !var8)
                        {
                            var6.finalize();
                            this.rulesets.add(var6);
                        }

                        var6 = new InvTweaksConfigInventoryRuleset(this.tree, var14.substring(0, var14.length() - 1));
                    }
                    else
                    {
                        try
                        {
                            String var9 = var6.registerLine(var14);

                            if (var7)
                            {
                                var8 = false;
                            }

                            if (var9 != null)
                            {
                                this.invalidKeywords.add(var9);
                            }
                        }
                        catch (InvalidParameterException var20)
                        {
                            ;
                        }
                    }
                }
            }

            var6.finalize();
            this.rulesets.add(var6);
            this.currentRuleset = 0;

            if (this.currentRulesetName != null)
            {
                int var24 = 0;

                for (Iterator var23 = this.rulesets.iterator(); var23.hasNext(); ++var24)
                {
                    InvTweaksConfigInventoryRuleset var25 = (InvTweaksConfigInventoryRuleset)var23.next();

                    if (var25.getName().equals(this.currentRulesetName))
                    {
                        this.currentRuleset = var24;
                        break;
                    }
                }
            }

            if (this.currentRuleset == 0)
            {
                if (!this.rulesets.isEmpty())
                {
                    this.currentRulesetName = ((InvTweaksConfigInventoryRuleset)this.rulesets.get(this.currentRuleset)).getName();
                }
                else
                {
                    this.currentRulesetName = null;
                }
            }
        }
    }

    public boolean refreshProperties() throws IOException
    {
        long var1 = (new File(InvTweaksConst.CONFIG_PROPS_FILE)).lastModified();

        if (this.storedConfigLastModified != var1)
        {
            this.storedConfigLastModified = var1;
            this.loadProperties();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void saveProperties()
    {
        File var1 = this.getPropertyFile();

        if (var1.exists())
        {
            try
            {
                FileOutputStream var2 = new FileOutputStream(var1);
                this.properties.store(var2, "Inventory Tweaks Configuration\n(Regarding shortcuts, all key names can be found at: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)");
                var2.flush();
                var2.close();
                this.storedConfigLastModified = (new File(InvTweaksConst.CONFIG_PROPS_FILE)).lastModified();
            }
            catch (IOException var3)
            {
                InvTweaks.logInGameStatic("Failed to save config file " + InvTweaksConst.CONFIG_PROPS_FILE);
            }

            this.sortKeyCode = Keyboard.getKeyIndex(this.getProperty("keySortInventory"));
        }
    }

    public Map getProperties(String var1)
    {
        HashMap var2 = new HashMap();
        Iterator var3 = this.properties.keySet().iterator();

        while (var3.hasNext())
        {
            Object var4 = var3.next();
            String var5 = (String)var4;

            if (var5.startsWith(var1))
            {
                var2.put(var5, this.properties.getProperty(var5));
            }
        }

        return var2;
    }

    public String getProperty(String var1)
    {
        String var2 = this.properties.getProperty(var1);
        return var2 != null ? var2 : "";
    }

    public void setProperty(String var1, String var2)
    {
        this.properties.put(var1, var2);
        this.saveProperties();

        if (var1.equals("enableMiddleClick"))
        {
            this.resolveConvenientInventoryConflicts();
        }
    }

    public InvTweaksItemTree getTree()
    {
        return this.tree;
    }

    public String getCurrentRulesetName()
    {
        return this.currentRulesetName;
    }

    public String switchConfig(int var1)
    {
        if (!this.rulesets.isEmpty() && var1 < this.rulesets.size() && var1 != this.currentRuleset)
        {
            this.currentRuleset = var1;
            this.currentRulesetName = ((InvTweaksConfigInventoryRuleset)this.rulesets.get(this.currentRuleset)).getName();
            return this.currentRulesetName;
        }
        else
        {
            return null;
        }
    }

    public String switchConfig()
    {
        return this.currentRuleset == -1 ? this.switchConfig(0) : this.switchConfig((this.currentRuleset + 1) % this.rulesets.size());
    }

    public Vector getRules()
    {
        return ((InvTweaksConfigInventoryRuleset)this.rulesets.get(this.currentRuleset)).getRules();
    }

    public Vector getInvalidKeywords()
    {
        return this.invalidKeywords;
    }

    public int[] getLockPriorities()
    {
        return ((InvTweaksConfigInventoryRuleset)this.rulesets.get(this.currentRuleset)).getLockPriorities();
    }

    public boolean[] getFrozenSlots()
    {
        return ((InvTweaksConfigInventoryRuleset)this.rulesets.get(this.currentRuleset)).getFrozenSlots();
    }

    public Vector getLockedSlots()
    {
        return ((InvTweaksConfigInventoryRuleset)this.rulesets.get(this.currentRuleset)).getLockedSlots();
    }

    public Level getLogLevel()
    {
        return ((InvTweaksConfigInventoryRuleset)this.rulesets.get(this.currentRuleset)).isDebugEnabled() ? Level.INFO : Level.WARNING;
    }

    public boolean isAutoRefillEnabled(int var1, int var2)
    {
        if (!this.getProperty("enableAutoRefill").equals("false"))
        {
            List var3 = this.tree.getItems(var1, var2);
            Vector var4 = ((InvTweaksConfigInventoryRuleset)this.rulesets.get(this.currentRuleset)).getAutoReplaceRules();
            boolean var5 = false;
            Iterator var6 = var4.iterator();

            while (var6.hasNext())
            {
                String var7 = (String)var6.next();

                if (var7.equals("nothing"))
                {
                    return false;
                }

                if (this.tree.matches(var3, var7))
                {
                    var5 = true;
                }
            }

            if (var5)
            {
                return true;
            }
            else if (var4.isEmpty())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    public int getClickDelay()
    {
        String slowSortingValue = getProperty(PROP_SLOW_SORTING);
        if (VALUE_FALSE.equals(slowSortingValue)) {
            return 0;
        }
        else {
         try{
         return Integer.parseInt(getProperty(PROP_SLOW_SORTING_DELAY));
         }catch (NumberFormatException e){
         return 0;
         }
        }
    }

    /*public int getClickDelay() //Unused
    {
        String var1 = this.getProperty("slowSorting");
        return "false".equals(var1) ? 0 : 30;
    }*/

    public void resolveConvenientInventoryConflicts()
    {
        boolean var1 = false;
        boolean var2 = false;

        try
        {
            Class var3 = Class.forName("ConvenientInventory");
            var1 = true;
            Field var4 = null;

            try
            {
                var4 = var3.getDeclaredField("middleClickEnabled");
            }
            catch (NoSuchFieldException var13)
            {
                ;
            }

            if (var4 != null)
            {
                boolean var5 = this.getProperty("enableMiddleClick").equals("true");
                var4.setAccessible(true);
                var4.setBoolean((Object)null, !var5);
            }
            else
            {
                Field var17 = var3.getDeclaredField("initialized");
                var17.setAccessible(true);
                Boolean var6 = (Boolean)var17.get((Object)null);

                if (!var6.booleanValue())
                {
                    Method var7 = var3.getDeclaredMethod("initialize", new Class[0]);
                    var7.setAccessible(true);
                    var7.invoke((Object)null, new Object[0]);
                }

                Field var19 = var3.getDeclaredField("actionMap");
                var19.setAccessible(true);
                List[][] var8 = (List[][])((List[][])var19.get((Object)null));

                if (var8 != null && var8[7] != null)
                {
                    List[] var9 = var8[7];
                    int var10 = var9.length;

                    for (int var11 = 0; var11 < var10; ++var11)
                    {
                        List var12 = var9[var11];

                        if (var12 != null && var12.size() == 1 && ((Integer)var12.get(0)).intValue() == 2)
                        {
                            var2 = true;
                            break;
                        }
                    }
                }
            }
        }
        catch (ClassNotFoundException var14)
        {
            ;
        }
        catch (Exception var15)
        {
            InvTweaks.logInGameErrorStatic("invtweaks.modcompat.ci.error", var15);
        }

        String var16 = this.getProperty("enableShortcuts");

        if (var1 && !var16.equals("convenientInventoryCompatibility"))
        {
            this.setProperty("enableShortcuts", "convenientInventoryCompatibility");
        }
        else if (!var1 && var16.equals("convenientInventoryCompatibility"))
        {
            this.setProperty("enableShortcuts", "true");
        }

        String var18 = this.getProperty("enableMiddleClick");

        if (var2 && !var18.equals("convenientInventoryCompatibility"))
        {
            this.setProperty("enableMiddleClick", "convenientInventoryCompatibility");
        }
        else if (!var2 && var18.equals("convenientInventoryCompatibility"))
        {
            this.setProperty("enableMiddleClick", "true");
        }
    }

    private void reset()
    {
        this.rulesets = new Vector();
        this.currentRuleset = -1;
        this.properties = new InvTweaksConfigProperties();
        this.properties.put("enableMiddleClick", "true");
        this.properties.put("showChestButtons", "true");
        this.properties.put("enableSortingOnPickup", "false");
        this.properties.put("enableAutoRefill", "true");
        this.properties.put("autoRefillBeforeBreak", "false");
        this.properties.put("enableSounds", "true");
        this.properties.put("enableShortcuts", "true");
        this.properties.put("enableAutoEquipArmor", "false");
        this.properties.put("keySortInventory", "R");
        this.properties.put("slowSorting", "false");
        this.properties.put(PROP_SLOW_SORTING_DELAY,"60");
        this.properties.put("shortcutKeyAllItems", "LCONTROL+LSHIFT, RCONTROL+RSHIFT");
        this.properties.put("shortcutKeyEverything", "SPACE");
        this.properties.put("shortcutKeyOneItem", "LCONTROL, RCONTROL");
        this.properties.put("shortcutKeyToUpperSection", "UP");
        this.properties.put("shortcutKeyToLowerSection", "DOWN");
        this.properties.put("shortcutKeyDrop", "LALT, RALT");
        this.properties.put("version", "1.47 (1.4.6)".split(" ")[0]);
        this.invalidKeywords = new Vector();
    }

    private void loadProperties() throws IOException
    {
        File var1 = this.getPropertyFile();
        InvTweaksConfigProperties var2 = new InvTweaksConfigProperties();

        if (var1 != null)
        {
            FileInputStream var3 = new FileInputStream(var1);
            var2.load(var3);
            var3.close();
            this.resolveConvenientInventoryConflicts();
        }

        var2.sortKeys();
        var2.remove("enableSortingSound");
        var2.remove("enableAutoRefillSound");
        var2.remove("shortcutKeyOneStack");

        if (var2.get("version") != null)
        {
            Iterator var5 = var2.entrySet().iterator();

            while (var5.hasNext())
            {
                Entry var4 = (Entry)var5.next();
                this.properties.put(var4.getKey(), var4.getValue());
            }

            if (this.properties.contains("enableAutoreplaceSound"))
            {
                this.properties.put("enableAutoRefillSound", (String)this.properties.get("enableAutoreplaceSound"));
                this.properties.remove("enableAutoreplaceSound");
            }
        }
    }

    private File getPropertyFile()
    {
        File var1 = new File(InvTweaksConst.CONFIG_PROPS_FILE);

        if (!var1.exists())
        {
            try
            {
                var1.createNewFile();
            }
            catch (IOException var3)
            {
                InvTweaks.logInGameStatic("invtweaks.propsfile.errors");
                return null;
            }
        }

        return var1;
    }

    public int getSortKeyCode()
    {
        return this.sortKeyCode;
    }
}
