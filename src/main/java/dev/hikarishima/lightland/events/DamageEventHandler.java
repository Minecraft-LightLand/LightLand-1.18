package dev.hikarishima.lightland.events;

import dev.hikarishima.lightland.compat.TeamAccessor;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneItemUseHelper;
import dev.hikarishima.lightland.content.arcane.internal.IArcaneItem;
import dev.hikarishima.lightland.content.common.item.generic.ExtraToolConfig;
import dev.hikarishima.lightland.content.common.item.generic.GenericTieredItem;
import dev.hikarishima.lightland.util.EffectAddUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("unused")
public class DamageEventHandler {

	public static LivingEntity toLiving(Entity e) {
		return e instanceof LivingEntity ? (LivingEntity) e : null;
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event) {
		DamageSource source = event.getSource();
		if (source.getDirectEntity() instanceof LightningBolt) {
			if (source.getEntity() instanceof Player) {
				if (TeamAccessor.areEntitiesInSameTeam(event.getEntityLiving(), toLiving(event.getSource().getEntity()))) {
					event.setCanceled(true);
					return;
				}
			}
		}
		if ((source.getMsgId().equals("player") || source.getMsgId().equals("mob")) && source.getDirectEntity() instanceof LivingEntity e) {
			ItemStack stack = e.getMainHandItem();
			if (stack.getItem() instanceof GenericTieredItem gen) {
				ExtraToolConfig config = gen.getExtraConfig();
				if (config.bypassMagic && !source.isBypassMagic()) source.bypassMagic();
				if (config.bypassArmor && !source.isBypassArmor()) source.bypassArmor();
			}
		}
	}

	@SubscribeEvent
	public static void onShieldBlock(ShieldBlockEvent event) {
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event) {
		DamageSource source = event.getSource();
		LivingEntity target = event.getEntityLiving();
		if ((source.getMsgId().equals("player") || source.getMsgId().equals("mob")) && source.getDirectEntity() instanceof LivingEntity e) {
			ItemStack stack = e.getMainHandItem();
			if (stack.getItem() instanceof IArcaneItem) {
				EffectAddUtil.addArcane(target, e);
				ArcaneItemUseHelper.addArcaneMana(stack, (int) event.getAmount());
			}
		} else if (source.getDirectEntity() instanceof LightningBolt && source.getEntity() instanceof LivingEntity e) {
			ItemStack stack = e.getMainHandItem();
			if (stack.getItem() instanceof IArcaneItem) {
				EffectAddUtil.addArcane(target, e);
				ArcaneItemUseHelper.addArcaneMana(stack, (int) event.getAmount());
			}
		}
	}

}
