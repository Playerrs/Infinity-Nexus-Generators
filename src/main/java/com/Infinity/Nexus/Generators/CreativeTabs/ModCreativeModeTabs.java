package com.Infinity.Nexus.Generators.CreativeTabs;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.block.ModBlocks;
import com.Infinity.Nexus.Generators.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, InfinityNexusGenerators.MOD_ID);

    public static final RegistryObject<CreativeModeTab> GENERATORS_TAB = CREATIVE_MODE_TABS.register("generators_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.DRILL.get()))
                    .title(Component.translatable("creativetab.generators_tab"))
                    .displayItems((displayParameters, output) -> {
                        output.accept(ModBlocks.REFINERY.get());

                        output.accept(ModItems.DRILL.get());
                        output.accept(ModItems.CRUDE_OIL_SCANNER.get());
                        output.accept(ModItems.BITUMEN.get());

                        output.accept(ModBlocks.BARREL.get());
                        output.accept(ModBlocks.INDUSTRIAL_BARREL.get());

                        output.accept(ModItems.CRUDE_OIL_BUCKET.get());
                        output.accept(ModItems.GASOLINE_BUCKET.get());
                        output.accept(ModItems.KEROSENE_BUCKET.get());
                        output.accept(ModItems.DIESEL_BUCKET.get());
                        output.accept(ModItems.LUBRICANT_BUCKET.get());
                        output.accept(ModItems.ROCKET_FUEL_BUCKET.get());


                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
