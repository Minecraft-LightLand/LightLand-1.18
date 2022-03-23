package dev.hikarishima.lightland.content.questline.world.structure.curseknight;

import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.Random;

public class CKMazePiece extends TemplateStructurePiece {

	private static final int SHIFT = 2;

	private static StructurePlaceSettings makeSettings(boolean ow, Rotation rotation, Mirror mirror) {
		BlockIgnoreProcessor blockignoreprocessor = ow ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
		return (new StructurePlaceSettings()).setIgnoreEntities(true)
				.addProcessor(blockignoreprocessor)
				.setRotation(rotation).setMirror(mirror);
	}

	private static BlockPos shiftPos(StructurePlaceSettings settings, BlockPos pos) {
		BlockPos center = new BlockPos(SHIFT, 0, SHIFT);
		BlockPos actual = StructureTemplate.transform(center, settings.getMirror(), settings.getRotation(), settings.getRotationPivot());
		return pos.offset(center.subtract(actual));
	}

	private static ResourceLocation makeResourceLocation(String id) {
		return new ResourceLocation(LightLand.MODID, "cursedknight_maze/" + id);
	}

	public CKMazePiece(StructureManager manager, CKMazeGenerator.CellInstance ins, BlockPos pos, boolean ow) {
		this(manager, ins, makeSettings(ow, ins.rot(), ins.mir()), pos);
	}

	private CKMazePiece(StructureManager manager, CKMazeGenerator.CellInstance ins, StructurePlaceSettings settings, BlockPos pos) {
		super(StructurePieceType.END_CITY_PIECE, 0, manager,
				makeResourceLocation(ins.id()), ins.id(), settings, shiftPos(settings, pos));
	}

	public CKMazePiece(StructureManager manager, CompoundTag tag) {
		super(StructurePieceType.END_CITY_PIECE, tag, manager, (id) ->
				makeSettings(tag.getBoolean("OW"),
						Rotation.valueOf(tag.getString("Rot")),
						Mirror.valueOf(tag.getString("Mir"))));
	}

	protected ResourceLocation makeTemplateLocation() {
		return makeResourceLocation(this.templateName);
	}


	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		super.addAdditionalSaveData(context, tag);
		tag.putString("Rot", this.placeSettings.getRotation().name());
		tag.putString("Mir", this.placeSettings.getMirror().name());
		tag.putBoolean("OW", this.placeSettings.getProcessors().get(0) == BlockIgnoreProcessor.STRUCTURE_BLOCK);
	}

	protected void handleDataMarker(String id, BlockPos pos, ServerLevelAccessor level, Random random, BoundingBox box) {
		if (id.startsWith("Chest")) {
			BlockPos blockpos = pos.below();
			if (box.isInside(blockpos)) {
				RandomizableContainerBlockEntity.setLootTable(level, random, blockpos, BuiltInLootTables.END_CITY_TREASURE);
			}
		}
	}

}
