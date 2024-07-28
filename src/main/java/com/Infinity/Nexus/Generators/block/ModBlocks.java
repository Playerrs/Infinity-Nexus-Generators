package com.Infinity.Nexus.Generators.block;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.block.custom.RefineryBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, InfinityNexusGenerators.MOD_ID);

    public static final RegistryObject<Block> REFINERY = BLOCKS.register("refinery",
            () -> new RefineryBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).requiresCorrectToolForDrops()
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.COPPER).mapColor(MapColor.RAW_IRON)));


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
