package net.tenth.factory.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;

public class FactoryMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Factory.MOD_ID);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, ()-> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }


    public static final RegistryObject<MenuType<SlightlyBiggerChestMenu>> SLIGHTLY_BIGGER_CHEST_MENU =
            registerMenuType(SlightlyBiggerChestMenu::new, "slightly_bigger_chest_menu");
    public static final RegistryObject<MenuType<SteamBoilerMenu>> STEAM_BOILER_MENU =
            registerMenuType(SteamBoilerMenu::new, "steam_boiler_menu");
}
