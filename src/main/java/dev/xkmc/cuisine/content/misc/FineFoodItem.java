package dev.xkmc.cuisine.content.misc;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.cuisine.init.data.CuisineTags.AllFluidTags;
import dev.xkmc.cuisine.init.data.CuisineTags.AllItemTags;
import dev.xkmc.cuisine.init.registrate.CuisineEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FineFoodItem extends Item {

	public enum TasteTag {
		ABSORB_SALT(AllItemTags.ABSORB_SALT, null),
		GREASY(AllItemTags.MEAT, AllFluidTags.GREASY),
		TOO_GREASY(AllItemTags.GREASY, null),
		VEGES(AllItemTags.VEGES, AllFluidTags.VEGES),
		SALTY(AllItemTags.SALTY, AllFluidTags.SALTY),
		SWEET(AllItemTags.SWEET, AllFluidTags.SWEET),
		SPICY(AllItemTags.SPICY, AllFluidTags.SPICY),
		NUMB(AllItemTags.NUMB, null),
		SOUR(AllItemTags.SOUR, AllFluidTags.SOUR),
		KELP(AllItemTags.KELP, AllFluidTags.KELP),
		SESAME(AllItemTags.SESAME, AllFluidTags.SESAME);

		private final AllItemTags item;
		private final AllFluidTags fluid;

		TasteTag(AllItemTags item, @Nullable AllFluidTags fluid) {
			this.item = item;
			this.fluid = fluid;
		}

		public boolean matches(Item val) {
			return item.matches(val);
		}

		public boolean matches(FluidStack val) {
			return fluid != null && fluid.matches(val.getFluid());
		}
	}

	public enum FoodProps {
		SWEET(TasteTag.SWEET, TasteTag.SPICY, null,
				CuisineEffects.TOO_SPICY, CuisineEffects.SPICY, null,
				CuisineEffects.SWEET, CuisineEffects.TOO_SWEET),
		SALTY(TasteTag.SALTY, TasteTag.ABSORB_SALT, null,
				CuisineEffects.BLAND, CuisineEffects.BLAND, CuisineEffects.BLAND,
				CuisineEffects.TASTEFUL, CuisineEffects.TOO_SALTY),
		SOUR(TasteTag.SOUR, null, null,
				null, null, null, CuisineEffects.SOUR, CuisineEffects.TOO_SOUR),
		GREASY(TasteTag.GREASY, TasteTag.VEGES, TasteTag.TOO_GREASY,
				CuisineEffects.NO_OIL, CuisineEffects.NO_OIL, CuisineEffects.NO_OIL,
				CuisineEffects.OIL, CuisineEffects.GREASY),
		SESAME(TasteTag.SESAME, null, null,
				null, null, null, CuisineEffects.SESAME, CuisineEffects.SESAME),
		KELP(TasteTag.KELP, null, null,
				null, null, null, CuisineEffects.KELP, CuisineEffects.KELP),
		NUMB(TasteTag.NUMB, null, null,
				null, null, null, CuisineEffects.NUMB, CuisineEffects.NUMB);

		private final TasteTag up, down, sup;
		private final CuisineEffects[] effs;

		FoodProps(TasteTag up, @Nullable TasteTag down, @Nullable TasteTag sup,
				  @Nullable CuisineEffects $n2, @Nullable CuisineEffects $n1, @Nullable CuisineEffects $0,
				  @Nullable CuisineEffects $1, @Nullable CuisineEffects $2) {
			this.up = up;
			this.down = down;
			this.sup = sup;
			this.effs = new CuisineEffects[]{$n2, $n1, $0, $1, $2};
		}
	}

	@SerialClass
	public static class FoodProperty {

		@Nullable
		public static FoodProperty load(ItemStack stack) {
			CompoundTag tag = stack.getTag();
			if (tag == null) return null;
			if (!tag.contains("cuisine")) return null;
			tag = tag.getCompound("cuisine");
			return TagCodec.fromTag(tag, FoodProperty.class);
		}

		public void save(ItemStack stack) {
			stack.getOrCreateTag().put("cuisine", TagCodec.toTag(new CompoundTag(), this));
		}

		@SerialClass.SerialField
		private final HashMap<FoodProps, Integer> map = new HashMap<>();

		@SerialClass.SerialField
		private int stochastic = 0;

		@SerialClass.SerialField
		private boolean fine = true;

		@SerialClass.SerialField
		private final HashMap<MobEffect, MobEffectInstance> effects = new HashMap<>();

		public void add(ItemStack stack) {
			Item item = stack.getItem();
			for (FoodProps props : FoodProps.values()) {
				int add = props.up.matches(item) ? 1 :
						props.down != null && props.down.matches(item) ? -1 :
								props.sup != null && props.sup.matches(item) ? 2 : 0;
				if (add != 0) {
					stochastic += stack.getCount();
					map.put(props, map.getOrDefault(props, 0) + add * stack.getCount());
				}
			}
			Optional.ofNullable(item.getFoodProperties(stack, null)).map(FoodProperties::getEffects)
					.ifPresent((e) -> e.forEach(p -> {
						MobEffectInstance ins = new MobEffectInstance(p.getFirst());
						float amp = p.getSecond();
						ins.duration *= amp;
						MobEffect eff = ins.getEffect();
						MobEffectInstance old = effects.get(eff);
						if (old != null) {
							old.update(ins);
							ins = old;
						}
						effects.put(eff, ins);
					}));
		}

		public void add(FluidStack stack) {
			for (FoodProps props : FoodProps.values()) {
				int add = props.up.matches(stack) ? 1 :
						props.down != null && props.down.matches(stack) ? -1 :
								props.sup != null && props.sup.matches(stack) ? 2 : 0;
				if (add != 0) {
					stochastic += stack.getAmount();
					map.put(props, map.getOrDefault(props, 0) + add * stack.getAmount());
				}
			}
		}

		public void wash() {
			map.clear();
			stochastic = 0;
			fine = false;
		}

		public List<CuisineEffects> getList() {
			List<CuisineEffects> ans = new ArrayList<>();
			map.forEach((k, v) -> {
				int val = Mth.clamp(v, -2, 2) + 2;
				CuisineEffects eff = k.effs[val];
				if (eff != null) {
					ans.add(eff);
				}
			});
			if (stochastic > 6) {
				ans.add(CuisineEffects.STOCHASTIC);
			}
			if (fine) {
				ans.add(CuisineEffects.FINE);
			}
			return ans;
		}

		public List<MobEffectInstance> getEffects() {
			List<MobEffectInstance> list = new ArrayList<>(effects.values());
			for (CuisineEffects eff : getList()) {
				list.add(eff.getEffect());
			}
			return list;
		}
	}

	public FineFoodItem(Properties properties) {
		super(properties);
	}

}
