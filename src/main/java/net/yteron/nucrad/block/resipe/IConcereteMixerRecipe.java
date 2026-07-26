package net.yteron.nucrad.block.resipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.yteron.nucrad.NucRad;

import java.rmi.registry.Registry;

public interface IConcereteMixerRecipe extends IRecipe<IInventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(NucRad.MOD_ID, "lightning");
    IRecipeType<IConcereteMixerRecipe> RECIPE_TYPE = IRecipeType.register(TYPE_ID.toString());

    @Override
    default IRecipeType<?> getType() {
        return RECIPE_TYPE;
    }
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }
    @Override
    default boolean isSpecial() {
        return true;
    }

}
