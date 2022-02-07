package dev.hikarishima.lightland.content.arcane.magic;

import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.util.math.RayTraceUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ThunderSword extends Arcane {

    public final float dist;

    public ThunderSword(int cost, float dist) {
        super(ArcaneType.ALKAID, cost);
        this.dist = dist;
    }

    @Override
    public boolean activate(Player player, LLPlayerData magic, ItemStack stack, LivingEntity target) {
        if (target == null) {
            target = RayTraceUtil.serverGetTarget(player);
            if (target == null) {
                return false;
            }
        }
        BlockPos pos = target.blockPosition();
        Level w = player.level;
        if (!w.isClientSide()) {
            LightningBolt e = new LightningBolt(EntityType.LIGHTNING_BOLT, w);
            e.moveTo(Vec3.atBottomCenterOf(pos));
            e.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
            w.addFreshEntity(e);
            e.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 5f, 1.0F);
        }
        return true;
    }
}
