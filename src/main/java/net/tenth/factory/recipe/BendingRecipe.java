package net.tenth.factory.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.tenth.factory.Factory;
import net.tenth.factory.item.FactoryItems;
import net.tenth.factory.util.FluidJSONUtil;
import org.jetbrains.annotations.Nullable;

public class BendingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final FluidStack steam;
    private final NonNullList<Ingredient> recipeItems;
    private final int circuitRequired;

    public BendingRecipe(ResourceLocation id, ItemStack output, FluidStack steam, int circuitRequired,
                         NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.steam = steam;
        this.circuitRequired = circuitRequired;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }
        for(int i = 1; i < pContainer.getContainerSize(); i++) {
            if (!pContainer.getItem(i).isEmpty()) {
                if(!recipeItems.get(0).test(pContainer.getItem(i))) continue;
                return pContainer.getItem(i).getItem() != FactoryItems.CIRCUIT.get() &&
                        pContainer.getItem(7).getCount() == circuitRequired;
            }
        }
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    public int getCircuitRequired() {
        return circuitRequired;
    }

    public FluidStack getSteam() {
        return steam;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BendingRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "bending";
    }

    public static class Serializer implements  RecipeSerializer<BendingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Factory.MOD_ID, "bending");

        @Override
        public BendingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            int circuitRequired = GsonHelper.getAsInt(pSerializedRecipe, "circuit_required");
            FluidStack steam = FluidJSONUtil.readFluid(pSerializedRecipe.get("steam").getAsJsonObject());

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new BendingRecipe(pRecipeId, output, steam, circuitRequired, inputs);
        }

        @Override
        public @Nullable BendingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            FluidStack steam = pBuffer.readFluidStack();
            int circuitRequired = pBuffer.readInt();
            ItemStack output = pBuffer.readItem();
            return new BendingRecipe(pRecipeId, output, steam, circuitRequired, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BendingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());

            for(Ingredient ing: pRecipe.getIngredients()) {
                ing.toNetwork(pBuffer);
            }

            pBuffer.writeFluidStack(pRecipe.getSteam());
            pBuffer.writeInt(pRecipe.getCircuitRequired());
            pBuffer.writeItemStack(pRecipe.getResultItem(), false);
        }
    }
}
