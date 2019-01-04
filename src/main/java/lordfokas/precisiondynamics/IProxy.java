package lordfokas.precisiondynamics;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface IProxy {
    void registerItemRenderer(Item item, int meta, String id, String submodel);
    void registerTESRs();
    void registerClientHandlers();
    void schedule(Runnable runnable);
    World getClientWorld();
    TextureAtlasSprite getBlockSprite(ResourceLocation texture);
}
