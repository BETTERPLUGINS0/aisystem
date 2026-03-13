package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("Terrain modes are used to decide the generator type currently used")
public enum IrisSpawnGroup {
   @Desc("Spawns on the terrain surface")
   NORMAL,
   @Desc("Spawns in cave-air and low light level areas")
   CAVE,
   @Desc("Spawns underwater")
   UNDERWATER,
   @Desc("Spawns in beaches")
   BEACH;

   // $FF: synthetic method
   private static IrisSpawnGroup[] $values() {
      return new IrisSpawnGroup[]{NORMAL, CAVE, UNDERWATER, BEACH};
   }
}
