package dev.hikarishima.lightland.init.registrate;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;

import javax.annotation.Nullable;

public class BiomeRegistrate {

	public static void register() {
	}

	public static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category,
							   float temperature, float downfall,
							   MobSpawnSettings.Builder mobs, BiomeGenerationSettings.Builder settings,
							   @Nullable Music music) {
		return biome(precipitation, category,
				temperature, downfall, 4159204, 329011,
				mobs, settings, music);
	}

	public static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category,
							   float temperature, float downfall, int water_color, int water_fog_color,
							   MobSpawnSettings.Builder mobs, BiomeGenerationSettings.Builder settings,
							   @Nullable Music music) {
		return (new Biome.BiomeBuilder())
				.precipitation(precipitation)
				.biomeCategory(category)
				.temperature(temperature)
				.downfall(downfall)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(water_color)
						.waterFogColor(water_fog_color)
						.fogColor(12638463)
						.skyColor(calculateSkyColor(temperature))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
						.backgroundMusic(music).build())
				.mobSpawnSettings(mobs.build()).generationSettings(settings.build()).build();
	}

	private static int calculateSkyColor(float p_194844_) {
		float $$1 = p_194844_ / 3.0F;
		$$1 = Mth.clamp($$1, -1.0F, 1.0F);
		return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
	}

}
