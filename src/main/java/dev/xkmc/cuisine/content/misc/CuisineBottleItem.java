package dev.xkmc.cuisine.content.misc;

import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.registrate.CuisineFluids;
import dev.xkmc.cuisine.init.registrate.CuisineItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class CuisineBottleItem extends Item {

	public static final int MAX = 250;

	public CuisineBottleItem(Item.Properties prop) {
		super(prop);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		FluidStack fluid = getFluid(stack);
		return fluid.getAmount() % 50 != 0;
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
		return 0xffffff;
	}

	public static FluidStack getFluid(ItemStack stack) {
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).resolve().orElse(null);
		return (item instanceof FluidHandlerItemStack ans) ? ans.getFluid() : FluidStack.EMPTY;
	}

	@SubscribeEvent
	public static void onItemStackCapability(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof CuisineBottleItem item) {
			event.addCapability(new ResourceLocation(Cuisine.MODID, "fluid"),
					new FluidHandlerItemStack(event.getObject(), MAX));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void onClientInit() {
		ItemProperties.register(CuisineItems.BOTTLE.get(), new ResourceLocation(Cuisine.MODID, "amount"),
				(stack, level, entity, i) -> getFluid(stack).getAmount() * 1f / MAX);
	}

}
