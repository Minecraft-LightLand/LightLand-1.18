package dev.xkmc.cuisine.content.tools.firepit;

import dev.hikarishima.lightland.util.damage.DamageUtil;
import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.mult.OnClickBlockMethod;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.lcy0x1.block.one.EntityInsideBlockMethod;
import dev.xkmc.cuisine.content.tools.firepit.methods.FirePitBurnMethod;
import dev.xkmc.cuisine.content.tools.firepit.methods.FirePitPutMethod;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class FirePitBlock {

	public static final BlockEntityBlockMethod<FirePitStickBlockEntity> STICK = new BlockEntityBlockMethodImpl<>(
			CuisineBlocks.TE_STICK, FirePitStickBlockEntity.class);

	public static final BlockEntityBlockMethod<FirePitWokBlockEntity> WOK = new BlockEntityBlockMethodImpl<>(
			CuisineBlocks.TE_WOK, FirePitWokBlockEntity.class);

	public static final FirePitPutMethod PUT = new FirePitPutMethod();
	public static final FirePitBurnMethod BURN = new FirePitBurnMethod();


}
