package dev.hikarishima.lightland.content.common.capability.player;

import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.common.capability.restriction.ArmorEnchant;
import dev.hikarishima.lightland.content.magic.item.MagicScroll;
import dev.hikarishima.lightland.content.magic.spell.internal.Spell;
import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import dev.hikarishima.lightland.network.packets.CapToClient;
import dev.hikarishima.lightland.util.EffectAddUtil;
import dev.lcy0x1.serial.NBTObj;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class MagicAbility {

	public static final int ACTIVATION = 600, SYNC_PERIOD = 100, ENCHANT_FACTOR = 4;
	public static final DamageSource LOAD = new DamageSource("spell_load").bypassArmor().bypassMagic();

	private final LLPlayerData parent;
	@SerialClass.SerialField
	public CompoundTag arcane_type = new CompoundTag();
	@SerialClass.SerialField
	public ListTag spell_activation = new ListTag();
	@SerialClass.SerialField
	public int magic_level, spell_level, tick;
	@SerialClass.SerialField
	public int magic_mana, spell_load;

	public int time_after_sync = 0;

	public MagicAbility(LLPlayerData parent) {
		this.parent = parent;
	}

	public void giveMana(int mana) {
		magic_mana = Mth.clamp(magic_mana + mana, 0, getMaxMana());
	}

	public void addSpellLoad(int load) {
		spell_load = Math.max(spell_load + load, 0);
	}

	public void tick() {
		tick++;
		time_after_sync++;
		if (tick % 20 == 0) {
			int armor_cost = (int) Math.ceil(1.0 / ENCHANT_FACTOR * ArmorEnchant.getArmorEnchantLevel(parent.player));
			int mana_restore = getManaRestoration();
			int spell_restore = getSpellReduction();
			int t0 = Math.min(armor_cost, mana_restore);
			armor_cost -= t0;
			mana_restore -= t0;
			spell_restore -= armor_cost;
			spell_restore = Math.max(-Math.max(100, getMaxSpellEndurance()) / 5, spell_restore);

			magic_mana = Mth.clamp(magic_mana + mana_restore, 0, getMaxMana());
			spell_load = Math.max(spell_load - spell_restore, 0);
			tick = 0;
			parent.abilityPoints.tickSeconds();
			int load = spell_load / Math.max(100, getMaxSpellEndurance());
			if (load == 1) {
				add(MobEffects.MOVEMENT_SLOWDOWN, 2);
				add(MobEffects.CONFUSION, 0);
				parent.player.hurt(LOAD, 1);
			}
			if (load == 2) {
				add(VanillaMagicRegistrate.HEAVY.get(), 4);
				add(MobEffects.BLINDNESS, 0);
				parent.player.hurt(LOAD, 4);
			}
			if (load == 3) {
				add(VanillaMagicRegistrate.HEAVY.get(), 4);
				add(MobEffects.BLINDNESS, 0);
				parent.player.hurt(LOAD, 16);
			}
			if (load >= 4) {
				add(VanillaMagicRegistrate.HEAVY.get(), 4);
				add(MobEffects.BLINDNESS, 0);
				parent.player.hurt(LOAD, 64);
			}
			if (!parent.world.isClientSide() && time_after_sync >= SYNC_PERIOD) {
				new CapToClient(CapToClient.Action.MAGIC_ABILITY, parent).toClientPlayer((ServerPlayer) parent.player);
			}
		}
		for (int i = 0; i < getMaxSpellSlot(); i++) {
			ItemStack stack = parent.player.getInventory().getItem(i);
			if (spell_activation.size() == i)
				spell_activation.add(new CompoundTag());
			CompoundTag tag = spell_activation.getCompound(i);
			tickSpell(stack, tag);
		}
	}

	private void add(MobEffect eff, int lv) {
		MobEffectInstance ins = new MobEffectInstance(eff, 40, lv);
		EffectAddUtil.addEffect(parent.player, ins, EffectAddUtil.AddReason.SELF, parent.player);
	}

	private void tickSpell(ItemStack stack, CompoundTag tag) {
		if (stack.getItem() instanceof MagicScroll) {
			String tag_spell = tag.getString("spell");
			Spell<?, ?> spell = MagicScroll.getSpell(stack);
			if (spell != null) {
				if (tag_spell.equals(spell.getID())) {
					int tick = tag.getInt("time");
					tag.putInt("time", tick + 1);
				} else {
					tag.putString("spell", spell.getID());
					tag.putInt("time", 0);
				}
				return;
			}
		}
		tag.putString("spell", "");
		tag.putInt("time", 0);
	}

	public int getMaxMana() {
		return magic_level * 100;
	}

	public int getManaRestoration() {
		return magic_level;
	}

	public int getMaxSpellSlot() {
		return spell_level;
	}

	public int getMaxSpellEndurance() {
		return spell_level * 100;
	}

	public int getSpellLoad() {
		return spell_load;
	}

	public int getSpellReduction() {
		return spell_level;
	}

	public boolean isArcaneTypeUnlocked(ArcaneType type) {
		return new NBTObj(arcane_type).getSub(type.getID()).tag.getInt("level") > 0;
	}

	public void unlockArcaneType(ArcaneType type, boolean force) {
		if (!isArcaneTypeUnlocked(type) && (force || parent.abilityPoints.levelArcane())) {
			new NBTObj(arcane_type).getSub(type.getID()).tag.putInt("level", 1);
		}
	}

	public double getSpellActivation(int id) {
		if (id < 0 || id >= getMaxSpellSlot())
			return 1;
		int time = spell_activation.getCompound(id).getInt("time");
		if (time >= ACTIVATION)
			return 0;
		return 1.0 * (ACTIVATION - time) / ACTIVATION;
	}

	public int getMana() {
		return magic_mana;
	}

}
