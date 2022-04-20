package dev.xkmc.cuisine.content.tools.pan;

import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.block.mult.DefaultStateBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.lcy0x1.block.one.LightBlockMethod;
import dev.xkmc.cuisine.content.tools.base.CuisineUtil;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class PanBlock implements CreateBlockStateBlockMethod, DefaultStateBlockMethod {

	public static final BlockEntityBlockMethod<PanBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_PAN, PanBlockEntity.class);

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.SIGNAL_FIRE, BlockStateProperties.OPEN);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.SIGNAL_FIRE, false).setValue(BlockStateProperties.OPEN, false);
	}

}
