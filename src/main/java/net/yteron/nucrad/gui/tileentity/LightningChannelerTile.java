package net.yteron.nucrad.gui.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import net.yteron.nucrad.gui.init.ModTileEntities;
import net.yteron.nucrad.init.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LightningChannelerTile extends TileEntity {

    // Инвентарь на 2 слота
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    // Конструктор с параметром (для регистрации)
    public LightningChannelerTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    // Конструктор без параметров (для удобства)
    public LightningChannelerTile() {
        this(ModTileEntities.LIGHTNING_CHANNELER_TILE.get());
    }

    // Создание инвентаря
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged(); // ✅ В 1.16.5 используется setChanged()
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: // Слот для стекла
                        return stack.getItem() == Items.GLASS_PANE;
                    case 1: // Слот для аметиста или огненного камня
                        return stack.getItem() == ModItems.CONCERETE_DEFEAT.get();
                    default:
                        return false;
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1; // Ограничиваем 1 предметом в слоте
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) {
                    return stack; // Нельзя вставить неподходящий предмет
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    // ✅ Сохранение данных (исправлено)
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
    }

    // ✅ Загрузка данных (исправлено)
    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.put("Inventory", itemHandler.serializeNBT());
        return compound;
    }

    // ✅ Capability для взаимодействия с инвентарём
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    // ✅ Очистка при удалении
    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    // ✅ Метод, вызываемый при ударе молнии
    public void lightningHasStruck() {
        // Проверяем, есть ли стекло в слоте 0
        boolean hasGlass = itemHandler.getStackInSlot(0).getCount() > 0
                && itemHandler.getStackInSlot(0).getItem() == Items.GLASS_PANE;

        // Проверяем, есть ли аметист в слоте 1
        boolean hasAmethyst = itemHandler.getStackInSlot(1).getCount() > 0
                && itemHandler.getStackInSlot(1).getItem() == ModItems.CONCERETE_DEFEAT.get();

        // Если оба предмета есть - создаём огненный камень
        if (hasGlass && hasAmethyst) {
            // Забираем стекло
            itemHandler.getStackInSlot(0).shrink(1);

            // Забираем аметист
            itemHandler.getStackInSlot(1).shrink(1);

            // Кладём огненный камень
            itemHandler.insertItem(1, new ItemStack(ModItems.CONCP.get()), false);

            // Отмечаем изменения
            setChanged();
        }
    }

    // ✅ Тик-метод (если нужен автоматический процесс)
    public void tick() {
        if (level == null || level.isClientSide) return;

        // Здесь можно добавить логику, если нужно что-то делать каждый тик
        // Например, проверять погоду или наличие молнии
    }

    // Геттер для прогресса (если нужен GUI)
    public int getProgress() {
        // Если есть стекло и аметист - прогресс 100%
        boolean hasGlass = itemHandler.getStackInSlot(0).getCount() > 0
                && itemHandler.getStackInSlot(0).getItem() == Items.GLASS_PANE;

        boolean hasAmethyst = itemHandler.getStackInSlot(1).getCount() > 0;

        return (hasGlass && hasAmethyst) ? 100 : 0;
    }
}