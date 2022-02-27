package dev.hikarishima.lightland.content.skill;

import dev.hikarishima.lightland.util.math.RayTraceUtil;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class MovementSkill extends Skill<MovementSkill.Config, MovementSkill.Data> {

    @SerialClass
    public static class Data extends SkillData {

        @SerialClass.SerialField
        public Vec3 velocity = Vec3.ZERO;

        @SerialClass.SerialField
        public int time = 0;

        @SerialClass.SerialField
        public boolean init = false;

        public void activate(Config config, ServerPlayer player) {
            int lv = Math.min(level, config.max_level);
            time = config.time[lv];
            velocity = RayTraceUtil.getRayTerm(Vec3.ZERO, player.getXRot(), player.getYRot(), config.velocity[lv]);
            init = true;
        }

    }

    @SerialClass
    public static class Config extends SkillConfig<Data> {

        @SerialClass.SerialField
        public int[] time;

        @SerialClass.SerialField
        public double[] velocity;

        @Override
        public boolean isValid() {
            if (!super.isValid()) return false;
            if (time.length != max_level) return false;
            if (velocity.length != max_level) return false;
            for (int i = 0; i < max_level; i++) {
                int t = time[i];
                double v = velocity[i];
                if (t <= 0 || t >= cooldown[i])
                    return false;
                if (v <= 0.25 || v >= 3)
                    return false;
            }
            return true;
        }

    }

    @Override
    public Data genData(Player player) {
        return new Data();
    }

    @Override
    public void activate(Level level, ServerPlayer player, Data data) {
        data.activate(getConfig(), player);
        super.activate(level, player, data);
    }

    @Override
    public void tick(Player player, Data data) {
        super.tick(player, data);
        if (data.time > 0) {
            if (player.level.isClientSide()) {
                player.setDeltaMovement(data.velocity);
                player.makeStuckInBlock(Blocks.AIR.defaultBlockState(), Vec3.ZERO);
            } else {
                if (data.init) {
                    data.init = false;
                    player.hasImpulse = true;
                }
                player.setDeltaMovement(data.velocity);
                player.makeStuckInBlock(Blocks.AIR.defaultBlockState(), Vec3.ZERO);
            }
            data.time--;
            if (data.time <= 0) {
                data.time = 0;
                data.velocity = Vec3.ZERO;
            }
        }
    }
}
