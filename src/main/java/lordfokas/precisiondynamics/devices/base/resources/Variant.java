package lordfokas.precisiondynamics.devices.base.resources;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.IStringSerializable;

public enum Variant implements IStringSerializable {
    ENERGY("energy", "blockRedstone", new Unit("RF")),
    FLUID("fluid", Items.BUCKET, new Unit("B", Unit.Scale.MILLI)),
    ITEM("item", Blocks.CHEST, new Unit("I"));

    public final String suffix;
    public final Object item;
    public final Unit unit;

    Variant(String suffix, Object item, Unit unit){
        this.suffix = suffix;
        this.item = item;
        this.unit = unit;
    }

    @Override public String getName() { return name().toLowerCase(); }
    public static final IProperty<Variant> PROPERTY = PropertyEnum.create("variant", Variant.class);
}
