package lordfokas.precisiondynamics.devices.base.configuration;

import net.minecraft.util.IStringSerializable;

public enum Face implements IStringSerializable {
    BLUE(Operation.INPUT),
    GREEN(Operation.INPUT),
    PURPLE(Operation.INPUT),
    ORANGE(Operation.OUTPUT),
    RED(Operation.OUTPUT),
    YELLOW(Operation.OUTPUT),
    NONE(null);

    private final Operation operation;
    Face(Operation operation){
        this.operation = operation;
    }

    public boolean allows(Operation operation){
        return this.operation == operation;
    }

    @Override
    public String getName(){
        return name().toLowerCase();
    }
}
