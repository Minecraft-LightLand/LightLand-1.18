package dev.hikarishima.lightland.content.common.capability;

import dev.hikarishima.lightland.content.skill.Skill;
import dev.hikarishima.lightland.content.skill.SkillConfig;
import dev.hikarishima.lightland.content.skill.SkillData;
import dev.hikarishima.lightland.util.annotation.DoubleSidedCall;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

@SerialClass
public class SkillCap {

    @SerialClass
    public static class Cont<S extends Skill<C, D>, C extends SkillConfig<D>, D extends SkillData> {

        @SerialClass.SerialField
        public S skill;

        @SerialClass.SerialField
        public D data;

        @Deprecated
        public Cont() {

        }

        public Cont(S skill) {
            this.skill = skill;
            this.data = skill.genData();
        }

        @DoubleSidedCall
        public boolean canActivate(Level level, Player player) {
            return skill.canActivate(level, player, data);
        }

        public void activate(Level level, ServerPlayer player) {
            skill.activate(level, player, data);
        }
    }

    private final LLPlayerData parent;

    @SerialClass.SerialField(generic = Cont.class)
    public ArrayList<Cont<?, ?, ?>> list = new ArrayList<>();

    public SkillCap(LLPlayerData parent) {
        this.parent = parent;
    }

}
