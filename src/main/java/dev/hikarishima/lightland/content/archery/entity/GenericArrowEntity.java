package dev.hikarishima.lightland.content.archery.entity;

import dev.hikarishima.lightland.content.archery.item.GenericArrowItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GenericArrowEntity extends AbstractArrow {

    public GenericArrowEntity(EntityType<GenericArrowEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

}
