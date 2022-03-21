package dev.hikarishima.lightland.events;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.hikarishima.lightland.init.registrate.EntityRegistrate;
import dev.hikarishima.lightland.init.registrate.WorldGenRegistrate;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenEventHandler {

	@SubscribeEvent
	public static void onBiomeRegister(BiomeLoadingEvent event) {
		if (event.getName().equals(Biomes.LUSH_CAVES.getRegistryName())) {
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_LAYLINE_ZOMBIE.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_LAYLINE_SKELETON.get(), 200, 4, 4));
		}
		if (event.getName().equals(Biomes.DARK_FOREST.getRegistryName())) {
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_CURSED_KNIGHT.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_CURSED_ARCHER.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_CURSED_SHIELD.get(), 200, 4, 4));
		}
		if (event.getName().equals(Biomes.SWAMP.getRegistryName())) {
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_POTION_SLIME.get(), 800, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_CARPET_SLIME.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_STONE_SLIME.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityRegistrate.ET_VINE_SLIME.get(), 200, 4, 4));
		}


		ResourceKey<ConfiguredStructureFeature<?, ?>> key = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, WorldGenRegistrate.ID_CKMAZE);

	}

}
