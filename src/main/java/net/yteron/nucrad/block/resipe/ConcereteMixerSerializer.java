package net.yteron.nucrad.block.resipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

public class ConcereteMixerSerializer<T extends AbstractCookingRecipe>extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>{
    private final int defaultCookingTime;
    private final ConcereteMixerSerializer.IFactory<T> factory;

    public ConcereteMixerSerializer(ConcereteMixerSerializer.IFactory<T> p_i50025_1_, int p_i50025_2_) {
        this.defaultCookingTime = p_i50025_2_;
        this.factory = p_i50025_1_;
    }

    @Override
    public T fromJson(ResourceLocation p_199425_1_, JsonObject p_199425_2_) {
        String s = JSONUtils.getAsString(p_199425_2_, "group", "");
        JsonElement jsonelement = (JsonElement)(JSONUtils.isArrayNode(p_199425_2_, "ingredient") ? JSONUtils.getAsJsonArray(p_199425_2_, "ingredient") : JSONUtils.getAsJsonObject(p_199425_2_, "ingredient"));
        Ingredient ingredient = Ingredient.fromJson(jsonelement);
        //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
        if (!p_199425_2_.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        ItemStack itemstack;
        if (p_199425_2_.get("result").isJsonObject()) itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "result"));
        else {
            String s1 = JSONUtils.getAsString(p_199425_2_, "result");
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                return new IllegalStateException("Item: " + s1 + " does not exist");
            }));
        }
        float f = JSONUtils.getAsFloat(p_199425_2_, "experience", 0.0F);
        int i = JSONUtils.getAsInt(p_199425_2_, "cookingtime", this.defaultCookingTime);
        return this.factory.create(p_199425_1_, s, ingredient, itemstack, f, i);
    }

    @Nullable
    @Override
    public T fromNetwork(ResourceLocation id, PacketBuffer buffer) {
        String s = buffer.readUtf(32767);
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack itemstack = buffer.readItem();
        float f = buffer.readFloat();
        int i = buffer.readVarInt();
        return this.factory.create(id, s, ingredient, itemstack, f, i);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, T recipe) {
        buffer.writeUtf(recipe.getGroup());
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem());
        buffer.writeFloat(recipe.getExperience());
        buffer.writeVarInt(recipe.getCookingTime());
    }
    interface IFactory<T extends AbstractCookingRecipe> {
        T create(ResourceLocation p_create_1_, String p_create_2_, Ingredient p_create_3_, ItemStack p_create_4_, float p_create_5_, int p_create_6_);
    }
}
