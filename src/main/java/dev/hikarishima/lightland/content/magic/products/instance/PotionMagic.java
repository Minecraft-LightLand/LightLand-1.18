package dev.hikarishima.lightland.content.magic.products.instance;

import com.hikarishima.lightland.magic.MagicRegistry;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.magic.products.MagicProduct;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.lcy0x1.util.NBTObj;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class PotionMagic extends MagicProduct<MobEffect, PotionMagic> {

    public PotionMagic(LLPlayerData player, NBTObj tag, ResourceLocation rl, IMagicRecipe<?> r) {
        super(MagicRegistry.MPT_EFF, player, tag, rl, r);
    }
}
