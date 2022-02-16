package dev.hikarishima.lightland.content.magic.ritual;

import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.content.magic.block.RitualSide;
import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SerialClass
@ParametersAreNonnullByDefault
public class PotionCoreRecipe extends AbstractMagicRitualRecipe<PotionCoreRecipe> {

    public PotionCoreRecipe(ResourceLocation id) {
        super(id, RecipeRegistrate.RSP_CORE.get());
    }

    @Override
    public void assemble(RitualCore.Inv inv, int level) {
        Map<MobEffect, MobEffectInstance> map = new HashMap<>();
        for (RitualSide.TE te : inv.sides) {
            ItemStack stack = te.getItem(0);
            if (stack.getItem() == Items.POTION) {
                for (MobEffectInstance ins : PotionUtils.getMobEffects(te.getItem(0))) {
                    map.put(ins.getEffect(), ins);
                }
            }
        }
        ItemStack stack = assemble(inv);
        PotionUtils.setCustomEffects(stack, new ArrayList<>(map.values()));
        inv.setItem(5, stack);
    }

}
