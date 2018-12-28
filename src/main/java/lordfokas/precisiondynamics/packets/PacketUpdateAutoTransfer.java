package lordfokas.precisiondynamics.packets;

import io.netty.buffer.ByteBuf;
import lordfokas.precisiondynamics.devices.base.TileEntityBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketUpdateAutoTransfer extends PacketBase.WorldBlock {
    private boolean autopull, autopush;

    public PacketUpdateAutoTransfer(){}
    public PacketUpdateAutoTransfer(World world, BlockPos pos, boolean autopull, boolean autopush){
        super(world, pos);
        this.autopull = autopull;
        this.autopush = autopush;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        autopull = buf.readBoolean();
        autopush = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(autopull);
        buf.writeBoolean(autopush);
    }

    @Override
    public void handleSelf() {
        ((TileEntityBase)world.getTileEntity(pos)).updateAutoTransfer(autopull, autopush);
    }
}
