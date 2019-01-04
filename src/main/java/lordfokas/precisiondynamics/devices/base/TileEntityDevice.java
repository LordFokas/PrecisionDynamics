package lordfokas.precisiondynamics.devices.base;

import lordfokas.precisiondynamics.devices.BlockDevice;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.configuration.Face;
import lordfokas.precisiondynamics.devices.base.resources.Variant;

public class TileEntityDevice extends TileEntityBase {

    protected TileEntityDevice(Face... allowedFaces){
        super(allowedFaces);
    }

    public Variant getVariant(){
        return world.getBlockState(pos).getValue(BlockDevice.VARIANT);
    }

    protected final Buffer getBuffer(){
        return getBuffer(getVariant());
    }
}
