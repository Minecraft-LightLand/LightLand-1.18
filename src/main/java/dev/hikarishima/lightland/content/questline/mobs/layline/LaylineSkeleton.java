package dev.hikarishima.lightland.content.questline.mobs.layline;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.util.LootTableTemplate;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public class LaylineSkeleton extends BaseLaylineMob<LaylineSkeleton> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.BONE, 0, 2, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(LightlandItems.LAYLINE_ORB.get(), 1))
						.when(LootTableTemplate.byPlayer())
						.when(LootTableTemplate.chance(0.1f, 0.01f)))
		);
	}

	public LaylineSkeleton(EntityType<LaylineSkeleton> type, Level level) {
		super(type, level, LaylineProperties.CONFIG_SKELETON);
	}

}
