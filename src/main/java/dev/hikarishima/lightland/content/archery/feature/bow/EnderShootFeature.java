package dev.hikarishima.lightland.content.archery.feature.bow;

import com.google.common.collect.Maps;
import dev.hikarishima.lightland.content.archery.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.feature.types.OnPullFeature;
import dev.hikarishima.lightland.content.archery.feature.types.OnShootFeature;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.events.ClientEntityEffectRenderEvents.EntityTarget;
import dev.hikarishima.lightland.network.packets.EnderAimSetPacket;
import dev.hikarishima.lightland.util.GenericItemStack;
import dev.lcy0x1.base.Proxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public record EnderShootFeature(int range) implements OnShootFeature, OnPullFeature {

    public static final int CLIENT_TIMEOUT = 200;
    public static final int SERVER_TIMEOUT = 400;

    static class EnderEntityTarget extends EntityTarget {

        private int timeout = 0;

        public EnderEntityTarget() {
            super(3, Math.PI / 180 * 5, 10);
        }

        @Override
        public void onChange(Entity entity) {
            UUID eid = entity == null ? null : entity.getUUID();
            new EnderAimSetPacket(Proxy.getClientPlayer().getUUID(), eid).toServer();
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

    static class ServerTarget {

        public UUID target;
        public int time;

        public ServerTarget(UUID target) {
            this.target = target;
            time = 0;
        }
    }

    public static final EntityTarget TARGET = new EnderEntityTarget();

    public static final ConcurrentMap<UUID, ServerTarget> TARGET_MAP = Maps.newConcurrentMap();

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

    public static void sync(EnderAimSetPacket packet) {
        if (packet.target == null) TARGET_MAP.remove(packet.player);
        else if (TARGET_MAP.containsKey(packet.player)) {
            ServerTarget target = TARGET_MAP.get(packet.player);
            target.target = packet.target;
            target.time = 0;
        } else TARGET_MAP.put(packet.player, new ServerTarget(packet.target));
    }

    @Override
    public boolean onShoot(Player player, Consumer<Consumer<GenericArrowEntity>> consumer) {
        if (player == null)
            return false;
        UUID id = player.getUUID();
        if (!TARGET_MAP.containsKey(id))
            return false;
        UUID tid = TARGET_MAP.get(id).target;
        if (tid == null)
            return false;
        Entity target = ((ServerLevel) player.level).getEntity(tid);
        if (target == null)
            return false;
        consumer.accept(entity -> entity.setPos(target.position().lerp(target.getEyePosition(), 0.5).add(entity.getDeltaMovement().scale(-1))));
        return true;
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
            Predicate<Entity> predicate = (e) -> (e instanceof LivingEntity) && !e.isSpectator();
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
