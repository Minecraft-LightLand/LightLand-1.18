package dev.xkmc.lightland.init;

import dev.xkmc.lightland.compat.GeneralCompatHandler;
import dev.xkmc.lightland.content.archery.item.GenericBowItem;
import dev.xkmc.lightland.content.common.render.ItemNameOverlay;
import dev.xkmc.lightland.content.common.render.LLOverlay;
import dev.xkmc.lightland.content.common.render.MagicWandOverlay;
import dev.xkmc.lightland.init.data.LangData;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
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
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerOverlays() {
		OverlayRegistry.enableOverlay(ForgeIngameGui.HOTBAR_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.JUMP_BAR_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.ITEM_NAME_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.PLAYER_HEALTH_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.ARMOR_LEVEL_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.CROSSHAIR_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.AIR_LEVEL_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.FOOD_LEVEL_ELEMENT, false);
		OverlayRegistry.enableOverlay(ForgeIngameGui.MOUNT_HEALTH_ELEMENT, false);
		OverlayRegistry.registerOverlayAbove(ForgeIngameGui.CROSSHAIR_ELEMENT, "MagicWand", MagicWandOverlay.INSTANCE);
		OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "lightland main", new LLOverlay());
		OverlayRegistry.registerOverlayAbove(ForgeIngameGui.ITEM_NAME_ELEMENT, "ItemShifted", new ItemNameOverlay());
		GeneralCompatHandler.handle(GeneralCompatHandler.Stage.OVERLAY);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerKeys() {
		ClientRegistry.registerKeyBinding(LangData.Keys.SKILL_1.map);
		ClientRegistry.registerKeyBinding(LangData.Keys.SKILL_2.map);
		ClientRegistry.registerKeyBinding(LangData.Keys.SKILL_3.map);

	}

}
