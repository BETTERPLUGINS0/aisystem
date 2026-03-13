package org.terraform.biome;

public enum BiomeType {
   OCEANIC(false),
   FLAT(true),
   MOUNTAINOUS(true),
   HIGH_MOUNTAINOUS(true),
   BEACH(true),
   DEEP_OCEANIC(false),
   RIVER(false);

   private final boolean isDry;

   private BiomeType(boolean param3) {
      this.isDry = isDry;
   }

   public boolean isDry() {
      return this.isDry;
   }

   // $FF: synthetic method
   private static BiomeType[] $values() {
      return new BiomeType[]{OCEANIC, FLAT, MOUNTAINOUS, HIGH_MOUNTAINOUS, BEACH, DEEP_OCEANIC, RIVER};
   }
}
