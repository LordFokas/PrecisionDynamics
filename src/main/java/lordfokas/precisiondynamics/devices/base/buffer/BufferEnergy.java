package lordfokas.precisiondynamics.devices.base.buffer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class BufferEnergy extends Buffer<IEnergyStorage, BufferEnergy> implements IEnergyStorage {
    @CapabilityInject(IEnergyStorage.class)
    private static Capability<IEnergyStorage> CAPABILITY_ENERGY;
    private static final int CAPACITY_ENERGY = 320_000;

    private EnergyStorage storage = new EnergyStorage(CAPACITY_ENERGY);

    @Override public void refill(BufferEnergy other) { pushInto(other.storage);}
    @Override public void pushInto(IEnergyStorage capability) { movedOut(refill(storage, capability), true); }
    @Override public void pullFrom(IEnergyStorage capability) { movedIn(refill(capability, storage), true); }

    private int refill(IEnergyStorage from, IEnergyStorage into){
        int toMove = from.extractEnergy(into.receiveEnergy(from.getEnergyStored(), true), true);
        return from.extractEnergy(into.receiveEnergy(toMove, false), false);
    }

    @Override public Capability<IEnergyStorage> getCapability() { return CAPABILITY_ENERGY; }

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

    // Access methods for GUIs
    @Override public int getSize(){ return storage.getMaxEnergyStored(); }
    @Override public int getStored(){ return storage.getEnergyStored(); }
}
