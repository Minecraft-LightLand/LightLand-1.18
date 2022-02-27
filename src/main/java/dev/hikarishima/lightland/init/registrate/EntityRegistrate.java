package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.hikarishima.lightland.content.common.entity.*;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
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
            .<WindBladeEntity>entity("wind_blade", WindBladeEntity::new, MobCategory.MISC)
            .properties(e -> e.sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(20).fireImmune())
            .renderer(() -> WindBladeEntityRenderer::new)
            .defaultLang().register();


    public static final EntityEntry<SpellEntity> ET_SPELL = REGISTRATE
            .<SpellEntity>entity("spell", SpellEntity::new, MobCategory.MISC)
            .properties(e -> e.sized(3f, 3f)
                    .setShouldReceiveVelocityUpdates(true)
                    .fireImmune().updateInterval(20))
            .renderer(() -> SpellEntityRenderer::new)
            .defaultLang().register();

    public static final EntityEntry<FireArrowEntity> ET_FIRE_ARROW = REGISTRATE
            .<FireArrowEntity>entity("fire_arrow", FireArrowEntity::new, MobCategory.MISC)
            .properties(e -> e.sized(1f, 1f).clientTrackingRange(4).updateInterval(20))
            .renderer(() -> TippableArrowRenderer::new)
            .defaultLang().register();

    public static final EntityEntry<MagicFireBallEntity> ET_FIRE_BALL = REGISTRATE
            .<MagicFireBallEntity>entity("fire_ball", MagicFireBallEntity::new, MobCategory.MISC)
            .properties(e -> e.sized(1f, 1f).clientTrackingRange(4).updateInterval(10))
            .renderer(() -> ctx -> new SpecialSpriteRenderer<>(ctx, ctx.getItemRenderer(), true))
            .defaultLang().register();

    public static void register() {
    }

}
