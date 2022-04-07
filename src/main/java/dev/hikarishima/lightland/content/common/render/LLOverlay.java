package dev.hikarishima.lightland.content.common.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.hikarishima.lightland.content.common.capability.player.AbilityPoints;
import dev.hikarishima.lightland.content.common.capability.player.CapProxy;
import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.menu.OverlayManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class LLOverlay implements IIngameOverlay {

	public static OverlayManager MANAGER = OverlayManager.get(LightLand.MODID, "widgets");

	@Override
	public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		if (gui.minecraft.options.hideGui) return;
		if (gui.minecraft.player == null) return;
		OverlayManager.ScreenRenderer renderer = MANAGER.getRenderer(gui, mStack);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		renderer.start();
		renderItem(renderer, partialTicks);
		SkillOverlay.render(renderer);
		renderHealth(renderer);
		renderFood(renderer);
		renderMagicBars(renderer);
		renderExperience(renderer);
		renderMiddle(renderer);
	}

	private void renderItem(OverlayManager.ScreenRenderer renderer, float partialTicks) {
		if (renderer.localPlayer == null) return;
		int x = 0;
		Player player = renderer.localPlayer;
		int sel = player.getInventory().selected;
		if (LLPlayerData.isProper(player)) {
			x = CapProxy.getHandler().magicAbility.spell_level;
		}
		// render hotbar
		if (x > 0) {
			int tlen = x == 9 ? 182 : 1 + x * 20;
			renderer.drawLeftRight("spell_list", tlen, 182);
		}
		if (x < 9) {
			int left = x == 0 ? 0 : 20 * x + 1;
			renderer.blit("item_list", left, 0, left, 0, -left, 0);
		}
		if (sel < x) {
			renderer.draw("spell_select", 20 * sel, 0);
		}
		if (sel >= x) {
			renderer.draw("item_select", 20 * sel, 0);
		}

		ItemStack itemstack = player.getOffhandItem();
		if (!itemstack.isEmpty()) {
			renderer.draw("assistant_list");
		}
		// render items
		int i1 = 1;
		int i = renderer.gui.screenWidth / 2;
		for (int j1 = 0; j1 < 9; ++j1) {
			int k1 = i - 90 + j1 * 20 + 2;
			int l1 = renderer.gui.screenHeight - 16 - 3;
			renderer.gui.renderSlot(k1, l1, partialTicks, player, player.getInventory().items.get(j1), i1++);
		}
		if (!itemstack.isEmpty()) {
			int j2 = renderer.gui.screenHeight - 16 - 3;
			renderer.gui.renderSlot(i - 91 - 26, j2, partialTicks, player, itemstack, i1);

		}
		renderer.start();
	}

	private void renderHealth(OverlayManager.ScreenRenderer renderer) {
		if (renderer.cameraEntity instanceof LivingEntity entity) {
			AttributeInstance attrMaxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
			if (attrMaxHealth == null) return;
			double healthMax = attrMaxHealth.getValue();
			double absorb = entity.getAbsorptionAmount();
			double health = entity.getHealth();
			renderer.draw("hp_strip_empty");
			boolean poison = entity.hasEffect(MobEffects.POISON);
			boolean wither = entity.hasEffect(MobEffects.WITHER);
			boolean frozen = entity.isFullyFrozen();
			if (wither) {
				renderer.drawLeftRight("hp_wither_strip", health, healthMax);
				renderer.draw("hp_wither");
			} else if (poison) {
				renderer.drawLeftRight("hp_poison_strip", health, healthMax);
				renderer.draw("hp_poison");
			} else if (frozen) {
				renderer.drawLeftRight("hp_freezing_strip", health, healthMax);
				renderer.draw("hp_freezing");
			} else if (absorb > 0.1) {
				renderer.drawLeftRight("hp_strip", health, healthMax);
				renderer.drawLeftRight("hp_gold_strip", absorb, healthMax);
				renderer.draw("hp_gold");
			} else {
				renderer.drawLeftRight("hp_strip", health, healthMax);
				renderer.draw("hp");
			}
		}
	}

	private void renderFood(OverlayManager.ScreenRenderer renderer) {
		LocalPlayer player = renderer.localPlayer;
		if (player == null) return;
		renderer.draw("food_strip_empty");
		if (player.getVehicle() instanceof LivingEntity mount) {
			double health = Math.ceil(mount.getHealth());
			double healthMax = mount.getMaxHealth();
			renderer.drawRightLeft("hp_mount_strip", health, healthMax);
			renderer.draw("hp_mount");
		} else if (player.isUnderWater() && player.getAirSupply() < 300) {
			int air = player.getAirSupply();
			renderer.drawRightLeft("oxygen_strip", air, 300);
			renderer.draw("oxygen");
		} else {
			FoodData stats = player.getFoodData();
			int level = stats.getFoodLevel();
			float sat = stats.getSaturationLevel();
			float ext = stats.getExhaustionLevel();
			renderer.drawRightLeft("food_strip", level, 20);
			renderer.drawRightLeft("saturation_strip", sat, 20);
			renderer.drawRightLeft("exhaustion", ext, 4);
		}
	}

	private void renderExperience(OverlayManager.ScreenRenderer renderer) {
		LocalPlayer player = renderer.localPlayer;
		if (player == null) return;
		renderer.draw("exp_empty");
		if (player.isRidingJumpable()) {
			renderer.drawLeftRight("jump", player.getJumpRidingScale(), 1);
		} else {
			renderer.drawLeftRight("exp", player.experienceProgress, 1);
		}
	}

	private void renderMagicBars(OverlayManager.ScreenRenderer renderer) {
		if (renderer.localPlayer == null || !LLPlayerData.isProper(renderer.localPlayer)) return;
		LLPlayerData data = CapProxy.getHandler();
		int mana = data.magicAbility.getMana();
		int mana_max = data.magicAbility.getMaxMana();
		int load = data.magicAbility.getSpellLoad();
		int load_max = data.magicAbility.getMaxSpellEndurance();
		renderer.draw("mp_strip_empty");
		renderer.drawLeftRight("mp_strip", mana, mana_max);
		renderer.draw("mp");

		renderer.draw("spell_strip_empty");
		renderer.drawLeftRight("spell_strip", load, load_max);
		renderer.draw("spell");
	}

	private void renderMiddle(OverlayManager.ScreenRenderer renderer) {
		renderer.draw("mid_list");
		renderer.draw("armor_column");
		renderer.draw("armor_tag");

		if (renderer.localPlayer == null || !LLPlayerData.isProper(renderer.localPlayer)) return;
		LLPlayerData data = CapProxy.getHandler();
		int exp = data.abilityPoints.exp;
		int max = AbilityPoints.expRequirement(data.abilityPoints.level);
		renderer.draw("lv_empty");
		renderer.drawBottomUp("lv_strip", exp, max);
		renderer.draw("lv");
	}

}
