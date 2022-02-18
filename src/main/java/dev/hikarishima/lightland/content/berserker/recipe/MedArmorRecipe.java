package dev.hikarishima.lightland.content.berserker.recipe;

import com.google.gson.JsonObject;
import dev.hikarishima.lightland.content.berserker.item.MedicineItem;
import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MedArmorRecipe extends ShapedRecipe {

    public MedArmorRecipe(ResourceLocation rl, String group, int w, int h, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(rl, group, w, h, ingredients, result);
    }

    @Override
    public boolean matches(CraftingContainer cont, Level level) {
        boolean match = super.matches(cont, level);
        if (!match) return false;
        ItemStack init = null;
        for (int i = 0; i < cont.getContainerSize(); i++) {
            ItemStack stack = cont.getItem(i);
            if (stack.getItem() instanceof MedicineItem) {
                if (init == null) {
                    init = stack;
                } else {
                    if (!MedicineItem.eq(stack, init))
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer cont) {
        ItemStack result = super.assemble(cont);
        ItemStack init = null;
        for (int i = 0; i < cont.getContainerSize(); i++) {
            ItemStack stack = cont.getItem(i);
            if (stack.getItem() instanceof MedicineItem) {
                init = stack;
                break;
            }
        }
        if (init != null)
            PotionUtils.setCustomEffects(result, PotionUtils.getCustomEffects(init));
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistrate.RSC_MED_ARMOR.get();
    }

    public static class Ser extends ShapedRecipe.Serializer {

        public ShapedRecipe fromJson(ResourceLocation id, JsonObject obj) {
            ShapedRecipe r = super.fromJson(id, obj);
            return new MedArmorRecipe(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.getResultItem());
        }

        public ShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf obj) {
            ShapedRecipe r = super.fromNetwork(id, obj);
            if (r == null) {
                return null;
            }
            return new MedArmorRecipe(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.getResultItem());
        }

    }

}
