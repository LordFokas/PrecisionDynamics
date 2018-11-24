package lordfokas.precisiondynamics.devices.base.resources;

public class Unit {
    public final String name;
    public final Scale base;

    public Unit(String name){ this(name, Scale.ONE); }
    public Unit(String name, Scale base){
        this.name = name;
        this.base = base;
    }

    public String format(double amount){
        Scale scale = base;
        if(amount != 0){ // Get the best possible scale for readability...
            while(amount < 1 && scale != Scale.PICO){
                amount *= 1000;
                scale = scale.prev();
            }
            while(amount > 1000 && scale != Scale.PETA){
                amount /= 1000;
                scale = scale.next();
            }
            amount += 0.000001; // Why properly round stuff when you can just hack it?
            String str = amount+""; // Dirty string conversion.
            str = str.substring(0, Math.min(4, str.length())); // Chop the end.
            if(str.endsWith(".")) str = str.replace(".", ""); // Chop trailing dots.
            return str + " " + scale.prefix + name; // Assemble.
        }else{
            return "0 " + scale.prefix + name; // It's zero! No need for math here!
        }
    }

    public enum Scale{
        PICO("p"),
        NANO("n"),
        MICRO("u"),
        MILLI("m"),
        ONE(""),
        KILO("k"),
        MEGA("M"),
        GIGA("G"),
        TERA("T"),
        PETA("P");

        public final String prefix;
        Scale(String prefix){ this.prefix = prefix; }
        public Scale prev(){ return values()[ordinal()-1]; }
        public Scale next(){ return values()[ordinal()+1]; }
    }
}
