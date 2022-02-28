package dev.hikarishima.lightland.content.magic.products.instance;

import com.hikarishima.lightland.magic.MagicRegistry;
import com.hikarishima.lightland.magic.capabilities.MagicHandler;
import com.hikarishima.lightland.magic.products.MagicProduct;
import com.hikarishima.lightland.magic.recipe.IMagicRecipe;
import com.hikarishima.lightland.magic.spell.internal.Spell;
import com.lcy0x1.core.util.NBTObj;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.magic.products.MagicProduct;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.content.magic.spell.internal.Spell;
import dev.lcy0x1.util.NBTObj;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;

public class SpellMagic extends MagicProduct<Spell<?, ?>, SpellMagic> {

    public SpellMagic(LLPlayerData player, NBTObj tag, ResourceLocation rl, IMagicRecipe<?> r) {
        super(MagicRegistry.MPT_SPELL, player, tag, rl, r);
    }

}
