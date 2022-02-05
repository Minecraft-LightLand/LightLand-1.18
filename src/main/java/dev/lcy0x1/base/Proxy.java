package dev.lcy0x1.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.server.ServerLifecycleHooks;

public class Proxy {

    @OnlyIn(Dist.CLIENT)
    public static AbstractClientPlayer getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static Player getPlayer() {
        return DistExecutor.unsafeRunForDist(() -> Proxy::getClientPlayer, () -> () -> null);
    }

    public static Level getWorld() {
        return DistExecutor.unsafeRunForDist(() -> Proxy::getClientWorld, () -> Proxy::getServerWorld);
    }

    @OnlyIn(Dist.CLIENT)
    public static ClientLevel getClientWorld() {
        return Minecraft.getInstance().level;
    }

    public static ServerLevel getServerWorld() {
        return ServerLifecycleHooks.getCurrentServer().overworld();
    }

}
