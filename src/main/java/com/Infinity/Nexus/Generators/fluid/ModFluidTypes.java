package com.Infinity.Nexus.Generators.fluid;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, InfinityNexusGenerators.MOD_ID);

    //        > Crude Oil
    //        > Gasoline
    //        > kerosene
    //        > Diesel
    //        > Lub Oil
    //        > Rocket Fuel
    public static final RegistryObject<FluidType> CRUDE_OIL = registerFluidType("crude_oil",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xA1202020,
                    new Vector3f(80f / 255f, 80f / 255f, 80f / 255f),
                    FluidType.Properties.create().viscosity(1000).density(1000)));

    public static final RegistryObject<FluidType> GASOLINE = registerFluidType("gasoline",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0x70134712,
                    new Vector3f(80f / 255f, 80f / 255f, 80f / 255f),
                    FluidType.Properties.create().viscosity(1000).density(1000)));

    public static final RegistryObject<FluidType> KEROSENE = registerFluidType("kerosene",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0x7050a6e1,
                    new Vector3f(80f / 255f, 80f / 255f, 80f / 255f),
                    FluidType.Properties.create().viscosity(1000).density(1000)));

    public static final RegistryObject<FluidType> DIESEL = registerFluidType("diesel",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0x70602020,
                    new Vector3f(80f / 255f, 80f / 255f, 80f / 255f),
                    FluidType.Properties.create().viscosity(1000).density(1000)));

    public static final RegistryObject<FluidType> LUBRICANT = registerFluidType("lubricant",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0x70DBCF5C,
                    new Vector3f(80f / 255f, 80f / 255f, 80f / 255f),
                    FluidType.Properties.create().viscosity(1000).density(1000)));

    public static final RegistryObject<FluidType> ROCKET_FUEL = registerFluidType("rocket_fuel",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0x70FFFAEF,
                    new Vector3f(80f / 255f, 80f / 255f, 80f / 255f),
                    FluidType.Properties.create().viscosity(1000).density(1000)));

    private static RegistryObject<FluidType> registerFluidType(String name, FluidType fluidType) {
        return FLUID_TYPES.register(name, () -> fluidType);
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
