package lordfokas.precisiondynamics.devices.base;

import lordfokas.precisiondynamics.PrecisionDynamics;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block {
    public BlockBase(String name) {
        super(Material.IRON);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(PrecisionDynamics.instance.tab);
        setHardness(3f);
        setResistance(5f);
    }

    public Item createItemBlock(){ return new ItemBlock(this).setRegistryName(getRegistryName()); }
    @Override public boolean hasTileEntity(IBlockState state) {
        return true;
    }
}
