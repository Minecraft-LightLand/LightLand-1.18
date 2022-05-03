package dev.xkmc.cuisine.init.data;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.EntityEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum WoodType {
	FRUITTREE, EMPOWERED, MYSTIC;
	public final BlockEntry<RotatedPillarBlock> LOG;
	public final BlockEntry<Block> PLANK;
	public final BlockEntry<TrapDoorBlock> TRAPDOOR;
	public final BlockEntry<DoorBlock> DOOR;
	public final BlockEntry<WoodButtonBlock> BUTTON;
	public final BlockEntry<PressurePlateBlock> PRESSURE_PLATE;
	public final BlockEntry<FenceBlock> FENCE;
	public final BlockEntry<FenceGateBlock> FENCE_GATE;
	public final BlockEntry<SlabBlock> SLAB;
	public final BlockEntry<StairBlock> STAIR;

	public final EntityEntry<BoatEntity> BOAT;
	public final ItemEntry<CustomBoatItem> BOAT_ITEM;

	WoodType() {
		LOG = REGISTRATE.block(getName() + "_log", p -> Blocks.log(MaterialColor.WOOD, MaterialColor.PODZOL))
				.blockstate((ctx, pvd) -> pvd.logBlock(ctx.getEntry())).defaultLoot()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.LOGS, BlockTags.LOGS_THAT_BURN)
				.item().tag(ItemTags.LOGS, ItemTags.LOGS_THAT_BURN).build().register();
		PLANK = REGISTRATE.block(getName() + "_planks", p -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)))
				.defaultBlockstate().defaultLoot()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.PLANKS)
				.item().tag(ItemTags.PLANKS).build().register();
		SLAB = REGISTRATE.block(getName() + "_slab", p -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)))
				.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get()), pvd.blockTexture(PLANK.get())))
				.item().tag(ItemTags.SLABS, ItemTags.WOODEN_SLABS).build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.SLABS, BlockTags.WOODEN_SLABS).register();

		STAIR = REGISTRATE.block(getName() + "_stairs", p -> new StairBlock(() -> PLANK.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)))
				.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get())))
				.item().tag(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS).build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.STAIRS, BlockTags.WOODEN_STAIRS).register();
		TRAPDOOR = REGISTRATE.block(getName() + "_trapdoor", p -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)))
				.blockstate((ctx, pvd) -> pvd.trapdoorBlock(ctx.getEntry(), pvd.blockTexture(ctx.getEntry()), true))
				.defaultLoot().item().model((ctx, pvd) -> pvd.getBuilder(ctx.getName())
						.parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID,
								"block/" + getName() + "_trapdoor_bottom"))))
				.tag(ItemTags.TRAPDOORS, ItemTags.WOODEN_TRAPDOORS).build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.TRAPDOORS, BlockTags.WOODEN_TRAPDOORS).register();
		DOOR = REGISTRATE.block(getName() + "_door", p -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)))
				.blockstate((ctx, pvd) -> pvd.doorBlock(ctx.getEntry(),
						new ResourceLocation(Cuisine.MODID, "block/" + getName() + "_door_lower"),
						new ResourceLocation(Cuisine.MODID, "block/" + getName() + "_door_upper")))
				.item().defaultModel().tag(ItemTags.DOORS, ItemTags.WOODEN_DOORS).build().addLayer(() -> RenderType::cutout)
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.DOORS, BlockTags.WOODEN_DOORS).register();
		BUTTON = REGISTRATE.block(getName() + "_button", p -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)))
				.blockstate((ctx, pvd) -> pvd.buttonBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get())))
				.item().model((ctx, pvd) -> pvd.buttonInventory(ctx.getName(), pvd.modLoc("block/" + getName() + "_planks")))
				.tag(ItemTags.BUTTONS, ItemTags.WOODEN_BUTTONS).build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.BUTTONS, BlockTags.WOODEN_BUTTONS).register();
		PRESSURE_PLATE = REGISTRATE.block(getName() + "_pressure_plate", p -> new PressurePlateBlock(
						PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)))
				.blockstate((ctx, pvd) -> pvd.pressurePlateBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get())))
				.item().tag(ItemTags.WOODEN_PRESSURE_PLATES).build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES)
				.register();
		FENCE = REGISTRATE.block(getName() + "_fence", p -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)))
				.blockstate((ctx, pvd) -> pvd.fenceBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get())))
				.item().model((ctx, pvd) -> pvd.fenceInventory(ctx.getName(), pvd.modLoc("block/" + getName() + "_planks")))
				.tag(ItemTags.FENCES, ItemTags.WOODEN_FENCES).build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.FENCES, BlockTags.WOODEN_FENCES).register();
		FENCE_GATE = REGISTRATE.block(getName() + "_fence_gate", p -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)))
				.blockstate((ctx, pvd) -> pvd.fenceGateBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get())))
				.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.FENCE_GATES).register();
		BOAT = REGISTRATE.<BoatEntity>entity(getName() + "_boat", (type, level) -> new BoatEntity(type, level, this), MobCategory.MISC)
				.properties(e -> e.sized(1.375F, 0.5625F).clientTrackingRange(10))
				.renderer(() -> ctx -> new BoatEntityRenderer(ctx, this)).register();
		BOAT_ITEM = REGISTRATE.item(getName() + "_boat", p -> new CustomBoatItem(this, p)).tag(ItemTags.BOATS).register();

	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {
	}

	private static boolean common_init = false, client_init = false;

	public static void onCommonInit() {
		if (common_init) return;
		common_init = true;
		for (WoodType type : WoodType.values()) {
			DispenserBlock.registerBehavior(type.BOAT_ITEM.get(), new BoatDispenseItemBehavior(type));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void onClientInit() {
		if (client_init) return;
		client_init = true;
		LayerDefinition layerDef = BoatModel.createBodyModel();
		for (WoodType type : WoodType.values()) {
			type.registerLayer(layerDef);
		}
	}

	@OnlyIn(Dist.CLIENT)
	ModelLayerLocation layer;

	@OnlyIn(Dist.CLIENT)
	private void registerLayer(LayerDefinition layerDef) {
		layer = new ModelLayerLocation(new ResourceLocation(Cuisine.MODID, "boat/" + getName() + "_boat"), "main");
		ForgeHooksClient.registerLayerDefinition(layer, () -> layerDef);
	}

}

class BoatEntity extends Boat {

	private final WoodType wood;

	public BoatEntity(EntityType<BoatEntity> type, Level level, WoodType wood) {
		super(type, level);
		this.wood = wood;
	}

	public BoatEntity(Level level, WoodType wood, double x, double y, double z) {
		this(wood.BOAT.get(), level, wood);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}


	@Override
	public Item getDropItem() {
		return wood.BOAT_ITEM.get();
	}

	protected void checkFallDamage(double p_38307_, boolean p_38308_, BlockState p_38309_, BlockPos p_38310_) {
		this.lastYd = this.getDeltaMovement().y;
		if (!this.isPassenger()) {
			if (p_38308_) {
				if (this.fallDistance > 3.0F) {
					if (this.status != Boat.Status.ON_LAND) {
						this.resetFallDistance();
						return;
					}

					this.causeFallDamage(this.fallDistance, 1.0F, DamageSource.FALL);
					if (!this.level.isClientSide && !this.isRemoved()) {
						this.kill();
						if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
							for (int i = 0; i < 3; ++i) {
								this.spawnAtLocation(wood.PLANK.get().asItem());
							}

							for (int j = 0; j < 2; ++j) {
								this.spawnAtLocation(Items.STICK);
							}
						}
					}
				}

				this.resetFallDistance();
			} else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && p_38307_ < 0.0D) {
				this.fallDistance -= (float) p_38307_;
			}

		}
	}

}

