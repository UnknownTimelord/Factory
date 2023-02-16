package net.tenth.factory.tab;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.tenth.factory.item.FactoryItems;

public class FactoryTabs {
    public static CreativeModeTab FACTORY_TAB = new CreativeModeTab("factory_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(FactoryItems.CIRCUIT.get());
        }
    };

}
