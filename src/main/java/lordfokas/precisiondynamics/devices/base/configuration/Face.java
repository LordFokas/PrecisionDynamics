package lordfokas.precisiondynamics.devices.base.configuration;

public enum Face {
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
}
