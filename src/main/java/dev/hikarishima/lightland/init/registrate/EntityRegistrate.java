package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.hikarishima.lightland.content.archery.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.entity.GenericArrowRenderer;
import net.minecraft.world.entity.MobCategory;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class EntityRegistrate {

    public static final EntityEntry<GenericArrowEntity> ET_ARROW = REGISTRATE
            .<GenericArrowEntity>entity("generic_arrow", GenericArrowEntity::new, MobCategory.MISC)
            .properties(e -> e.sized(0.5F, 0.5F)
                    .clientTrackingRange(4).updateInterval(20)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> GenericArrowRenderer::new)
            .defaultLang()
            .register();

    public static void register() {
    }

}
