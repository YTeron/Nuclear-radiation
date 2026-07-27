package net.yteron.nucrad.block.resipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class ConcereteMixerRecipe implements IConcereteMixerRecipe{
    protected final IRecipeType<?> type;
    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient ingredient;
    protected final Ingredient tingredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;

    public ConcereteMixerRecipe(IRecipeType<?> type, ResourceLocation id, String group, Ingredient ingredient, Ingredient tingredient, ItemStack result, float experience, int cookingTime) {
        this.type = type;
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.tingredient = tingredient;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(IInventory inv, World world) {

        ItemStack slot0 = inv.getItem(0);
        ItemStack slot1 = inv.getItem(1);
        boolean match1 = this.ingredient.test(slot0);
        boolean match2 = this.tingredient.test(slot1);
        return match1 && match2;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return this.result.copy();
    }
    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        nonnulllist.add(this.tingredient);
        return nonnulllist;
    }
    public float getExperience() {
            return this.experience;
        }

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }
    public String getGroup() {
        return this.group;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeType<?> getType() {
        return this.type;
    }
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.LIGHTNING_SERIALIZER.get();
    }
    public static class ConcereteMixerRecipeType implements IRecipeType<ConcereteMixerRecipe>{
        @Override
        public String toString() {
            return ConcereteMixerRecipe.TYPE_ID.toString();
        }
    }
    public static class Serealazire extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ConcereteMixerRecipe>{

        @Override
        public ConcereteMixerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            // 1. Читаем группу (необязательно)
            String group = JSONUtils.getAsString(json, "group", "");

            // 2. Читаем ингредиенты (массив из 2 элементов)
            JsonArray ingredientsArray = JSONUtils.getAsJsonArray(json, "ingredients");
            Ingredient ingredient1 = Ingredient.fromJson(ingredientsArray.get(0));
            Ingredient ingredient2 = Ingredient.fromJson(ingredientsArray.get(1));

            // 3. Читаем результат
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));

            // 4. Читаем опыт и время
            float experience = JSONUtils.getAsFloat(json, "experience", 0.0F);
            int cookingTime = JSONUtils.getAsInt(json, "cookingtime", 200);

            // 5. Создаем и возвращаем рецепт
            return new ConcereteMixerRecipe(
                    ModRecipeTypes.LIGHTNING_RECIPE,  // тип
                    recipeId,                               // ID
                    group,                                  // группа
                    ingredient1,                            // ингредиент 1
                    ingredient2,                            // ингредиент 2
                    output,                                 // результат
                    experience,                             // опыт
                    cookingTime                             // время
            );
        }

        @Nullable
        @Override
        public ConcereteMixerRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readUtf(32767);

            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            Ingredient ingredient2 = Ingredient.fromNetwork(buffer);

            ItemStack output = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();

            return new ConcereteMixerRecipe(
                    ModRecipeTypes.LIGHTNING_RECIPE,
                    recipeId,
                    group,
                    ingredient1,
                    ingredient2,
                    output,
                    experience,
                    cookingTime
            );
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ConcereteMixerRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());

            recipe.ingredient.toNetwork(buffer);
            recipe.tingredient.toNetwork(buffer);

            buffer.writeItem(recipe.getResultItem());

            buffer.writeFloat(recipe.getExperience());
            buffer.writeVarInt(recipe.getCookingTime());
        }
    }
}
