package lordfokas.precisiondynamics.packets;

import lordfokas.precisiondynamics.PrecisionDynamics;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.LinkedList;
import java.util.List;

public enum PacketHandler implements IMessageHandler<PacketBase, PacketBase> {
    INSTANCE;

    private SimpleNetworkWrapper network;
    private final List<Class<? extends PacketBase>> clientPackets = new LinkedList<Class<? extends PacketBase>>(){{}};
    private final List<Class<? extends PacketBase>> serverPackets = new LinkedList<Class<? extends PacketBase>>(){{
        add(PacketUpdateCounterStats.class);
    }};

    public void init(){
        network = NetworkRegistry.INSTANCE.newSimpleChannel(PrecisionDynamics.MODID);
        int i = 0;
        for(Class<? extends PacketBase> packet : clientPackets){
            network.registerMessage(this, packet, i++, Side.SERVER);
        }
        for(Class<? extends PacketBase> packet : serverPackets){
            network.registerMessage(this, packet, i++, Side.CLIENT);
        }
    }

    @Override
    public PacketBase onMessage(PacketBase packet, MessageContext messageContext) {
        PrecisionDynamics.proxy.schedule(packet::handleSelf);
        return null;
    }

    public void send(PacketBase packet, List<EntityPlayerMP> players){
        for(EntityPlayerMP player : players)
            network.sendTo(packet, player);
    }
}
