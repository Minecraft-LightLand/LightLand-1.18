package dev.hikarishima.lightland.init.worldgenreg;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
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

import java.util.ArrayList;
import java.util.List;

public class StructureRegistrate {

	static final List<StructureEntry<?, ?, ?>> LIST = new ArrayList<>();

	public static class StructureEntry<S extends BaseStructureFeature<S, C>, P extends StructurePiece, C extends FeatureConfiguration> {

		final ResourceLocation feature_id, piece_id;
		public final StructurePieceType piece_type;
		final RegistryEntry<S> entry;
		final C feature_config;


		StructureEntry(String feature_name, String piece_name,
					   StructurePieceType.StructureTemplateType piece_gen,
					   NonNullSupplier<S> structure_gen,
					   C feature_config) {
			this.feature_id = new ResourceLocation(LightLand.MODID, feature_name);
			this.piece_id = new ResourceLocation(LightLand.MODID, piece_name);
			this.piece_type = Registry.register(Registry.STRUCTURE_PIECE, piece_id, piece_gen);
			this.entry = LightLand.REGISTRATE.simple(feature_name, StructureFeature.class, structure_gen);
			this.feature_config = feature_config;
			LIST.add(this);
		}

	}

	public static final StructureEntry<CKMazeFeature, CKMazePiece, NoneFeatureConfiguration> CKMAZE =
			new StructureEntry<>("cursedknight_maze", "cursedknight_maze_piece",
					CKMazePiece::new, () -> new CKMazeFeature(NoneFeatureConfiguration.CODEC),
					NoneFeatureConfiguration.INSTANCE);

	public static void register() {
	}

}
