package com.Infinity.Nexus.Generators.fluid;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.block.ModBlocks;
import com.Infinity.Nexus.Generators.item.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(
            ForgeRegistries.FLUIDS, InfinityNexusGenerators.MOD_ID);
    //        > Crude Oil
    //        > Gasoline
    //        > kerosene
    //        > Diesel
    //        > Lub Oil
    //        > Rocket Fuel
    public static final RegistryObject<FlowingFluid> SOURCE_CRUDE_OIL = FLUIDS.register("crude_oil_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.CRUDE_OIL_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CRUDE_OIL = FLUIDS.register("flowing_crude_oil_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CRUDE_OIL_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_GASOLINE = FLUIDS.register("gasoline_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.GASOLINE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GASOLINE = FLUIDS.register("flowing_gasoline_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.GASOLINE_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_KEROSENE = FLUIDS.register("kerosene_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.KEROSENE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_KEROSENE = FLUIDS.register("flowing_kerosene_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.KEROSENE_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_DIESEL = FLUIDS.register("diesel_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.DIESEL_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DIESEL = FLUIDS.register("flowing_diesel_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.DIESEL_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_LUB_OIL = FLUIDS.register("lubricant_oil_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.LUBRICANT_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_LUB_OIL = FLUIDS.register("flowing_lubricant_oil_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.LUBRICANT_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_ROCKET_FUEL = FLUIDS.register("rocket_fuel_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.ROCKET_FUEL_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_ROCKET_FUEL = FLUIDS.register("flowing_rocket_fuel_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ROCKET_FUEL_PROPERTIES));


    public static final ForgeFlowingFluid.Properties CRUDE_OIL_PROPERTIES = new ForgeFlowingFluid.Properties(
        ModFluidTypes.CRUDE_OIL, SOURCE_CRUDE_OIL, FLOWING_CRUDE_OIL)
            .slopeFindDistance(2).levelDecreasePerBlock(1).block(ModBlocks.CRUDE_OIL_BLOCK)
            .bucket(ModItems.CRUDE_OIL_BUCKET);

    public static final ForgeFlowingFluid.Properties GASOLINE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.GASOLINE, SOURCE_GASOLINE, FLOWING_GASOLINE)
            .slopeFindDistance(2).levelDecreasePerBlock(1).block(ModBlocks.GASOLINE_BLOCK)
            .bucket(ModItems.GASOLINE_BUCKET);

    public static final ForgeFlowingFluid.Properties KEROSENE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.KEROSENE, SOURCE_KEROSENE, FLOWING_KEROSENE)
            .slopeFindDistance(2).levelDecreasePerBlock(1).block(ModBlocks.KEROSENE_BLOCK)
            .bucket(ModItems.KEROSENE_BUCKET);

    public static final ForgeFlowingFluid.Properties DIESEL_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.DIESEL, SOURCE_DIESEL, FLOWING_DIESEL)
            .slopeFindDistance(2).levelDecreasePerBlock(1).block(ModBlocks.DIESEL_BLOCK)
            .bucket(ModItems.DIESEL_BUCKET);

    public static final ForgeFlowingFluid.Properties LUBRICANT_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.LUBRICANT, SOURCE_LUB_OIL, FLOWING_LUB_OIL)
            .slopeFindDistance(2).levelDecreasePerBlock(1).block(ModBlocks.LUBRICANT_BLOCK)
            .bucket(ModItems.LUBRICANT_BUCKET);

    public static final ForgeFlowingFluid.Properties ROCKET_FUEL_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.ROCKET_FUEL, SOURCE_ROCKET_FUEL, FLOWING_ROCKET_FUEL)
            .slopeFindDistance(2).levelDecreasePerBlock(1).block(ModBlocks.ROCKET_FUEL_BLOCK)
            .bucket(ModItems.ROCKET_FUEL_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }

}
