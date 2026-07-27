package net.yteron.nucrad.gui.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.yteron.nucrad.block.resipe.ConcereteMixerRecipe;
import net.yteron.nucrad.gui.init.ModContainers;

import javax.annotation.Nullable;

public class ASimpleContainer extends Container {

    private final TileEntity tileEntity;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;
    private final IIntArray data;


    public ASimpleContainer(int windowId, World world, BlockPos pos,
                            PlayerInventory playerInventory, PlayerEntity player, IIntArray data) {
        super(ModContainers.CONCERETE_CONTAINER.get(), windowId);
        this.data = data;
        this.tileEntity = world.getBlockEntity(pos); // ✅ Исправлено
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.addDataSlots(data);


        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 53, 32));
                addSlot(new SlotItemHandler(h, 1, 53, 54));
                addSlot(new SlotItemHandler(h, 2, 106, 41));
            });
        }

        // ✅ Слоты игрока
        layoutPlayerInventorySlots(8, 86);
    }
    public ASimpleContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        this(windowId, playerInventory.player.level, data.readBlockPos(),
                playerInventory, playerInventory.player, new IntArray(4));
    }

    // ✅ Конструктор для быстрого создания (без data)
    public ASimpleContainer(int windowId, World world, BlockPos pos,
                            PlayerInventory playerInventory, PlayerEntity player) {
        this(windowId, world, pos, playerInventory, player, new IntArray(2));
    }
    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Инвентарь игрока (9-35)
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Горячая панель (0-8)
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }
    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 3;

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) { // ✅ Исправлено имя метода
        Slot sourceSlot = this.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Если слот из инвентаря игрока
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!this.moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }
        // Если слот из TileEntity
        else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!this.moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX,
                    VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }
    @Override
    public boolean stillValid(PlayerEntity player) {
        return tileEntity != null && !tileEntity.isRemoved() &&
                player.distanceToSqr(tileEntity.getBlockPos().getX() + 0.5D,
                        tileEntity.getBlockPos().getY() + 0.5D,
                        tileEntity.getBlockPos().getZ() + 0.5D) <= 64.0D;
    }
    @OnlyIn(Dist.CLIENT)
    public int getCookProgress() {
        int cookTime = this.data.get(0);
        int totalTime = this.data.get(1);
        if (totalTime == 0) {
            totalTime = 200;
        }
        // 24 - ширина стрелки на текстуре
        return cookTime * 24 / totalTime;
    }
    @OnlyIn(Dist.CLIENT)
    public int getProgressPercent() {
        int cookTime = this.data.get(0);
        int totalTime = this.data.get(1);
        if (totalTime == 0) {
            totalTime = 200;
        }
        // Проценты от 0 до 100
        return cookTime * 100 / totalTime;
    }

    // ✅ НОВЫЙ МЕТОД: для квадрата (0-15)
    @OnlyIn(Dist.CLIENT)
    public int getProgressHeight() {
        int cookTime = this.data.get(0);
        int totalTime = this.data.get(1);
        if (totalTime == 0) {
            totalTime = 200;
        }
        // Высота от 0 до 15
        return cookTime * 17 / totalTime;
    }
}
