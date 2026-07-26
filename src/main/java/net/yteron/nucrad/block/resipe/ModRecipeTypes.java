package net.yteron.nucrad.block.resipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;

public class ModRecipeTypes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NucRad.MOD_ID);

    public static final RegistryObject<ConcereteMixerRecipe.Serealazire> LIGHTNING_SERIALIZER
            = RECIPE_SERIALIZER.register("lightning", ConcereteMixerRecipe.Serealazire::new);

    public static IRecipeType<ConcereteMixerRecipe> LIGHTNING_RECIPE
            = new ConcereteMixerRecipe.ConcereteMixerRecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, ConcereteMixerRecipe.TYPE_ID, LIGHTNING_RECIPE);
    }
}
