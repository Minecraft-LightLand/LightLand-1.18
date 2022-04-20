package dev.xkmc.cuisine.init;

import dev.xkmc.cuisine.content.fruits.CuisineLeaveBlock;
import dev.xkmc.cuisine.init.data.CuisineTreeType;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class CuisineRendering {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onBlockColorsInit(ColorHandlerEvent.Block event) {
		BlockColors blockColors = event.getBlockColors();
		BlockColor color = (state, tint, pos, index) -> {
			if (state.getBlock() instanceof CuisineLeaveBlock leave) {
				return leave.getType().color;
			}
			return -1;
		};
		for (CuisineTreeType type : CuisineTreeType.values()) {
			blockColors.register(color, type.leave.get());
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemColorsInit(ColorHandlerEvent.Item event) {
		ItemColors itemColors = event.getItemColors();
		ItemColor color = (stack, index) -> {
			if (stack.getItem() instanceof BlockItem block && block.getBlock() instanceof CuisineLeaveBlock leave)
				return leave.getType().color;
			return -1;
		};
		for (CuisineTreeType type : CuisineTreeType.values()) {
			itemColors.register(color, type.leave.get());
		}
	}

}