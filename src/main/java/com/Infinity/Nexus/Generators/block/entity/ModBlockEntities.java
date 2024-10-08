package com.Infinity.Nexus.Generators.block.entity;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, InfinityNexusGenerators.MOD_ID);

    public static final RegistryObject<BlockEntityType<RefineryBlockEntity>> REFINERY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("refinery_block_entity", () ->
                    BlockEntityType.Builder.of(RefineryBlockEntity::new, ModBlocks.REFINERY.get()).build(null));

    public static final RegistryObject<BlockEntityType<IndustrialBarrelBlockEntity>> INDUSTRIAL_BARREL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("industrial_barrel_block_entity", () ->
                    BlockEntityType.Builder.of((pPos, pBlockState) -> new IndustrialBarrelBlockEntity(pPos, pBlockState), ModBlocks.INDUSTRIAL_BARREL.get()).build(null));

    public static final RegistryObject<BlockEntityType<BarrelBlockEntity>> BARREL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("barrel_block_entity", () ->
                    BlockEntityType.Builder.of(BarrelBlockEntity::new, ModBlocks.BARREL.get()).build(null));

    public static final RegistryObject<BlockEntityType<FractionatingTankBlockEntity>> FRACTIONATING_TANK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fractionating_tank_block_entity", () ->
                    BlockEntityType.Builder.of(FractionatingTankBlockEntity::new, ModBlocks.FRACTIONATING_TANK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
