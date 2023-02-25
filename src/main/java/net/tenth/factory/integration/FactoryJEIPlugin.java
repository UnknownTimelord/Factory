package net.tenth.factory.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.tenth.factory.Factory;
import net.tenth.factory.recipe.BendingRecipe;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class FactoryJEIPlugin implements IModPlugin {
    public static RecipeType<BendingRecipe> BENDING_TYPE =
            new RecipeType<>(BendingRecipeCategory.UID, BendingRecipe.class);
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Factory.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                BendingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<BendingRecipe> recipesBending = rm.getAllRecipesFor(BendingRecipe.Type.INSTANCE);
        registration.addRecipes(BENDING_TYPE, recipesBending);
    }
}
