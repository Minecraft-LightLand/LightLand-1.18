package dev.hikarishima.lightland.content.questline.mobs.layline;

import dev.hikarishima.lightland.content.questline.common.mobs.BaseMonster;
import dev.hikarishima.lightland.content.questline.common.mobs.SimpleEquipment;
import dev.hikarishima.lightland.content.questline.common.mobs.SoundPackage;
import dev.hikarishima.lightland.init.data.GenItem;
import dev.hikarishima.lightland.init.registrate.EntityRegistrate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings({"rawtype", "unchecked", "unsafe"})
public class LaylineProperties {

	public static final SoundPackage SOUND_ZOMBIE = new SoundPackage(SoundEvents.ZOMBIE_AMBIENT, SoundEvents.ZOMBIE_HURT, SoundEvents.ZOMBIE_DEATH, SoundEvents.ZOMBIE_STEP);
	public static final SoundPackage SOUND_SKELETON = new SoundPackage(SoundEvents.SKELETON_AMBIENT, SoundEvents.SKELETON_HURT, SoundEvents.SKELETON_DEATH, SoundEvents.SKELETON_STEP);

	private static final Function<EquipmentSlot, SimpleEquipment> ARMOR_GEN = (slot) -> genEquip(slot, 0.8, 10, 30,
			wrap(GenItem.Mats.LAYROOT.getArmor(slot), 70), wrap(GenItem.Mats.LAYLINE.getArmor(slot), 10));

	public static final SimpleEquipment LAYLINE_HEAD = ARMOR_GEN.apply(EquipmentSlot.HEAD);
	public static final SimpleEquipment LAYLINE_CHEST = ARMOR_GEN.apply(EquipmentSlot.CHEST);
	public static final SimpleEquipment LAYLINE_LEGS = ARMOR_GEN.apply(EquipmentSlot.LEGS);
	public static final SimpleEquipment LAYLINE_FEET = ARMOR_GEN.apply(EquipmentSlot.FEET);
	public static final SimpleEquipment LAYLINE_MEELEE = genEquip(EquipmentSlot.MAINHAND, 1, 10, 30,
			wrap(GenItem.Mats.LAYROOT.getTool(GenItem.Tools.SWORD), 100), wrap(GenItem.Mats.LAYLINE.getTool(GenItem.Tools.SWORD), 10),
			wrap(GenItem.Mats.LAYROOT.getTool(GenItem.Tools.AXE), 50), wrap(GenItem.Mats.LAYLINE.getTool(GenItem.Tools.AXE), 5),
			wrap(GenItem.Mats.LAYROOT.getTool(GenItem.Tools.PICKAXE), 100), wrap(GenItem.Mats.LAYLINE.getTool(GenItem.Tools.PICKAXE), 10),
			wrap(GenItem.Mats.LAYROOT.getTool(GenItem.Tools.SHOVEL), 50), wrap(GenItem.Mats.LAYLINE.getTool(GenItem.Tools.SHOVEL), 5),
			wrap(GenItem.Mats.LAYROOT.getTool(GenItem.Tools.HOE), 50), wrap(GenItem.Mats.LAYLINE.getTool(GenItem.Tools.HOE), 5));

	public static final SimpleEquipment LAYLINE_BOW = genEquip(EquipmentSlot.MAINHAND, 1, 10, 30,
			wrap(Items.BOW, 100));

	public static final Set<EntityType<?>> ALLY_TYPE = Set.of(
			EntityRegistrate.ET_LAYLINE_ZOMBIE.get(), EntityRegistrate.ET_LAYLINE_SKELETON.get());

	public static final BaseMonster.EntityConfig CONFIG_ZOMBIE = new BaseMonster.EntityConfig(MobType.UNDEAD, SOUND_ZOMBIE,
			List.of(LAYLINE_HEAD, LAYLINE_CHEST, LAYLINE_LEGS, LAYLINE_FEET, LAYLINE_MEELEE), List.of(), ALLY_TYPE);

	public static final BaseMonster.EntityConfig CONFIG_SKELETON = new BaseMonster.EntityConfig(MobType.UNDEAD, SOUND_SKELETON,
			List.of(LAYLINE_HEAD, LAYLINE_CHEST, LAYLINE_LEGS, LAYLINE_FEET, LAYLINE_BOW), List.of(), ALLY_TYPE);

	public static final Set<EntityType<?>> CONVERT_TYPE_ZOMBIE = Set.of(
			EntityType.PLAYER, EntityType.VILLAGER, EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK,
			EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.ZOMBIE_VILLAGER,
			EntityType.PILLAGER, EntityType.ILLUSIONER, EntityType.EVOKER, EntityType.VINDICATOR,
			EntityType.WITCH, EntityType.WANDERING_TRADER
	);

	public static final Set<EntityType<?>> CONVERT_TYPE_SKELETON = Set.of(
			EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON
	);

	public static final Set<EntityType<?>> CONVERT_TYPE = new HashSet<>();

	static {
		CONVERT_TYPE.addAll(CONVERT_TYPE_ZOMBIE);
		CONVERT_TYPE.addAll(CONVERT_TYPE_SKELETON);
	}


	public static WeightedEntry.Wrapper<Item> wrap(Item item, int weight) {
		return WeightedEntry.wrap(item, weight);
	}

	public static SimpleEquipment genEquip(EquipmentSlot slot, double chance, int lv1, int lv2, WeightedEntry.Wrapper<Item>... items) {
		return new SimpleEquipment(slot, WeightedRandomList.create(items), chance, lv1, lv2);
	}

	public static void convert(ServerLevel level, LivingEntity target) {
		Monster monster = null;
		if (LaylineProperties.CONVERT_TYPE_ZOMBIE.contains(target.getType()))
			monster = new LaylineZombie(EntityRegistrate.ET_LAYLINE_ZOMBIE.get(), level);
		if (LaylineProperties.CONVERT_TYPE_SKELETON.contains(target.getType()))
			monster = new LaylineSkeleton(EntityRegistrate.ET_LAYLINE_SKELETON.get(), level);
		if (target.getType() == EntityRegistrate.ET_LAYLINE_ZOMBIE.get())
			monster = new LaylineSkeleton(EntityRegistrate.ET_LAYLINE_SKELETON.get(), level);
		if (monster != null) {
			monster.copyPosition(target);
			if (target.hasCustomName())
				monster.setCustomName(target.getCustomName());
			else if (target instanceof Player player)
				target.setCustomName(player.getDisplayName());
			if (!(target instanceof Player))
				for (EquipmentSlot slot : EquipmentSlot.values()) {
					ItemStack stack = target.getItemBySlot(slot);
					if (stack.isEmpty()) continue;
					monster.setItemSlot(slot, stack);
					monster.setDropChance(slot, 0);
				}
			monster.finalizeSpawn(level, level.getCurrentDifficultyAt(monster.blockPosition()),
					MobSpawnType.CONVERSION, null, null);
			level.addFreshEntity(monster);
		}
	}

}
