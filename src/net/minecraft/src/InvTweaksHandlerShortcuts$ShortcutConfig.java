package net.minecraft.src;

import net.minecraft.src.ItemStack;

class InvTweaksHandlerShortcuts$ShortcutConfig
{
    public InvTweaksShortcutType type;
    public InvTweaksContainerSection fromSection;
    public int fromIndex;
    public ItemStack fromStack;
    public InvTweaksContainerSection toSection;
    public int toIndex;
    public boolean drop;
    public boolean forceEmptySlot;

    final InvTweaksHandlerShortcuts this$0;

    private InvTweaksHandlerShortcuts$ShortcutConfig(InvTweaksHandlerShortcuts var1)
    {
        this.this$0 = var1;
        this.type = null;
        this.fromSection = null;
        this.fromIndex = -1;
        this.fromStack = null;
        this.toSection = null;
        this.toIndex = -1;
        this.drop = false;
        this.forceEmptySlot = false;
    }

    InvTweaksHandlerShortcuts$ShortcutConfig(InvTweaksHandlerShortcuts var1, InvTweaksHandlerShortcuts$1 var2)
    {
        this(var1);
    }
}
