package dev.hikarishima.lightland.content.magic.spell;

import dev.hikarishima.lightland.content.common.entity.FireArrowEntity;
import dev.hikarishima.lightland.content.common.entity.MagicFireBallEntity;
import dev.hikarishima.lightland.content.common.entity.SpellEntity;
import dev.hikarishima.lightland.content.magic.spell.internal.ActivationConfig;
import dev.hikarishima.lightland.content.magic.spell.internal.SimpleSpell;
import dev.hikarishima.lightland.content.magic.spell.internal.SpellConfig;
import dev.hikarishima.lightland.util.math.RayTraceUtil;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireArrowSpell extends SimpleSpell<FireArrowSpell.Config> {

	@Override
	public int getDistance(Player player) {
		return 64;
	}

	@Override
	public Config getConfig(Level world, Player player) {
		return SpellConfig.get(this, world, player);
	}

	@Override
	protected void activate(Level world, ServerPlayer player, ActivationConfig activation, Config config) {
		SpellEntity e = new SpellEntity(world);
		e.setData(activation, config.spell_time);
		e.setAction(spell -> {
			int t = spell.tickCount - config.spell_time.setup;
			if (t < 0 || t > config.spell_time.duration - config.spell_time.close)
				return;
			if (t % config.period != 0)
				return;
			for (int i = 0; i < config.repeat; i++) {
				Vec3 target = activation.pos;
				float angle = (float) (Math.random() * 360);
				float radius = (float) (Math.random() * config.radius);
				target = RayTraceUtil.getRayTerm(target, 0, angle, radius);
				if (config.explosion == 0) {
					addArrow(target, player, world, config);
				} else {
					addFireball(target, player, world, config);
				}
			}
		});
		world.addFreshEntity(e);
	}

	private void addArrow(Vec3 target, Player player, Level world, Config config) {
		Arrow e = new FireArrowEntity(world, player);
		e.pickup = AbstractArrow.Pickup.DISALLOWED;
		e.setSecondsOnFire(100);
		Vec3 pos = target.add(0, config.distance, 0);
		e.setPos(pos.x, pos.y, pos.z);
		Vec3 velocity = new Vec3(0, -config.velocity, 0);
		e.setDeltaMovement(velocity);
		e.setCritArrow(true);
		e.setBaseDamage(config.damage);
		world.addFreshEntity(e);
	}

	private void addFireball(Vec3 target, Player player, Level world, Config config) {
		Vec3 pos = target.add(0, config.distance, 0);
		MagicFireBallEntity e = new MagicFireBallEntity(world, player, pos, config.size);
		Vec3 velocity = new Vec3(0, -config.velocity, 0);
		e.setDeltaMovement(velocity);
		e.explosionPower = config.explosion;
		world.addFreshEntity(e);
	}

	@SerialClass
	public static class Config extends SpellConfig {

		@SerialClass.SerialField
		public int period, repeat, explosion;

		@SerialClass.SerialField
		public float damage, distance, velocity, radius, size;

		@SerialClass.SerialField
		public SpellDisplay spell_time;

	}

}
