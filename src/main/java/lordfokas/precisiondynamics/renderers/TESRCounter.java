package lordfokas.precisiondynamics.renderers;

import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class TESRCounter extends TileEntitySpecialRenderer<TileEntityDeviceCounter> {
    private static final double SCALE = 0.0125;

    @Override
    public void render(TileEntityDeviceCounter counter, double x, double y, double z, float partial, int destroy, float alpha) {
        GL11.glPushMatrix();
        GL11.glTranslated(x+0.5, y, z+0.5); // Move to ZX center of block
        GL11.glRotated(counter.getFacingAngle(), 0, 1, 0); // Rotate to facing
        GL11.glTranslated(0, 0.5, -0.505); // Move to XY center of face
        GL11.glRotated(180,0, 0, 1); // Text is upside down, rotate
        GL11.glScaled(SCALE, SCALE, SCALE); // Scale the text WAY down
        GlStateManager.disableLighting();
        draw(counter);
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }

    private void draw(TileEntityDeviceCounter counter){
        int color = 0xFF000000;
        FontRenderer renderer = getFontRenderer();
        String rate = counter.getRate();
        renderer.drawString(rate, -((float)renderer.getStringWidth(rate))/2, -8, color, false);
        String total = counter.getTotal();
        renderer.drawString(total, -((float)renderer.getStringWidth(total))/2, 2, color, false);
    }
}
