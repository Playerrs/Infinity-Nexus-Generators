package com.Infinity.Nexus.Generators.screen.refinery;

import com.Infinity.Nexus.Core.renderer.EnergyInfoArea;
import com.Infinity.Nexus.Core.renderer.FluidTankRenderer;
import com.Infinity.Nexus.Core.renderer.InfoArea;
import com.Infinity.Nexus.Core.renderer.RenderScreenTooltips;
import com.Infinity.Nexus.Core.utils.MouseUtil;
import com.Infinity.Nexus.Generators.InfinityNexusGenerators;
import com.Infinity.Nexus.Generators.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public class RefineryScreen extends AbstractContainerScreen<RefineryMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(InfinityNexusGenerators.MOD_ID, "textures/gui/refinery_gui.png");
    private EnergyInfoArea energyInfoArea;
    private FluidTankRenderer fluidRenderer;
    private FluidTankRenderer fluidRenderer1;
    private FluidTankRenderer fluidRenderer2;
    private FluidTankRenderer fluidRenderer3;
    private FluidTankRenderer fluidRenderer4;

    public RefineryScreen(RefineryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init(); //editar os treco bonito
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        assignFluidTank(); //Vários tanks tem que dar uma mexida ae
        assignEnergyInfoArea();
    }
    private void assignFluidTank() {
        fluidRenderer = new FluidTankRenderer(Config.refinery_fluid_capacity, true, 6, 62);
        fluidRenderer1 = new FluidTankRenderer(10000, true, 6, 47);
        fluidRenderer2 = new FluidTankRenderer(10000, true, 6, 52);
        fluidRenderer3 = new FluidTankRenderer(10000, true, 6, 57);
        fluidRenderer4 = new FluidTankRenderer(10000, true, 6, 62);
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x + 159, y + 6, menu.getBlockEntity().getEnergyStorage());
    }
     @Override
     protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
         int x = (width - imageWidth) / 2;
         int y = (height - imageHeight) / 2;
         pGuiGraphics.drawString(this.font,this.playerInventoryTitle,8,74,0XFFFFFF);
         pGuiGraphics.drawString(this.font,this.title,8,-9,0XFFFFFF);

         renderEnergyAreaTooltips(pGuiGraphics,pMouseX,pMouseY, x, y);
         renderTooltips(pGuiGraphics,pMouseX,pMouseY, x, y);
         renderFluidAreaTooltips(pGuiGraphics,pMouseX,pMouseY, x, y, menu.blockEntity.getTank(0), 39,6, fluidRenderer);
         renderFluidAreaTooltips(pGuiGraphics,pMouseX,pMouseY, x, y, menu.blockEntity.getTank(1), 76,21, fluidRenderer1);
         renderFluidAreaTooltips(pGuiGraphics,pMouseX,pMouseY, x, y, menu.blockEntity.getTank(2), 88,16, fluidRenderer2);
         renderFluidAreaTooltips(pGuiGraphics,pMouseX,pMouseY, x, y, menu.blockEntity.getTank(3), 100,11, fluidRenderer3);
         renderFluidAreaTooltips(pGuiGraphics,pMouseX,pMouseY, x, y, menu.blockEntity.getTank(4), 113,6, fluidRenderer4);

         InfoArea.draw(pGuiGraphics);
         super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
     }

    private void renderFluidAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y,
                                         FluidStack stack, int offsetX, int offsetY, FluidTankRenderer renderer) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, renderer)) {
            guiGraphics.renderTooltip(this.font, renderer.getTooltip(stack, TooltipFlag.Default.NORMAL),Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyAreaTooltips(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 159,  6, 6, 62)) {
            pGuiGraphics.renderTooltip(this.font, energyInfoArea.getTooltips(), Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }
    private void renderTooltips(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if (Screen.hasShiftDown()) {
            if (isMouseAboveArea(pMouseX, pMouseY, x, y, -12, 10, 17, 53)) {
                RenderScreenTooltips.renderUpgradeSlotTooltipAndItems(this.font, pGuiGraphics, pMouseX, pMouseY, x, y);
            }else if (isMouseAboveArea(pMouseX, pMouseY, x, y, 7, 28, 17, 17)) {
                RenderScreenTooltips.renderComponentSlotTooltipAndItems(this.font, pGuiGraphics, pMouseX, pMouseY, x, y);
            }else if(isMouseAboveArea(pMouseX, pMouseY, x, y, 133, 51, 17, 17)) {
                List<Component> components = List.of(Component.literal("Output Slot"));
                RenderScreenTooltips.renderTooltipArea(this.font, pGuiGraphics ,components, pMouseX, pMouseY, x, y);
            }else if(isMouseAboveArea(pMouseX, pMouseY, x, y, 39, 6, 6, 62)) {
                List<Component> components = List.of(Component.literal("Input Fluid"));
                RenderScreenTooltips.renderTooltipArea(this.font, pGuiGraphics ,components, pMouseX, pMouseY, x, y);
            }else if(isMouseAboveArea(pMouseX, pMouseY, x, y, 76, 21, 6, 47)) {
                List<Component> components = List.of(Component.literal("First Output Fluid"));
                RenderScreenTooltips.renderTooltipArea(this.font, pGuiGraphics ,components, pMouseX, pMouseY, x, y);
            }else if(isMouseAboveArea(pMouseX, pMouseY, x, y, 88, 16, 6, 52)) {
                List<Component> components = List.of(Component.literal("Second Output Fluid"));
                RenderScreenTooltips.renderTooltipArea(this.font, pGuiGraphics ,components, pMouseX, pMouseY, x, y);
            }else if(isMouseAboveArea(pMouseX, pMouseY, x, y, 100, 11, 6, 57)) {
                List<Component> components = List.of(Component.literal("Tried Output Fluid"));
                RenderScreenTooltips.renderTooltipArea(this.font, pGuiGraphics ,components, pMouseX, pMouseY, x, y);
            }else if(isMouseAboveArea(pMouseX, pMouseY, x, y, 112, 6, 6, 62)) {
                List<Component> components = List.of(Component.literal("Fourth Output Fluid"));
                RenderScreenTooltips.renderTooltipArea(this.font, pGuiGraphics ,components, pMouseX, pMouseY, x, y);
            }
        }
    }
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if(Screen.hasShiftDown() || isMouseAboveArea(pMouseX, pMouseY, x, y, - 15, + 10, 17, 54)) {
            RenderScreenTooltips.renderComponentSlotTooltip(guiGraphics, TEXTURE, x - 15, y + 10, 193, 84, 18, 131);
        }else{
            RenderScreenTooltips.renderComponentSlotTooltip(guiGraphics, TEXTURE, x - 3, y + 10, 193, 84, 18, 131);
        }
        guiGraphics.blit(TEXTURE, x + 2, y-14, 2, 167, 174, 64);
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
        energyInfoArea.render(guiGraphics);
        fluidRenderer.render(guiGraphics, x+40, y+6, menu.blockEntity.getTank(0));
        fluidRenderer1.render(guiGraphics, x+76, y+21, menu.blockEntity.getTank(1));
        fluidRenderer2.render(guiGraphics, x+88, y+16, menu.blockEntity.getTank(2));
        fluidRenderer3.render(guiGraphics, x+100, y+11, menu.blockEntity.getTank(3));
        fluidRenderer4.render(guiGraphics, x+112, y+6, menu.blockEntity.getTank(4));
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
     if(menu.isCrafting()) {
         guiGraphics.blit(TEXTURE, x + 57, y + 59, 176, 0, menu.getScaledProgress(), 9);
         guiGraphics.blit(TEXTURE, x + 68, y + 59, 187, 0, 2, 9-menu.getScaledProgress());
         guiGraphics.blit(TEXTURE, x + 52, y + 27, 189, 0, 18, 13-menu.getScaledProgress());
     }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidTankRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }
    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
