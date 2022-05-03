package dev.xkmc.lightland.content.questline.mobs.swamp;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MaterialSlime<T extends MaterialSlime<T>> extends BaseSlime<T> {

	public MaterialSlime(EntityType<T> type, Level level) {
		super(type, level);
	}

	@Override
	public void actuallyHurt(DamageSource source, float damage) {
		if (source.getDirectEntity() instanceof LivingEntity le) {
			ItemStack stack = le.getMainHandItem();
			damage = damageFactor(source, stack, damage);
		}
		super.actuallyHurt(source, damage);
	}

	protected float damageFactor(DamageSource source, ItemStack stack, float damage) {
		return damage;
	}

}
