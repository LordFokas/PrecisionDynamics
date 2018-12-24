package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.devices.base.configuration.Face;
import lordfokas.precisiondynamics.devices.base.resources.Variant;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.TileEntityDevice;
import lordfokas.precisiondynamics.packets.PacketHandler;
import lordfokas.precisiondynamics.packets.PacketUpdateCounterStats;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileEntityDeviceCounter extends TileEntityDevice implements ITickable {
    private static final int CYCLE_TICKS = 20;
    private static final int TP_FACTOR = 10_000_000;
    private final Buffer<?, ?> buffer;
    public long historic = 0;
    public long throughput = 0;
    private boolean autopush = true, autopull = false;

    public TileEntityDeviceCounter(Variant variant) {
        super(variant);
        buffer = getBuffer();
        buffer.setMode(true, true);
        buffer.startCounting();
        mapComponent(buffer, Face.BLUE, Face.ORANGE);
    }

    @Override
    public void update() {
        if(world.isRemote) return;
        if(autopush){
            for(int i = 0; i < 10; i++)
            pushAdjacent(buffer, Face.ORANGE);
        }
        if(autopull) pullAdjacent(buffer, Face.BLUE);
        if(world.getWorldTime() % CYCLE_TICKS == 0){
            throughput = buffer.getOutputAmount();
            buffer.resetAmounts();
            historic += throughput;
            throughput = throughput * TP_FACTOR / CYCLE_TICKS;
            updateEligibleClients();
        }
    }

    private void updateEligibleClients(){
        List<EntityPlayerMP> players = getEligibleClients();
        if(players.size() == 0) return;
        PacketHandler.INSTANCE.send(new PacketUpdateCounterStats(pos, throughput, historic), players);
    }

    private List<EntityPlayerMP> getEligibleClients(){
        return world.getPlayers(EntityPlayerMP.class, player -> pos.distanceSq(player.getPosition()) <= 225 );
    }

    public void onUpdate(long throughput, long historic){
        this.throughput = throughput;
        this.historic = historic;
    }

    public int getFacingAngle(){
        int angle = 0;
        EnumFacing facing = ((BlockDevice)blockType).getFacing(world.getBlockState(pos));
        if(facing.getAxis() == EnumFacing.Axis.X) angle += 90;
        if(facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) angle += 180;
        return angle;
    }

    // Access methods for GUIs
    public String getRate(){ return variant.unit.format( ((double)throughput)/TP_FACTOR) + "/t"; }
    public String getTotal(){ return variant.unit.format(historic); }
    public Buffer getDeviceBuffer(){ return buffer; }
}
