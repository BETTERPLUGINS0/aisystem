package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import java.util.UUID;

public final class UniqueIdUtil {
   private UniqueIdUtil() {
   }

   public static UUID fromIntArray(int[] array) {
      if (array.length != 4) {
         throw new IllegalStateException("Invalid encoded uuid length: " + array.length + " != 4");
      } else {
         return new UUID((long)array[0] << 32 | (long)array[1] & 4294967295L, (long)array[2] << 32 | (long)array[3] & 4294967295L);
      }
   }

   public static int[] toIntArray(UUID uniqueId) {
      return new int[]{(int)(uniqueId.getMostSignificantBits() >> 32), (int)uniqueId.getMostSignificantBits(), (int)(uniqueId.getLeastSignificantBits() >> 32), (int)uniqueId.getLeastSignificantBits()};
   }
}
