package dev.lcy0x1.util;

import com.google.gson.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Capable of serializing primitive type, Arrays, Item, ItemStacl, Ingredient
 * <br>
 * Not capable of handling inheritance, collections
 */
public class Serializer {

	public static final Map<Class<?>, ClassHandler<?>> MAP = new HashMap<>();
	public static final Gson gson = new Gson();

	static {
		new ClassHandler<>(long.class, JsonElement::getAsLong, FriendlyByteBuf::readLong, FriendlyByteBuf::writeLong, Long.class);
		new ClassHandler<>(int.class, JsonElement::getAsInt, FriendlyByteBuf::readInt, FriendlyByteBuf::writeInt, Integer.class);
		new ClassHandler<Short>(short.class, JsonElement::getAsShort, FriendlyByteBuf::readShort, FriendlyByteBuf::writeShort, Short.class);
		new ClassHandler<Byte>(byte.class, JsonElement::getAsByte, FriendlyByteBuf::readByte, FriendlyByteBuf::writeByte, Byte.class);
		new ClassHandler<Character>(char.class, JsonElement::getAsCharacter, FriendlyByteBuf::readChar, FriendlyByteBuf::writeChar, Character.class);
		new ClassHandler<>(boolean.class, JsonElement::getAsBoolean, FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean, Boolean.class);
		new ClassHandler<>(double.class, JsonElement::getAsDouble, FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble, Double.class);
		new ClassHandler<>(float.class, JsonElement::getAsFloat, FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat, Float.class);
		new ClassHandler<>(String.class, JsonElement::getAsString, FriendlyByteBuf::readUtf, FriendlyByteBuf::writeUtf);
		new ClassHandler<>(ItemStack.class, (e) -> ShapedRecipe.itemStackFromJson(e.getAsJsonObject()), FriendlyByteBuf::readItem, (p, is) -> p.writeItemStack(is, false));
		new ClassHandler<>(Ingredient.class, Ingredient::fromJson, Ingredient::fromNetwork, (p, o) -> o.toNetwork(p));
		new ClassHandler<>(CompoundTag.class, (e) -> new CompoundTag(), FriendlyByteBuf::readAnySizeNbt, FriendlyByteBuf::writeNbt);

		new StringClassHandler<>(ResourceLocation.class, ResourceLocation::new, ResourceLocation::toString);
		new ClassHandler<>(UUID.class, e -> UUID.fromString(e.getAsString()), p -> new UUID(p.readLong(), p.readLong()),
				(p, e) -> {
					p.writeLong(e.getMostSignificantBits());
					p.writeLong(e.getLeastSignificantBits());
				});

		new RLClassHandler<>(Item.class, () -> ForgeRegistries.ITEMS);
		new RLClassHandler<>(Block.class, () -> ForgeRegistries.BLOCKS);
		new RLClassHandler<>(Potion.class, () -> ForgeRegistries.POTIONS);
		new RLClassHandler<>(Enchantment.class, () -> ForgeRegistries.ENCHANTMENTS);
		new RLClassHandler<>(MobEffect.class, () -> ForgeRegistries.MOB_EFFECTS);
	}

	@SuppressWarnings("unchecked")
	public static <T> T from(JsonElement obj, Class<T> cls, T ans) {
		return ExceptionHandler.get(() -> (T) fromRaw(obj, TypeInfo.of(cls), ans));
	}

	@SuppressWarnings("unchecked")
	public static <T> T from(FriendlyByteBuf buf, Class<T> cls, T ans) {
		return ExceptionHandler.get(() -> (T) fromRaw(buf, TypeInfo.of(cls), ans));
	}

	public static Object fromImpl(JsonObject obj, Class<?> cls, Object ans) throws Exception {
		if (obj.has("_class")) {
			cls = Class.forName(obj.get("_class").getAsString());
		}
		if (cls.getAnnotation(SerialClass.class) == null)
			throw new Exception("invalid class " + cls + " with object " + obj);
		if (ans == null)
			ans = cls.getConstructor().newInstance();
		Class<?> mcls = cls;
		while (cls.getAnnotation(SerialClass.class) != null) {
			for (Field f : cls.getDeclaredFields()) {
				if (f.getAnnotation(SerialClass.SerialField.class) != null) {
					if (obj.has(f.getName())) {
						f.setAccessible(true);
						f.set(ans, fromRaw(obj.get(f.getName()), TypeInfo.of(f), null));
					}
				}
			}
			cls = cls.getSuperclass();
		}
		cls = mcls;
		while (cls.getAnnotation(SerialClass.class) != null) {
			Method m0 = null;
			for (Method m : cls.getDeclaredMethods()) {
				if (m.getAnnotation(SerialClass.OnInject.class) != null) {
					m0 = m;
				}
			}
			if (m0 != null) {
				m0.invoke(ans);
				break;
			}
			cls = cls.getSuperclass();
		}
		return ans;
	}

