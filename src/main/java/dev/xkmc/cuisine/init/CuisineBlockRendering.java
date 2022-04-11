package dev.xkmc.cuisine.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.cuisine.content.fruits.CuisineLeaveBlock;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class CuisineBlockRendering {

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
		for (BlockEntry<?> entry : CuisineBlocks.LEAVE) {
			blockColors.register(color, entry.get());
		}
	}

}