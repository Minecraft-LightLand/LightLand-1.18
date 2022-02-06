package dev.hikarishima.lightland.content.spell.internal;

import dev.hikarishima.lightland.content.common.capability.CapProxy;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.network.packets.CapToClient;
import dev.lcy0x1.base.NamedEntry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class Spell<C extends SpellConfig, A extends ActivationConfig> extends NamedEntry<Spell<?, ?>> implements IForgeRegistryEntry<Spell<?,?>> {

    public Spell() {
        super(() -> LightLandRegistry.SPELL);
    }

    protected abstract A canActivate(Type type, Level world, Player player);

    public abstract C getConfig(Level world, Player player);

    protected abstract void activate(Level world, Player player, A activation, C config);

    public boolean attempt(Type type, Level world, Player player) {
        boolean ans = inner_attempt(type, world, player);
        if (!world.isClientSide()) {
            ServerPlayer e = (ServerPlayer) player;
            new CapToClient(CapToClient.Action.MAGIC_ABILITY, LLPlayerData.get(e)).toClientPlayer(e);
        }
        return ans;
    }

    private boolean inner_attempt(Type type, Level world, Player player) {
        A a = canActivate(type, world, player);
        if (a == null)
            return false;
        C c = getConfig(player.level, player);
        LLPlayerData handler = LLPlayerData.get(player);
        if (type == Type.WAND) {
            int margin = CapProxy.getMargin(player);
            if (c.mana_cost - margin > handler.magicAbility.getMana()) {
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
