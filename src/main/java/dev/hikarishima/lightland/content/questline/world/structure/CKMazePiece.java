package dev.hikarishima.lightland.content.questline.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.Random;

public class CKMazePiece extends TemplateStructurePiece {

	public CKMazePiece(StructureManager manager, String id, BlockPos pos, Rotation rotation, boolean ow) {
		super(StructurePieceType.END_CITY_PIECE, 0, manager, makeResourceLocation(id), id, makeSettings(ow, rotation), pos);
	}

	public CKMazePiece(StructureManager manager, CompoundTag tag) {
		super(StructurePieceType.END_CITY_PIECE, tag, manager, (id) ->
				makeSettings(tag.getBoolean("OW"), Rotation.valueOf(tag.getString("Rot"))));
	}

	private static StructurePlaceSettings makeSettings(boolean ow, Rotation rotation) {
		BlockIgnoreProcessor blockignoreprocessor = ow ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
		return (new StructurePlaceSettings()).setIgnoreEntities(true).addProcessor(blockignoreprocessor).setRotation(rotation);
	}

	protected ResourceLocation makeTemplateLocation() {
		return makeResourceLocation(this.templateName);
	}

	private static ResourceLocation makeResourceLocation(String id) {
		return new ResourceLocation("end_city/" + id);
	}

	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		super.addAdditionalSaveData(context, tag);
		tag.putString("Rot", this.placeSettings.getRotation().name());
		tag.putBoolean("OW", this.placeSettings.getProcessors().get(0) == BlockIgnoreProcessor.STRUCTURE_BLOCK);
	}

	protected void handleDataMarker(String id, BlockPos pos, ServerLevelAccessor leevel, Random random, BoundingBox box) {
		if (id.startsWith("Chest")) {
			BlockPos blockpos = pos.below();
			if (box.isInside(blockpos)) {
				RandomizableContainerBlockEntity.setLootTable(leevel, random, blockpos, BuiltInLootTables.END_CITY_TREASURE);
			}
		}

	}

}
