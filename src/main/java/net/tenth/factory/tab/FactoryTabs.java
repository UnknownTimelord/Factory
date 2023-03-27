package net.tenth.factory.tab;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.tenth.factory.block.FactoryBlocks;
import net.tenth.factory.item.FactoryItems;

public class FactoryTabs {
    public static CreativeModeTab FACTORY_TAB = new CreativeModeTab("factory_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(FactoryItems.CIRCUIT.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            /* Items */
            // Materials
            pItems.add(new ItemStack(FactoryBlocks.ALUMINUM_ORE.get()));
            pItems.add(new ItemStack(FactoryBlocks.DEEPSLATE_ALUMINUM_ORE.get()));
            pItems.add(new ItemStack(FactoryBlocks.GRAVEL_ALUMINUM_ORE.get()));
            pItems.add(new ItemStack(FactoryBlocks.NETHERRACK_ALUMINUM_ORE.get()));
            pItems.add(new ItemStack(FactoryBlocks.ENDSTONE_ALUMINUM_ORE.get()));
            pItems.add(new ItemStack(FactoryItems.RAW_ALUMINUM.get()));
            pItems.add(new ItemStack(FactoryItems.ALUMINUM_INGOT.get()));
            // Plates
            pItems.add(new ItemStack(FactoryItems.ALUMINUM_PLATE.get()));
            pItems.add(new ItemStack(FactoryItems.COPPER_PLATE.get()));
            pItems.add(new ItemStack(FactoryItems.IRON_PLATE.get()));
            pItems.add(new ItemStack(FactoryItems.GOLD_PLATE.get()));
            pItems.add(new ItemStack(FactoryItems.DIAMOND_PLATE.get()));
            pItems.add(new ItemStack(FactoryItems.NETHERITE_PLATE.get()));
            // Pickaxes
            pItems.add(new ItemStack(FactoryItems.COPPER_PICKAXE.get()));
            // Circuits
            pItems.add(new ItemStack(FactoryItems.UNFIRED_PCB.get()));
            pItems.add(new ItemStack(FactoryItems.CIRCUIT_BOARD.get()));
            pItems.add(new ItemStack(FactoryItems.CIRCUIT.get()));

            /* Blocks */
            pItems.add(new ItemStack(FactoryBlocks.SLIGHTLY_BIGGER_CHEST.get()));
            pItems.add(new ItemStack(FactoryBlocks.STEAM_BOILER.get()));
            pItems.add(new ItemStack(FactoryBlocks.STEAM_BENDER.get()));
            super.fillItemList(pItems);
        }
    };

}
