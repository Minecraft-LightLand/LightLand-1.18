package dev.hikarishima.lightland.content.spell.internal;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class SimpleSpell<C extends SpellConfig> extends Spell<C, ActivationConfig> {

    @Override
    protected final ActivationConfig canActivate(Type type, Level world, Player player) {
        return new ActivationConfig(world, player, getDistance(player));
    }

}
