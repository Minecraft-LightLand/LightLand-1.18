package dev.hikarishima.lightland.content.archery.feature.bow;

import dev.hikarishima.lightland.content.archery.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.feature.types.OnPullFeature;
import dev.hikarishima.lightland.content.archery.feature.types.OnShootFeature;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.util.GenericItemStack;
import net.minecraft.world.entity.player.Player;

public class EnderShootFeature implements OnShootFeature, OnPullFeature {

    @Override
    public void onShoot(GenericArrowEntity entity) {

    }

    @Override
    public void onPull(Player player, GenericItemStack<GenericBowItem> bow) {

    }

    @Override
    public void tickAim(Player player, GenericItemStack<GenericBowItem> bow) {

    }

    @Override
    public void stopAim(Player player, GenericItemStack<GenericBowItem> bow) {

    }
}
