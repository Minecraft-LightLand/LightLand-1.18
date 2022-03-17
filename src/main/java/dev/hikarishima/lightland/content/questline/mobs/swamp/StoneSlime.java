package dev.hikarishima.lightland.content.questline.mobs.swamp;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.content.questline.common.mobs.LootTableTemplate;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public class StoneSlime extends MaterialSlime<StoneSlime> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.SLIME_BALL, 0, 2, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.COBBLESTONE, 0, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(ItemRegistrate.DIRTY_SLIME.get(), 1))
						.when(LootTableTemplate.chance(0.2f))
						.when(LootTableTemplate.byPlayer())));
	}

	public StoneSlime(EntityType<StoneSlime> type, Level level) {
		super(type, level);
	}

	@Override
	protected float damageFactor(DamageSource source, ItemStack stack, float damage) {
		if (stack.getItem() instanceof PickaxeItem) {
			return damage * 3;
		}
		return damage;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void setSize(int size, boolean regen) {
		super.setSize(size, regen);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((size + 3) * (size + 3));
		this.getAttribute(Attributes.ARMOR).setBaseValue(size * 5);
		if (regen) {
			this.setHealth(this.getMaxHealth());
		}
	}

	@Override
	protected float getAttackDamage() {
		return super.getAttackDamage() + 2;
	}

}
