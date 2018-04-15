package lordfokas.precisiondynamics.renderers;

import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class TESRCounter extends TileEntitySpecialRenderer<TileEntityDeviceCounter> {
    private static final double SCALE = 0.015;

    @Override
    public void render(TileEntityDeviceCounter counter, double x, double y, double z, float partial, int destroy, float alpha) {
        String rate = counter.throughput + " " + counter.variant.unit + "/t";
        GL11.glPushMatrix();
        GL11.glTranslated(x+1, y+1, z-0.01);
        GL11.glScaled(SCALE, SCALE, SCALE);
        GL11.glRotated(180,0, 0, 1);
        getFontRenderer().drawString(rate, 0, 0, 0xFF0000, false);
        GL11.glPopMatrix();
    }
}
