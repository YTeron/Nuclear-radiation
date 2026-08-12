package net.yteron.nucrad.gui.tileentity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.yteron.nucrad.block.resipe.ConcereteMixerRecipe;
import net.yteron.nucrad.block.resipe.ModRecipeTypes;
import net.yteron.nucrad.gui.init.ModTileEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

public class CrusherTileEntiy extends TileEntity implements IRecipeHolder, ITickableTileEntity, IRecipeHelperPopulator
{
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private int cookingProgress;
    private int cookingTotalTime;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final IRecipeType<ConcereteMixerRecipe> recipeType;
    protected final IIntArray dataAccess = new IIntArray() {
        public int get(int p_221476_1_) {
            switch(p_221476_1_) {
                case 0:
                    return CrusherTileEntiy.this.cookingProgress;
                case 1:
                    return CrusherTileEntiy.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int p_221477_1_, int p_221477_2_) {
            switch(p_221477_1_) {
                case 0:
                    CrusherTileEntiy.this.cookingProgress = p_221477_2_;
                    break;
                case 1:
                    CrusherTileEntiy.this.cookingTotalTime = p_221477_2_;
            }

        }

        public int getCount() {
            return 2;
        }
    };
    public CrusherTileEntiy(TileEntityType<?> tileEntityType, IRecipeType<ConcereteMixerRecipe> recipeType) {
        super(tileEntityType);
        this.recipeType = recipeType;
    }
    public CrusherTileEntiy() {
        this(ModTileEntities.A_SIMPLE_TILE.get(), ModRecipeTypes.LIGHTNING_RECIPE);
    }
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (slot == 0 || slot == 1) {
                    cookingProgress = 0;
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };
    }
    public IIntArray getDataAccess() {
        return dataAccess;
    }
    @Nullable
    private IRecipe<?> currentRecipe; // Храним текущий рецепт

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        this.currentRecipe = recipe; // Сохраняем использованный рецепт
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return this.currentRecipe; // Возвращаем сохраненный рецепт
    }
    public void awardUsedRecipes(PlayerEntity player) {
        if (this.currentRecipe != null) {
            player.awardRecipes(Arrays.asList(this.currentRecipe));
            this.currentRecipe = null; // Очищаем после выдачи
        }
    }
    public boolean canCraft() {
        if (itemHandler.getStackInSlot(0).isEmpty() || itemHandler.getStackInSlot(1).isEmpty()) {
            return false;
        }
        Inventory inv = new Inventory(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Optional<ConcereteMixerRecipe> recipe = this.level.getRecipeManager()
                .getRecipeFor((IRecipeType<ConcereteMixerRecipe>) ModRecipeTypes.LIGHTNING_RECIPE, inv, this.level)
                .map(r -> (ConcereteMixerRecipe) r);

        if (!recipe.isPresent()) {
            System.out.println("not resipe");
            return false;

        }

        ItemStack result = recipe.get().getResultItem();
        ItemStack outputSlot = itemHandler.getStackInSlot(2);

        if (outputSlot.isEmpty()) {
            return true;
        }

        if (!ItemStack.isSame(outputSlot, result)) {
            return false;
        }

        return outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize();
    }
    public void craft() {
        Inventory inv = new Inventory(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Optional<ConcereteMixerRecipe> recipe = this.level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.LIGHTNING_RECIPE, inv, this.level);

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem();
            craftTheItem(output);
            setChanged();
        });

    }

    private void craftTheItem(ItemStack output) {
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);
        itemHandler.insertItem(2, output, false);
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        if (canCraft()) {
            cookingProgress++;
            System.out.println("Progress: " + cookingProgress + "/" + cookingTotalTime);
            if (cookingProgress >= cookingTotalTime) {
                cookingProgress = 0;
                craft();
                setChanged();
            }
        } else {
            if (cookingProgress > 0) {
                cookingProgress = 0;
                setChanged();
            }
        }
    }
    @Override
    public void fillStackedContents(RecipeItemHelper p_194018_1_) {

    }
    // ✅ Сохранение данных (исправлено)
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
        this.cookingProgress = nbt.getInt("CookTime");
        this.cookingTotalTime = nbt.getInt("CookTimeTotal");
        CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");
        for (String s : compoundnbt.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.put("Inventory", itemHandler.serializeNBT());
        nbt.putInt("CookTime", this.cookingProgress);
        nbt.putInt("CookTimeTotal", this.cookingTotalTime);

        // ✅ Исправлено: сохраняем использованные рецепты
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipesUsed.forEach((id, count) -> {
            compoundnbt.putInt(id.toString(), count);
        });
        nbt.put("RecipesUsed", compoundnbt);

        return nbt;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }
}
