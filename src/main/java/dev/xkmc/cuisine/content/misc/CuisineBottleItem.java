package dev.xkmc.cuisine.content.misc;

import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.data.LangData;
import dev.xkmc.cuisine.init.registrate.CuisineFluids;
import dev.xkmc.cuisine.init.registrate.CuisineItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CuisineBottleItem extends Item {

	public static final int GRAN = 50, USE = 5, MAX = GRAN * USE;

	public CuisineBottleItem(Item.Properties prop) {
		super(prop);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
		if (this.allowdedIn(tab)) {
			for (CuisineFluids fluids : CuisineFluids.values()) {
				ItemStack stack = new ItemStack(this);
				if (fluids.fluid.get() != Fluids.EMPTY)
					new FluidHandlerItemStack(stack, MAX)
							.fill(new FluidStack(fluids.fluid.get(), MAX),
									IFluidHandler.FluidAction.EXECUTE);
				stacks.add(stack);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
		FluidStack fluid = getFluid(stack);
		if (!fluid.isEmpty()) {
			components.add(LangData.TOOLTIP_FLUID_TITLE.get());
			components.add(fluid.getDisplayName());
			if (fluid.getAmount() % GRAN == 0)
				components.add(LangData.TOOLTIP_FLUID_USES.get(fluid.getAmount() / GRAN, USE));
			else
				components.add(LangData.TOOLTIP_FLUID_AMOUNT.get(fluid.getAmount(), MAX));
		}
		super.appendHoverText(stack, level, components, flag);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		return fluid.getAmount() % GRAN != 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		return Math.round(fluid.getAmount() / 13f / MAX);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return 0xffffff;
	}

	public static int getFluidColor(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		for (CuisineFluids type : CuisineFluids.values()) {
			if (type.fluid.get() == fluid.getFluid())
				return type.color;
		}
		return 0xff00ff;
	}

	public static FluidStack getFluid(ItemStack stack) {
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).resolve().orElse(null);
		return (item instanceof BottleHandler ans) ? ans.getFluid() : FluidStack.EMPTY;
	}

	@SubscribeEvent
	public static void onItemStackCapability(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof CuisineBottleItem) {
			event.addCapability(new ResourceLocation(Cuisine.MODID, "fluid"),
					new BottleHandler(event.getObject(), MAX));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void onClientInit() {
		ItemProperties.register(CuisineItems.BOTTLE.get(), new ResourceLocation(Cuisine.MODID, "amount"),
				(stack, level, entity, i) -> getFluid(stack).getAmount() * 1f / MAX);
	}

}

class BottleHandler extends FluidHandlerItemStack {

	public BottleHandler(@NotNull ItemStack container, int capacity) {
		super(container, capacity);
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return CuisineTags.AllFluidTags.JAR_ACCEPT.matches(stack.getFluid());
	}
}
