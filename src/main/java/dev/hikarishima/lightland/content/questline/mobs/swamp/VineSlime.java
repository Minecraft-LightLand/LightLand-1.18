package dev.hikarishima.lightland.content.questline.mobs.swamp;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.init.registrate.LightlandBlocks;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.lcy0x1.base.LootTableTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public class VineSlime extends MaterialSlime<VineSlime> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.SLIME_BALL, 0, 2, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.VINE, 0, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(LightlandItems.DIRTY_SLIME.get(), 1))
						.when(LootTableTemplate.chance(0.2f))
						.when(LootTableTemplate.byPlayer())));
	}

	public VineSlime(EntityType<VineSlime> type, Level level) {
		super(type, level);
	}

	@Override
	protected float damageFactor(DamageSource source, ItemStack stack, float damage) {
		if (source.isProjectile()) {
			return damage / 2;
		}
		if (stack.getItem() instanceof AxeItem) {
			return damage * 2;
		}
		return damage;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (tickCount % 100 == 0) {
			this.heal(1);
		}
	}

	@Override
	public void actuallyHurt(DamageSource source, float damage) {
		super.actuallyHurt(source, damage);
		if (isDeadOrDying() && source.getDirectEntity() instanceof LivingEntity le) {
			if (!source.isExplosion() && !source.isMagic()) {
				BlockPos pos = le.blockPosition();
				if (level.getBlockState(pos).isAir())
					level.setBlockAndUpdate(pos, LightlandBlocks.SLIME_VINE.getDefaultState());
				else if (level.getBlockState(pos.above()).isAir())
					level.setBlockAndUpdate(pos.above(), LightlandBlocks.SLIME_VINE.getDefaultState());
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void setSize(int size, boolean regen) {
		super.setSize(size, regen);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((size + 1) * (size + 1));
		this.getAttribute(Attributes.ARMOR).setBaseValue(size * 2);
		this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(size);
		if (regen) {
			this.setHealth(this.getMaxHealth());
		}
	}

	@Override
	protected float getAttackDamage() {
		return super.getAttackDamage() + 1;
	}

}
