package net.tenth.factory.integration;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.tenth.factory.Factory;
import net.tenth.factory.block.entity.SteamBenderEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class JadeComponentProvider implements IBlockComponentProvider {

    public static JadeComponentProvider INSTANCE = new JadeComponentProvider();
    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {

        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        if(blockEntity instanceof SteamBenderEntity) {
            SteamBenderEntity steamBender = (SteamBenderEntity) Minecraft.getInstance().level.getBlockEntity(blockEntity.getBlockPos());
            iTooltip.add(Component.translatable("factory.progress: " + steamBender.getProgress() / 78 * 100 + "% / 100%"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Factory.MOD_ID, "jade_provider");
    }
}
