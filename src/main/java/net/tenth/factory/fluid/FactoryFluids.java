package net.tenth.factory.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;
import net.tenth.factory.block.FactoryBlocks;
import net.tenth.factory.item.FactoryItems;

public class FactoryFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Factory.MOD_ID);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }


    public static final RegistryObject<FlowingFluid> SOURCE_STEAM = FLUIDS.register("steam",
            ()-> new ForgeFlowingFluid.Source(FactoryFluids.STEAM_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_STEAM = FLUIDS.register("flowing_steam",
            ()-> new ForgeFlowingFluid.Flowing(FactoryFluids.STEAM_PROPERTIES));

    public static final ForgeFlowingFluid.Properties STEAM_PROPERTIES = new ForgeFlowingFluid.Properties(
            FactoryFluidTypes.STEAM_FLUID_TYPE, SOURCE_STEAM, FLOWING_STEAM)
            .slopeFindDistance(1).levelDecreasePerBlock(1)
            .block(null)
            .bucket(null);

}
