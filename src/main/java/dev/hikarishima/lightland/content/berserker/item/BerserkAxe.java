package dev.hikarishima.lightland.content.berserker.item;

import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BerserkAxe extends AxeItem {

    public BerserkAxe(Tier tier, float damage, float cooldown, Properties properties) {
        super(tier, damage, cooldown, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        if (user instanceof ServerPlayer player) {
            {
                MobEffectInstance ins = player.getEffect(VanillaMagicRegistrate.BLOOD_THURST.get());
                if (ins != null) {
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, (ins.getAmplifier() + 1) * 4));
                }
            }
            {
                MobEffectInstance ins = player.getEffect(VanillaMagicRegistrate.ARMOR_BREAKER.get());
                if (ins != null) {
                    target.addEffect(new MobEffectInstance(VanillaMagicRegistrate.ARMOR_REDUCE.get(), 200, ins.getAmplifier()));
                }
            }
        }
        return super.hurtEnemy(stack, target, user);
    }
}
