package net.yteron.nucrad.block.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraftforge.common.ToolType;

public class ConcereteIronDefeat extends Block {
    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(2)
            .strength(12.0f, 150.0f)
            .harvestTool(ToolType.PICKAXE)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 0)
            .dynamicShape()
            .sound(SoundType.STONE);
    public ConcereteIronDefeat() {
        super(PROPERTIES);
    }
}
