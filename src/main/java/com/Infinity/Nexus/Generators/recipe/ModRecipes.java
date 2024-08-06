package com.Infinity.Nexus.Generators.recipe;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, InfinityNexusGenerators.MOD_ID);
    public static final RegistryObject<RecipeSerializer<RefineryRecipes>> REFINERY_SERIALIZER = SERIALIZER.register("refinery", () -> RefineryRecipes.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
    }
}
