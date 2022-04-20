package dev.xkmc.cuisine.content.tools.mortar;

import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.lcy0x1.block.one.ShapeBlockMethod;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MortarBlock implements OnClickBlockMethod, ShapeBlockMethod {

	public static final BlockEntityBlockMethod<MortarBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_MORTAR, MortarBlockEntity.class);

	private static final VoxelShape SHAPE = Block.box(3D, 0D, 3D, 13.0D, 5.0D, 13.0D);

	@Override
	public InteractionResult onClick(BlockState bs, Level w, BlockPos pos, Player pl, InteractionHand h, BlockHitResult r) {
		ItemStack stack = pl.getItemInHand(h);
		MortarBlockEntity te = Objects.requireNonNull((MortarBlockEntity) w.getBlockEntity(pos));
		if (pl.isShiftKeyDown()) {
			if (!w.isClientSide())
				te.dumpInventory();
			return InteractionResult.SUCCESS;
		}

		if (!stack.isEmpty()) {
			ItemStack copy = stack.copy();
			copy.setCount(1);
			ItemStack remain = te.inventory.addItem(copy);
			if (remain.isEmpty()) stack.shrink(1);
			return InteractionResult.SUCCESS;
		} else {
			if (te.step())
				return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}
}
