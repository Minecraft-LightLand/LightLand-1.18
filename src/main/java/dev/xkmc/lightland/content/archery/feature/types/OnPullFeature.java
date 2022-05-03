package dev.xkmc.lightland.content.archery.feature.types;

import dev.xkmc.l2library.util.GenericItemStack;
import dev.xkmc.lightland.content.archery.feature.BowArrowFeature;
import dev.xkmc.lightland.content.archery.item.GenericBowItem;
import net.minecraft.world.entity.player.Player;

public interface OnPullFeature extends BowArrowFeature {

	void onPull(Player player, GenericItemStack<GenericBowItem> bow);

	void tickAim(Player player, GenericItemStack<GenericBowItem> bow);

	void stopAim(Player player, GenericItemStack<GenericBowItem> bow);

}
