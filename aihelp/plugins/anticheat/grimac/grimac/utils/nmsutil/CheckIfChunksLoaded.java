package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import lombok.Generated;

public final class CheckIfChunksLoaded {
   public static boolean areChunksUnloadedAt(GrimPlayer player, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      if (maxY >= player.compensatedWorld.getMinHeight() && minY < player.compensatedWorld.getMaxHeight()) {
         minX >>= 4;
         minZ >>= 4;
         maxX >>= 4;
         maxZ >>= 4;

         for(int i = minX; i <= maxX; ++i) {
            for(int j = minZ; j <= maxZ; ++j) {
               if (player.compensatedWorld.getChunk(i, j) == null) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   @Generated
   private CheckIfChunksLoaded() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
