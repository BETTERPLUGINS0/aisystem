package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.AlphaColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.CodecNameable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MapUtil;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AttributeModifier<T, A> {
   Map<AttributeModifier.Operation, AttributeModifier<Boolean, ?>> BOOLEAN_LIBRARY = MapUtil.createMap(new SimpleEntry(AttributeModifier.Operation.AND, BooleanModifier.AND), new SimpleEntry(AttributeModifier.Operation.NAND, BooleanModifier.NAND), new SimpleEntry(AttributeModifier.Operation.OR, BooleanModifier.OR), new SimpleEntry(AttributeModifier.Operation.NOR, BooleanModifier.NOR), new SimpleEntry(AttributeModifier.Operation.XOR, BooleanModifier.XOR), new SimpleEntry(AttributeModifier.Operation.XNOR, BooleanModifier.XNOR));
   Map<AttributeModifier.Operation, AttributeModifier<Float, ?>> FLOAT_LIBRARY = MapUtil.createMap(new SimpleEntry(AttributeModifier.Operation.ALPHA_BLEND, FloatModifier.ALPHA_BLEND), new SimpleEntry(AttributeModifier.Operation.ADD, FloatModifier.ADD), new SimpleEntry(AttributeModifier.Operation.SUBTRACT, FloatModifier.SUBTRACT), new SimpleEntry(AttributeModifier.Operation.MULTIPLY, FloatModifier.MULTIPLY), new SimpleEntry(AttributeModifier.Operation.MINIMUM, FloatModifier.MINIMUM), new SimpleEntry(AttributeModifier.Operation.MAXIMUM, FloatModifier.MAXIMUM));
   Map<AttributeModifier.Operation, AttributeModifier<Color, ?>> RGB_COLOR_LIBRARY = MapUtil.createMap(new SimpleEntry(AttributeModifier.Operation.ALPHA_BLEND, ColorModifier.ALPHA_BLEND), new SimpleEntry(AttributeModifier.Operation.ADD, ColorModifier.ADD), new SimpleEntry(AttributeModifier.Operation.SUBTRACT, ColorModifier.SUBTRACT), new SimpleEntry(AttributeModifier.Operation.MULTIPLY, ColorModifier.MULTIPLY), new SimpleEntry(AttributeModifier.Operation.BLEND_TO_GRAY, ColorModifier.BLEND_TO_GRAY));
   Map<AttributeModifier.Operation, AttributeModifier<AlphaColor, ?>> ARGB_COLOR_LIBRARY = MapUtil.createMap(new SimpleEntry(AttributeModifier.Operation.ALPHA_BLEND, AlphaColorModifier.ALPHA_BLEND), new SimpleEntry(AttributeModifier.Operation.ADD, AlphaColorModifier.ADD), new SimpleEntry(AttributeModifier.Operation.SUBTRACT, AlphaColorModifier.SUBTRACT), new SimpleEntry(AttributeModifier.Operation.MULTIPLY, AlphaColorModifier.MULTIPLY), new SimpleEntry(AttributeModifier.Operation.BLEND_TO_GRAY, AlphaColorModifier.BLEND_TO_GRAY));

   static <T> AttributeModifier<T, T> override() {
      return AttributeModifier.OverrideModifier.INSTANCE;
   }

   T apply(T value, A arg);

   NbtCodec<A> argumentCodec(EnvironmentAttribute<T> attribute);

   public static final class OverrideModifier<T> implements AttributeModifier<T, T> {
      private static final AttributeModifier.OverrideModifier<?> INSTANCE = new AttributeModifier.OverrideModifier();

      public T apply(T value, T arg) {
         return arg;
      }

      public NbtCodec<T> argumentCodec(EnvironmentAttribute<T> attribute) {
         return attribute.getType().getValueCodec();
      }
   }

   public static enum Operation implements CodecNameable {
      OVERRIDE("override"),
      ALPHA_BLEND("alpha_blend"),
      ADD("add"),
      SUBTRACT("subtract"),
      MULTIPLY("multiply"),
      BLEND_TO_GRAY("blend_to_gray"),
      MINIMUM("minimum"),
      MAXIMUM("maximum"),
      AND("and"),
      NAND("nand"),
      OR("or"),
      NOR("nor"),
      XOR("xor"),
      XNOR("xnor");

      public static final NbtCodec<AttributeModifier.Operation> CODEC = NbtCodecs.forEnum(values());
      private final String name;

      private Operation(String name) {
         this.name = name;
      }

      public String getCodecName() {
         return this.name;
      }

      // $FF: synthetic method
      private static AttributeModifier.Operation[] $values() {
         return new AttributeModifier.Operation[]{OVERRIDE, ALPHA_BLEND, ADD, SUBTRACT, MULTIPLY, BLEND_TO_GRAY, MINIMUM, MAXIMUM, AND, NAND, OR, NOR, XOR, XNOR};
      }
   }
}
