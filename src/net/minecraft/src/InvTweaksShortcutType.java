package net.minecraft.src;

public enum InvTweaksShortcutType
{
    MOVE_ALL_ITEMS,
    MOVE_EVERYTHING,
    MOVE_ONE_STACK,
    MOVE_ONE_ITEM,
    MOVE_UP,
    MOVE_DOWN,
    MOVE_TO_SPECIFIC_HOTBAR_SLOT,
    DROP;

    public static InvTweaksShortcutType fromConfigKey(String var0)
    {
        return "shortcutKeyAllItems".equals(var0) ? MOVE_ALL_ITEMS : ("shortcutKeyEverything".equals(var0) ? MOVE_EVERYTHING : ("shortcutKeyToLowerSection".equals(var0) ? MOVE_DOWN : ("shortcutKeyDrop".equals(var0) ? DROP : ("shortcutKeyOneItem".equals(var0) ? MOVE_ONE_ITEM : ("shortcutKeyToUpperSection".equals(var0) ? MOVE_UP : null)))));
    }
}
