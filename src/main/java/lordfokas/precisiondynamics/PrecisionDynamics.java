package lordfokas.precisiondynamics;

import lordfokas.precisiondynamics.devices.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = PrecisionDynamics.MODID, name = PrecisionDynamics.NAME, version = PrecisionDynamics.VERSION,
        dependencies = "required-after:thermalexpansion")
public class PrecisionDynamics{
    public static final String MODID = "precisiondynamics";
    public static final String NAME = "Precision Dynamics";
    public static final String VERSION = "0.0.1-dev";
    public static final Logger logger = LogManager.getLogger(MODID);;

    @Instance(MODID)
    public static PrecisionDynamics instance;

    @SidedProxy(clientSide = "lordfokas.precisiondynamics.ProxyClient",
                serverSide = "lordfokas.precisiondynamics.ProxyDedicatedServer")
    private static IProxy proxy;

    public final CreativeTabs tab = new TabPrecisionDynamics();
    public BlockDevice balancer, counter, prioritizer;
    public BlockCrossover crossover;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt){
        balancer = new BlockDevice(EnumDevice.BALANCER);
        counter = new BlockDevice(EnumDevice.COUNTER);
        prioritizer = new BlockDevice(EnumDevice.PRIORITIZER);
        crossover = new BlockCrossover();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt){
        Object servo = GameRegistry.makeItemStack("thermalfoundation:material", 512, 1, null);
        Object frame = GameRegistry.makeItemStack("thermalexpansion:frame", 64, 1, null);
        for(EnumDevice device : EnumDevice.values())
        for(EnumVariant variant : EnumVariant.values()){
            GameRegistry.addShapedRecipe(new ResourceLocation(MODID, device.name+"_"+variant.suffix),
                    null, device.getStack(variant),
                    "nCn", "BFB", "GSG",
                    'n', "nugget" + device.metal,
                    'C', device.getTop(variant),
                    'B', device.getSides(variant),
                    'F', frame,
                    'G', "gear" + device.metal,
                    'S', servo
            );
        }

        int[][] combinations = new int[][]{
                new int[]{0, 1, 2}, new int[]{1, 2, 0}, new int[]{2, 0, 1},
                new int[]{2, 1, 0}, new int[]{1, 0, 2}, new int[]{0, 2, 1},
        };
        for(int i = 0; i < combinations.length; i++){
            int[] order = combinations[i];
            GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "crossover_"+i),
                    null, new ItemStack(crossover),
                    "n0n", "1P2", "nSn",
                    'n', "nuggetConstantan",
                    '0', getVariantItem(order[0]),
                    '1', getVariantItem(order[1]),
                    '2', getVariantItem(order[2]),
                    'P', "plateConstantan",
                    'S', servo
            );
        }
    }

    // TODO: should not be here, move to EnumVariant?
    private Object getVariantItem(int index){
        return EnumVariant.values()[index].item;
    }

    @EventBusSubscriber
    public static class RegistrationHandler{
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> evt){
            evt.getRegistry().registerAll(
                    instance.balancer,
                    instance.counter,
                    instance.prioritizer,
                    instance.crossover
            );

            GameRegistry.registerTileEntity(TileEntityDeviceBalancer.class, "te_balancer");
            GameRegistry.registerTileEntity(TileEntityDeviceCounter.class, "te_counter");
            GameRegistry.registerTileEntity(TileEntityDevicePrioritizer.class, "te_prioritizer");
            GameRegistry.registerTileEntity(TileEntityCrossover.class, "te_crossover");
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> evt){
            evt.getRegistry().registerAll(
                    instance.balancer.createItemBlock(),
                    instance.counter.createItemBlock(),
                    instance.prioritizer.createItemBlock(),
                    instance.crossover.createItemBlock()
            );
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent evt){
            for(EnumVariant variant : EnumVariant.values()){
                proxy.registerItemRenderer(Item.getItemFromBlock(instance.balancer), variant.ordinal(),
                        instance.balancer.device.name, "facing=player,variant="+variant.getName());
                proxy.registerItemRenderer(Item.getItemFromBlock(instance.counter), variant.ordinal(),
                        instance.counter.device.name, "facing=player,variant="+variant.getName());
                proxy.registerItemRenderer(Item.getItemFromBlock(instance.prioritizer), variant.ordinal(),
                        instance.prioritizer.device.name, "facing=player,variant="+variant.getName());
            }
            proxy.registerItemRenderer(Item.getItemFromBlock(instance.crossover), 0,
                    "crossover", "inventory");
        }
    }
}
