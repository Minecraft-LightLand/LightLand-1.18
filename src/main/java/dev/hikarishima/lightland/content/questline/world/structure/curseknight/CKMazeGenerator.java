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

	public static final String BOSS_ROOM = "boss_room", TOP_FLAT = "top_flat",
			EDGE_TOP_WALL = "edge_top_wall", EDGE_TOP_CORNER = "edge_top_corner",
			EDGE_HIGH_WALL = "edge_high_wall", EDGE_HIGH_CORNER = "edge_high_corner",
			EDGE_CORNER = "edge_corner", EDGE_WALL = "edge_wall", EDGE_DOOR = "edge_door";

	public record CellInstance(String id, Rotation rot, Mirror mir) {

	}

	public static CellInstance parseCell(MazeGen maze, int[] sets, LeafMarker[][] values, int x, int z) {
		int dire = maze.ans[x][z];
		// 1 = left
		// 2 = right
		// 4 = up
		// 8 = down

		RoomType room = RoomType.values()[values[x][z].level == 0 ? sets[values[x][z].getColor()] : 0];
		boolean d1 = values[x][z].level > 0 && x > 0 && values[x - 1][z].isLeaf(sets);
		boolean d2 = values[x][z].level > 0 && x < maze.w - 1 && values[x + 1][z].isLeaf(sets);
		boolean d4 = values[x][z].level > 0 && z > 0 && values[x][z - 1].isLeaf(sets);
		boolean d8 = values[x][z].level > 0 && z < maze.w - 1 && values[x][z + 1].isLeaf(sets);

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

	public static final int LENGTH = 15, HEIGHT = 11;
	public static final int[] LAYERS = {7, 7, 7};

	public static void addPieces(StructureManager manager, BlockPos pos, List<StructurePiece> children, WorldgenRandom r, MazeConfig conf) {
		pos = pos.above();
		MazeGen[] mazes = new MazeGen[LAYERS.length];

		// maze body
		for (int i = 0; i < LAYERS.length; i++) {
			mazes[i] = new MazeGen(LAYERS[i], r, conf, new MazeGen.Debugger());
			mazes[i].gen();
			MazeIterator<LeafMarker, LeafMarker.LeafSetData> itr = MazeRegistry.MARKER.generate(mazes[i].ans, mazes[i].r, mazes[i].r);
			int[] sets = new int[itr.global.current_color + 1];
			for (int c = 0; c < sets.length; c++) {
				sets[c] = r.nextInt(RoomType.values().length - 1) + 1;
			}
			if (i == 1) {
				sets[itr.value[0][mazes[i].r].getColor()] = 0;
				sets[itr.value[mazes[i].w - 1][mazes[i].r].getColor()] = 0;
				sets[itr.value[mazes[i].r][0].getColor()] = 0;
				sets[itr.value[mazes[i].r][mazes[i].w - 1].getColor()] = 0;
				mazes[i].ans[0][mazes[i].r] |= 1;
				mazes[i].ans[mazes[i].w - 1][mazes[i].r] |= 2;
				mazes[i].ans[mazes[i].r][0] |= 4;
				mazes[i].ans[mazes[i].r][mazes[i].w - 1] |= 8;
			}
			for (int x = 0; x < mazes[i].w; x++)
				for (int z = 0; z < mazes[i].w; z++) {
					if (Math.abs(x - mazes[i].r) <= 1 && Math.abs(z - mazes[i].r) <= 1)
						continue;
					CellInstance ins = parseCell(mazes[i], sets, itr.value, x, z);
					children.add(new CKMazePiece(manager, ins, pos.offset(
							(x - mazes[i].r) * LENGTH,
							(i - 1) * HEIGHT,
							(z - mazes[i].r) * LENGTH
					), CKMazePiece.ShiftType.FLAT, true));
				}
		}

		// boos room
		children.add(new CKMazePiece(manager, new CellInstance(BOSS_ROOM, Rotation.NONE, Mirror.NONE), pos.offset(
				-LENGTH, -HEIGHT, -LENGTH), CKMazePiece.ShiftType.FLAT, true));

		// roof
		for (int x = 0; x < mazes[2].w; x++) {
			for (int z = 0; z < mazes[2].w; z++) {
				children.add(new CKMazePiece(manager, new CellInstance(TOP_FLAT, Rotation.NONE, Mirror.NONE), pos.offset(
						(x - mazes[2].r) * LENGTH,
						2 * HEIGHT,
						(z - mazes[2].r) * LENGTH
				), CKMazePiece.ShiftType.FLAT, false));
			}
		}

		// roof edge
		int start_pos = -mazes[2].r * LENGTH - 1;
		int end_pos = (mazes[2].w - mazes[2].r) * LENGTH;
		for (int i = 0; i < mazes[2].w; i++) {
			int ix = (i - mazes[2].r) * LENGTH;
			addEdges(manager, children, pos, end_pos, ix, Rotation.NONE, i == mazes[1].r);
			addEdges(manager, children, pos, start_pos, ix + LENGTH - 1, Rotation.CLOCKWISE_180, i == mazes[1].r);
			addEdges(manager, children, pos, ix + LENGTH - 1, end_pos, Rotation.CLOCKWISE_90, i == mazes[1].r);
			addEdges(manager, children, pos, ix, start_pos, Rotation.COUNTERCLOCKWISE_90, i == mazes[1].r);
		}
		addCorners(manager, children, pos, end_pos, end_pos, Rotation.NONE);
		addCorners(manager, children, pos, start_pos, start_pos, Rotation.CLOCKWISE_180);
		addCorners(manager, children, pos, start_pos, end_pos, Rotation.CLOCKWISE_90);
		addCorners(manager, children, pos, end_pos, start_pos, Rotation.COUNTERCLOCKWISE_90);

	}

	private static void addEdges(StructureManager manager, List<StructurePiece> children, BlockPos pos, int x, int z, Rotation rot, boolean door) {
		children.add(new CKMazePiece(manager, new CellInstance(EDGE_TOP_WALL, rot, Mirror.NONE),
				pos.offset(x, 2 * HEIGHT, z), CKMazePiece.ShiftType.EDGE, false));
		children.add(new CKMazePiece(manager, new CellInstance(EDGE_HIGH_WALL, rot, Mirror.NONE),
				pos.offset(x, HEIGHT, z), CKMazePiece.ShiftType.EDGE, false));
		children.add(new CKMazePiece(manager, new CellInstance(door ? EDGE_DOOR : EDGE_WALL, rot, Mirror.NONE),
				pos.offset(x, 0, z), CKMazePiece.ShiftType.EDGE, true));
	}

	private static void addCorners(StructureManager manager, List<StructurePiece> children, BlockPos pos, int x, int z, Rotation rot) {
		children.add(new CKMazePiece(manager, new CellInstance(EDGE_TOP_CORNER, rot, Mirror.NONE),
				pos.offset(x, 2 * HEIGHT, z), CKMazePiece.ShiftType.NONE, false));
		children.add(new CKMazePiece(manager, new CellInstance(EDGE_HIGH_CORNER, rot, Mirror.NONE),
				pos.offset(x, HEIGHT, z), CKMazePiece.ShiftType.NONE, false));
		children.add(new CKMazePiece(manager, new CellInstance(EDGE_CORNER, rot, Mirror.NONE),
				pos.offset(x, 0, z), CKMazePiece.ShiftType.NONE, false));
	}

}
