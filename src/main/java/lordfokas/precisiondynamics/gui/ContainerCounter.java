package lordfokas.precisiondynamics.gui;

import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import lordfokas.precisiondynamics.devices.base.buffer.Buffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCounter extends Container {
    private static final int SIZE = 1;
    private static final int STORED = 2;

    private TileEntityDeviceCounter te;

    public int size = -1;
    public int stored = -1;

    public ContainerCounter(IInventory inv, TileEntityDeviceCounter te){
        for(int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(inv, i, 8+(18*i), 150));
        for(int j = 0; j < 3; j++)
        for(int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(inv, i+(j*9)+9, 8+(18*i), 92+(18*j)));
        this.te = te;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        Buffer buffer = te.getDeviceBuffer();
        int sz = buffer.getSize();
        if(size != sz){
            update(SIZE, sz);
            size = sz;
        }
        int st = buffer.getStored();
        if(stored != st){
            update(STORED, st);
            stored = st;
        }
    }

    private void update(int prop, int val){ for(IContainerListener l : listeners) l.sendWindowProperty(this, prop, val); }
    @Override public boolean canInteractWith(EntityPlayer entityPlayer){ return true; }

    @Override
    public void updateProgressBar(int prop, int val) {
        if(prop == SIZE){
            size = val;
        }else if(prop == STORED){
            stored = val;
        }
    }
}
