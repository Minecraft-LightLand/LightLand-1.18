package dev.hikarishima.lightland.init.worldgenreg;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.questline.world.structure.BaseStructureFeature;
import dev.hikarishima.lightland.content.questline.world.structure.curseknight.CKMazeFeature;
import dev.hikarishima.lightland.content.questline.world.structure.curseknight.CKMazePiece;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureRegistrate {

	static final List<StructureEntry<?, ?, ?>> LIST = new ArrayList<>();

	static class StructureEntry<S extends BaseStructureFeature<S, C>, P extends StructurePiece, C extends FeatureConfiguration> {

		final ResourceLocation feature_id, piece_id;
		final StructurePieceType piece_type;
		final RegistryEntry<S> entry;
		final Supplier<List<ResourceKey<Biome>>> biomes;
		final StructureFeatureConfiguration config;
		final C feature_config;
		ConfiguredStructureFeature<C, S> configured;


		StructureEntry(String feature_name, String piece_name,
					   StructurePieceType.StructureTemplateType piece_gen,
					   NonNullSupplier<S> structure_gen,
					   C feature_config,
					   StructureFeatureConfiguration config,
					   Supplier<List<ResourceKey<Biome>>> biomes) {
			this.feature_id = new ResourceLocation(LightLand.MODID, feature_name);
			this.piece_id = new ResourceLocation(LightLand.MODID, piece_name);
			this.piece_type = Registry.register(Registry.STRUCTURE_PIECE, piece_id, piece_gen);
			this.entry = LightLand.REGISTRATE.simple(feature_name, StructureFeature.class, structure_gen);
			this.feature_config = feature_config;
			this.config = config;
			this.biomes = biomes;
			LIST.add(this);
		}

		void configure() {
			configured = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
					feature_id, entry.get().configured(feature_config));
		}

		ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> mapBiome() {
			return multimapOf(configured, biomes.get());
		}

		private static <K, V> ImmutableMultimap<K, V> multimapOf(K key, Collection<V> values) {
			ImmutableMultimap.Builder<K, V> builder = ImmutableMultimap.builder();
			builder.putAll(key, values);
			return builder.build();
		}

	}

	public static final StructureEntry<CKMazeFeature, CKMazePiece, NoneFeatureConfiguration> CKMAZE =
			new StructureEntry<>("cursedknight_maze", "cursedknight_mae_piece",
					CKMazePiece::new, () -> new CKMazeFeature(NoneFeatureConfiguration.CODEC),
					NoneFeatureConfiguration.INSTANCE,
					new StructureFeatureConfiguration(80, 10, 26243534),
					() -> List.of(Biomes.PLAINS, Biomes.DARK_FOREST, Biomes.OCEAN));

	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			LIST.forEach(entry -> StructureFeature.STRUCTURES_REGISTRY.put(entry.feature_id.toString(), entry.entry.get()));
			ImmutableMap.Builder<StructureFeature<?>, StructureFeatureConfiguration> defaultSettings = ImmutableMap.builder();
			Set<StructureFeature<?>> ignore = LIST.stream().map(e -> e.entry.get()).collect(Collectors.toSet());
			StructureSettings.DEFAULTS.entrySet().stream().filter(entry -> !ignore.contains(entry.getKey())).forEach(defaultSettings::put);
			LIST.forEach(e -> defaultSettings.put(e.entry.get(), e.config));
			for (NoiseGeneratorSettings dimensionSettings : BuiltinRegistries.NOISE_GENERATOR_SETTINGS) {
				LIST.forEach(e -> dimensionSettings.structureSettings().structureConfig().put(e.entry.get(), e.config));
			}
			StructureSettings.DEFAULTS = defaultSettings.build();
			LIST.forEach(StructureEntry::configure);
		});
	}

	/**
	 * Adds the settings to all default dimension settings
	 */
	public static void addDefaultStructureBiomes() {
		for (NoiseGeneratorSettings dimensionSettings : BuiltinRegistries.NOISE_GENERATOR_SETTINGS) {
			StructureSettings settings = dimensionSettings.structureSettings();
			if (!settings.configuredStructures.containsKey(CKMAZE.entry.get())) {
				ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> builder = ImmutableMap.builder();
				builder.putAll(settings.configuredStructures);
				LIST.forEach(e -> builder.put(e.entry.get(), e.mapBiome()));
				settings.configuredStructures = builder.build();
			}
		}
	}

	/**
	 * Gets all biomes that are not registered with the biome dictionary and matches the given predicate
	 */
	private static Stream<ResourceKey<Biome>> getBiomes(Registry<Biome> registry, Predicate<Biome.BiomeCategory> predicate) {
		return registry.entrySet().stream().filter(biome -> !BiomeDictionary.hasAnyType(biome.getKey()) &&
				predicate.test(biome.getValue().getBiomeCategory())).map(Map.Entry::getKey);
	}

	public static void register() {
	}

}
