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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TileEntityBase extends TileEntity{
    private final Map<Capability, Object> capabilities = new HashMap<>();

    protected final Buffer getBuffer(Variant variant){
        switch(variant){
            case ENERGY: return new BufferEnergy();
            case FLUID: return new BufferFluid();
            case ITEM: return new BufferItem();
            default: return null;
        }
    }

    protected final <T> void pushAdjacent(Buffer<T, ?> buffer){
        List<T> capabilities = getAdjacentCapabilities(buffer, Operation.OUTPUT);
        for(T capability : capabilities){
            buffer.pushInto(capability);
        }
    }

    protected final <T> void pullAdjacent(Buffer<T, ?> buffer){
        List<T> capabilities = getAdjacentCapabilities(buffer, Operation.INPUT);
        for(T capability : capabilities){
            buffer.pullFrom(capability);
        }
    }

    protected final <T> List<T> getAdjacentCapabilities(Buffer<T, ?> buffer, Operation intent){
        return getAdjacentCapabilities(buffer.getCapability(), intent);
    }

    protected final <T> List<T> getAdjacentCapabilities(Capability<T> capability, Operation intent){
        List<T> list = new LinkedList<>();
        for(EnumFacing facing : EnumFacing.values()){
            TileEntity adjacent = world.getTileEntity(getPos().offset(facing));
            if(adjacent == null) continue;
            EnumFacing side = facing.getOpposite();
            if(adjacent.hasCapability(capability, side)){
                list.add(adjacent.getCapability(capability, side));
            }
        }
        return list;
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
