package dev.hikarishima.lightland.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record GenericItemStack<I extends Item>(I item, ItemStack stack){

}
