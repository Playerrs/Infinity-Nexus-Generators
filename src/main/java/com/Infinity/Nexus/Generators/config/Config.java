package com.Infinity.Nexus.Generators.config;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = InfinityNexusGenerators.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue REFINERY_ENERGY_CAPACITY = BUILDER
            .comment("Refinery energy capacity")
            .defineInRange("refinery_energy_capacity", 600000, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue REFINERY_ENERGY_TRANSFER_RATE = BUILDER
            .comment("Refinery energy transfer rate per tick")
            .defineInRange("refinery_energy_transfer_rate", 60000, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue REFINERY_FLUID_CAPACITY = BUILDER
            .comment("Refinery fluid capacity in mb")
            .defineInRange("refinery_fluid_capacity", 10000, 0, Integer.MAX_VALUE);

    //Daqui pra baixo é só exemplo
    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);


    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int refinery_energy_capacity;
    public static int refinery_energy_transfer_rate;
    public static int refinery_fluid_capacity;

    public static boolean logDirtBlock;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

        refinery_energy_capacity = REFINERY_ENERGY_CAPACITY.get();
        refinery_energy_transfer_rate = REFINERY_ENERGY_TRANSFER_RATE.get();
        refinery_fluid_capacity = REFINERY_FLUID_CAPACITY.get();

        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());
    }
}
