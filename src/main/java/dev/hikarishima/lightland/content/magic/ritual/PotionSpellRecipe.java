package dev.hikarishima.lightland.content.magic.ritual;

import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.content.magic.block.RitualSide;
import dev.hikarishima.lightland.content.magic.item.MagicScroll;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SerialClass
@ParametersAreNonnullByDefault
public class PotionSpellRecipe extends AbstractRitualRecipe<PotionSpellRecipe> {

	public PotionSpellRecipe(ResourceLocation id) {
		super(id, RecipeRegistrate.RSP_SPELL.get());
	}

	@Override
	public void assemble(RitualCore.Inv inv, int level) {
		ItemStack core = inv.core.getItem(0).copy();
		List<MobEffectInstance> list = PotionUtils.getCustomEffects(core);
		MagicScroll.TargetType target = MagicScroll.getTarget(core);
		double radius = MagicScroll.getRadius(core);
		inv.setItem(5, assemble(inv));
		for (RitualSide.TE te : inv.sides) {
			ItemStack stack = te.getItem(0);
			if (stack.getItem() == ItemRegistrate.SPELL_CARD.get()) {
				MagicScroll.initEffect(list, stack);
				MagicScroll.setTarget(target, stack);
				MagicScroll.setRadius(radius, stack);
			}
		}
	}

}
