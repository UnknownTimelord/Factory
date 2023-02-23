package net.tenth.factory.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;
import net.tenth.factory.block.FactoryBlocks;
import net.tenth.factory.block.custom.SteamBoiler;

public class FactoryBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Factory.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static RegistryObject<BlockEntityType<SlightlyBiggerChestEntity>> SLIGHTLY_BIGGER_CHEST_ENTITY = BLOCK_ENTITIES.register("slightly_bigger_chest",
            ()-> BlockEntityType.Builder.of(SlightlyBiggerChestEntity::new,
                    FactoryBlocks.SLIGHTLY_BIGGER_CHEST.get()).build(null));

    public static RegistryObject<BlockEntityType<SteamBoilerEntity>> STEAM_BOILER_ENTITY = BLOCK_ENTITIES.register("steam_boiler",
            ()-> BlockEntityType.Builder.of(SteamBoilerEntity::new,
                    FactoryBlocks.STEAM_BOILER.get()).build(null));

    public static RegistryObject<BlockEntityType<SteamBenderEntity>> STEAM_BENDER_ENTITY = BLOCK_ENTITIES.register("steam_bender",
            ()-> BlockEntityType.Builder.of(SteamBenderEntity::new,
                    FactoryBlocks.STEAM_BENDER.get()).build(null));
}
