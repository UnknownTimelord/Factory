package net.tenth.factory;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tenth.factory.block.FactoryBlocks;
import net.tenth.factory.block.entity.FactoryBlockEntities;
import net.tenth.factory.fluid.FactoryFluidTypes;
import net.tenth.factory.fluid.FactoryFluids;
import net.tenth.factory.item.FactoryItems;
import net.tenth.factory.networking.FactoryMessages;
import net.tenth.factory.recipe.FactoryRecipes;
import net.tenth.factory.screen.FactoryMenuTypes;
import net.tenth.factory.screen.SlightlyBiggerChestScreen;
import net.tenth.factory.screen.SteamBoilerMenu;
import net.tenth.factory.screen.SteamBoilerScreen;
import net.tenth.factory.sound.FactorySounds;
import org.slf4j.Logger;
@Mod(Factory.MOD_ID)
public class Factory
{
    public static final String MOD_ID = "factory";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Factory()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        FactoryItems.register(modEventBus);
        FactoryBlocks.register(modEventBus);
        FactoryBlockEntities.register(modEventBus);
        FactoryFluidTypes.register(modEventBus);
        FactoryRecipes.register(modEventBus);
        FactoryFluids.register(modEventBus);
        FactoryMenuTypes.register(modEventBus);
        FactorySounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(()-> {
           FactoryMessages.register();
        });
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(FactoryMenuTypes.SLIGHTLY_BIGGER_CHEST_MENU.get(), SlightlyBiggerChestScreen::new);
            MenuScreens.register(FactoryMenuTypes.STEAM_BOILER_MENU.get(), SteamBoilerScreen::new);
        }
    }
}
