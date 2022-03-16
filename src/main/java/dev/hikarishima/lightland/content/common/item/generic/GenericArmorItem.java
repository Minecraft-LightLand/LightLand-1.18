package dev.hikarishima.lightland.content.common.item.generic;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		config.inventoryTick(stack, level, entity, slot, selected);
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		config.onArmorTick(stack, world, player);
	}

	public ExtraArmorConfig getConfig() {
		return config;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return config.modify(super.getAttributeModifiers(slot, stack), slot, stack);
	}

}
