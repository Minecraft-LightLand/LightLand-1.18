package dev.xkmc.lightland.content.questline.common.mobs;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class AlertClassGoal extends HurtByTargetGoal {

	private final Class<? extends Monster> cls;

	public AlertClassGoal(PathfinderMob mob, Class<? extends Monster> cls) {
		super(mob);
		this.cls = cls;
	}

	protected void alertOthers() {
		double d0 = this.getFollowDistance();
		AABB aabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d0, 10.0D, d0);
		List<? extends Mob> list = this.mob.level.getEntitiesOfClass(cls, aabb, EntitySelector.NO_SPECTATORS);
		for (Mob mob : list) {
			if (this.mob != mob && mob.getTarget() == null &&
					!mob.isAlliedTo(this.mob.getLastHurtByMob())) {
				alertOther(mob, this.mob.getLastHurtByMob());
			}
		}
	}
}
