package lordfokas.precisiondynamics.devices;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCrossover extends BlockBase {
    public BlockCrossover() {
        super("crossover");
    }

    @Nullable
    @Override
    public TileEntityCrossover createTileEntity(World world, IBlockState state) {
        return null;
    }
}
