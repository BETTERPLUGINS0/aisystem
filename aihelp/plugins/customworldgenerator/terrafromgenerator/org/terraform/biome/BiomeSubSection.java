package org.terraform.biome;

public enum BiomeSubSection {
   POSITIVE_X(1, 0),
   NEGATIVE_X(-1, 0),
   POSITIVE_Z(0, 1),
   NEGATIVE_Z(0, -1),
   NONE(0, 0);

   public final int relX;
   public final int relZ;

   private BiomeSubSection(int param3, int param4) {
      this.relX = relX;
      this.relZ = relZ;
   }

   // $FF: synthetic method
   private static BiomeSubSection[] $values() {
      return new BiomeSubSection[]{POSITIVE_X, NEGATIVE_X, POSITIVE_Z, NEGATIVE_Z, NONE};
   }
}
