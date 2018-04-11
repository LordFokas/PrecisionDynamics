package lordfokas.precisiondynamics;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabPrecisionDynamics extends CreativeTabs {

    public TabPrecisionDynamics(){
        super(PrecisionDynamics.MODID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(PrecisionDynamics.instance.prioritizer);
    }

    @Override
    public String getTranslatedTabLabel() {
        return PrecisionDynamics.NAME;
    }
}
