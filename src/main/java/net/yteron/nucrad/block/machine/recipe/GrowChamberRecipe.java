package net.yteron.nucrad.block.machine.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GrowChamberRecipe implements IRecipe<Input> {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack output;
    private final int growthTime;  // Время роста в тиках

    // КОНСТРУКТОР
    public GrowChamberRecipe(ResourceLocation id, Ingredient ingredient, ItemStack output, int growthTime) {
        this.id = id;
        this.ingredient = ingredient;
        this.output = output;
        this.growthTime = growthTime;
    }

    // Альтернативный конструктор (если нужен только Ingredient)
    public GrowChamberRecipe(Ingredient ingredient) {
        this.id = new ResourceLocation("nucrad", "grow_chamber");
        this.ingredient = ingredient;
        this.output = ItemStack.EMPTY;
        this.growthTime = 100;
    }

    @Override
    public boolean matches(Input inventory, World world) {
        // Проверяем, совпадает ли предмет в первом слоте с ингредиентом
        return this.ingredient.test(inventory.getItem(0));
    }

    @Override
    public ItemStack assemble(Input inventory) {
        // Возвращаем результат крафта
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.GROWTH_SERIALIZER.get();  // Нужно зарегистрировать
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.GROWTH_TYPE;  // Нужно зарегистрировать
    }

    // Геттер для времени роста
    public int getGrowthTime() {
        return this.growthTime;
    }

    // Геттер для ингредиента (если нужен)
    public Ingredient getIngredient() {
        return this.ingredient;
    }
}