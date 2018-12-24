package lordfokas.precisiondynamics.packets;

import io.netty.buffer.ByteBuf;
import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import net.minecraft.util.math.BlockPos;

public class PacketUpdateCounterStats extends PacketBase.Block {
    private long throughput;
    private long historic;

    public PacketUpdateCounterStats(){}
    public PacketUpdateCounterStats(BlockPos pos, long throughput, long historic){
        super(pos);
        this.throughput = throughput;
        this.historic = historic;
    }

    @Override
    public void handleSelf() {
        ((TileEntityDeviceCounter)PrecisionDynamics.proxy.getClientWorld().getTileEntity(pos))
                .onUpdate(throughput, historic);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        throughput = buf.readLong();
        historic = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeLong(throughput);
        buf.writeLong(historic);
    }
}
