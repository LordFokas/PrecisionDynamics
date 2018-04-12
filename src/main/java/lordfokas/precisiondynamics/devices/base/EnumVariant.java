package lordfokas.precisiondynamics.devices.base;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.IStringSerializable;

public enum EnumVariant implements IStringSerializable {
    ENERGY("energy", "blockRedstone"),
    FLUID("fluid", Items.BUCKET),
    ITEM("item", Blocks.CHEST);

    public final String suffix;
    public final Object item;

    EnumVariant(String suffix, Object item){
        this.suffix = suffix;
        this.item = item;
    }

    @Override public String getName() { return name().toLowerCase(); }
    public static final IProperty<EnumVariant> PROPERTY = PropertyEnum.create("variant", EnumVariant.class);
}
