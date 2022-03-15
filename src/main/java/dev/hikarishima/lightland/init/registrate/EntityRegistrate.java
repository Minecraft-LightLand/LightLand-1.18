package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.hikarishima.lightland.content.common.entity.*;
import dev.hikarishima.lightland.content.questline.mobs.cursedknight.*;
import dev.hikarishima.lightland.content.questline.mobs.layline.LaylineSkeleton;
import dev.hikarishima.lightland.content.questline.mobs.layline.LaylineSkeletonRenderer;
import dev.hikarishima.lightland.content.questline.mobs.layline.LaylineZombie;
import dev.hikarishima.lightland.content.questline.mobs.layline.LaylineZombieRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class EntityRegistrate {

	public static final EntityEntry<GenericArrowEntity> ET_ARROW = REGISTRATE
			.<GenericArrowEntity>entity("generic_arrow", GenericArrowEntity::new, MobCategory.MISC)
			.properties(e -> e.sized(0.5F, 0.5F)
					.clientTrackingRange(4).updateInterval(20)
					.setShouldReceiveVelocityUpdates(true))
			.renderer(() -> GenericArrowRenderer::new)
			.defaultLang().register();

	public static final EntityEntry<WindBladeEntity> ET_WIND_BLADE = REGISTRATE
			.<WindBladeEntity>entity("wind_blade", WindBladeEntity::new, MobCategory.MISC)
			.properties(e -> e.sized(0.5f, 0.5f)
					.clientTrackingRange(4)
					.setShouldReceiveVelocityUpdates(true)
					.updateInterval(20).fireImmune())
			.renderer(() -> WindBladeEntityRenderer::new)
			.defaultLang().register();


	public static final EntityEntry<SpellEntity> ET_SPELL = REGISTRATE
			.<SpellEntity>entity("spell", SpellEntity::new, MobCategory.MISC)
			.properties(e -> e.sized(3f, 3f)
					.setShouldReceiveVelocityUpdates(true)
					.fireImmune().updateInterval(20))
			.renderer(() -> SpellEntityRenderer::new)
			.defaultLang().register();

	public static final EntityEntry<FireArrowEntity> ET_FIRE_ARROW = REGISTRATE
			.<FireArrowEntity>entity("fire_arrow", FireArrowEntity::new, MobCategory.MISC)
			.properties(e -> e.sized(1f, 1f).clientTrackingRange(4).updateInterval(20))
			.renderer(() -> TippableArrowRenderer::new)
			.defaultLang().register();

	public static final EntityEntry<MagicFireBallEntity> ET_FIRE_BALL = REGISTRATE
			.<MagicFireBallEntity>entity("fire_ball", MagicFireBallEntity::new, MobCategory.MISC)
			.properties(e -> e.sized(1f, 1f).clientTrackingRange(4).updateInterval(10))
			.renderer(() -> ctx -> new SpecialSpriteRenderer<>(ctx, ctx.getItemRenderer(), true))
			.defaultLang().register();

	public static final EntityEntry<LaylineZombie> ET_LAYLINE_ZOMBIE = REGISTRATE
			.entity("layline_zombie", LaylineZombie::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.6f, 1.95f).clientTrackingRange(8))
			.renderer(() -> LaylineZombieRenderer::new).loot(LaylineZombie::loot).defaultLang().register();

	public static final EntityEntry<LaylineSkeleton> ET_LAYLINE_SKELETON = REGISTRATE
			.entity("layline_skeleton", LaylineSkeleton::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.6f, 1.95f).clientTrackingRange(8))
			.renderer(() -> LaylineSkeletonRenderer::new).loot(LaylineSkeleton::loot).defaultLang().register();


	public static final EntityEntry<CursedKnight> ET_CURSED_KNIGHT = REGISTRATE
			.entity("cursed_knight", CursedKnight::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.6f, 1.95f).clientTrackingRange(8))
			.renderer(() -> CursedKnightRenderer::new).loot(BaseCursedKnight::loot).defaultLang().register();


	public static final EntityEntry<CursedArcher> ET_CURSED_ARCHER = REGISTRATE
			.entity("cursed_archer", CursedArcher::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.6f, 1.95f).clientTrackingRange(8))
			.renderer(() -> CursedKnightRenderer::new).loot(BaseCursedKnight::loot).defaultLang().register();


	public static final EntityEntry<CursedShield> ET_CURSED_SHIELD = REGISTRATE
			.entity("cursed_shield", CursedShield::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.6f, 1.95f).clientTrackingRange(8))
			.renderer(() -> CursedKnightRenderer::new).loot(BaseCursedKnight::loot).defaultLang().register();

	public static void register() {
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ET_LAYLINE_ZOMBIE.get(), Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25F)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.FOLLOW_RANGE, 35.0D).build());

		event.put(ET_LAYLINE_SKELETON.get(), Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25F)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.FOLLOW_RANGE, 35.0D).build());


		event.put(ET_CURSED_KNIGHT.get(), Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 6.0D)
				.add(Attributes.FOLLOW_RANGE, 35.0D).build());


		event.put(ET_CURSED_ARCHER.get(), Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.35F)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.FOLLOW_RANGE, 35.0D).build());


		event.put(ET_CURSED_SHIELD.get(), Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25F)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.FOLLOW_RANGE, 35.0D).build());

	}

}
