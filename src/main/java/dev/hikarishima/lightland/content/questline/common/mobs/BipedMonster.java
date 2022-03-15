package dev.hikarishima.lightland.content.questline.common.mobs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BipedMonster<T extends BipedMonster<T>> extends BaseMonster<T> implements RangedAttackMob {


	private final RangedBowAttackGoal<T> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
	private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {
		public void stop() {
			super.stop();
			BipedMonster.this.setAggressive(false);
		}

		public void start() {
			super.start();
			BipedMonster.this.setAggressive(true);
		}
	};

	protected BipedMonster(EntityType<T> type, Level level, EntityConfig config) {
		super(type, level, config);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public void reassessWeaponGoal() {
		if (!this.level.isClientSide) {
			this.goalSelector.removeGoal(this.meleeGoal);
			this.goalSelector.removeGoal(this.bowGoal);
			ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem));
			if (itemstack.is(Items.BOW)) {
				this.bowGoal.setMinAttackInterval(20);
				this.goalSelector.addGoal(4, this.bowGoal);
			} else {
				this.goalSelector.addGoal(4, this.meleeGoal);
			}

		}
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.reassessWeaponGoal();
	}

	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
		super.setItemSlot(slot, stack);
		if (!this.level.isClientSide) {
			this.reassessWeaponGoal();
		}
	}

	public void performRangedAttack(LivingEntity target, float damage) {
		ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
		AbstractArrow arrow = this.getArrow(itemstack, damage);
		if (this.getMainHandItem().getItem() instanceof BowItem)
			arrow = ((BowItem) this.getMainHandItem().getItem()).customArrow(arrow);
		double d0 = target.getX() - this.getX();
		double d1 = target.getY(1 / 3d) - arrow.getY();
		double d2 = target.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		arrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));
		this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.level.addFreshEntity(arrow);
	}

	private AbstractArrow getArrow(ItemStack itemstack, float damage) {
		AbstractArrow arrow = ((ArrowItem) Items.ARROW).createArrow(level, itemstack, this);
		arrow.setEnchantmentEffectsFromEntity(this, damage);
		return arrow;
	}

}
