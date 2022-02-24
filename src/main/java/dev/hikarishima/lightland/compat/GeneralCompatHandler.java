package dev.hikarishima.lightland.compat;

import dev.lcy0x1.util.ExceptionHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.ModList;

public class GeneralCompatHandler {

    public enum Stage {
        OVERLAY
    }

    public static void handle(Stage stage) {
        if (ModList.get().isLoaded("appleskin")) {
            ExceptionHandler.ignore(() -> AppleSkinCompatHandler.handleCompat(stage));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static OverlayRegistry.OverlayEntry getOverlay(String str) {
        return OverlayRegistry.orderedEntries().stream().filter(e -> e.getDisplayName().equals(str)).findFirst().orElse(null);
    }

}
