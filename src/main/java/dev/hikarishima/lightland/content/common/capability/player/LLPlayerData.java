package dev.hikarishima.lightland.content.common.capability.player;

import dev.lcy0x1.base.Proxy;
import dev.lcy0x1.serial.ExceptionHandler;
import dev.lcy0x1.serial.SerialClass;
import dev.lcy0x1.serial.codec.TagCodec;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.function.Consumer;

@SerialClass
public class LLPlayerData {

	public static Capability<LLPlayerData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static LLPlayerData get(Player e) {
		LLPlayerData data;
		if (!e.getCapability(CAPABILITY).isPresent()) {
			e.reviveCaps();
			data = e.getCapability(CAPABILITY).resolve().get().check();
			e.invalidateCaps();
		} else data = e.getCapability(CAPABILITY).resolve().get().check();
		return data;
	}

	public static boolean isProper(Player player) {
		return player.getCapability(CAPABILITY).isPresent();
	}

	private static CompoundTag revive_cache;

	@OnlyIn(Dist.CLIENT)
	public static void cacheSet(CompoundTag tag, boolean force) {
		AbstractClientPlayer pl = Proxy.getClientPlayer();
		if (!force && pl != null && pl.getCapability(CAPABILITY).cast().resolve().isPresent()) {
			LLPlayerData m = LLPlayerData.get(pl);
			m.reset(Reset.FOR_INJECT);
			ExceptionHandler.run(() -> TagCodec.fromTag(tag, LLPlayerData.class, m, f -> true));
			m.init();
		} else revive_cache = tag;
	}

	@OnlyIn(Dist.CLIENT)
	public static CompoundTag getCache(Player pl) {
		CompoundTag tag = revive_cache;
		revive_cache = null;
		if (tag == null)
			tag = TagCodec.toTag(new CompoundTag(), get(pl));
		return tag;
	}

	@SerialClass.SerialField
	public State state = State.PREINJECT;
	@SerialClass.SerialField
	public AbilityPoints abilityPoints = new AbilityPoints(this);
	@SerialClass.SerialField
	public MagicAbility magicAbility = new MagicAbility(this);
	@SerialClass.SerialField
	public SkillCap skillCap = new SkillCap(this);
	@SerialClass.SerialField
	public MagicHolder magicHolder = new MagicHolder(this);
	public Player player;
	public Level world;

	public void tick() {
		magicAbility.tick();
		skillCap.tick();
	}

	public void reset(Reset reset) {
		reset.cons.accept(this);
	}

	protected void init() {
		if (state == null) {
			reset(Reset.FOR_INJECT);
		}
		if (state != State.ACTIVE) {
			magicHolder.checkUnlocks();
			abilityPoints.updateAttribute();
			state = State.ACTIVE;
		}
	}

	public void reInit() {
		state = State.PREINIT;
		check();
	}

	private LLPlayerData check() {
		if (state != State.ACTIVE)
			init();
		return this;
	}

	@SerialClass.OnInject
	public void onInject() {
		if (state == State.PREINJECT || state == State.ACTIVE)
			state = State.PREINIT;
	}

	public enum State {
		PREINJECT, PREINIT, ACTIVE
	}

	public enum Reset {
		ABILITY((h) -> {
			h.magicAbility = new MagicAbility(h);
			h.abilityPoints = new AbilityPoints(h);
			h.abilityPoints.updateAttribute();
		}), HOLDER((h) -> {
			h.magicHolder = new MagicHolder(h);
			h.magicHolder.checkUnlocks();
		}), SKILL(h -> {
			h.skillCap = new SkillCap(h);
		}),
		ALL((h) -> {
			ABILITY.cons.accept(h);
			HOLDER.cons.accept(h);
			SKILL.cons.accept(h);
		}), FOR_INJECT((h) -> {
			h.state = State.PREINJECT;
			h.magicAbility = new MagicAbility(h);
			h.abilityPoints = new AbilityPoints(h);
			h.skillCap = new SkillCap(h);
			h.magicHolder = new MagicHolder(h);
		});

		final Consumer<LLPlayerData> cons;

		Reset(Consumer<LLPlayerData> cons) {
			this.cons = cons;
		}
	}

}
