package lordfokas.precisiondynamics.devices.base;

import lordfokas.precisiondynamics.devices.base.buffer.Buffer;

public class TileEntityDevice extends TileEntityBase {
    protected final EnumVariant variant;

    protected TileEntityDevice(EnumVariant variant){
        this.variant = variant;
    }

    protected final Buffer getBuffer(){
        return getBuffer(variant);
    }
}
