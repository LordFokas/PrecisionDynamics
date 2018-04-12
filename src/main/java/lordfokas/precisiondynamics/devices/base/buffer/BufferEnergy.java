package lordfokas.precisiondynamics.devices.base.buffer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class BufferEnergy extends Buffer<BufferEnergy> implements IEnergyStorage {
    private EnergyStorage storage = new EnergyStorage(CAPACITY_ENERGY);

    @Override
    public void refill(BufferEnergy other) {
        int filled = other.storage.receiveEnergy(storage.getEnergyStored(), false);
        storage.extractEnergy(filled, false);
    }

    @Override public Capability getCapability() { return CAPABILITY_ENERGY; }

    @Override
    public NBTTagCompound serialize() {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("capacity", storage.getMaxEnergyStored());
        data.setInteger("stored", storage.getEnergyStored());
        return data;
    }

    @Override
    public void deserialize(NBTTagCompound data) {
        int capacity = data.getInteger("capacity");
        int stored = data.getInteger("stored");
        storage = new EnergyStorage(capacity, capacity, capacity, stored);
    }

    @Override public int receiveEnergy(int i, boolean sim) { return movedIn(storage.receiveEnergy(i, sim), !sim); }
    @Override public int extractEnergy(int i, boolean sim) { return movedOut(storage.extractEnergy(i, sim), !sim); }
    @Override public int getEnergyStored() { return storage.getEnergyStored(); }
    @Override public int getMaxEnergyStored() { return storage.getMaxEnergyStored(); }
    @Override public boolean canExtract() { return canOutput; }
    @Override public boolean canReceive() { return canInput; }
}
