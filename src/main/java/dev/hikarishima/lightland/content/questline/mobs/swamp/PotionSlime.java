package dev.hikarishima.lightland.content.questline.mobs.swamp;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.content.questline.common.mobs.LootTableTemplate;
import dev.hikarishima.lightland.util.EffectAddUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

public class PotionSlime extends BaseSlime<PotionSlime> {

	private static final EntityDataAccessor<String> ID_EFFECT = SynchedEntityData.defineId(PotionSlime.class, EntityDataSerializers.STRING);

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0).add(
						LootTableTemplate.getItem(Items.SLIME_BALL, 0, 2, 1))));
	}

	private static final ThreadLocal<String> TEMP = new ThreadLocal<>();

	public PotionSlime(EntityType<PotionSlime> type, Level level) {
		super(type, level);
		String str = TEMP.get();
		if (str != null && str.length() > 0)
			this.entityData.set(ID_EFFECT, str);
	}

	protected SlimeProperties.SlimeConfig getConfig() {
		SlimeProperties ins = SlimeProperties.getInstance();
		if (getPotionID().length() == 0 || ins == null) return SlimeProperties.DEF;
		return ins.map.getOrDefault(getPotionID(), SlimeProperties.DEF);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_EFFECT, "");
	}

	protected String getPotionID() {
		return this.entityData.get(ID_EFFECT);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		MobEffectInstance ins = getConfig().ins;
		if (ins.getEffect().isBeneficial() && !hasEffect(ins.getEffect())) {
			EffectAddUtil.addEffect(this, ins, EffectAddUtil.AddReason.SELF, this);
		}
	}

	@Override
	public void doEnchantDamageEffects(LivingEntity self, Entity target) {
		super.doEnchantDamageEffects(self, target);
		MobEffectInstance ins = getConfig().ins;
		if (!ins.getEffect().isBeneficial() && target instanceof LivingEntity le) {
			EffectAddUtil.addEffect(le, ins, EffectAddUtil.AddReason.NONE, self);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("slime_potion")) {
			this.entityData.set(ID_EFFECT, tag.getString("slime_potion"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("slime_potion", getPotionID());
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
		setupPotion();
		return super.finalizeSpawn(level, difficulty, type, groupData, tag);
	}

	public void setupPotion() {
		if (getConfig() == SlimeProperties.DEF) {
			this.entityData.set(ID_EFFECT, SlimeProperties.getRandomConfig(level.getRandom()));
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		TEMP.set(getPotionID());
		super.remove(reason);
		TEMP.set(null);
	}

	@Override
	protected void dropFromLootTable(DamageSource source, boolean by_player) {
		super.dropFromLootTable(source, by_player);
		if (getSize() == 1 && by_player && this.lastHurtByPlayer != null) {
			if (random.nextDouble() < getConfig().chance) {
				spawnAtLocation(new ItemStack(getConfig().drop));
			}
		}
	}
}
