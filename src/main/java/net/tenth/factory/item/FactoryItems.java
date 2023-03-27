package net.tenth.factory.item;

import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;
public class FactoryItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Factory.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // Materials

    public static RegistryObject<Item> RAW_ALUMINUM = ITEMS.register("raw_aluminum",
            ()-> new Item(new Item.Properties()));
    public static RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot",
            ()-> new Item(new Item.Properties()));

    // Plates

    public static RegistryObject<Item> ALUMINUM_PLATE = ITEMS.register("aluminum_plate",
            ()-> new Item(new Item.Properties()));

    public static RegistryObject<Item> COPPER_PLATE = ITEMS.register("copper_plate",
            ()-> new Item(new Item.Properties()));

    public static RegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate",
            ()-> new Item(new Item.Properties()));

    public static RegistryObject<Item> GOLD_PLATE = ITEMS.register("gold_plate",
            ()-> new Item(new Item.Properties()));

    public static RegistryObject<Item> DIAMOND_PLATE = ITEMS.register("diamond_plate",
            ()-> new Item(new Item.Properties()));

    public static RegistryObject<Item> NETHERITE_PLATE = ITEMS.register("netherite_plate",
            ()-> new Item(new Item.Properties()));

    // Pickaxes

    public static RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe",
            ()-> new PickaxeItem(FactoryToolTiers.COPPER, 0, 0, new Item.Properties()));

    // Circuits

    public static RegistryObject<Item> UNFIRED_PCB = ITEMS.register("unfired_pcb",
            ()-> new Item(new Item.Properties()));

    public static RegistryObject<Item> CIRCUIT_BOARD = ITEMS.register("circuit_board",
            ()-> new Item(new Item.Properties()));

    public static RegistryObject<Item> CIRCUIT = ITEMS.register("circuit",
            ()-> new Item(new Item.Properties()));

}
