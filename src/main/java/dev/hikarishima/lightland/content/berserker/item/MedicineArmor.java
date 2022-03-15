package dev.hikarishima.lightland.content.berserker.item;

import dev.hikarishima.lightland.util.EffectAddUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MedicineArmor extends ArmorItem implements MedicineItem {

	public MedicineArmor(ArmorMaterial mat, EquipmentSlot slot, Properties prop) {
		super(mat, slot, prop);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		List<MobEffectInstance> list = PotionUtils.getCustomEffects(stack);
		for (MobEffectInstance ins : list) {
			MobEffectInstance a = new MobEffectInstance(ins.getEffect(), ins.getDuration() * amount, ins.getAmplifier(),
					ins.isAmbient(), ins.isVisible(), ins.showIcon());
			EffectAddUtil.addEffect(entity, a, EffectAddUtil.AddReason.SELF, entity);
		}
		return super.damageItem(stack, amount, entity, onBroken);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(stack, list, 1);
	}

}
