package lordfokas.precisiondynamics.devices.base;

import lordfokas.precisiondynamics.devices.base.buffer.Buffer;

import java.util.List;

public class TileEntityDevice extends TileEntityBase {
    public final Variant variant;

    protected TileEntityDevice(Variant variant){
        this.variant = variant;
    }

    protected final Buffer getBuffer(){
        return getBuffer(variant);
    }
}
