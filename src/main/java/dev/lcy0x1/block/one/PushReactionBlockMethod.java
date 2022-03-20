package dev.lcy0x1.block.one;

import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public interface PushReactionBlockMethod extends SingletonBlockMethod {

	PushReaction getPistonPushReaction(BlockState state);

}
