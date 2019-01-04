package lordfokas.precisiondynamics.packets;

import io.netty.buffer.ByteBuf;
import lordfokas.precisiondynamics.devices.base.TileEntityBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketUpdateColorInterface extends PacketBase.WorldBlock {
    private byte[] sides = new byte[6];

    public PacketUpdateColorInterface(){}
    public PacketUpdateColorInterface(World world, BlockPos pos, byte[] sides){
        super(world, pos);
        this.sides = sides;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        buf.readBytes(sides);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBytes(sides);
    }

    @Override
    public void handleSelf() {
        ((TileEntityBase)world.getTileEntity(pos)).updateColorInterface(sides);
    }
}
