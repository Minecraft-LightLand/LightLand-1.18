package dev.hikarishima.lightland.util.math;

import net.minecraft.world.phys.Vec3;

public class MathHelper {

    public static double horSq(Vec3 vec3) {
        return vec3.x * vec3.x + vec3.z * vec3.z;
    }
}
