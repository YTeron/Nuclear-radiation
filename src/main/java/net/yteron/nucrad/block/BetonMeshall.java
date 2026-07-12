package net.yteron.nucrad.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraftforge.common.ToolType;

public class BetonMeshall extends Block {
    private static final AbstractBlock.Properties PROPERTIES = AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(2)
            .strength(10.0f, 6.0f)
            .harvestTool(ToolType.PICKAXE)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 0)
            .dynamicShape()
            .sound(SoundType.METAL);

    public BetonMeshall() {
        super(PROPERTIES);
    }
}
