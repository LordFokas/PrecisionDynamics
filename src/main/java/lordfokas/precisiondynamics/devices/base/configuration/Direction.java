package lordfokas.precisiondynamics.devices.base.configuration;

import net.minecraft.util.EnumFacing;

public enum Direction {
    TOP,
    BOTTOM,
    FRONT,
    BACK,
    LEFT,
    RIGHT;

    private static final Direction[][] matrix;

    static{
        matrix = new Direction[EnumFacing.values().length][EnumFacing.values().length];
        for(EnumFacing from : EnumFacing.values())
        for(EnumFacing to : EnumFacing.values()){
            matrix[from.ordinal()][to.ordinal()] = calc(from, to);
        }
    }

    public static Direction offset(EnumFacing from, EnumFacing to){
        return matrix[from.ordinal()][to.ordinal()];
    }

    private static Direction calc(EnumFacing from, EnumFacing to){
        if(to == EnumFacing.UP) return TOP;
        if(to == EnumFacing.DOWN) return BOTTOM;
        if(to == from.getOpposite()) return BACK;
        if(to == from) return FRONT;
        if(to.rotateYCCW() == from) return LEFT; // calling these on TO instead
        if(to.rotateY() == from) return  RIGHT; // avoids crashes
        return null;
    }

    public EnumFacing getFacing(EnumFacing from){
        switch(this){
            case TOP: return EnumFacing.UP;
            case BOTTOM: return EnumFacing.DOWN;
            case FRONT: return from;
            case BACK: return from.getOpposite();
            case LEFT: return from.rotateY();
            case RIGHT: return from.rotateYCCW();
            default: return null;
        }
    }

    public static Direction lookup(String name){
        for(Direction dir : values())
            if(dir.name().equals(name))
                return dir;
        return null;
    }
}
