package dev.hikarishima.lightland.content.questline.mobs.swamp;

import dev.hikarishima.lightland.network.config.ConfigSyncManager;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Random;

@SerialClass
public class SlimeProperties extends ConfigSyncManager.BaseConfig {

	@SerialClass
	public static class SlimeConfig {

		@SerialClass.SerialField
		public MobEffect effect = MobEffects.POISON;
		@SerialClass.SerialField
		public int weight = 0;
		@SerialClass.SerialField
		public Item drop = Items.SPIDER_EYE;
		@SerialClass.SerialField
		public double chance = 0.2;
		@SerialClass.SerialField
		public int duration = 100;
		@SerialClass.SerialField
		public int amplifier = 0;

		public String id = "";
		public MobEffectInstance ins = new MobEffectInstance(effect, duration, amplifier);

	}

	public static final SlimeConfig DEF = new SlimeConfig();

	@Nullable
	public static SlimeProperties getInstance() {
		return (SlimeProperties) ConfigSyncManager.CONFIGS.get("lightland:potion_slime_drop");
	}

	@SerialClass.SerialField
	public HashMap<String, SlimeConfig> map = new HashMap<>();

	@SerialClass.OnInject
	public void onInject() {
		map.forEach((v, k) -> {
			k.id = v;
			k.ins = new MobEffectInstance(k.effect, k.duration, k.amplifier);
		});
	}

	public static String getRandomConfig(Random random) {
		SlimeProperties ins = getInstance();
		if (ins == null) return "";
		return WeightedRandomList.create(ins.map.values().stream().map(e -> WeightedEntry.wrap(e.id, e.weight)).toList())
				.getRandom(random).map(WeightedEntry.Wrapper::getData).orElse("");
	}
}
