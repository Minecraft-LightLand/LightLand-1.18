package dev.hikarishima.lightland.content.questline.world.structure;

import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.maze.MazeConfig;
import dev.lcy0x1.maze.MazeGen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;
import java.util.Random;

public class CKMazePieces {

	public enum CellType {
		END, STRAIGHT, CORNER, T_WAY, CROSS;
	}

	public static class Piece extends TemplateStructurePiece {

		public Piece(StructureManager manager, String id, BlockPos pos, Rotation rotation, boolean ow) {
			super(StructurePieceType.END_CITY_PIECE, 0, manager, makeResourceLocation(id), id, makeSettings(ow, rotation), pos);
		}

		public Piece(StructureManager manager, CompoundTag tag) {
			super(StructurePieceType.END_CITY_PIECE, tag, manager, (id) ->
					makeSettings(tag.getBoolean("OW"), Rotation.valueOf(tag.getString("Rot"))));
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
			this(context.structureManager(), tag);
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

	public static final String CORE = "core";
	public static final String STRAIGHT = "straight";
	public static final String CORNER = "corner";
	public static final String T_WAY = "t_way";
	public static final String CROSS = "cross";
	public static final String END = "end";

	public static final ResourceLocation SIDE_EDGE = new ResourceLocation("oceanmaze:oceanmaze/side_edge");
	public static final ResourceLocation SIDE_CORNER = new ResourceLocation("oceanmaze:oceanmaze/side_corner");
	public static final ResourceLocation LAST_SIDE_EDGE = new ResourceLocation("oceanmaze:oceanmaze/last_side_edge");
	public static final ResourceLocation LAST_SIDE_CORNER = new ResourceLocation("oceanmaze:oceanmaze/last_side_corner");
	public static final ResourceLocation TOP_FACE = new ResourceLocation("oceanmaze:oceanmaze/top_face");
	public static final ResourceLocation TOP_EDGE = new ResourceLocation("oceanmaze:oceanmaze/top_edge");
	public static final ResourceLocation TOP_CORNER = new ResourceLocation("oceanmaze:oceanmaze/top_corner");
	public static final ResourceLocation BOTTOM_FACE = new ResourceLocation("oceanmaze:oceanmaze/bottom_face");
	public static final ResourceLocation BOTTOM_EDGE = new ResourceLocation("oceanmaze:oceanmaze/bottom_edge");
	public static final ResourceLocation BOTTOM_CORNER = new ResourceLocation("oceanmaze:oceanmaze/bottom_corner");

	public static final int[] LAYERS = {7, 7, 7, 7, 7};
	public static final int[] WEIGHTS = {5, 10, 15};

	public static void addPieces(StructureManager manager, BlockPos pos, List<StructurePiece> children, WorldgenRandom r,
								 MazeConfig conf) {
		MazeGen[] mazes = new MazeGen[LAYERS.length];
		for (int i = 0; i < LAYERS.length; i++) {
			mazes[i] = new MazeGen(LAYERS[i], r, conf, new MazeGen.Debugger());
			mazes[i].gen();
			int[][] map = mazes[i].ans;
			for (int x = 0; x < mazes[i].w; x++)
				for (int z = 0; z < mazes[i].w; z++) {
					int dire = map[x][z];
					CellType ct = parse_ct(dire);
					String id = parse_id(ct, r, Math.abs(x - mazes[i].r), Math.abs(z - mazes[i].r));
					children.add(new Piece(manager, id, pos.offset((x - mazes[i].r) * 5, -i * 5, (z - mazes[i].r) * 5),
							parse_rot(dire), false));
				}
		}
	}

	private static CellType parse_ct(int cell) {
		if (cell == 1 || cell == 2 || cell == 4 || cell == 8)
			return CellType.END;
		if (cell == 3 || cell == 12)
			return CellType.STRAIGHT;
		if (cell == 5 || cell == 9 || cell == 6 || cell == 10)
			return CellType.CORNER;
		if (cell == 7 || cell == 11 || cell == 13 || cell == 14)
			return CellType.T_WAY;
		if (cell == 15)
			return CellType.CROSS;
		LightLand.LOGGER.error("invalid cell " + cell);
		return CellType.CROSS;
	}

	private static String parse_id(CellType ct, WorldgenRandom r, int x, int z) {
		if (ct == CellType.STRAIGHT)
			return STRAIGHT;
		if (ct == CellType.CORNER)
			return CORNER;
		if (ct == CellType.T_WAY)
			return T_WAY;
		if (ct == CellType.CROSS)
			return CROSS;
		if (ct == CellType.END) {
			if (x == 0 && z == 0)
				return CORE;
			return END;
		}
		return END;
	}

	private static Rotation parse_rot(int cell) {
		if (cell == 1)
			return Rotation.NONE;
		if (cell == 2)
			return Rotation.CLOCKWISE_180;
		if (cell == 8)
			return Rotation.COUNTERCLOCKWISE_90;
		if (cell == 4)
			return Rotation.CLOCKWISE_90;

		if (cell == 3)
			return Rotation.NONE;
		if (cell == 12)
			return Rotation.CLOCKWISE_90;

		if (cell == 9)
			return Rotation.NONE;
		if (cell == 6)
			return Rotation.CLOCKWISE_180;
		if (cell == 10)
			return Rotation.COUNTERCLOCKWISE_90;
		if (cell == 5)
			return Rotation.CLOCKWISE_90;

		if (cell == 13)
			return Rotation.NONE;
		if (cell == 14)
			return Rotation.CLOCKWISE_180;
		if (cell == 11)
			return Rotation.COUNTERCLOCKWISE_90;
		if (cell == 7)
			return Rotation.CLOCKWISE_90;

		if (cell == 15)
			return Rotation.NONE;
		LightLand.LOGGER.error("invalid cell " + cell);
		return Rotation.NONE;
	}

}
