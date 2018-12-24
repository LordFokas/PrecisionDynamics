package lordfokas.precisiondynamics.devices.base;

import lordfokas.precisiondynamics.devices.BlockDevice;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.buffer.BufferEnergy;
import lordfokas.precisiondynamics.devices.base.buffer.BufferItem;
import lordfokas.precisiondynamics.devices.base.buffer.BufferFluid;
import lordfokas.precisiondynamics.devices.base.configuration.Direction;
import lordfokas.precisiondynamics.devices.base.configuration.Face;
import lordfokas.precisiondynamics.devices.base.configuration.Operation;
import lordfokas.precisiondynamics.devices.base.resources.Variant;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TileEntityBase extends TileEntity{
    private final Map<Direction, Face> ioConfig = new EnumMap<>(Direction.class);
    private final Map<Face, List<ICapabilityComponent>> ioComponents = new EnumMap<>(Face.class);

    protected TileEntityBase(){
        ioComponents.put(Face.NONE, new LinkedList<>());

        // TODO: remove test assignments
        ioConfig.put(Direction.LEFT, Face.BLUE);
        ioConfig.put(Direction.RIGHT, Face.ORANGE);
    }

    /** Get a standard buffer implementation corresponding to this device's variant (fluid, item, energy ...). */
    protected final Buffer getBuffer(Variant variant){
        switch(variant){
            case ENERGY: return new BufferEnergy();
            case FLUID: return new BufferFluid();
            case ITEM: return new BufferItem();
            default: return null;
        }
    }

    /** Attempt to push resources from the buffer into blocks adjacent to these faces (interface colors). */
    protected final <T> void pushAdjacent(Buffer<T, ?> buffer, Face... faces){
        List<T> capabilities = getAdjacentCapabilities(buffer, faces);
        for(T capability : capabilities){
            buffer.pushInto(capability);
        }
    }

    /** Attempt to pull resources into the buffer from blocks adjacent to these faces (interface colors). */
    protected final <T> void pullAdjacent(Buffer<T, ?> buffer, Face... faces){
        List<T> capabilities = getAdjacentCapabilities(buffer, faces);
        for(T capability : capabilities){
            buffer.pullFrom(capability);
        }
    }

    /** @return all instances of this buffer's capability in blocks adjacent to these faces (interface colors). */
    protected final <T> List<T> getAdjacentCapabilities(Buffer<T, ?> buffer, Face... faces){
        return getAdjacentCapabilities(buffer.getCapability(), faces);
    }

    /** @return all instances of the capability in blocks adjacent to these faces (interface colors). */
    protected final <T> List<T> getAdjacentCapabilities(Capability<T> capability, Face ... faces){
        List<T> list = new LinkedList<>();
        for(EnumFacing facing : getFacingsFromFaces(faces)){
            TileEntity adjacent = world.getTileEntity(getPos().offset(facing));
            if(adjacent == null) continue;
            EnumFacing side = facing.getOpposite();
            if(adjacent.hasCapability(capability, side)){
                list.add(adjacent.getCapability(capability, side));
            }
        }
        return list;
    }

    /** @return all the directions these faces (interface colors) exist in. */
    protected final List<EnumFacing> getFacingsFromFaces(Face ... faces){
        List<EnumFacing> facings = new LinkedList<>();
        EnumFacing from = getFacing();
        for(Map.Entry<Direction, Face> entry : ioConfig.entrySet())
        for(Face face : faces){
            if(face == entry.getValue()){
                facings.add(entry.getKey().getFacing(from));
                break;
            }
        }
        return facings;
    }

    /** Maps a component to one or many faces (interface colors). */
    protected final void mapComponent(@Nonnull ICapabilityComponent component, Face ... faces){
        for(Face face : faces){
            if(!ioComponents.containsKey(face))
                ioComponents.put(face, new ArrayList<>());
            ioComponents.get(face).add(component);
        }
    }

    /** @return what direction this device is facing. */
    protected EnumFacing getFacing(){
        if(blockType instanceof BlockDevice) return ((BlockDevice)blockType).getFacing(world.getBlockState(pos));
        return EnumFacing.NORTH;
    }

    /** @return all components mapped to the face (interface color) that might exist in this direction. */
    private List<ICapabilityComponent> getComponentsOnSide(EnumFacing facing){
        Direction direction = Direction.offset(getFacing(), facing);
        Face face = ioConfig.get(direction);
        return ioComponents.get(face);
    }

    /** Check if a capability exists in a face. This is standard MC TE interfacing. */
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return getCapability(capability, facing) != null;
    }

    /** Get a capability from a face. This is standard MC TE interfacing. */
    @Nullable @Override @SuppressWarnings("unchecked") // bitch I know what I'm doing!
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        List<ICapabilityComponent> components = getComponentsOnSide(facing);
        if(components != null)
            for(ICapabilityComponent component : components){
                if(component.getCapability() == capability){
                    if(component instanceof Buffer) { // If the component is a buffer, only allow that face's operation.
                        Direction direction = Direction.offset(getFacing(), facing);
                        Face face = ioConfig.get(direction);
                        ((Buffer) component).setMode(face.allows(Operation.OUTPUT), face.allows(Operation.INPUT));
                    }
                    return (T) component;
                }
            }
        return null;
    }
}
