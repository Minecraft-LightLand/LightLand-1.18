package dev.hikarishima.lightland.content.common.item.generic;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class GenericArmorItem extends ArmorItem {

	private final ExtraArmorConfig config;

	public GenericArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties prop, ExtraArmorConfig config) {
		super(material, slot, prop);
		this.config = config;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return config.damageItem(stack, amount, entity);
	}

	public ExtraArmorConfig getConfig(){
		return config;
	}
}
