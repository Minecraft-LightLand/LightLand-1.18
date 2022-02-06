package dev.hikarishima.lightland.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class RayTraceUtil {

    public static EntityHitResult rayTraceEntity(Player player, double reach, Predicate<Entity> pred) {
        Level world = player.level;
        Vec3 pos = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        Vec3 end = getRayTerm(pos, player.getXRot(), player.getYRot(), reach);
        AABB box = new AABB(pos, end).inflate(1);
        return ProjectileUtil.getEntityHitResult(world, player, pos, end, box, pred);
    }

    public static Vec3 getRayTerm(Vec3 pos, float xRot, float yRot, double reach) {
        float f2 = Mth.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-xRot * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-xRot * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        return pos.add(f6 * reach, f5 * reach, f7 * reach);
    }

}
