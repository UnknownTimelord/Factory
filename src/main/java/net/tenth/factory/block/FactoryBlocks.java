package net.tenth.factory.block;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;
import net.tenth.factory.block.custom.*;
import net.tenth.factory.fluid.FactoryFluids;
import net.tenth.factory.item.FactoryItems;
import net.tenth.factory.tab.FactoryTabs;

import java.util.function.Supplier;

public class FactoryBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Factory.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name,RegistryObject<T> block) {
        return FactoryItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    // Misc. Functional Blocks

    public static final RegistryObject<Block> SLIGHTLY_BIGGER_CHEST = registerBlock("slightly_bigger_chest",
            ()-> new SlightlyBiggerChest(BlockBehaviour.Properties.of(Material.WOOD)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .strength(1f)));

    // Machines

    public static final RegistryObject<Block> STEAM_BOILER = registerBlock("steam_boiler",
            ()-> new SteamBoiler(BlockBehaviour.Properties.of(Material.METAL)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(2f)));

    public static final RegistryObject<Block> STEAM_BENDER = registerBlock("steam_bender",
            ()-> new SteamBender(BlockBehaviour.Properties.of(Material.METAL)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(2f)));
}
