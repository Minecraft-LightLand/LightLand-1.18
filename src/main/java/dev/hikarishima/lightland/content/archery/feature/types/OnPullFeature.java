package dev.hikarishima.lightland.content.archery.feature.types;

import dev.hikarishima.lightland.content.archery.feature.BowArrowFeature;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.xkmc.l2library.util.GenericItemStack;
import net.minecraft.world.entity.player.Player;

public interface OnPullFeature extends BowArrowFeature {

	void onPull(Player player, GenericItemStack<GenericBowItem> bow);

	void tickAim(Player player, GenericItemStack<GenericBowItem> bow);

	void stopAim(Player player, GenericItemStack<GenericBowItem> bow);

}
