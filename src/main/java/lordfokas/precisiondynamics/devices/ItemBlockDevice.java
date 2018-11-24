package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.base.resources.Variant;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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
        if(tab == PrecisionDynamics.instance.tab)
        for(Variant variant : Variant.values()){
            list.add(block.device.getStack(variant));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return block.getUnlocalizedName() + "_" + Variant.values()[stack.getMetadata()].suffix;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        list.add(I18n.format(getUnlocalizedName(stack) + ".tooltip"));
        list.add("");
        String rate = "\u00A7c*ERROR*"; // should never happen, but this way we know.
        switch(Variant.values()[stack.getMetadata()]){
            case ENERGY: rate = "\u00A7e320 kRF/t"; break;
            case FLUID: rate = "\u00A7e32 B/t"; break;
            case ITEM: rate = "\u00A7e16 IS/t"; break;
        }
        String translated = I18n.format("tooltip.throughput1", rate);
        list.add(translated);
    }
}
