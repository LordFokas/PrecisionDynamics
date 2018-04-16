package lordfokas.precisiondynamics.devices.base;

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
        if(amount != 0){
            while(amount < 1 && scale != Scale.PICO){
                amount *= 1000;
                scale = scale.prev();
            }
            while(amount > 1000 && scale != Scale.PETA){
                amount /= 1000;
                scale = scale.next();
            }
            String str = amount+"";
            str = str.substring(0, Math.min(4, str.length()));
            if(str.endsWith(".")) str = str.replace(".", "");
            return str + " " + scale.prefix + name;
        }else{
            return "0 " + scale.prefix + name;
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
