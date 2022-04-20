package dev.hikarishima.lightland.content.questline.mobs.layline;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.lcy0x1.base.LootTableTemplate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public class LaylineZombie extends BaseLaylineMob<LaylineZombie> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.ROTTEN_FLESH, 0, 2, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(LightlandItems.LAYLINE_ORB.get(), 0, 1))
						.when(LootTableTemplate.byPlayer())
						.when(LootTableTemplate.chance(0.1f, 0.01f)))
		);
	}

	public LaylineZombie(EntityType<LaylineZombie> type, Level level) {
		super(type, level, LaylineProperties.CONFIG_ZOMBIE);
	}

	@Override
	public void remove(RemovalReason reason) {
		if (!this.level.isClientSide && this.isDeadOrDying()) {
			LaylineProperties.convert((ServerLevel) level, this);
		}
		super.remove(reason);
	}

}
