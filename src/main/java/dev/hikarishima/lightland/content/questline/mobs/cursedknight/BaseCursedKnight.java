package dev.hikarishima.lightland.content.questline.mobs.cursedknight;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.hikarishima.lightland.content.questline.common.mobs.AlertClassGoal;
import dev.hikarishima.lightland.content.questline.common.mobs.BipedMonster;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.xkmc.l2library.util.LootTableTemplate;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;

public class BaseCursedKnight<T extends BaseCursedKnight<T>> extends BipedMonster<T> {

	public static void loot(RegistrateEntityLootTables table, EntityType<?> type) {
		table.add(type, new LootTable.Builder()
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(LightlandItems.KNIGHT_SCRAP.get(), 0, 1, 1))
						.when(LootTableTemplate.byPlayer()))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(LightlandItems.CURSED_DROPLET.get(), 0, 1))
						.when(LootTableTemplate.byPlayer())
						.when(LootTableTemplate.chance(0.1f, 0.01f)))
		);
	}

	private static final int RANGE = 10;

	private final MobEffect eff;

	protected BaseCursedKnight(EntityType<T> type, Level level, EntityConfig config, MobEffect eff) {
		super(type, level, config);
		this.eff = eff;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!level.isClientSide() && tickCount % 20 == 0) {
			for (BaseCursedKnight<?> e : level.getEntities(EntityTypeTest.forClass(BaseCursedKnight.class),
					new AABB(position(), position()).inflate(RANGE), e -> true)) {
				if (distanceToSqr(e) > RANGE * RANGE) continue;
				addCurseEffect(e);
			}
			addCurseEffect(this);
		}
	}

	private void addCurseEffect(BaseCursedKnight<?> e) {
		if (e.hasEffect(eff))
			return;
		e.addEffect(new MobEffectInstance(eff, 100));
	}


	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new AlertClassGoal(this, BaseCursedKnight.class).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

}
