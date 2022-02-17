package dev.hikarishima.lightland.content.burserker.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.List;

public interface MedicineItem {

    static boolean eq(ItemStack a, ItemStack b) {
        List<MobEffectInstance> la = PotionUtils.getCustomEffects(a);
        List<MobEffectInstance> lb = PotionUtils.getCustomEffects(b);
        if (la.size() != lb.size()) return false;
        for (int i = 0; i < la.size(); i++) {
            MobEffectInstance ma = la.get(i);
            MobEffectInstance mb = lb.get(i);
            if (ma.getEffect() != mb.getEffect()) return false;
            if (ma.getAmplifier() != mb.getAmplifier()) return false;
            if (ma.getDuration() != mb.getDuration()) return false;
        }
        return true;
    }

}
