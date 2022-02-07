package dev.hikarishima.lightland.content.archery.feature.arrow;

import dev.hikarishima.lightland.content.archery.feature.types.FlightControlFeature;

public class NoFallArrowFeature extends FlightControlFeature {

    public NoFallArrowFeature(int life) {
        this.gravity = 0;
        this.inertia = 1;
        this.water_inertia = 1;
        this.life = life;
    }

}
