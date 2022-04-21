package dev.hikarishima.lightland.compat.jei.recipes;

import dev.hikarishima.lightland.compat.jei.LightLandJeiPlugin;
import dev.hikarishima.lightland.compat.jei.ingredients.ElementIngredient;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.hikarishima.lightland.init.registrate.LightlandMenu;
import dev.lcy0x1.base.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DisEnchanterRecipeCategory extends BaseRecipeCategory<IMagicRecipe<?>, DisEnchanterRecipeCategory> {

	protected static final ResourceLocation BG = new ResourceLocation(LightLand.MODID, "textures/jei/background.png");

	public DisEnchanterRecipeCategory() {
		super(new ResourceLocation(LightLand.MODID, "disenchant"), cast(IMagicRecipe.class));
	}

	public DisEnchanterRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 18, 176, 18);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, LightlandItems.DISENC_BOOK.get().getDefaultInstance());
		return this;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent(LightlandMenu.getLangKey(LightlandMenu.MT_DISENC.get()));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IMagicRecipe<?> recipe, IFocusGroup focuses) {
		Enchantment ench = (Enchantment) recipe.getProduct().item;
		List<ItemStack> l0 = new ArrayList<>();
		List<ItemStack> l1 = new ArrayList<>();
		List<ItemStack> l2 = new ArrayList<>();
		for (int i = 1; i <= ench.getMaxLevel(); i++) {
			l0.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ench, i)));
			l1.add(new ItemStack(Items.GOLD_NUGGET, i));
			l2.add(new ItemStack(LightlandItems.ENC_GOLD_NUGGET.get(), i));
		}
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(VanillaTypes.ITEM_STACK, l0);
		builder.addSlot(RecipeIngredientRole.INPUT, 19, 1).addIngredients(VanillaTypes.ITEM_STACK, l1);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 69, 1).addIngredients(VanillaTypes.ITEM_STACK, l2);
		IIngredientType<ElementIngredient> type = LightLandJeiPlugin.INSTANCE.ELEM_TYPE;

		int ind = 1;
		Map<MagicElement, Integer> map = new HashMap<>();
		for (MagicElement me : recipe.getElements()) {
			map.put(me, map.getOrDefault(me, 0) + 1);
		}
		for (Map.Entry<MagicElement, Integer> entry : map.entrySet()) {
			List<ElementIngredient> ans = new ArrayList<>();
			for (int i = 1; i <= ench.getMaxLevel(); i++)
				ans.add(new ElementIngredient(entry.getKey(), entry.getValue() * i));
			builder.addSlot(RecipeIngredientRole.OUTPUT, 69 + 18 * ind, 1)
					.addIngredients(type, ans);
			ind++;
		}
	}

}
