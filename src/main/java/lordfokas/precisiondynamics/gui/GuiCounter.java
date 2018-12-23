package lordfokas.precisiondynamics.gui;

import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiCounter extends GuiContainer {
    private static final int WIDTH = 176, HEIGHT = 174;
    private static final ResourceLocation background = new ResourceLocation(PrecisionDynamics.MODID,"textures/gui/counter.png");
    private static final int textColor = 0x303030;

    private final TileEntityDeviceCounter te;
    private final Gauge gauge;

    public GuiCounter(TileEntityDeviceCounter te, ContainerCounter container){
        super(container);
        this.te = te;
        this.gauge = Gauge.wrap(te.getDeviceBuffer(), this);
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int a, int b) {
        gauge.render(152, 15);

        // headers
        fontRenderer.drawString(te.variant.suffix + " Counter", 7, 5, 0);
        fontRenderer.drawString("Inventory", 7, 82, 0);

        // measurements
        fontRenderer.drawString("Rate: " + te.getRate(), 10,  17, textColor);
        fontRenderer.drawString("Total: " + te.getTotal(), 10, 27, textColor);
    }
}
