package dev.hikarishima.lightland.compat;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.DistExecutor;

public class AppleSkinCompatHandler {

	public static void handleCompat(GeneralCompatHandler.Stage stage) {
		if (stage == GeneralCompatHandler.Stage.OVERLAY) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> AppleSkinCompatHandler::modifyOverlay);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void modifyOverlay() {
		OverlayRegistry.enableOverlay(GeneralCompatHandler.getOverlay("AppleSkin Health Overlay").getOverlay(), false);
	}

}
