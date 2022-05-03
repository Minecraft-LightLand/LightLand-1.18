package dev.xkmc.lightland.content.magic.spell.internal;

import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.util.ServerOnly;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.init.special.LightLandRegistry;
import dev.xkmc.lightland.network.packets.CapToClient;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class Spell<C extends SpellConfig, A extends ActivationConfig> extends NamedEntry<Spell<?, ?>> implements IForgeRegistryEntry<Spell<?, ?>> {

	public Spell() {
		super(() -> LightLandRegistry.SPELL);
	}

	protected abstract A canActivate(Type type, Level world, ServerPlayer player);

	public abstract C getConfig(Level world, Player player);

	protected abstract void activate(Level world, ServerPlayer player, A activation, C config);

	@ServerOnly
	public boolean attempt(Type type, Level world, ServerPlayer player) {
		boolean ans = inner_attempt(type, world, player);
		if (ans)
			new CapToClient(CapToClient.Action.MAGIC_ABILITY, LLPlayerData.get(player)).toClientPlayer(player);
		return ans;
	}

	@ServerOnly
	private boolean inner_attempt(Type type, Level world, ServerPlayer player) {
		A a = canActivate(type, world, player);
		if (a == null)
			return false;
		C c = getConfig(player.level, player);
		LLPlayerData handler = LLPlayerData.get(player);
		if (type == Type.WAND) {
			if (c.mana_cost > handler.magicAbility.getMana()) {
				return false;
			}
			handler.magicAbility.giveMana(-c.mana_cost);
		} else {
			handler.magicAbility.addSpellLoad(c.spell_load);
		}
		activate(world, player, a, c);
		return true;
	}

	public abstract int getDistance(Player player);

	public enum Type {
		SCROLL, WAND
	}

}
