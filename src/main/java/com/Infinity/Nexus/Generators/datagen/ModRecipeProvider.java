package com.Infinity.Nexus.Generators.datagen;

import com.Infinity.Nexus.Generators.block.ModBlocks;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REFINERY.get())
                .pattern("ABA")
                .pattern("CDE")
                .pattern("FFF")
                .define('A', Items.BUCKET)
                .define('B', Blocks.GLASS_PANE)
                .define('C', Blocks.MAGMA_BLOCK)
                .define('D', Blocks.IRON_BLOCK)
                .define('E', Blocks.HOPPER)
                .define('F', Blocks.COPPER_BLOCK)
                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
                .save(consumer, "refinery");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INDUSTRIAL_BARREL.get())
                .pattern("ABA")
                .pattern("ACA")
                .pattern("ABA")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('C', Items.BUCKET)
                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
                .save(consumer, "barrel");


        //nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, ModItemsAdditions.BRONZE_NUGGET.get(), RecipeCategory.MISC, ModItemsAdditions.BRONZE_INGOT.get());

    }
}
