package dev.xkmc.lightland.mixin;

import com.google.gson.JsonObject;
import dev.xkmc.lightland.content.magic.ritual.AbstractRitualRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

	@Inject(method = "itemStackFromJson", at = @At("HEAD"), cancellable = true)
	private static void injectItemFromJson(JsonObject obj, CallbackInfoReturnable<ItemStack> info) {
		if (obj.has("enchant_book")) {
			JsonObject book = obj.getAsJsonObject("enchant_book");
			Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(book.get("id").getAsString()));
			int lvl = book.get("lvl").getAsInt();
			assert ench != null;
			info.setReturnValue(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ench, lvl)));
			info.cancel();
		}
		if (obj.has("massive_stack")) {
			JsonObject jo = obj.getAsJsonObject("massive_stack");
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jo.get("item").getAsString()));
			int count = jo.get("count").getAsInt();
			ItemStack ans = Items.SHULKER_BOX.getDefaultInstance();
			NonNullList<ItemStack> nonnulllist = AbstractRitualRecipe.fill(item, count);
			ContainerHelper.saveAllItems(ans.getOrCreateTagElement("BlockEntityTag"), nonnulllist);
			info.setReturnValue(ans);
			info.cancel();
		}
	}

}
