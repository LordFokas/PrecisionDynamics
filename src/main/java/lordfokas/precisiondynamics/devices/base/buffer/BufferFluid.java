package lordfokas.precisiondynamics.devices.base.buffer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class BufferFluid extends Buffer<IFluidHandler, BufferFluid> implements IFluidHandler {
    @CapabilityInject(IFluidHandler.class)
    private static Capability<IFluidHandler> CAPABILITY_FLUID;
    private static final int CAPACITY_FLUID = 32_000;

    private final FluidTank tank = new FluidTank(CAPACITY_FLUID){{ setCanFill(true); setCanDrain(true); }};

    @Override public void refill(BufferFluid other) { pushInto(other.tank); }
    @Override public void pushInto(IFluidHandler capability) { movedOut(refill(tank, capability), true); }
    @Override public void pullFrom(IFluidHandler capability) { movedIn(refill(capability, tank), true); }

    private int refill(IFluidHandler from, IFluidHandler into){
        int toMove = into.fill(from.drain(Integer.MAX_VALUE, false), false);
        if(toMove == 0) return 0;
        FluidStack drained = from.drain(toMove, true);
        into.fill(drained, true);
        return drained.amount;
    }

    @Override public Capability<IFluidHandler> getCapability() { return CAPABILITY_FLUID; }
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

    // Access methods for GUIs
    @Override public int getSize(){ return tank.getCapacity(); }
    @Override public int getStored(){ return tank.getFluidAmount(); }
}
