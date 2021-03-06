package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.Textures;
import lordfokas.precisiondynamics.devices.base.TileEntityDevice;
import lordfokas.precisiondynamics.devices.base.configuration.Face;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import lordfokas.precisiondynamics.devices.base.resources.Variant;
import lordfokas.precisiondynamics.packets.PacketHandler;
import lordfokas.precisiondynamics.packets.PacketUpdateCounterStats;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityDeviceCounter extends TileEntityDevice implements ITickable {
    private static final int CYCLE_TICKS = 20;
    private static final int TP_FACTOR = 10_000_000;
    private Buffer<?, ?> buffer;
    public long historic = 0;
    public long throughput = 0;

    public TileEntityDeviceCounter() {
        super(Face.NONE, Face.BLUE, Face.ORANGE);
    }

    public TileEntityDeviceCounter(Variant variant) {
        this();
        buffer = getBuffer(variant);
        buffer.setMode(true, true);
        buffer.startCounting();
        mapComponent(buffer, Face.BLUE, Face.ORANGE);
    }

    @Override
    public void update() {
        if(world.isRemote) return;
        if(getTransferOut()) pushAdjacent(buffer, Face.ORANGE);
        if(getTransferIn())  pullAdjacent(buffer, Face.BLUE);
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
    public String getRate(){ return getVariant().unit.format( ((double)throughput)/TP_FACTOR) + "/t"; }
    public String getTotal(){ return getVariant().unit.format(historic); }
    public Buffer getDeviceBuffer(){ return buffer; }

    @Override
    protected ResourceLocation getFrontTexture(int pass) {
        if(pass == 0) return Textures.FACE_COUNTER;
        else switch(getVariant()){
            case ENERGY: return Textures.FACE_COUNTER_ENERGY;
            case FLUID: return Textures.FACE_COUNTER_FLUID;
            case ITEM: return Textures.FACE_COUNTER_ITEM;
            default: return null;
        }
    }

    @Override
    protected void setWorldCreate(World p_setWorldCreate_1_){
        super.setWorldCreate(p_setWorldCreate_1_);
    }

    // [MC] TE Serialization
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setLong("historic", historic);
        nbt.setLong("throughput", throughput);
        nbt.setTag("buffer", Buffer.wrap(buffer));
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        historic = nbt.getLong("historic");
        throughput = nbt.getLong("throughput");
        buffer = Buffer.unwrap(nbt.getCompoundTag("buffer"));
    }
}
