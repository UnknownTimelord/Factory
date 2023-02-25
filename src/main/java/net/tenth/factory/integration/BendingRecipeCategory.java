package net.tenth.factory.integration;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tenth.factory.Factory;
import net.tenth.factory.block.FactoryBlocks;
import net.tenth.factory.fluid.FactoryFluids;
import net.tenth.factory.item.FactoryItems;
import net.tenth.factory.recipe.BendingRecipe;

import java.util.List;

import static net.minecraft.client.gui.GuiComponent.blit;

public class BendingRecipeCategory implements IRecipeCategory<BendingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Factory.MOD_ID, "bending");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Factory.MOD_ID, "textures/gui/bending_gui_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
    private int arrow = 0;

    public BendingRecipeCategory(IGuiHelper pHelper) {
        this.background = pHelper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = pHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FactoryBlocks.STEAM_BENDER.get()));
    }
    @Override
    public RecipeType<BendingRecipe> getRecipeType() {
        return FactoryJEIPlugin.BENDING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("factory.bending_jei_category");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(BendingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack pPoseStack, double pMouseX, double pMouseY) {
        blit(pPoseStack, 87, 30, 0, 176, 0,31, arrow / 100, 256, 256);
        arrow++;
        if(arrow > 1600) {
            arrow = 0;
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BendingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 25).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 150, 35).setFluidRenderer(2000, false, 16, 16);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 124, 35).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.CATALYST, 137, 59).addItemStack(new ItemStack(FactoryItems.CIRCUIT.get(), recipe.getCircuitRequired()));
    }
}
