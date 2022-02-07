package dev.hikarishima.lightland.content.arcane.magic;

import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import dev.hikarishima.lightland.util.LightLandFakeEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EarthAxe extends Arcane {

    private final float radius;
    private final int time;

    public EarthAxe(float radius, int time) {
        super(ArcaneType.PHECDA, 0);
        this.radius = radius;
        this.time = time;
    }

    @Override
    public boolean activate(Player player, LLPlayerData magic, ItemStack stack, LivingEntity target) {
        if (target == null)
            return false;
        Level w = player.level;
        strike(w, player, target);
        if (!w.isClientSide()) {
            search(w, player, radius, player.getPosition(1), target, this::strike);
            LightLandFakeEntity.addEffect(target, new MobEffectInstance(VanillaMagicRegistrate.HEAVY.get(), time, 1), player);
            LightLandFakeEntity.addEffect(target, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 4), player);
            double x = target.getX();
            double y = target.getY();
            double z = target.getZ();
            target.teleportTo(x, y - 1, z);
        }
        return true;
    }

    private void strike(Level w, Player player, LivingEntity target) {
        if (!w.isClientSide()) {
            LightLandFakeEntity.addEffect(target, new MobEffectInstance(VanillaMagicRegistrate.HEAVY.get(), time, 0), player);
            LightLandFakeEntity.addEffect(target, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 2), player);
        }
    }

}
