package dev.hikarishima.lightland.content.questline.world.structure.curseknight;

import dev.lcy0x1.maze.generator.MazeConfig;
import dev.lcy0x1.maze.generator.MazeGen;
import dev.lcy0x1.maze.objective.LeafMarker;
import dev.lcy0x1.maze.objective.MazeIterator;
import dev.lcy0x1.maze.objective.MazeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;
import java.util.Locale;

public class CKMazeGenerator {

	public enum RoomType {
		HALLWAY, LIBRARY, LABORATORY, PRISON
	}

	public enum RoomCellType {
		STRAIGHT, CORNER, END;

		public String getID(RoomType type) {
			return (type.name() + "_" + name()).toLowerCase(Locale.ROOT);
		}
	}

	public enum CrossType {
		NO_DOOR, ONE_DOOR, ADJACENT_DOOR, OPPOSITE_DOOR, THREE_DOOR;

		public String getID() {
			return "cross_" + name().toLowerCase(Locale.ROOT);
		}

		public static CellInstance get(boolean left, boolean right, boolean up, boolean down) {
			int val = (left ? 1 : 0) + (right ? 2 : 0) + (up ? 4 : 0) + (down ? 8 : 0);
			return switch (val) {
				case 1 -> new CellInstance(ONE_DOOR.getID(), Rotation.NONE, Mirror.NONE);
				case 2 -> new CellInstance(ONE_DOOR.getID(), Rotation.CLOCKWISE_180, Mirror.NONE);
				case 3 -> new CellInstance(OPPOSITE_DOOR.getID(), Rotation.NONE, Mirror.NONE);
				case 4 -> new CellInstance(ONE_DOOR.getID(), Rotation.CLOCKWISE_90, Mirror.NONE);
				case 5 -> new CellInstance(ADJACENT_DOOR.getID(), Rotation.CLOCKWISE_90, Mirror.NONE);
				case 6 -> new CellInstance(ADJACENT_DOOR.getID(), Rotation.CLOCKWISE_180, Mirror.NONE);
				case 7 -> new CellInstance(THREE_DOOR.getID(), Rotation.CLOCKWISE_90, Mirror.NONE);
				case 8 -> new CellInstance(ONE_DOOR.getID(), Rotation.COUNTERCLOCKWISE_90, Mirror.NONE);
				case 9 -> new CellInstance(ADJACENT_DOOR.getID(), Rotation.NONE, Mirror.NONE);
				case 10 -> new CellInstance(ADJACENT_DOOR.getID(), Rotation.COUNTERCLOCKWISE_90, Mirror.NONE);
				case 11 -> new CellInstance(THREE_DOOR.getID(), Rotation.COUNTERCLOCKWISE_90, Mirror.NONE);
				case 12 -> new CellInstance(OPPOSITE_DOOR.getID(), Rotation.CLOCKWISE_90, Mirror.NONE);
				case 13 -> new CellInstance(THREE_DOOR.getID(), Rotation.NONE, Mirror.NONE);
				case 14 -> new CellInstance(THREE_DOOR.getID(), Rotation.CLOCKWISE_180, Mirror.NONE);
				default -> new CellInstance(NO_DOOR.getID(), Rotation.NONE, Mirror.NONE);
			};
		}

	}

	public enum TWayType {
		NO_DOOR, CENTER_DOOR, LEFT_DOOR, CENTER_WAY, LEFT_WAY;

		public String getID() {
			return "t_way_" + name().toLowerCase(Locale.ROOT);
		}

		public static CellInstance get(boolean left, boolean center, boolean right, Rotation rot) {
			Mirror none = Mirror.NONE;
			Mirror mir = Mirror.LEFT_RIGHT;
			if (left) {
				if (center) {
					return new CellInstance(LEFT_WAY.getID(), rot, mir);
				} else if (right) {
					return new CellInstance(CENTER_WAY.getID(), rot, none);
				} else {
					return new CellInstance(LEFT_DOOR.getID(), rot, none);
				}
			} else if (center) {
				if (right) {
					return new CellInstance(LEFT_WAY.getID(), rot, none);
				} else {
					return new CellInstance(CENTER_DOOR.getID(), rot, none);
				}
			} else if (right) {
				return new CellInstance(LEFT_DOOR.getID(), rot, mir);
			} else {
				return new CellInstance(NO_DOOR.getID(), rot, none);
			}
		}

	}

