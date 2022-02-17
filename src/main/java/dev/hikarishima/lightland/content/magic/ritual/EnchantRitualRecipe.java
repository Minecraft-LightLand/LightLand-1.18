package dev.hikarishima.lightland.content.magic.ritual;

import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@SerialClass
@ParametersAreNonnullByDefault
public class EnchantRitualRecipe extends AbstractLevelRitualRecipe<EnchantRitualRecipe> {

    public EnchantRitualRecipe(ResourceLocation id) {
        super(id, RecipeRegistrate.RS_ENCH.get());
    }

    public void assemble(RitualCore.Inv inv, int level) {
        ItemStack stack = assemble(inv);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        map.replaceAll((e, v) -> v + level - 1);
        EnchantmentHelper.setEnchantments(map, stack);
        inv.setItem(5, stack);
    }

}
