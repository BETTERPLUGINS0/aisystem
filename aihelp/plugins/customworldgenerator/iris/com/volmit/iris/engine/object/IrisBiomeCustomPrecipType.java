package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("Snow, rain, or nothing")
public enum IrisBiomeCustomPrecipType {
   @Desc("No downfall")
   none,
   @Desc("Rain downfall")
   rain,
   @Desc("Snow downfall")
   snow;

   // $FF: synthetic method
   private static IrisBiomeCustomPrecipType[] $values() {
      return new IrisBiomeCustomPrecipType[]{none, rain, snow};
   }
}
