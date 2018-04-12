package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.devices.base.EnumVariant;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.TileEntityDevice;
import net.minecraft.util.ITickable;

public class TileEntityDeviceCounter extends TileEntityDevice implements ITickable {
    private final Buffer buffer;

    public TileEntityDeviceCounter(EnumVariant variant) {
        super(variant);
        buffer = getBuffer();
        buffer.setMode(true, true);
        buffer.startCounting();
        mapComponent(buffer);
    }

    @Override
    public void update() {
        if(world.isRemote) return;
        if(world.getWorldTime() % 20 == 0){
            int out = buffer.getOutputAmount();
            int in = buffer.getInputAmount();
            buffer.resetAmounts();
            System.out.println("In: " + in + " / Out: " + out);
        }
    }
}
