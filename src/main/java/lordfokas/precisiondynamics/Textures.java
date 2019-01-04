package lordfokas.precisiondynamics;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class Textures {
    @InJSON public static final ResourceLocation FACE_COUNTER        = rl("face_counter");
    @InJSON public static final ResourceLocation FACE_COUNTER_ENERGY = rl("face_counter_energy");
    @InJSON public static final ResourceLocation FACE_COUNTER_FLUID  = rl("face_counter_fluid");
    @InJSON public static final ResourceLocation FACE_COUNTER_ITEM   = rl("face_counter_item");

    @InJSON public static final ResourceLocation DEVICE_SIDE   = rl("device_side");
    @InJSON public static final ResourceLocation DEVICE_TOP    = rl("device_top");
    @InJSON public static final ResourceLocation DEVICE_BOTTOM = rl("device_bottom");

            public static final ResourceLocation IO_BLUE   = rl("io_blue");
            public static final ResourceLocation IO_GREEN  = rl("io_green");
            public static final ResourceLocation IO_PURPLE = rl("io_purple");
            public static final ResourceLocation IO_ORANGE = rl("io_orange");
            public static final ResourceLocation IO_RED    = rl("io_red");
            public static final ResourceLocation IO_YELLOW = rl("io_yellow");

    @InJSON public static final ResourceLocation NONE = rl("transparent");

    // *** Here be H4X0RZ (because I'm lazy as all fuck!) *** *** *** ***
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface InJSON {} // If a texture is already loaded by JSON models there's no point in re-loading it.

    public static void register(TextureMap map){
        try{
            for (Field f : Textures.class.getDeclaredFields())
                if(f.getAnnotation(InJSON.class) == null)
                    map.registerSprite((ResourceLocation) f.get(null));
        }catch(Exception e){ throw new RuntimeException(e); }
    }

    private static ResourceLocation rl(String sprite){ return new ResourceLocation(PrecisionDynamics.MODID, sprite); }
}
