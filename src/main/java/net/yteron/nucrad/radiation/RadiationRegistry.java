package net.yteron.nucrad.radiation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.yteron.nucrad.init.Modblock;

import java.util.HashMap;
import java.util.Map;

public class RadiationRegistry {
    private static final Map<Block, Double> BLOCK_RADIATION = new HashMap<>();
    private static final Map<BlockPos, Double> ACCUMULETED_RADIATION = new HashMap<BlockPos, Double>();
    public static void register(Block block, Double radiation) {
        BLOCK_RADIATION.put(block, radiation);
    }
    public static void registerRadiation() {
        register(Blocks.REDSTONE_ORE, 0.5);
        register(Blocks.LAVA, 2.0);
        register(Blocks.MAGMA_BLOCK, 3.0);

        register(Modblock.DEAD_EARH.get(), 100.0);
        register(Modblock.DEAD_SAND.get(), 120.0);
    }
    public static double getRadiation(World world, BlockPos pos) {
        if (world == null || pos == null) return 0.0;
        if (!world.isLoaded(pos)) return 0.0;
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        double base = BLOCK_RADIATION.getOrDefault(block, 0.0);
        double accumulated = ACCUMULETED_RADIATION.getOrDefault(pos, 0.0);

        return base + accumulated;
    }

    public static void addRadiation(BlockPos pos, double amount) {
        double current = ACCUMULETED_RADIATION.getOrDefault(pos, 0.0);
        ACCUMULETED_RADIATION.put(pos, current + amount);
    }

    public static void clearRadiation(BlockPos pos) {
        ACCUMULETED_RADIATION.remove(pos);
    }
}
