package dev.hikarishima.lightland.init.data;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.content.common.item.api.Mat;
import dev.hikarishima.lightland.content.common.item.generic.*;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import dev.hikarishima.lightland.init.special.LLRegistrate;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

@SuppressWarnings({"unchecked", "rawtypes", "unsafe"})
public class GenItem {

	private static final Function<Tools, RawToolFactory> TOOL_GEN_FUNC = tool -> switch (tool) {
		case SWORD -> SwordItem::new;
		case AXE -> AxeItem::new;
		case SHOVEL -> ShovelItem::new;
		case PICKAXE -> PickaxeItem::new;
		case HOE -> HoeItem::new;
	};

	private static TieredItem genGenericTool(Mats mat, Tools tool, Item.Properties prop) {
		int dmg = mat.tool_stats.add_dmg[tool.ordinal()] - 1;
		float speed = mat.tool_stats.add_speed[tool.ordinal()] - 4;
		return switch (tool) {
			case SWORD -> new GenericSwordItem(mat.tier, dmg, speed, prop, mat.tool_extra);
			case AXE -> new GenericAxeItem(mat.tier, dmg, speed, prop, mat.tool_extra);
			case SHOVEL -> new GenericShovelItem(mat.tier, dmg, speed, prop, mat.tool_extra);
			case PICKAXE -> new GenericPickaxeItem(mat.tier, dmg, speed, prop, mat.tool_extra);
			case HOE -> new GenericHoeItem(mat.tier, dmg, speed, prop, mat.tool_extra);
		};
	}

	public static final ToolConfig TOOL_DEF = new ToolConfig(a -> a.defaultLang()
			.model((ctx, pvd) -> pvd.handheld(ctx::getEntry)), fromToolGen(TOOL_GEN_FUNC));
	public static final ToolConfig TOOL_GEN = new ToolConfig(a -> a.defaultLang()
			.model((ctx, pvd) -> pvd.handheld(ctx::getEntry)), GenItem::genGenericTool);
	public static final ArmorConfig ARMOR_DEF = new ArmorConfig(a -> a.defaultLang().defaultModel(),
			(mat, slot, prop) -> new ArmorItem(mat.mat, slot, prop));
	public static final ArmorConfig ARMOR_GEN = new ArmorConfig(a -> a.defaultLang().defaultModel(),
			(mat, slot, prop) -> new GenericArmorItem(mat.mat, slot, prop, mat.armor_extra));

	public enum Mats {
		STEEL("steel", 2, SoundEvents.ARMOR_EQUIP_IRON,
				new ToolStats(500, 6, new int[]{6, 9, 4, 4, 1},
						new float[]{1.6f, 0.9f, 1f, 1.2f, 3f}, 14),
				new ArmorStats(20, new int[]{2, 5, 6, 2}, 1, 0, 9),
				TOOL_DEF, ARMOR_DEF, new ExtraToolConfig(), new ExtraArmorConfig()),
		LAYROOT("layroot", 2, SoundEvents.ARMOR_EQUIP_IRON,
				new ToolStats(300, 6, new int[]{6, 9, 4, 4, 1},
						new float[]{1.8f, 1.0f, 1.1f, 1.3f, 3.3f}, 18),
				new ArmorStats(20, new int[]{2, 5, 6, 2}, 0, 0, 19),
				TOOL_GEN, ARMOR_GEN, new ExtraToolConfig().repairChance(1e-2),
				new ExtraArmorConfig().repairChance(1e-2)),
		LAYLINE("layline", 2, SoundEvents.ARMOR_EQUIP_IRON,
				new ToolStats(500, 8, new int[]{6, 9, 4, 4, 2},
						new float[]{2.0f, 1.1f, 1.3f, 1.5f, 3.6f}, 20),
				new ArmorStats(25, new int[]{2, 5, 6, 2}, 1, 0, 22),
				TOOL_GEN, ARMOR_GEN, new ExtraToolConfig().repairChance(2e-2),
				new ExtraArmorConfig().repairChance(2e-2)),
		OLDROOT("oldroot", 3, SoundEvents.ARMOR_EQUIP_IRON,
				new ToolStats(700, 10, new int[]{7, 10, 4, 4, 3},
						new float[]{2.0f, 1.1f, 1.5f, 1.7f, 4f}, 22),
				new ArmorStats(30, new int[]{2, 5, 6, 2}, 2, 0, 25),
				TOOL_GEN, ARMOR_GEN, new ExtraToolConfig().repairChance(4e-2),
				new ExtraArmorConfig().repairChance(4e-2)),
		KNIGHTSTEEL("knightsteel", 3, SoundEvents.ARMOR_EQUIP_IRON,
				new ToolStats(500, 6, new int[]{6, 9, 4, 4, 1},
						new float[]{1.6f, 0.9f, 1f, 1.2f, 3f}, 18),
				new ArmorStats(25, new int[]{2, 5, 6, 2}, 2, 0.1f, 12),
				TOOL_DEF, ARMOR_DEF, new ExtraToolConfig(), new ExtraArmorConfig()),
		DISPELLIUM("dispellium", 2, SoundEvents.ARMOR_EQUIP_IRON,
				new ToolStats(250, 6, new int[]{6, 9, 4, 4, 1},
						new float[]{1.6f, 0.9f, 1f, 1.2f, 3f}, 0),
				new ArmorStats(15, new int[]{2, 5, 6, 2}, 0, 0, 0),
				TOOL_GEN, ARMOR_GEN, new ExtraToolConfig().setBypassMagic(),
				new ExtraArmorConfig().setMagicImmune(40)),
		HEAVYSTEEL("heavysteel", 3, SoundEvents.ARMOR_EQUIP_IRON,
				new ToolStats(700, 5, new int[]{10, 14, 4, 4, 1},
						new float[]{1f, 0.7f, 0.8f, 1f, 2f}, 14),
				new ArmorStats(30, new int[]{3, 5, 6, 3}, 3, 0.2f, 9),
				TOOL_DEF, ARMOR_DEF, new ExtraToolConfig(), new ExtraArmorConfig());

