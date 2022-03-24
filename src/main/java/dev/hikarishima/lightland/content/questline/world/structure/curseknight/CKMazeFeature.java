package dev.hikarishima.lightland.content.questline.world.structure.curseknight;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import dev.hikarishima.lightland.content.questline.world.structure.BaseStructureFeature;
import dev.lcy0x1.maze.generator.MazeConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.QuartPos;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CKMazeFeature extends BaseStructureFeature<CKMazeFeature, NoneFeatureConfiguration> {

	private static final List<MobSpawnSettings.SpawnerData> SPAWN_LIST = List.of(

	);

	public CKMazeFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec, CKMazeFeature::pieceGeneratorSupplier, CKMazeFeature::afterPlace);
	}

	protected boolean linearSeparation() {
		return false;
	}

	private static int getYPositionForFeature(ChunkPos chunkPos, ChunkGenerator generator, LevelHeightAccessor level) {
		Random random = new Random(chunkPos.x + chunkPos.z * 10387313L);
		Rotation rotation = Rotation.getRandom(random);
		int i = 5;
		int j = 5;
		if (rotation == Rotation.CLOCKWISE_90) {
			i = -5;
		} else if (rotation == Rotation.CLOCKWISE_180) {
			i = -5;
			j = -5;
		} else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
			j = -5;
		}

		int k = chunkPos.getBlockX(7);
		int l = chunkPos.getBlockZ(7);
		int i1 = generator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG, level);
		int j1 = generator.getFirstOccupiedHeight(k, l + j, Heightmap.Types.WORLD_SURFACE_WG, level);
		int k1 = generator.getFirstOccupiedHeight(k + i, l, Heightmap.Types.WORLD_SURFACE_WG, level);
		int l1 = generator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Types.WORLD_SURFACE_WG, level);
		return Math.min(Math.min(i1, j1), Math.min(k1, l1));
	}

	private static Optional<PieceGenerator<NoneFeatureConfiguration>> pieceGeneratorSupplier(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
		int i = getYPositionForFeature(context.chunkPos(), context.chunkGenerator(), context.heightAccessor());
		if (i < -64) {
			return Optional.empty();
		} else {
			BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(i);
			return !context.validBiome().test(context.chunkGenerator().getNoiseBiome(
					QuartPos.fromBlock(blockpos.getX()),
					QuartPos.fromBlock(blockpos.getY()),
					QuartPos.fromBlock(blockpos.getZ()))) ?
					Optional.empty() : Optional.of((builder, ctx) -> {
				MazeConfig config = new MazeConfig();
				config.invariant = 2;
				config.survive = 4;
				config.INVARIANCE_RIM = new int[][]{{0, 1, 2, 3, 4, 5, 6, 7}, {0, 4, 8, 12, 1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15}};
				List<StructurePiece> list = Lists.newArrayList();
				CKMazeGenerator.addPieces(ctx.structureManager(), blockpos, list, ctx.random(), config);
				list.forEach(builder::addPiece);

			});
		}
	}


	private static void afterPlace(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator generator,
								   Random random, BoundingBox box, ChunkPos chunkPos, PiecesContainer container) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		int i = level.getMinBuildHeight();
		BoundingBox boundingbox = container.calculateBoundingBox();
		int j = boundingbox.minY();

		for (int k = box.minX(); k <= box.maxX(); ++k) {
			for (int l = box.minZ(); l <= box.maxZ(); ++l) {
				pos.set(k, j, l);
				while (boundingbox.isInside(pos) && !container.isInsidePiece(pos))
					pos.move(Direction.UP);
				if (!level.isEmptyBlock(pos) && boundingbox.isInside(pos)) {
					for (int i1 = pos.getY() - 1; i1 > i; --i1) {
						pos.setY(i1);
						if (!level.isEmptyBlock(pos) && !level.getBlockState(pos).getMaterial().isLiquid()) {
							break;
						}

						level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 2);
					}
				}
			}
		}

	}

	@Override
	public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList(MobCategory category) {
		return SPAWN_LIST;
	}
}
