package dev.xkmc.cuisine.init.data;

import dev.xkmc.cuisine.content.fruits.CuisineTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;

import java.util.Locale;

public enum CuisineTreeType {
	CITRON, GRAPEFRUIT, LEMON, LIME, MANDARIN, ORANGE, POMELO;

	private final CuisineTreeGrower grower = new CuisineTreeGrower(this);

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public AbstractTreeGrower getGrower() {
		return grower;
	}
}
