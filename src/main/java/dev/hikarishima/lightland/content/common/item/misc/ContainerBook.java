package dev.hikarishima.lightland.content.common.item.misc;

import dev.hikarishima.lightland.init.registrate.MenuRegistrate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ContainerBook extends Item implements MenuProvider {

    private final Supplier<MenuType<?>> cont;
    private final IFac fac;

    public ContainerBook(Properties props, Supplier<MenuType<?>> cont, IFac fac) {
        super(props);
        this.cont = cont;
        this.fac = fac;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide()) {
            player.openMenu(this);
        } else {
            player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0f, 1.0f);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(MenuRegistrate.getLangKey(cont.get()));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int wid, Inventory plInv, Player pl) {
        return fac.create(wid, plInv, pl);
    }

    public interface IFac {

        AbstractContainerMenu create(int wid, Inventory plInv, Player pl);

    }

}
