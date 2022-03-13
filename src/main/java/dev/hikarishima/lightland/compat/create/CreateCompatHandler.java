package dev.hikarishima.lightland.compat.create;

import dev.hikarishima.lightland.compat.GeneralCompatHandler;

public class CreateCompatHandler {

	public static void handleCompat(GeneralCompatHandler.Stage stage) {
		if (stage == GeneralCompatHandler.Stage.INIT) {
			CreateItems.register();
		}
	}

}
