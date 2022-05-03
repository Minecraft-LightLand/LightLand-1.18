package dev.xkmc.lightland.content.magic.item;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.common.item.api.IGlowingTarget;
import dev.xkmc.lightland.content.magic.products.MagicProduct;
import dev.xkmc.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.lightland.content.magic.spell.internal.Spell;
import dev.xkmc.lightland.init.data.LangData;
import dev.xkmc.lightland.init.special.MagicRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicWand extends Item implements IGlowingTarget {

	public MagicWand(Item.Properties props) {
		super(props.stacksTo(1));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getOrCreateTag().getString("recipe").length() > 0;
	}


	@Nullable
	public MagicProduct<?, ?> getData(Player player, ItemStack stack) {
		String str = stack.getOrCreateTag().getString("recipe");
		if (str.length() == 0)
			return null;
		LLPlayerData h = LLPlayerData.get(player);
		IMagicRecipe<?> r = h.magicHolder.getRecipe(new ResourceLocation(str));
		MagicProduct<?, ?> p = h.magicHolder.getProduct(r);
		return p.usable() ? p : null;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		String str = stack.getOrCreateTag().getString("recipe");
		if (str.length() == 0)
			return InteractionResultHolder.pass(stack);
		MagicProduct<?, ?> p = getData(player, stack);
		stack.getOrCreateTag().remove("recipe");
		if (p == null) {
			return InteractionResultHolder.pass(stack);
		}
		if (!world.isClientSide() && p.type == MagicRegistry.MPT_SPELL.get()) {
			Spell<?, ?> sp = (Spell<?, ?>) p.item;
			if (sp.attempt(Spell.Type.WAND, player.level, (ServerPlayer) player))
				player.getCooldowns().addCooldown(this, 60);
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		Player pl = Proxy.getPlayer();
		if (world != null && pl != null) {
			MagicProduct<?, ?> p = getData(pl, stack);
			if (p != null) {
				list.add(new TranslatableComponent(p.getDescriptionID()));
				if (p.type == MagicRegistry.MPT_SPELL.get()) {
					Spell<?, ?> spell = (Spell<?, ?>) p.item;
					int cost = spell.getConfig(world, pl).mana_cost;
					list.add(LangData.IDS.MANA_COST.get(cost));
				}
			}
		}
		super.appendHoverText(stack, world, list, flag);
	}

	public void setMagic(IMagicRecipe<?> recipe, ItemStack stack) {
		stack.getOrCreateTag().putString("recipe", recipe.id.toString());
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public int getDistance(ItemStack stack) {
		MagicProduct<?, ?> prod = getData(Proxy.getClientPlayer(), stack);
		if (prod == null || prod.type != MagicRegistry.MPT_SPELL.get())
			return 0;
		Spell<?, ?> spell = (Spell<?, ?>) prod.item;
		return spell.getDistance(Proxy.getClientPlayer());
	}
}
