package dev.hikarishima.lightland.content.common.item.generic;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExtraArmorConfig {

	public double repair_chance = 0, damage_chance = 1;

	public int magic_immune = 0;

	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity) {
		double raw = amount * damage_chance;
		int floor = (int) Math.floor(raw);
		double rem = raw - floor;
		return floor + (entity.level.random.nextDouble() < rem ? 1 : 0);
	}

	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		if (stack.isDamaged() && repair_chance > level.getRandom().nextDouble()) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	public ExtraArmorConfig repairChance(double chance) {
		this.repair_chance = chance;
		return this;
	}

	public ExtraArmorConfig damageChance(double chance) {
		this.damage_chance = chance;
		return this;
	}

	public ExtraArmorConfig setMagicImmune(int percent){
		magic_immune = percent;
		return this;
	}

	public int getMagicImmune() {
		return magic_immune;
	}
}
