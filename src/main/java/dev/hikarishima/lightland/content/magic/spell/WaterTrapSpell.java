package dev.hikarishima.lightland.content.magic.spell;

import dev.hikarishima.lightland.compat.TeamAccessor;
import dev.hikarishima.lightland.content.common.entity.SpellEntity;
import dev.hikarishima.lightland.content.magic.spell.internal.ActivationConfig;
import dev.hikarishima.lightland.content.magic.spell.internal.SimpleSpell;
import dev.hikarishima.lightland.content.magic.spell.internal.SpellConfig;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import dev.hikarishima.lightland.util.LightLandFakeEntity;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class WaterTrapSpell extends SimpleSpell<WaterTrapSpell.Config> {

	@Override
	public Config getConfig(Level world, Player player) {
		return SpellConfig.get(this, world, player);
	}

	@Override
	public int getDistance(Player player) {
		return 64;
	}

	@Override
	protected void activate(Level world, ServerPlayer player, ActivationConfig activation, Config config) {
		SpellEntity e = new SpellEntity(world);
		e.setData(activation, config.spell_time);
		e.setAction(spell -> {
			int t = spell.tickCount - config.spell_time.setup;
			if (t != 0)
				return;
			world.getEntities(player, new AABB(spell.blockPosition()).inflate(config.radius),
					ent -> ent instanceof LivingEntity le &&
							!TeamAccessor.arePlayerAndEntityInSameTeam(player, le) &&
							le.position().distanceTo(spell.position()) < config.radius
			).forEach(le -> LightLandFakeEntity.addEffect((LivingEntity) le,
					new MobEffectInstance(VanillaMagicRegistrate.WATER_TRAP.get(), config.effect_time, config.effect_level), player));
		});
		world.addFreshEntity(e);
	}

	public static class Activation extends ActivationConfig {

		public Activation(Level world, Player player) {
			super(world, player, 64);
		}

	}

	@SerialClass
	public static class Config extends SpellConfig {

		@SerialClass.SerialField
		public int effect_time, effect_level;

		@SerialClass.SerialField
		public float radius;

		@SerialClass.SerialField
		public SpellDisplay spell_time;

	}

}
