package dev.xkmc.lightland.content.archery.feature.arrow;

import dev.xkmc.lightland.content.archery.feature.types.OnHitFeature;
import dev.xkmc.lightland.content.common.entity.GenericArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EnderArrowFeature implements OnHitFeature {

	@Override
	public void onHitEntity(GenericArrowEntity genericArrow, LivingEntity target) {
		Entity owner = genericArrow.getOwner();
		if (owner != null) {
			Vec3 pos = owner.getPosition(1);
			Vec3 tpos = target.getPosition(1);
			owner.teleportTo(tpos.x, tpos.y, tpos.z);
			target.teleportTo(pos.x, pos.y, pos.z);
		}
	}

	@Override
	public void onHitBlock(GenericArrowEntity genericArrow, BlockHitResult result) {
		Entity owner = genericArrow.getOwner();
		if (owner != null) {
			Vec3 pos = result.getLocation();
			owner.teleportTo(pos.x, pos.y, pos.z);
		}
		genericArrow.discard();
	}
}
