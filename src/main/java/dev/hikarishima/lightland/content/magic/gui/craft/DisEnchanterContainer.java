package dev.hikarishima.lightland.content.magic.gui.craft;

import com.google.common.collect.Maps;
import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.common.capability.player.MagicHolder;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.init.special.MagicRegistry;
import dev.xkmc.l2library.menu.BaseContainerMenu;
import dev.xkmc.l2library.menu.SpriteManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DisEnchanterContainer extends BaseContainerMenu<DisEnchanterContainer> {

	public static final SpriteManager MANAGER = new SpriteManager(LightLand.MODID, "disenchanter");

	protected final Map<MagicElement, Integer> map = Maps.newLinkedHashMap();
	protected final Map<Enchantment, IMagicRecipe<?>> ench_map;
	protected final Map<Enchantment, MagicElement[]> temp = Maps.newLinkedHashMap();

	public DisEnchanterContainer(MenuType<DisEnchanterContainer> type, int wid, Inventory plInv) {
		super(type, wid, plInv, MANAGER, (menu) -> new BaseContainer<>(3, menu), true);
		addSlot("main_slot", stack -> stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK);
		addSlot("gold_slot", stack -> stack.getItem() == Items.GOLD_NUGGET);
		addSlot("ench_slot", stack -> false);
		ench_map = IMagicRecipe.getMap(plInv.player.level, MagicRegistry.MPT_ENCH.get());
		for (Enchantment enc : ForgeRegistries.ENCHANTMENTS.getValues()) {
			if (!ench_map.containsKey(enc)) {
				int seed = enc.getRegistryName().toString().hashCode();
				Random r = new Random(seed);
				MagicElement[] elems = new MagicElement[3];
				List<MagicElement> list = new ArrayList<>(LightLandRegistry.ELEMENT.getValues());
				for (int i = 0; i < 3; i++) {
					elems[i] = list.get(r.nextInt(list.size()));
				}
				temp.put(enc, elems);
			}
		}
	}

	@Override
	public boolean clickMenuButton(Player pl, int btn) {
		ItemStack stack = container.getItem(0);
		if (stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK) {
			Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(stack);
			int[] arr = new int[1];
			enchs.entrySet().removeIf((e) -> {
				if (e.getValue() > 0 && (ench_map.containsKey(e.getKey()) || temp.containsKey(e.getKey()))) {
					arr[0] += e.getValue();
					return true;
				}
				return false;
			});
			MagicHolder h = LLPlayerData.get(inventory.player).magicHolder;
			map.forEach(h::addElement);
			if (stack.getItem() == Items.ENCHANTED_BOOK) {
				container.setItem(0, Items.BOOK.getDefaultInstance());
			} else {
				EnchantmentHelper.setEnchantments(enchs, stack);
			}
			ItemStack res = container.getItem(2);
			if (arr[0] > 64)
				arr[0] = 64;
			if (!res.isEmpty() && 64 - res.getCount() < arr[0])
				arr[0] = 64 - res.getCount();
			ItemStack gold = container.getItem(1);
			if (gold.getCount() <= arr[0]) {
				arr[0] = gold.getCount();
				container.setItem(1, ItemStack.EMPTY);
			} else gold.shrink(arr[0]);
			if (res.isEmpty()) {
				container.setItem(2, new ItemStack(LightlandItems.ENC_GOLD_NUGGET.get(), arr[0]));
			} else res.grow(arr[0]);
			slotsChanged(container);
			return true;
		}
		return super.clickMenuButton(pl, btn);
	}

	@Override
	public void slotsChanged(Container inv) {
		ItemStack stack = inv.getItem(0);
		map.clear();
		if (stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK) {
			for (Map.Entry<Enchantment, Integer> e : EnchantmentHelper.getEnchantments(stack).entrySet()) {
				if (e.getValue() > 0) {
					Enchantment ench = e.getKey();
					if (ench_map.containsKey(ench)) {
						IMagicRecipe<?> r = ench_map.get(ench);
						for (MagicElement elem : r.getElements()) {
							if (map.containsKey(elem))
								map.put(elem, map.get(elem) + e.getValue());
							else
								map.put(elem, e.getValue());
						}
					}
					//TODO temporary fix to unknown enchantments
					else if (temp.containsKey(ench)) {
						MagicElement[] elems = temp.get(ench);
						for (MagicElement elem : elems) {
							if (map.containsKey(elem))
								map.put(elem, map.get(elem) + e.getValue());
							else
								map.put(elem, e.getValue());
						}
					}
				}
			}
		}
		super.slotsChanged(inv);
	}

}
