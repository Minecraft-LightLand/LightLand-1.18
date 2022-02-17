package dev.hikarishima.lightland.content.burserker.item;

import dev.hikarishima.lightland.content.common.item.Mat;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MedicineArmor extends ArmorItem implements MedicineItem{

    public MedicineArmor(EquipmentSlot slot, Properties prop) {
        super(Mat.MEDICINE_LEATHER, slot, prop);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        List<MobEffectInstance> list = PotionUtils.getCustomEffects(stack);
        for (MobEffectInstance ins : list) {
            MobEffectInstance a = new MobEffectInstance(ins.getEffect(), ins.getDuration() * amount, ins.getAmplifier(),
                    ins.isAmbient(), ins.isVisible(), ins.showIcon());
            entity.addEffect(a);
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        PotionUtils.addPotionTooltip(stack, list, 1);
    }

}
