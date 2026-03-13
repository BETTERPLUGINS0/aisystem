package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import org.bukkit.World;

@Desc("Represents a weather type")
public enum IrisWeather {
   @Desc("Represents when weather is not causing downfall")
   NONE,
   @Desc("Represents rain or snow")
   DOWNFALL,
   @Desc("Represents rain or snow with thunder")
   DOWNFALL_WITH_THUNDER,
   @Desc("Any weather")
   ANY;

   public boolean is(World world) {
      boolean var10000;
      switch(this.ordinal()) {
      case 0:
         var10000 = var1.isClearWeather();
         break;
      case 1:
         var10000 = var1.hasStorm();
         break;
      case 2:
         var10000 = var1.hasStorm() && var1.isThundering();
         break;
      case 3:
         var10000 = true;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   // $FF: synthetic method
   private static IrisWeather[] $values() {
      return new IrisWeather[]{NONE, DOWNFALL, DOWNFALL_WITH_THUNDER, ANY};
   }
}
