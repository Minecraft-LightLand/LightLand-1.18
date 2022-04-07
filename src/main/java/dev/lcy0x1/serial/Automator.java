package dev.lcy0x1.serial;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SerializableUUID;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Capable of handing primitive types, array, BlockPos, ItemStack, inheritance
 * <br>
 * Not capable of handing collections
 */
public class Automator {

	private static final Map<Class<?>, ClassHandler<?, ?>> MAP = new HashMap<>();

	static {
		new ClassHandler<>(Long.class, LongTag::getAsLong, LongTag::valueOf, long.class);
		new ClassHandler<>(Integer.class, IntTag::getAsInt, IntTag::valueOf, int.class);
		new ClassHandler<>(Short.class, ShortTag::getAsShort, ShortTag::valueOf, short.class);
		new ClassHandler<>(Byte.class, ByteTag::getAsByte, ByteTag::valueOf, byte.class);
		new ClassHandler<ByteTag, Boolean>(Boolean.class, tag -> tag.getAsByte() != 0, ByteTag::valueOf, boolean.class);
		new ClassHandler<>(Float.class, FloatTag::getAsFloat, FloatTag::valueOf, float.class);
		new ClassHandler<>(Double.class, DoubleTag::getAsDouble, DoubleTag::valueOf, double.class);
		new ClassHandler<>(long[].class, LongArrayTag::getAsLongArray, LongArrayTag::new);
		new ClassHandler<>(int[].class, IntArrayTag::getAsIntArray, IntArrayTag::new);
		new ClassHandler<>(byte[].class, ByteArrayTag::getAsByteArray, ByteArrayTag::new);
		new ClassHandler<StringTag, String>(String.class, Tag::getAsString, StringTag::valueOf);
		new ClassHandler<>(ItemStack.class, ItemStack::of, is -> is.save(new CompoundTag()));
		new ClassHandler<CompoundTag, BlockPos>(BlockPos.class,
				tag -> new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")),
				obj -> {
					CompoundTag tag = new CompoundTag();
					tag.putInt("x", obj.getX());
					tag.putInt("y", obj.getY());
					tag.putInt("z", obj.getZ());
					return tag;
				});
		new ClassHandler<CompoundTag, Vec3>(Vec3.class,
				tag -> new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z")),
				obj -> {
					CompoundTag tag = new CompoundTag();
					tag.putDouble("x", obj.x());
					tag.putDouble("y", obj.y());
					tag.putDouble("z", obj.z());
					return tag;
				});
		new ClassHandler<IntArrayTag, UUID>(UUID.class,
				tag -> SerializableUUID.uuidFromIntArray(tag.getAsIntArray()),
				id -> new IntArrayTag(SerializableUUID.uuidToIntArray(id))
		);
		new ClassHandler<CompoundTag, CompoundTag>(CompoundTag.class, e -> e, e -> e);
		new ClassHandler<ListTag, ListTag>(ListTag.class, e -> e, e -> e);
		new ClassHandler<StringTag, ResourceLocation>(ResourceLocation.class, tag -> new ResourceLocation(tag.getAsString()), rl -> StringTag.valueOf(rl.toString()));
		new RegistryClassHandler<>(Block.class, () -> ForgeRegistries.BLOCKS);
		new RegistryClassHandler<>(Item.class, () -> ForgeRegistries.ITEMS);
		new RegistryClassHandler<>(Enchantment.class, () -> ForgeRegistries.ENCHANTMENTS);
		new RegistryClassHandler<>(MobEffect.class, () -> ForgeRegistries.MOB_EFFECTS);
	}

