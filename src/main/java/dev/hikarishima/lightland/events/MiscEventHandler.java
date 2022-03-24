package dev.hikarishima.lightland.events;

import dev.hikarishima.lightland.content.common.capability.restriction.ArmorEnchant;
import dev.hikarishima.lightland.content.common.capability.restriction.ArmorWeight;
import dev.hikarishima.lightland.content.common.effect.ForceEffect;
import dev.hikarishima.lightland.content.common.item.backpack.BackpackItem;
import dev.hikarishima.lightland.content.common.item.backpack.EnderBackpackItem;
import dev.hikarishima.lightland.content.common.item.backpack.WorldChestItem;
import dev.hikarishima.lightland.content.common.render.MagicWandOverlay;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.data.Lore;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import dev.hikarishima.lightland.network.packets.SlotClickToServer;
import dev.hikarishima.lightland.util.EffectAddUtil;
import dev.lcy0x1.base.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class MiscEventHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onScreenClick(ScreenEvent.MouseClickedEvent event) {
		Screen screen = event.getScreen();
		if (event.getButton() == 1 &&
				screen instanceof AbstractContainerScreen cont) {
			Slot slot = cont.findSlot(event.getMouseX(), event.getMouseY());
			boolean b0 = slot != null;
			boolean b1 = b0 && slot.container == Proxy.getClientPlayer().getInventory();
			boolean b2 = b0 && !(cont.getMenu() instanceof CreativeModeInventoryScreen.ItemPickerMenu);
			if (b1 || b2) {
				int inv = b1 ? slot.getSlotIndex() : -1;
				int ind = inv == -1 ? slot.index : -1;
				if ((inv >= 0 || ind >= 0) && (slot.getItem().getItem() instanceof EnderBackpackItem ||
						slot.getItem().getItem() instanceof WorldChestItem ||
						inv >= 0 && slot.getItem().getItem() instanceof BackpackItem)) {
					new SlotClickToServer(ind, inv).toServer();
					event.setCanceled(true);
				}
			}
		}

	}

	@SubscribeEvent
	public static void onTargetSet(LivingSetAttackTargetEvent event) {
		if (event.getEntityLiving().hasEffect(VanillaMagicRegistrate.T_CLEAR.get()))
			event.setCanceled(true);
		if (event.getTarget() != null && event.getTarget().hasEffect(VanillaMagicRegistrate.T_HIDE.get()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onVisibilityGet(LivingEvent.LivingVisibilityEvent event) {
		if (event.getEntityLiving().hasEffect(VanillaMagicRegistrate.T_HIDE.get()))
			event.modifyVisibility(0);
	}

	@SubscribeEvent
	public static void onEntityKnockBack(LivingKnockBackEvent event) {
		if (event.getEntityLiving().hasEffect(VanillaMagicRegistrate.NO_KB.get()))
			event.setCanceled(true);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void keyEvent(InputEvent.KeyInputEvent event) {
		if (Minecraft.getInstance().screen == null && Proxy.getClientPlayer() != null && MagicWandOverlay.has_magic_wand) {
			MagicWandOverlay.input(event.getKey(), event.getAction());
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onTooltipEvent(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() instanceof ArmorItem) {
			int weight = ArmorWeight.getWeight(event.getItemStack());
			double load = ArmorEnchant.getItemArmorEnchantLevel(event.getItemStack());
			boolean tooHeavy = !ArmorWeight.canPutOn(Proxy.getClientPlayer(), event.getItemStack());
			boolean tooMagic = !ArmorEnchant.canPutOn(Proxy.getClientPlayer(), event.getItemStack());
			if (weight > 0) {
				TranslatableComponent comp = LangData.IDS.ARMOR_WEIGHT.get(weight);
				if (tooHeavy)
					comp.withStyle(ChatFormatting.RED);
				event.getToolTip().add(comp);
			}
			if (load > 0) {
				TranslatableComponent comp = LangData.IDS.ARMOR_ENCHANT.get(load);
				if (tooMagic)
					comp.withStyle(ChatFormatting.RED);
				event.getToolTip().add(comp);
			}
			if (ArmorEnchant.isCursed(event.getItemStack())) {
				event.getToolTip().add(Lore.ENCHANT_LOAD.get().withStyle(ChatFormatting.RED));
			}
		}
	}

	@SubscribeEvent
	public static void onPotionTest(PotionEvent.PotionApplicableEvent event) {
		boolean flag = event.getEntityLiving().hasEffect(VanillaMagicRegistrate.CLEANSE.get());
		flag |= event.getEntityLiving().hasEffect(VanillaMagicRegistrate.DISPELL.get());
		if (flag) {
			if (event.getPotionEffect().getEffect() instanceof ForceEffect)
				return;
			if (EffectAddUtil.getReason() == EffectAddUtil.AddReason.FORCE)
				return;
			if (EffectAddUtil.getReason() == EffectAddUtil.AddReason.SELF)
				return;
			if (EffectAddUtil.getReason() == EffectAddUtil.AddReason.SKILL)
				return;
			if (event.getPotionEffect().getEffect() == VanillaMagicRegistrate.CLEANSE.get())
				return;
			if (event.getPotionEffect().getEffect() == VanillaMagicRegistrate.DISPELL.get())
				return;
			event.setResult(Event.Result.DENY);
		}
	}

	@SubscribeEvent
	public static void onPotionAdded(PotionEvent.PotionAddedEvent event) {
		boolean flag = event.getPotionEffect().getEffect() == VanillaMagicRegistrate.CLEANSE.get();
		flag |= event.getPotionEffect().getEffect() == VanillaMagicRegistrate.DISPELL.get();
		if (flag) {
			List<MobEffectInstance> list = new ArrayList<>(event.getEntityLiving().getActiveEffects());
			for (MobEffectInstance ins : list) {
				if (ins.getEffect() instanceof ForceEffect)
					continue;
				if (ins.getEffect() == VanillaMagicRegistrate.CLEANSE.get())
					continue;
				if (ins.getEffect() == VanillaMagicRegistrate.DISPELL.get())
					continue;
				event.getEntityLiving().removeEffect(ins.getEffect());
			}
		}
	}

}
