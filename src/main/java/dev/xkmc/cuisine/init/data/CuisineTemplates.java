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
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

public class CuisineTemplates {

	public enum Veges {
		CHILI, CHINESE_CABBAGE, EGGPLANT, GARLIC,
		GINGER, GREEN_PEPPER, LEEK, LETTUCE, ONION, PEANUT, RED_PEPPER,
		SCALLION, SESAME, SICHUAN_PEPPER(5), SOYBEAN, SPINACH, TOMATO, TURNIP,
		//CORN(4, 3),
		//CUCUMBER(4, 4),
		//RICE(4, 4, "root", "stage"),
		;

		public final int stage_lower, stage_upper;
		public final String lower, upper;

		Veges() {
			this(4);
		}

		Veges(int stage) {
			this(stage, 0, "stage", "");
		}

		Veges(int stage, int stage_high) {
			this(stage, stage_high, "lower", "upper");
		}

		Veges(int stage, int stage_high, String lower, String upper) {
			this.stage_lower = stage;
			this.stage_upper = stage_high;
			this.lower = lower;
			this.upper = upper;
		}

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
			return BlockStateProperties.AGE_7;
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
		private static final ResourceLocation MODEL_CORN = new ResourceLocation(Cuisine.MODID, "block/double_cross_crop");
		private static final ResourceLocation MODEL_RICE = new ResourceLocation(Cuisine.MODID, "block/water_crop");

		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}

		public BlockCuisineCrops createBlock(BlockBehaviour.Properties p) {
			//TODO corn
			//TODO double
			return new BlockCuisineCrops(this, PROPERTIES);
		}

		public void generate(DataGenContext<Block, BlockCuisineCrops> ctx, RegistrateBlockstateProvider pvd) {
			//TODO double
			ModelFile[] models = new ModelFile[getStage()];
			for (int i = 0; i < getStage(); i++) {
				String modelname = ctx.getName() + "_stage_" + i;
				models[i] = pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL))
						.texture("crop", new ResourceLocation(Cuisine.MODID, "block/" + modelname));
			}
			pvd.getVariantBuilder(ctx.getEntry()).forAllStates(state -> ConfiguredModel.builder()
					.modelFile(models[getModelStage(state.getValue(getAge()))]).build());
		}

		public void loot(RegistrateBlockLootTables table, BlockCuisineCrops block) {
			//TODO double upper
			table.add(block, LootTable.lootTable()
					.withPool(LootTableTemplate.getPool(1, 0).add(LootTableTemplate.getItem(getSeed(), 1)))
					.withPool(LootTableTemplate.getPool(1, 0).add(LootTableTemplate.cropDrop(getSeed())
							.when(LootTableTemplate.withBlockState(getEntry().get(), getAge(), getMaxAge()))))
					.apply(ApplyExplosionDecay.explosionDecay()));
		}

		private int getModelStage(int stage) {
			return stage == 0 ? 0 : (stage - 1) * (getStage() - 2) / (getMaxAge() - 1) + 1;
		}

	}

}
