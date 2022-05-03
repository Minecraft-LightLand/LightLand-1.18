package dev.hikarishima.lightland.init.worldgenreg;

import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.questline.world.structure.BaseStructureFeature;
import dev.hikarishima.lightland.content.questline.world.structure.curseknight.CKMazeFeature;
import dev.hikarishima.lightland.content.questline.world.structure.curseknight.CKMazePiece;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class StructureRegistrate {

	static final List<StructureEntry<?, ?, ?>> LIST = new ArrayList<>();

	public static class StructureEntry<S extends BaseStructureFeature<S, C>, P extends StructurePiece, C extends FeatureConfiguration> {

		public StructurePieceType piece_type;

		final ResourceLocation feature_id, piece_id;
		final RegistryEntry<S> entry;
		final C feature_config;

		private final StructurePieceType.StructureTemplateType piece_gen;


		StructureEntry(String feature_name, String piece_name,
					   StructurePieceType.StructureTemplateType piece_gen,
					   NonNullSupplier<S> structure_gen,
					   C feature_config) {
			this.feature_id = new ResourceLocation(LightLand.MODID, feature_name);
			this.piece_id = new ResourceLocation(LightLand.MODID, piece_name);
			this.entry = LightLand.REGISTRATE.simple(feature_name, StructureFeature.class, structure_gen);
			this.feature_config = feature_config;
			this.piece_gen = piece_gen;
			LIST.add(this);
		}

		void registerType() {
			this.piece_type = Registry.register(Registry.STRUCTURE_PIECE, piece_id, piece_gen);
		}

	}

	public static final StructureEntry<CKMazeFeature, CKMazePiece, NoneFeatureConfiguration> CKMAZE =
			new StructureEntry<>("cursedknight_maze", "cursedknight_maze_piece",
					CKMazePiece::new, () -> new CKMazeFeature(NoneFeatureConfiguration.CODEC),
					NoneFeatureConfiguration.INSTANCE);

	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> LIST.forEach(StructureEntry::registerType));
	}

	public static void register() {
	}

}
