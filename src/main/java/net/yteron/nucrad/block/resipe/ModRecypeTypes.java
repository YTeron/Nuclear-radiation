package net.yteron.nucrad.block.resipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.yteron.nucrad.NucRad;




public class ModRecypeTypes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =

            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NucRad.MOD_ID);

    public static final IRecipeType<ConcereteMixerRecipe> CONCRETE_MIXER =
            IRecipeType.register(new ResourceLocation(NucRad.MOD_ID, "concrete_mixer").toString());


    public static final RegistryObject<IRecipeSerializer<ConcereteMixerRecipe>> CONCRETE_MIXER_SERIALIZER =
            RECIPE_SERIALIZER.register("concrete_mixer",
                    () -> new ConcereteMixerSerializer<>(ConcereteMixerRecipe::new, 200));



    public static void register(IEventBus eventBus){
        RECIPE_SERIALIZER.register(eventBus);
    }
}
