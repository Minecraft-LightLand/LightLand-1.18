package dev.hikarishima.lightland.content.magic.ritual;

import dev.hikarishima.lightland.init.registrate.LightlandRecipe;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class BasicRitualRecipe extends AbstractRitualRecipe<BasicRitualRecipe> {

	public BasicRitualRecipe(ResourceLocation id) {
		super(id, LightlandRecipe.RS_DEF.get());
	}
}
