package dev.hikarishima.lightland.content.common.item.misc;

import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.l2library.serial.ExceptionHandler;
import dev.xkmc.l2library.serial.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class RecordPearl extends Item {

	public RecordPearl(Properties props) {
		super(props.stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		LLPlayerData handler = LLPlayerData.get(player);
		if (isFoil(stack)) {
			if (handler.abilityPoints.getProfession() != null)
				return InteractionResultHolder.fail(stack);
			CompoundTag tag = stack.getTagElement("player_cap");
			handler.reset(LLPlayerData.Reset.FOR_INJECT);
			ExceptionHandler.run(() -> TagCodec.fromTag(tag, LLPlayerData.class, handler, f -> true));
			handler.reInit();
			stack.removeTagKey("player_cap");
			return InteractionResultHolder.success(stack);
		} else {
			if (handler.abilityPoints.getProfession() == null)
				return InteractionResultHolder.fail(stack);
			CompoundTag tag = stack.getOrCreateTagElement("player_cap");
			TagCodec.toTag(tag, handler);
			handler.reset(LLPlayerData.Reset.ALL);
			return InteractionResultHolder.success(stack);
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getTagElement("player_cap") != null;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		if (isFoil(stack)) {
			LLPlayerData handler = TagCodec.fromTag(stack.getTagElement("player_cap"), LLPlayerData.class);
			list.add(handler.abilityPoints.profession.getDesc());
		}
		super.appendHoverText(stack, world, list, flag);
	}
}
