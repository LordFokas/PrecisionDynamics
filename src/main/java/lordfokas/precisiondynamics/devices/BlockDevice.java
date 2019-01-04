package lordfokas.precisiondynamics.devices;

import lordfokas.precisiondynamics.PrecisionDynamics;
import lordfokas.precisiondynamics.devices.base.BlockBase;
import lordfokas.precisiondynamics.devices.base.TileEntityBase;
import lordfokas.precisiondynamics.devices.base.TileEntityDevice;
import lordfokas.precisiondynamics.devices.base.configuration.Direction;
import lordfokas.precisiondynamics.devices.base.configuration.Face;
import lordfokas.precisiondynamics.devices.base.resources.Variant;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties.PropertyAdapter;

import javax.annotation.Nullable;

public class BlockDevice extends BlockBase {
    public static final IProperty<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class,
            EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.EAST);
    public static final IProperty<Variant> VARIANT = Variant.PROPERTY;

    public static final IUnlistedProperty<Face> FACE_TOP    = io("io_top");
    public static final IUnlistedProperty<Face> FACE_BOTTOM = io("io_bottom");
    public static final IUnlistedProperty<Face> FACE_BACK   = io("io_back");
    public static final IUnlistedProperty<Face> FACE_LEFT   = io("io_left");
    public static final IUnlistedProperty<Face> FACE_RIGHT  = io("io_right");

    private static IUnlistedProperty<Face> io(String name){
        return new PropertyAdapter<>(PropertyEnum.create(name, Face.class));
    }

    public final DeviceType device;

    public BlockDevice(DeviceType device) {
        super(device.name);
        this.device = device;
        setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public Item createItemBlock() { return new ItemBlockDevice(this); }

    @Nullable
    @Override
    public TileEntityDevice createTileEntity(World world, IBlockState state) {
        Variant variant = state.getValue(VARIANT);
        switch(device){
            case BALANCER: return null;
            case COUNTER: return new TileEntityDeviceCounter(variant);
            case PRIORITIZER: return null;
            default: return null;
        }

    }

    // ***** // BLOCK STATE STUFF // ***** ***** ***** //
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
                .add(FACING, VARIANT) // Listed Properties
                .add(FACE_TOP, FACE_BOTTOM, FACE_BACK, FACE_LEFT, FACE_RIGHT) // Unlisted Properties
                .build();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(VARIANT).ordinal() << 2) | (state.getValue(FACING).ordinal() - 2);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState()
                .withProperty(VARIANT, Variant.values()[meta>>2])
                .withProperty(FACING, EnumFacing.values()[(meta&3)+2]);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos){
        TileEntityBase device = (TileEntityBase) world.getTileEntity(pos);
        return ((IExtendedBlockState)state)
                .withProperty(FACE_TOP,    device.getColorInterface(Direction.TOP))
                .withProperty(FACE_BOTTOM, device.getColorInterface(Direction.BOTTOM))
                .withProperty(FACE_BACK,   device.getColorInterface(Direction.BACK))
                .withProperty(FACE_LEFT,   device.getColorInterface(Direction.LEFT))
                .withProperty(FACE_RIGHT,  device.getColorInterface(Direction.RIGHT));
    }

    protected IBlockState getBaseStateForPlacement(ItemStack stack) {
        return getDefaultState().withProperty(VARIANT, Variant.values()[stack.getMetadata()]);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float h, float i, float j, int x, EntityLivingBase player, EnumHand hand) {
        return getBaseStateForPlacement(player.getHeldItem(hand))
                .withProperty(FACING, player.getHorizontalFacing().getOpposite());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    public EnumFacing getFacing(IBlockState state){ return state.getValue(FACING); }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float x, float y, float z) {
        if(world.isRemote) return true;
        if(player.isSneaking()) return false;
        player.openGui(PrecisionDynamics.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
