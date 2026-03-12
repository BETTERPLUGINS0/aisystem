package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum WeatheringCopperState {
   UNAFFECTED,
   EXPOSED,
   WEATHERED,
   OXIDIZED;

   private static final WeatheringCopperState[] STATES = values();

   public static WeatheringCopperState read(PacketWrapper<?> wrapper) {
      return (WeatheringCopperState)wrapper.readEnum((Enum[])STATES);
   }

   public static void write(PacketWrapper<?> wrapper, WeatheringCopperState state) {
      wrapper.writeEnum(state);
   }

   // $FF: synthetic method
   private static WeatheringCopperState[] $values() {
      return new WeatheringCopperState[]{UNAFFECTED, EXPOSED, WEATHERED, OXIDIZED};
   }
}
