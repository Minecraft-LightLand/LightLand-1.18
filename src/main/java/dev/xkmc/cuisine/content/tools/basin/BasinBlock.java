package dev.xkmc.cuisine.content.tools.basin;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.mult.FallOnBlockMethod;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2library.block.one.EntityInsideBlockMethod;
import dev.xkmc.l2library.block.one.ShapeBlockMethod;
import dev.xkmc.cuisine.content.tools.base.CuisineUtil;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.util.DamageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BasinBlock implements FallOnBlockMethod, ShapeBlockMethod, EntityInsideBlockMethod {

	private static final VoxelShape OUTSIDE = Block.box(0D, 0D, 0D, 16.0D, 8.0D, 16.0D);
	private static final VoxelShape INSIDE = Block.box(1.0D, 1.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	protected static final VoxelShape SHAPE = Shapes.join(OUTSIDE, INSIDE, BooleanOp.ONLY_FIRST);

	public static final BlockEntityBlockMethod<BasinBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_BASIN, BasinBlockEntity.class);

	@Override
	public boolean fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float height) {
		if (level.getBlockEntity(pos) instanceof BasinBlockEntity te && te.step()) {
			if (level.isClientSide()) {
				CuisineUtil.spawnParticle(level, pos, level.getRandom());
			}
		}
		return true;
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity le && level.getBlockEntity(pos) instanceof BasinBlockEntity te) {
			if (te.checkBlockBelow()) {
				DamageUtil.dealDamage(le, DamageSource.IN_FIRE, 1);
			}
		}
	}

}
