package dev.xkmc.lightland.content.archery.feature.bow;

import dev.xkmc.l2library.util.GenericItemStack;
import dev.xkmc.lightland.content.archery.feature.types.OnPullFeature;
import dev.xkmc.lightland.content.archery.item.GenericBowItem;
import dev.xkmc.lightland.events.generic.ClientEntityEffectRenderEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public record GlowTargetAimFeature(int range) implements OnPullFeature, IGlowFeature {

	public static final ClientEntityEffectRenderEvents.EntityTarget TARGET = new ClientEntityEffectRenderEvents.EntityTarget(3, Math.PI / 180 * 5, 2);

	@Override
	public void onPull(Player player, GenericItemStack<GenericBowItem> bow) {

	}

	@Override
	public void tickAim(Player player, GenericItemStack<GenericBowItem> bow) {
		if (player.level.isClientSide()) {
			Vec3 vec3 = player.getEyePosition();
			Vec3 vec31 = player.getViewVector(1.0F).scale(range);
			Vec3 vec32 = vec3.add(vec31);
			AABB aabb = player.getBoundingBox().expandTowards(vec31).inflate(1.0D);
			int sq = range * range;
			Predicate<Entity> predicate = (e) -> e instanceof LivingEntity && !e.isSpectator();
			EntityHitResult result = ProjectileUtil.getEntityHitResult(player, vec3, vec32, aabb, predicate, sq);
			if (result != null && vec3.distanceToSqr(result.getLocation()) < sq) {
				TARGET.updateTarget(result.getEntity());
			}
		}
	}

	@Override
	public void stopAim(Player player, GenericItemStack<GenericBowItem> bow) {
		TARGET.updateTarget(null);
	}

}
