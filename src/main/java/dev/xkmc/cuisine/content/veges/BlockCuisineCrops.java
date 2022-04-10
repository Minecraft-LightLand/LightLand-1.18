package dev.xkmc.cuisine.content.veges;

import dev.xkmc.cuisine.init.data.CuisineTemplates;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockCuisineCrops extends CropBlock {

	private static final ThreadLocal<CuisineTemplates.Veges> TEMP = new ThreadLocal<>();

	private static Properties warp(CuisineTemplates.Veges type, Properties props) {
		TEMP.set(type);
		return props;
	}

	private CuisineTemplates.Veges type;

	public BlockCuisineCrops(CuisineTemplates.Veges type, Properties props) {
		super(warp(type, props));
		getType();
	}

	public CuisineTemplates.Veges getType() {
		if (this.type != null) {
			return type;
		}
		type = TEMP.get();
		TEMP.set(null);
		return type;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return getType().getEntry().get();
	}

	@Override
	public IntegerProperty getAgeProperty() {
		return getType().getAge();
	}

	@Override
	public int getMaxAge() {
		return getType().getMaxAge();
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty());
	}

}