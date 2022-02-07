package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.hikarishima.lightland.content.common.entity.*;
import net.minecraft.world.entity.MobCategory;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class EntityRegistrate {

    public static final EntityEntry<GenericArrowEntity> ET_ARROW = REGISTRATE
            .<GenericArrowEntity>entity("generic_arrow", GenericArrowEntity::new, MobCategory.MISC)
            .properties(e -> e.sized(0.5F, 0.5F)
                    .clientTrackingRange(4).updateInterval(20)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> GenericArrowRenderer::new)
            .defaultLang().register();

    public static final EntityEntry<WindBladeEntity> ET_WIND_BLADE = REGISTRATE
            .<WindBladeEntity>entity("wind_blade",WindBladeEntity::new, MobCategory.MISC)
            .properties(e->e.sized(0.5f,0.5f)
                    .updateInterval(20).fireImmune())
            .renderer(()-> WindBladeEntityRenderer::new)
            .defaultLang().register();


    public static final EntityEntry<SpellEntity> ET_SPELL = REGISTRATE
            .<SpellEntity>entity("spell",SpellEntity::new, MobCategory.MISC)
            .properties(e->e.sized(3f,3f)
                    .setShouldReceiveVelocityUpdates(true)
                    .fireImmune().updateInterval(20))
            .renderer(()->SpellEntityRenderer::new)
            .defaultLang().register();

    public static void register() {
    }

}
