package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("The mob spawn group")
public enum IrisBiomeCustomSpawnType {
   @Desc("Typical monsters that spawn at night, like zombies and skeletons")
   MONSTER,
   @Desc("Typical creatures like sheep, pigs, cows")
   CREATURE,
   @Desc("Eg bats")
   AMBIENT,
   @Desc("Odd spawn group but ok")
   UNDERGROUND_WATER_CREATURE,
   @Desc("Water mobs like squid, dolphins")
   WATER_CREATURE,
   @Desc("Fish")
   WATER_AMBIENT,
   @Desc("Unknown")
   MISC;

   // $FF: synthetic method
   private static IrisBiomeCustomSpawnType[] $values() {
      return new IrisBiomeCustomSpawnType[]{MONSTER, CREATURE, AMBIENT, UNDERGROUND_WATER_CREATURE, WATER_CREATURE, WATER_AMBIENT, MISC};
   }
}
