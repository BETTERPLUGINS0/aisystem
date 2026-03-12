package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.easing;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.FloatUnaryOperator;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.easing.EasingFunctions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EasingTypes {
   private static final VersionedRegistry<EasingType> REGISTRY = new VersionedRegistry("easing_type");
   public static final EasingType CONSTANT = define("constant", EasingFunctions::constant);
   public static final EasingType LINEAR = define("linear", EasingFunctions::linear);
   public static final EasingType IN_BACK = define("in_back", EasingFunctions::inBack);
   public static final EasingType IN_BOUNCE = define("in_bounce", EasingFunctions::inBounce);
   public static final EasingType IN_CIRC = define("in_circ", EasingFunctions::inCirc);
   public static final EasingType IN_CUBIC = define("in_cubic", EasingFunctions::inCubic);
   public static final EasingType IN_ELASTIC = define("in_elastic", EasingFunctions::inElastic);
   public static final EasingType IN_EXPO = define("in_expo", EasingFunctions::inExpo);
   public static final EasingType IN_QUAD = define("in_quad", EasingFunctions::inQuad);
   public static final EasingType IN_QUART = define("in_quart", EasingFunctions::inQuart);
   public static final EasingType IN_QUINT = define("in_quint", EasingFunctions::inQuint);
   public static final EasingType IN_SINE = define("in_sine", EasingFunctions::inSine);
   public static final EasingType IN_OUT_BACK = define("in_out_back", EasingFunctions::inOutBack);
   public static final EasingType IN_OUT_BOUNCE = define("in_out_bounce", EasingFunctions::inOutBounce);
   public static final EasingType IN_OUT_CIRC = define("in_out_circ", EasingFunctions::inOutCirc);
   public static final EasingType IN_OUT_CUBIC = define("in_out_cubic", EasingFunctions::inOutCubic);
   public static final EasingType IN_OUT_ELASTIC = define("in_out_elastic", EasingFunctions::inOutElastic);
   public static final EasingType IN_OUT_EXPO = define("in_out_expo", EasingFunctions::inOutExpo);
   public static final EasingType IN_OUT_QUAD = define("in_out_quad", EasingFunctions::inOutQuad);
   public static final EasingType IN_OUT_QUART = define("in_out_quart", EasingFunctions::inOutQuart);
   public static final EasingType IN_OUT_QUINT = define("in_out_quint", EasingFunctions::inOutQuint);
   public static final EasingType IN_OUT_SINE = define("in_out_sine", EasingFunctions::inOutSine);
   public static final EasingType OUT_BACK = define("out_back", EasingFunctions::outBack);
   public static final EasingType OUT_BOUNCE = define("out_bounce", EasingFunctions::outBounce);
   public static final EasingType OUT_CIRC = define("out_circ", EasingFunctions::outCirc);
   public static final EasingType OUT_CUBIC = define("out_cubic", EasingFunctions::outCubic);
   public static final EasingType OUT_ELASTIC = define("out_elastic", EasingFunctions::outElastic);
   public static final EasingType OUT_EXPO = define("out_expo", EasingFunctions::outExpo);
   public static final EasingType OUT_QUAD = define("out_quad", EasingFunctions::outQuad);
   public static final EasingType OUT_QUART = define("out_quart", EasingFunctions::outQuart);
   public static final EasingType OUT_QUINT = define("out_quint", EasingFunctions::outQuint);
   public static final EasingType OUT_SINE = define("out_sine", EasingFunctions::outSine);

   private EasingTypes() {
   }

   @ApiStatus.Internal
   public static EasingType define(String name, FloatUnaryOperator operator) {
      return (EasingType)REGISTRY.define(name, (data) -> {
         return new StaticEasingType(data, operator);
      });
   }

   public static VersionedRegistry<EasingType> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
