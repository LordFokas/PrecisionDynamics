package lordfokas.precisiondynamics.devices.base;

import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.buffer.BufferEnergy;
import lordfokas.precisiondynamics.devices.base.buffer.BufferItem;
import lordfokas.precisiondynamics.devices.base.buffer.BufferFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TileEntityBase extends TileEntity{
    private final Map<Capability, Object> capabilities = new HashMap<>();

    protected final Buffer getBuffer(EnumVariant variant){
        switch(variant){
            case ENERGY: return new BufferEnergy();
            case FLUID: return new BufferFluid();
            case ITEM: return new BufferItem();
            default: return null;
        }
    }

    protected final void mapComponent(@Nonnull ICapabilityComponent component){
        Capability capability = component.getCapability();
        capabilities.put(capability, component);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capabilities.containsKey(capability);
    }

    @Nullable @Override @SuppressWarnings("unchecked") // bitch I know what I'm doing!
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return (T) capabilities.get(capability);
    }
}
