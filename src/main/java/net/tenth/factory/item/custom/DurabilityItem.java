package net.tenth.factory.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DurabilityItem extends Item {
    public DurabilityItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        if(copy.getDamageValue() == copy.getMaxDamage() - 1) {
            return itemStack;
        }
        else {
            copy.setDamageValue(copy.getDamageValue() + 1);
        }
        return copy;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
