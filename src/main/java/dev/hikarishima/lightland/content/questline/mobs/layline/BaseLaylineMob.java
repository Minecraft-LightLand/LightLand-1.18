package dev.hikarishima.lightland.content.questline.mobs.layline;

import dev.hikarishima.lightland.content.questline.common.mobs.AlertClassGoal;
import dev.hikarishima.lightland.content.questline.common.mobs.BipedMonster;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BaseLaylineMob<T extends BaseLaylineMob<T>> extends BipedMonster<T> {

	protected BaseLaylineMob(EntityType<T> type, Level level, EntityConfig config) {
		super(type, level, config);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new AlertClassGoal(this, BaseLaylineMob.class).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, true,
				e -> LaylineProperties.CONVERT_TYPE.contains(e.getType())));
	}

	@Override
	public boolean canPickUpLoot() {
		return false;
	}

	@Override
	public void killed(ServerLevel level, LivingEntity target) {
		LaylineProperties.convert(level, target);
	}

}
