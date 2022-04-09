package dev.hikarishima.lightland.compat.jei.recipes;

import dev.hikarishima.lightland.content.common.capability.player.CapProxy;
import dev.hikarishima.lightland.content.magic.item.MagicWand;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.content.magic.ritual.AbstractRitualRecipe;
import dev.hikarishima.lightland.content.magic.ritual.PotionBoostRecipe;
import dev.hikarishima.lightland.content.magic.ritual.PotionSpellRecipe;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.registrate.BlockRegistrate;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicCraftRecipeCategory implements IRecipeCategory<AbstractRitualRecipe<?>> {

	private static final ResourceLocation BG = new ResourceLocation(LightLand.MODID, "textures/jei/background.png");

	private final ResourceLocation id;
	private IDrawable background, icon;

	public MagicCraftRecipeCategory() {
		this.id = new ResourceLocation(LightLand.MODID, "ritual");
	}

	public MagicCraftRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 36, 145, 54);
		icon = guiHelper.createDrawableIngredient(BlockRegistrate.B_RITUAL_CORE.get().asItem().getDefaultInstance());
		return this;
	}

	@Override
	public ResourceLocation getUid() {
		return id;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Class getRecipeClass() {
		return AbstractRitualRecipe.class;
	}

	@Override
	public Component getTitle() {
		return LangData.IDS.CONT_RITUAL.get();
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(AbstractRitualRecipe<?> sl, IIngredients list) {
		List<Ingredient> input = new ArrayList<>();
		input.add(Ingredient.of(sl.core.input));
		for (AbstractRitualRecipe.Entry ent : sl.side) {
			if (!ent.input.isEmpty()) {
				input.add(Ingredient.of(ent.input));
			}
		}
		input.add(Ingredient.of(ItemRegistrate.MAGIC_WAND.get().getDefaultInstance()));
		list.setInputIngredients(input);
		List<ItemStack> output = new ArrayList<>();
		output.add(sl.core.output);
		for (AbstractRitualRecipe.Entry ent : sl.side) {
			if (!ent.output.isEmpty()) {
				output.add(ent.output);
			}
		}
		list.setOutputs(VanillaTypes.ITEM, output);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, AbstractRitualRecipe<?> sl, IIngredients list) {
		List<AbstractRitualRecipe.Entry> entry = new ArrayList<>(sl.side);
		while (entry.size() < 8) {
			entry.add(new AbstractRitualRecipe.Entry());
		}
		entry.add(4, sl.core);

		int in = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				ItemStack item = specialProcess(sl, entry.get(i * 3 + j).input, i * 3 + j == 4);
				if (!item.isEmpty())
					set(layout.getItemStacks(),
							Collections.singletonList(item),
							in++, true, j * 18, i * 18);
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				ItemStack item = specialProcess(sl, entry.get(i * 3 + j).output, i * 3 + j == 4);
				if (!item.isEmpty())
					set(layout.getItemStacks(),
							Collections.singletonList(item),
							in++, false, 90 + j * 18, i * 18);
			}
		}
		MagicWand wand = ItemRegistrate.MAGIC_WAND.get();
		ItemStack wand_stack = wand.getDefaultInstance();

        IMagicRecipe<?> magic = sl.getMagic() == null ? null : CapProxy.getHandler().magicHolder.getRecipe(sl.getMagic());
        if (magic != null) {
            wand.setMagic(magic, wand_stack);
        }

		set(layout.getItemStacks(), Collections.singletonList(wand_stack), in, true, 63, 0);
	}

	private static ItemStack specialProcess(AbstractRitualRecipe<?> sl, ItemStack stack, boolean isCore) {
		if (sl instanceof PotionBoostRecipe) {
			if (isCore) {
				stack = stack.copy();
				List<MobEffectInstance> list = PotionUtils.getCustomEffects(stack);
				MobEffect eff = ForgeRegistries.MOB_EFFECTS.getValue(((PotionBoostRecipe) sl).effect);
				list = list.stream().map(e -> {
					if (e.getEffect() != eff) {
						return new MobEffectInstance(eff, e.getDuration(), e.getAmplifier());
					}
					return e;
				}).collect(Collectors.toList());
				PotionUtils.setCustomEffects(stack, list);
			}
		}
		if (sl instanceof PotionSpellRecipe) {
			if (!isCore) {
				stack = stack.copy();
				CompoundTag compoundnbt = stack.getOrCreateTag();
				ListTag listnbt = compoundnbt.getList("CustomPotionEffects", 9);
				compoundnbt.put("CustomPotionEffects", listnbt);
				return stack;
			}
		}
		return stack;
	}

	private static <T> void set(IGuiIngredientGroup<T> group, List<T> t, int ind, boolean bool, int x, int y) {
		group.init(ind, bool, x, y);
		group.set(ind, t);
	}

}
