package net.yteron.nucrad.block.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

public class IronFencee extends GlassBlock {

    // ========================================
    // ФОРМЫ ДЛЯ РАЗНЫХ СОЕДИНЕНИЙ
    // ========================================

    // Центральный столб
    private static final VoxelShape CENTER = VoxelShapes.box(0.4D, 0.0D, 0.4D, 0.6D, 1.0D, 0.6D);

    // Одиночные соединения
    private static final VoxelShape NORTH = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.0D, 0.6D, 1.0D, 0.4D)
    );
    private static final VoxelShape SOUTH = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.6D, 0.6D, 1.0D, 1.0D)
    );
    private static final VoxelShape WEST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.0D, 0.0D, 0.4D, 0.4D, 1.0D, 0.6D)
    );
    private static final VoxelShape EAST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.6D, 0.0D, 0.4D, 1.0D, 1.0D, 0.6D)
    );

    // Двойные соединения (прямые)
    private static final VoxelShape NORTH_SOUTH = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.0D, 0.6D, 1.0D, 0.4D),
            VoxelShapes.box(0.4D, 0.0D, 0.6D, 0.6D, 1.0D, 1.0D)
    );
    private static final VoxelShape WEST_EAST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.0D, 0.0D, 0.4D, 0.4D, 1.0D, 0.6D),
            VoxelShapes.box(0.6D, 0.0D, 0.4D, 1.0D, 1.0D, 0.6D)
    );

    // Двойные соединения (угловые)
    private static final VoxelShape NORTH_WEST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.0D, 0.6D, 1.0D, 0.4D),
            VoxelShapes.box(0.0D, 0.0D, 0.4D, 0.4D, 1.0D, 0.6D)
    );
    private static final VoxelShape NORTH_EAST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.0D, 0.6D, 1.0D, 0.4D),
            VoxelShapes.box(0.6D, 0.0D, 0.4D, 1.0D, 1.0D, 0.6D)
    );
    private static final VoxelShape SOUTH_WEST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.6D, 0.6D, 1.0D, 1.0D),
            VoxelShapes.box(0.0D, 0.0D, 0.4D, 0.4D, 1.0D, 0.6D)
    );
    private static final VoxelShape SOUTH_EAST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.6D, 0.6D, 1.0D, 1.0D),
            VoxelShapes.box(0.6D, 0.0D, 0.4D, 1.0D, 1.0D, 0.6D)
    );

    // Тройные соединения
    private static final VoxelShape NORTH_WEST_EAST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.0D, 0.6D, 1.0D, 0.4D),
            VoxelShapes.box(0.0D, 0.0D, 0.4D, 0.4D, 1.0D, 0.6D),
            VoxelShapes.box(0.6D, 0.0D, 0.4D, 1.0D, 1.0D, 0.6D)
    );
    private static final VoxelShape SOUTH_WEST_EAST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.6D, 0.6D, 1.0D, 1.0D),
            VoxelShapes.box(0.0D, 0.0D, 0.4D, 0.4D, 1.0D, 0.6D),
            VoxelShapes.box(0.6D, 0.0D, 0.4D, 1.0D, 1.0D, 0.6D)
    );
    private static final VoxelShape NORTH_SOUTH_WEST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.0D, 0.6D, 1.0D, 0.4D),
            VoxelShapes.box(0.4D, 0.0D, 0.6D, 0.6D, 1.0D, 1.0D),
            VoxelShapes.box(0.0D, 0.0D, 0.4D, 0.4D, 1.0D, 0.6D)
    );
    private static final VoxelShape NORTH_SOUTH_EAST = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.0D, 0.6D, 1.0D, 0.4D),
            VoxelShapes.box(0.4D, 0.0D, 0.6D, 0.6D, 1.0D, 1.0D),
            VoxelShapes.box(0.6D, 0.0D, 0.4D, 1.0D, 1.0D, 0.6D)
    );

    // Четыре соединения (все стороны)
    private static final VoxelShape ALL = VoxelShapes.or(
            CENTER,
            VoxelShapes.box(0.4D, 0.0D, 0.0D, 0.6D, 1.0D, 0.4D),
            VoxelShapes.box(0.4D, 0.0D, 0.6D, 0.6D, 1.0D, 1.0D),
            VoxelShapes.box(0.0D, 0.0D, 0.4D, 0.4D, 1.0D, 0.6D),
            VoxelShapes.box(0.6D, 0.0D, 0.4D, 1.0D, 1.0D, 0.6D)
    );

    private static final Properties PROPERTIES = Properties.copy(Blocks.GLASS)
            .harvestLevel(1)
            .strength(3.0f, 4.0f)
            .harvestTool(ToolType.PICKAXE)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .isViewBlocking((state, reader, pos) -> false)
            .isRedstoneConductor((state, reader, pos) -> false)
            .dynamicShape()
            .sound(SoundType.METAL);

    public IronFencee() {
        super(PROPERTIES);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(BlockStateProperties.WEST, false)
                        .setValue(BlockStateProperties.EAST, false)
                        .setValue(BlockStateProperties.NORTH, false)
                        .setValue(BlockStateProperties.SOUTH, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(
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

        boolean west = canConnect(world.getBlockState(pos.west()));
        boolean east = canConnect(world.getBlockState(pos.east()));
        boolean north = canConnect(world.getBlockState(pos.north()));
        boolean south = canConnect(world.getBlockState(pos.south()));

        return this.defaultBlockState()
                .setValue(BlockStateProperties.WEST, west)
                .setValue(BlockStateProperties.EAST, east)
                .setValue(BlockStateProperties.NORTH, north)
                .setValue(BlockStateProperties.SOUTH, south);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction,
                                  BlockState neighborState, IWorld world,
                                  BlockPos pos, BlockPos neighborPos) {
        boolean connected = canConnect(neighborState);

        switch (direction) {
            case WEST: return state.setValue(BlockStateProperties.WEST, connected);
            case EAST: return state.setValue(BlockStateProperties.EAST, connected);
            case NORTH: return state.setValue(BlockStateProperties.NORTH, connected);
            case SOUTH: return state.setValue(BlockStateProperties.SOUTH, connected);
            default: return state;
        }
    }

    public boolean canConnect(BlockState state) {
        return state.getBlock() == this ||
                state.isSolidRender(null,null) ||
                state.isCollisionShapeFullBlock(null, null);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world,
                               BlockPos pos, ISelectionContext context) {
        boolean west = state.getValue(BlockStateProperties.WEST);
        boolean east = state.getValue(BlockStateProperties.EAST);
        boolean north = state.getValue(BlockStateProperties.NORTH);
        boolean south = state.getValue(BlockStateProperties.SOUTH);

        // Определяем форму в зависимости от количества и направления соединений
        if (north && south && west && east) {
            return ALL;  // 4 соединения
        } else if (north && south && west) {
            return NORTH_SOUTH_WEST;  // 3 соединения (без востока)
        } else if (north && south && east) {
            return NORTH_SOUTH_EAST;  // 3 соединения (без запада)
        } else if (north && west && east) {
            return NORTH_WEST_EAST;  // 3 соединения (без юга)
        } else if (south && west && east) {
            return SOUTH_WEST_EAST;  // 3 соединения (без севера)
        } else if (north && west) {
            return NORTH_WEST;  // 2 соединения (угол: север + запад)
        } else if (north && east) {
            return NORTH_EAST;  // 2 соединения (угол: север + восток)
        } else if (south && west) {
            return SOUTH_WEST;  // 2 соединения (угол: юг + запад)
        } else if (south && east) {
            return SOUTH_EAST;  // 2 соединения (угол: юг + восток)
        } else if (north && south) {
            return NORTH_SOUTH;  // 2 соединения (прямо: север + юг)
        } else if (west && east) {
            return WEST_EAST;  // 2 соединения (прямо: запад + восток)
        } else if (north) {
            return NORTH;  // 1 соединение (север)
        } else if (south) {
            return SOUTH;  // 1 соединение (юг)
        } else if (west) {
            return WEST;  // 1 соединение (запад)
        } else if (east) {
            return EAST;  // 1 соединение (восток)
        } else {
            return CENTER;  // Нет соединений
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

}