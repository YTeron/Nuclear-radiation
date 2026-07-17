package net.yteron.nucrad.block.machine.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;

public class ModRecipes {

    // Регистрация для рецептов
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NucRad.MOD_ID);

    // Тип рецепта
    public static IRecipeType<GrowChamberRecipe> GROWTH_TYPE =
            IRecipeType.register("nucrad:grow_chamber");

    // Сериализатор рецепта
    public static final RegistryObject<IRecipeSerializer<GrowChamberRecipe>> GROWTH_SERIALIZER =
            RECIPE_SERIALIZERS.register("grow_chamber",
                    () -> new GrowChamberRecipeSerializer());

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}