package lordfokas.precisiondynamics.gui;

import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiCounter extends GuiContainer {
    private static final int WIDTH = 176, HEIGHT = 166;
    private static final ResourceLocation background = new ResourceLocation(PrecisionDynamics.MODID,"textures/gui/counter.png");

    private TileEntityDeviceCounter te;

    public GuiCounter(TileEntityDeviceCounter te, ContainerCounter container){
        super(container);
        this.te = te;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        fontRenderer.drawString(te.variant.suffix + " Counter", guiLeft + 7, guiTop + 5, 0);
        fontRenderer.drawString("Inventory", guiLeft + 7, guiTop + 74, 0);
    }
}
