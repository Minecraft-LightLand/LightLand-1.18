package test;

import dev.hikarishima.lightland.content.common.capability.player.SkillCap;
import dev.xkmc.l2library.serial.codec.TypeInfo;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Test {

	public static final Map<String, List<SkillCap.Cont<?, ?, ?>[][]>> LIST = null;

	public static void main(String[] args) {
		try {
			Field f = Test.class.getDeclaredField("LIST");
			TypeInfo info = TypeInfo.of(f);
			LogManager.getLogger().info(info.getGenericType(1).getGenericType(0).getAsClass());
		} catch (Exception e) {
			LogManager.getLogger().throwing(e);
		}
	}

}
