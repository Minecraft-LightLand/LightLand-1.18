package dev.hikarishima.lightland.content.archery.controller;

import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.util.GenericItemStack;
import net.minecraft.world.entity.player.Player;

public class BowFeatureController {

    public static int getMaxPullTime(GenericItemStack<GenericBowItem> bow){
        return 72000;
    }

    public static void startUsing(Player player, GenericItemStack<GenericBowItem> bow) {
        bow.item().config.feature().pull.forEach(e->e.onPull(player, bow));
    }

    public static void usingTick(Player player, GenericItemStack<GenericBowItem> bow) {
        bow.item().config.feature().pull.forEach(e->e.tickAim(player, bow));
    }

    public static void stopUsing(Player player, GenericItemStack<GenericBowItem> bow) {
        bow.item().config.feature().pull.forEach(e->e.stopAim(player, bow));

    }
}
