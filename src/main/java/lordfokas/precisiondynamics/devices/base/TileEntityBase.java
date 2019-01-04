package lordfokas.precisiondynamics.devices.base;

import cofh.api.tileentity.IReconfigurableFacing;
import cofh.api.tileentity.IReconfigurableSides;
import cofh.api.tileentity.ITransferControl;
import cofh.core.render.ISidedTexture;
import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.Textures;
import lordfokas.precisiondynamics.devices.BlockDevice;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.buffer.BufferEnergy;
import lordfokas.precisiondynamics.devices.base.buffer.BufferItem;
import lordfokas.precisiondynamics.devices.base.buffer.BufferFluid;
import lordfokas.precisiondynamics.devices.base.configuration.Direction;
import lordfokas.precisiondynamics.devices.base.configuration.Face;
import lordfokas.precisiondynamics.devices.base.configuration.Operation;
import lordfokas.precisiondynamics.devices.base.resources.Variant;
import lordfokas.precisiondynamics.packets.PacketHandler;
import lordfokas.precisiondynamics.packets.PacketUpdateAutoTransfer;
import lordfokas.precisiondynamics.packets.PacketUpdateColorInterface;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TileEntityBase extends TileEntity
implements IReconfigurableSides, IReconfigurableFacing, ITransferControl, ISidedTexture {
    private final Map<Direction, Face> ioConfig = new EnumMap<>(Direction.class);
    private final Map<Face, List<ICapabilityComponent>> ioComponents = new EnumMap<>(Face.class);
    private final Face[] allowedFaces;
    private boolean autopush = true, autopull = false;

    protected TileEntityBase(Face... allowedFaces){
        this.allowedFaces = allowedFaces;
        ioComponents.put(Face.NONE, new LinkedList<>());
        resetColorInterfaces();

        /*
        // TODO: remove test assignments
        ioConfig.put(Direction.LEFT, Face.BLUE);
        ioConfig.put(Direction.RIGHT, Face.ORANGE);*/
    }

    @Override
    public void onLoad(){
        // System.err.printf("Loaded %d interfaces on %s side\n", ioConfig.size(), world.isRemote ? "Client" : "Server");
    }

    protected final void resetColorInterfaces(){ for(Direction d : Direction.values()) ioConfig.put(d, Face.NONE); }
    public Face getColorInterface(Direction dir){ return ioConfig.get(dir); }

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
        EnumFacing from = getDeviceFacing();
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
    public EnumFacing getDeviceFacing(){
        if(blockType instanceof BlockDevice) return ((BlockDevice)blockType).getFacing(world.getBlockState(pos));
        return EnumFacing.NORTH;
    }

    /** @return all components mapped to the face (interface color) that might exist in this direction. */
    private List<ICapabilityComponent> getComponentsOnSide(EnumFacing facing){
        Direction direction = Direction.offset(getDeviceFacing(), facing);
        Face face = ioConfig.get(direction);
        return ioComponents.get(face);
    }

    /** Check if a capability exists in a face. This is standard MC TE interfacing. */
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
        return getCapability(capability, facing) != null;
    }

    /** Get a capability from a face. This is standard MC TE interfacing. */
    @Nullable @Override @SuppressWarnings("unchecked") // bitch I know what I'm doing!
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
        List<ICapabilityComponent> components = getComponentsOnSide(facing);
        if(components != null)
            for(ICapabilityComponent component : components)
                if(component.getCapability() == capability){
                    if(component instanceof Buffer){ // If the component is a buffer, only allow that face's operation.
                        Direction direction = Direction.offset(getDeviceFacing(), facing);
                        Face face = ioConfig.get(direction);
                        ((Buffer) component).setMode(face.allows(Operation.OUTPUT), face.allows(Operation.INPUT));
                    }
                    return (T) component;
                }
        return null;
    }

    protected void triggerBlockChanged(){
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 2);
    }

    // [CoFH] IReconfigurableSides
    @Override
    public boolean setSide(int side, int config){
        if(side == getFacing()) return false;
        config = (config + allowedFaces.length) % allowedFaces.length;
        Face face = allowedFaces[config];
        EnumFacing facing = EnumFacing.values()[side];
        Direction dir = Direction.offset(getDeviceFacing(), facing);
        ioConfig.put(dir, face);
        return sendColorInterfaceUpdate();
    }

    @Override public boolean decrSide(int side){ return setSide(side, getSide(side) - 1); }
    @Override public boolean incrSide(int side){ return setSide(side, getSide(side) + 1); }
    @Override public boolean resetSides(){ resetColorInterfaces(); return sendColorInterfaceUpdate(); }
    @Override public int getNumConfig(int side){ return side == getFacing() ? 0 : allowedFaces.length; }
    private int getSide(int side){ return side == getFacing() ? 0 : indexOfSide(side); }

    private int indexOfSide(int side){
        EnumFacing facing = EnumFacing.values()[side];
        Direction dir = Direction.offset(getDeviceFacing(), facing);
        Face face = ioConfig.get(dir);
        for(int idx = 0; idx < allowedFaces.length; idx++)
            if(allowedFaces[idx] == face) return idx;
        return -1;
    }

    private boolean sendColorInterfaceUpdate(){
        if(world.isRemote){
            byte[] sides = new byte[6];
            for(int side = 0; side < 6; side++)
                sides[side] = ((byte)(getSide(side) & 0x0F));
            PacketHandler.INSTANCE.send(new PacketUpdateColorInterface(world, pos, sides));
        }
        return true;
    }

    public void updateColorInterface(byte[] sides){
        for(int side = 0; side < sides.length; side++){
            setSide(side, sides[side]);
        }

        // TODO: update self & neighbor blocks!
        // world.notifyNeighborsOfStateChange(pos, blockType, true);
        triggerBlockChanged();
    }

    // [CoFH] IReconfigurableFacing
    @Override public int getFacing(){ return getDeviceFacing().ordinal(); }
    @Override public boolean allowYAxisFacing(){ return false; } // NEVER!
    @Override public boolean rotateBlock(){ return false; } // Maybe some day. If you behave well.
    @Override public boolean setFacing(int side, boolean alternate){ return false; } // Not today.

    // [CoFH] ITransferControl
    @Override public boolean hasTransferIn(){ return true; }
    @Override public boolean hasTransferOut(){ return true; }
    @Override public boolean getTransferIn(){ return autopull; }
    @Override public boolean getTransferOut(){ return autopush; }
    @Override public boolean setTransferIn(boolean autopull){ this.autopull = autopull; return sendAutoTransferUpdate(); }
    @Override public boolean setTransferOut(boolean autopush){ this.autopush = autopush; return sendAutoTransferUpdate(); }

    private boolean sendAutoTransferUpdate(){
        if(world.isRemote)
            PacketHandler.INSTANCE.send(new PacketUpdateAutoTransfer(world, pos, autopull, autopush));
        return true;
    }

    public void updateAutoTransfer(boolean autopull, boolean autopush){
        this.autopull = autopull;
        this.autopush = autopush;
    }

    // [CoFH] ISidedTexture
    @Override public int getNumPasses(){ return 2; }

    @Override
    public TextureAtlasSprite getTexture(int side, int pass){
        return PrecisionDynamics.proxy.getBlockSprite(selectTexture(side, pass));
    }

    protected ResourceLocation getFrontTexture(int pass){ return Textures.DEVICE_SIDE; }

    private ResourceLocation selectTexture(int side, int pass){
        if(side == getFacing())
            return getFrontTexture(pass);
        if(pass == 0){
            switch(side){
                case 0:  return Textures.DEVICE_BOTTOM;
                case 1:  return Textures.DEVICE_TOP;
                default: return Textures.DEVICE_SIDE;
            }
        }else{
            Direction dir = Direction.offset(getDeviceFacing(), EnumFacing.values()[side]);
            Face face = ioConfig.get(dir);
            switch (face) {
                case BLUE:   return Textures.IO_BLUE;
                case GREEN:  return Textures.IO_GREEN;
                case PURPLE: return Textures.IO_PURPLE;
                case ORANGE: return Textures.IO_ORANGE;
                case RED:    return Textures.IO_RED;
                case YELLOW: return Textures.IO_YELLOW;
            }
        }
        return Textures.NONE;
    }

    // [MC] TE Serialization
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setBoolean("autopush", autopush);
        nbt.setBoolean("autopull", autopull);
        nbt.setTag("ioConfig", writeIOConfig(new NBTTagCompound()));
        return nbt;
    }

    private NBTTagCompound writeIOConfig(NBTTagCompound nbt){
        for(Map.Entry<Direction, Face> entry : ioConfig.entrySet()){
            nbt.setInteger(entry.getKey().name(), entry.getValue().ordinal());
        }
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        autopush = nbt.getBoolean("autopush");
        autopull = nbt.getBoolean("autopull");
        readIOConfig(nbt.getCompoundTag("ioConfig"));
    }

    private void readIOConfig(NBTTagCompound nbt){
        ioConfig.clear();
        for(String name : nbt.getKeySet()){
            ioConfig.put(Direction.lookup(name), Face.values()[nbt.getInteger(name)]);
        }
    }

    @Override
    public final void deserializeNBT(NBTTagCompound nbt){
        super.deserializeNBT(nbt);
        triggerBlockChanged();
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt){
        super.handleUpdateTag(nbt);
        triggerBlockChanged();
    }

    @Override // for when the TE is created by invoking the empty constructor with reflection.
    protected void setWorldCreate(World world){
        this.world = world;
    }

    @Override // Are you fucking serious Mojang?
    public NBTTagCompound getUpdateTag(){
        return writeToNBT(new NBTTagCompound());
    }
}
