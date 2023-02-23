package net.tenth.factory.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
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
import net.tenth.factory.screen.SteamBenderMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SteamBenderEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot != 0;
        }
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
    protected final ContainerData data;
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
        System.out.println("ACTUAL BLOCK ENTITY: " + this);
    }

    public void setCircuit(SteamBenderEntity pEntity, int pCircuit) { // Used by CircuitSyncC2SPacket
        System.out.println("BlockEntity from setCircuit in SteamBoilerEntity: " + pEntity);
        System.out.println("The circuit this function is trying to set it to: " + pCircuit);
        data.set
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
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
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

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SteamBenderEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }
        pEntity.itemHandler.setStackInSlot(7, new ItemStack(FactoryItems.CIRCUIT.get(), pEntity.circuitNumber));
        if(pEntity.circuitNumber >= 25) {
            pEntity.resetCircuit();
        }
        // System.out.println("Circuit Number " + pEntity.circuitNumber);
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
        if(hasRecipe(pEntity)) {
        }
    }

    private static boolean hasRecipe(SteamBenderEntity pEntity) {
        return false;
    }
}
