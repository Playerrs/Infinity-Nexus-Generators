package com.Infinity.Nexus.Generators.item;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    static DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, InfinityNexusGenerators.MOD_ID);

    public static final RegistryObject<Item> DRILL = ITEMS.register("drill",
            () -> new Item(new Item.Properties().durability(500)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
