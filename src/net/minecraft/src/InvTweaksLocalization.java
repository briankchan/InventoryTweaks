package net.minecraft.src;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import net.minecraft.src.InvTweaksObfuscation;

public class InvTweaksLocalization
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    private static final String LANG_RESOURCES_LOCATION = "invtweaks/lang/";
    private static final String DEFAULT_LANGUAGE = "en_US";
    private static Properties defaultMappings = new Properties();
    private static Properties mappings = new Properties();
    private static String loadedLanguage = null;

    public static synchronized String get(String var0)
    {
        String var1 = InvTweaksObfuscation.getCurrentLanguage();

        if (!var1.equals(loadedLanguage))
        {
            loadedLanguage = load(var1);
        }

        return mappings.getProperty(var0, defaultMappings.getProperty(var0, var0));
    }

    private static String load(String var0)
    {
        defaultMappings.clear();
        mappings.clear();

        try
        {
            InputStream var1 = InvTweaksLocalization.class.getResourceAsStream("invtweaks/lang/" + var0 + ".properties");
            InputStream var2 = InvTweaksLocalization.class.getResourceAsStream("invtweaks/lang/en_US.properties");
            mappings.load(var1 == null ? var2 : var1);
            defaultMappings.load(var2);

            if (var1 != null)
            {
                var1.close();
            }

            var2.close();
        }
        catch (Exception var3)
        {
            var3.printStackTrace();
        }

        return var0;
    }
}
