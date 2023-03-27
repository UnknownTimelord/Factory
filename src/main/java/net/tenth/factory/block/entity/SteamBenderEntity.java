package net.tenth.factory.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.tenth.factory.fluid.FactoryFluids;
import net.tenth.factory.item.FactoryItems;
import net.tenth.factory.networking.FactoryMessages;
import net.tenth.factory.networking.packet.FluidSyncS2CPacket;
import net.tenth.factory.recipe.BendingRecipe;
import net.tenth.factory.screen.SteamBenderMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SteamBenderEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) { return slot != 0; }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final FluidTank STEAM_TANK = new FluidTank(2000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                FactoryMessages.sendToClients(new FluidSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == FactoryFluids.SOURCE_STEAM.get();
        }
    };

    public void setFluid(FluidStack stack) {
        this.STEAM_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.STEAM_TANK.getFluid();
    }

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    private int progress = 0;
    private int maxProgress = 78;
    private int circuitNumber = 0;
    public final ContainerData data;
    public SteamBenderEntity(BlockPos pPos, BlockState pBlockState) {
        super(FactoryBlockEntities.STEAM_BENDER_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SteamBenderEntity.this.progress;
                    case 1 -> SteamBenderEntity.this.maxProgress;
                    case 2 -> SteamBenderEntity.this.circuitNumber;
                    default -> 0;
                };
            }
            @Override public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SteamBenderEntity.this.progress = pValue;
                    case 1 -> SteamBenderEntity.this.maxProgress = pValue;
                    case 2 -> SteamBenderEntity.this.circuitNumber = pValue;
                }
            }
            @Override public int getCount() {
                return 3;
            }
        };
    }

    public void setCircuit(SteamBenderEntity pEntity, int pCircuit) { // Used by CircuitSyncC2SPacket
        pEntity.circuitNumber = pCircuit;
    }

    @Override
    public Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SteamBenderMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) { // Make it so only the output slot can be extracted.
            return lazyItemHandler.cast();
        }
        if(cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()-> itemHandler);
        lazyFluidHandler = LazyOptional.of(()-> STEAM_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag = STEAM_TANK.writeToNBT(pTag);
        pTag.putInt("steam_bender.progress", this.progress);
        pTag.putInt("steam_bender.circuit", this.circuitNumber);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        STEAM_TANK.readFromNBT(pTag);
        progress = pTag.getInt("steam_bender.progress");
        circuitNumber = pTag.getInt("steam_bender.circuit");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            if(i == 7) break;
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition,  inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SteamBenderEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }
        if(pEntity.circuitNumber >= 25 || pEntity.circuitNumber < 0) { // Circuit is 0-24, anything under or over this is invalid
            pEntity.resetCircuit(); // Thus, we reset the circuit
        }
        /* Creates an ItemStack of CIRCUIT, amount = this entity's circuitNumber */
        pEntity.itemHandler.setStackInSlot(7, new ItemStack(FactoryItems.CIRCUIT.get(), pEntity.circuitNumber));
        if(hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, blockPos, blockState);
            if(pEntity.progress == pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, blockPos, blockState);
        }
    }

    private void resetCircuit() {
        this.circuitNumber = 0;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(SteamBenderEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer pContainer = new SimpleContainer(pEntity.itemHandler.getSlots());
        for(int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            pContainer.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<BendingRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(BendingRecipe.Type.INSTANCE, pContainer, level);

        int foundItem = 0;

        if(recipe.isPresent()) {
            for(int i = 1; i < pContainer.getContainerSize(); i++) {
                if (!pContainer.getItem(i).isEmpty() && !pContainer.getItem(i).is(FactoryItems.CIRCUIT.get())) {
                    if (recipe.get().getIngredients().get(0).test(pContainer.getItem(i))) {
                        foundItem = i;
                        break;
                    }
                }
            }
        }

        if(hasRecipe(pEntity)) {
            pEntity.STEAM_TANK.drain(recipe.get().getSteam().getAmount(), IFluidHandler.FluidAction.EXECUTE);
            pEntity.itemHandler.extractItem(foundItem, 1, false);
            pEntity.itemHandler.setStackInSlot(0, new ItemStack(recipe.get().getResultItem().getItem(),
                    pEntity.itemHandler.getStackInSlot(0).getCount() + 1));
            pEntity.resetProgress();
        }
    }

    private static boolean hasRecipe(SteamBenderEntity pEntity) {
        Level level = pEntity.level;
        assert level != null;
        SimpleContainer pContainer = new SimpleContainer(pEntity.itemHandler.getSlots());
        for(int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            pContainer.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<BendingRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(BendingRecipe.Type.INSTANCE, pContainer, level);

        return recipe.isPresent() && outputSlotInsertableAmount(pContainer)
                && outputSlotInsertableItem(pContainer, recipe.get().getResultItem())
                && steamInTank(pEntity, recipe);
    }

    private static boolean steamInTank(SteamBenderEntity pEntity, Optional<BendingRecipe> recipe) {
        return recipe.get().getSteam().equals(pEntity.STEAM_TANK.getFluid()) && recipe.get().getSteam().getAmount() == pEntity.STEAM_TANK.getFluidAmount();
    }

    public static boolean outputSlotInsertableItem(SimpleContainer pContainer, ItemStack pStack) {
        return pContainer.getItem(0).getItem() == pStack.getItem() || pContainer.getItem(0).isEmpty();
    }

    public static boolean outputSlotInsertableAmount(SimpleContainer pContainer) {
        return pContainer.getItem(0).getMaxStackSize() > pContainer.getItem(0).getCount();
    }

    public int getProgress() {
        return this.progress;
    }
}
