package dev.xkmc.cuisine.content.tools.basin;

import dev.hikarishima.lightland.util.damage.DamageUtil;
import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.AnimateTickBlockMethod;
import dev.lcy0x1.block.mult.FallOnBlockMethod;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.lcy0x1.block.one.EntityInsideBlockMethod;
import dev.lcy0x1.block.one.ShapeBlockMethod;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public class BasinBlock implements OnClickBlockMethod, FallOnBlockMethod, AnimateTickBlockMethod, ShapeBlockMethod, EntityInsideBlockMethod {

	private static final VoxelShape OUTSIDE = Block.box(0D, 0D, 0D, 16.0D, 8.0D, 16.0D);
	private static final VoxelShape INSIDE = Block.box(1.0D, 1.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	protected static final VoxelShape SHAPE = Shapes.join(OUTSIDE, INSIDE, BooleanOp.ONLY_FIRST);

	public static final BlockEntityBlockMethod<BasinBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_BASIN, BasinBlockEntity.class);

	@Override
	public InteractionResult onClick(BlockState bs, Level w, BlockPos pos, Player pl, InteractionHand h, BlockHitResult r) {
		ItemStack stack = pl.getItemInHand(h);
		BasinBlockEntity te = Objects.requireNonNull((BasinBlockEntity) w.getBlockEntity(pos));
		if (pl.isShiftKeyDown()) {
			if (!w.isClientSide())
				te.dumpInventory();
			return InteractionResult.SUCCESS;
		}

		if (!stack.isEmpty()) {
			LazyOptional<IFluidHandlerItem> opt = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
			if (opt.resolve().isPresent()) {
				IFluidHandlerItem item = opt.resolve().get();
				FluidStack fluidStack = item.getFluidInTank(0);
				if (fluidStack.isEmpty() || CuisineTags.AllFluidTags.JAR_ACCEPT.matches(fluidStack.getFluid())) {
					return FluidUtil.interactWithFluidHandler(pl, h, w, pos, r.getDirection()) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
				}
			}
			ItemStack copy = stack.copy();
			copy.setCount(1);
			ItemStack remain = te.inventory.addItem(copy);
			if (remain.isEmpty()) stack.shrink(1);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float height) {
		if (level.getBlockEntity(pos) instanceof BasinBlockEntity te && te.step > 0) {
			if (level.isClientSide()) {
				Random r = level.getRandom();
				double d0 = pos.getX() + 1 - r.nextFloat() * 0.5F;
				double d1 = pos.getY() + 1 - r.nextFloat() * 0.5F;
				double d2 = pos.getZ() + 1 - r.nextFloat() * 0.5F;
				level.addParticle(ParticleTypes.END_ROD, d0, d1, d2, r.nextGaussian() * 0.005D, r.nextGaussian() * 0.005D, r.nextGaussian() * 0.005D);
			}
			te.step();
		}
		return true;
	}


	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random r) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof BasinBlockEntity jar) {
			if (jar.max_time > 0) {
				double d0 = pos.getX() + 1 - r.nextFloat() * 0.5F;
				double d1 = pos.getY() + 1 - r.nextFloat() * 0.5F;
				double d2 = pos.getZ() + 1 - r.nextFloat() * 0.5F;
				if (r.nextInt(5) == 0) {
					world.addParticle(ParticleTypes.END_ROD, d0, d1, d2, r.nextGaussian() * 0.005D, r.nextGaussian() * 0.005D, r.nextGaussian() * 0.005D);
				}
			}
		}
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
