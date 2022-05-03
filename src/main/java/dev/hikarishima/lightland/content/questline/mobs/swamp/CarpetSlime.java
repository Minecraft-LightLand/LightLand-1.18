package dev.hikarishima.lightland.content.questline.mobs.swamp;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.init.registrate.LightlandBlocks;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.xkmc.l2library.util.LootTableTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public class CarpetSlime extends MaterialSlime<CarpetSlime> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.SLIME_BALL, 0, 2, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(LightlandItems.UNSTABLE_SLIME.get(), 1))
						.when(LootTableTemplate.chance(0.05f))
						.when(LootTableTemplate.byPlayer())));
	}

	public CarpetSlime(EntityType<CarpetSlime> type, Level level) {
		super(type, level);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (isOnGround()) {
			int r = getSize() >= 3 ? 1 : 0;
			for (int x = -r; x <= r; x++)
				for (int z = -r; z <= r; z++) {
					BlockPos pos = blockPosition().offset(x, 0, z);
					if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).isCollisionShapeFullBlock(level, pos.below()))
						level.setBlockAndUpdate(pos, LightlandBlocks.SLIME_CARPET.getDefaultState());
				}
		}
	}

	@Override
	protected float getAttackDamage() {
		return super.getAttackDamage() + 1;
	}

}
