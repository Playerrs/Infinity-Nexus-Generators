package com.Infinity.Nexus.Generators.screen;

import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.screen.gasPoweredGenerator.GasPoweredGeneratorMenu;
import com.Infinity.Nexus.Generators.screen.refinery.RefineryMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, InfinityNexusGenerators.MOD_ID);

    public static final RegistryObject<MenuType<RefineryMenu>> REFINERY_MENU = registerMenuType(RefineryMenu::new, "refinery_menu");

    public static final RegistryObject<MenuType<GasPoweredGeneratorMenu>> GAS_POWERED_GENERATOR_MENU =
            registerMenuType(GasPoweredGeneratorMenu::new, "gas_powered_generator_menu");

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name){
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
