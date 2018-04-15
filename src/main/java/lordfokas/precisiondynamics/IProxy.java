package lordfokas.precisiondynamics;

import net.minecraft.item.Item;
import net.minecraft.world.World;

public interface IProxy {
    void registerItemRenderer(Item item, int meta, String id, String submodel);
    void registerTESRs();
    void schedule(Runnable runnable);
    World getClientWorld();
}
