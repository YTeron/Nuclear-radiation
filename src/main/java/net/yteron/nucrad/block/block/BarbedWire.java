package net.yteron.nucrad.block.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BarbedWire extends Block {
    private static final VoxelShape SHAPE = VoxelShapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    private static final VoxelShape EMPTY_SHAPE = VoxelShapes.empty();
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world,
                                        BlockPos pos, ISelectionContext context) {
        return EMPTY_SHAPE;  // Сущности проходят сквозь
    }
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

    public BarbedWire() {
        super(PROPERTIES);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                        .setValue(BlockStateProperties.WEST, false)
                        .setValue(BlockStateProperties.EAST, false)
                        .setValue(BlockStateProperties.NORTH, false)
                        .setValue(BlockStateProperties.SOUTH, false)
        );
    }
    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        // Замедление как у паутины
        if (entity instanceof LivingEntity) {
            // Устанавливаем скорость как в паутине (0.25 - как у паутины)
            entity.makeStuckInBlock(state, new Vector3d(0.25D, 0.05D, 0.25D));
            entity.hurt(DamageSource.CACTUS, 3.0f);
        }
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(
                BlockStateProperties.HORIZONTAL_FACING,
                BlockStateProperties.WEST,
                BlockStateProperties.EAST,
                BlockStateProperties.NORTH,
                BlockStateProperties.SOUTH
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IWorld world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection().getOpposite();

        // ПРОВЕРЯЕМ ТОЛЬКО НУЖНЫЕ СОЕДИНЕНИЯ
        boolean west = false;
        boolean east = false;
        boolean north = false;
        boolean south = false;

        switch (facing) {
            case NORTH:
                west = canConnect(world.getBlockState(pos.west()));
                east = canConnect(world.getBlockState(pos.east()));
                // north и south не проверяем
                break;
            case EAST:
                north = canConnect(world.getBlockState(pos.north()));
                south = canConnect(world.getBlockState(pos.south()));
                // west и east не проверяем
                break;
            case SOUTH:
                west = canConnect(world.getBlockState(pos.west()));
                east = canConnect(world.getBlockState(pos.east()));
                // north и south не проверяем
                break;
            case WEST:
                north = canConnect(world.getBlockState(pos.north()));
                south = canConnect(world.getBlockState(pos.south()));
                // west и east не проверяем
                break;
            default:
                break;
        }

        return this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, facing)
                .setValue(BlockStateProperties.WEST, west)
                .setValue(BlockStateProperties.EAST, east)
                .setValue(BlockStateProperties.NORTH, north)
                .setValue(BlockStateProperties.SOUTH, south);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction,
                                  BlockState neighborState, IWorld world,
                                  BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        boolean connected = canConnect(neighborState);

        // ОБНОВЛЯЕМ ТОЛЬКО НУЖНЫЕ СОЕДИНЕНИЯ
        switch (facing) {
            case NORTH:
            case SOUTH:
                if (direction == Direction.WEST) {
                    return state.setValue(BlockStateProperties.WEST, connected);
                } else if (direction == Direction.EAST) {
                    return state.setValue(BlockStateProperties.EAST, connected);
                }
                break;
            case EAST:
            case WEST:
                if (direction == Direction.NORTH) {
                    return state.setValue(BlockStateProperties.NORTH, connected);
                } else if (direction == Direction.SOUTH) {
                    return state.setValue(BlockStateProperties.SOUTH, connected);
                }
                break;
            default:
                break;
        }
        return state;
    }

    public boolean canConnect(BlockState state) {
        return state.getBlock() == this;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world,
                               BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;  // Пропускает свет
    }
}