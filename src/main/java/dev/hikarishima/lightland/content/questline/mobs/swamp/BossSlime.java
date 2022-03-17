package dev.hikarishima.lightland.content.questline.mobs.swamp;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.content.questline.common.mobs.LootTableTemplate;
import dev.hikarishima.lightland.init.registrate.EntityRegistrate;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BossSlime extends MaterialSlime<BossSlime> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.SLIME_BALL, 4, 8, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(ItemRegistrate.DIRTY_SLIME.get(), 1, 3, 1))
						.when(LootTableTemplate.byPlayer())));
	}

	public static int MAX_LV = 16;

	public BossSlime(EntityType<BossSlime> type, Level level) {
		super(type, level);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.tickCount % 20 == 0) {
			this.heal(1);
		}
		if (this.getHealth() >= this.getMaxHealth() - 1 && this.getSize() < MAX_LV) {
			this.setSize(this.getSize() + 1, false);
		}
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		damage = recalculateDamage(source, damage);
		return super.hurt(source, damage);
	}

	private float recalculateDamage(DamageSource source, float damage) {
		if (source.isFire() || source.isExplosion())
			return damage;
		if (source.isFall())
			return damage * 0.1f;
		if (source.isMagic())
			return damage * 0.2f;
		if (source.isProjectile())
			return damage * 0.1f;
		if (source.getDirectEntity() instanceof LivingEntity le) {
			ItemStack stack = le.getMainHandItem();
			if (stack.getItem() instanceof ShovelItem)
				return damage * 2f;
			return damage * 0.2f;
		}
		return damage;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void setSize(int size, boolean regen) {
		super.setSize(size, regen);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.min(256, (size + 8) * (size + 8)));
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((size + 8));
		this.getAttribute(Attributes.ARMOR).setBaseValue(Mth.clamp(10, size + 8, 20));
		this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(Mth.clamp(5, size + 4, 10));
		this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(Math.min(0.8, size * 0.1));
		if (regen) {
			this.setHealth(size >= MAX_LV ? this.getMaxHealth() : this.getMaxHealth() / 2);
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		int size = this.getSize();
		if (!this.level.isClientSide && size > 1 && this.isDeadOrDying()) {
			Component name = this.getCustomName();
			boolean noAi = this.isNoAi();
			float f = (float) size / 4.0F;
			int next_size = size / 2;
			List<EntityType<? extends BaseSlime<?>>> list = getNext();
			for (int l = 0; l < list.size(); ++l) {
				float f1 = ((float) (l % 2) - 0.5F) * f;
				float f2 = ((float) (l / 2 % 2) - 0.5F) * f;
				float f3 = ((float) (l / 4 % 2)) * f;
				BaseSlime<?> slime = list.get(l).create(this.level);
				if (this.isPersistenceRequired()) {
					slime.setPersistenceRequired();
				}
				slime.setCustomName(name);
				slime.setNoAi(noAi);
				slime.setInvulnerable(this.isInvulnerable());
				if (slime instanceof PotionSlime potion) potion.setupPotion();
				slime.setSize(next_size, true);
				slime.moveTo(this.getX() + (double) f1,
						this.getY() + 0.5D + f3,
						this.getZ() + (double) f2,
						this.random.nextFloat() * 360.0F, 0.0F);
				this.level.addFreshEntity(slime);
			}
		}
		this.setRemoved(reason);
		if (reason == Entity.RemovalReason.KILLED) {
			this.gameEvent(GameEvent.ENTITY_KILLED);
		}
		this.invalidateCaps();
	}

	private List<EntityType<? extends BaseSlime<?>>> getNext() {
		ArrayList<EntityType<? extends BaseSlime<?>>> list = new ArrayList<>();
		list.add(EntityRegistrate.ET_BOSS_SLIME.get());
		list.add(EntityRegistrate.ET_STONE_SLIME.get());
		list.add(EntityRegistrate.ET_VINE_SLIME.get());
		for (int i = 0; i < 5; i++)
			list.add(EntityRegistrate.ET_POTION_SLIME.get());
		return list;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType type,
										@Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
		groupData = super.finalizeSpawn(level, difficulty, type, groupData, tag);
		setSize(MAX_LV, true);
		return groupData;
	}

	public void seeDeath(BaseSlime<?> slime) {
		heal(slime.getMaxHealth());
	}
}
