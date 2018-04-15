package lordfokas.precisiondynamics.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class PacketBase implements IMessage {
    public abstract void handleSelf();

    public abstract static class Block extends PacketBase{
        protected BlockPos pos;

        protected Block(){}
        protected Block(BlockPos pos){
            this.pos = pos;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            pos = BlockPos.fromLong(buf.readLong());
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeLong(pos.toLong());
        }
    }
}
