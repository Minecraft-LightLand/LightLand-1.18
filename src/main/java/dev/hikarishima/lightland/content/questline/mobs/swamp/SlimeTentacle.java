package dev.hikarishima.lightland.content.questline.mobs.swamp;

import dev.hikarishima.lightland.content.common.entity.ISizedItemEntity;
import dev.hikarishima.lightland.init.registrate.EntityRegistrate;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class SlimeTentacle extends Projectile implements ISizedItemEntity, ItemSupplier {

	private static final EntityDataAccessor<Integer> DATA_OWNER = SynchedEntityData.defineId(FishingHook.class, EntityDataSerializers.INT);

	private boolean client_hit = false;

	public SlimeTentacle(EntityType<SlimeTentacle> type, Level level) {
		super(type, level);
	}

	public SlimeTentacle(Level level, LivingEntity entity) {
		this(EntityRegistrate.ET_SLIME_TENTACLE.get(), level);
		setOwner(entity);
		setPos(getOwnerPos());
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_OWNER, -1);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		if (accessor == DATA_OWNER && level.isClientSide()) {
			int id = this.entityData.get(DATA_OWNER);
			setOwner(id == -1 ? null : level.getEntity(id));
		}
		super.onSyncedDataUpdated(accessor);
	}

	@Override
	public void setOwner(@Nullable Entity entity) {
		if (!level.isClientSide()) this.entityData.set(DATA_OWNER, entity == null ? -1 : entity.getId());
		super.setOwner(entity);
	}

	@Override
	public void tick() {
		super.tick();

		HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
		if (hitresult.getType() != HitResult.Type.MISS) {
			this.onHit(hitresult);
			if (level.isClientSide())
				client_hit = true;
			else discard();
		}

		this.checkInsideBlocks();
		Vec3 vec3 = this.getDeltaMovement();
		double d2 = this.getX() + vec3.x;
		double d0 = this.getY() + vec3.y;
		double d1 = this.getZ() + vec3.z;
		this.updateRotation();
		this.setPos(d2, d0, d1);

		Entity owner = getOwner();
		if (!level.isClientSide() && (owner == null || position().distanceTo(owner.position()) > 64)) {
			discard();
		}
		if (level.isClientSide() && !client_hit) {
			makeParticle();
		}
	}

	private void makeParticle() {
		Entity owner = getOwner();
		if (owner == null) return;
		Vec3 owner_pos = getOwnerPos();
		double dis = position().distanceTo(owner_pos);
		float freq = 1;
		for (int i = 0; i < Math.ceil(dis * freq); i++) {
			Vec3 vec = position().subtract(owner_pos).normalize().scale((i + random.nextFloat()) / freq).add(owner_pos);
			this.level.addParticle(ParticleTypes.ITEM_SLIME, vec.x, vec.y, vec.z, 0, 0, 0);
		}
	}

	private Vec3 getOwnerPos() {
		return getOwner().getEyePosition().lerp(getOwner().position(), 0.2);
	}

	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level.isClientSide || result.getEntity() instanceof LocalPlayer) {
			Entity owner = getOwner();
			if (result.getEntity() instanceof LivingEntity le && owner instanceof LivingEntity) {
				le.hasImpulse = true;
				Vec3 diff = le.getPosition(1).subtract(getOwnerPos()).normalize();
				le.setDeltaMovement(diff.scale(-3).add(0, 0.4f, 0));
			}
		}
	}

	@Override
	public final Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public float getSize() {
		return 1;
	}

	@Override
	public ItemStack getItem() {
		return Items.SLIME_BALL.getDefaultInstance();
	}
}
