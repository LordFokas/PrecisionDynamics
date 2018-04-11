package lordfokas.precisiondynamics.devices;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBlockDevice extends ItemBlock {
    private final BlockDevice block;

    public ItemBlockDevice(BlockDevice block) {
        super(block);
        this.block = block;
        setRegistryName(block.getRegistryName());
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        for(EnumVariant variant : EnumVariant.values()){
            list.add(block.device.getStack(variant));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return block.getUnlocalizedName() + "_" + EnumVariant.values()[stack.getMetadata()].suffix;
    }
}
