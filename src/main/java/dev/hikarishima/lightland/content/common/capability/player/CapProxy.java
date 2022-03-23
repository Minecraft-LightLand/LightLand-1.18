package dev.hikarishima.lightland.content.common.capability.player;

import dev.lcy0x1.base.Proxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CapProxy {

	@OnlyIn(Dist.CLIENT)
	public static LLPlayerData getHandler() {
		return LLPlayerData.get(Proxy.getClientPlayer());
	}

}
