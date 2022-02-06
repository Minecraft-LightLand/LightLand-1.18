package net.minecraft.world.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;

public class LightLandFakeEntity {

    public static void actuallyHurt(LivingEntity e, DamageSource source, float damage) {
        e.actuallyHurt(source, damage);
    }

    public static float getDamageAfterArmorAbsorb(LivingEntity e, DamageSource source, float damage) {
        return e.getDamageAfterArmorAbsorb(source, damage);
    }

    public static float getDamageAfterMagicAbsorb(LivingEntity e, DamageSource source, float damage) {
        return e.getDamageAfterMagicAbsorb(source, damage);
    }

    public static void hurtArmor(LivingEntity e, DamageSource source, float damage) {
        e.hurtArmor(source, damage);
    }

    /**
     * force add effect, make boss not override
     * for icon use only, such as Arcane Mark on Wither and Ender Dragon
     */
    public static void addEffect(LivingEntity e, MobEffectInstance ins, Entity source) {
        MobEffectInstance effectinstance = e.getActiveEffectsMap().get(ins.getEffect());
        MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionAddedEvent(e, effectinstance, ins, source));
        if (effectinstance == null) {
            e.getActiveEffectsMap().put(ins.getEffect(), ins);
            e.onEffectAdded(ins, source);
        } else if (effectinstance.update(ins)) {
            e.onEffectUpdated(effectinstance, true, source);
        }
    }

    public static float getEquipmentDropChance(Mob entity, EquipmentSlot slot) {
        return entity.getEquipmentDropChance(slot);
    }

}
