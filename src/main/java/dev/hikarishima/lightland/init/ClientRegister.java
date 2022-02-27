package dev.hikarishima.lightland.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.compat.GeneralCompatHandler;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.content.berserker.item.MedicineArmor;
import dev.hikarishima.lightland.content.common.item.backpack.BackpackItem;
import dev.hikarishima.lightland.content.common.item.backpack.EnderBackpackItem;
import dev.hikarishima.lightland.content.common.render.*;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;

import java.util.ArrayList;
import java.util.List;

public class ClientRegister {

    public static final List<GenericBowItem> BOW_LIKE = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    public static void registerItemProperties() {
        for (GenericBowItem bow : BOW_LIKE) {
            ItemProperties.register(bow, new ResourceLocation("pull"), (stack, level, entity, i) -> entity == null || entity.getUseItem() != stack ? 0.0F : bow.getPullForTime(entity, stack.getUseDuration() - entity.getUseItemRemainingTicks()));
            ItemProperties.register(bow, new ResourceLocation("pulling"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        }
        for (ItemEntry<BackpackItem> entry : ItemRegistrate.BACKPACKS) {
            ItemProperties.register(entry.get(), new ResourceLocation("open"), BackpackItem::isOpened);
        }
        ItemProperties.register(ItemRegistrate.ENDER_BACKPACK.get(), new ResourceLocation("open"), EnderBackpackItem::isOpened);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        {
            ItemColor color = (stack, val) -> val > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack);
            for (ItemEntry<MedicineArmor> entry : ItemRegistrate.MEDICINE_ARMOR)
                event.getItemColors().register(color, entry.get());
            for (ItemEntry<MedicineArmor> entry : ItemRegistrate.KING_MED_ARMOR)
                event.getItemColors().register(color, entry.get());
            event.getItemColors().register(color, ItemRegistrate.MEDICINE_LEATHER.get());
            event.getItemColors().register(color, ItemRegistrate.KING_MED_LEATHER.get());
        }
        {
            ItemColor color = (stack, val) -> val == 0 ? -1 : ((BackpackItem) stack.getItem()).color.getMaterialColor().col;
            for (ItemEntry<BackpackItem> entry : ItemRegistrate.BACKPACKS)
                event.getItemColors().register(color, entry.get());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerOverlays() {
        OverlayRegistry.enableOverlay(ForgeIngameGui.HOTBAR_ELEMENT, false);
        OverlayRegistry.enableOverlay(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT, false);
        OverlayRegistry.enableOverlay(ForgeIngameGui.JUMP_BAR_ELEMENT, false);
        OverlayRegistry.enableOverlay(ForgeIngameGui.ITEM_NAME_ELEMENT, false);
        OverlayRegistry.enableOverlay(ForgeIngameGui.PLAYER_HEALTH_ELEMENT, false);
        OverlayRegistry.enableOverlay(ForgeIngameGui.ARMOR_LEVEL_ELEMENT, false);
        OverlayRegistry.registerOverlayTop("SpellHotbar", new SpellOverlay());
        OverlayRegistry.registerOverlayTop("LLExperience", new BarOverlay());
        OverlayRegistry.registerOverlayTop("ItemShifted", new ItemNameOverlay());
        OverlayRegistry.registerOverlayTop("PlayerHealth", new HealthOverlay());
        OverlayRegistry.registerOverlayTop("SkillBar", new SkillOverlay());
        GeneralCompatHandler.handle(GeneralCompatHandler.Stage.OVERLAY);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerKeys() {
        ClientRegistry.registerKeyBinding(LangData.Keys.SKILL_1.map);
        ClientRegistry.registerKeyBinding(LangData.Keys.SKILL_2.map);
        ClientRegistry.registerKeyBinding(LangData.Keys.SKILL_3.map);
        ClientRegistry.registerKeyBinding(LangData.Keys.SKILL_4.map);
    }

}
