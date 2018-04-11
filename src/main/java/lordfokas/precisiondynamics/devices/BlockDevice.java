package lordfokas.precisiondynamics.devices;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDevice extends BlockBase {
    public static final IProperty<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class,
            EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.EAST);
    public static final IProperty<EnumVariant> VARIANT = EnumVariant.PROPERTY;
    public final EnumDevice device;

    public BlockDevice(EnumDevice device) {
        super(device.name);
        this.device = device;
        setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public Item createItemBlock() { return new ItemBlockDevice(this); }

    @Nullable
    @Override
    public TileEntityDevice createTileEntity(World world, IBlockState state) {
        switch(device){
            case BALANCER: return null;
            case COUNTER: return new TileEntityDeviceCounter();
            case PRIORITIZER: return null;
            default: return null;
        }
    }

    // ***** // BLOCK STATE STUFF // ***** ***** ***** //
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, VARIANT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(VARIANT).ordinal() << 2) | (state.getValue(FACING).ordinal() - 2);
    }

    @Override // TODO: find out the replacement method
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState()
                .withProperty(VARIANT, EnumVariant.values()[meta>>2])
                .withProperty(FACING, EnumFacing.values()[(meta&3)+2]);
    }

    protected IBlockState getBaseStateForPlacement(ItemStack stack) {
        return getDefaultState().withProperty(VARIANT, EnumVariant.values()[stack.getMetadata()]);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float h, float i, float j,
                                            int x, EntityLivingBase player, EnumHand hand) {
        return getBaseStateForPlacement(player.getHeldItem(hand))
                .withProperty(FACING, player.getHorizontalFacing().getOpposite());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }
}
