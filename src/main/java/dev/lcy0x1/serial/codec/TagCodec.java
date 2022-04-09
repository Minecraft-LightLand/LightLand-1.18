package dev.lcy0x1.serial.codec;

import dev.lcy0x1.serial.ExceptionHandler;
import dev.lcy0x1.serial.SerialClass;
import dev.lcy0x1.serial.handler.Handlers;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Capable of handing primitive types, array, BlockPos, ItemStack, inheritance
 * <br>
 * Not capable of handing collections
 */
public class TagCodec {

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
		if (Handlers.NBT_MAP.containsKey(cls.getAsClass()))
			return Handlers.NBT_MAP.get(cls.getAsClass()).fromTag(tag);
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
						Handlers.NBT_MAP.get(key.getAsClass()).fromTag(StringTag.valueOf(str));
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
		if (Handlers.NBT_MAP.containsKey(cls.getAsClass()))
			return Handlers.NBT_MAP.get(cls.getAsClass()).toTag(obj);
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

}