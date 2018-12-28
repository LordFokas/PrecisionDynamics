package lordfokas.precisiondynamics.gui;

import cofh.core.gui.GuiContainerCore;
import cofh.core.gui.element.tab.TabConfiguration;
import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.DeviceType;
import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCounter extends GuiContainerCore {
    private static final int WIDTH = 176, HEIGHT = 174;
    private static final ResourceLocation background = new ResourceLocation(PrecisionDynamics.MODID,"textures/gui/counter.png");
    private static final int textColor = 0x303030;

    private final ItemStack stack;
    private final ContainerCounter container;
    private final TileEntityDeviceCounter te;
    private final Gauge gauge;

    public GuiCounter(TileEntityDeviceCounter te, ContainerCounter container){
        super(container);
        super.xSize = WIDTH;
        super.ySize = HEIGHT;
        this.container = container;
        this.te = te;
        this.gauge = new Gauge(te.variant, this);
        this.stack = DeviceType.COUNTER.getStack(te.variant);

        // TODO: remove debug / sorcery-oriented-programming
        Class[] clsa = new Class[2];
        for(Class cls : te.getClass().getSuperclass().getSuperclass().getInterfaces())
            if(cls.getName().equals("cofh.api.tileentity.IReconfigurableSides"))
                clsa[0] = cls;
        for(Class cls : TabConfiguration.class.getConstructors()[0].getParameterTypes())
            if(cls.getName().equals("cofh.api.tileentity.IReconfigurableSides"))
                clsa[1] = cls;
        System.err.println(clsa[0]+" == "+clsa[1]+" : " + (clsa[0] == clsa[1]));
        System.err.println(clsa[0].getClassLoader());
        System.err.println(clsa[1].getClassLoader());
        // ===

        this.addTab(new TabConfiguration(this, this.te));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float ticks, int x, int y) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawTabs(ticks, false);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        gauge.setPercentage(((float)container.stored)/((float)container.size));
        gauge.render(152, 15);

        // headers
        fontRenderer.drawString(stack.getDisplayName(), 7, 5, 0);
        fontRenderer.drawString("Inventory", 7, 82, 0);

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
