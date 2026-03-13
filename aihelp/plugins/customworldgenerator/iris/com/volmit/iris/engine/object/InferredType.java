package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("Represents a biome type")
public enum InferredType {
   @Desc("Represents any shore biome type")
   SHORE,
   @Desc("Represents any land biome type")
   LAND,
   @Desc("Represents any sea biome type")
   SEA,
   @Desc("Represents any cave biome type")
   CAVE;

   // $FF: synthetic method
   private static InferredType[] $values() {
      return new InferredType[]{SHORE, LAND, SEA, CAVE};
   }
}
