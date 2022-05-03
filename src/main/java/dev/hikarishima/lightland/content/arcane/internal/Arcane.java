package dev.hikarishima.lightland.content.arcane.internal;

import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.hikarishima.lightland.compat.TeamAccessor;
import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.init.registrate.LightlandVanillaMagic;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.util.DoubleSidedCall;
import dev.xkmc.l2library.util.ServerOnly;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class Arcane extends NamedEntry<Arcane> {

	public final RegistryEntry<ArcaneType> type;

	public final int cost;

	public Arcane(RegistryEntry<ArcaneType> type, int cost) {
		super(() -> LightLandRegistry.ARCANE);
		this.type = type;
		this.cost = cost;
	}

	@DoubleSidedCall
	public abstract boolean activate(Player player, LLPlayerData magic, ItemStack stack, LivingEntity target);

	@ServerOnly
	public static void search(Level w, Player player, double radius, Vec3 center, LivingEntity target, boolean require_mark, Strike strike) {
		w.getEntities(player, new AABB(center, center).inflate(radius), e -> {
			if (!(e instanceof LivingEntity le))
				return false;
			if (e == player || e == target || TeamAccessor.arePlayerAndEntityInSameTeam((ServerPlayer) player, le))
				return false;
			if (e.getPosition(1).distanceToSqr(center) > radius * radius)
				return false;
			return !require_mark || ((LivingEntity) e).hasEffect(LightlandVanillaMagic.ARCANE.get());
		}).forEach(e -> strike.strike(w, player, (LivingEntity) e));
	}

	@FunctionalInterface
	public interface Strike {

		void strike(Level level, Player player, LivingEntity target);

	}

}
