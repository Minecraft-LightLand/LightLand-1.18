package dev.xkmc.lightland.compat.create;

import dev.xkmc.lightland.compat.GeneralCompatHandler;

public class CreateCompatHandler {

	public static void handleCompat(GeneralCompatHandler.Stage stage) {
		if (stage == GeneralCompatHandler.Stage.INIT) {
			CreateItems.register();
		}
	}

}
