package dev.hikarishima.lightland.network.packets;

import dev.hikarishima.lightland.content.common.capability.*;
import dev.hikarishima.lightland.network.SerialPacketBase;
import dev.lcy0x1.base.Proxy;
import dev.lcy0x1.util.Automator;
import dev.lcy0x1.util.ExceptionHandler;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Function;

@SerialClass
public class CapToClient extends SerialPacketBase {

    @SerialClass.SerialField
    public Action action;

    @SerialClass.SerialField
    public CompoundTag tag;

    @Deprecated
    public CapToClient() {

    }

    public CapToClient(Action action, LLPlayerData handler) {
        this.action = action;
        this.tag = action.server.apply(handler);
    }

    public void handle(NetworkEvent.Context context) {
        if (action != Action.ALL && action != Action.CLONE && !Proxy.getClientPlayer().isAlive())
            return;
        action.client.accept(tag);
    }

    public static void reset(ServerPlayer e, LLPlayerData.Reset reset) {
        CapToClient msg = new CapToClient(Action.RESET, null);
        msg.tag.putInt("ordinal", reset.ordinal());
        msg.toClientPlayer(e);
    }

    public enum Action {
        DEBUG((m) -> Automator.toTag(new CompoundTag(), m), (tag) -> {
            LLPlayerData m = CapProxy.getHandler();
            CompoundTag comp = ExceptionHandler.get(() -> Automator.toTag(new CompoundTag(), LLPlayerData.class, m, f -> true));
            CapToServer.sendDebugInfo(tag, comp);
        }),
        ALL((m) -> Automator.toTag(new CompoundTag(), m), tag -> LLPlayerData.cacheSet(tag, false)),
        CLONE((m) -> Automator.toTag(new CompoundTag(), m), tag -> LLPlayerData.cacheSet(tag, true)),
        ARCANE_TYPE((m) -> m.magicAbility.arcane_type, (tag) -> {
            MagicAbility abi = CapProxy.getHandler().magicAbility;
            abi.arcane_type = tag;
        }), MAGIC_ABILITY((m) -> Automator.toTag(new CompoundTag(), m.magicAbility), (tag) -> {
            LLPlayerData h = CapProxy.getHandler();
            h.magicAbility = new MagicAbility(h);
            ExceptionHandler.run(() -> Automator.fromTag(tag, MagicAbility.class, h.magicAbility, f -> true));
            h.reInit();
        }), ABILITY_POINT((m) -> Automator.toTag(new CompoundTag(), m.abilityPoints), (tag) -> {
            LLPlayerData h = CapProxy.getHandler();
            h.abilityPoints = new AbilityPoints(h);
            ExceptionHandler.run(() -> Automator.fromTag(tag, AbilityPoints.class, h.abilityPoints, f -> true));
            h.reInit();
        }), RESET(m -> new CompoundTag(), tag -> {
            LLPlayerData h = CapProxy.getHandler();
            h.reset(LLPlayerData.Reset.values()[tag.getInt("ordinal")]);
        }), SKILL(m -> Automator.toTag(new CompoundTag(), m.skillCap), tag -> {
            LLPlayerData h = CapProxy.getHandler();
            h.skillCap = new SkillCap(h);
            ExceptionHandler.run(() -> Automator.fromTag(tag, SkillCap.class, h.skillCap, f -> true));
        });

        public final Function<LLPlayerData, CompoundTag> server;
        public final Consumer<CompoundTag> client;


        Action(Function<LLPlayerData, CompoundTag> server, Consumer<CompoundTag> client) {
            this.server = server;
            this.client = client;
        }
    }

}
