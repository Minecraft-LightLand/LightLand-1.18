package dev.hikarishima.lightland.content.archery.controller;

import dev.hikarishima.lightland.content.archery.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.feature.FeatureList;
import dev.hikarishima.lightland.content.archery.item.GenericArrowItem;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.util.GenericItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ArrowFeatureController {

    public record BowArrowUseContext(Level level, LivingEntity user, boolean no_consume, float power) {

    }

    public static boolean canBowUseArrow(GenericBowItem bow, GenericItemStack<GenericArrowItem> arrow) {
        return FeatureList.canMerge(bow.config.feature(), arrow.item().config.feature());
    }

    public static AbstractArrow createArrowEntity(BowArrowUseContext ctx,
                                                  GenericItemStack<GenericBowItem> bow,
                                                  GenericItemStack<GenericArrowItem> arrow) {
        GenericArrowEntity entity = new GenericArrowEntity(ctx.level(), ctx.user(),
                new GenericArrowEntity.ArrowEntityData(bow, arrow, ctx.no_consume, ctx.power));
        return entity;
    }

}
