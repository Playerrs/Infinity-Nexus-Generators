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
    public static final RegistryObject<Item> GASOLINE_BUCKET = ITEMS.register("gasoline_bucket",
            () -> new BucketItem(ModFluids.SOURCE_GASOLINE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> KEROSENE_BUCKET = ITEMS.register("kerosene_bucket",
            () -> new BucketItem(ModFluids.SOURCE_KEROSENE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> DIESEL_BUCKET = ITEMS.register("diesel_bucket",
            () -> new BucketItem(ModFluids.SOURCE_DIESEL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> LUBRICANT_BUCKET = ITEMS.register("lubricant_bucket",
            () -> new BucketItem(ModFluids.SOURCE_LUB_OIL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> ROCKET_FUEL_BUCKET = ITEMS.register("rocket_fuel_bucket",
            () -> new BucketItem(ModFluids.SOURCE_ROCKET_FUEL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
