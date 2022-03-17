package dev.hikarishima.lightland.content.questline.mobs.swamp;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

public class BaseSlime<T extends BaseSlime<T>> extends Slime {

	public BaseSlime(EntityType<T> type, Level level) {
		super(type, level);
	}

	@Override
	public final Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void setSize(int size, boolean regen) {
		super.setSize(size, regen);
	}

	@Override
	public void remove(RemovalReason reason) {
		level.getEntitiesOfClass(BossSlime.class, new AABB(position(), position()).inflate(8))
				.forEach(e -> e.seeDeath(this));
		super.remove(reason);
	}
}