		final String id;
		final Tier tier;
		final ArmorMaterial mat;
		final ToolConfig tool_config;
		final ArmorConfig armor_config;
		final ToolStats tool_stats;
		final ExtraToolConfig tool_extra;
		final ExtraArmorConfig armor_extra;

		Mats(String name, int level,
			 SoundEvent equip_sound, ToolStats tool, ArmorStats armor,
			 ToolConfig tool_config, ArmorConfig armor_config,
			 ExtraToolConfig tool_extra, ExtraArmorConfig armor_extra) {
			Supplier<Ingredient> ing = () -> Ingredient.of(ItemRegistrate.MAT_INGOTS[ordinal()].get());
			this.id = name;
			this.tier = new ForgeTier(level, tool.durability, tool.speed, 0, tool.enchant,
					getBlockTag(level), ing);
			this.mat = new Mat(name, armor.durability, armor.protection,
					armor.enchant, equip_sound, armor.tough, armor.kb, ing);
			this.tool_config = tool_config;
			this.armor_config = armor_config;
			this.tool_stats = tool;
			this.tool_extra = tool_extra;
			this.armor_extra = armor_extra;
		}

		public Item getArmor(EquipmentSlot slot) {
			return ItemRegistrate.GEN_ITEM[ordinal()][slot.getIndex()].get();
		}

		public Item getTool(Tools tool) {
			return ItemRegistrate.GEN_ITEM[ordinal()][4 + tool.ordinal()].get();
		}

	}

	public enum Tools {
		SWORD, AXE, SHOVEL, PICKAXE, HOE
	}

	public record ToolStats(int durability, int speed, int[] add_dmg, float[] add_speed, int enchant) {
	}

	public record ArmorStats(int durability, int[] protection, float tough, float kb, int enchant) {
	}

	public interface ArmorFactory {

		ArmorItem get(Mats mat, EquipmentSlot slot, Item.Properties props);

	}

	public interface RawToolFactory {

		TieredItem get(Tier tier, int dmg, float speed, Item.Properties props);
	}

	public interface ToolFactory {

		TieredItem get(Mats mat, Tools tool, Item.Properties props);

	}

	public interface EntryProcessor {

		ItemBuilder<Item, LLRegistrate> apply(ItemBuilder<Item, LLRegistrate> builder);

	}

	public record ToolConfig(EntryProcessor func, ToolFactory sup) {
	}

	public record ArmorConfig(EntryProcessor func, ArmorFactory sup) {
	}

	private static Tag<Block> getBlockTag(int level) {
		return switch (level) {
			case 0 -> Tags.Blocks.NEEDS_WOOD_TOOL;
			case 1 -> BlockTags.NEEDS_STONE_TOOL;
			case 2 -> BlockTags.NEEDS_IRON_TOOL;
			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> Tags.Blocks.NEEDS_NETHERITE_TOOL;
		};
	}

	private static ToolFactory fromToolGen(Function<Tools, RawToolFactory> gen) {
		return (mat, tool, prop) -> gen.apply(tool).get(mat.tier,
				mat.tool_stats.add_dmg[tool.ordinal()] - 1,
				mat.tool_stats.add_speed[tool.ordinal()] - 4, prop);
	}

	public static ItemEntry<Item>[][] genItem() {
		int n = Mats.values().length;
		ItemEntry[][] ans = new ItemEntry[n][9];
		for (int i = 0; i < n; i++) {
			Mats mat = Mats.values()[i];
			String id = mat.id;
			BiFunction<String, EquipmentSlot, ItemEntry> armor_gen = (str, slot) ->
					mat.armor_config.func.apply(REGISTRATE.item(id + str,
							p -> mat.armor_config.sup.get(mat, slot, p))).register();
			ans[i][3] = armor_gen.apply("_helmet", EquipmentSlot.HEAD);
			ans[i][2] = armor_gen.apply("_chestplate", EquipmentSlot.CHEST);
			ans[i][1] = armor_gen.apply("_leggings", EquipmentSlot.LEGS);
			ans[i][0] = armor_gen.apply("_boots", EquipmentSlot.FEET);
			BiFunction<String, Tools, ItemEntry> tool_gen = (str, tool) ->
					mat.tool_config.func.apply(REGISTRATE.item(id + str,
							p -> mat.tool_config.sup.get(mat, tool, p))).register();
			for (int j = 0; j < Tools.values().length; j++) {
				Tools tool = Tools.values()[j];
				ans[i][4 + j] = tool_gen.apply("_" + tool.name().toLowerCase(Locale.ROOT), tool);
			}
		}
		return ans;
	}

	public static ItemEntry<Item>[] genMats(String suffix) {
		int n = Mats.values().length;
		ItemEntry[] ans = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			ans[i] = REGISTRATE.item(Mats.values()[i].id + suffix, Item::new).defaultModel().defaultLang().register();
		}
		return ans;
	}

	public static BlockEntry<Block>[] genBlockMats() {
		int n = Mats.values().length;
		BlockEntry[] ans = new BlockEntry[n];
		for (int i = 0; i < n; i++) {
			ans[i] = REGISTRATE.block(Mats.values()[i].id + "_block", p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
					.defaultLoot().defaultBlockstate().simpleItem().defaultLang().register();
		}
		return ans;
	}

}
