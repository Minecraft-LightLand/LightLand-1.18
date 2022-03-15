package dev.hikarishima.lightland.content.questline.mobs.layline;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.geom.ModelPart;

public class LaylineZombieModel extends AbstractZombieModel<LaylineZombie> {

	protected LaylineZombieModel(ModelPart part) {
		super(part);
	}

	@Override
	public boolean isAggressive(LaylineZombie entity) {
		return entity.isAggressive();
	}
}
