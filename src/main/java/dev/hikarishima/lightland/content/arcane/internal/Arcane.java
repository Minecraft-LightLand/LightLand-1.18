package dev.hikarishima.lightland.content.arcane.internal;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.lcy0x1.base.NamedEntry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class Arcane extends NamedEntry<Arcane> {

    public final ArcaneType type;

    public final int cost;

    public Arcane(ArcaneType type, int cost) {
        super(() -> LightLandRegistry.ARCANE);
        this.type = type;
        this.cost = cost;
    }

    public abstract boolean activate(Player player, LLPlayerData magic, ItemStack stack, LivingEntity target);
}
