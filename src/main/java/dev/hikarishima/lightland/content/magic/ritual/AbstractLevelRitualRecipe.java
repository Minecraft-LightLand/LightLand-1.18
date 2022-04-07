package dev.hikarishima.lightland.content.magic.ritual;

import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.lcy0x1.base.BaseRecipe;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

@SerialClass
public class AbstractLevelRitualRecipe<R extends AbstractLevelRitualRecipe<R>> extends AbstractRitualRecipe<R> {

	@SerialClass.SerialField
	public ResourceLocation magic_recipe;

	@SerialClass.SerialField
	public int[] levels;

	public AbstractLevelRitualRecipe(ResourceLocation id, BaseRecipe.RecType<R, AbstractRitualRecipe<?>, RitualCore.Inv> fac) {
		super(id, fac);
	}

	@Nullable
	public ResourceLocation getMagic() {
		return magic_recipe;
	}

	public int getLevel(int cost) {
		for (int i = 0; i < levels.length; i++) {
			if (cost > levels[i]) {
				return i;
			}
		}
		return levels.length;
	}

	public int getNextLevel(int cost) {
		for (int level : levels) {
			if (cost > level) {
				return level;
			}
		}
		return 0;
	}

}
