package dev.xkmc.lightland.content.magic.ritual;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.content.magic.block.RitualCore;
import dev.xkmc.lightland.init.registrate.LightlandRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@SerialClass
@ParametersAreNonnullByDefault
public class PotionBoostRecipe extends AbstractLevelRitualRecipe<PotionBoostRecipe> {

	@SerialClass.SerialField
	public ResourceLocation effect;

	@SerialClass.SerialField
	public int modify_level;

	public PotionBoostRecipe(ResourceLocation id) {
		super(id, LightlandRecipe.RSP_BOOST.get());
	}

	public void assemble(RitualCore.Inv inv, int level) {
		ItemStack stack = inv.core.getItem(0).copy();
		List<MobEffectInstance> list = new ArrayList<>();
		for (MobEffectInstance ins : PotionUtils.getCustomEffects(stack)) {
			if (ins.getEffect().getRegistryName().equals(effect)) {
				if (ins.getAmplifier() < level) {
					if (modify_level == -1)
						continue;
					list.add(new MobEffectInstance(ins.getEffect(), ins.getDuration(), level - 1));
					continue;
				}
			}
			list.add(ins);
		}
		PotionUtils.setCustomEffects(stack, list);
		assemble(inv);
		inv.setItem(5, stack);
	}

}
