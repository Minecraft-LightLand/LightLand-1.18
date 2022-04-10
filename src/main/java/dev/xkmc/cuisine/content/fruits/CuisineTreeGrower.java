package dev.xkmc.cuisine.content.fruits;

import dev.xkmc.cuisine.init.data.CuisineTreeType;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Random;

public class CuisineTreeGrower extends AbstractTreeGrower {

	public final CuisineTreeType type;

	public CuisineTreeGrower(CuisineTreeType type) {
		this.type = type;
	}

	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random p_204329_, boolean p_204330_) {
		if (p_204329_.nextInt(10) == 0) {
			return p_204330_ ? TreeFeatures.FANCY_OAK_BEES_005 : TreeFeatures.FANCY_OAK;
		} else {
			return p_204330_ ? TreeFeatures.OAK_BEES_005 : TreeFeatures.OAK;
		}
	}
}
