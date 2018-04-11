package lordfokas.precisiondynamics;

import net.minecraft.item.Item;

public interface IProxy {
    void registerItemRenderer(Item item, int meta, String id, String submodel);
}
