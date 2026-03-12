package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class InputControlTypes {
   private static final VersionedRegistry<InputControlType<?>> REGISTRY = new VersionedRegistry("input_control_type");
   public static final InputControlType<BooleanInputControl> BOOLEAN = define("boolean", BooleanInputControl::decode, BooleanInputControl::encode);
   public static final InputControlType<NumberRangeInputControl> NUMBER_RANGE = define("number_range", NumberRangeInputControl::decode, NumberRangeInputControl::encode);
   public static final InputControlType<SingleOptionInputControl> SINGLE_OPTION = define("single_option", SingleOptionInputControl::decode, SingleOptionInputControl::encode);
   public static final InputControlType<TextInputControl> TEXT = define("text", TextInputControl::decode, TextInputControl::encode);

   private InputControlTypes() {
   }

   public static VersionedRegistry<InputControlType<?>> getRegistry() {
      return REGISTRY;
   }

   @ApiStatus.Internal
   public static <T extends InputControl> InputControlType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
      return (InputControlType)REGISTRY.define(name, (data) -> {
         return new StaticInputControlType(data, decoder, encoder);
      });
   }

   static {
      REGISTRY.unloadMappings();
   }
}
