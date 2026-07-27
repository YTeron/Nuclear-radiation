package net.yteron.nucrad.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import net.yteron.nucrad.block.resipe.ModRecipeTypes;
import net.yteron.nucrad.gui.container.ASimpleContainer;
import net.yteron.nucrad.gui.init.ModTileEntities;
import net.yteron.nucrad.gui.tileentity.ASimleTileEntity;


import javax.annotation.Nullable;

public class ConcereteMixer extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(0)
            .strength(4.0f, 6.0f)
            .harvestTool(ToolType.SHOVEL)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 0)
            .dynamicShape()
            .sound(SoundType.METAL);

    public ConcereteMixer() {
        super(PROPERTIES);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ASimleTileEntity(ModTileEntities.A_SIMPLE_TILE.get(), ModRecipeTypes.LIGHTNING_RECIPE);
    }

    // ✅ Говорим, что у блока есть TileEntity
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    // ✅ Открывает GUI при ПКМ
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos,
                                PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof ASimleTileEntity) {
                ASimleTileEntity te = (ASimleTileEntity) tileEntity;

                NetworkHooks.openGui((ServerPlayerEntity) player,
                        new INamedContainerProvider() {
                            @Override
                            public ITextComponent getDisplayName() {
                                return new StringTextComponent("Concrete Mixer");
                            }

                            @Override
                            public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
                                // ✅ Передаем dataAccess
                                return new ASimpleContainer(id, world, pos, inventory, player, te.getDataAccess());
                            }
                        },
                        pos
                );
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}