package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.tropicalfish;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;

public interface TropicalFishPattern extends MappedEntity {
   TropicalFishPattern.Base getBase();

   public static enum Base {
      SMALL,
      LARGE;

      // $FF: synthetic method
      private static TropicalFishPattern.Base[] $values() {
         return new TropicalFishPattern.Base[]{SMALL, LARGE};
      }
   }
}
