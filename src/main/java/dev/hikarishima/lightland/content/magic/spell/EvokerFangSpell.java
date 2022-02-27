package dev.hikarishima.lightland.content.magic.spell;

import dev.hikarishima.lightland.content.common.entity.SpellEntity;
import dev.hikarishima.lightland.content.magic.spell.internal.ActivationConfig;
import dev.hikarishima.lightland.content.magic.spell.internal.SimpleSpell;
import dev.hikarishima.lightland.content.magic.spell.internal.SpellConfig;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;

public class EvokerFangSpell extends SimpleSpell<EvokerFangSpell.Config> {

    @Override
    public int getDistance(Player player) {
        return 0;
    }

    @Override
    public Config getConfig(Level world, Player player) {
        return SpellConfig.get(this, world, player);
    }

    @Override
    protected void activate(Level world, ServerPlayer player, ActivationConfig activation, Config config) {
        SpellEntity e = new SpellEntity(world);
        e.setData(player, config.spell_time, SpellEntity.SpellPlane.VERTICAL);
        world.addFreshEntity(e);
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        for (int i = 0; i < config.layers.length; i++) {
            Layer layer = config.layers[i];
            for (int j = 0; j < layer.count; j++) {
                double angle = layer.angle * Math.PI / 180 + 2f * Math.PI * j / layer.count;
                double x0 = x + layer.radius * Math.cos(angle);
                double z0 = z + layer.radius * Math.sin(angle);
                world.addFreshEntity(new EvokerFangs(world, x0, y, z0, (float) angle, layer.delay, player));
            }
        }
    }

    @SerialClass
    public static class Config extends SpellConfig {

        @SerialClass.SerialField
        public Layer[] layers;

        @SerialClass.SerialField
        public SpellDisplay spell_time;

    }

    @SerialClass
    public static class Layer {

        @SerialClass.SerialField
        public int count, delay;

        @SerialClass.SerialField
        public double angle, radius;

    }

}
