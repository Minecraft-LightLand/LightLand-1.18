package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.hikarishima.lightland.content.common.particle.EmeraldParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class ParticleRegistrate {

    public static final RegistryEntry<SimpleParticleType> EMERALD = REGISTRATE.simple("emerald", ParticleType.class, () -> new SimpleParticleType(false));

    public static void register() {

    }

    @OnlyIn(Dist.CLIENT)
    public static void registerClient() {
        Minecraft.getInstance().particleEngine.register(EMERALD.get(), new EmeraldParticle.Factory());
    }

}
