package net.tenth.factory.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.tenth.factory.Factory;
import net.tenth.factory.util.FactoryTags;

import java.util.List;

public class FactoryToolTiers {
    public static Tier COPPER;

    static {
        COPPER = TierSortingRegistry.registerTier(
                new ForgeTier(2, 150, 2f, 2f, 14, FactoryTags.Blocks.NEEDS_COPPER_TOOL, () -> Ingredient.of(Items.COPPER_INGOT)),
                new ResourceLocation(Factory.MOD_ID, "copper"), List.of(Tiers.STONE), List.of()
        );
    }
}
