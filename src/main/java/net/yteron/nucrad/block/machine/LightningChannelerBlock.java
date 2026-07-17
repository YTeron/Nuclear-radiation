package net.yteron.nucrad.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import net.yteron.nucrad.gui.container.LightningChannelerContainer;
import net.yteron.nucrad.gui.tileentity.LightningChannelerTile;

import javax.annotation.Nullable;

public class LightningChannelerBlock extends Block {
    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(0)
            .strength(4.0f, 6.0f)
            .harvestTool(ToolType.SHOVEL)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 0)
            .dynamicShape()
            .sound(SoundType.METAL);

    public LightningChannelerBlock() {
        super(PROPERTIES);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LightningChannelerTile();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos,
                                PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof LightningChannelerTile) {
                // ✅ Открываем GUI
                NetworkHooks.openGui((ServerPlayerEntity) player,
                        new INamedContainerProvider() {
                            @Override
                            public ITextComponent getDisplayName() {
                                return new StringTextComponent("Lightning Channeler");
                            }

                            @Override
                            public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
                                return new LightningChannelerContainer(id, world, pos, inventory, player);
                            }
                        },
                        pos
                );
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}