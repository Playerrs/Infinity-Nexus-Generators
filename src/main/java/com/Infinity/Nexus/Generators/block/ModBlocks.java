package com.Infinity.Nexus.Generators.block;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.block.custom.*;
import com.Infinity.Nexus.Generators.fluid.ModFluids;
import com.Infinity.Nexus.Generators.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, InfinityNexusGenerators.MOD_ID);


    public static final RegistryObject<Block> REFINERY = registerBlock("refinery",
            () -> new Refinery(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).requiresCorrectToolForDrops().noParticlesOnBreak().mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistryObject<Block> FRACTIONATING_TANK = registerBlock("fractionating_tank",
            () -> new FractionatingTank(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noLootTable().strength(1.0F).mapColor(MapColor.TERRACOTTA_RED)));
public static final RegistryObject<Block> GASOLINE_POWERED_GENERATOR = registerBlock("gasoline_powered_generator",
            () -> new GasPoweredGenerator(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(1.0F).mapColor(MapColor.TERRACOTTA_RED)));


    public static final RegistryObject<Block> INDUSTRIAL_BARREL = registerBlock("industrial_barrel",
            () -> new IndustrialBarrel(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noLootTable().strength(1.0F).mapColor(MapColor.TERRACOTTA_RED)));

    public static final RegistryObject<Block> BARREL = registerBlock("barrel",
            () -> new Barrel(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noLootTable().strength(1.0F).mapColor(MapColor.SAND)));



    // Relation to Fluids
    public static final RegistryObject<LiquidBlock> CRUDE_OIL_BLOCK = BLOCKS.register("crude_oil_block",
            () -> new LiquidBlock(ModFluids.SOURCE_CRUDE_OIL, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
    public static final RegistryObject<LiquidBlock> GASOLINE_BLOCK = BLOCKS.register("gasoline_block",
            () -> new LiquidBlock(ModFluids.SOURCE_GASOLINE,  BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
    public static final RegistryObject<LiquidBlock> KEROSENE_BLOCK = BLOCKS.register("kerosene_block",
            () -> new LiquidBlock(ModFluids.SOURCE_KEROSENE,  BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
    public static final RegistryObject<LiquidBlock> DIESEL_BLOCK = BLOCKS.register("diesel_block",
            () -> new LiquidBlock(ModFluids.SOURCE_DIESEL,  BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
    public static final RegistryObject<LiquidBlock> LUBRICANT_BLOCK = BLOCKS.register("lubricant_block",
            () -> new LiquidBlock(ModFluids.SOURCE_LUB_OIL,  BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
    public static final RegistryObject<LiquidBlock> ROCKET_FUEL_BLOCK = BLOCKS.register("rocket_fuel_block",
            () -> new LiquidBlock(ModFluids.SOURCE_ROCKET_FUEL,  BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
