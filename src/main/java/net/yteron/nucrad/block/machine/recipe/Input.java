package net.yteron.nucrad.block.machine.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class Input implements IInventory {
    private ItemStack inputItem;   // ← УБРАЛИ final
    private ItemStack inputItem2;  // ← УБРАЛИ final

    public Input(ItemStack inputItem, ItemStack inputItem2) {
        this.inputItem = inputItem;
        this.inputItem2 = inputItem2;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return this.inputItem.isEmpty() && this.inputItem2.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == 0) {
            return this.inputItem;
        }
        if (slot == 1) {
            return this.inputItem2;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == 0 && !this.inputItem.isEmpty()) {
            ItemStack removed = this.inputItem.copy();
            this.inputItem.shrink(amount);
            return removed;
        }
        if (slot == 1 && !this.inputItem2.isEmpty()) {
            ItemStack removed = this.inputItem2.copy();
            this.inputItem2.shrink(amount);
            return removed;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == 0 && !this.inputItem.isEmpty()) {
            ItemStack removed = this.inputItem.copy();
            this.inputItem.setCount(0);
            return removed;
        }
        if (slot == 1 && !this.inputItem2.isEmpty()) {
            ItemStack removed = this.inputItem2.copy();
            this.inputItem2.setCount(0);
            return removed;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) {
            this.inputItem = stack;
        }
        if (slot == 1) {
            this.inputItem2 = stack;
        }
    }

    @Override
    public void setChanged() {
        // Можно оставить пустым
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.inputItem.setCount(0);
        this.inputItem2.setCount(0);
    }
}