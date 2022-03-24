package dev.hikarishima.lightland.content.questline.world;

import dev.hikarishima.lightland.init.registrate.EntityRegistrate;
import dev.hikarishima.lightland.init.worldgenreg.WorldGenRegistrate;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

public class LaylineCaveBiome {

	public static Biome lushCaves() {
		MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
		addMobs(mobs);
		BiomeGenerationSettings.Builder settings = new BiomeGenerationSettings.Builder();
		addFeatures(settings);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_LUSH_CAVES);
		return WorldGenRegistrate.biome(Biome.Precipitation.RAIN, Biome.BiomeCategory.UNDERGROUND,
				0.5F, 0.5F, mobs, settings, music);
	}

	private static void addMobs(MobSpawnSettings.Builder mobs) {
		mobs.addSpawn(MobCategory.AXOLOTLS, new MobSpawnSettings.SpawnerData(EntityType.AXOLOTL, 10, 4, 6));
		mobs.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));
		mobs.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
		mobs.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 4, 6));

		mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistrate.ET_LAYLINE_ZOMBIE.get(), 100, 4, 4));
		mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityRegistrate.ET_LAYLINE_SKELETON.get(), 100, 4, 4));
		mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4));
		mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 100, 4, 4));
		mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4));
		mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
		mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4));
	}

	private static void addFeatures(BiomeGenerationSettings.Builder settings) {

		BiomeDefaultFeatures.addDefaultCarversAndLakes(settings);
		BiomeDefaultFeatures.addDefaultCrystalFormations(settings);
		BiomeDefaultFeatures.addDefaultMonsterRoom(settings);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(settings);
		BiomeDefaultFeatures.addDefaultSprings(settings);
		BiomeDefaultFeatures.addSurfaceFreezing(settings);
		BiomeDefaultFeatures.addPlainGrass(settings);
		BiomeDefaultFeatures.addDefaultOres(settings);
		BiomeDefaultFeatures.addLushCavesSpecialOres(settings);
		BiomeDefaultFeatures.addDefaultSoftDisks(settings);

		settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CEILING_VEGETATION);
		settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.ROOTED_AZALEA_TREE);
		settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.SPORE_BLOSSOM);

		settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CAVE_VINES);
		settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CLAY);
		settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_VEGETATION);
		settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CLASSIC_VINES);
	}

}
