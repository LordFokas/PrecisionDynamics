package lordfokas.precisiondynamics.gui;

import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.base.resources.Variant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class Gauge {
    private static final ResourceLocation texture = new ResourceLocation(PrecisionDynamics.MODID, "textures/gui/gauges.png");
    private Variant variant;
    private GuiContainer gui;
    private float pct;

    public Gauge(Variant variant, GuiContainer gui){
        this.variant = variant;
        this.gui = gui;
    }

    public void setPercentage(float pct){ this.pct = pct; }

    public void render(int x, int y){
        gui.mc.getTextureManager().bindTexture(texture);
        int offset = variant.ordinal()*32;
        gui.drawTexturedModalRect(x, y, offset, 0, 16, 64);
        int h = (int)(64F * pct);
        gui.drawTexturedModalRect(x, y+64-h, offset + 16, 64-h, 16, h);
    }
}
