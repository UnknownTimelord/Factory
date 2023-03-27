package net.tenth.factory.world.feature;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;
import net.tenth.factory.block.FactoryBlocks;

import java.util.List;
import java.util.function.Supplier;

public class FactoryConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?,?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Factory.MOD_ID);

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_FACTORY_ORES = Suppliers.memoize(() -> List.of(
            // Aluminum
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, FactoryBlocks.ALUMINUM_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, FactoryBlocks.DEEPSLATE_ALUMINUM_ORE.get().defaultBlockState()),
            OreConfiguration.target(new BlockMatchTest(Blocks.GRAVEL), FactoryBlocks.GRAVEL_ALUMINUM_ORE.get().defaultBlockState())
    ));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> NETHER_FACTORY_ORES = Suppliers.memoize(() -> List.of(
            // Aluminum
            OreConfiguration.target(new BlockMatchTest(Blocks.GRAVEL), FactoryBlocks.GRAVEL_ALUMINUM_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, FactoryBlocks.NETHERRACK_ALUMINUM_ORE.get().defaultBlockState())
    ));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> END_FACTORY_ORES = Suppliers.memoize(() -> List.of(
            // Aluminum
            OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), FactoryBlocks.ENDSTONE_ALUMINUM_ORE.get().defaultBlockState())
            ));

    public static final RegistryObject<ConfiguredFeature<?,?>> OVERWORLD_FACTORY_ORE = CONFIGURED_FEATURES.register("overworld_factory_ore",
            ()-> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_FACTORY_ORES.get(), 7)));

    public static final RegistryObject<ConfiguredFeature<?,?>> NETHER_FACTORY_ORE = CONFIGURED_FEATURES.register("nether_factory_ore",
            ()-> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(NETHER_FACTORY_ORES.get(), 7)));

    public static final RegistryObject<ConfiguredFeature<?,?>> END_FACTORY_ORE = CONFIGURED_FEATURES.register("end_factory_ore",
            ()-> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(END_FACTORY_ORES.get(), 7)));
}
