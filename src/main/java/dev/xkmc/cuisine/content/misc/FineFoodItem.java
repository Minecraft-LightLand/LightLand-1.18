package dev.xkmc.cuisine.content.misc;

import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.init.data.CuisineTags.AllItemTags;
import dev.xkmc.cuisine.init.registrate.CuisineEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FineFoodItem extends Item {

	public enum FoodProps {
		SWEET(AllItemTags.SWEET, AllItemTags.SPICY, null,
				CuisineEffects.TOO_SPICY, CuisineEffects.SPICY, null,
				CuisineEffects.SWEET, CuisineEffects.TOO_SWEET),
		SALTY(AllItemTags.SALTY, AllItemTags.ABSORB_SALT, null,
				CuisineEffects.BLAND, CuisineEffects.BLAND, CuisineEffects.BLAND,
				CuisineEffects.TASTEFUL, CuisineEffects.TOO_SALTY),
		SOUR(AllItemTags.SOUR, null, null,
				null, null, null, CuisineEffects.SOUR, CuisineEffects.TOO_SOUR),
		GREASY(AllItemTags.MEAT, AllItemTags.VEGES, AllItemTags.GREASY,
				CuisineEffects.NO_OIL, CuisineEffects.NO_OIL, CuisineEffects.NO_OIL,
				CuisineEffects.OIL, CuisineEffects.GREASY),
		SESAME(AllItemTags.SESAME, null, null,
				null, null, null, CuisineEffects.SESAME, CuisineEffects.SESAME),
		KELP(AllItemTags.KELP, null, null,
				null, null, null, CuisineEffects.KELP, CuisineEffects.KELP),
		NUMB(AllItemTags.NUMB, null, null,
				null, null, null, CuisineEffects.NUMB, CuisineEffects.NUMB);

		private final AllItemTags up, down, sup;
		private final CuisineEffects[] effs;

		FoodProps(AllItemTags up, @Nullable AllItemTags down, @Nullable AllItemTags sup,
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

		@SerialClass.SerialField
		public HashMap<FoodProps, Integer> map = new HashMap<>();

		public void add(Item item) {
			for (FoodProps props : FoodProps.values()) {
				int add = props.up.matches(item) ? 1 :
						props.down != null && props.down.matches(item) ? -1 :
								props.sup != null && props.sup.matches(item) ? 2 : 0;
				map.put(props, map.getOrDefault(props, 0) + add);
			}
		}

		public List<MobEffectInstance> getList() {
			List<MobEffectInstance> ans = new ArrayList<>();
			map.forEach((k, v) -> {
				int val = Mth.clamp(v, -2, 2) + 2;
				CuisineEffects eff = k.effs[val];
				if (eff != null) {
					ans.add(eff.getEffect());
				}
			});
			return ans;
		}

	}

	public FineFoodItem(Properties properties) {
		super(properties);
	}

}
