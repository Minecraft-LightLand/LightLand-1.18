package dev.hikarishima.lightland.content.questline.mobs.layline;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.content.questline.common.mobs.BipedMonster;
import dev.hikarishima.lightland.content.questline.common.mobs.LootTableTemplate;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public class LaylineZombie extends BipedMonster<LaylineZombie> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(Items.ROTTEN_FLESH, 0, 2, 1)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(ItemRegistrate.ENC_GOLD_NUGGET.get(), 0, 1))
						.when(LootTableTemplate.byPlayer())
						.when(LootTableTemplate.chance(0.1f, 0.01f)))
		);
	}

	public LaylineZombie(EntityType<LaylineZombie> type, Level level) {
		super(type, level, LaylineProperties.CONFIG_ZOMBIE);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, true,
				e -> LaylineProperties.CONVERT_TYPE.contains(e.getType())));
	}

	@Override
	public void killed(ServerLevel level, LivingEntity target) {
		LaylineProperties.convert(level, target);
	}

	@Override
	public void remove(RemovalReason reason) {
		if (!this.level.isClientSide && this.isDeadOrDying()) {
			LaylineProperties.convert((ServerLevel) level, this);
		}
		super.remove(reason);
	}
}
