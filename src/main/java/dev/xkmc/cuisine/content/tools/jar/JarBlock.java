package dev.xkmc.cuisine.content.tools.jar;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;

public class JarBlock {

	public static final BlockEntityBlockMethod<JarBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_JAR, JarBlockEntity.class);

}
