package dev.hikarishima.lightland.init.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.lcy0x1.util.SerialClass;
import dev.lcy0x1.util.Serializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

@SerialClass
public class EnchantmentIngredient extends Ingredient {

	public static final Ser SERIALIZER = new Ser();

	private static ItemStack getDefaultItem(Enchantment enchantment, int min_level) {
		ItemStack ans = new ItemStack(Items.ENCHANTED_BOOK);
		ans.enchant(enchantment, min_level);
		return ans;
	}

	@SerialClass.SerialField
	public Enchantment enchantment;
	@SerialClass.SerialField
	public int level;

	protected EnchantmentIngredient(Enchantment enchantment, int level) {
		super(Stream.of(new ItemValue(getDefaultItem(enchantment, level))));
		this.enchantment = enchantment;
		this.level = level;
	}

	@Override
	public boolean test(@Nullable ItemStack stack) {
		return stack != null && EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack) >= level;
	}

	@Override
	public JsonElement toJson() {
		JsonObject ans = new JsonObject();
		ans.addProperty("enchantment", enchantment.getRegistryName().toString());
		ans.addProperty("level", level);
		return ans;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return SERIALIZER;
	}

	public static class Ser implements IIngredientSerializer<EnchantmentIngredient> {

		@Override
		public EnchantmentIngredient parse(FriendlyByteBuf buffer) {
			return Serializer.from(buffer, EnchantmentIngredient.class, null);
		}

		@Override
		public EnchantmentIngredient parse(JsonObject json) {
			return Serializer.from(json, EnchantmentIngredient.class, null);
		}

		@Override
		public void write(FriendlyByteBuf buffer, EnchantmentIngredient ingredient) {
			Serializer.to(buffer, ingredient);
		}
	}

}
