package dev.hikarishima.lightland.content.questline.world.structure.curseknight;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import dev.hikarishima.lightland.content.questline.world.structure.BaseStructureFeature;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.maze.generator.MazeConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CKMazeFeature extends BaseStructureFeature<CKMazeFeature, NoneFeatureConfiguration> {

	public CKMazeFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec, CKMazeFeature::pieceGeneratorSupplier, CKMazeFeature::afterPlace);
	}

	private static Optional<PieceGenerator<NoneFeatureConfiguration>> pieceGeneratorSupplier(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
		BlockPos pos = context.chunkPos().getMiddleBlockPosition(0);
		int topLandY = context.chunkGenerator().getFirstFreeHeight(pos.getX(), pos.getZ(),
				Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
		BlockPos blockpos = pos.above(topLandY);
		Optional<PieceGenerator<NoneFeatureConfiguration>> generator =
				!context.validBiome().test(context.chunkGenerator().getNoiseBiome(
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
		if (generator.isPresent()) {
			LightLand.LOGGER.log(Level.DEBUG, "Rundown House at {}", blockpos);
		}
		return generator;
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

}
