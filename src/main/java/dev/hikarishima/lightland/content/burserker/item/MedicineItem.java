package dev.hikarishima.lightland.content.burserker.item;

import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public interface MedicineItem {

    public static boolean eq(ItemStack a, ItemStack b) {
        return Objects.equals(a.getOrCreateTag().get("CustomPotionEffects"), b.getOrCreateTag().get("CustomPotionEffects"));
    }

}
