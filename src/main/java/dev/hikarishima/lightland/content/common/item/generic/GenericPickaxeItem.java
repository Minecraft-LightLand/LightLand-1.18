package dev.hikarishima.lightland.content.common.item.generic;

import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GenericPickaxeItem extends PickaxeItem implements GenericTieredItem {

	private final ExtraToolConfig config;

	public GenericPickaxeItem(Tier tier, int damage, float speed, Properties prop, ExtraToolConfig config) {
		super(tier, damage, speed, prop);
		this.config = config;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		config.inventoryTick(stack, level, entity, slot, selected);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return config.damageItem(stack, amount, entity);
	}

	@Override
	public boolean canBeDepleted() {
		return config.canBeDepleted;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
		config.onHit(stack, target, user);
		if (config.tool_hit > 0)
			stack.hurtAndBreak(config.tool_hit, user, (level) -> level.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
		if (config.tool_mine > 0 &&state.getDestroySpeed(level, pos) != 0.0F) {
			stack.hurtAndBreak(config.tool_mine, entity, (l) -> l.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		}
		return true;
	}

	@NotNull
	@Override
	public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
		return super.getSweepHitBox(stack, player, target);
	}

	@Override
	public ExtraToolConfig getExtraConfig() {
		return config;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return config.modify(super.getAttributeModifiers(slot, stack), slot, stack);
	}
	
}
