package dev.hikarishima.lightland.content.skill;

import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.network.config.SkillDataConfig;
import dev.hikarishima.lightland.util.annotation.DoubleSidedCall;
import dev.hikarishima.lightland.util.annotation.ServerOnly;
import dev.lcy0x1.base.NamedEntry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class Skill<C extends SkillConfig<D>, D extends SkillData> extends NamedEntry<Skill<?, ?>> {

    public Skill() {
        super(() -> LightLandRegistry.SKILL);
    }

    /**
     * checked by client first. When client pass the check, check on server and then activate
     */
    @DoubleSidedCall
    public boolean canActivate(Level level, Player player, D data) {
        return data.cooldown == 0 && getConfig() != null;
    }

    @ServerOnly
    public void activate(Level level, ServerPlayer player, D data) {
        data.cooldown = getConfig().getCooldown(data);
    }

    @DoubleSidedCall
    public C getConfig() {
        return SkillDataConfig.getConfig(getRegistryName());
    }

}
