package dev.hikarishima.lightland.events;

import dev.hikarishima.lightland.content.archery.feature.bow.GlowTargetAimFeature;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.lcy0x1.base.Proxy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEntityEffectRenderEvents {

    public static class EntityTarget {

        public final double max_distance, max_angle;
        public final int max_time;
        public int time;
        public Entity target;

        public EntityTarget(double max_distance, double max_angle, int max_time) {
            this.max_distance = max_distance;
            this.max_angle = max_angle;
            this.max_time = max_time;
        }

        public void updateTarget(Entity entity) {
            if (target != entity) {
                onChange(entity);
            }
            target = entity;
            time = 0;
        }

        public void onChange(Entity entity) {

        }

        public void tickRender() {
            if (target == null) {
                return;
            }
            Player player = Proxy.getClientPlayer();
            if (player == null) {
                updateTarget(null);
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GenericBowItem bow) {
                if (bow.config.feature().pull.stream().noneMatch(e -> e instanceof GlowTargetAimFeature)) {
                    updateTarget(null);
                    return;
                }
            } else {
                updateTarget(null);
                return;
            }
            Vec3 pos_a = player.getEyePosition();
            Vec3 vec = player.getViewVector(1);
            Vec3 pos_b = target.getPosition(1);
            Vec3 diff = pos_b.subtract(pos_a);
            double dot = diff.dot(vec);
            double len_d = diff.length();
            double len_v = vec.length();
            double angle = Math.acos(dot / len_d / len_v);
            double dist = Math.sin(angle) * len_d;
            if (angle > max_angle && dist > max_distance) {
                updateTarget(null);
            }
            time++;
            if (time >= max_time) {
                updateTarget(null);
            }
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        GlowTargetAimFeature.TARGET.tickRender();
    }

    @SubscribeEvent
    public static void fov(FOVModifierEvent event) {
        Player player = Proxy.getClientPlayer();
        if (player == null)
            return;
        if (player.getMainHandItem().getItem() instanceof GenericBowItem bow) {
            float f = event.getFov();
            float i = player.getTicksUsingItem();
            float p = bow.config.fov_time();
            event.setNewfov(f * (1 - Math.min(1, i / p) * bow.config.fov()));
        }
    }

}
