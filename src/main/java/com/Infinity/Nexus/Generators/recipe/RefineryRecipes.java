package com.Infinity.Nexus.Generators.recipe;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.block.entity.RefineryBlockEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RefineryRecipes implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final FluidStack fluid;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int duration;
    private final int energy;

    public RefineryRecipes(NonNullList<Ingredient> inputItems,FluidStack fluid, ItemStack output, ResourceLocation id, int duration, int energy) {
        this.inputItems = inputItems;
        this.fluid = fluid;
        this.output = output;
        this.id = id;
        this.duration = duration;
        this.energy = energy;

    }


    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }
        int componentSlot = RefineryBlockEntity.getComponentSlot();
        ItemStack stack = pContainer.getItem(componentSlot);
        return (inputItems.get(0).test(stack));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public  ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    public int getDuration() {
        return duration;
    }

    public int getEnergy() {
        return energy;
    }

    public FluidStack getFluid() {
        return fluid.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    public static class Type implements RecipeType<RefineryRecipes> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "refinery";
    }

    public static class Serializer implements RecipeSerializer<RefineryRecipes> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(InfinityNexusGenerators.MOD_ID, "refinery");

        @Override
        public @NotNull RefineryRecipes fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");

            FluidStack fluidStack = new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(pSerializedRecipe.get("fluidType").getAsString()))),
                    pSerializedRecipe.get("fluidAmount").getAsInt());

            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            for (int i = 0; i < ingredients.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            int duration = pSerializedRecipe.get("duration").getAsInt();
            int energy = pSerializedRecipe.get("energy").getAsInt();

            System.out.println("TTSS: "+ fluidStack.getTranslationKey());
            System.out.println("TTSS: "+ pSerializedRecipe.get("fluidType").getAsString());
            System.out.println("TTSS: "+ output.copy().getHoverName());

            return new RefineryRecipes(inputs, fluidStack, output, pRecipeId, duration, energy);
        }

        @Override
        public @Nullable RefineryRecipes fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            //1
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            FluidStack fluidStack = pBuffer.readFluidStack();

            for(int i = 0; i < inputs.size(); i++) {
                //2
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }
            //3
            int duration = pBuffer.readInt();
            int energy = pBuffer.readInt();
            ItemStack output = pBuffer.readItem();

            return new RefineryRecipes(inputs, fluidStack, output, pRecipeId, duration, energy);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, RefineryRecipes pRecipe) {
            //1
            pBuffer.writeInt(pRecipe.getIngredients().size());
            pBuffer.writeFluidStack(pRecipe.fluid);

            for (Ingredient ing : pRecipe.getIngredients()) {
                //2
                ing.toNetwork(pBuffer);
            }
            //3
            pBuffer.writeInt(pRecipe.duration);
            pBuffer.writeInt(pRecipe.energy);

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}