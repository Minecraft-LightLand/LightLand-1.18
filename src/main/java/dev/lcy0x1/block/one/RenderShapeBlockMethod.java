package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface RenderShapeBlockMethod extends SingletonBlockMethod {

	@OnlyIn(Dist.CLIENT)
	RenderShape getRenderShape(BlockState state);

}
