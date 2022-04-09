package dev.lcy0x1.serial.handler;

import net.minecraft.nbt.StringTag;

import java.util.function.Function;

public class StringClassHandler<T> extends ClassHandler<StringTag, T> {

	public StringClassHandler(Class<T> cls, Function<String, T> fj, Function<T, String> tp) {
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
				}, (p, t) -> p.writeUtf(t == null ? "" : tp.apply(t)),
				t -> fj.apply(t.toString()),
				e -> StringTag.valueOf(tp.apply(e)));
	}

}
