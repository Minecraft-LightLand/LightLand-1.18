package dev.hikarishima.lightland.content.archery.controller;

import dev.hikarishima.lightland.content.archery.item.GenericArrowItem;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.util.GenericItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public class ArrowFeatureController {

    public record BowArrowUseContext(Level level, LivingEntity user) {

    }

    public static boolean canBowUseArrow(GenericBowItem bow, GenericItemStack<GenericArrowItem> arrow) {
        return true;
    }

    public static AbstractArrow createArrowEntity(BowArrowUseContext ctx, GenericItemStack<GenericBowItem> bow, GenericItemStack<GenericArrowItem> arrow) {
        return arrow.item().createArrow(ctx.level(), arrow.stack(), ctx.user());
    }

}
