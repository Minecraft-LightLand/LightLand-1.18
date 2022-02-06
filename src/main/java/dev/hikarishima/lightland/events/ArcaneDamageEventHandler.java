package dev.hikarishima.lightland.events;

import dev.hikarishima.lightland.init.special.ArcaneRegistry;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneItemUseHelper;
import dev.hikarishima.lightland.content.arcane.internal.IArcaneItem;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import dev.hikarishima.lightland.util.LightLandFakeEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("unused")
public class ArcaneDamageEventHandler {

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getDirectEntity() instanceof LightningBolt) {
            if (event.getSource().getEntity() instanceof Player) {
                if (event.getEntityLiving().isAlliedTo(event.getSource().getEntity())) {
                    event.setCanceled(true);
                    return;
                }
                LightLandFakeEntity.addEffect(event.getEntityLiving(),
                        new MobEffectInstance(VanillaMagicRegistrate.ARCANE.get(), ArcaneRegistry.ARCANE_TIME),
                        event.getSource().getEntity());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntityLiving();
        if ((source.getMsgId().equals("player") || source.getMsgId().equals("mob")) && source.getDirectEntity() instanceof LivingEntity e) {
            ItemStack stack = e.getMainHandItem();
            if (stack.getItem() instanceof IArcaneItem) {
                ArcaneItemUseHelper.addArcaneMana(stack, (int) event.getAmount());
                LightLandFakeEntity.addEffect(target, new MobEffectInstance(VanillaMagicRegistrate.ARCANE.get(), ArcaneRegistry.ARCANE_TIME), e);
            }
        }
    }

}
