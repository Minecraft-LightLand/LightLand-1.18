package dev.hikarishima.lightland.content.questline.mobs.swamp;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.content.common.item.generic.GenericTieredItem;
import dev.hikarishima.lightland.content.questline.common.mobs.LootTableTemplate;
import dev.hikarishima.lightland.init.registrate.EntityRegistrate;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BossSlime extends MaterialSlime<BossSlime> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(ItemRegistrate.SLIME_TENTACLE.get(), 4, 8, 1))
						.when(LootTableTemplate.byPlayer()))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(ItemRegistrate.UNSTABLE_SLIME.get(), 4, 8, 1))
						.when(LootTableTemplate.byPlayer()))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(ItemRegistrate.BOSS_SLIME.get(), 1, 2, 1))
						.when(LootTableTemplate.byPlayer())));
	}

	public static int MAX_LV = 8;

	public BossSlime(EntityType<BossSlime> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean canBeAffected(MobEffectInstance ins) {
		if (ins.getEffect() == MobEffects.POISON) return false;
		return super.canBeAffected(ins);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		boolean flag = hasEffect(VanillaMagicRegistrate.DISPELL.get()) || hasEffect(VanillaMagicRegistrate.CLEANSE.get());
		if (this.tickCount % 20 == 0) {
			if (!flag) {
				this.heal(1);
			} else {
				hurt(DamageSource.MAGIC.bypassMagic().bypassArmor(), 2);
			}
		}
		if (this.tickCount % 200 == 0) {
			if (this.getTarget() != null) {
				Vec3 vec = this.getTarget().getEyePosition().subtract(getEyePosition()).normalize().scale(getSize() / 2f);
				summon(getRandomSummon(), (float) vec.x, (float) vec.y, (float) vec.z);
			}
		}
		if (this.tickCount % 200 == 100) {
			LivingEntity target = getTarget();
			if (target != null && target.getEyePosition().distanceTo(getEyePosition()) > 8) {
				SlimeTentacle snowball = new SlimeTentacle(level, this);
				Vec3 vec = target.getEyePosition().subtract(getEyePosition());
				double speed = 1.5 + 1.5 * Math.log(getSize()) / Math.log(MAX_LV);
				snowball.shoot(vec.x, vec.y, vec.z, (float) speed, 0);
				level.addFreshEntity(snowball);
			}
		}

		if (this.getHealth() >= this.getMaxHealth() - 1 && this.getSize() < MAX_LV) {
			this.setSize(this.getSize() + 1, false);
		}
	}

	@Override
	public void actuallyHurt(DamageSource source, float damage) {
		MobEffectInstance ins = getEffect(VanillaMagicRegistrate.DISPELL.get());
		float new_damage = recalculateDamage(source, damage);
		if (ins != null && new_damage < damage) {
			int factor = 1 << (ins.getAmplifier() + 1);
			new_damage = damage - (damage - new_damage) / factor;
		}
		super.actuallyHurt(source, new_damage);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void setSize(int size, boolean regen) {
		super.setSize(size, regen);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.min(256, (size + 8) * (size + 8)));
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((size + 8));
		this.getAttribute(Attributes.ARMOR).setBaseValue(Mth.clamp(4, size + 3, 10));
		this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(Math.min(0.8, size * 0.1));
		if (regen) {
			this.setHealth(size >= MAX_LV ? this.getMaxHealth() : this.getMaxHealth() / 2);
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		int size = this.getSize();
		if (!this.level.isClientSide && size > 1 && this.isDeadOrDying()) {
			float f = (float) size / 4.0F;
			List<EntityType<? extends BaseSlime<?>>> list = getNext();
			for (int l = 0; l < list.size(); ++l) {
				float f1 = ((float) (l % 2) - 0.5F) * f;
				float f2 = ((float) (l / 2 % 2) - 0.5F) * f;
				float f3 = ((float) (l / 4 % 2)) * f;
				summon(list.get(l), f1, f2, f3);
			}
		}
		this.setRemoved(reason);
		if (reason == Entity.RemovalReason.KILLED) {
			this.gameEvent(GameEvent.ENTITY_KILLED);
		}
		this.invalidateCaps();
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

	private void summon(EntityType<? extends BaseSlime<?>> type, float f1, float f2, float f3) {
		int size = this.getSize();
		Component name = this.getCustomName();
		boolean noAi = this.isNoAi();
		int next_size = size / 2;

		BaseSlime<?> slime = type.create(this.level);
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

	private List<EntityType<? extends BaseSlime<?>>> getNext() {
		ArrayList<EntityType<? extends BaseSlime<?>>> list = new ArrayList<>();
		list.add(EntityRegistrate.ET_BOSS_SLIME.get());
		list.add(EntityRegistrate.ET_STONE_SLIME.get());
		list.add(EntityRegistrate.ET_VINE_SLIME.get());
		list.add(EntityRegistrate.ET_CARPET_SLIME.get());
		for (int i = 0; i < 4; i++)
			list.add(EntityRegistrate.ET_POTION_SLIME.get());
		return list;
	}

	private EntityType<? extends BaseSlime<?>> getRandomSummon() {
		return WeightedRandomList.create(
						WeightedEntry.wrap(EntityRegistrate.ET_STONE_SLIME.get(), 20),
						WeightedEntry.wrap(EntityRegistrate.ET_VINE_SLIME.get(), 20),
						WeightedEntry.wrap(EntityRegistrate.ET_CARPET_SLIME.get(), 20),
						WeightedEntry.wrap(EntityRegistrate.ET_POTION_SLIME.get(), 60))
				.getRandom(random).get().getData();
	}

	private float recalculateDamage(DamageSource source, float damage) {
		if (source.getDirectEntity() instanceof LivingEntity le) {
			ItemStack stack = le.getMainHandItem();
			if (stack.getItem() instanceof ShovelItem)
				return damage * 4f;
			if (stack.getItem() instanceof GenericTieredItem tier) {
				if (tier.getExtraConfig().bypassMagic)
					return damage * 2f;
			}
		}
		if (source.isBypassMagic() || source.isFire() || source.isExplosion())
			return damage;
		if (source.isFall())
			return damage * 0.1f;
		if (source.isMagic())
			return damage * 0.2f;
		if (source.isProjectile())
			return damage * 0.2f;
		if (source.getDirectEntity() instanceof LivingEntity)
			return damage * 0.4f;
		return damage;
	}

}
