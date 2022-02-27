package dev.hikarishima.lightland.content.common.item.backpack;

import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.MenuRegistrate;
import dev.hikarishima.lightland.util.annotation.ServerOnly;
import dev.lcy0x1.menu.BaseContainerMenu;
import dev.lcy0x1.util.SpriteManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BackpackContainer extends BaseContainerMenu<BackpackContainer> {

    public static final SpriteManager[] MANAGERS = new SpriteManager[4];

    static {
        for (int i = 0; i < 4; i++) {
            MANAGERS[i] = new SpriteManager(LightLand.MODID, "backpack_" + (i + 3));
        }
    }

    public static BackpackContainer fromNetwork(MenuType<BackpackContainer> type, int windowId, Inventory inv, FriendlyByteBuf buf) {
        InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        UUID id = buf.readUUID();
        int row = buf.readInt();
        return new BackpackContainer(windowId, inv, hand, id, row);
    }

    protected final Player player;
    protected final InteractionHand hand;
    protected final UUID uuid;

    public BackpackContainer(int windowId, Inventory inventory, InteractionHand hand, UUID uuid, int row) {
        super(MenuRegistrate.MT_BACKPACK.get(), windowId, inventory, MANAGERS[row - 3], menu -> new BaseContainer<>(row * 9, menu), false);
        this.player = inventory.player;
        this.hand = hand;
        this.uuid = uuid;
        this.addSlot("grid", stack -> !(stack.getItem() instanceof BackpackItem));
        if (!this.player.level.isClientSide()) {
            ItemStack stack = getStack();
            if (!stack.isEmpty()) {
                ListTag tag = BackpackItem.getListTag(stack);
                for (int i = 0; i < tag.size(); i++) {
                    this.container.setItem(i, ItemStack.of((CompoundTag) tag.get(i)));
                }
            }
        }
    }

    @ServerOnly
    @Override
    public boolean stillValid(Player player) {
        return !getStack().isEmpty();
    }

    @ServerOnly
    public ItemStack getStack() {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getTag();
        if (tag == null) return ItemStack.EMPTY;
        if (!tag.contains("container_id")) return ItemStack.EMPTY;
        if (!tag.getUUID("container_id").equals(uuid)) return ItemStack.EMPTY;
        return stack;
    }


    @Override
    public void removed(Player player) {
        if (!player.level.isClientSide) {
            ItemStack stack = getStack();
            if (!stack.isEmpty()) {
                ListTag list = new ListTag();
                for (int i = 0; i < this.container.getContainerSize(); i++) {
                    list.add(i, this.container.getItem(i).save(new CompoundTag()));
                }
                BackpackItem.setListTag(stack, list);
            }
        }
        super.removed(player);
    }

}
