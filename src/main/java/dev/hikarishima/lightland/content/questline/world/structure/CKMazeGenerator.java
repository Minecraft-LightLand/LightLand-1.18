package dev.hikarishima.lightland.content.questline.world.structure;

import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.maze.generator.MazeConfig;
import dev.lcy0x1.maze.generator.MazeGen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;

public class CKMazeGenerator {

	public enum CellType {
		END, STRAIGHT, CORNER, T_WAY, CROSS;
	}

	public static final String CROSS = "cross_0",
			CROSS_1 = "cross_1",
			CROSS_STRAIGHT = "cross_2_straight",
			CROSS_CORNER = "cross_2_corner",
			CROSS_3 = "cross_3";
	public static final String T_WAY = "t_way_0",
			T_LEFT_DOOR = "t_way_1_left",
			T_CENTER_DOOR = "t_way_1_center",
			T_RIGHT_WAY = "t_way_2_right",
			T_CENTER_WAY = "t_way_2_center";
	public static final String STRAIGHT = "straight_0";
	public static final String CORNER = "corner_0";
	public static final String END = "end_0";
	public static final String CORE = "core";

	

	public static final int LENGTH = 15, HEIGHT = 9;
	public static final int[] LAYERS = {7, 7, 7, 7, 7};

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
					children.add(new CKMazePiece(manager, id, pos.offset(
							(x - mazes[i].r) * LENGTH,
							-i * HEIGHT,
							(z - mazes[i].r) * LENGTH
					), parse_rot(dire), false));
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
				return END;
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
