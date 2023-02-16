package net.tenth.factory.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;

public class FactorySounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Factory.MOD_ID);

    private static RegistryObject<SoundEvent> registerSoundEvent (String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Factory.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    public static final RegistryObject<SoundEvent> OPEN_BIGGER_CHEST = registerSoundEvent("open_bigger_chest");

    public static final RegistryObject<SoundEvent> CLOSE_BIGGER_CHEST = registerSoundEvent("close_bigger_chest");

}
