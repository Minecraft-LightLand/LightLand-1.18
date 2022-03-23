package dev.hikarishima.lightland.content.common.item.backpack;

import dev.hikarishima.lightland.content.common.capability.worldstorage.StorageContainer;
import dev.hikarishima.lightland.content.common.capability.worldstorage.WorldStorage;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.registrate.BlockRegistrate;
import dev.hikarishima.lightland.util.annotation.ServerOnly;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorldChestItem extends BlockItem {

	public static final class MenuPvd implements MenuProvider {

		private final ServerPlayer player;
		private final ItemStack stack;
		private final int color;

		public MenuPvd(ServerPlayer player, ItemStack stack, int color) {
			this.player = player;
			this.stack = stack;
			this.color = color;
		}

		@Override
		public Component getDisplayName() {
			return stack.getDisplayName();
		}

		@ServerOnly
		@Override
		public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
			StorageContainer container = getContainer((ServerLevel) player.level).get();
			return new WorldChestContainer(id, inventory, container.id, container.container, container);
		}

		@ServerOnly
		public void writeBuffer(FriendlyByteBuf buf) {
			CompoundTag tag = stack.getOrCreateTag();
			UUID id = tag.getUUID("owner_id");
			buf.writeUUID(id);
		}

		@ServerOnly
		private Optional<StorageContainer> getContainer(ServerLevel level) {
			CompoundTag tag = stack.getOrCreateTag();
			UUID id = tag.getUUID("owner_id");
			long pwd = tag.getLong("password");
			return WorldStorage.get(level).getOrCreateStorage(id, color, pwd);
		}

		@ServerOnly
		public void open() {
			if (!stack.getOrCreateTag().contains("owner_id")) {
				stack.getOrCreateTag().putUUID("owner_id", player.getUUID());
				stack.getOrCreateTag().putString("owner_name",player.getName().getString());
				stack.getOrCreateTag().putLong("password", color);
			}
			if (player.level.isClientSide() || getContainer((ServerLevel) player.level).isEmpty())
				return;
			NetworkHooks.openGui(player, this, this::writeBuffer);
		}

	}

	public final DyeColor color;

	public WorldChestItem(DyeColor color, Properties props) {
		super(BlockRegistrate.WORLD_CHEST.get(), props);
		this.color = color;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			new MenuPvd((ServerPlayer) player, stack, color.getId()).open();
		} else {
			player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1, 1);
		}
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		CompoundTag tag = stack.getTag();
		if (tag == null) return;
		if (tag.contains("owner_name")) {
			String name = tag.getString("owner_name");
			list.add(LangData.IDS.STORAGE_OWNER.get(name));
		}
	}

}
