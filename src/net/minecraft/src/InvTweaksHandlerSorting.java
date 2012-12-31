package net.minecraft.src;

import invtweaks.InvTweaksConst;
import invtweaks.InvTweaksItemTree;
import invtweaks.InvTweaksItemTreeItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.src.InvTweaksObfuscation;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class InvTweaksHandlerSorting extends InvTweaksObfuscation
{
    private static final Logger log = Logger.getLogger("InvTweaks");
    public static final boolean STACK_NOT_EMPTIED = true;
    public static final boolean STACK_EMPTIED = false;
    private static int[] DEFAULT_LOCK_PRIORITIES = null;
    private static boolean[] DEFAULT_FROZEN_SLOTS = null;
    private static final int MAX_CONTAINER_SIZE = 999;
    public static final int ALGORITHM_DEFAULT = 0;
    public static final int ALGORITHM_VERTICAL = 1;
    public static final int ALGORITHM_HORIZONTAL = 2;
    public static final int ALGORITHM_INVENTORY = 3;
    public static final int ALGORITHM_EVEN_STACKS = 4;
    private InvTweaksContainerSectionManager containerMgr;
    private int algorithm;
    private int size;
    private boolean sortArmorParts;
    private int clickDelay;
    private InvTweaksItemTree tree;
    private Vector rules;
    private int[] rulePriority;
    private int[] keywordOrder;
    private int[] lockPriorities;
    private boolean[] frozenSlots;

    public InvTweaksHandlerSorting(Minecraft var1, InvTweaksConfig var2, InvTweaksContainerSection var3, int var4, int var5) throws Exception
    {
        super(var1);
        int var6;

        if (DEFAULT_LOCK_PRIORITIES == null)
        {
            DEFAULT_LOCK_PRIORITIES = new int[999];

            for (var6 = 0; var6 < 999; ++var6)
            {
                DEFAULT_LOCK_PRIORITIES[var6] = 0;
            }
        }

        if (DEFAULT_FROZEN_SLOTS == null)
        {
            DEFAULT_FROZEN_SLOTS = new boolean[999];

            for (var6 = 0; var6 < 999; ++var6)
            {
                DEFAULT_FROZEN_SLOTS[var6] = false;
            }
        }

        this.clickDelay = var2.getClickDelay();
        this.containerMgr = new InvTweaksContainerSectionManager(var1, var3);
        this.containerMgr.setClickDelay(this.clickDelay);
        this.size = this.containerMgr.getSize();
        this.sortArmorParts = var2.getProperty("enableAutoEquipArmor").equals("true") && !this.isGuiInventoryCreative(this.getCurrentScreen());
        this.rules = var2.getRules();
        this.tree = var2.getTree();

        if (var3 == InvTweaksContainerSection.INVENTORY)
        {
            this.lockPriorities = var2.getLockPriorities();
            this.frozenSlots = var2.getFrozenSlots();
            this.algorithm = 3;
        }
        else
        {
            this.lockPriorities = DEFAULT_LOCK_PRIORITIES;
            this.frozenSlots = DEFAULT_FROZEN_SLOTS;
            this.algorithm = var4;

            if (var4 != 0)
            {
                this.computeLineSortingRules(var5, var4 == 2);
            }
        }

        this.rulePriority = new int[this.size];
        this.keywordOrder = new int[this.size];

        for (var6 = 0; var6 < this.size; ++var6)
        {
            this.rulePriority[var6] = -1;
            ItemStack var7 = this.containerMgr.getItemStack(var6);

            if (var7 != null)
            {
                this.keywordOrder[var6] = this.getItemOrder(var7);
            }
            else
            {
                this.keywordOrder[var6] = -1;
            }
        }
    }

    public void sort() throws TimeoutException
    {
        long var1 = System.nanoTime();
        InvTweaksContainerManager var3 = new InvTweaksContainerManager(this.mc);
        var3.setClickDelay(this.clickDelay);
        int var4;

        if (this.getHeldStack() != null)
        {
            var4 = var3.getFirstEmptyIndex(InvTweaksContainerSection.INVENTORY);

            if (var4 == -1)
            {
                return;
            }

            var3.putHoldItemDown(InvTweaksContainerSection.INVENTORY, var4);
        }

        if (this.algorithm != 0)
        {
            int var5;
            int var12;
            int var22;

            if (algorithm == 4) {
                log.info("Distributing items.");

                //item and slot counts for each unique item
                HashMap itemCounts = new HashMap();
                for(int i = 0; i < size; i++) {
                    ItemStack stack = containerMgr.getItemStack(i);
                    if(stack != null) {
                        List<Integer> item = Arrays.asList(getItemID(stack),getItemDamage(stack));
                        int[] count = (int[])itemCounts.get(item);
                        if(count == null) {
                            int[] newCount = {getStackSize(stack),1};
                            itemCounts.put(item,newCount);
                        } else {
                            count[0] += getStackSize(stack); //amount of item
                            count[1]++;                      //slots with item
                        }
                    }
                }

                //handle each unique item separately
                for(Object object:itemCounts.entrySet()) {
                    Map.Entry entry = (Map.Entry)object;
                    List<Integer> item = (List<Integer>)entry.getKey();
                    int[] count = (int[])entry.getValue();
                    int numPerSlot = count[0]/count[1];  //totalNumber/numberOfSlots

                    //skip hacked itemstacks that are larger than their max size
                    //no idea why they would be here, but may as well account for them anyway
                    if(numPerSlot <= getMaxStackSize(new ItemStack(item.get(0),1,0))) {

                        //linkedlists to store which stacks have too many/few items
                        LinkedList smallStacks = new LinkedList();
                        LinkedList largeStacks = new LinkedList();
                        for(int i = 0; i < size; i++) {
                            ItemStack stack = containerMgr.getItemStack(i);
                            if(stack != null && Arrays.asList(getItemID(stack),getItemDamage(stack)).equals(item)) {
                                int stackSize = getStackSize(stack);
                                if(stackSize > numPerSlot)
                                    largeStacks.offer(i);
                                else if(stackSize < numPerSlot)
                                    smallStacks.offer(i);
                            }
                        }

                        //move items from stacks with too many to those with too little
                        while((!smallStacks.isEmpty())) {
                            int largeIndex = (Integer)largeStacks.peek();
                            int largeSize = getStackSize(containerMgr.getItemStack(largeIndex));
                            int smallIndex = (Integer)smallStacks.peek();
                            int smallSize = getStackSize(containerMgr.getItemStack(smallIndex));
                            containerMgr.moveSome(largeIndex, smallIndex, Math.min(numPerSlot-smallSize,largeSize-numPerSlot));

                            //update stack lists
                            largeSize = getStackSize(containerMgr.getItemStack(largeIndex));
                            smallSize = getStackSize(containerMgr.getItemStack(smallIndex));
                            if(largeSize == numPerSlot)
                                largeStacks.remove();
                            if(smallSize == numPerSlot)
                                smallStacks.remove();
                        }

                        //put all leftover into one stack for easy removal
                        while(largeStacks.size() > 1) {
                            int largeIndex = (Integer)largeStacks.poll();
                            int largeSize = getStackSize(containerMgr.getItemStack(largeIndex));
                            containerMgr.moveSome(largeIndex,(Integer)largeStacks.peek(),largeSize-numPerSlot);
                        }
                    }
                }

                //mark all items as moved. (is there a better way?)
                for(int i=0;i<size;i++)
                    markAsMoved(i,1);

            } else if (algorithm == 3)
            {
                log.info("Handling crafting slots.");

                if (var3.hasSection(InvTweaksContainerSection.CRAFTING_IN))
                {
                    List var15 = var3.getSlots(InvTweaksContainerSection.CRAFTING_IN);
                    var5 = var3.getFirstEmptyIndex(InvTweaksContainerSection.INVENTORY);

                    if (var5 != -1)
                    {
                        Iterator var6 = var15.iterator();

                        while (var6.hasNext())
                        {
                            Slot var7 = (Slot)var6.next();

                            if (this.hasStack(var7))
                            {
                                var3.move(InvTweaksContainerSection.CRAFTING_IN, var3.getSlotIndex(this.getSlotNumber(var7)), InvTweaksContainerSection.INVENTORY, var5);
                                var5 = var3.getFirstEmptyIndex(InvTweaksContainerSection.INVENTORY);

                                if (var5 == -1)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }

                log.info("Merging stacks.");

                for (var4 = this.size - 1; var4 >= 0; --var4)
                {
                    ItemStack var18 = this.containerMgr.getItemStack(var4);

                    if (var18 != null)
                    {
                        Item var20 = this.getItem(var18);

                        if (this.isDamageable(var20))
                        {
                            if (this.sortArmorParts && this.isItemArmor(var20))
                            {
                                ItemArmor var21 = this.asItemArmor(var20);

                                if (var3.hasSection(InvTweaksContainerSection.ARMOR))
                                {
                                    List var8 = var3.getSlots(InvTweaksContainerSection.ARMOR);
                                    Iterator var9 = var8.iterator();

                                    while (var9.hasNext())
                                    {
                                        Slot var10 = (Slot)var9.next();
                                        boolean var11 = false;

                                        if (!this.hasStack(var10))
                                        {
                                            var11 = true;
                                        }
                                        else
                                        {
                                            var12 = this.getArmorLevel(this.asItemArmor(this.getItem(this.getStack(var10))));

                                            if (var12 < this.getArmorLevel(var21) || var12 == this.getArmorLevel(var21) && this.getItemDamage(this.getStack(var10)) < this.getItemDamage(var18))
                                            {
                                                var11 = true;
                                            }
                                        }

                                        if (this.areSlotAndStackCompatible(var10, var18) && var11)
                                        {
                                            var3.move(InvTweaksContainerSection.INVENTORY, var4, InvTweaksContainerSection.ARMOR, var3.getSlotIndex(this.getSlotNumber(var10)));
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            var22 = 0;
                            int[] var24 = this.lockPriorities;
                            int var25 = var24.length;

                            for (int var28 = 0; var28 < var25; ++var28)
                            {
                                Integer var29 = Integer.valueOf(var24[var28]);

                                if (var29.intValue() > 0)
                                {
                                    ItemStack var30 = this.containerMgr.getItemStack(var22);

                                    if (var30 != null && this.areItemsEqual(var18, var30))
                                    {
                                        this.move(var4, var22, Integer.MAX_VALUE);
                                        this.markAsNotMoved(var22);

                                        if (this.containerMgr.getItemStack(var4) == null)
                                        {
                                            break;
                                        }
                                    }
                                }

                                ++var22;
                            }
                        }
                    }
                }
            }

            log.info("Applying rules.");
            Iterator var16 = this.rules.iterator();

            while (var16.hasNext())
            {
                InvTweaksConfigSortingRule var17 = (InvTweaksConfigSortingRule)var16.next();
                int var19 = var17.getPriority();

                if (log.getLevel() == InvTweaksConst.DEBUG)
                {
                    log.info("Rule : " + var17.getKeyword() + "(" + var19 + ")");
                }

                for (var22 = 0; var22 < this.size; ++var22)
                {
                    ItemStack var23 = this.containerMgr.getItemStack(var22);

                    if (this.hasToBeMoved(var22) && this.lockPriorities[var22] < var19)
                    {
                        List var26 = this.tree.getItems(this.getItemID(var23), this.getItemDamage(var23));

                        if (this.tree.matches(var26, var17.getKeyword()))
                        {
                            int[] var27 = var17.getPreferredSlots();
                            int var31 = var22;

                            for (var12 = 0; var12 < var27.length; ++var12)
                            {
                                int var13 = var27[var12];
                                int var14 = this.move(var31, var13, var19);

                                if (var14 != -1)
                                {
                                    if (var14 == var13)
                                    {
                                        break;
                                    }

                                    var23 = this.containerMgr.getItemStack(var14);
                                    var26 = this.tree.getItems(this.getItemID(var23), this.getItemDamage(var23));

                                    if (!this.tree.matches(var26, var17.getKeyword()))
                                    {
                                        break;
                                    }

                                    var31 = var14;
                                    var12 = -1;
                                }
                            }
                        }
                    }
                }
            }

            log.info("Locking stacks.");

            for (var5 = 0; var5 < this.size; ++var5)
            {
                if (this.hasToBeMoved(var5) && this.lockPriorities[var5] > 0)
                {
                    this.markAsMoved(var5, 1);
                }
            }
        }

        this.defaultSorting();

        if (log.getLevel() == InvTweaksConst.DEBUG)
        {
            var1 = System.nanoTime() - var1;
            log.info("Sorting done in " + var1 + "ns");
        }

        if (this.getHeldStack() != null)
        {
            var4 = var3.getFirstEmptyIndex(InvTweaksContainerSection.INVENTORY);

            if (var4 != -1)
            {
                var3.putHoldItemDown(InvTweaksContainerSection.INVENTORY, var4);
            }
        }
    }

    private void defaultSorting() throws TimeoutException
    {
        log.info("Default sorting.");
        Vector var1 = new Vector();
        Vector var2 = new Vector();
        int var3;

        for (var3 = 0; var3 < this.size; ++var3)
        {
            if (this.hasToBeMoved(var3))
            {
                var1.add(Integer.valueOf(var3));
                var2.add(Integer.valueOf(var3));
            }
        }

        var3 = 0;

        while (var1.size() > 0 && var3++ < 50)
        {
            Iterator var4 = var1.iterator();

            while (var4.hasNext())
            {
                int var5 = ((Integer)var4.next()).intValue();

                if (this.hasToBeMoved(var5))
                {
                    for (int var6 = 0; var6 < this.size; ++var6)
                    {
                        if (this.move(var5, var6, 1) != -1)
                        {
                            var2.remove(Integer.valueOf(var6));
                            break;
                        }
                    }
                }
                else
                {
                    var2.remove(Integer.valueOf(var5));
                }
            }

            var1.clear();
            var1.addAll(var2);
        }

        if (var3 == 100)
        {
            log.warning("Sorting takes too long, aborting.");
        }
    }

    private int move(int var1, int var2, int var3) throws TimeoutException
    {
        ItemStack var4 = this.containerMgr.getItemStack(var1);
        ItemStack var5 = this.containerMgr.getItemStack(var2);

        if (var4 != null && !this.frozenSlots[var2] && !this.frozenSlots[var1])
        {
            if (this.lockPriorities[var1] <= var3)
            {
                if (var1 == var2)
                {
                    this.markAsMoved(var1, var3);
                    return var2;
                }

                if (var5 == null && this.lockPriorities[var2] <= var3 && !this.frozenSlots[var2])
                {
                    this.rulePriority[var1] = -1;
                    this.keywordOrder[var1] = -1;
                    this.rulePriority[var2] = var3;
                    this.keywordOrder[var2] = this.getItemOrder(var4);
                    this.containerMgr.move(var1, var2);
                    return var2;
                }

                if (var5 != null)
                {
                    boolean var6 = false;

                    if (this.lockPriorities[var2] <= var3)
                    {
                        if (this.rulePriority[var2] < var3)
                        {
                            var6 = true;
                        }
                        else if (this.rulePriority[var2] == var3 && this.isOrderedBefore(var1, var2))
                        {
                            var6 = true;
                        }
                    }

                    if (!this.hasDataTags(var4) && !this.hasDataTags(var5) && this.areItemsEqual(var4, var5))
                    {
                        if (this.getStackSize(var5) < this.getMaxStackSize(var5))
                        {
                            var6 = true;
                        }
                        else if (this.getStackSize(var4) > this.getMaxStackSize(var4))
                        {
                            var6 = false;
                        }
                    }

                    if (var6)
                    {
                        this.keywordOrder[var2] = this.keywordOrder[var1];
                        this.rulePriority[var2] = var3;
                        this.rulePriority[var1] = -1;
                        this.containerMgr.move(var1, var2);
                        ItemStack var7 = this.containerMgr.getItemStack(var1);

                        if (var7 == null)
                        {
                            return var2;
                        }

                        int var8 = var1;

                        if (this.lockPriorities[var2] > this.lockPriorities[var1])
                        {
                            for (int var9 = 0; var9 < this.size; ++var9)
                            {
                                if (this.containerMgr.getItemStack(var9) == null && this.lockPriorities[var9] == 0)
                                {
                                    var8 = var9;
                                    break;
                                }
                            }
                        }

                        if (var8 != var1)
                        {
                            this.containerMgr.move(var1, var8);
                        }

                        this.rulePriority[var8] = -1;
                        this.keywordOrder[var8] = this.getItemOrder(var7);
                        return var8;
                    }
                }
            }

            return -1;
        }
        else
        {
            return -1;
        }
    }

    private void markAsMoved(int var1, int var2)
    {
        this.rulePriority[var1] = var2;
    }

    private void markAsNotMoved(int var1)
    {
        this.rulePriority[var1] = -1;
    }

    private boolean hasToBeMoved(int var1)
    {
        return this.containerMgr.getItemStack(var1) != null && this.rulePriority[var1] == -1;
    }

    private boolean isOrderedBefore(int var1, int var2)
    {
        ItemStack var3 = this.containerMgr.getItemStack(var1);
        ItemStack var4 = this.containerMgr.getItemStack(var2);
        return var4 == null ? true : (var3 != null && this.keywordOrder[var1] != -1 ? (this.keywordOrder[var1] == this.keywordOrder[var2] ? (this.getItemID(var3) == this.getItemID(var4) ? (this.getItemDamage(var3) != this.getItemDamage(var4) ? (this.isItemStackDamageable(var3) ? this.getItemDamage(var3) > this.getItemDamage(var4) : this.getItemDamage(var3) < this.getItemDamage(var4)) : this.getStackSize(var3) > this.getStackSize(var4)) : this.getItemID(var3) > this.getItemID(var4)) : this.keywordOrder[var1] < this.keywordOrder[var2]) : false);
    }

    private int getItemOrder(ItemStack var1)
    {
        List var2 = this.tree.getItems(this.getItemID(var1), this.getItemDamage(var1));
        return var2 != null && var2.size() > 0 ? ((InvTweaksItemTreeItem)var2.get(0)).getOrder() : Integer.MAX_VALUE;
    }

    private void computeLineSortingRules(int var1, boolean var2)
    {
        this.rules = new Vector();
        Map var3 = this.computeContainerStats();
        ArrayList var4 = new ArrayList();
        int var5 = var3.size();
        int var6 = this.getContainerColumnSize(var1);
        int var9 = this.size;
        int var10 = 0;
        Integer var12;

        for (Iterator var11 = var3.values().iterator(); var11.hasNext(); var10 += var12.intValue())
        {
            var12 = (Integer)var11.next();
        }

        if (var5 != 0)
        {
            ArrayList var22 = new ArrayList(var3.keySet());
            boolean var23 = true;

            while (var23)
            {
                var23 = false;
                Iterator var13 = var22.iterator();

                while (var13.hasNext())
                {
                    InvTweaksItemTreeItem var14 = (InvTweaksItemTreeItem)var13.next();
                    Integer var15 = (Integer)var3.get(var14);

                    if (var15.intValue() > (var2 ? var1 : var6) && !var4.contains(var14))
                    {
                        var23 = true;
                        var4.add(var14);
                        var22.remove(var14);
                        break;
                    }
                }
            }

            Collections.sort(var22, Collections.reverseOrder());
            var4.addAll(var22);
            int var7;
            int var8;

            if (var2)
            {
                var8 = 1;
                var7 = var1 / ((var5 + var6 - 1) / var6);
            }
            else
            {
                var7 = 1;
                var8 = var6 / ((var5 + var1 - 1) / var1);
            }

            char var24 = 97;
            char var26 = (char)(var24 - 1 + var6);
            char var25 = 49;
            char var16 = (char)(var25 - 1 + var1);
            Iterator var17 = var4.iterator();

            while (var17.hasNext())
            {
                InvTweaksItemTreeItem var18 = (InvTweaksItemTreeItem)var17.next();
                int var19 = var7;
                int var20 = var8;

                while (((Integer)var3.get(var18)).intValue() > var20 * var19)
                {
                    if (var2)
                    {
                        if (var25 + var19 < var16)
                        {
                            var19 = var16 - var25 + 1;
                        }
                        else
                        {
                            if (var24 + var20 >= var26)
                            {
                                break;
                            }

                            ++var20;
                        }
                    }
                    else if (var24 + var20 < var26)
                    {
                        var20 = var26 - var24 + 1;
                    }
                    else
                    {
                        if (var25 + var19 >= var16)
                        {
                            break;
                        }

                        ++var19;
                    }
                }

                if (var2 && var25 + var19 == var16)
                {
                    ++var19;
                }
                else if (!var2 && var24 + var20 == var26)
                {
                    ++var20;
                }

                String var21 = var24 + "" + var25 + "-" + (char)(var24 - 1 + var20) + (char)(var25 - 1 + var19);

                if (!var2)
                {
                    var21 = var21 + 'v';
                }

                this.rules.add(new InvTweaksConfigSortingRule(this.tree, var21, var18.getName(), this.size, var1));
                var9 -= var20 * var19;
                var10 -= ((Integer)var3.get(var18)).intValue();

                if (var9 < var10)
                {
                    break;
                }

                if (var2)
                {
                    if (var25 + var19 + var7 <= var16 + 1)
                    {
                        var25 = (char)(var25 + var19);
                    }
                    else
                    {
                        var25 = 49;
                        var24 = (char)(var24 + var20);
                    }
                }
                else if (var24 + var20 + var8 <= var26 + 1)
                {
                    var24 = (char)(var24 + var20);
                }
                else
                {
                    var24 = 97;
                    var25 = (char)(var25 + var19);
                }

                if (var24 > var26 || var25 > var16)
                {
                    break;
                }
            }

            String var27;

            if (var2)
            {
                var27 = var26 + "1-a" + var16;
            }
            else
            {
                var27 = "a" + var16 + "-" + var26 + "1v";
            }

            this.rules.add(new InvTweaksConfigSortingRule(this.tree, var27, this.tree.getRootCategory().getName(), this.size, var1));
        }
    }

    private Map computeContainerStats()
    {
        HashMap var1 = new HashMap();
        HashMap var2 = new HashMap();

        for (int var3 = 0; var3 < this.size; ++var3)
        {
            ItemStack var4 = this.containerMgr.getItemStack(var3);

            if (var4 != null)
            {
                int var5 = this.getItemID(var4) * 100000 + (this.getMaxStackSize(var4) != 1 ? this.getItemDamage(var4) : 0);
                InvTweaksItemTreeItem var6 = (InvTweaksItemTreeItem)var2.get(Integer.valueOf(var5));

                if (var6 == null)
                {
                    var6 = (InvTweaksItemTreeItem)this.tree.getItems(this.getItemID(var4), this.getItemDamage(var4)).get(0);
                    var2.put(Integer.valueOf(var5), var6);
                    var1.put(var6, Integer.valueOf(1));
                }
                else
                {
                    var1.put(var6, Integer.valueOf(((Integer)var1.get(var6)).intValue() + 1));
                }
            }
        }

        return var1;
    }

    private int getContainerColumnSize(int var1)
    {
        return this.size / var1;
    }
}
