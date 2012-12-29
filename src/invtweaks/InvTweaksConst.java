package invtweaks;

import java.io.File;
import java.util.logging.Level;
import net.minecraft.client.Minecraft;

public class InvTweaksConst
{
    public static final String MOD_VERSION = "1.47 (1.4.6)";
    public static final String TREE_VERSION = "1.4.0";
    public static final int RULESET_SWAP_DELAY = 1000;
    public static final int AUTO_REFILL_DELAY = 200;
    public static final int AUTO_REFILL_DAMAGE_TRESHOLD = 5;
    public static final int POLLING_DELAY = 3;
    public static final int POLLING_TIMEOUT = 1500;
    public static final int CHEST_ALGORITHM_SWAP_MAX_INTERVAL = 2000;
    public static final int TOOLTIP_DELAY = 800;
    public static final int INTERRUPT_DELAY = 300;
    public static final String MINECRAFT_DIR = getMinecraftDir();
    public static final String MINECRAFT_CONFIG_DIR = MINECRAFT_DIR + "config" + File.separatorChar;
    public static final String CONFIG_PROPS_FILE = MINECRAFT_CONFIG_DIR + "InvTweaks.cfg";
    public static final String CONFIG_RULES_FILE = MINECRAFT_CONFIG_DIR + "InvTweaksRules.txt";
    public static final String CONFIG_TREE_FILE = MINECRAFT_CONFIG_DIR + "InvTweaksTree.txt";
    public static final String OLD_CONFIG_TREE_FILE = MINECRAFT_CONFIG_DIR + "InvTweaksTree.xml";
    public static final String OLDER_CONFIG_RULES_FILE = MINECRAFT_DIR + "InvTweaksRules.txt";
    public static final String OLDER_CONFIG_TREE_FILE = MINECRAFT_DIR + "InvTweaksTree.txt";
    public static final String DEFAULT_CONFIG_FILE = "DefaultConfig.dat";
    public static final String DEFAULT_CONFIG_TREE_FILE = "DefaultTree.dat";
    public static final String HELP_URL = "http://modding.kalam-alami.net/invtweaks";
    public static final String INGAME_LOG_PREFIX = "InvTweaks: ";
    public static final Level DEFAULT_LOG_LEVEL = Level.WARNING;
    public static final Level DEBUG = Level.INFO;
    public static final int JIMEOWAN_ID = 54696386;
    public static final int INVENTORY_SIZE = 36;
    public static final int INVENTORY_ROW_SIZE = 9;
    public static final int CHEST_ROW_SIZE = 9;
    public static final int DISPENSER_ROW_SIZE = 3;
    public static final int INVENTORY_HOTBAR_SIZE = 9;
    public static final int PLAYER_INVENTORY_WINDOW_ID = 0;
    public static final int SLOW_SORTING_DELAY = 30;

    public static String getMinecraftDir()
    {
        String var0 = Minecraft.getMinecraftDir().getAbsolutePath();
        return var0.endsWith(".") ? var0.substring(0, var0.length() - 1) : (var0.endsWith(File.separator) ? var0 : var0 + File.separatorChar);
    }
}
