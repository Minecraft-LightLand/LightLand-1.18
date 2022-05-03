package dev.xkmc.cuisine.init.data;

import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.repack.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.util.LootTableTemplate;
import dev.xkmc.cuisine.content.veges.CornBlock;
import dev.xkmc.cuisine.content.veges.CuisineCropBlock;
import dev.xkmc.cuisine.content.veges.DoubleCropBlock;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.client.renderer.RenderType;
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

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;
import static dev.xkmc.cuisine.init.data.CuisineTags.AllItemTags.*;

public enum CuisineCropType {
	CHILI(SIDE, SPICY),
	CHINESE_CABBAGE(VEGES),
	CORN(4, 3, ModelType.CORN, STAPLE),
	CUCUMBER(4, 4, ModelType.DOUBLE, VEGES),
	EGGPLANT(VEGES),
	GARLIC(4, ModelType.CROP, SIDE),
	GINGER(SIDE),
	GREEN_PEPPER(VEGES, SPICY),
	LEEK(VEGES),
	LETTUCE(VEGES),
	ONION(SIDE),
	PEANUT(STAPLE),
	RED_PEPPER(VEGES, SPICY),
	RICE(4, 4, ModelType.WATER, STAPLE),
	SCALLION(SIDE),
	SESAME(SIDE),
	SICHUAN_PEPPER(5, CONDIMENT, NUMB),
	SOYBEAN(STAPLE),
	SPINACH(VEGES),
	TOMATO(VEGES),
	TURNIP(VEGES),
	;

	public final int stage_lower, stage_upper;
	public final ModelType type;
	public final CuisineTags.AllItemTags[] tags;
	public final BlockEntry<CuisineCropBlock> entry;

	CuisineCropType(CuisineTags.AllItemTags... tags) {
		this(4, tags);
	}

	CuisineCropType(int stage, CuisineTags.AllItemTags... tags) {
		this(stage, ModelType.CROSS, tags);
	}

	CuisineCropType(int stage, ModelType type, CuisineTags.AllItemTags... tags) {
		this(stage, 0, type, tags);
	}

	CuisineCropType(int stage, int stage_high, ModelType type, CuisineTags.AllItemTags... tags) {
		this.stage_lower = stage;
		this.stage_upper = stage_high;
		this.type = type;
		this.tags = tags;

		entry = REGISTRATE.block(getName(), this::createBlock)
				.addLayer(() -> RenderType::cutout).blockstate(this::generate).loot(this::loot)
				.item().tag(CuisineTags.map(tags))
				.defaultModel().build().defaultLang().register();
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
		return entry;
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
	private static final ResourceLocation UPPER = new ResourceLocation(Cuisine.MODID, "block/upper_crop");

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public CuisineCropBlock createBlock(BlockBehaviour.Properties p) {
		if (type == ModelType.CORN) return new CornBlock(this, PROPERTIES);
		if (isDouble()) return new DoubleCropBlock(this, PROPERTIES);
		return new CuisineCropBlock(this, PROPERTIES);
	}

	public void generate(DataGenContext<Block, CuisineCropBlock> ctx, RegistrateBlockstateProvider pvd) {
		ModelFile[] models = new ModelFile[getStage()];
		for (int i = 0; i < getStage(); i++) {
			models[i] = type.sup.get(this, ctx, pvd, i);
		}
		ModelFile air = isDouble() ? upper(ctx, pvd, type == ModelType.WATER ? "_stage_1" : "_upper_1") : null;
		pvd.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
			int age = state.getValue(getAge());
			if (age > getMaxAge()) {
				return ConfiguredModel.builder().modelFile(air).build();
			} else {
				return ConfiguredModel.builder().modelFile(models[getModelStage(age)]).build();
			}
		});
	}

	public void loot(RegistrateBlockLootTables table, CuisineCropBlock block) {
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

	private BlockModelBuilder crop(DataGenContext<Block, CuisineCropBlock> ctx, RegistrateBlockstateProvider pvd, int i) {
		String modelname = ctx.getName() + "_stage_" + i;
		ResourceLocation texture = new ResourceLocation(Cuisine.MODID, "block/" + modelname);
		return pvd.models().crop(modelname, texture);
	}

	private BlockModelBuilder cross(DataGenContext<Block, CuisineCropBlock> ctx, RegistrateBlockstateProvider pvd, int i) {
		String modelname = ctx.getName() + "_stage_" + i;
		ResourceLocation texture = new ResourceLocation(Cuisine.MODID, "block/" + modelname);
		return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL))
				.texture("crop", texture);
	}

	private BlockModelBuilder upper(DataGenContext<Block, CuisineCropBlock> ctx, RegistrateBlockstateProvider pvd, String mid) {
		String modelname = ctx.getName() + "_upper";
		ResourceLocation texture = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + mid);
		return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(UPPER))
				.texture("crop", texture);
	}

	private BlockModelBuilder cropHigh(DataGenContext<Block, CuisineCropBlock> ctx, RegistrateBlockstateProvider pvd, int i) {
		String modelname = ctx.getName() + "_stage_" + i;
		ResourceLocation lower = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_lower_" + i);
		ResourceLocation upper = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_upper_" + i);
		if (i < stage_lower - stage_upper)
			return pvd.models().crop(modelname, lower);
		return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL_DOUBLE))
				.texture("lower", lower).texture("upper", upper);
	}

	private BlockModelBuilder crossHigh(DataGenContext<Block, CuisineCropBlock> ctx, RegistrateBlockstateProvider pvd, int i) {
		String modelname = ctx.getName() + "_stage_" + i;
		ResourceLocation lower = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_lower_" + i);
		ResourceLocation upper = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_upper_" + i);
		if (i < stage_lower - stage_upper)
			return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL))
					.texture("crop", lower);
		return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL_DOUBLE_CROSS))
				.texture("lower", lower).texture("upper", upper);
	}

	private BlockModelBuilder water(DataGenContext<Block, CuisineCropBlock> ctx, RegistrateBlockstateProvider pvd, int i) {
		String modelname = ctx.getName() + "_stage_" + i;
		ResourceLocation crop = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_stage_" + i);
		ResourceLocation root = new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName() + "_root_" + i);
		return pvd.models().getBuilder(modelname).parent(new ModelFile.UncheckedModelFile(MODEL_WATER))
				.texture("crop", crop).texture("root", root);
	}

	private int getModelStage(int stage) {
		return stage == 0 ? 0 : (stage - 1) * (getStage() - 2) / (getMaxAge() - 1) + 1;
	}

	public static void register() {
	}

	public enum ModelType {
		CROP(CuisineCropType::crop), CROSS(CuisineCropType::cross), DOUBLE(CuisineCropType::cropHigh), CORN(CuisineCropType::crossHigh), WATER(CuisineCropType::water);

		public final ModelSupplier sup;

		ModelType(ModelSupplier sup) {
			this.sup = sup;
		}
	}

	public interface ModelSupplier {

		BlockModelBuilder get(CuisineCropType veges, DataGenContext<Block, CuisineCropBlock> ctx, RegistrateBlockstateProvider pvd, int i);

	}
}
