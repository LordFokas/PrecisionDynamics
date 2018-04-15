package lordfokas.precisiondynamics.devices.base;

import net.minecraftforge.common.capabilities.Capability;

public interface ICapabilityComponent<C> {
    Capability<C> getCapability();
}
