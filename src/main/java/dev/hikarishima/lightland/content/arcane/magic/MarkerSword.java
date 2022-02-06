package dev.hikarishima.lightland.content.arcane.magic;

import dev.hikarishima.lightland.content.arcane.ArcaneRegistry;
import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import net.minecraft.world.effect.MobEffectInstance;
import dev.hikarishima.lightland.util.LightLandFakeEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class MarkerSword extends Arcane {

    public final float radius;

    public MarkerSword(int cost, float radius) {
        super(ArcaneType.ALKAID, cost);
        this.radius = radius;
    }

    @Override
    public boolean activate(Player player, LLPlayerData magic, ItemStack stack, LivingEntity target) {
        Level w = player.level;
        if (!w.isClientSide()) {
            w.getEntities(player, new AABB(player.blockPosition()).inflate(radius), e -> {
                if (!(e instanceof Mob))
                    return false;
                return e != target && !e.isAlliedTo(e);
            }).forEach(e -> LightLandFakeEntity.addEffect((LivingEntity) e, new MobEffectInstance(VanillaMagicRegistrate.ARCANE.get(), ArcaneRegistry.ARCANE_TIME), player));
        }
        return true;
    }
}
