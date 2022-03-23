package dev.hikarishima.lightland.content.questline.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

public class BaseStructureFeature<S extends BaseStructureFeature<S, C>, C extends FeatureConfiguration> extends StructureFeature<C> {

	public BaseStructureFeature(Codec<C> codec, PieceGeneratorSupplier<C> gen) {
		super(codec, gen);
	}

	public BaseStructureFeature(Codec<C> codec, PieceGeneratorSupplier<C> gen, PostPlacementProcessor post) {
		super(codec, gen, post);
	}

	public ConfiguredStructureFeature<C, S> configured(C config) {
		return new ConfiguredStructureFeature<>(getThis(), config);
	}

	@SuppressWarnings("unchecked")
	public S getThis() {
		return (S) this;
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

}
