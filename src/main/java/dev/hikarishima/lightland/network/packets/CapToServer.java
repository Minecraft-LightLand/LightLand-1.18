package dev.hikarishima.lightland.network.packets;

import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.common.capability.player.AbilityPoints;
import dev.hikarishima.lightland.content.common.capability.player.CapProxy;
import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.common.capability.player.MagicHolder;
import dev.hikarishima.lightland.content.magic.item.MagicWand;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.hikarishima.lightland.content.magic.products.MagicProduct;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.content.profession.Profession;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.network.SerialPacketBase;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

@SerialClass
public class CapToServer extends SerialPacketBase {

	public enum Action {
		HEX((handler, tag) -> {
			MagicHolder holder = handler.magicHolder;
			String str = tag.getString("product");
			MagicProduct<?, ?> prod = holder.getProduct(holder.getRecipe(new ResourceLocation(str)));
			CompoundTag ctag = prod.tag.tag;
			Set<String> set = new HashSet<>(ctag.getAllKeys());
			for (String key : set) {
				ctag.remove(key);
			}
			CompoundTag dtag = tag.getCompound("data");
			for (String key : dtag.getAllKeys()) {
				ctag.put(key, Objects.requireNonNull(dtag.get(key)));
			}
			holder.checkUnlocks();
		}),
		DEBUG((handler, tag) -> {
			LogManager.getLogger().info("server: " + tag.getCompound("server"));
			LogManager.getLogger().info("client: " + tag.getCompound("client"));
		}),
		LEVEL((handler, tag) -> {
			AbilityPoints.LevelType.values()[tag.getInt("type")].doLevelUp(handler);
		}),
		PROFESSION((handler, tag) -> {
			Profession prof = LightLandRegistry.PROFESSION.getValue(new ResourceLocation(tag.getString("id")));
			if (prof == null)
				return;
			handler.abilityPoints.setProfession(prof);
		}),
		ELEMENTAL((handler, tag) -> {
			MagicElement elem = LightLandRegistry.ELEMENT.getValue(new ResourceLocation(tag.getString("id")));
			if (elem == null)
				return;
			if (handler.abilityPoints.canLevelElement() && handler.magicHolder.addElementalMastery(elem))
				handler.abilityPoints.levelElement();
		}),
		ARCANE((handler, tag) -> {
			ArcaneType type = LightLandRegistry.ARCANE_TYPE.getValue(new ResourceLocation(tag.getString("id")));
			if (type == null)
				return;
			if (handler.abilityPoints.canLevelArcane() && !handler.magicAbility.isArcaneTypeUnlocked(type)) {
				handler.magicAbility.unlockArcaneType(type, false);
			}
		}),
		WAND((handler, tag) -> {
			Player player = handler.player;
			IMagicRecipe<?> recipe = handler.magicHolder.getRecipe(new ResourceLocation(tag.getString("recipe")));
			if (recipe == null)
				return;
			ItemStack stack = player.getMainHandItem();
			if (!(stack.getItem() instanceof MagicWand)) {
				stack = player.getOffhandItem();
			}
			if (!(stack.getItem() instanceof MagicWand))
				return;
			MagicWand wand = (MagicWand) stack.getItem();
			wand.setMagic(recipe, stack);
		});

		private final BiConsumer<LLPlayerData, CompoundTag> cons;

		Action(BiConsumer<LLPlayerData, CompoundTag> cons) {
			this.cons = cons;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendHexUpdate(MagicProduct<?, ?> prod) {
		CompoundTag tag = new CompoundTag();
		tag.putString("product", prod.recipe.id.toString());
		tag.put("data", prod.tag.tag);
		new CapToServer(Action.HEX, tag).toServer();
		CapProxy.getHandler().magicHolder.checkUnlocks();
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendDebugInfo(CompoundTag s, CompoundTag c) {
		CompoundTag tag = new CompoundTag();
		tag.put("server", s);
		tag.put("client", c);
		new CapToServer(Action.DEBUG, tag).toServer();
	}

	@OnlyIn(Dist.CLIENT)
	public static void levelUpAbility(AbilityPoints.LevelType type) {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", type.ordinal());
		new CapToServer(Action.LEVEL, tag).toServer();
	}

	@OnlyIn(Dist.CLIENT)
	public static void setProfession(Profession prof) {
		CompoundTag tag = new CompoundTag();
		tag.putString("id", prof.getID());
		new CapToServer(Action.PROFESSION, tag).toServer();
	}

	@OnlyIn(Dist.CLIENT)
	public static void unlockArcaneType(ArcaneType type) {
		CompoundTag tag = new CompoundTag();
		tag.putString("id", type.getID());
		new CapToServer(Action.ARCANE, tag).toServer();
	}

	@OnlyIn(Dist.CLIENT)
	public static void addElemMastery(MagicElement elem) {
		CompoundTag tag = new CompoundTag();
		tag.putString("id", elem.getID());
		new CapToServer(Action.ELEMENTAL, tag).toServer();
	}

	@OnlyIn(Dist.CLIENT)
	public static void activateWand(IMagicRecipe<?> recipe) {
		CompoundTag tag = new CompoundTag();
		tag.putString("recipe", recipe.id.toString());
		Action.WAND.cons.accept(CapProxy.getHandler(), tag);
		new CapToServer(Action.WAND, tag).toServer();
	}

	@SerialClass.SerialField
	public Action action;
	@SerialClass.SerialField
	public CompoundTag tag;

	@Deprecated
	public CapToServer() {

	}

	private CapToServer(Action act, CompoundTag tag) {
		this.action = act;
		this.tag = tag;
	}

	public void handle(NetworkEvent.Context ctx) {
		ServerPlayer player = ctx.getSender();
		if (player == null || !player.isAlive())
			return;
		LLPlayerData handler = LLPlayerData.get(player);
		action.cons.accept(handler, tag);
	}

}
