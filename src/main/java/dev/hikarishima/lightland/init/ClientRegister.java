package dev.hikarishima.lightland.init;

import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ClientRegister {

    public static final List<GenericBowItem> BOW_LIKE = new ArrayList<>();

    public static void registerItemProperties() {
        for (GenericBowItem bow : BOW_LIKE) {
            ItemProperties.register(bow, new ResourceLocation("pull"), (stack, level, entity, i) -> entity == null ? 0.0F : entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / bow.config.pull_time());
            ItemProperties.register(bow, new ResourceLocation("pulling"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        }
    }

}
