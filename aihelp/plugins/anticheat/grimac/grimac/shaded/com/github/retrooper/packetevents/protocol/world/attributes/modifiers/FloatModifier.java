package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.AlphaFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface FloatModifier<A> extends AttributeModifier<Float, A> {
   FloatModifier<AlphaFloat> ALPHA_BLEND = new FloatModifier<AlphaFloat>() {
      public Float apply(Float value, AlphaFloat arg) {
         return MathUtil.lerp(arg.getAlpha(), value, arg.getValue());
      }

      public NbtCodec<AlphaFloat> argumentCodec(EnvironmentAttribute<Float> attribute) {
         return AlphaFloat.CODEC;
      }
   };
   FloatModifier<Float> ADD = Float::sum;
   FloatModifier<Float> SUBTRACT = (a, b) -> {
      return a - b;
   };
   FloatModifier<Float> MULTIPLY = (a, b) -> {
      return a * b;
   };
   FloatModifier<Float> MINIMUM = Math::min;
   FloatModifier<Float> MAXIMUM = Math::max;

   @FunctionalInterface
   public interface Simple extends FloatModifier<Float> {
      default NbtCodec<Float> argumentCodec(EnvironmentAttribute<Float> attribute) {
         return NbtCodecs.FLOAT;
      }
   }
}
