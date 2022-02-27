package dev.hikarishima.lightland.content.common.capability;

import dev.hikarishima.lightland.content.skill.Skill;
import dev.hikarishima.lightland.content.skill.SkillConfig;
import dev.hikarishima.lightland.content.skill.SkillData;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.network.packets.SkillToServer;
import dev.hikarishima.lightland.util.annotation.DoubleSidedCall;
import dev.hikarishima.lightland.util.annotation.ServerOnly;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

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

        @ServerOnly
        public Cont(ServerPlayer player, S skill) {
            this.skill = skill;
            this.data = skill.genData(player);
        }

        @DoubleSidedCall
        public boolean canActivate(Level level, Player player) {
            return skill.canActivate(level, player, data);
        }

        @ServerOnly
        public void activate(Level level, ServerPlayer player) {
            skill.activate(level, player, data);
            refreshCooldown();
        }

        @ServerOnly
        public void refreshCooldown() {
            if (skill.getConfig() == null) return;
            data.cooldown = skill.getConfig().getCooldown(data);
        }

        @DoubleSidedCall
        public void tick(Player player) {
            skill.tick(player, data);
        }
    }

    private final LLPlayerData parent;

    @SerialClass.SerialField(generic = Cont.class)
    public ArrayList<Cont<?, ?, ?>> list = new ArrayList<>();

    public SkillCap(LLPlayerData parent) {
        this.parent = parent;
    }

    @DoubleSidedCall
    public void tick() {
        for (int i = 0; i < list.size(); i++) {
            Cont<?, ?, ?> cont = list.get(i);
            cont.tick(parent.player);
            int I = i;
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                if (cont.data.cooldown == 0 && LangData.Keys.values()[I].map.isDown()) {
                    SkillToServer.clientActivate(I);
                }
            });
        }
    }

}
