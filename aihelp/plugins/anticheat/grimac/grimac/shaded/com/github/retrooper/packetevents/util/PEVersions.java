package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import java.time.Instant;

public final class PEVersions {
   public static final String RAW = "2.11.2+675bc5c-SNAPSHOT";
   public static final Instant BUILD_TIMESTAMP = Instant.ofEpochMilli(1769231749098L);
   public static final PEVersion CURRENT = new PEVersion(2, 11, 2, "675bc5c");
   public static final PEVersion UNKNOWN = new PEVersion(0, 0, 0);

   private PEVersions() {
      throw new IllegalStateException();
   }
}
