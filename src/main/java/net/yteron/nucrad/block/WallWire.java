package net.yteron.nucrad.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

public class WallWire extends BarbedWire{

    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(1)
            .strength(3.0f, 4.0f)
            .harvestTool(ToolType.PICKAXE)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .isViewBlocking((state, reader, pos) -> false)
            .isRedstoneConductor((state, reader, pos) -> false)
            .dynamicShape()
            .sound(SoundType.METAL);
    @Override
    public boolean canConnect(BlockState state) {
        return state.getBlock() == this;
    }

}
