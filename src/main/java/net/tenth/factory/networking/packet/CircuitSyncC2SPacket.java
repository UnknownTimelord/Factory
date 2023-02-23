package net.tenth.factory.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.tenth.factory.block.entity.SteamBenderEntity;

import java.util.function.Supplier;

public class CircuitSyncC2SPacket {
    private final int circuitNumber;
    private final BlockPos pos;

    public CircuitSyncC2SPacket(int pCircuit, BlockPos pPos) {
        this.circuitNumber = pCircuit;
        this.pos = pPos;
    }

    public CircuitSyncC2SPacket(FriendlyByteBuf buf) {
        this.circuitNumber = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(circuitNumber);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Level level  = Minecraft.getInstance().level;
            if(level.getBlockEntity(pos) instanceof SteamBenderEntity blockEntity) {
                System.out.println("Packet circuitNumber: " + circuitNumber);
                System.out.println("Packet BlockEntity: " + blockEntity);
                blockEntity.setCircuit(blockEntity, circuitNumber);
            }
        });
        return true;
    }
}
