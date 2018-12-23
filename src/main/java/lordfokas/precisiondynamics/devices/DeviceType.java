package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.base.resources.Variant;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum DeviceType {
    BALANCER("balancer", "Bronze", null, "obsidian"),
    COUNTER("counter", "Aluminum", "dustGlowstone", null),
    PRIORITIZER("prioritizer", "Nickel", null, null);

    public final String name, metal;
    private final Object sides, top;

    DeviceType(String name, String metal, Object sides, Object top){
        this.name = name;
        this.metal = metal;
        this.sides = sides;
        this.top = top;
    }

    /** Get an item for a recipe. If there is a device item use that, otherwise use the variant's material. */
    private Object fallback(Object base, Object type){
        return base == null ? type : base;
    }

    /** Get the item for the sides of the recipe. */
    public Object getSides(Variant variant){
        return fallback(sides, variant.item);
    }

    /** Get the item for the top of the recipe. */
    public Object getTop(Variant variant){
        return fallback(top, variant.item);
    }

    /** Create an item stack of this device in the given variant. */
    public ItemStack getStack(Variant variant){
        return GameRegistry.makeItemStack(PrecisionDynamics.MODID+":"+name, variant.ordinal(), 1, null);
    }
}
