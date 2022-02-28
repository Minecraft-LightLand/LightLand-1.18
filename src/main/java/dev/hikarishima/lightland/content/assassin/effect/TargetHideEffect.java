package dev.hikarishima.lightland.content.assassin.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

public class TargetHideEffect extends MobEffect {

    public TargetHideEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public boolean isDurationEffectTick(int tick, int level) {
        return tick % 10 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity self, int level) {
        if (self.level.isClientSide()) return;
        int radius = 20 << level;
        for (Entity e : self.level.getEntities(self, new AABB(self.position(), self.position()).inflate(radius))) {
            if (!(e instanceof Mob mob)) continue;
            if (e.distanceToSqr(self) > radius * radius) continue;
            LivingEntity le = mob.getTarget();
            if (le != self) continue;
            mob.setTarget(null);
        }

    }

}
