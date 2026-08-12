package net.yteron.nucrad.block.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.yteron.nucrad.radiation.ChunkRaditonManager;

public class DeadEarth extends Block {
    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(2)
            .strength(10.0f, 6.0f)
            .harvestTool(ToolType.SHOVEL)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 0)
            .dynamicShape()
            .sound(SoundType.SOUL_SOIL);
    public DeadEarth() {
        super(PROPERTIES);
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            float radLevel = 50.0F;
            ChunkRaditonManager.proxy.setRadiation(world, pos.getX(), pos.getY(), pos.getZ(), radLevel);
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide && newState.getBlock() != this) {
            ChunkRaditonManager.proxy.decrementRad(world, pos.getX(), pos.getY(), pos.getZ(), 5.0F);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}
