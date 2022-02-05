package dev.hikarishima.lightland.content.archery.feature.types;

import dev.hikarishima.lightland.content.archery.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.feature.BowArrowFeature;

public interface OnShootFeature extends BowArrowFeature {

    void onShoot(GenericArrowEntity entity);

}
