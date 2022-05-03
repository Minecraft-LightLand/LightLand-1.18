package dev.xkmc.lightland.content.questline.mobs.cursedknight;

import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CursedArcher extends BaseCursedKnight<CursedArcher> {

	public CursedArcher(EntityType<CursedArcher> type, Level level) {
		super(type, level, CursedKnightProperties.CONFIG_ARCHER, MobEffects.MOVEMENT_SPEED);
	}

	protected AbstractArrow getArrow(ItemStack itemstack, float damage) {
		Arrow arrow = new Arrow(level, this);
		arrow.setEnchantmentEffectsFromEntity(this, damage);
		WeightedRandomList<WeightedEntry.Wrapper<MobEffectInstance>> list = CursedKnightProperties.EFFECTS;
		if (getTarget() != null) {
			list = WeightedRandomList.create(list.unwrap().stream().filter(e -> e.getData().getEffect().isInstantenous() ||
					!getTarget().hasEffect(e.getData().getEffect())).toList());
		}
		list.getRandom(random).ifPresent(e -> arrow.addEffect(e.getData()));
		return arrow;
	}

}
