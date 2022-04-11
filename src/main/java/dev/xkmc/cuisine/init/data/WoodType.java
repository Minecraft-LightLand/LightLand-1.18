package dev.xkmc.cuisine.init.data;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum WoodType {
	FRUITTREE;
	public final BlockEntry<RotatedPillarBlock> LOG;
	public final BlockEntry<Block> PLANK;
	public final BlockEntry<TrapDoorBlock> TRAPDOOR;
	public final BlockEntry<DoorBlock> DOOR;
	public final BlockEntry<WoodButtonBlock> BUTTON;
	public final BlockEntry<FenceBlock> FENCE;
	public final BlockEntry<FenceGateBlock> FENCE_GATE;

	WoodType() {
		LOG = REGISTRATE.block(getName() + "_log", p -> Blocks.log(MaterialColor.WOOD, MaterialColor.PODZOL))
				.blockstate((ctx, pvd) -> pvd.logBlock(ctx.getEntry())).defaultLoot()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.LOGS, BlockTags.LOGS_THAT_BURN).simpleItem().register();
		PLANK = REGISTRATE.block(getName() + "_planks", p -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)))
				.defaultBlockstate().defaultLoot()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.PLANKS).simpleItem().register();
		TRAPDOOR = REGISTRATE.block(getName() + "_trapdoor", p -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)))
				.blockstate((ctx, pvd) -> pvd.trapdoorBlock(ctx.getEntry(), pvd.blockTexture(ctx.getEntry()), true))
				.defaultLoot().item().model((ctx, pvd) -> pvd.getBuilder(ctx.getName())
						.parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID,
								"block/" + getName() + "_trapdoor_bottom")))).build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.TRAPDOORS, BlockTags.WOODEN_TRAPDOORS).register();
		DOOR = REGISTRATE.block(getName() + "_door", p -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)))
				.blockstate((ctx, pvd) -> pvd.doorBlock(ctx.getEntry(),
						new ResourceLocation(Cuisine.MODID, "block/" + getName() + "_door_lower"),
						new ResourceLocation(Cuisine.MODID, "block/" + getName() + "_door_upper")))
				.item().defaultModel().build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.DOORS, BlockTags.WOODEN_DOORS).register();
		BUTTON = REGISTRATE.block(getName() + "_button", p -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)))
				.blockstate((ctx, pvd) -> pvd.buttonBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get())))
				.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.BUTTONS, BlockTags.WOODEN_BUTTONS).register();
		FENCE = REGISTRATE.block(getName() + "_fence", p -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)))
				.blockstate((ctx, pvd) -> pvd.fenceBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get())))
				.item().model((ctx, pvd) -> pvd.getBuilder(ctx.getName())
						.parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID,
								"block/" + getName() + "_fence_post")))).build()
				.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.FENCES, BlockTags.WOODEN_FENCES).register();
		FENCE_GATE = REGISTRATE.block(getName() + "_fence_gate", p -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)))
				.blockstate((ctx, pvd) -> pvd.fenceGateBlock(ctx.getEntry(), pvd.blockTexture(PLANK.get())))
				.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.FENCE_GATES).register();
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {
	}

}
