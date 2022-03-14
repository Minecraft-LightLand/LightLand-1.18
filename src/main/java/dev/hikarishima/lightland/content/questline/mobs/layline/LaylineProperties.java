package dev.hikarishima.lightland.content.questline.mobs.layline;

import dev.hikarishima.lightland.content.questline.common.mobs.SimpleEquipment;
import dev.hikarishima.lightland.content.questline.common.mobs.SoundPackage;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@SuppressWarnings({"rawtype", "unchecked", "unsafe"})
public class LaylineProperties {

	public static final SoundPackage SOUND_ZOMBIE = new SoundPackage(SoundEvents.ZOMBIE_AMBIENT, SoundEvents.ZOMBIE_HURT, SoundEvents.ZOMBIE_DEATH, SoundEvents.ZOMBIE_STEP);
	public static final SoundPackage SOUND_SKELETON = new SoundPackage(SoundEvents.SKELETON_AMBIENT, SoundEvents.SKELETON_HURT, SoundEvents.SKELETON_DEATH, SoundEvents.SKELETON_STEP);

	public static final SimpleEquipment LAYLINE_HEAD = genEquip(EquipmentSlot.HEAD, 0.8, 10, 30, wrap(Items.IRON_HELMET, 70), wrap(Items.DIAMOND_HELMET, 10));
	public static final SimpleEquipment LAYLINE_CHEST = genEquip(EquipmentSlot.CHEST, 0.8, 10, 30, wrap(Items.IRON_CHESTPLATE, 70), wrap(Items.DIAMOND_CHESTPLATE, 10));
	public static final SimpleEquipment LAYLINE_LEGS = genEquip(EquipmentSlot.LEGS, 0.8, 10, 30, wrap(Items.IRON_LEGGINGS, 70), wrap(Items.DIAMOND_LEGGINGS, 10));
	public static final SimpleEquipment LAYLINE_FEET = genEquip(EquipmentSlot.FEET, 0.8, 10, 30, wrap(Items.IRON_BOOTS, 70), wrap(Items.DIAMOND_BOOTS, 10));
	public static final SimpleEquipment LAYLINE_MEELEE = genEquip(EquipmentSlot.MAINHAND, 1, 10, 30,
			wrap(Items.IRON_SWORD, 100), wrap(Items.DIAMOND_SWORD, 10),
			wrap(Items.IRON_AXE, 50), wrap(Items.DIAMOND_AXE, 5),
			wrap(Items.IRON_PICKAXE, 100), wrap(Items.DIAMOND_PICKAXE, 10),
			wrap(Items.IRON_SHOVEL, 50), wrap(Items.DIAMOND_SHOVEL, 5),
			wrap(Items.IRON_HOE, 50), wrap(Items.DIAMOND_HOE, 5));

	public static WeightedEntry.Wrapper<Item> wrap(Item item, int weight) {
		return WeightedEntry.wrap(item, weight);
	}

	public static SimpleEquipment genEquip(EquipmentSlot slot, double chance, int lv1, int lv2, WeightedEntry.Wrapper<Item>... items) {
		return new SimpleEquipment(slot, WeightedRandomList.create(items), chance, lv1, lv2);
	}

}
