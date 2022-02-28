package dev.hikarishima.lightland.content.magic.products.instance;

import com.hikarishima.lightland.magic.MagicRegistry;
import com.hikarishima.lightland.magic.capabilities.MagicHandler;
import com.hikarishima.lightland.magic.products.MagicProduct;
import com.hikarishima.lightland.magic.recipe.IMagicRecipe;
import com.lcy0x1.core.util.NBTObj;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.magic.products.MagicProduct;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.lcy0x1.util.NBTObj;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentMagic extends MagicProduct<Enchantment, EnchantmentMagic> {

    public EnchantmentMagic(LLPlayerData player, NBTObj nbtManager, ResourceLocation rl, IMagicRecipe<?> r) {
        super(MagicRegistry.MPT_ENCH, player, nbtManager, rl, r);
    }

}
