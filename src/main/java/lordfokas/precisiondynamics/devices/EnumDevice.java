package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.base.EnumVariant;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumDevice {
    BALANCER("balancer", "Bronze", null, "obsidian"),
    COUNTER("counter", "Aluminum", "dustGlowstone", null),
    PRIORITIZER("prioritizer", "Nickel", null, null);

    public final String name, metal;
    private final Object sides, top;

    EnumDevice(String name, String metal, Object sides, Object top){
        this.name = name;
        this.metal = metal;
        this.sides = sides;
        this.top = top;
    }

    private Object fallback(Object base, Object type){
        return base == null ? type : base;
    }

    public Object getSides(EnumVariant variant){
        return fallback(sides, variant.item);
    }

    public Object getTop(EnumVariant variant){
        return fallback(top, variant.item);
    }

    public ItemStack getStack(EnumVariant variant){
        return GameRegistry.makeItemStack(PrecisionDynamics.MODID+":"+name, variant.ordinal(), 1, null);
    }
}
