package net.tenth.factory.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tenth.factory.Factory;

public class FactoryRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Factory.MOD_ID);

    public static void register (IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }

    public static final RegistryObject<RecipeSerializer<BendingRecipe>> BENDING_RECIPE_SERIALIZER =
            SERIALIZERS.register("bending", ()-> BendingRecipe.Serializer.INSTANCE);
}
