package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("The custom biome category. Vanilla asks for this, basically what represents your biome closest?")
public enum IrisBiomeCustomCategory {
   beach,
   desert,
   extreme_hills,
   forest,
   icy,
   jungle,
   mesa,
   mushroom,
   nether,
   none,
   ocean,
   plains,
   river,
   savanna,
   swamp,
   taiga,
   the_end;

   // $FF: synthetic method
   private static IrisBiomeCustomCategory[] $values() {
      return new IrisBiomeCustomCategory[]{beach, desert, extreme_hills, forest, icy, jungle, mesa, mushroom, nether, none, ocean, plains, river, savanna, swamp, taiga, the_end};
   }
}