	public static Object fromImpl(FriendlyByteBuf buf, Class<?> cls, Object ans) throws Exception {
		if (cls.getAnnotation(SerialClass.class) == null)
			throw new Exception("cannot deserialize " + cls);
		if (ans == null)
			ans = cls.getConstructor().newInstance();
		Class<?> mcls = cls;
		while (cls.getAnnotation(SerialClass.class) != null) {
			TreeMap<String, Field> map = new TreeMap<>();
			for (Field f : cls.getDeclaredFields()) {
				if (f.getAnnotation(SerialClass.SerialField.class) != null) {
					map.put(f.getName(), f);
				}
			}
			for (Field f : map.values()) {
				f.setAccessible(true);
				f.set(ans, fromRaw(buf, TypeInfo.of(f), null));
			}
			cls = cls.getSuperclass();
		}
		cls = mcls;
		while (cls.getAnnotation(SerialClass.class) != null) {
			Method m0 = null;
			for (Method m : cls.getDeclaredMethods()) {
				if (m.getAnnotation(SerialClass.OnInject.class) != null) {
					m0 = m;
				}
			}
			if (m0 != null) {
				m0.invoke(ans);
				break;
			}
			cls = cls.getSuperclass();
		}
		return ans;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Object fromRaw(JsonElement e, TypeInfo cls, Object ans) throws Exception {
		if (cls.isArray()) {
			JsonArray arr = e.getAsJsonArray();
			TypeInfo com = cls.getComponentType();
			int n = arr.size();
			if (ans == null) ans = Array.newInstance(com.getAsClass(), n);
			for (int i = 0; i < n; i++) {
				Array.set(ans, i, fromRaw(arr.get(i), com, null));
			}
			return ans;
		}
		if (List.class.isAssignableFrom(cls.getAsClass())) {
			JsonArray arr = e.getAsJsonArray();
			TypeInfo com = cls.getGenericType(0);
			int n = arr.size();
			if (ans == null) ans = cls.newInstance();
			List list = (List) ans;
			for (int i = 0; i < n; i++) {
				list.add(fromRaw(arr.get(i), com, null));
			}
			return ans;
		}
		if (Map.class.isAssignableFrom(cls.getAsClass())) {
			if (ans == null)
				ans = cls.newInstance();
			TypeInfo ckey = cls.getGenericType(0);
			TypeInfo cval = cls.getGenericType(1);
			if (e.isJsonArray()) {
				for (JsonElement je : e.getAsJsonArray()) {
					JsonObject jeo = je.getAsJsonObject();
					((Map) ans).put(fromRaw(jeo.get("_key"), ckey, null), fromRaw(jeo.get("_val"), cval, null));
				}
				return ans;
			}
			if (e.isJsonObject()) {
				for (Map.Entry<String, JsonElement> ent : e.getAsJsonObject().entrySet()) {
					Object key = ckey.getAsClass() == String.class ? ent.getKey() :
							MAP.get(ckey.getAsClass()).fromJson.apply(new JsonPrimitive(ent.getKey()));
					((Map) ans).put(key, fromRaw(ent.getValue(), cval, null));
				}
				return ans;
			}
		}
		if (cls.getAsClass().isEnum())
			return Enum.valueOf((Class) cls.getAsClass(), e.getAsString());
		if (cls.getAsClass() == JsonElement.class)
			return e;
		if (MAP.containsKey(cls.getAsClass()))
			return MAP.get(cls.getAsClass()).fromJson.apply(e);
		return fromImpl(e.getAsJsonObject(), cls.getAsClass(), ans);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Object fromRaw(FriendlyByteBuf buf, TypeInfo cls, Object ans) throws Exception {
		byte type = buf.readByte();
		if (type == 0)
			return null;
		else if (type == 2) {
			cls = TypeInfo.of(Class.forName(buf.readUtf()));
		}
		if (cls.isArray()) {
			int n = buf.readInt();
			TypeInfo com = cls.getComponentType();
			if (ans == null)
				ans = Array.newInstance(com.getAsClass(), n);
			for (int i = 0; i < n; i++) {
				Array.set(ans, i, fromRaw(buf, com, null));
			}
			return ans;
		}
		if (List.class.isAssignableFrom(cls.getAsClass())) {
			int n = buf.readInt();
			TypeInfo com = cls.getGenericType(0);
			if (ans == null) ans = cls.newInstance();
			List list = (List) ans;
			for (int i = 0; i < n; i++) {
				list.add(fromRaw(buf, com, null));
			}
			return ans;
		}
		if (Map.class.isAssignableFrom(cls.getAsClass())) {
			if (ans == null)
				ans = cls.newInstance();
			int n = buf.readInt();
			TypeInfo ckey = cls.getGenericType(0);
			TypeInfo cval = cls.getGenericType(1);
			for (int i = 0; i < n; i++) {
				Object key = fromRaw(buf, ckey, null);
				Object val = fromRaw(buf, cval, null);
				((Map) ans).put(key, val);
			}
			return ans;
		}
		if (cls.getAsClass().isEnum())
			return Enum.valueOf((Class) cls.getAsClass(), buf.readUtf());
		if (MAP.containsKey(cls.getAsClass()))
			return MAP.get(cls.getAsClass()).fromPacket.apply(buf);
		return fromImpl(buf, cls.getAsClass(), ans);
	}

	public static <T> void to(FriendlyByteBuf buf, T obj) {
		ExceptionHandler.run(() -> toRaw(buf, TypeInfo.of(obj.getClass()), obj));
	}

	public static void toImpl(FriendlyByteBuf buf, Class<?> cls, Object obj) throws Exception {
		if (cls.getAnnotation(SerialClass.class) == null)
			throw new Exception("cannot serialize " + cls);
		while (cls.getAnnotation(SerialClass.class) != null) {
			TreeMap<String, Field> map = new TreeMap<>();
			for (Field f : cls.getDeclaredFields()) {
				if (f.getAnnotation(SerialClass.SerialField.class) != null) {
					map.put(f.getName(), f);
				}
			}
			for (Field f : map.values()) {
				f.setAccessible(true);
				toRaw(buf, TypeInfo.of(f), f.get(obj));
			}
			cls = cls.getSuperclass();
		}
	}

	public static void toRaw(FriendlyByteBuf buf, TypeInfo cls, Object obj) throws Exception {
		if (obj == null) {
			buf.writeByte(0);
			return;
		} else if (obj.getClass() != cls.getAsClass() && obj.getClass().isAnnotationPresent(SerialClass.class)) {
			buf.writeByte(2);
			cls = TypeInfo.of(obj.getClass());
			buf.writeUtf(cls.getAsClass().getName());
		} else {
			buf.writeByte(1);
		}
		if (cls.isArray()) {
			int n = Array.getLength(obj);
			buf.writeInt(n);
			TypeInfo com = cls.getComponentType();
			for (int i = 0; i < n; i++) {
				toRaw(buf, com, Array.get(obj, i));
			}
		} else if (List.class.isAssignableFrom(cls.getAsClass())) {
			List<?> list = (List<?>) obj;
			buf.writeInt(list.size());
			TypeInfo com = cls.getGenericType(0);
			for (Object o : list) {
				toRaw(buf, com, o);
			}
		} else if (Map.class.isAssignableFrom(cls.getAsClass())) {
			Map<?, ?> map = (Map<?, ?>) obj;
			buf.writeInt(map.size());
			TypeInfo ckey = cls.getGenericType(0);
			TypeInfo cval = cls.getGenericType(1);
			for (Map.Entry<?, ?> ent : map.entrySet()) {
				toRaw(buf, ckey, ent.getKey());
				toRaw(buf, cval, ent.getValue());
			}
		} else if (cls.getAsClass().isEnum())
			buf.writeUtf(((Enum<?>) obj).name());
		else if (MAP.containsKey(cls.getAsClass()))
			MAP.get(cls.getAsClass()).toPacket.accept(buf, obj);
		else
			toImpl(buf, cls.getAsClass(), obj);

	}

	public static class ClassHandler<T> {

		public Function<JsonElement, ?> fromJson;
		public Function<FriendlyByteBuf, ?> fromPacket;
		public BiConsumer<FriendlyByteBuf, Object> toPacket;

		@SuppressWarnings("unchecked")
		public ClassHandler(Class<?> cls, Function<JsonElement, T> fj, Function<FriendlyByteBuf, T> fp,
							BiConsumer<FriendlyByteBuf, T> tp, Class<?>... others) {
			this.fromJson = fj;
			this.fromPacket = fp;
			this.toPacket = (BiConsumer<FriendlyByteBuf, Object>) tp;
			MAP.put(cls, this);
			for (Class<?> c : others)
				MAP.put(c, this);
		}

	}

	public static class StringClassHandler<T> extends ClassHandler<T> {

		public StringClassHandler(Class<?> cls, Function<String, T> fj, Function<T, String> tp) {
			super(cls, e -> {
				if (e.isJsonNull())
					return null;
				String str = e.getAsString();
				if (str.length() == 0)
					return null;
				return fj.apply(str);
			}, p -> {
				String str = p.readUtf();
				if (str.length() == 0)
					return null;
				return fj.apply(str);
			}, (p, t) -> p.writeUtf(t == null ? "" : tp.apply(t)));
		}

	}

	public static class RLClassHandler<T extends IForgeRegistryEntry<T>> extends ClassHandler<T> {

		public RLClassHandler(Class<?> cls, Supplier<IForgeRegistry<T>> r) {
			super(cls, e -> e.isJsonNull() ? null : r.get().getValue(new ResourceLocation(e.getAsString())),
					p -> {
						String str = p.readUtf();
						if (str.length() == 0)
							return null;
						return r.get().getValue(new ResourceLocation(str));
					},
					(p, t) -> p.writeUtf(t == null ? "" : t.getRegistryName().toString()));
		}

	}

}