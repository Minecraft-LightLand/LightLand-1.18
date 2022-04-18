package dev.hikarishima.lightland.compat.jei.recipes;

import dev.hikarishima.lightland.content.common.capability.player.CapProxy;
import dev.hikarishima.lightland.content.magic.item.MagicWand;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.content.magic.ritual.AbstractRitualRecipe;
import dev.hikarishima.lightland.content.magic.ritual.PotionBoostRecipe;
import dev.hikarishima.lightland.content.magic.ritual.PotionSpellRecipe;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.registrate.LightlandBlocks;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicCraftRecipeCategory extends BaseRecipeCategory<AbstractRitualRecipe<?>, MagicCraftRecipeCategory> {

	public MagicCraftRecipeCategory() {
		super(new ResourceLocation(LightLand.MODID, "ritual"), cast(AbstractRitualRecipe.class));
	}

	public MagicCraftRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 36, 145, 54);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, LightlandBlocks.B_RITUAL_CORE.get().asItem().getDefaultInstance());
		return this;
	}

	@Override
	public Component getTitle() {
		return LangData.IDS.CONT_RITUAL.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, AbstractRitualRecipe<?> recipe, IFocusGroup focuses) {
		List<AbstractRitualRecipe.Entry> entry = new ArrayList<>(recipe.side);
		while (entry.size() < 8) {
			entry.add(new AbstractRitualRecipe.Entry());
		}
		entry.add(4, recipe.core);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				ItemStack item = specialProcess(recipe, entry.get(i * 3 + j).input, i * 3 + j == 4);
				if (!item.isEmpty()) {
					builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 1, i * 18 + 1)
							.addIngredient(VanillaTypes.ITEM_STACK, item);
				}
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				ItemStack item = specialProcess(recipe, entry.get(i * 3 + j).output, i * 3 + j == 4);
				if (!item.isEmpty()) {
					builder.addSlot(RecipeIngredientRole.OUTPUT, 90 + j * 18 + 1, i * 18 + 1)
							.addIngredient(VanillaTypes.ITEM_STACK, item);
				}
			}
		}
		MagicWand wand = LightlandItems.MAGIC_WAND.get();
		ItemStack wand_stack = wand.getDefaultInstance();
		IMagicRecipe<?> magic = recipe.getMagic() == null ? null : CapProxy.getHandler().magicHolder.getRecipe(recipe.getMagic());
		if (magic != null) {
			wand.setMagic(magic, wand_stack);
		}
		builder.addSlot(RecipeIngredientRole.CATALYST, 64, 1).addIngredient(VanillaTypes.ITEM_STACK, wand_stack);
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

}
