package lordfokas.precisiondynamics.gui;

import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerCounter extends Container {
    private TileEntityDeviceCounter te;

    public ContainerCounter(IInventory playerInventory, TileEntityDeviceCounter te){
        this.te = te;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
}
