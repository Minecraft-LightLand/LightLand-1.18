package dev.xkmc.lightland.content.magic.products.info;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.advancements.FrameType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@SerialClass
public class DisplayInfo {

	public static final float SCALE = 1;

	@SerialClass.SerialField
	public int x, y;

	@SerialClass.SerialField
	public FrameType type = FrameType.TASK;

	@SerialClass.SerialField
	public Item icon = Items.PAPER;

	public float getX() {
		return x * SCALE;
	}

	public float getY() {
		return y * SCALE;
	}

	public FrameType getFrame() {
		return type;
	}

	public ItemStack getIcon() {
		return icon.getDefaultInstance();
	}
}
