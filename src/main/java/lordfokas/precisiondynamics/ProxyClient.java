package lordfokas.precisiondynamics;

import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import lordfokas.precisiondynamics.renderers.TESRCounter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ProxyClient implements IProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id, String submodel) {
        ModelLoader.setCustomModelResourceLocation(item, meta,
                new ModelResourceLocation(PrecisionDynamics.MODID + ":" + id, submodel));
    }

    @Override
    public void registerTESRs() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDeviceCounter.class, new TESRCounter());
    }

    @Override public void schedule(Runnable runnable) { Minecraft.getMinecraft().addScheduledTask(runnable); }
    @Override public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public TextureAtlasSprite getBlockSprite(ResourceLocation texture) {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
    }
}
