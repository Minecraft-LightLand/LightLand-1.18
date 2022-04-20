package dev.xkmc.cuisine.content.tools.jar;

import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.AnimateTickBlockMethod;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.xkmc.cuisine.content.tools.base.CuisineUtil;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.Objects;
import java.util.Random;

public class JarBlock {

	public static final BlockEntityBlockMethod<JarBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_JAR, JarBlockEntity.class);

}
