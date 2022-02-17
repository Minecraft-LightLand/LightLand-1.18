package dev.hikarishima.lightland.content.magic.ritual;

import com.mojang.datafixers.util.Pair;
import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.lcy0x1.base.BaseRecipe;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SerialClass
public class AbstractRitualRecipe<R extends AbstractRitualRecipe<R>> extends BaseRecipe<R, AbstractRitualRecipe<?>, RitualCore.Inv> {

    @SerialClass.SerialField
    public Entry core;

    @SerialClass.SerialField(generic = Entry.class)
    public ArrayList<Entry> side;

    public AbstractRitualRecipe(ResourceLocation id, RecType<R, AbstractRitualRecipe<?>, RitualCore.Inv> fac) {
        super(id, fac);
    }

    @Override
    public boolean matches(RitualCore.Inv inv, Level world) {
        if (!core.test(inv.getItem(5)))
            return false;
        List<Entry> temp = side.stream().filter(e -> !e.input.isEmpty()).collect(Collectors.toList());
        for (int i = 0; i < 9; i++) {
            if (i == 5)
                continue;
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                Optional<Entry> entry = temp.stream().filter(e -> e.test(stack)).findFirst();
                if (!entry.isPresent())
                    return false;
                temp.remove(entry.get());
            }
        }
        return temp.isEmpty();
    }

    @Deprecated
    @Override
    public ItemStack assemble(RitualCore.Inv inv) {
        if (!core.test(inv.getItem(5)))
            return ItemStack.EMPTY;
        ItemStack ans = core.output.copy();
        List<Entry> temp = side.stream().filter(e -> !e.input.isEmpty()).collect(Collectors.toList());
        for (int i = 0; i < 9; i++) {
            if (i == 5)
                continue;
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                Optional<Entry> entry = temp.stream().filter(e -> e.test(stack)).findFirst();
                if (!entry.isPresent())
                    return ItemStack.EMPTY;
                temp.remove(entry.get());
                inv.setItem(i, entry.get().getOutput(stack));
            }
        }
        return ans;
    }

    public void assemble(RitualCore.Inv inv, int level) {
        inv.setItem(5, assemble(inv));
    }

    @Override
    public boolean canCraftInDimensions(int r, int c) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return core.output;
    }

    @SerialClass
    public static class Entry {

        @SerialClass.SerialField
        public ItemStack input = ItemStack.EMPTY;

        @SerialClass.SerialField
        public ItemStack output = ItemStack.EMPTY;

        public boolean test(ItemStack stack) {
            if (input.getItem() == Items.SHULKER_BOX &&
                    input.getTagElement("BlockEntityTag") != null) {
                if (stack.getItem() instanceof BlockItem &&
                        ((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock &&
                        stack.getTagElement("BlockEntityTag") != null) {
                    NonNullList<ItemStack> list_a = NonNullList.withSize(27, ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(input.getOrCreateTagElement("BlockEntityTag"), list_a);
                    NonNullList<ItemStack> list_b = NonNullList.withSize(27, ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(stack.getOrCreateTagElement("BlockEntityTag"), list_b);
                    Pair<Item, Integer> stack_a = aggregate(list_a);
                    Pair<Item, Integer> stack_b = aggregate(list_b);
                    if (stack_a == null || stack_b == null || stack_a.getFirst() != stack_b.getFirst()) {
                        return false;
                    }
                    return stack_b.getSecond() >= stack_a.getSecond();
                }
                return false;
            }
            if (input.getItem() == Items.ENCHANTED_BOOK) {
                if (stack.getItem() != input.getItem())
                    return false;
                for (Map.Entry<Enchantment, Integer> ent : EnchantmentHelper.getEnchantments(input).entrySet()) {
                    Integer i = EnchantmentHelper.getEnchantments(stack).get(ent.getKey());
                    if (i == null || i < ent.getValue())
                        return false;
                }
                return true;
            }
            return stack.getItem() == input.getItem();
        }

        @SuppressWarnings("ConstantConditions")
        public ItemStack getOutput(ItemStack stack) {
            if (input.getItem() == Items.SHULKER_BOX &&
                    input.getTagElement("BlockEntityTag") != null) {
                if (stack.getItem() instanceof BlockItem &&
                        ((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock &&
                        stack.getTagElement("BlockEntityTag") != null) {
                    NonNullList<ItemStack> list_a = NonNullList.withSize(27, ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(input.getOrCreateTagElement("BlockEntityTag"), list_a);
                    NonNullList<ItemStack> list_b = NonNullList.withSize(27, ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(stack.getOrCreateTagElement("BlockEntityTag"), list_b);
                    Pair<Item, Integer> stack_a = aggregate(list_a);
                    Pair<Item, Integer> stack_b = aggregate(list_b);
                    ItemStack ans = stack.copy();
                    ContainerHelper.saveAllItems(ans.getOrCreateTagElement("BlockEntityTag"), fill(stack_b.getFirst(), stack_b.getSecond() - stack_a.getSecond()));
                    return ans;
                }
            }
            return output.copy();
        }

    }

    @Nullable
    public ResourceLocation getMagic() {
        return null;
    }

    public int getLevel(int cost) {
        return 1;
    }

    public int getNextLevel(int cost) {
        return 0;
    }

    @Nullable
    private static Pair<Item, Integer> aggregate(NonNullList<ItemStack> list) {
        Item item = null;
        int count = 0;
        for (ItemStack stack_b : list) {
            if (stack_b.isEmpty()) {
                continue;
            }
            if (item == null) {
                item = stack_b.getItem();
                count = stack_b.getCount();
            } else {
                if (item != stack_b.getItem()) {
                    return null;
                }
                count += stack_b.getCount();
            }
        }
        return Pair.of(item, count);
    }

    @SuppressWarnings("deprecation")
    public static NonNullList<ItemStack> fill(Item item, int count) {
        NonNullList<ItemStack> list = NonNullList.withSize(27, ItemStack.EMPTY);
        for (int i = 0; i < 27; i++) {
            if (count == 0)
                break;
            int c = Math.min(count, item.getMaxStackSize());
            list.set(i, new ItemStack(item, c));
            count -= c;
        }
        return list;
    }

}
