package dev.hikarishima.lightland.init.registrate;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.hikarishima.lightland.content.questline.world.structure.CKMazeFeature;
import dev.hikarishima.lightland.content.questline.world.structure.CKMazePieces;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class WorldGenRegistrate {

	public static final ResourceLocation ID_CKMAZE = new ResourceLocation(LightLand.MODID, "cursedknight_maze");
	public static final StructurePieceType SPT_CKMAZE = CKMazePieces.Piece::new;
	public static final StructureFeature<NoneFeatureConfiguration> SF_CKMAZE = new CKMazeFeature(NoneFeatureConfiguration.CODEC);
	public static final ConfiguredStructureFeature<?, ?> CSF_CKMAZE = SF_CKMAZE.configured(NoneFeatureConfiguration.INSTANCE);


	public static void onInit() {
		Registry.register(Registry.STRUCTURE_PIECE, ID_CKMAZE, SPT_CKMAZE);

		StructureFeature.STRUCTURES_REGISTRY.put(ID_CKMAZE.toString(), SF_CKMAZE);
		StructureFeature.STEP.put(SF_CKMAZE, GenerationStep.Decoration.SURFACE_STRUCTURES);
		LightLand.REGISTRATE.simple("cursedknight_maze", StructureFeature.class, () -> SF_CKMAZE);
		StructureFeatures.register(ID_CKMAZE.toString(), CSF_CKMAZE);

	}

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
