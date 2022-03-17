package dev.hikarishima.lightland.content.questline.mobs.swamp;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class BaseSlime<T extends BaseSlime<T>> extends Slime {

	public BaseSlime(EntityType<T> type, Level level) {
		super(type, level);
	}

	@Override
	public final Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
