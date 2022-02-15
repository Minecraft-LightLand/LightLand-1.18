package dev.hikarishima.lightland.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.hikarishima.lightland.content.archery.feature.bow.GlowTargetAimFeature;
import dev.hikarishima.lightland.content.archery.feature.bow.IGlowFeature;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.content.common.effect.EmeraldPopeEffect;
import dev.hikarishima.lightland.content.common.item.IGlowingTarget;
import dev.hikarishima.lightland.content.common.render.LLRenderState;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.ParticleRegistrate;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import dev.hikarishima.lightland.util.math.RayTraceUtil;
import dev.lcy0x1.base.Proxy;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Map;

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

        @OnlyIn(Dist.CLIENT)
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
                if (bow.config.feature().pull.stream().noneMatch(e -> e instanceof IGlowFeature)) {
                    updateTarget(null);
                    return;
                }
            } else if (!(stack.getItem() instanceof IGlowingTarget)) {
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (Proxy.getClientPlayer() == null) {
            EffectSyncEvents.EFFECT_MAP.clear();
        } else {
            AbstractClientPlayer player = Proxy.getClientPlayer();
            MobEffectInstance ins = player.getEffect(VanillaMagicRegistrate.EMERALD.get());
            if (ins != null) {
                int lv = ins.getAmplifier();
                int r = EmeraldPopeEffect.RADIUS * (1 + lv);
                int count = (1 + lv) * (1 + lv) * 4;
                for (int i = 0; i < count; i++) {
                    addParticle(player.level, player.position(), r);
                }
            }
        }
        GlowTargetAimFeature.TARGET.tickRender();
        RayTraceUtil.TARGET.tickRender();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void fov(FOVModifierEvent event) {
        Player player = Proxy.getClientPlayer();
        if (player == null)
            return;
        if (player.getMainHandItem().getItem() instanceof GenericBowItem bow) {
            float f = event.getFov();
            float i = player.getTicksUsingItem();
            MobEffectInstance ins = player.getEffect(VanillaMagicRegistrate.QUICK_PULL.get());
            if (ins != null) {
                i *= 1.5 + 0.5 * ins.getAmplifier();
            }
            float p = bow.config.fov_time();
            event.setNewfov(f * (1 - Math.min(1, i / p) * bow.config.fov()));
        }
    }

    private record DelayedEntityRender(LivingEntity entity, ResourceLocation rl) {

    }

    public static final ResourceLocation ARCANE_ICON = new ResourceLocation(LightLand.MODID, "textures/effect_overlay/arcane.png");
    public static final ResourceLocation WATER_TRAP_ICON = new ResourceLocation(LightLand.MODID, "textures/effect_overlay/water_trap.png");
    public static final ResourceLocation FLAME_ICON = new ResourceLocation(LightLand.MODID, "textures/effect_overlay/flame.png");

    private static final ArrayList<DelayedEntityRender> ICONS = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void levelRenderLast(RenderLevelLastEvent event) {
        LevelRenderer renderer = event.getLevelRenderer();
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        PoseStack stack = event.getPoseStack();
        RenderSystem.disableDepthTest();
        for (DelayedEntityRender icon : ICONS) {
            renderIcon(icon.entity(), stack, buffers, icon.rl(), event.getPartialTick(), camera, renderer.entityRenderDispatcher);
        }
        buffers.endBatch();
        ICONS.clear();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onLivingEntityRender(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (EffectSyncEvents.EFFECT_MAP.containsKey(entity.getUUID())) {
            Map<MobEffect, Integer> map = EffectSyncEvents.EFFECT_MAP.get(entity.getUUID());
            if (map.containsKey(VanillaMagicRegistrate.ARCANE.get())) {
                ICONS.add(new DelayedEntityRender(entity, ARCANE_ICON));
            }
            if (map.containsKey(VanillaMagicRegistrate.WATER_TRAP.get())) {
                ICONS.add(new DelayedEntityRender(entity, WATER_TRAP_ICON));
            }
            if (map.containsKey(VanillaMagicRegistrate.FLAME.get())) {
                ICONS.add(new DelayedEntityRender(entity, FLAME_ICON));
            }
            if (map.containsKey(VanillaMagicRegistrate.EMERALD.get())) {
                if (!Minecraft.getInstance().isPaused() && entity != Proxy.getClientPlayer()) {
                    int lv = map.get(VanillaMagicRegistrate.EMERALD.get());
                    int r = EmeraldPopeEffect.RADIUS * (1 + lv);
                    int count = (1 + lv) * (1 + lv) * 4;
                    for (int i = 0; i < count; i++) {
                        addParticle(entity.level, entity.position(), r);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderIcon(LivingEntity entity, PoseStack matrix, MultiBufferSource buffer, ResourceLocation rl,
                                   float partial, Camera camera, EntityRenderDispatcher dispatcher) {
        float f = entity.getBbHeight() / 2;

        double x0 = Mth.lerp(partial, entity.xOld, entity.getX());
        double y0 = Mth.lerp(partial, entity.yOld, entity.getY());
        double z0 = Mth.lerp(partial, entity.zOld, entity.getZ());
        Vec3 offset = dispatcher.getRenderer(entity).getRenderOffset(entity, partial);
        Vec3 cam_pos = camera.getPosition();
        double d2 = x0 - cam_pos.x + offset.x();
        double d3 = y0 - cam_pos.y + offset.y();
        double d0 = z0 - cam_pos.z + offset.z();

        matrix.pushPose();
        matrix.translate(d2, d3 + f, d0);
        matrix.mulPose(camera.rotation());
        PoseStack.Pose entry = matrix.last();
        VertexConsumer ivertexbuilder = buffer.getBuffer(LLRenderState.get2DIcon(rl));
        iconVertex(entry, ivertexbuilder, 0.5f, -0.5f, 0, 1);
        iconVertex(entry, ivertexbuilder, -0.5f, -0.5f, 1, 1);
        iconVertex(entry, ivertexbuilder, -0.5f, 0.5f, 1, 0);
        iconVertex(entry, ivertexbuilder, 0.5f, 0.5f, 0, 0);
        matrix.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    private static void iconVertex(PoseStack.Pose entry, VertexConsumer builder, float x, float y, float u, float v) {
        builder.vertex(entry.pose(), x, y, 0)
                .uv(u, v)
                .normal(entry.normal(), 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    private static void addParticle(Level w, Vec3 vec, int r) {
        float tpi = (float) (Math.PI * 2);
        Vec3 v0 = new Vec3(0, r, 0);
        Vec3 v1 = v0.xRot(tpi / 3).yRot((float) (Math.random() * tpi));
        float a0 = (float) (Math.random() * tpi);
        float b0 = (float) Math.acos(2 * Math.random() - 1);
        v0 = v0.xRot(a0).yRot(b0);
        v1 = v1.xRot(a0).yRot(b0);
        w.addAlwaysVisibleParticle(ParticleRegistrate.EMERALD.get(),
                vec.x + v0.x,
                vec.y + v0.y,
                vec.z + v0.z,
                vec.x + v1.x,
                vec.y + v1.y,
                vec.z + v1.z);
    }

}
