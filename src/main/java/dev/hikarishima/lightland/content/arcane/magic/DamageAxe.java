package dev.hikarishima.lightland.content.arcane.magic;

import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DamageAxe extends Arcane {

    private final float damage;

    public DamageAxe(int cost, float damage) {
        super(ArcaneType.DUBHE, cost);
        this.damage = damage;
    }

    @Override
    public boolean activate(Player player, LLPlayerData magic, ItemStack stack, LivingEntity target) {
        if (target == null)
            return false;
        Level w = player.level;
        if (w.isClientSide())
            return true;
        /*MagicDamageSource source = new MagicDamageSource(player, player);
        source.add(new MagicDamageEntry(DamageSource.playerAttack(player).bypassArmor(), damage));
        source.add(new MagicDamageEntry(DamageSource.playerAttack(player).bypassMagic(), damage));
        source.add(new MagicDamageEntry(DamageSource.playerAttack(player).bypassArmor().bypassMagic(), damage)
                .setPost(ArcaneRegistry.postDamage(stack)));
        target.hurt(source, damage);*/ //TODO
        return true;
    }

}
