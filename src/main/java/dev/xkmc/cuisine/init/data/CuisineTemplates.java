package dev.xkmc.cuisine.init.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.hikarishima.lightland.util.LootTableTemplate;
import dev.xkmc.cuisine.content.veges.BlockCuisineCrops;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

public class CuisineTemplates {

	enum ModelType {
		CROP(Veges::crop), CROSS(Veges::cross), DOUBLE(Veges::cropHigh), CORN(Veges::crossHigh), RICE(Veges::water);

		public final ModelSupplier sup;

		ModelType(ModelSupplier sup) {
			this.sup = sup;
		}
	}

	public enum Veges {
		CHILI, CHINESE_CABBAGE, EGGPLANT,
		GINGER, GREEN_PEPPER, LEEK, LETTUCE, ONION, PEANUT, RED_PEPPER,
		SCALLION, SESAME, SICHUAN_PEPPER(5), SOYBEAN, SPINACH, TOMATO, TURNIP,
		GARLIC(4, ModelType.CROP),
		CUCUMBER(4, 4, ModelType.DOUBLE),
		CORN(4, 3, ModelType.CORN),
		RICE(4, 4, ModelType.RICE),
		;

		public final int stage_lower, stage_upper;
		public final ModelType type;

		Veges() {
			this(4);
		}

		Veges(int stage) {
			this(stage, ModelType.CROSS);
		}

		Veges(int stage, ModelType type) {
			this(stage, 0, type);
		}

		Veges(int stage, int stage_high, ModelType type) {
			this.stage_lower = stage;
			this.stage_upper = stage_high;
			this.type = type;
		}

		/* --- Behavior --- */

		private static final IntegerProperty AGE_7 = BlockStateProperties.AGE_7;
		private static final IntegerProperty AGE_8 = IntegerProperty.create("age", 0, 8);

		public int getStage() {
			return stage_lower;
		}

		public boolean isDouble() {
			return stage_upper > 0;
		}

		public BlockEntry<?> getEntry() {
			return CuisineBlocks.VEGES[ordinal()];
		}

		public IntegerProperty getAge() {
			return isDouble() ? AGE_8 : AGE_7;
		}

		public int getMaxAge() {
			return 7;
		}

		public Item getSeed() {
			return getEntry().get().asItem();
		}

		/* --- Data Gen --- */

		private static final BlockBehaviour.Properties PROPERTIES = BlockBehaviour.Properties.of(Material.PLANT)
				.noCollission().randomTicks().instabreak().sound(SoundType.CROP);

		private static final ResourceLocation MODEL = new ResourceLocation(Cuisine.MODID, "block/cross_crop");
		private static final ResourceLocation MODEL_DOUBLE = new ResourceLocation(Cuisine.MODID, "block/double_crop");
		private static final ResourceLocation MODEL_DOUBLE_CROSS = new ResourceLocation(Cuisine.MODID, "block/double_cross_crop");
		private static final ResourceLocation MODEL_WATER = new ResourceLocation(Cuisine.MODID, "block/water_crop");
		private static final ResourceLocation VOID = new ResourceLocation(Cuisine.MODID, "block/void");

		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}

		public BlockCuisineCrops createBlock(BlockBehaviour.Properties p) {
			//TODO corn
			//TODO double
			return new BlockCuisineCrops(this, PROPERTIES);
		}

		public void generate(DataGenContext<Block, BlockCuisineCrops> ctx, RegistrateBlockstateProvider pvd) {
			ModelFile[] models = new ModelFile[getStage()];
			for (int i = 0; i < getStage(); i++) {
				models[i] = type.sup.get(this, ctx, pvd, i);
			}
			ModelFile air = new ModelFile.UncheckedModelFile(VOID);
			pvd.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
				int age = state.getValue(getAge());
				if (age > getMaxAge()) {
					return ConfiguredModel.builder().modelFile(air).build();
				} else {
					return ConfiguredModel.builder().modelFile(models[getModelStage(age)]).build();
				}
			});
		}

		public void loot(RegistrateBlockLootTables table, BlockCuisineCrops block) {
			LootPoolEntryContainer.Builder<?> base = LootTableTemplate.getItem(getSeed(), 1);
			if (isDouble()) {
				base.when(LootTableTemplate.withBlockState(getEntry().get(), getAge(), 0, getMaxAge()));
			}
			table.add(block, LootTable.lootTable()
					.withPool(LootTableTemplate.getPool(1, 0).add(base))
					.withPool(LootTableTemplate.getPool(1, 0).add(LootTableTemplate.cropDrop(getSeed())
							.when(LootTableTemplate.withBlockState(getEntry().get(), getAge(), getMaxAge()))))
					.apply(ApplyExplosionDecay.explosionDecay()));
		}

		private BlockModelBuilder crop(DataGenContext<Block, BlockCuisineCrops> ctx, RegistrateBlockstateProvider pvd, int i) {
			String modelname = ctx.getName() + "_stage_" + i;
			ResourceLocation texture = new ResourceLocation(Cuisine.MODID, "block/" + modelname);
			return pvd.models().crop(modelname, texture);
		}

		private BlockModelBuilder cross(DataGenContext<Block, BlockCuisineCrops> ctx, RegistrateBlockstateProvider pvd, int i) {
			String modelname = ctx.getName() + "_stage_" + i;
			ResourceLocation texture = new ResourceLocation(Cuisine.MODID, "block/" + modelname);
			return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL))
					.texture("crop", texture);
		}

		private BlockModelBuilder cropHigh(DataGenContext<Block, BlockCuisineCrops> ctx, RegistrateBlockstateProvider pvd, int i) {
			String modelname = ctx.getName() + "_stage_" + i;
			ResourceLocation lower = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_lower_" + i);
			ResourceLocation upper = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_upper_" + i);
			if (i < stage_lower - stage_upper)
				return pvd.models().crop(modelname, lower);
			return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL_DOUBLE))
					.texture("lower", lower).texture("upper", upper);
		}

		private BlockModelBuilder crossHigh(DataGenContext<Block, BlockCuisineCrops> ctx, RegistrateBlockstateProvider pvd, int i) {
			String modelname = ctx.getName() + "_stage_" + i;
			ResourceLocation lower = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_lower_" + i);
			ResourceLocation upper = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_upper_" + i);
			if (i < stage_lower - stage_upper)
				return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL))
						.texture("crop", lower);
			return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL_DOUBLE_CROSS))
					.texture("lower", lower).texture("upper", upper);
		}

		private BlockModelBuilder water(DataGenContext<Block, BlockCuisineCrops> ctx, RegistrateBlockstateProvider pvd, int i) {
			String modelname = ctx.getName() + "_stage_" + i;
			ResourceLocation crop = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_stage_" + i);
			ResourceLocation root = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_root_" + i);
			return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL_WATER))
					.texture("crop", crop).texture("root", root);
		}

		private int getModelStage(int stage) {
			return stage == 0 ? 0 : (stage - 1) * (getStage() - 2) / (getMaxAge() - 1) + 1;
		}

	}

	private interface ModelSupplier {

		BlockModelBuilder get(Veges veges, DataGenContext<Block, BlockCuisineCrops> ctx, RegistrateBlockstateProvider pvd, int i);

	}

}
