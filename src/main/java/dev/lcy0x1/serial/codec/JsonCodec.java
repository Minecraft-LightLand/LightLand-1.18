package dev.lcy0x1.serial.codec;

import com.google.gson.*;
import dev.lcy0x1.serial.ExceptionHandler;
import dev.lcy0x1.serial.SerialClass;
import dev.lcy0x1.serial.handler.Handlers;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Capable of serializing primitive type, Arrays, Item, ItemStacl, Ingredient
 * <br>
 * Not capable of handling inheritance, collections
 */
public class JsonCodec {

	private static final Gson GSON = new Gson();

	@SuppressWarnings("unchecked")
	public static <T> T from(JsonElement obj, Class<T> cls, T ans) {
		return ExceptionHandler.get(() -> (T) fromRaw(obj, TypeInfo.of(cls), ans));
	}

	private static Object fromImpl(JsonObject obj, Class<?> cls, Object ans) throws Exception {
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

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Object fromRaw(JsonElement e, TypeInfo cls, Object ans) throws Exception {
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
		if (ans instanceof AliasCollection<?> alias) {
			JsonArray arr = e.getAsJsonArray();
			TypeInfo com = TypeInfo.of(alias.getElemClass());
			int n = arr.size();
			for (int i = 0; i < n; i++) {
				alias.setRaw(n, i, fromRaw(arr.get(i), com, null));
			}
			return alias;
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
							Handlers.JSON_MAP.get(ckey.getAsClass()).fromJson(new JsonPrimitive(ent.getKey()));
					((Map) ans).put(key, fromRaw(ent.getValue(), cval, null));
				}
				return ans;
			}
		}
		if (cls.getAsClass().isEnum())
			return Enum.valueOf((Class) cls.getAsClass(), e.getAsString());
		if (cls.getAsClass() == JsonElement.class)
			return e;
		if (Handlers.JSON_MAP.containsKey(cls.getAsClass()))
			return Handlers.JSON_MAP.get(cls.getAsClass()).fromJson(e);
		return fromImpl(e.getAsJsonObject(), cls.getAsClass(), ans);
	}

}