package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.AlphaColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AlphaColorModifier<A> extends AttributeModifier<AlphaColor, A> {
   AlphaColorModifier<AlphaColor> ALPHA_BLEND = Color::blendWith;
   AlphaColorModifier<Color> ADD = AlphaColor::plus;
   AlphaColorModifier<Color> SUBTRACT = AlphaColor::minus;
   AlphaColorModifier<AlphaColor> MULTIPLY = AlphaColor::times;
   AlphaColorModifier<BlendToGrayArgument> BLEND_TO_GRAY = new AlphaColorModifier<BlendToGrayArgument>() {
      public AlphaColor apply(AlphaColor value, BlendToGrayArgument arg) {
         return arg.blend(value);
      }

      public NbtCodec<BlendToGrayArgument> argumentCodec(EnvironmentAttribute<AlphaColor> attribute) {
         return BlendToGrayArgument.CODEC;
      }
   };

   @FunctionalInterface
   public interface ArgbModifier extends AlphaColorModifier<AlphaColor> {
      default NbtCodec<AlphaColor> argumentCodec(EnvironmentAttribute<AlphaColor> attribute) {
         return NbtCodecs.ARGB_COLOR;
      }
   }

   @FunctionalInterface
   public interface RgbModifier extends AlphaColorModifier<Color> {
      default NbtCodec<Color> argumentCodec(EnvironmentAttribute<AlphaColor> attribute) {
         return NbtCodecs.RGB_COLOR;
      }
   }
}
