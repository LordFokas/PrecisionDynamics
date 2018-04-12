package lordfokas.precisiondynamics.devices.base.buffer;

import lordfokas.precisiondynamics.devices.base.ICapabilityComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class Buffer<T extends Buffer<T>> implements ICapabilityComponent, INBTSerializable<NBTTagCompound> {
    @CapabilityInject(IEnergyStorage.class) protected static Capability<IEnergyStorage> CAPABILITY_ENERGY;
    @CapabilityInject(IFluidHandler.class) protected static Capability<IFluidHandler> CAPABILITY_FLUID;
    @CapabilityInject(IItemHandler.class) protected static Capability<IItemHandler> CAPABILITY_ITEM;
    protected static final int CAPACITY_ENERGY = 320_000;
    protected static final int CAPACITY_FLUID = 32_000;
    protected static final int CAPACITY_ITEM = 16;

    protected int input = 0, output = 0;
    protected boolean canInput = false, canOutput = false, isCounting = false;

    public abstract void refill(T other);
    public abstract NBTTagCompound serialize();
    public abstract void deserialize(NBTTagCompound data);

    public void setMode(boolean output, boolean input){
        canOutput = output;
        canInput = input;
    }

    public final int getOutputAmount() { return output; }
    public final int getInputAmount() { return input; }
    public final void resetAmounts(){ output = input = 0; }
    public final void startCounting(){ isCounting = true; }

    protected final int movedOut(int moved, boolean didMove){
        if(isCounting && didMove)
            output += moved;
        return moved;
    }

    protected final int movedIn(int moved, boolean didMove){
        if(isCounting && didMove)
            input += moved;
        return moved;
    }

    @Override
    public final NBTTagCompound serializeNBT() {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("output", output);
        data.setInteger("input", input);
        data.setBoolean("canOutput", canOutput);
        data.setBoolean("canInput", canInput);
        data.setTag("variant", serialize());
        return data;
    }

    @Override
    public final void deserializeNBT(NBTTagCompound data) {
        output = data.getInteger("output");
        input = data.getInteger("input");
        canOutput = data.getBoolean("canOutput");
        canInput = data.getBoolean("canInput");
        deserialize(data.getCompoundTag("variant"));
    }
}
