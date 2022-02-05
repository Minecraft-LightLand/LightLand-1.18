package dev.hikarishima.lightland.content.archery.controller;

import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.util.GenericItemStack;

public class BowFeatureController {

    public static int getMaxPullTime(GenericItemStack<GenericBowItem> bow){
        return 72000;
    }

    public static void startUsing(GenericItemStack<GenericBowItem> bow) {

    }

    public static void stopUsing(GenericItemStack<GenericBowItem> bow) {

    }
}
