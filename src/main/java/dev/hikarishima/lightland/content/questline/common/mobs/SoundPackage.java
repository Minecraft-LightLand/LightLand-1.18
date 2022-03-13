package dev.hikarishima.lightland.content.questline.common.mobs;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;

public record SoundPackage(SoundEvent ambient, SoundEvent hurt, SoundEvent death, SoundEvent step) {

}
