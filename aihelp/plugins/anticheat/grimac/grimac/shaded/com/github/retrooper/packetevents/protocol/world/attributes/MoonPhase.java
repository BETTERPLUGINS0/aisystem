package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.CodecNameable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum MoonPhase implements CodecNameable {
   FULL_MOON("full_moon"),
   WANING_GIBBOUS("waning_gibbous"),
   THIRD_QUARTER("third_quarter"),
   WANING_CRESCENT("waning_crescent"),
   NEW_MOON("new_moon"),
   WAXING_CRESCENT("waxing_crescent"),
   FIRST_QUARTER("first_quarter"),
   WAXING_GIBBOUS("waxing_gibbous");

   public static final NbtCodec<MoonPhase> CODEC = NbtCodecs.forEnum(values());
   private final String name;

   private MoonPhase(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public String getCodecName() {
      return this.name;
   }

   // $FF: synthetic method
   private static MoonPhase[] $values() {
      return new MoonPhase[]{FULL_MOON, WANING_GIBBOUS, THIRD_QUARTER, WANING_CRESCENT, NEW_MOON, WAXING_CRESCENT, FIRST_QUARTER, WAXING_GIBBOUS};
   }
}
