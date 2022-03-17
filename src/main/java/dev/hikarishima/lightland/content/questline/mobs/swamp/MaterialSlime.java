package dev.hikarishima.lightland.content.questline.mobs.swamp;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;

public class MaterialSlime<T extends MaterialSlime<T>> extends BaseSlime<T> {

	public MaterialSlime(EntityType<T> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		if (source.getDirectEntity() instanceof LivingEntity le) {
			ItemStack stack = le.getMainHandItem();
			damage = damageFactor(source, stack, damage);
		}
		return super.hurt(source, damage);
	}

	protected float damageFactor(DamageSource source, ItemStack stack, float damage) {
		return damage;
	}

}
