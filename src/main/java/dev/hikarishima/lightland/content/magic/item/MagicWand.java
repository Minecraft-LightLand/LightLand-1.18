package dev.hikarishima.lightland.content.magic.item;

import dev.hikarishima.lightland.content.common.item.api.IGlowingTarget;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

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

    /*
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
    public ActionResult<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        String str = stack.getOrCreateTag().getString("recipe");
        if (str.length() == 0)
            return ActionResult.pass(stack);
        MagicProduct<?, ?> p = getData(player, stack);
        stack.getOrCreateTag().remove("recipe");
        if (p == null) {
            return ActionResult.pass(stack);
        }
        if (p.type == MagicRegistry.MPT_SPELL) {
            Spell<?, ?> sp = (Spell<?, ?>) p.item;
            if (sp.attempt(Spell.Type.WAND, player.level, player))
                player.getCooldowns().addCooldown(this, 60);
        }
        return ActionResult.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        Player pl = Proxy.getPlayer();
        if (world != null && pl != null) {
            MagicProduct<?, ?> p = getData(pl, stack);
            if (p != null) {
                list.add(new TranslationTextComponent(p.getDescriptionID()));
                if (p.type == MagicRegistry.MPT_SPELL) {
                    Spell<?, ?> spell = (Spell<?, ?>) p.item;
                    int cost = spell.getConfig(world, pl).mana_cost;
                    list.add(Translator.get("tooltip.mana_cost", cost));
                }
            }
        }
        super.appendHoverText(stack, world, list, flag);
    }

    public void setMagic(IMagicRecipe<?> recipe, ItemStack stack) {
        stack.getOrCreateTag().putString("recipe", recipe.id.toString());
    }
    */

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getDistance(ItemStack stack) {
        return 0;
        /*
        MagicProduct<?, ?> prod = getData(Proxy.getClientPlayer(), stack);
        if (prod == null || prod.type != MagicRegistry.MPT_SPELL)
            return 0;
        Spell<?, ?> spell = (Spell<?, ?>) prod.item;
        return spell.getDistance(Proxy.getClientPlayer());
        TODO
         */
    }
}
