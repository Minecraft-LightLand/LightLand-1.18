package dev.xkmc.lightland.content.common.capability.player;

import dev.xkmc.l2library.serial.NBTObj;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.content.magic.products.MagicElement;
import dev.xkmc.lightland.content.magic.products.MagicProduct;
import dev.xkmc.lightland.content.magic.products.MagicProductType;
import dev.xkmc.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.lightland.init.special.LightLandRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

@SerialClass
public class MagicHolder {

	public static final int MAX_ELEMENTAL_MASTERY = 3;

	private final Map<MagicProductType<?, ?>, Map<ResourceLocation, MagicProduct<?, ?>>> product_cache = new HashMap<>();
	private final Map<ResourceLocation, IMagicRecipe<?>> recipe_cache = new HashMap<>();
	private final LLPlayerData parent;

	@SerialClass.SerialField
	public CompoundTag masteries = new CompoundTag();
	@SerialClass.SerialField
	public CompoundTag elements = new CompoundTag();
	@SerialClass.SerialField
	public CompoundTag products = new CompoundTag();

	MagicHolder(LLPlayerData parent) {
		this.parent = parent;
	}

	public int getElementalMastery(MagicElement elem) {
		return masteries.getInt(elem.getID());
	}

	public boolean addElementalMastery(MagicElement elem) {
		int current = getElementalMastery(elem);
		if (current >= MAX_ELEMENTAL_MASTERY)
			return false;
		masteries.putInt(elem.getID(), current + 1);
		checkUnlocks();
		return true;
	}

	public void checkUnlocks() {
		List<IMagicRecipe<?>> list = IMagicRecipe.getAll(parent.world);
		for (IMagicRecipe<?> r : list) {
			ResourceLocation id = r.id;
			recipe_cache.put(id, r);
		}
		for (IMagicRecipe<?> r : list) {
			if (isUnlocked(r))
				getProduct(r).setUnlock();
		}
	}

	private boolean isUnlocked(IMagicRecipe<?> r) {
		for (IMagicRecipe.ElementalMastery elem : r.elemental_mastery)
			if (getElementalMastery(elem.element) < elem.level)
				return false;
		for (ResourceLocation rl : r.predecessor) {
			MagicProduct<?, ?> prod = getProduct(recipe_cache.get(rl));
			if (prod != null && !prod.usable())
				return false;
		}
		return true;
	}

	@Nullable
	public IMagicRecipe<?> getRecipe(ResourceLocation rl) {
		return recipe_cache.get(rl);
	}

	public Collection<IMagicRecipe<?>> listRecipe() {
		return recipe_cache.values();
	}

	public MagicProduct<?, ?> getProduct(IMagicRecipe<?> r) {
		if (r == null)
			return null;
		MagicProductType<?, ?> type = r.product_type;
		Map<ResourceLocation, MagicProduct<?, ?>> submap;
		if (!product_cache.containsKey(type))
			product_cache.put(type, submap = new HashMap<>());
		else submap = product_cache.get(type);
		if (submap.containsKey(r.product_id))
			return submap.get(r.product_id);
		NBTObj nbt = new NBTObj(products).getSub(type.getID()).getSub(r.product_id.toString());
		MagicProduct<?, ?> ans = type.fac.get(parent, nbt, r.product_id, r);
		submap.put(r.product_id, ans);
		return ans;
	}

	public void addElement(MagicElement elem, Integer val) {
		elements.putInt(elem.getID(), elements.getInt(elem.getID()) + val);
	}

	public int getElement(MagicElement elem) {
		return elements.getInt(elem.getID());
	}

	@Nullable
	public IMagicRecipe<?> getTree(List<MagicElement> elem) {
		if (elem.size() == 0) {
			return null;
		}
		MagicElement type = elem.get(0);
		MagicProductType<?, ?> res = LightLandRegistry.PRODUCT_TYPE.get().getValues().stream()
				.filter(e -> e.elem == type)
				.findFirst().orElseThrow(() -> new NoSuchElementException("no matching type"));
		if (!product_cache.containsKey(res))
			return null;
		List<MagicProduct<?, ?>> ans = product_cache.get(res).values().stream()
				.filter(e -> e.usable() && e.matchList(elem)).toList();
		if (ans.size() == 1)
			return ans.get(0).recipe;
		return null;
	}

}
