package dev.hikarishima.lightland.util;

import com.google.common.collect.Maps;
import dev.hikarishima.lightland.events.ClientEntityEffectRenderEvents;
import dev.hikarishima.lightland.network.packets.TargetSetPacket;
import dev.lcy0x1.base.Proxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

public class RayTraceUtil {

    public static final int CLIENT_TIMEOUT = 200;
    public static final int SERVER_TIMEOUT = 400;

    public static final ClientEntityEffectRenderEvents.EntityTarget TARGET = new EnderEntityTarget();
    public static final ConcurrentMap<UUID, ServerTarget> TARGET_MAP = Maps.newConcurrentMap();

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

    public static void serverTick() {
        TARGET_MAP.entrySet().removeIf(e -> {
            Optional<ServerPlayer> player = Proxy.getServer().map(MinecraftServer::getPlayerList).map(x -> x.getPlayer(e.getKey()));
            if (player.isEmpty()) return true;
            ServerTarget target = e.getValue();
            Entity entity = ((ServerLevel) (player.get().level)).getEntity(target.target);
            if (entity == null || entity.isRemoved() || !entity.isAlive()) {
                return true;
            }
            target.time++;
            return target.time >= SERVER_TIMEOUT;
        });
    }

    public static void sync(TargetSetPacket packet) {
        if (packet.target == null) TARGET_MAP.remove(packet.player);
        else if (TARGET_MAP.containsKey(packet.player)) {
            ServerTarget target = TARGET_MAP.get(packet.player);
            target.target = packet.target;
            target.time = 0;
        } else TARGET_MAP.put(packet.player, new ServerTarget(packet.target));
    }

    public static class EnderEntityTarget extends ClientEntityEffectRenderEvents.EntityTarget {

        private int timeout = 0;

        public EnderEntityTarget() {
            super(3, Math.PI / 180 * 5, 10);
        }

        @Override
        public void onChange(Entity entity) {
            UUID eid = entity == null ? null : entity.getUUID();
            new TargetSetPacket(Proxy.getClientPlayer().getUUID(), eid).toServer();
            timeout = 0;
        }

        @Override
        public void tickRender() {
            super.tickRender();
            if (target != null) {
                timeout++;
                if (timeout > CLIENT_TIMEOUT) {
                    onChange(target);
                }
            }
        }
    }

    public static class ServerTarget {

        public UUID target;
        public int time;

        public ServerTarget(UUID target) {
            this.target = target;
            time = 0;
        }
    }

    public static void clientUpdateTarget(Player player, double range) {
        if (player.level.isClientSide()) {
            Vec3 vec3 = player.getEyePosition();
            Vec3 vec31 = player.getViewVector(1.0F).scale(range);
            Vec3 vec32 = vec3.add(vec31);
            AABB aabb = player.getBoundingBox().expandTowards(vec31).inflate(1.0D);
            double sq = range * range;
            Predicate<Entity> predicate = (e) -> (e instanceof LivingEntity) && !e.isSpectator();
            EntityHitResult result = ProjectileUtil.getEntityHitResult(player, vec3, vec32, aabb, predicate, sq);
            if (result != null && vec3.distanceToSqr(result.getLocation()) < sq) {
                RayTraceUtil.TARGET.updateTarget(result.getEntity());
            }
        }
    }

    public static LivingEntity serverGetTarget(Player player) {
        if (player.level.isClientSide()) {
            return (LivingEntity) TARGET.target;
        }
        UUID id = player.getUUID();
        if (!RayTraceUtil.TARGET_MAP.containsKey(id))
            return null;
        UUID tid = RayTraceUtil.TARGET_MAP.get(id).target;
        if (tid == null)
            return null;
        return (LivingEntity) (((ServerLevel) player.level).getEntity(tid));
    }
}
