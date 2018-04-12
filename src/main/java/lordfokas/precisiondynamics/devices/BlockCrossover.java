package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.devices.base.BlockBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCrossover extends BlockBase {
    public BlockCrossover() {
        super("crossover");
    }

    @Nullable
    @Override
    public TileEntityCrossover createTileEntity(World world, IBlockState state) {
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        list.add(I18n.format("tile.crossover.tooltip"));
        list.add("");
        list.add("\u00A7n"+I18n.format("tooltip.throughput3"));
        list.add(I18n.format("tooltip.throughput.energy", "\u00A7e320 kRF/t"));
        list.add(I18n.format("tooltip.throughput.fluid", "\u00A7e32 B/t"));
        list.add(I18n.format("tooltip.throughput.item", "\u00A7e16 IS/t"));
    }
}
