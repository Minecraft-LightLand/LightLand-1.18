package dev.hikarishima.lightland.content.archery.controller;

import dev.hikarishima.lightland.content.archery.feature.FeatureList;
import dev.hikarishima.lightland.content.archery.feature.types.OnShootFeature;
import dev.hikarishima.lightland.content.archery.item.GenericArrowItem;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.content.common.entity.GenericArrowEntity;
import dev.hikarishima.lightland.util.GenericItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ArrowFeatureController {

    public record BowArrowUseContext(Level level, Player user, boolean no_consume, float power) {

    }

    public static boolean canBowUseArrow(GenericBowItem bow, GenericItemStack<GenericArrowItem> arrow) {
        return FeatureList.canMerge(bow.config.feature(), arrow.item().config.feature());
    }

    public static AbstractArrow createArrowEntity(BowArrowUseContext ctx,
                                                  GenericItemStack<GenericBowItem> bow,
                                                  GenericItemStack<GenericArrowItem> arrow) {
        FeatureList features = Objects.requireNonNull(FeatureList.merge(bow.item().config.feature(), arrow.item().config.feature()));
        List<Consumer<GenericArrowEntity>> list = new ArrayList<>();
        for (OnShootFeature e : features.shot)
            if (!e.onShoot(ctx.user, list::add))
                return null;
        GenericArrowEntity ans = new GenericArrowEntity(ctx.level(), ctx.user(),
                new GenericArrowEntity.ArrowEntityData(bow, arrow, ctx.no_consume, ctx.power), features);
        list.forEach(e -> e.accept(ans));
        return ans;
    }

}
