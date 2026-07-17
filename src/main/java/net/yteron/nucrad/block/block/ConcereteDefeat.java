package net.yteron.nucrad.block.block;

import net.minecraft.block.*;
import net.minecraftforge.common.ToolType;

public class ConcereteDefeat extends Block {
    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(2)
            .strength(12.0f, 100.0f)
            .harvestTool(ToolType.PICKAXE)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 0)
            .dynamicShape()
            .sound(SoundType.STONE);
    public ConcereteDefeat() {
        super(PROPERTIES);
    }
}
