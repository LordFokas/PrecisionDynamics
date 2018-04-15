package lordfokas.precisiondynamics.devices.base;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.capabilities.Capability;

public enum Variant implements IStringSerializable {
    ENERGY("energy", "blockRedstone", "RF"),
    FLUID("fluid", Items.BUCKET, "mB"),
    ITEM("item", Blocks.CHEST, "I");

    public final String suffix;
    public final Object item;
    public final String unit;

    Variant(String suffix, Object item, String unit){
        this.suffix = suffix;
        this.item = item;
        this.unit = unit;
    }

    @Override public String getName() { return name().toLowerCase(); }
    public static final IProperty<Variant> PROPERTY = PropertyEnum.create("variant", Variant.class);
}
