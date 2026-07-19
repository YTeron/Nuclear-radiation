package net.yteron.nucrad.block.resipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.yteron.nucrad.init.Modblock;

public class ConcereteMixerRecipe extends AbstractCookingRecipe {
    public ConcereteMixerRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(IModRecipeSerializer.CONCERETE_MIXER_RECIPE_I_RECIPE_TYPE,
                id, group, ingredient, result, experience, cookingTime);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(Modblock.CONCERETE_MIXER.get());
    }

    public IRecipeSerializer<?> getSerializer() {
        return IRecipeSerializer.SMELTING_RECIPE;
    }
    public int getCookingTime() {
        return this.cookingTime;
    }
}
