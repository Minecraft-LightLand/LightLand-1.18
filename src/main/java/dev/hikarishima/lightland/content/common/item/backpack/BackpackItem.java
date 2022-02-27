package dev.hikarishima.lightland.content.common.item.backpack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;

public class BackpackItem extends Item {

    public static ListTag getListTag(ItemStack stack) {
        if (stack.getOrCreateTag().contains("Items")) {
            return stack.getOrCreateTag().getList("Items", Tag.TAG_COMPOUND);
        } else {
            return new ListTag();
        }
    }

    public static void setListTag(ItemStack stack, ListTag list) {
        stack.getOrCreateTag().put("Items", list);
    }

    public static final class MenuPvd implements MenuProvider {

        private final ServerPlayer player;
        private final int slot;
        private final ItemStack stack;

        public MenuPvd(ServerPlayer player, InteractionHand hand, ItemStack stack) {
            this.player = player;
            slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;
            this.stack = stack;
        }

        public MenuPvd(ServerPlayer player, int slot, ItemStack stack) {
            this.player = player;
            this.slot = slot;
            this.stack = stack;
        }

        @Override
        public Component getDisplayName() {
            return stack.getDisplayName();
        }

        @Override
        public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            UUID uuid = tag.getUUID("container_id");
            return new BackpackContainer(id, inventory, slot, uuid, tag.getInt("rows"));
        }

        public void writeBuffer(FriendlyByteBuf buf) {
            CompoundTag tag = stack.getOrCreateTag();
            UUID id = tag.getUUID("container_id");
            buf.writeInt(slot);
            buf.writeUUID(id);
            buf.writeInt(tag.getInt("rows"));
        }

        public void open() {
            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.getBoolean("init")) {
                tag.putBoolean("init", true);
                tag.putUUID("container_id", UUID.randomUUID());
                tag.putInt("rows", 3);
            }
            NetworkHooks.openGui(player, this, this::writeBuffer);
        }

    }

    public final DyeColor color;

    public BackpackItem(DyeColor color, Properties props) {
        super(props);
        this.color = color;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            new MenuPvd((ServerPlayer) player, hand, stack).open();
        } else {
            player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1, 1);
        }
        return InteractionResultHolder.success(stack);
    }

}
