package net.yteron.nucrad.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraftforge.common.ToolType;

public class DeadSand extends Block {
    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(2)
            .strength(10.0f, 6.0f)
            .harvestTool(ToolType.SHOVEL)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 0)
            .dynamicShape()
            .sound(SoundType.SOUL_SAND);
    public DeadSand() {
        super(PROPERTIES);
    }
}
