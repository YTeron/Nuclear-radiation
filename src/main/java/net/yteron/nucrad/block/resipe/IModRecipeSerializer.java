package net.yteron.nucrad.block.resipe;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.yteron.nucrad.NucRad;

import java.util.Optional;


public interface IModRecipeSerializer<T extends IRecipe<?>> {

    ResourceLocation TYPE_ID = new ResourceLocation(NucRad.MOD_ID, "concrete_mixer");
    IRecipeType<ConcereteMixerRecipe> CONCERETE_MIXER_RECIPE_I_RECIPE_TYPE = register("concrete_mixer");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String p_222147_0_) {
        return (IRecipeType)Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(p_222147_0_), new IRecipeType<T>() {
            public String toString() {
                return p_222147_0_;
            }
        });
    }

    default <C extends IInventory> Optional<IRecipe<C>> tryMatch(IRecipe<C> p_222148_1_, World p_222148_2_, C p_222148_3_) {
        return p_222148_1_.matches(p_222148_3_, p_222148_2_) ? Optional.of(p_222148_1_) : Optional.empty();
    }
}
