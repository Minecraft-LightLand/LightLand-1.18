package dev.hikarishima.lightland.content.archery.feature.arrow;

import dev.hikarishima.lightland.content.archery.feature.types.OnShootFeature;
import dev.hikarishima.lightland.content.common.entity.GenericArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;
import java.util.function.Predicate;

public record EnderArrowFeature(int range) implements OnShootFeature {

    @Override
    public boolean onShoot(Player player, Consumer<Consumer<GenericArrowEntity>> consumer) {
        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = player.getViewVector(1.0F).scale(range);
        Vec3 vec32 = vec3.add(vec31);
        AABB aabb = player.getBoundingBox().expandTowards(vec31).inflate(1.0D);
        int sq = range * range;
        Predicate<Entity> predicate = (e) -> (e instanceof LivingEntity) && !e.isSpectator();
        EntityHitResult result = ProjectileUtil.getEntityHitResult(player, vec3, vec32, aabb, predicate, sq);
        if (result != null && vec3.distanceToSqr(result.getLocation()) < sq) {
            Entity target = result.getEntity();
            consumer.accept(entity -> entity.setPos(target.position().lerp(target.getEyePosition(), 0.5).add(entity.getDeltaMovement().scale(-1))));
            return true;
        }
        return false;
    }

}
