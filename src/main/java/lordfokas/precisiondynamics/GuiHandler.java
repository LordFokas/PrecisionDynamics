package lordfokas.precisiondynamics;

import lordfokas.precisiondynamics.devices.TileEntityDeviceCounter;
import lordfokas.precisiondynamics.gui.ContainerCounter;
import lordfokas.precisiondynamics.gui.GuiCounter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityDeviceCounter) {
            return new ContainerCounter(player.inventory, (TileEntityDeviceCounter) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityDeviceCounter) {
            TileEntityDeviceCounter containerTileEntity = (TileEntityDeviceCounter) te;
            return new GuiCounter(containerTileEntity, new ContainerCounter(player.inventory, containerTileEntity));
        }
        return null;
    }
}
