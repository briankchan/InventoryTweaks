package net.minecraft.src;

class InvTweaksHandlerShortcuts$1
{
    static final int[] $SwitchMap$InvTweaksContainerSection;

    static final int[] $SwitchMap$InvTweaksShortcutType = new int[InvTweaksShortcutType.values().length];

    static
    {
        try
        {
            $SwitchMap$InvTweaksShortcutType[InvTweaksShortcutType.MOVE_ONE_STACK.ordinal()] = 1;
        }
        catch (NoSuchFieldError var8)
        {
            ;
        }

        try
        {
            $SwitchMap$InvTweaksShortcutType[InvTweaksShortcutType.MOVE_ONE_ITEM.ordinal()] = 2;
        }
        catch (NoSuchFieldError var7)
        {
            ;
        }

        try
        {
            $SwitchMap$InvTweaksShortcutType[InvTweaksShortcutType.MOVE_ALL_ITEMS.ordinal()] = 3;
        }
        catch (NoSuchFieldError var6)
        {
            ;
        }

        try
        {
            $SwitchMap$InvTweaksShortcutType[InvTweaksShortcutType.MOVE_EVERYTHING.ordinal()] = 4;
        }
        catch (NoSuchFieldError var5)
        {
            ;
        }

        $SwitchMap$InvTweaksContainerSection = new int[InvTweaksContainerSection.values().length];

        try
        {
            $SwitchMap$InvTweaksContainerSection[InvTweaksContainerSection.CHEST.ordinal()] = 1;
        }
        catch (NoSuchFieldError var4)
        {
            ;
        }

        try
        {
            $SwitchMap$InvTweaksContainerSection[InvTweaksContainerSection.INVENTORY_HOTBAR.ordinal()] = 2;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            $SwitchMap$InvTweaksContainerSection[InvTweaksContainerSection.CRAFTING_IN.ordinal()] = 3;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$InvTweaksContainerSection[InvTweaksContainerSection.FURNACE_IN.ordinal()] = 4;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
