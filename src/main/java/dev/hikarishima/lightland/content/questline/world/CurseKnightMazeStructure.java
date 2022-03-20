package dev.hikarishima.lightland.content.questline.world;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.WoodlandMansionPieces;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CurseKnightMazeStructure extends StructureFeature<NoneFeatureConfiguration> {

	public CurseKnightMazeStructure(Codec<NoneFeatureConfiguration> codec) {
		super(codec, CurseKnightMazeStructure::pieceGeneratorSupplier, CurseKnightMazeStructure::afterPlace);
	}

	private static Optional<PieceGenerator<NoneFeatureConfiguration>> pieceGeneratorSupplier(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
		Rotation rotation = Rotation.getRandom(worldgenrandom);
		int x_len = 5;
		int z_len = 5;
		if (rotation == Rotation.CLOCKWISE_90) {
			x_len = -x_len;
		} else if (rotation == Rotation.CLOCKWISE_180) {
			x_len = -x_len;
			z_len = -z_len;
		} else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
			z_len = -z_len;
		}
		int x0 = context.chunkPos().getBlockX(7);
		int z0 = context.chunkPos().getBlockZ(7);

		int[] heights = context.getCornerHeights(x0, x_len, z0, z_len);
		int min_height = Math.min(Math.min(heights[0], heights[1]), Math.min(heights[2], heights[3]));
		if (min_height < 60) {
			return Optional.empty();
		} else if (!context.validBiome().test(context.chunkGenerator()
				.getNoiseBiome(QuartPos.fromBlock(x0), QuartPos.fromBlock(heights[0]), QuartPos.fromBlock(z0)))) {
			return Optional.empty();
		} else {
			BlockPos blockpos = new BlockPos(context.chunkPos().getMiddleBlockX(), min_height + 1, context.chunkPos().getMiddleBlockZ());
			return Optional.of((p_197192_, p_197193_) -> {
				List<WoodlandMansionPieces.WoodlandMansionPiece> list = Lists.newLinkedList();
				WoodlandMansionPieces.generateMansion(p_197193_.structureManager(), blockpos, rotation, list, worldgenrandom);
				list.forEach(p_197192_::addPiece);
			});
		}
	}

	private static void afterPlace(WorldGenLevel p_191195_, StructureFeatureManager p_191196_, ChunkGenerator p_191197_, Random p_191198_, BoundingBox p_191199_, ChunkPos p_191200_, PiecesContainer p_191201_) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		int i = p_191195_.getMinBuildHeight();
		BoundingBox boundingbox = p_191201_.calculateBoundingBox();
		int j = boundingbox.minY();

		for (int k = p_191199_.minX(); k <= p_191199_.maxX(); ++k) {
			for (int l = p_191199_.minZ(); l <= p_191199_.maxZ(); ++l) {
				blockpos$mutableblockpos.set(k, j, l);
				if (!p_191195_.isEmptyBlock(blockpos$mutableblockpos) && boundingbox.isInside(blockpos$mutableblockpos) && p_191201_.isInsidePiece(blockpos$mutableblockpos)) {
					for (int i1 = j - 1; i1 > i; --i1) {
						blockpos$mutableblockpos.setY(i1);
						if (!p_191195_.isEmptyBlock(blockpos$mutableblockpos) && !p_191195_.getBlockState(blockpos$mutableblockpos).getMaterial().isLiquid()) {
							break;
						}

						p_191195_.setBlock(blockpos$mutableblockpos, Blocks.COBBLESTONE.defaultBlockState(), 2);
					}
				}
			}
		}

	}

}
