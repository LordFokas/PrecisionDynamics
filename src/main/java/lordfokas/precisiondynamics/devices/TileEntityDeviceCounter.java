package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.devices.base.Variant;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.TileEntityDevice;
import lordfokas.precisiondynamics.packets.PacketHandler;
import lordfokas.precisiondynamics.packets.PacketUpdateCounterStats;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileEntityDeviceCounter extends TileEntityDevice implements ITickable {
    private final Buffer<?, ?> buffer;
    public long historic = 0;
    public int throughput = 0;
    private boolean autopush = false, autopull = false;

    public TileEntityDeviceCounter(Variant variant) {
        super(variant);
        buffer = getBuffer();
        buffer.setMode(true, true);
        buffer.startCounting();
        mapComponent(buffer);
    }

    @Override
    public void update() {
        if(world.isRemote) return;
        if(autopush) pushAdjacent(buffer);
        if(autopull) pullAdjacent(buffer);
        if(world.getWorldTime() % 20 == 0){
            throughput = buffer.getOutputAmount();
            buffer.resetAmounts();
            historic += throughput;
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

    public void onUpdate(int throughput, long historic){
        this.throughput = throughput;
        this.historic = historic;
    }
}
