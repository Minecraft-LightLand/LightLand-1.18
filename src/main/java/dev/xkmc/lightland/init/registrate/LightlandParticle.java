package dev.xkmc.lightland.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.lightland.content.common.particle.EmeraldParticle;
import dev.xkmc.lightland.init.LightLand;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LightlandParticle {

	public static final RegistryEntry<SimpleParticleType> EMERALD = LightLand.REGISTRATE.simple("emerald", ParticleType.class, () -> new SimpleParticleType(false));

	public static void register() {

	}

	@OnlyIn(Dist.CLIENT)
	public static void registerClient() {
		Minecraft.getInstance().particleEngine.register(EMERALD.get(), new EmeraldParticle.Factory());
	}

}
