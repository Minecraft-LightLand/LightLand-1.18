package dev.hikarishima.lightland.network.packets;

import dev.hikarishima.lightland.events.generic.EffectSyncEvents;
import dev.hikarishima.lightland.network.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class EffectToClient extends SimplePacketBase {

    public UUID entity;
    public MobEffect effect;
    public boolean exist;
    public int level;

    public EffectToClient(UUID entity, MobEffect effect, boolean exist, int level) {
        this.entity = entity;
        this.effect = effect;
        this.exist = exist;
        this.level = level;
    }

    public EffectToClient(FriendlyByteBuf buf) {
        entity = buf.readUUID();
        exist = buf.readBoolean();
        if (exist) {
            effect = ForgeRegistries.MOB_EFFECTS.getValue(buf.readResourceLocation());
            level = buf.readInt();
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(entity);
        buffer.writeBoolean(exist);
        if (exist) {
            buffer.writeResourceLocation(effect.getRegistryName());
            buffer.writeInt(level);
        }
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> EffectSyncEvents.sync(this));
        context.get().setPacketHandled(true);
    }
}
