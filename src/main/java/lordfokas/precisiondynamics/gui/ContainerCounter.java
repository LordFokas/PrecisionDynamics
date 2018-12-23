package lordfokas.precisiondynamics.gui;

import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;

public class ContainerCounter extends Container {
    private static final int SIZE = 1;
    private static final int STORED = 2;

    private TileEntityDeviceCounter te;

    public int size = -1;
    public int stored = -1;

    public ContainerCounter(IInventory playerInventory, TileEntityDeviceCounter te){
        this.te = te;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        Buffer buffer = te.getDeviceBuffer();
        int sz = buffer.getSize();
        if(size != sz){
            sendUpdate(SIZE, sz);
            size = sz;
        }
        int st = buffer.getStored();
        if(stored != st){
            sendUpdate(STORED, st);
            stored = st;
        }
    }

    private void sendUpdate(int prop, int val){
        for(IContainerListener listener : listeners) {
            listener.sendWindowProperty(this, prop, val);
        }
    }

    @Override
    public void updateProgressBar(int prop, int val) {
        if(prop == SIZE){
            size = val;
        }else if(prop == STORED){
            stored = val;
        }
    }
}
