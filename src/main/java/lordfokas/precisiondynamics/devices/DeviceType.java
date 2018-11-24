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

    private Object fallback(Object base, Object type){
        return base == null ? type : base;
    }

    public Object getSides(Variant variant){
        return fallback(sides, variant.item);
    }

    public Object getTop(Variant variant){
        return fallback(top, variant.item);
    }

    public ItemStack getStack(Variant variant){
        return GameRegistry.makeItemStack(PrecisionDynamics.MODID+":"+name, variant.ordinal(), 1, null);
    }
}
