package dev.hikarishima.lightland.content.magic.item;

import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.common.capability.player.MagicAbility;
import dev.hikarishima.lightland.content.common.item.api.IGlowingTarget;
import dev.hikarishima.lightland.content.magic.spell.internal.Spell;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.util.EffectAddUtil;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicScroll extends Item implements IGlowingTarget {

	public final ScrollType type;

	public MagicScroll(ScrollType type, Properties props) {
		super(type.apply(props));
		this.type = type;
	}

	public static void initItemStack(Spell<?, ?> spell, ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTagElement("spell");
		tag.putString("spell", spell.getID());
		stack.getOrCreateTag().remove("CustomPotionEffects");
	}

	public static void initEffect(List<MobEffectInstance> list, ItemStack stack) {
		stack.getOrCreateTag().remove("CustomPotionEffects");
		stack.getOrCreateTagElement("spell").remove("spell");
		PotionUtils.setCustomEffects(stack, list);
	}

	public static void setTarget(TargetType type, ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTagElement("spell");
		tag.putString("target", type.name());
	}

	public static TargetType getTarget(ItemStack stack) {
		String type = stack.getOrCreateTagElement("spell").getString("target");
		try {
			return Enum.valueOf(TargetType.class, type);
		} catch (Exception e) {
			return TargetType.ALL;
		}
	}

	public static void setRadius(double radius, ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTagElement("spell");
		tag.putDouble("radius", radius);
	}

	public static double getRadius(ItemStack stack) {
		return Math.max(5, stack.getOrCreateTagElement("spell").getDouble("radius"));
	}

	@Nullable
	public static Spell<?, ?> getSpell(ItemStack stack) {
		String id = stack.getOrCreateTagElement("spell").getString("spell");
		if (id.length() == 0) return null;
		ResourceLocation rl = new ResourceLocation(id);
		return LightLandRegistry.SPELL.getValue(rl);
	}

	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		Spell<?, ?> spell = getSpell(stack);
		if (spell != null) list.add(new TranslatableComponent(spell.getDescriptionId()));
		else if (stack.getOrCreateTag().contains("CustomPotionEffects")) {
			list.add(getTarget(stack).text());
			list.add(LangData.IDS.POTION_RADIUS.get(getRadius(stack)));
			PotionUtils.addPotionTooltip(stack, list, 1);
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		Player pl = Proxy.getPlayer();
		if (pl == null) return false;
		MagicAbility ability = LLPlayerData.get(pl).magicAbility;
		int id = -1;
		for (int i = 0; i < 9; i++) {
			if (pl.getInventory().getItem(i) == stack) {
				id = i;
				break;
			}
		}
		if (id == -1) return false;
		if (id >= ability.getMaxSpellSlot()) return true;
		return ability.getSpellActivation(id) > 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		Player pl = Proxy.getPlayer();
		if (pl == null) return 0;
		MagicAbility ability = LLPlayerData.get(pl).magicAbility;
		int id = -1;
		for (int i = 0; i < ability.getMaxSpellSlot(); i++) {
			if (pl.getInventory().getItem(i) == stack) {
				id = i;
				break;
			}
		}
		if (id == -1) return 0;
		return (int) (13 - 13 * ability.getSpellActivation(id));

	}

	@Override
	public int getBarColor(ItemStack stack) {
		return getBarWidth(stack) == 13 ? 0xFFFFFF : 0xFF5555;
	}

	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int selected = player.getInventory().selected;
		LLPlayerData handler = LLPlayerData.get(player);
		if (handler.magicAbility.getMaxSpellSlot() <= selected) return InteractionResultHolder.fail(stack);
		if (player.getInventory().getItem(selected) != stack) return InteractionResultHolder.fail(stack);
		if (handler.magicAbility.getSpellActivation(selected) != 0) return InteractionResultHolder.fail(stack);
		List<MobEffectInstance> list = PotionUtils.getCustomEffects(stack);
		if (list.size() > 0) {
			if (!world.isClientSide()) {
				double radius = getRadius(stack);
				TargetType target = getTarget(stack);
				for (Entity e : world.getEntities(null, new AABB(player.blockPosition()).inflate(radius))) {
					if (!(e instanceof LivingEntity)) {
						continue;
					}
					if (e.distanceTo(player) > radius) {
						continue;
					}
					if (target == TargetType.ALLY && !e.isAlliedTo(player) || target == TargetType.ENEMY && e.isAlliedTo(player)) {
						continue;
					}
					for (MobEffectInstance ins : list) {
						EffectAddUtil.addEffect((LivingEntity) e, ins, EffectAddUtil.AddReason.PROF, player);
					}
				}
			}
			return InteractionResultHolder.success(stack);
		}
		Spell<?, ?> spell = getSpell(stack);
		if (!world.isClientSide()) {
			if (spell == null || !spell.attempt(Spell.Type.SCROLL, world, (ServerPlayer) player))
				return InteractionResultHolder.fail(stack);
			player.getCooldowns().addCooldown(this, 10);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public int getDistance(ItemStack stack) {
		Spell<?, ?> spell = getSpell(stack);
		if (spell == null) return 0;
		return spell.getDistance(Proxy.getClientPlayer());
	}

	public enum ScrollType {
		CARD(64, LightlandItems.SPELL_CARD),
		PARCHMENT(16, LightlandItems.SPELL_PARCHMENT),
		SCROLL(4, LightlandItems.SPELL_SCROLL);

		public final int stack;
		private final Supplier<MagicScroll> item;

		ScrollType(int stack, Supplier<MagicScroll> item) {
			this.stack = stack;
			this.item = item;
		}

		public Properties apply(Properties props) {
			props.stacksTo(stack);
			return props;
		}

		public MagicScroll toItem() {
			return item.get();
		}
	}

	public enum TargetType {
		ALLY, ENEMY, ALL;

		public Component text() {
			return LangData.IDS.valueOf("TARGET_" + name()).get();
		}
	}

}
