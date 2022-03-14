package dev.hikarishima.lightland.content.questline.common.mobs;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BipedMonster<T extends BipedMonster<T>> extends BaseMonster<T> {

	protected BipedMonster(EntityType<T> type, Level level, EntityConfig config) {
		super(type, level, config);
	}

	@Override
	protected void registerGoals() {
	}

	@Override
	protected int getExperienceReward(Player player) {
		return super.getExperienceReward(player);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void aiStep() {
		super.aiStep();
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		return super.hurt(source, damage);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		return super.doHurtTarget(target);
	}

	@Override
	public void killed(ServerLevel p_19929_, LivingEntity p_19930_) {
		super.killed(p_19929_, p_19930_);
	}

}
