package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public interface SpecialDropBlockMethod extends SingletonBlockMethod {

	List<ItemStack> getDrops(BlockState state, LootContext.Builder builder);

}
