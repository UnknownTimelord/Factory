package net.tenth.factory.fluid;

import com.mojang.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;

public class FactoryFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation STEAM_OVERLAY_RL = new ResourceLocation(Factory.MOD_ID, "misc/steam_overlay");
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Factory.MOD_ID);

    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, ()-> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, STEAM_OVERLAY_RL,
                0xA1808080, new Vector3f(128f / 255f, 128f / 255f, 125f / 255f), properties));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }

    public static final RegistryObject<FluidType> STEAM_FLUID_TYPE = register("steam",
            FluidType.Properties.create().density(0).viscosity(0).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));
}
