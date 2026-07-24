package net.yteron.nucrad.gui.tileentity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import net.yteron.nucrad.block.resipe.ConcereteMixerRecipe;
import net.yteron.nucrad.block.resipe.ModRecypeTypes;
import net.yteron.nucrad.gui.init.ModTileEntities;
import net.yteron.nucrad.init.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ASimpleTile extends TileEntity implements ISidedInventory, ITickableTileEntity {
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{2};
    // Инвентарь на 2 слота
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    protected final IRecipeType<ConcereteMixerRecipe> recipeType;
    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private int cookingProgress;
    private int cookingTotalTime;

    public ASimpleTile() {
        this(ModTileEntities.A_SIMPLE_TILE.get(), ModRecypeTypes.CONCRETE_MIXER);
    }

    public ASimpleTile(TileEntityType<?> tileEntityType, IRecipeType<ConcereteMixerRecipe> recipeType) {
        super(tileEntityType);
        this.cookingTotalTime = 200;
        this.recipeType = recipeType;
    }



    private ItemStackHandler createHandler() {
        return new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged(); // ✅ В 1.16.5()
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };
    }

    // ✅ Сохранение данных (исправлено)
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
        this.cookingProgress = nbt.getInt("CookTime");
        this.cookingTotalTime = nbt.getInt("CookTimeTotal");
        CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");
        for(String s : compoundnbt.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
    }

    // ✅ Загрузка данных (исправлено)
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.put("Inventory", itemHandler.serializeNBT());
        nbt.putInt("CookTime", this.cookingProgress);
        nbt.putInt("CookTimeTotal", this.cookingTotalTime);
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipesUsed.forEach((p_235643_1_, p_235643_2_) -> {
            compoundnbt.putInt(p_235643_1_.toString(), p_235643_2_);
        });
        nbt.put("RecipesUsed", compoundnbt);
        return nbt;
    }

    public void tick() {
        if (level == null || level.isClientSide) return;
        if(canSmelt()){
            this.cookingProgress++;
            if (this.cookingProgress >= this.cookingTotalTime) {
                this.cookingProgress = 0;
                this.cookingTotalTime = this.getTotalCookTime();
                this.smeltItem();
                this.setChanged();
            }
        } else {
            this.cookingProgress = 0;
        }
    }
    protected boolean canSmelt() {
        if (this.items.get(0).isEmpty()) {
            return false;
        }
        if (this.items.get(1).isEmpty()) {
            return false;
        }

        // Проверяем рецепт
        ConcereteMixerRecipe recipe = this.getRecipe();
        if (recipe == null) {
            return false;
        }

        // Проверяем выходной слот
        ItemStack result = recipe.getResultItem();
        ItemStack output = this.items.get(2);

        if (output.isEmpty()) {
            return true;
        }

        if (!output.sameItem(result)) {
            return false;
        }

        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    protected void smeltItem() {
        ConcereteMixerRecipe recipe = this.getRecipe();
        if (recipe == null) return;

        ItemStack input = this.items.get(0);
        ItemStack input2 = this.items.get(1);
        ItemStack output = this.items.get(2);
        ItemStack result = recipe.getResultItem();


        if (output.isEmpty()) {
            this.items.set(2, result.copy());
        } else {
            output.grow(result.getCount());
        }

        input.shrink(1);
        input2.shrink(1);
        this.setChanged();
    }

    protected ConcereteMixerRecipe getRecipe() {
        if (this.level == null) return null;

        return this.level.getRecipeManager()
                .getRecipeFor(this.recipeType, this, this.level)
                .orElse(null);
    }
    protected int getTotalCookTime() {
        if (this.level == null) {
            return 200;
        }

        return this.level.getRecipeManager()
                .getRecipeFor(this.recipeType, this, this.level)
                .map(ConcereteMixerRecipe::getCookingTime) // ✅ Свой метод
                .orElse(200);
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
    public int getProgress() {
        return this.cookingProgress;
    }
    @Override
    public int [] getSlotsForFace(Direction p_180463_1_){
        if (p_180463_1_ == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return p_180463_1_ == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }
    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_){
        return p_180462_1_ != 2;
    }
    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_){
        return true;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ItemStackHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                this.worldPosition.getX() + 0.5D,
                this.worldPosition.getY() + 0.5D,
                this.worldPosition.getZ() + 0.5D
        ) <= 64.0D;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 2) { // Выходной слот
            return false;
        }
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}