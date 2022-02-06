package dev.hikarishima.lightland.content.arcane.internal;

import dev.hikarishima.lightland.content.arcane.item.ArcaneAxe;
import dev.hikarishima.lightland.content.arcane.item.ArcaneSword;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.events.ItemUseEventHandler;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ArcaneItemUseHelper implements ItemUseEventHandler.ItemClickHandler {

    public static final ArcaneItemUseHelper INSTANCE = new ArcaneItemUseHelper();

    private ArcaneItemUseHelper() {
        ItemUseEventHandler.LIST.add(this);
    }

    public static boolean isArcaneItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ArcaneSword || item instanceof ArcaneAxe;
    }

    /**
     * execute in both server and client, specific action side logic handled inside Arcane class
     */
    public static boolean executeArcane(
            Player player, LLPlayerData magic,
            ItemStack stack, ArcaneType type, LivingEntity target) {
        if (!magic.magicAbility.isArcaneTypeUnlocked(type))
            return false;
        CompoundTag tag = stack.getTagElement("arcane");
        if (tag == null || !tag.contains(type.getID()))
            return false;
        String str = tag.getString(type.getID());
        ResourceLocation rl = new ResourceLocation(str);
        Arcane arcane = LightLandRegistry.ARCANE.getValue(rl);
        if (arcane == null || arcane.cost > tag.getInt("mana"))
            return false;
        if (arcane.activate(player, magic, stack, target)) {
            if (!player.level.isClientSide())
                tag.putInt("mana", tag.getInt("mana") - arcane.cost);
            return true;
        }
        return false;
    }

    /**
     * execute on both server and client (for animation)
     */
    public static void rightClickAxe(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement("arcane");
        tag.putBoolean("charged", !tag.getBoolean("charged"));
    }

    public static boolean isAxeCharged(ItemStack stack) {
        return stack.getOrCreateTagElement("arcane").getBoolean("charged");
    }

    public static void addArcaneMana(ItemStack stack, int mana) {
        IArcaneItem item = (IArcaneItem) stack.getItem();
        CompoundTag tag = stack.getOrCreateTagElement("arcane");
        tag.putInt("mana", Mth.clamp(tag.getInt("mana") + mana, 0, item.getMaxMana(stack)));
    }

    public static int getArcaneMana(ItemStack stack) {
        return stack.getOrCreateTagElement("arcane").getInt("mana");
    }

    /**
     * executes on server only, play animation in client
     */
    private static void handleLeftClickEvent(ItemStack stack, PlayerInteractEvent event, LivingEntity target) {
        Player player = event.getPlayer();
        LLPlayerData magic = LLPlayerData.get(player);
        if (stack.getItem() instanceof ArcaneAxe) {
            ArcaneType type = isAxeCharged(stack) ? ArcaneType.DUBHE : ArcaneType.MEGREZ;
            if (executeArcane(player, magic, stack, type, target)) {
                if (event.isCancelable())
                    event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        } else if (stack.getItem() instanceof ArcaneSword) {
            if (executeArcane(player, magic, stack, ArcaneType.ALIOTH, target)) {
                if (event.isCancelable())
                    event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    /**
     * executes on server only, play animation in client
     */
    private static void handleRightClickEvent(ItemStack stack, PlayerInteractEvent event, LivingEntity target) {
        boolean cancellable = event.isCancelable();
        if (stack.getItem() instanceof ArcaneAxe) {
            rightClickAxe(stack);
            if (cancellable) event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        } else if (stack.getItem() instanceof ArcaneSword) {
            if (executeArcane(event.getPlayer(),
                    LLPlayerData.get(event.getPlayer()),
                    stack, ArcaneType.ALKAID, target)) {
                if (cancellable) event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    public static LivingEntity toLiving(Entity e) {
        return e instanceof LivingEntity ? (LivingEntity) e : null;
    }

    @Override
    public boolean predicate(ItemStack stack, Class<? extends PlayerEvent> cls, PlayerEvent event) {
        return isArcaneItem(stack);
    }

    @Override
    public void onPlayerLeftClickEmpty(ItemStack stack, PlayerInteractEvent.LeftClickEmpty event) {
        handleLeftClickEvent(stack, event, null);
    }

    @Override
    public void onPlayerLeftClickBlock(ItemStack stack, PlayerInteractEvent.LeftClickBlock event) {
        handleLeftClickEvent(stack, event, null);
    }

    /**
     * executes on both server and client side
     */
    @Override
    public void onPlayerLeftClickEntity(ItemStack stack, AttackEntityEvent event) {
        float charge = event.getPlayer().getAttackStrengthScale(0.5f);
        if (event.getEntityLiving() != null && charge > 0.9f) {
            addArcaneMana(stack, 1);
        }
    }

    /**
     * executes on server only
     */
    @Override
    public void onCriticalHit(ItemStack stack, CriticalHitEvent event) {
        if (event.getPlayer().level.isClientSide())
            return;
        Player player = event.getPlayer();
        LLPlayerData magic = LLPlayerData.get(player);
        Entity e = event.getTarget();
        LivingEntity le = toLiving(event.getTarget());
        ArcaneType type = null;
        boolean cr = event.isVanillaCritical();
        if (stack.getItem() instanceof ArcaneAxe) {
            boolean ch = isAxeCharged(stack);
            type = cr ? ch ? ArcaneType.MERAK : ArcaneType.PHECDA : ch ? ArcaneType.DUBHE : ArcaneType.MEGREZ;
        } else if (stack.getItem() instanceof ArcaneSword) {
            type = cr ? ArcaneType.MIZAR : ArcaneType.ALIOTH;
        }
        if (type != null)
            executeArcane(player, magic, stack, type, le);
    }

    @Override
    public void onPlayerRightClickEmpty(ItemStack stack, PlayerInteractEvent.RightClickEmpty event) {
        handleRightClickEvent(stack, event, null);
    }

    @Override
    public void onPlayerRightClickBlock(ItemStack stack, PlayerInteractEvent.RightClickBlock event) {
        handleRightClickEvent(stack, event, null);
    }

    @Override
    public void onPlayerRightClickEntity(ItemStack stack, PlayerInteractEvent.EntityInteract event) {
        handleRightClickEvent(stack, event, toLiving(event.getTarget()));
    }

}
