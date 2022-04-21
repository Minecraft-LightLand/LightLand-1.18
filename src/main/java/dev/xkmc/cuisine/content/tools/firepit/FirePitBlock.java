package dev.xkmc.cuisine.content.tools.firepit;

import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.xkmc.cuisine.content.tools.firepit.methods.FirePitBurnMethod;
import dev.xkmc.cuisine.content.tools.firepit.methods.FirePitPutMethod;
import dev.xkmc.cuisine.content.tools.firepit.methods.FirePitShapeMethod;
import dev.xkmc.cuisine.content.tools.firepit.stick.FirePitStickBlockEntity;
import dev.xkmc.cuisine.content.tools.firepit.wok.FirePitWokBlockEntity;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;

public class FirePitBlock {

	public static final BlockEntityBlockMethod<FirePitStickBlockEntity> STICK = new BlockEntityBlockMethodImpl<>(
			CuisineBlocks.TE_STICK, FirePitStickBlockEntity.class);

	public static final BlockEntityBlockMethod<FirePitWokBlockEntity> WOK = new BlockEntityBlockMethodImpl<>(
			CuisineBlocks.TE_WOK, FirePitWokBlockEntity.class);

	public static final FirePitPutMethod PUT = new FirePitPutMethod();
	public static final FirePitBurnMethod BURN = new FirePitBurnMethod();

	public static final FirePitShapeMethod SHAPE_BASE = new FirePitShapeMethod(5);
	public static final FirePitShapeMethod SHAPE_WOK = new FirePitShapeMethod(4);
	public static final FirePitShapeMethod SHAPE_BASE_AND_WOK = new FirePitShapeMethod(8);


}
