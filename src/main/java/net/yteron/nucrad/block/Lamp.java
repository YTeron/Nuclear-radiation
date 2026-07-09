package net.yteron.nucrad.block;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class Lamp extends Block {
    // Формы для разных направлений
    private static final VoxelShape SHAPE_LOWER = VoxelShapes.box(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);

    // Верхняя часть - только верхняя половина
    private static final VoxelShape SHAPE_UPPER = VoxelShapes.box(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final Properties PROPERTIES = Properties.copy(Blocks.IRON_BLOCK)
            .harvestLevel(1)
            .strength(3.0f, 4.0f)
            .harvestTool(ToolType.PICKAXE)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel((state) -> 15)
            .dynamicShape()
            .sound(SoundType.METAL);

    public Lamp() {
        super(PROPERTIES);

        // Регистрируем стандартное состояние блока
        // По умолчанию: смотрит на север, нижняя половина
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                        .setValue(HALF, DoubleBlockHalf.LOWER)  // По умолчанию нижняя часть
        );
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        // Добавляем все свойства, которые могут меняться у блока
        builder.add(
                BlockStateProperties.HORIZONTAL_FACING,  // Направление (север/юг/запад/восток)
                HALF                                    // Половина (нижняя/верхняя)
        );
    }
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Получаем позицию, куда игрок ставит блок
        BlockPos pos = context.getClickedPos();

        // Проверяем, что место для верхней части свободно
        // (y < 255 - чтобы не выйти за пределы мира)
        if (pos.getY() < 255 && context.getLevel().getBlockState(pos.above()).canBeReplaced(context)) {

            // Возвращаем состояние для НИЖНЕЙ части
            return this.defaultBlockState()
                    .setValue(HALF, DoubleBlockHalf.LOWER)  // Это нижняя часть
                    .setValue(BlockStateProperties.HORIZONTAL_FACING,
                            context.getHorizontalDirection().getOpposite()); // Поворачиваем к игроку
        }

        // Если место занято - блок не ставится
        return null;
    }

    // ========================================
    // 7. УСТАНОВКА ВЕРХНЕЙ ЧАСТИ
    // ========================================

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        // Когда блок поставлен, автоматически ставим верхнюю часть
        world.setBlock(
                pos.above(),  // Позиция на 1 блок выше
                state.setValue(HALF, DoubleBlockHalf.UPPER),  // Это верхняя часть
                3  // Флаг обновления (3 = обновить блок и соседей)
        );
    }

    // ========================================
    // 8. ОБНОВЛЕНИЕ СОСЕДЕЙ
    // ========================================

    @Override
    public BlockState updateShape(BlockState state, Direction direction,
                                  BlockState neighborState, net.minecraft.world.IWorld world,
                                  BlockPos pos, BlockPos neighborPos) {

        // Получаем, какая это половина (нижняя или верхняя)
        DoubleBlockHalf half = state.getValue(HALF);

        // Если изменение происходит по вертикали (вверх/вниз)
        // и это связь между нижней и верхней частью
        if (direction.getAxis() == Direction.Axis.Y &&
                half == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {

            // Проверяем, что соседний блок - это такая же лампа и у него противоположная половина
            if (neighborState.is(this) && neighborState.getValue(HALF) != half) {
                // Если всё правильно - синхронизируем направление
                return state.setValue(BlockStateProperties.HORIZONTAL_FACING,
                        neighborState.getValue(BlockStateProperties.HORIZONTAL_FACING));
            } else {
                // Если соседняя часть пропала - удаляем блок (превращаем в воздух)
                return Blocks.AIR.defaultBlockState();
            }
        }

        // Если нижняя часть потеряла опору снизу - удаляем блок
        if (half == DoubleBlockHalf.LOWER && direction == Direction.DOWN &&
                !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    // ========================================
    // 9. ПРОВЕРКА ВОЗМОЖНОСТИ СУЩЕСТВОВАНИЯ
    // ========================================

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = world.getBlockState(below);

        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            // Нижняя часть должна стоять на твёрдом блоке
            return belowState.isFaceSturdy(world, below, Direction.UP);
        } else {
            // Верхняя часть должна иметь снизу такую же лампу
            return belowState.is(this);
        }
    }

    // ========================================
    // 10. ОБРАБОТКА РАЗРУШЕНИЯ В КРЕАТИВЕ
    // ========================================

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // Копия метода preventCreativeDropFromBottomPart
        if (!world.isClientSide && player.isCreative()) {
            // Получаем половину блока
            DoubleBlockHalf half = state.getValue(HALF);

            // Если это нижняя часть - удаляем верхнюю
            if (half == DoubleBlockHalf.LOWER) {
                BlockPos upperPos = pos.above();
                BlockState upperState = world.getBlockState(upperPos);

                // Проверяем, что верхняя часть - это наша лампа
                if (upperState.is(this) && upperState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    // Удаляем верхнюю часть без выпадения предметов
                    world.setBlock(upperPos, Blocks.AIR.defaultBlockState(), 35);
                    world.levelEvent(player, 2001, upperPos, Block.getId(upperState));
                }
            }
        }
        super.playerWillDestroy(world, pos, state, player);
    }

    // ========================================
    // 11. ФОРМА БЛОКА (ВИЗУАЛЬНАЯ)
    // ========================================

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world,
                               BlockPos pos, ISelectionContext context) {
        // В зависимости от половины - разная форма
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return SHAPE_LOWER;  // Нижняя часть (с подставкой)
        } else {
            return SHAPE_UPPER;  // Верхняя часть (только шар)
        }
    }

    // ========================================
    // 12. УРОВЕНЬ СВЕТА
    // ========================================

    @Override
    public int getLightValue(BlockState state, net.minecraft.world.IBlockReader world, BlockPos pos) {
        return 15;  // Максимальный свет
    }
}