package dev.hikarishima.lightland.content.archery.feature.bow;

import dev.hikarishima.lightland.content.archery.feature.types.OnPullFeature;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.util.GenericItemStack;
import dev.lcy0x1.base.Proxy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class GlowTargetAimFeature implements OnPullFeature {

    public static Entity TARGET = null;
    public static int TIME = 0;

    public static void updateGlow(Entity entity) {
        TARGET = entity;
        TIME = 0;
    }

    public static void tickRender(){
        Player player = Proxy.getClientPlayer();
        Vec3 vec = player.getViewVector(1);
    }

    public final int range;

    public GlowTargetAimFeature(int range) {
        this.range = range;
    }

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
                updateGlow(result.getEntity());
            }
        }
    }

    @Override
    public void stopAim(Player player, GenericItemStack<GenericBowItem> bow) {
        TARGET = null;
        TIME = 0;
    }

}
