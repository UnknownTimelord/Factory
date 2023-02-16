package net.tenth.factory.item;

import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;
import net.tenth.factory.fluid.FactoryFluids;
import net.tenth.factory.tab.FactoryTabs;

public class FactoryItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Factory.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // Materials

    public static RegistryObject<Item> ALUMINUM = ITEMS.register("aluminum",
            ()-> new Item(new Item.Properties()
                    .tab(FactoryTabs.FACTORY_TAB)));

    // Liquids

    public static RegistryObject<BucketItem> STEAM_BUCKET = ITEMS.register("steam_bucket",
            ()-> new BucketItem(FactoryFluids.SOURCE_STEAM, new Item.Properties()
                    .tab(FactoryTabs.FACTORY_TAB)
                    .stacksTo(1)
                    .craftRemainder(Items.BUCKET)));

    // Pickaxes

    public static RegistryObject<Item> PICKAXE_HEAD = ITEMS.register("pickaxe_head",
            ()-> new Item(new Item.Properties()
                    .tab(FactoryTabs.FACTORY_TAB)));

    public static RegistryObject<Item> TOOL_ROD = ITEMS.register("tool_rod",
            ()-> new Item(new Item.Properties()
                    .tab(FactoryTabs.FACTORY_TAB)));

    public static RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe",
            ()-> new PickaxeItem(Tiers.IRON, 0, 0, new Item.Properties()
                    .tab(FactoryTabs.FACTORY_TAB)));

    // Circuits

    public static RegistryObject<Item> UNFIRED_PCB = ITEMS.register("unfired_pcb",
            ()-> new Item(new Item.Properties()
                    .tab(FactoryTabs.FACTORY_TAB)));

    public static RegistryObject<Item> CIRCUIT_BOARD = ITEMS.register("circuit_board",
            ()-> new Item(new Item.Properties()
                    .tab(FactoryTabs.FACTORY_TAB)));

    public static RegistryObject<Item> CIRCUIT = ITEMS.register("circuit",
            ()-> new Item(new Item.Properties()
                    .tab(FactoryTabs.FACTORY_TAB)));


}
