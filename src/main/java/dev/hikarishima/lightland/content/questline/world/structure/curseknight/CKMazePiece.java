package dev.hikarishima.lightland.content.questline.world.structure.curseknight;

import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.worldgenreg.StructureRegistrate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
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

	private static final int SHIFT = 7;

	public enum ShiftType {
		FLAT(new BlockPos(SHIFT, 0, SHIFT)),
		EDGE(new BlockPos(0, 0, SHIFT)),
		NONE(new BlockPos(0, 0, 0));

		public final BlockPos pos;

		ShiftType(BlockPos pos) {
			this.pos = pos;
		}
	}

	private static StructurePlaceSettings makeSettings(boolean inner, Rotation rotation, Mirror mirror, ShiftType shift) {
		BlockIgnoreProcessor processor = inner ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
		return (new StructurePlaceSettings()).setIgnoreEntities(true)
				.addProcessor(processor).setKeepLiquids(!inner)
				.setRotation(rotation).setMirror(mirror);
	}

	private static BlockPos shiftPos(StructurePlaceSettings settings, BlockPos pos, ShiftType shift) {
		if (shift == ShiftType.FLAT) {
			BlockPos center = shift.pos;
			BlockPos actual = StructureTemplate.transform(center, settings.getMirror(), settings.getRotation(), settings.getRotationPivot());
			return pos.offset(center.subtract(actual));
		}
		return pos;
	}

	private static ResourceLocation makeResourceLocation(String id) {
		return new ResourceLocation(LightLand.MODID, "cursedknight_maze/" + id);
	}

	private final ShiftType shift;

	public CKMazePiece(StructureManager manager, CKMazeGenerator.CellInstance ins, BlockPos pos, ShiftType shift, boolean inner) {
		this(manager, ins, makeSettings(inner, ins.rot(), ins.mir(), shift), pos, shift);
	}

	private CKMazePiece(StructureManager manager, CKMazeGenerator.CellInstance ins, StructurePlaceSettings settings, BlockPos pos, ShiftType shift) {
		super(StructureRegistrate.CKMAZE.piece_type, 0, manager,
				makeResourceLocation(ins.id()), ins.id(), settings, shiftPos(settings, pos, shift));
		this.shift = shift;
	}

	public CKMazePiece(StructureManager manager, CompoundTag tag) {
		super(StructureRegistrate.CKMAZE.piece_type, tag, manager, (id) ->
				makeSettings(tag.getBoolean("OW"),
						Rotation.valueOf(tag.getString("Rot")),
						Mirror.valueOf(tag.getString("Mir")),
						ShiftType.valueOf(tag.getString("Shift"))));
		this.shift = ShiftType.valueOf(tag.getString("Shift"));
	}

	protected ResourceLocation makeTemplateLocation() {
		return makeResourceLocation(this.templateName);
	}


	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		super.addAdditionalSaveData(context, tag);
		tag.putString("Rot", this.placeSettings.getRotation().name());
		tag.putString("Mir", this.placeSettings.getMirror().name());
		tag.putBoolean("OW", this.placeSettings.getProcessors().get(0) == BlockIgnoreProcessor.STRUCTURE_BLOCK);
		tag.putString("Shift", this.shift.name());
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
