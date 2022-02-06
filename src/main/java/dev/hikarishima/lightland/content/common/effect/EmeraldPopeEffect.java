package dev.hikarishima.lightland.content.common.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EmeraldPopeEffect extends MobEffect {

    public static final int RADIUS = 10;

    public EmeraldPopeEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    public void applyEffectTick(LivingEntity self, int level) {
        if (self.level.isClientSide())
            return;
        int radius = (level + 1) * RADIUS;
        int damage = (level + 1) * 10;
        DamageSource source = new IndirectEntityDamageSource("emerald", self, self);
        for (Entity e : self.level.getEntities(self, new AABB(self.blockPosition()).inflate(radius))) {
            if (e instanceof LivingEntity && !e.isAlliedTo(self) && ((LivingEntity) e).hurtTime == 0 &&
                    e.position().distanceToSqr(self.position()) < radius * radius) {
                double dist = e.position().distanceTo(self.position());
                if (dist > 0.1) {
                    ((LivingEntity) e).knockback(0.4F, e.position().x - self.position().x, e.position().z - self.position().z);
                }
                e.hurt(source, damage);
            }
        }
    }

    public boolean isDurationEffectTick(int tick, int lv) {
        return tick % 10 == 0;
    }

}
