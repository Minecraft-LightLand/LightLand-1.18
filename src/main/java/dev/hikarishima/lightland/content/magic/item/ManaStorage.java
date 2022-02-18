package dev.hikarishima.lightland.content.magic.item;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.network.packets.CapToClient;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManaStorage extends Item {

    public static final int ARCANE_COST = 16;

    public final Item container;
    public final int mana;

    public ManaStorage(Properties props, Item container, int mana) {
        super(props);
        this.container = container;
        this.mana = mana;
    }

    public ItemStack finishUsingItem(ItemStack stack, Level w, LivingEntity e) {
        if (e instanceof ServerPlayer sp) {
            if (stack.isEdible()) {
                LLPlayerData data = LLPlayerData.get(sp);
                data.magicAbility.giveMana(mana);
                data.magicAbility.addSpellLoad(-mana);
                new CapToClient(CapToClient.Action.MAGIC_ABILITY, data).toClientPlayer(sp);
            }
        }
        return super.finishUsingItem(stack, w, e);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
