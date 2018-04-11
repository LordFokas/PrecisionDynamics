package lordfokas.precisiondynamics;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ProxyClient implements IProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id, String submodel) {
        ModelLoader.setCustomModelResourceLocation(item, meta,
                new ModelResourceLocation(PrecisionDynamics.MODID + ":" + id, submodel));
    }
}
