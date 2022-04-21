package dev.xkmc.cuisine.content.tools.base;

import dev.xkmc.cuisine.content.tools.base.methods.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Random;

public class CuisineUtil {

	public static final DumpInventory DUMP = new DumpInventory();
	public static final AddOneItem ADD = new AddOneItem();
	public static final FireClick FIRE = new FireClick();
	public static final LidClick<?> LID = new LidClick<>();
	public static final TakeResultClick<?> TAKE = new TakeResultClick<>();
	public static final StepClick<?> STEP = new StepClick<>();
	public static final TimeProcessing TIME = new TimeProcessing();

	@Nonnull
	public static Ingredient getContainer(ItemStack result) {
		ItemStack container = result.getItem().getContainerItem(result);
		if (!container.isEmpty()) return Ingredient.of(container);
		if (result.getItem() instanceof BowlFoodItem) {
			return Ingredient.of(Items.BOWL);
		}
		return Ingredient.EMPTY;
	}

	public static void spawnParticle(Level world, BlockPos pos, Random r) {
		double d0 = pos.getX() + 1 - r.nextFloat() * 0.5F;
		double d1 = pos.getY() + 1 - r.nextFloat() * 0.5F;
		double d2 = pos.getZ() + 1 - r.nextFloat() * 0.5F;
		if (r.nextInt(5) == 0) {
			world.addParticle(ParticleTypes.END_ROD, d0, d1, d2, r.nextGaussian() * 0.005D, r.nextGaussian() * 0.005D, r.nextGaussian() * 0.005D);
		}
	}

	public static void placeBack(Player player, ItemStack stack) {
		player.getInventory().placeItemBackInInventory(stack);
	}


	public static void hurtAndBreak(Player pl, ItemStack stack, InteractionHand hand) {
		stack.hurtAndBreak(1, pl, (player) -> player.broadcastBreakEvent(hand));
	}
}
