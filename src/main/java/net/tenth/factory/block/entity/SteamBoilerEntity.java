package net.tenth.factory.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.tenth.factory.fluid.FactoryFluids;
import net.tenth.factory.networking.FactoryMessages;
import net.tenth.factory.networking.packet.FluidSyncS2CPacket;
import net.tenth.factory.screen.SteamBoilerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SteamBoilerEntity extends BlockEntity implements MenuProvider {
    private final FluidTank STEAM_TANK = new FluidTank(64000) {
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
    protected final ContainerData data;
    public SteamBoilerEntity(BlockPos pPos, BlockState pBlockState) {
        super(FactoryBlockEntities.STEAM_BOILER_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override public int get(int pIndex) {return 0;}
            @Override public void set(int pIndex, int pValue) {}
            @Override public int getCount() { return 0; } // No changes needed yet (not using extra data)
        };
    }

    @Override
    public Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        FactoryMessages.sendToClients(new FluidSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new SteamBoilerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast(); // This should allow pipes to extract and insert fluid, but it doesn't.
        }
        return super.getCapability(cap);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(()-> STEAM_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag = STEAM_TANK.writeToNBT(pTag);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        STEAM_TANK.readFromNBT(pTag);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SteamBoilerEntity pEntity) {
    }
}