class BoatEntityRenderer extends BoatRenderer {

	private final ResourceLocation id;
	private final BoatModel model;

	public BoatEntityRenderer(EntityRendererProvider.Context context, WoodType type) {
		super(context);
		id = new ResourceLocation(Cuisine.MODID, "textures/entity/" + type.getName() + "_boat.png");
		model = new BoatModel(context.bakeLayer(type.layer));
	}

	public Pair<ResourceLocation, BoatModel> getModelWithLocation(Boat boat) {
		return Pair.of(id, model);
	}

}

class CustomBoatItem extends Item {

	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

	private final WoodType type;

	public CustomBoatItem(WoodType type, Properties properties) {
		super(properties);
		this.type = type;
	}

	public InteractionResultHolder<ItemStack> use(Level p_40622_, Player p_40623_, InteractionHand p_40624_) {
		ItemStack itemstack = p_40623_.getItemInHand(p_40624_);
		HitResult hitresult = getPlayerPOVHitResult(p_40622_, p_40623_, ClipContext.Fluid.ANY);
		if (hitresult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemstack);
		} else {
			Vec3 vec3 = p_40623_.getViewVector(1.0F);
			double d0 = 5.0D;
			List<Entity> list = p_40622_.getEntities(p_40623_, p_40623_.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
			if (!list.isEmpty()) {
				Vec3 vec31 = p_40623_.getEyePosition();

				for (Entity entity : list) {
					AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (aabb.contains(vec31)) {
						return InteractionResultHolder.pass(itemstack);
					}
				}
			}

			if (hitresult.getType() == HitResult.Type.BLOCK) {
				BoatEntity boat = new BoatEntity(p_40622_, type, hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z);
				boat.setYRot(p_40623_.getYRot());
				if (!p_40622_.noCollision(boat, boat.getBoundingBox())) {
					return InteractionResultHolder.fail(itemstack);
				} else {
					if (!p_40622_.isClientSide) {
						p_40622_.addFreshEntity(boat);
						p_40622_.gameEvent(p_40623_, GameEvent.ENTITY_PLACE, new BlockPos(hitresult.getLocation()));
						if (!p_40623_.getAbilities().instabuild) {
							itemstack.shrink(1);
						}
					}

					p_40623_.awardStat(Stats.ITEM_USED.get(this));
					return InteractionResultHolder.sidedSuccess(itemstack, p_40622_.isClientSide());
				}
			} else {
				return InteractionResultHolder.pass(itemstack);
			}
		}
	}


}

class BoatDispenseItemBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
	private final WoodType type;

	public BoatDispenseItemBehavior(WoodType p_123371_) {
		this.type = p_123371_;
	}

	public ItemStack execute(BlockSource p_123375_, ItemStack p_123376_) {
		Direction direction = p_123375_.getBlockState().getValue(DispenserBlock.FACING);
		Level level = p_123375_.getLevel();
		double d0 = p_123375_.x() + (double) ((float) direction.getStepX() * 1.125F);
		double d1 = p_123375_.y() + (double) ((float) direction.getStepY() * 1.125F);
		double d2 = p_123375_.z() + (double) ((float) direction.getStepZ() * 1.125F);
		BlockPos blockpos = p_123375_.getPos().relative(direction);
		double d3;
		if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
			d3 = 1.0D;
		} else {
			if (!level.getBlockState(blockpos).isAir() || !level.getFluidState(blockpos.below()).is(FluidTags.WATER)) {
				return this.defaultDispenseItemBehavior.dispense(p_123375_, p_123376_);
			}

			d3 = 0.0D;
		}

		BoatEntity boat = new BoatEntity(level, type, d0, d1 + d3, d2);
		boat.setYRot(direction.toYRot());
		level.addFreshEntity(boat);
		p_123376_.shrink(1);
		return p_123376_;
	}

	protected void playSound(BlockSource p_123373_) {
		p_123373_.getLevel().levelEvent(1000, p_123373_.getPos(), 0);
	}
}