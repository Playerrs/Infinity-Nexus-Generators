package com.Infinity.Nexus.Generators.item;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.fluid.ModFluids;
import com.Infinity.Nexus.Generators.item.custom.CrudeOilScannerItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, InfinityNexusGenerators.MOD_ID);


    public static final RegistryObject<Item> DRILL = ITEMS.register("drill",
            () -> new Item(new Item.Properties().durability(512)));

    public static final RegistryObject<Item> CRUDE_OIL_SCANNER = ITEMS.register("crude_oil_scanner",
            () -> new CrudeOilScannerItem(new Item.Properties().durability(256)));

    // Relation to Fluids
    public static final RegistryObject<Item> CRUDE_OIL_BUCKET = ITEMS.register("crude_oil_bucket",
            () -> new BucketItem(ModFluids.SOURCE_CRUDE_OIL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
