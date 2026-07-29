package net.yteron.nucrad.radiation;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.yteron.nucrad.init.Modblock;

import static net.yteron.nucrad.radiation.RadiationRegistry.getRadiation;

public class RadiationSystem {
    private static final double DIRT_TRANSFORM_THRESHOLD = 50.0;
    private static final double SAND_TRANSFORM_THRESHOLD = 60.0;

    public static void transformBlock(World world, BlockPos pos, double radiation) {
        if (world.isClientSide) return;
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == Blocks.DIRT || block == Blocks.GRASS && DIRT_TRANSFORM_THRESHOLD > radiation) {
            world.setBlock(pos, Modblock.DEAD_EARH.get().defaultBlockState(), 3);
            RadiationRegistry.addRadiation(pos, radiation);
        } else if (block == Blocks.SAND && SAND_TRANSFORM_THRESHOLD > radiation) {
            world.setBlock(pos, Modblock.DEAD_SAND.get().defaultBlockState(), 3);
            RadiationRegistry.addRadiation(pos, radiation);
        }

    }

    public static void spreadingRadition(World world, BlockPos pos,double radiation) {
        if (world.isClientSide) return;
        radiation/=2;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dz == 0 && dx == 0 && dy == 0) continue;

                    BlockPos cubePos = pos.offset(dx, dy, dz);
                    BlockState state = world.getBlockState(cubePos);
                    Block block = state.getBlock();

                    if (block instanceof IRadBlocks) {
                        IRadBlocks radBlock = (IRadBlocks) block;
                        double rad = radBlock.getRadiation();
                        double defeatRad = radBlock.getDefeatRadiation();
                        double itogRadiation = rad + radiation - defeatRad;
                        if (radiation<getRadiation(world,cubePos)-defeatRad)
                            RadiationRegistry.addRadiation(cubePos, itogRadiation);
                    }
                    else if (radiation<getRadiation(world,cubePos))
                    {
                        RadiationRegistry.addRadiation(cubePos, radiation);
                        transformBlock(world,cubePos,radiation);
                    }

                }
            }
        }
    }



}
