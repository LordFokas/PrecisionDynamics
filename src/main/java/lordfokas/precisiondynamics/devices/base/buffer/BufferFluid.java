package lordfokas.precisiondynamics.devices.base.buffer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class BufferFluid extends Buffer<BufferFluid> implements IFluidHandler {
    private final FluidTank tank = new FluidTank(CAPACITY_FLUID){{ setCanFill(true); setCanDrain(true); }};

    @Override
    public void refill(BufferFluid other) {
        int filled = other.tank.fill(tank.getFluid(), true);
        tank.drain(filled, true);
    }

    @Override public Capability getCapability() { return CAPABILITY_FLUID; }
    @Override public NBTTagCompound serialize() { return tank.writeToNBT(new NBTTagCompound()); }
    @Override public void deserialize(NBTTagCompound data) { tank.readFromNBT(data); }
    @Override public IFluidTankProperties[] getTankProperties() { return tank.getTankProperties(); }

    @Override
    public int fill(FluidStack fluidStack, boolean b) {
        if(!canInput) return 0;
        return movedIn(tank.fill(fluidStack, b), b);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack fluidStack, boolean mov) {
        if(!canOutput) return null;
        return movedOut(tank.drain(fluidStack, mov), mov);
    }

    @Nullable @Override
    public FluidStack drain(int i, boolean mov) {
        if(!canOutput) return null;
        return movedOut(tank.drain(i, mov), mov);
    }

    @Nullable
    private FluidStack movedOut(@Nullable FluidStack fs, boolean b){
        if(fs != null) movedOut(fs.amount, b);
        return fs;
    }
}
