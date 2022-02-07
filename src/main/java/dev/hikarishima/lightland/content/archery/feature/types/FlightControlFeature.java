package dev.hikarishima.lightland.content.archery.feature.types;

import dev.hikarishima.lightland.content.common.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.feature.BowArrowFeature;
import net.minecraft.world.phys.Vec3;

public class FlightControlFeature implements BowArrowFeature {

    public static final FlightControlFeature INSTANCE = new FlightControlFeature();

    public float gravity = 0.05f;
    public float inertia = 0.99f;
    public float water_inertia = 0.6f;
    public int life = -1;
    public int ground_life = 1200;

    public void tickMotion(GenericArrowEntity entity, Vec3 velocity){
        float inertia = entity.isInWater() ? water_inertia : this.inertia;
        velocity = velocity.scale(inertia);
        float grav = !entity.isNoGravity() && !entity.isNoPhysics() ? gravity : 0;
        entity.setDeltaMovement(velocity.x, velocity.y - grav, velocity.z);
    }

}
