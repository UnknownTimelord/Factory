package net.tenth.factory.world.feature;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;

import java.util.List;

public class FactoryPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Factory.MOD_ID);

    public static List<PlacementModifier> orePlacement(PlacementModifier pPlacementModifier1, PlacementModifier pPlacementModifier2) {
        return List.of(pPlacementModifier1, InSquarePlacement.spread(), pPlacementModifier2, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int pChance, PlacementModifier pPlacementModifier) {
        return orePlacement(CountPlacement.of(pChance), pPlacementModifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pPlacementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pPlacementModifier);
    }

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }

    public static final RegistryObject<PlacedFeature> OVERWORLD_FACTORY_ORE_PLACED = PLACED_FEATURES.register("overworld_factory_ore_placed",
            ()-> new PlacedFeature(FactoryConfiguredFeatures.OVERWORLD_FACTORY_ORE.getHolder().get(),
                    commonOrePlacement(7,
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    public static final RegistryObject<PlacedFeature> NETHER_FACTORY_ORE_PLACED = PLACED_FEATURES.register("nether_factory_ore_placed",
            ()-> new PlacedFeature(FactoryConfiguredFeatures.NETHER_FACTORY_ORE.getHolder().get(),
                    commonOrePlacement(7,
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    public static final RegistryObject<PlacedFeature> END_FACTORY_ORE_PLACED = PLACED_FEATURES.register("end_factory_ore_placed",
            ()-> new PlacedFeature(FactoryConfiguredFeatures.END_FACTORY_ORE.getHolder().get(),
                    commonOrePlacement(7,
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));
}
