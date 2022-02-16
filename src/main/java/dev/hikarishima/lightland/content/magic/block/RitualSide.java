package dev.hikarishima.lightland.content.magic.block;

import com.hikarishima.lightland.magic.registry.MagicContainerRegistry;
import com.lcy0x1.base.block.type.TileEntitySupplier;
import com.lcy0x1.core.util.SerialClass;
import dev.lcy0x1.util.SerialClass;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RitualSide {
    public static final TileEntitySupplier TILE_ENTITY_SUPPLIER_BUILDER = TE::new;

    @SerialClass
    public static class TE extends RitualTE {

        public TE() {
            super(MagicContainerRegistry.TE_RITUAL_SIDE.get());
        }

    }

}
