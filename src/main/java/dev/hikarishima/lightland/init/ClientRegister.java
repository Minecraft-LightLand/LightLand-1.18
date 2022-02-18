package dev.hikarishima.lightland.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.content.berserker.item.MedicineArmor;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientRegister {

    public static final List<GenericBowItem> BOW_LIKE = new ArrayList<>();

    public static void registerItemProperties() {
        for (GenericBowItem bow : BOW_LIKE) {
            ItemProperties.register(bow, new ResourceLocation("pull"), (stack, level, entity, i) -> entity == null || entity.getUseItem() != stack ? 0.0F : bow.getPullForTime(entity, stack.getUseDuration() - entity.getUseItemRemainingTicks()));
            ItemProperties.register(bow, new ResourceLocation("pulling"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        ItemColor color = (stack, val) -> val > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack);
        for (ItemEntry<MedicineArmor> entry : ItemRegistrate.MEDICINE_ARMOR)
            event.getItemColors().register(color, entry.get());
        event.getItemColors().register(color, ItemRegistrate.MEDICINE_LEATHER.get());
    }

}
