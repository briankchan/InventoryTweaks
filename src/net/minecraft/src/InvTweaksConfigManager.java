package net.minecraft.src;

import invtweaks.InvTweaksConst;
import invtweaks.InvTweaksItemTreeLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.Minecraft;

public class InvTweaksConfigManager
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private Minecraft mc;
    private InvTweaksConfig config = null;
    private long storedConfigLastModified = 0L;
    private InvTweaksHandlerAutoRefill autoRefillHandler = null;
    private InvTweaksHandlerShortcuts shortcutsHandler = null;

    public InvTweaksConfigManager(Minecraft var1)
    {
        this.mc = var1;
    }

    public boolean makeSureConfigurationIsLoaded()
    {
        try
        {
            if (this.config != null && this.config.refreshProperties())
            {
                this.shortcutsHandler = new InvTweaksHandlerShortcuts(this.mc, this.config);
                InvTweaks.logInGameStatic("invtweaks.propsfile.loaded");
            }
        }
        catch (IOException var3)
        {
            InvTweaks.logInGameErrorStatic("invtweaks.loadconfig.refresh.error", var3);
        }

        long var1 = this.computeConfigLastModified();

        if (this.config != null)
        {
            return this.storedConfigLastModified != var1 ? this.loadConfig() : true;
        }
        else
        {
            this.storedConfigLastModified = var1;
            return this.loadConfig();
        }
    }

    public InvTweaksConfig getConfig()
    {
        return this.config;
    }

    public InvTweaksHandlerAutoRefill getAutoRefillHandler()
    {
        return this.autoRefillHandler;
    }

    public InvTweaksHandlerShortcuts getShortcutsHandler()
    {
        return this.shortcutsHandler;
    }

    private long computeConfigLastModified()
    {
        return (new File(InvTweaksConst.CONFIG_RULES_FILE)).lastModified() + (new File(InvTweaksConst.CONFIG_TREE_FILE)).lastModified();
    }

    private boolean loadConfig()
    {
        File var1 = new File(InvTweaksConst.MINECRAFT_CONFIG_DIR);

        if (!var1.exists())
        {
            var1.mkdir();
        }

        try
        {
            if (!InvTweaksItemTreeLoader.isValidVersion(InvTweaksConst.CONFIG_TREE_FILE))
            {
                this.backupFile(new File(InvTweaksConst.CONFIG_TREE_FILE), InvTweaksConst.CONFIG_TREE_FILE);
            }
        }
        catch (Exception var7)
        {
            log.warning("Failed to check item tree version: " + var7.getMessage());
        }

        if ((new File(InvTweaksConst.OLDER_CONFIG_RULES_FILE)).exists())
        {
            if ((new File(InvTweaksConst.CONFIG_RULES_FILE)).exists())
            {
                this.backupFile(new File(InvTweaksConst.CONFIG_RULES_FILE), InvTweaksConst.CONFIG_RULES_FILE);
            }

            (new File(InvTweaksConst.OLDER_CONFIG_RULES_FILE)).renameTo(new File(InvTweaksConst.CONFIG_RULES_FILE));
        }

        if ((new File(InvTweaksConst.OLDER_CONFIG_TREE_FILE)).exists())
        {
            this.backupFile(new File(InvTweaksConst.OLDER_CONFIG_TREE_FILE), InvTweaksConst.CONFIG_TREE_FILE);
        }

        if ((new File(InvTweaksConst.OLD_CONFIG_TREE_FILE)).exists())
        {
            (new File(InvTweaksConst.OLD_CONFIG_TREE_FILE)).renameTo(new File(InvTweaksConst.CONFIG_TREE_FILE));
        }

        if (!(new File(InvTweaksConst.CONFIG_RULES_FILE)).exists() && this.extractFile("DefaultConfig.dat", InvTweaksConst.CONFIG_RULES_FILE))
        {
            InvTweaks.logInGameStatic(InvTweaksConst.CONFIG_RULES_FILE + " " + InvTweaksLocalization.get("invtweaks.loadconfig.invalidkeywords"));
        }

        if (!(new File(InvTweaksConst.CONFIG_TREE_FILE)).exists() && this.extractFile("DefaultTree.dat", InvTweaksConst.CONFIG_TREE_FILE))
        {
            InvTweaks.logInGameStatic(InvTweaksConst.CONFIG_TREE_FILE + " " + InvTweaksLocalization.get("invtweaks.loadconfig.invalidkeywords"));
        }

        this.storedConfigLastModified = this.computeConfigLastModified();
        String var2 = null;
        Exception var3 = null;

        try
        {
            if (this.config == null)
            {
                this.config = new InvTweaksConfig(InvTweaksConst.CONFIG_RULES_FILE, InvTweaksConst.CONFIG_TREE_FILE);
                this.autoRefillHandler = new InvTweaksHandlerAutoRefill(this.mc, this.config);
                this.shortcutsHandler = new InvTweaksHandlerShortcuts(this.mc, this.config);
            }

            this.config.load();
            this.shortcutsHandler.loadShortcuts();
            log.setLevel(this.config.getLogLevel());
            InvTweaks.logInGameStatic("invtweaks.loadconfig.done");
            this.showConfigErrors(this.config);
        }
        catch (FileNotFoundException var5)
        {
            var2 = "Config file not found";
        }
        catch (Exception var6)
        {
            var2 = "Error while loading config";
            var3 = var6;
        }

        if (var2 != null)
        {
            InvTweaks.logInGameErrorStatic(var2, var3);
            log.severe(var2);
            this.config = null;
            return false;
        }
        else
        {
            return true;
        }
    }

    private void backupFile(File var1, String var2)
    {
        File var3 = new File(var2 + ".bak");

        if (var3.exists())
        {
            var3.delete();
        }

        var1.renameTo(var3);
    }

    private boolean extractFile(String var1, String var2)
    {
        String var3 = "";
        URL var4 = InvTweaks.class.getResource(var1);

        if (var4 != null)
        {
            InputStream var5 = null;

            try
            {
                Object var6 = var4.getContent();
                byte[] var7;

                if (var6 instanceof InputStream)
                {
                    for (var5 = (InputStream)var6; var5.available() > 0; var3 = var3 + new String(var7))
                    {
                        var7 = new byte[var5.available()];
                        var5.read(var7);
                    }
                }
            }
            catch (IOException var29)
            {
                var4 = null;
            }
            finally
            {
                if (var5 != null)
                {
                    try
                    {
                        var5.close();
                    }
                    catch (IOException var24)
                    {
                        var24.printStackTrace();
                    }
                }
            }
        }

        if (var4 == null)
        {
            File var31 = new File(InvTweaksConst.MINECRAFT_DIR + File.separatorChar + "mods");
            File[] var33 = var31.listFiles();

            if (var33 != null && var33.length > 0)
            {
                File[] var34 = var33;
                int var8 = var33.length;
                label178:

                for (int var9 = 0; var9 < var8; ++var9)
                {
                    File var10 = var34[var9];

                    try
                    {
                        ZipFile var11 = new ZipFile(var10);
                        ZipEntry var12 = var11.getEntry(var1);

                        if (var12 != null)
                        {
                            InputStream var13 = var11.getInputStream(var12);

                            while (true)
                            {
                                if (var13.available() <= 0)
                                {
                                    break label178;
                                }

                                byte[] var14 = new byte[var13.available()];
                                var13.read(var14);
                                var3 = var3 + new String(var14);
                            }
                        }
                    }
                    catch (Exception var28)
                    {
                        log.warning("Failed to extract " + var1 + " from mod: " + var28.getMessage());
                    }
                }
            }
        }

        if (!var3.isEmpty())
        {
            try
            {
                FileWriter var32 = new FileWriter(var2);
                var32.write(var3);
                var32.close();
                return true;
            }
            catch (IOException var26)
            {
                try
                {
                    InvTweaks.logInGameStatic(String.format(InvTweaksLocalization.get("invtweaks.extract.create.error"), new Object[] {var2}));
                }
                catch (IllegalFormatException var25)
                {
                    InvTweaks.logInGameStatic("[16] The mod won\'t work, because " + var2 + " creation failed!");
                }

                log.severe("Cannot create " + var2 + " file: " + var26.getMessage());
                return false;
            }
        }
        else
        {
            try
            {
                InvTweaks.logInGameStatic(String.format(InvTweaksLocalization.get("invtweaks.extract.find.error"), new Object[] {var1}));
            }
            catch (IllegalFormatException var27)
            {
                InvTweaks.logInGameStatic("[15] The mod won\'t work, because " + var1 + " creation failed!");
            }

            log.severe("Cannot create " + var2 + " file: " + var1 + " not found");
            return false;
        }
    }

    private void showConfigErrors(InvTweaksConfig var1)
    {
        Vector var2 = var1.getInvalidKeywords();

        if (var2.size() > 0)
        {
            String var3 = InvTweaksLocalization.get("invtweaks.loadconfig.invalidkeywords") + ": ";
            String var5;

            for (Iterator var4 = var1.getInvalidKeywords().iterator(); var4.hasNext(); var3 = var3 + var5 + " ")
            {
                var5 = (String)var4.next();
            }

            InvTweaks.logInGameStatic(var3);
        }
    }
}
