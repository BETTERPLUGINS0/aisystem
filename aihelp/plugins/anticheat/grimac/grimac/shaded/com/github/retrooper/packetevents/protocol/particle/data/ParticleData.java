package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticleData {
   static ParticleData EMPTY = new ParticleData();

   public static <T extends ParticleData> T emptyData() {
      return EMPTY;
   }

   public boolean isEmpty() {
      return true;
   }
}
