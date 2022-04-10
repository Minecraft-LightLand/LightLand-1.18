package dev.xkmc.cuisine.content.veges;

import dev.xkmc.cuisine.init.data.CuisineTemplates;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

@SuppressWarnings("deprecation")
public class BlockCuisineCrops extends CropBlock {

	public final CuisineTemplates.Veges type;

	public BlockCuisineCrops(CuisineTemplates.Veges type, Properties props) {
		super(props);
		this.type = type;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return type.getEntry().get();
	}
}