	public static Object fromTag(CompoundTag tag, Class<?> cls, Object obj, Predicate<SerialClass.SerialField> pred)
			throws Exception {
		if (tag.contains("_class"))
			cls = Class.forName(tag.getString("_class"));
		if (obj == null)
			obj = cls.getConstructor().newInstance();
		Class<?> mcls = cls;
		while (cls.getAnnotation(SerialClass.class) != null) {
			for (Field f : cls.getDeclaredFields()) {
				SerialClass.SerialField sf = f.getAnnotation(SerialClass.SerialField.class);
				if (sf == null || !pred.test(sf))
					continue;
				Tag itag = tag.get(f.getName());
				f.setAccessible(true);
				if (itag != null)
					f.set(obj, fromTagRaw(itag, TypeInfo.of(f), f.get(obj), pred));
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
				m0.invoke(obj);
				break;
			}
			cls = cls.getSuperclass();
		}
		return obj;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Object fromTagRaw(Tag tag, TypeInfo cls, Object def, Predicate<SerialClass.SerialField> pred) throws Exception {
		if (tag == null)
			if (cls.getAsClass() == ItemStack.class)
				return ItemStack.EMPTY;
			else
				return null;
		if (MAP.containsKey(cls.getAsClass()))
			return MAP.get(cls.getAsClass()).fromTag.apply(tag);
		if (cls.isArray()) {
			ListTag list = (ListTag) tag;
			int n = list.size();
			TypeInfo com = cls.getComponentType();
			Object ans = Array.newInstance(com.getAsClass(), n);
			for (int i = 0; i < n; i++) {
				Array.set(ans, i, fromTagRaw(list.get(i), com, null, pred));
			}
			return ans;
		}
		if (List.class.isAssignableFrom(cls.getAsClass())) {
			ListTag list = (ListTag) tag;
			TypeInfo com = cls.getGenericType(0);
			if (def == null)
				def = cls.newInstance();
			List ans = (List<?>) def;
			ans.clear();
			for (Tag iTag : list) {
				ans.add(fromTagRaw(iTag, com, null, pred));
			}
			return ans;
		}
		if (Map.class.isAssignableFrom(cls.getAsClass())) {
			if (def == null)
				def = cls.newInstance();
			TypeInfo key = cls.getGenericType(0);
			TypeInfo val = cls.getGenericType(1);
			CompoundTag ctag = (CompoundTag) tag;
			Map map = (Map) def;
			map.clear();
			for (String str : ctag.getAllKeys()) {
				Object mkey = key.getAsClass() == String.class ? str :
						MAP.get(key.getAsClass()).fromTag.apply(StringTag.valueOf(str));
				map.put(mkey, fromTagRaw(ctag.get(str), val, null, pred));
			}
			return map;
		}
		if (cls.getAsClass().isEnum()) {
			return Enum.valueOf((Class) cls.getAsClass(), tag.getAsString());
		}
		if (cls.getAsClass().getAnnotation(SerialClass.class) != null)
			return fromTag((CompoundTag) tag, cls.getAsClass(), def, pred);
		throw new Exception("unsupported class " + cls);
	}

	public static CompoundTag toTag(CompoundTag tag, Class<?> cls, Object obj, Predicate<SerialClass.SerialField> pred)
			throws Exception {
		if (obj == null)
			return tag;
		if (obj.getClass() != cls) {
			tag.putString("_class", obj.getClass().getName());
			cls = obj.getClass();
		}
		while (cls.getAnnotation(SerialClass.class) != null) {
			for (Field f : cls.getDeclaredFields()) {
				SerialClass.SerialField sf = f.getAnnotation(SerialClass.SerialField.class);
				if (sf == null || !pred.test(sf))
					continue;
				f.setAccessible(true);
				if (f.get(obj) != null)
					tag.put(f.getName(), toTagRaw(TypeInfo.of(f), f.get(obj), pred));
			}
			cls = cls.getSuperclass();
		}
		return tag;
	}

	public static CompoundTag toTag(CompoundTag tag, Object obj) {
		return ExceptionHandler.get(() -> toTag(tag, obj.getClass(), obj, f -> true));
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromTag(CompoundTag tag, Class<?> cls) {
		return (T) ExceptionHandler.get(() -> fromTag(tag, cls, null, f -> true));
	}

	public static Tag toTagRaw(TypeInfo cls, Object obj, Predicate<SerialClass.SerialField> pred) throws Exception {
		if (MAP.containsKey(cls.getAsClass()))
			return MAP.get(cls.getAsClass()).toTag.apply(obj);
		if (cls.isArray()) {
			ListTag list = new ListTag();
			int n = Array.getLength(obj);
			TypeInfo com = cls.getComponentType();
			for (int i = 0; i < n; i++) {
				list.add(toTagRaw(com, Array.get(obj, i), pred));
			}
			return list;
		}
		if (List.class.isAssignableFrom(cls.getAsClass())) {
			ListTag list = new ListTag();
			int n = ((List<?>) obj).size();
			TypeInfo com = cls.getGenericType(0);
			for (int i = 0; i < n; i++) {
				list.add(toTagRaw(com, ((List<?>) obj).get(i), pred));
			}
			return list;
		}
		if (Map.class.isAssignableFrom(cls.getAsClass())) {
			TypeInfo key = cls.getGenericType(0);
			TypeInfo val = cls.getGenericType(1);
			CompoundTag ctag = new CompoundTag();
			Map<?, ?> map = (Map<?, ?>) obj;
			for (Object str : map.keySet()) {
				String mkey = key.getAsClass() == String.class ? (String) str :
						toTagRaw(key, str, pred).getAsString();
				ctag.put(mkey, toTagRaw(val, map.get(str), pred));
			}
			return ctag;
		}
		if (cls.getAsClass().isEnum())
			return StringTag.valueOf(((Enum<?>) obj).name());
		if (cls.getAsClass().getAnnotation(SerialClass.class) != null)
			return toTag(new CompoundTag(), cls.getAsClass(), obj, pred);
		throw new Exception("unsupported class " + cls);
	}

	public static class ClassHandler<R extends Tag, T> {

		private final Function<Tag, ?> fromTag;
		private final Function<Object, Tag> toTag;

		@SuppressWarnings("unchecked")
		public ClassHandler(Class<T> cls, Function<R, T> ft, Function<T, Tag> tt, Class<?>... alt) {
			fromTag = (Function<Tag, ?>) ft;
			toTag = (Function<Object, Tag>) tt;
			MAP.put(cls, this);
			for (Class<?> c : alt)
				MAP.put(c, this);
		}

	}

	public static class RegistryClassHandler<T extends IForgeRegistryEntry<T>> extends ClassHandler<StringTag, T> {

		public RegistryClassHandler(Class<T> cls, Supplier<IForgeRegistry<T>> sup) {
			super(cls, s -> s.getAsString().length() == 0 ? null : sup.get().getValue(new ResourceLocation(s.getAsString())),
					t -> t == null ? StringTag.valueOf("") : StringTag.valueOf(t.getRegistryName().toString()));
		}
	}

}