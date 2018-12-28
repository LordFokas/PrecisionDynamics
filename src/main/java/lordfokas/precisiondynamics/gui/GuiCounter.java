package lordfokas.precisiondynamics.gui;

import cofh.core.gui.GuiContainerCore;
import cofh.core.gui.element.tab.TabConfigurationTransfer;
import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.DeviceType;
import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCounter extends GuiContainerCore {
    private static final int WIDTH = 176, HEIGHT = 174;
    private static final ResourceLocation background = new ResourceLocation(PrecisionDynamics.MODID,"textures/gui/counter.png");
    private static final int textColor = 0x404040;

    private final ItemStack stack;
    private final ContainerCounter container;
    private final TileEntityDeviceCounter te;
    private final Gauge gauge;

    public GuiCounter(TileEntityDeviceCounter te, ContainerCounter container){
        super(container, background); // The background isn't being used yet, but oh well.
        super.xSize = WIDTH;
        super.ySize = HEIGHT;
        this.container = container;
        this.te = te;
        this.gauge = new Gauge(te.variant, this);
        this.stack = DeviceType.COUNTER.getStack(te.variant);
    }

    @Override
    public void initGui(){
        super.initGui();
        addTab(new TabConfigurationTransfer(this, te));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float ticks, int x, int y) {
        mc.getTextureManager().bindTexture(background);
        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0);
        drawTexturedModalRect(0, 0, 0, 0, xSize, ySize);
        drawTabs(ticks, false);
        GlStateManager.popMatrix();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        gauge.setPercentage(((float)container.stored)/((float)container.size));
        gauge.render(152, 15);

        // headers
        fontRenderer.drawString(stack.getDisplayName(), 7, 5, textColor);
        fontRenderer.drawString("Inventory", 7, 82, textColor);

        // measurements
        fontRenderer.drawString("Rate: " + te.getRate(), 10,  17, textColor);
        fontRenderer.drawString("Total: " + te.getTotal(), 10, 27, textColor);

        // DEBUG
        fontRenderer.drawString("*** *** DEBUG *** ***", 10,  50, 0xFF0000);
        fontRenderer.drawString("SZ: " + container.size, 10,  60, 0xFF0000);
        fontRenderer.drawString("ST: " + container.stored, 10, 70, 0xFF0000);

        drawTabs(0, true);
    }
}
