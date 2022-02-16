package dev.hikarishima.lightland.content.magic.ritual;

import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class BasicMagicRitualRecipe extends AbstractMagicRitualRecipe<BasicMagicRitualRecipe> {

    public BasicMagicRitualRecipe(ResourceLocation id) {
        super(id, RecipeRegistrate.RS_DEF.get());
    }
}
