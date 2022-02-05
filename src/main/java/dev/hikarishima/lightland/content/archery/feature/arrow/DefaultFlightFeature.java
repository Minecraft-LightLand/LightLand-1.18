package dev.hikarishima.lightland.content.archery.feature.arrow;

import dev.hikarishima.lightland.content.archery.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.feature.types.FlightControlFeature;
import net.minecraft.world.phys.Vec3;

public class DefaultFlightFeature extends FlightControlFeature {

    public static final FlightControlFeature INSTANCE = new DefaultFlightFeature();

    @Override
    public void tickMotion(GenericArrowEntity entity, Vec3 velocity) {
        float inertia = entity.isInWater() ? water_inertia : this.inertia;
        velocity = velocity.scale(inertia);
        float grav = !entity.isNoGravity() && !entity.isNoPhysics() ? gravity : 0;
        entity.setDeltaMovement(velocity.x, velocity.y - grav, velocity.z);
    }
}
