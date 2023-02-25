package net.tenth.factory.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tenth.factory.Factory;
import net.tenth.factory.item.FactoryItems;
import net.tenth.factory.networking.FactoryMessages;
import net.tenth.factory.networking.packet.CircuitSyncC2SPacket;
import net.tenth.factory.screen.renderer.FluidTankRenderer;
import net.tenth.factory.util.MouseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SteamBenderScreen extends AbstractContainerScreen<SteamBenderMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Factory.MOD_ID, "textures/gui/bending_gui.png");
    private FluidTankRenderer renderer;
    private Level level;
    public SteamBenderScreen(SteamBenderMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.level = pPlayerInventory.player.getLevel();
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        renderer = new FluidTankRenderer(2000, true, 16, 16);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderFluidAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    private void renderFluidAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(IsMouseAboveArea(pMouseX, pMouseY, x, y, 150, 35)) {
            renderTooltip(pPoseStack, renderer.getTooltip(menu.getFluidStack(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private boolean IsMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(pPoseStack, x, y);
        renderer.render(pPoseStack, x + 150, y + 35, menu.getFluidStack());
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 87, y + 30, 176, 0,  31, menu.getScaledProgress());
        }
    }

    @Override
    protected void slotClicked(@NotNull Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
        System.out.println("Mouse Button: " + pMouseButton);
        if(pSlotId == 43 && pMouseButton == 0) { // Left Click
            FactoryMessages.sendToServer(new CircuitSyncC2SPacket(menu.getCircuit() + 1, menu.blockEntity.getBlockPos()));
        }
        if(pSlotId == 43 && pMouseButton == 1) { // Right Click
            if(menu.getCircuit() == 0) {
                FactoryMessages.sendToServer(new CircuitSyncC2SPacket(menu.getCircuit() + 24, menu.blockEntity.getBlockPos()));
            } else {
                FactoryMessages.sendToServer(new CircuitSyncC2SPacket(menu.getCircuit() - 1, menu.blockEntity.getBlockPos()));
            }
        }
        super.slotClicked(pSlot, pSlotId, pMouseButton, pType);
    }

    @Override
    public @Nullable Slot getSlotUnderMouse() {
        return super.getSlotUnderMouse();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
}

