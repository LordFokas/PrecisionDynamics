package lordfokas.precisiondynamics.gui;

import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.buffer.BufferEnergy;
import lordfokas.precisiondynamics.devices.base.buffer.BufferFluid;
import lordfokas.precisiondynamics.devices.base.buffer.BufferItem;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public abstract class Gauge<B extends Buffer> {
    private static final ResourceLocation texture = new ResourceLocation(PrecisionDynamics.MODID, "textures/gui/gauges.png");
    protected GuiContainer gui;
    protected B buffer;

    @SuppressWarnings("unchecked") // bitch get off me!
    public static Gauge wrap(Buffer buffer, GuiContainer gui){
        if(buffer instanceof BufferEnergy) return new GaugeEnergy().with((BufferEnergy) buffer, gui);
        if(buffer instanceof BufferFluid) return new GaugeFluid().with((BufferFluid) buffer, gui);
        if(buffer instanceof BufferItem) return new GaugeItem().with((BufferItem) buffer, gui);
        return null;
    }

    protected Gauge with(B buffer, GuiContainer gui){
        this.buffer = buffer;
        this.gui = gui;
        return this;
    }

    protected final void setup(){ gui.mc.getTextureManager().bindTexture(texture); }
    public abstract void render(int x, int y);

    protected void draw121(int gx, int gy, int tx, int ty, int w, int h){
        gui.drawTexturedModalRect(gx, gy, tx, ty, w, h);
    }

    private static class GaugeEnergy extends Gauge<BufferEnergy>{
        public void render(int x, int y){
            setup();
            draw121(x, y, 18, 1, 16, 64);
            // draw121(x, y, 11, 1, 16, 64);
        }
    }

    private static class GaugeFluid extends Gauge<BufferFluid>{
        public void render(int x, int y){
            setup();
            draw121(x, y, 35, 1, 16, 64);
        }
    }

    private static class GaugeItem extends Gauge<BufferItem>{
        public void render(int x, int y){
            setup();
            draw121(x, y, 69, 1, 16, 64);
            // draw121(x, y, 52, 1, 16, 64);
        }
    }
}
