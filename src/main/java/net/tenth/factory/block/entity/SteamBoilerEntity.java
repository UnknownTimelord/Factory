package net.tenth.factory.block.entity;

import com.ibm.icu.impl.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.tenth.factory.block.BlockFinder;
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
    private int progress = 0;
    private int maxProgress = 78;
    protected final ContainerData data;
    public SteamBoilerEntity(BlockPos pPos, BlockState pBlockState) {
        super(FactoryBlockEntities.STEAM_BOILER_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SteamBoilerEntity.this.progress;
                    case 1 -> SteamBoilerEntity.this.maxProgress;
                    default -> 0;
                };
            }
            @Override public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SteamBoilerEntity.this.progress = pValue;
                    case 1 -> SteamBoilerEntity.this.maxProgress = pValue;
                }
            }
            @Override public int getCount() {
                return 2;
            }
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
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
        pTag.putInt("steam_boiler.progress", this.progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        STEAM_TANK.readFromNBT(pTag);
        progress = pTag.getInt("steam_boiler.progress");
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SteamBoilerEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }
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
        if(pEntity.progress >= 79) {
            pEntity.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(SteamBoilerEntity pEntity) {
        Level level = pEntity.getLevel();
        assert level != null;
        if(hasRecipe(pEntity)) {
            switch(waterNearby(pEntity).second) {
                case "north" -> {
                    level.setBlock(pEntity.getBlockPos().north(), Blocks.AIR.defaultBlockState(), 2);
                    level.updateNeighborsAt(pEntity.getBlockPos().north(), Blocks.AIR);
                    pEntity.STEAM_TANK.fill(new FluidStack(FactoryFluids.SOURCE_STEAM.get(), 2000), IFluidHandler.FluidAction.EXECUTE);
                }
                case "east" -> {
                    level.setBlock(pEntity.getBlockPos().east(), Blocks.AIR.defaultBlockState(), 2);
                    level.updateNeighborsAt(pEntity.getBlockPos().east(), Blocks.AIR);
                    pEntity.STEAM_TANK.fill(new FluidStack(FactoryFluids.SOURCE_STEAM.get(), 2000), IFluidHandler.FluidAction.EXECUTE);
                }
                case "south" -> {
                    level.setBlock(pEntity.getBlockPos().south(), Blocks.AIR.defaultBlockState(), 2);
                    level.updateNeighborsAt(pEntity.getBlockPos().south(), Blocks.AIR);
                    pEntity.STEAM_TANK.fill(new FluidStack(FactoryFluids.SOURCE_STEAM.get(), 2000), IFluidHandler.FluidAction.EXECUTE);
                }
                case "west" -> {
                    level.setBlock(pEntity.getBlockPos().west(), Blocks.AIR.defaultBlockState(), 2);
                    level.updateNeighborsAt(pEntity.getBlockPos().west(), Blocks.AIR);
                    pEntity.STEAM_TANK.fill(new FluidStack(FactoryFluids.SOURCE_STEAM.get(), 2000), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    private static boolean hasRecipe(SteamBoilerEntity pEntity) {
        return waterNearby(pEntity).first && pEntity.STEAM_TANK.getFluidAmount() != 64000;
    }

    private static Pair<Boolean, String> waterNearby(BlockEntity pEntity) {
        Level level = pEntity.getLevel();
        assert level != null;
        if(BlockFinder.getNorth(pEntity) == Blocks.WATER && level.getBlockState(pEntity.getBlockPos().north()).getValue(BlockStateProperties.LEVEL) == 0) {
            return Pair.of(true, "north");
        }
        if(BlockFinder.getEast(pEntity) == Blocks.WATER && level.getBlockState(pEntity.getBlockPos().east()).getValue(BlockStateProperties.LEVEL) == 0) {
            return Pair.of(true, "east");
        }
        if(BlockFinder.getSouth(pEntity) == Blocks.WATER && level.getBlockState(pEntity.getBlockPos().south()).getValue(BlockStateProperties.LEVEL) == 0) {
            return Pair.of(true, "south");
        }
        if(BlockFinder.getWest(pEntity) == Blocks.WATER && level.getBlockState(pEntity.getBlockPos().west()).getValue(BlockStateProperties.LEVEL) == 0) {
            return Pair.of(true, "west");
        }
        return Pair.of(false, "none");
    }

}
