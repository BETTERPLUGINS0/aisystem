package github.nighter.smartspawner.updates;

import org.jetbrains.annotations.NotNull;

public class Version implements Comparable<Version> {
   private final int[] parts;
   private static final int MAX_PARTS = 4;

   public Version(String version) {
      version = version.replaceAll("[^0-9.].*$", "").replaceAll("^[^0-9]*", "");
      String[] split = version.split("\\.");
      this.parts = new int[4];

      for(int i = 0; i < 4; ++i) {
         if (i < split.length) {
            try {
               this.parts[i] = Integer.parseInt(split[i]);
            } catch (NumberFormatException var5) {
               this.parts[i] = 0;
            }
         } else {
            this.parts[i] = 0;
         }
      }

   }

   public int compareTo(@NotNull Version other) {
      for(int i = 0; i < 4; ++i) {
         if (this.parts[i] != other.parts[i]) {
            return this.parts[i] - other.parts[i];
         }
      }

      return 0;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < 3; ++i) {
         if (i > 0) {
            sb.append('.');
         }

         sb.append(this.parts[i]);
      }

      if (this.parts[3] > 0) {
         sb.append('.').append(this.parts[3]);
      }

      return sb.toString();
   }
}