	public record CellInstance(String id, Rotation rot, Mirror mir) {

	}

	public static CellInstance parseCell(MazeGen maze, int[] sets, LeafMarker[][] values, int x, int z) {
		int dire = maze.ans[x][z];
		// 1 = left
		// 2 = right
		// 4 = up
		// 8 = down

		RoomType room = RoomType.values()[values[x][z].level == 0 ? sets[values[x][z].getColor()] : 0];
		boolean d1 = values[x][z].level > 0 && x > 0 && values[x - 1][z].level == 0;
		boolean d2 = values[x][z].level > 0 && x < maze.w - 1 && values[x + 1][z].level == 0;
		boolean d4 = values[x][z].level > 0 && z > 0 && values[x][z - 1].level == 0;
		boolean d8 = values[x][z].level > 0 && z < maze.w - 1 && values[x][z + 1].level == 0;

		return switch (dire) {
			case 1 -> new CellInstance(RoomCellType.END.getID(room), Rotation.NONE, Mirror.NONE);
			case 2 -> new CellInstance(RoomCellType.END.getID(room), Rotation.CLOCKWISE_180, Mirror.NONE);
			case 3 -> new CellInstance(RoomCellType.STRAIGHT.getID(room), Rotation.NONE, Mirror.NONE);
			case 4 -> new CellInstance(RoomCellType.END.getID(room), Rotation.CLOCKWISE_90, Mirror.NONE);
			case 5 -> new CellInstance(RoomCellType.CORNER.getID(room), Rotation.CLOCKWISE_90, Mirror.NONE);
			case 6 -> new CellInstance(RoomCellType.CORNER.getID(room), Rotation.CLOCKWISE_180, Mirror.NONE);
			case 7 -> TWayType.get(d1, d4, d2, Rotation.CLOCKWISE_90);
			case 8 -> new CellInstance(RoomCellType.END.getID(room), Rotation.COUNTERCLOCKWISE_90, Mirror.NONE);
			case 9 -> new CellInstance(RoomCellType.CORNER.getID(room), Rotation.NONE, Mirror.NONE);
			case 10 -> new CellInstance(RoomCellType.CORNER.getID(room), Rotation.COUNTERCLOCKWISE_90, Mirror.NONE);
			case 11 -> TWayType.get(d2, d8, d1, Rotation.COUNTERCLOCKWISE_90);
			case 12 -> new CellInstance(RoomCellType.STRAIGHT.getID(room), Rotation.CLOCKWISE_90, Mirror.NONE);
			case 13 -> TWayType.get(d8, d1, d4, Rotation.NONE);
			case 14 -> TWayType.get(d4, d2, d8, Rotation.CLOCKWISE_180);
			case 15 -> CrossType.get(d1, d2, d4, d8);
			default -> new CellInstance("core", Rotation.NONE, Mirror.NONE);
		};
	}

	public static final int LENGTH = 5, HEIGHT = 9;
	public static final int[] LAYERS = {12};

	public static void addPieces(StructureManager manager, BlockPos pos, List<StructurePiece> children, WorldgenRandom r, MazeConfig conf) {
		MazeGen[] mazes = new MazeGen[LAYERS.length];
		for (int i = 0; i < LAYERS.length; i++) {
			mazes[i] = new MazeGen(LAYERS[i], r, conf, new MazeGen.Debugger());
			mazes[i].gen();
			MazeIterator<LeafMarker, LeafMarker.LeafSetData> itr = MazeRegistry.MARKER.generate(mazes[i].ans, mazes[i].r, mazes[i].r);
			int[] sets = new int[itr.global.current_color + 1];
			for (int c = 0; c < sets.length; c++) {
				sets[c] = r.nextInt(RoomType.values().length - 1) + 1;
			}
			for (int x = 0; x < mazes[i].w; x++)
				for (int z = 0; z < mazes[i].w; z++) {
					CellInstance ins = parseCell(mazes[i], sets, itr.value, x, z);
					children.add(new CKMazePiece(manager, ins, pos.offset(
							(x - mazes[i].r) * LENGTH,
							50 - i * HEIGHT,
							(z - mazes[i].r) * LENGTH
					), false));
				}
		}
	}

}
