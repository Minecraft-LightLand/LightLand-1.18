package dev.hikarishima.lightland.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.hikarishima.lightland.content.common.effect.EmeraldPopeEffect;
import dev.hikarishima.lightland.content.common.render.LLRenderState;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.ParticleRegistrate;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import dev.hikarishima.lightland.network.packets.EffectToClient;
import dev.lcy0x1.base.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class EffectSyncEvents {

    public static final ResourceLocation RL_ENTITY_BODY_ICON = new ResourceLocation(LightLand.MODID, "textures/arcane_icon.png");
    public static final ResourceLocation WATER_TRAP_ICON = new ResourceLocation(LightLand.MODID, "textures/water_trap_icon.png");

    private static final Map<UUID, Map<MobEffect, Integer>> EFFECT_MAP = new HashMap<>();
    private static final Set<MobEffect> TRACKED = new HashSet<>();

    public static void init() {
        TRACKED.add(VanillaMagicRegistrate.ARCANE.get());
        TRACKED.add(VanillaMagicRegistrate.WATER_TRAP.get());
        TRACKED.add(VanillaMagicRegistrate.FLAME.get());
        TRACKED.add(VanillaMagicRegistrate.EMERALD.get());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onLivingEntityRender(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        LivingEntityRenderer<?, ?> renderer = event.getRenderer();
        if (EFFECT_MAP.containsKey(entity.getUUID())) {
            Map<MobEffect, Integer> map = EFFECT_MAP.get(entity.getUUID());
            if (map.containsKey(VanillaMagicRegistrate.ARCANE.get())) {
                renderIcon(entity, event.getPoseStack(), event.getMultiBufferSource(), renderer.entityRenderDispatcher, RL_ENTITY_BODY_ICON);
            }
            if (map.containsKey(VanillaMagicRegistrate.WATER_TRAP.get())) {
                renderIcon(entity, event.getPoseStack(), event.getMultiBufferSource(), renderer.entityRenderDispatcher, WATER_TRAP_ICON);
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
    public static void sync(EffectToClient eff) {
        Map<MobEffect, Integer> set = EFFECT_MAP.get(eff.entity);
        if (eff.exist) {
            if (set == null) {
                EFFECT_MAP.put(eff.entity, set = new HashMap<>());
            }
            set.put(eff.effect, eff.level);
        } else if (set != null) {
            set.remove(eff.effect);
        }
    }

    @SubscribeEvent
    public static void onPotionAddedEvent(PotionEvent.PotionAddedEvent event) {
        if (TRACKED.contains(event.getPotionEffect().getEffect())) {
            onEffectAppear(event.getPotionEffect().getEffect(), event.getEntityLiving(), event.getPotionEffect().getAmplifier());
        }
    }

    @SubscribeEvent
    public static void onPotionExpiryEvent(PotionEvent.PotionExpiryEvent event) {
        if (event.getPotionEffect() != null && TRACKED.contains(event.getPotionEffect().getEffect())) {
            onEffectDisappear(event.getPotionEffect().getEffect(), event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof LivingEntity le))
            return;
        for (MobEffect eff : le.getActiveEffectsMap().keySet()) {
            if (TRACKED.contains(eff)) {
                onEffectAppear(eff, le, le.getActiveEffectsMap().get(eff).getAmplifier());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
        if (!(event.getTarget() instanceof LivingEntity le))
            return;
        for (MobEffect eff : le.getActiveEffectsMap().keySet()) {
            if (TRACKED.contains(eff)) {
                onEffectDisappear(eff, le);
            }
        }
    }

    @SubscribeEvent
    public static void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer e = (ServerPlayer) event.getPlayer();
        if (e != null) {
            for (MobEffect eff : e.getActiveEffectsMap().keySet()) {
                if (TRACKED.contains(eff)) {
                    onEffectAppear(eff, e, e.getActiveEffectsMap().get(eff).getAmplifier());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer e = (ServerPlayer) event.getPlayer();
        if (e != null) {
            for (MobEffect eff : e.getActiveEffectsMap().keySet()) {
                if (TRACKED.contains(eff)) {
                    onEffectDisappear(eff, e);
                }
            }
        }
    }

    private static void onEffectAppear(MobEffect eff, LivingEntity e, int lv) {
        new EffectToClient(e.getUUID(), eff, true, lv).toTrackingPlayers(e);
    }

    private static void onEffectDisappear(MobEffect eff, LivingEntity e) {
        new EffectToClient(e.getUUID(), eff, false, 0).toTrackingPlayers(e);
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderIcon(Entity entity, PoseStack matrix, MultiBufferSource buffer, EntityRenderDispatcher manager, ResourceLocation rl) {
        float f = entity.getBbHeight() / 2;
        matrix.pushPose();
        matrix.translate(0, f, 0);
        matrix.mulPose(manager.cameraOrientation());
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
