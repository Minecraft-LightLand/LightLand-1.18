package dev.hikarishima.lightland.content.common.entity;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.hikarishima.lightland.content.archery.feature.FeatureList;
import dev.hikarishima.lightland.content.archery.feature.types.FlightControlFeature;
import dev.hikarishima.lightland.content.archery.item.GenericArrowItem;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.EntityRegistrate;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import dev.hikarishima.lightland.util.GenericItemStack;
import dev.hikarishima.lightland.util.annotation.ServerOnly;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public class GenericArrowEntity extends AbstractArrow implements IEntityAdditionalSpawnData {

    public record ArrowEntityData(GenericItemStack<GenericBowItem> bow, GenericItemStack<GenericArrowItem> arrow,
                                  boolean no_consume, float power) {

        public static final Codec<ArrowEntityData> CODEC = RecordCodecBuilder.create(i -> i.group(
                ItemStack.CODEC.fieldOf("bow").forGetter(e -> e.bow().stack()),
                ItemStack.CODEC.fieldOf("arrow").forGetter(e -> e.arrow().stack()),
                Codec.BOOL.fieldOf("no_consume").forGetter(ArrowEntityData::no_consume),
                Codec.FLOAT.fieldOf("power").forGetter(ArrowEntityData::power)
        ).apply(i, (bow, arrow, no_consume, power) -> new ArrowEntityData(
                GenericItemStack.of(bow),
                GenericItemStack.of(arrow),
                no_consume, power
        )));

        public static final ArrowEntityData DEFAULT = new ArrowEntityData(
                GenericItemStack.from(ItemRegistrate.STARTER_BOW.get()),
                GenericItemStack.from(ItemRegistrate.STARTER_ARROW.get()),
                false, 1);

    }

    @ServerOnly
    public ArrowEntityData data = ArrowEntityData.DEFAULT;

    @ServerOnly
    public FeatureList features = new FeatureList();

    public GenericArrowEntity(EntityType<GenericArrowEntity> type, Level level) {
        super(type, level);
    }

    public GenericArrowEntity(Level level, LivingEntity user, ArrowEntityData data, FeatureList features) {
        super(EntityRegistrate.ET_ARROW.get(), user, level);
        this.data = data;
        this.features = features;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        features.hit.forEach(e -> e.onHitEntity(this, target));
    }

    @ServerOnly
    @Override
    protected ItemStack getPickupItem() {
        return data.arrow.stack();
    }

    @Override
    public void tick() {
        Vec3 velocity = getDeltaMovement();
        super.tick();
        FlightControlFeature flight = features.getFlightControl();
        flight.tickMotion(this, velocity);
        if (flight.life > 0 && this.tickCount > flight.life) {
            this.discard();
        }
    }

    protected void tickDespawn() {
        ++this.life;
        if (this.life >= features.getFlightControl().ground_life) {
            this.discard();
        }

    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        features.hit.forEach(e -> e.onHitBlock(this, result));
    }

    @ServerOnly
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        DataResult<Tag> data_tag = ArrowEntityData.CODEC.encodeStart(NbtOps.INSTANCE, data);
        if (data_tag.error().isPresent()) {
            LightLand.LOGGER.error(data_tag.error().get());
        } else if (data_tag.get().left().isPresent()) {
            tag.put("lightland-archery", data_tag.get().left().get());
        }
    }

    @ServerOnly
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("lightland-archery")) {
            CompoundTag data_tag = tag.getCompound("ligtland-archery");
            DataResult<Pair<ArrowEntityData, Tag>> result = ArrowEntityData.CODEC.decode(NbtOps.INSTANCE, data_tag);
            result.get().left().ifPresent(e -> this.data = e.getFirst());
        }
        features = Objects.requireNonNull(FeatureList.merge(data.bow().item().config.feature(), data.arrow().item().config.feature().get()));
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        DataResult<Tag> data_tag = ArrowEntityData.CODEC.encodeStart(NbtOps.INSTANCE, data);
        if (data_tag.error().isPresent()) {
            LightLand.LOGGER.error(data_tag.error().get());
        } else if (data_tag.get().left().isPresent()) {
            buffer.writeNbt((CompoundTag) data_tag.get().left().get());
        }

    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        CompoundTag data_tag = additionalData.readAnySizeNbt();
        DataResult<Pair<ArrowEntityData, Tag>> result = ArrowEntityData.CODEC.decode(NbtOps.INSTANCE, data_tag);
        result.get().left().ifPresent(e -> this.data = e.getFirst());
        features = Objects.requireNonNull(FeatureList.merge(data.bow().item().config.feature(), data.arrow().item().config.feature().get()));
        features.shot.forEach(e -> e.onClientShoot(this));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
