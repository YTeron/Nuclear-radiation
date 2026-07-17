package net.yteron.nucrad.block.machine.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class GrowChamberRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<GrowChamberRecipe> {

    @Override
    public GrowChamberRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        // Читаем ингредиент
        Ingredient ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "ingredient"));

        // Читаем результат
        ItemStack output = ItemStack.EMPTY;
        if (json.has("result")) {
            JsonObject resultObject = JSONUtils.getAsJsonObject(json, "result");
            String itemName = JSONUtils.getAsString(resultObject, "item");
            int count = JSONUtils.getAsInt(resultObject, "count", 1);
            // TODO: Получить Item из itemName
        }

        // Читаем время роста (опционально)
        int growthTime = JSONUtils.getAsInt(json, "growthTime", 100);

        return new GrowChamberRecipe(recipeId, ingredient, output, growthTime);
    }

    @Nullable
    @Override
    public GrowChamberRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int growthTime = buffer.readInt();
        return new GrowChamberRecipe(recipeId, ingredient, output, growthTime);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, GrowChamberRecipe recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem());
        buffer.writeInt(recipe.getGrowthTime());
    }
}