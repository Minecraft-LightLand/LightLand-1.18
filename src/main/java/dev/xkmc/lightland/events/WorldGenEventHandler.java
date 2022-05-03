package dev.xkmc.lightland.events;

import dev.xkmc.lightland.init.registrate.LightlandEntities;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenEventHandler {

	@SubscribeEvent
	public static void onBiomeRegister(BiomeLoadingEvent event) {
		if (event.getName().equals(Biomes.LUSH_CAVES.getRegistryName())) {
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_LAYLINE_ZOMBIE.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_LAYLINE_SKELETON.get(), 200, 4, 4));
		}
		if (event.getName().equals(Biomes.DARK_FOREST.getRegistryName())) {
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_CURSED_KNIGHT.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_CURSED_ARCHER.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_CURSED_SHIELD.get(), 200, 4, 4));
		}
		if (event.getName().equals(Biomes.SWAMP.getRegistryName())) {
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_POTION_SLIME.get(), 800, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_CARPET_SLIME.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_STONE_SLIME.get(), 200, 4, 4));
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					LightlandEntities.ET_VINE_SLIME.get(), 200, 4, 4));
		}

	}

	@SubscribeEvent
	public static void onServerStart(ServerAboutToStartEvent event) {
	}

}
