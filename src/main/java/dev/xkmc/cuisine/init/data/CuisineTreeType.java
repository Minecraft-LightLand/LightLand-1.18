package dev.xkmc.cuisine.init.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.util.LootTableTemplate;
import dev.xkmc.cuisine.content.fruits.CuisineLeaveBlock;
import dev.xkmc.cuisine.content.fruits.CuisineTreeGrower;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

public enum CuisineTreeType {
	CITRON("sm"), GRAPEFRUIT("lg"), LEMON("md"), LIME("md"),
	MANDARIN("sm"), ORANGE("md"), POMELO("lg");

	private final CuisineTreeGrower grower = new CuisineTreeGrower(this);
	private final ResourceLocation fruit_type;

	CuisineTreeType(String fruit_type) {
		this.fruit_type = new ResourceLocation(Cuisine.MODID, "block/fruit_" + fruit_type);
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public AbstractTreeGrower getGrower() {
		return grower;
	}

	public BlockEntry<?> getLeave() {
		return CuisineBlocks.LEAVE[ordinal()];
	}

	public BlockEntry<?> getSapling() {
		return CuisineBlocks.SAPLING[ordinal()];
	}

	public ItemEntry<?> getFruit() {
		return CuisineItems.FRUITS[ordinal()];
	}

	private static final ResourceLocation FLOWER = new ResourceLocation(Cuisine.MODID, "block/flowers");

	public void generate(DataGenContext<Block, CuisineLeaveBlock> ctx, RegistrateBlockstateProvider pvd) {
		ModelFile leave = pvd.models().withExistingParent(ctx.getName(), "block/leaves")
				.texture("all", new ResourceLocation(Cuisine.MODID, "block/leaves_" + getName()));
		ModelFile flower = pvd.models().getBuilder("flowers_" + ctx.getName()).parent(new ModelFile.UncheckedModelFile(FLOWER))
				.texture("0", new ResourceLocation(Cuisine.MODID, "block/flowers_" + getName()));
		ModelFile fruit = pvd.models().getBuilder("fruit_" + ctx.getName()).parent(new ModelFile.UncheckedModelFile(fruit_type))
				.texture("0", new ResourceLocation(Cuisine.MODID, "item/" + getName()));
		pvd.getVariantBuilder(ctx.getEntry())
				.partialState().with(CuisineLeaveBlock.AGE, 0).addModels(ConfiguredModel.builder().modelFile(leave).build())
				.partialState().with(CuisineLeaveBlock.AGE, 1).addModels(ConfiguredModel.builder().modelFile(leave).build())
				.partialState().with(CuisineLeaveBlock.AGE, 2).addModels(ConfiguredModel.builder().modelFile(leave).modelFile(flower).build())
				.partialState().with(CuisineLeaveBlock.AGE, 3).addModels(ConfiguredModel.builder().modelFile(leave).modelFile(fruit).build());
	}

	public void loot(RegistrateBlockLootTables table, CuisineLeaveBlock block) {
		table.add(block, LootTable.lootTable().withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(getFruit().get(), 1))
						.when(LootTableTemplate.withBlockState(block, CuisineLeaveBlock.AGE, 3)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(getLeave().get().asItem(), 1))
						.when(LootTableTemplate.shearOrSilk(false)))
				.withPool(LootTableTemplate.getPool(1, 0)
						.add(LootTableTemplate.getItem(getSapling().get().asItem(), 1)
								.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
										1 / 20f, 1 / 16f, 1 / 12f, 1 / 10f))
								.when(ExplosionCondition.survivesExplosion()))
						.add(LootTableTemplate.getItem(Items.STICK, 1, 2)
								.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
										1 / 50f, 1 / 45f, 1 / 40f, 1 / 30f, 1 / 10f)))
						.when(LootTableTemplate.shearOrSilk(true))));
	}

}
