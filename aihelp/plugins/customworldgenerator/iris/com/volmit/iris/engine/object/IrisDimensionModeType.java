package com.volmit.iris.engine.object;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineMode;
import com.volmit.iris.engine.mode.ModeEnclosure;
import com.volmit.iris.engine.mode.ModeIslands;
import com.volmit.iris.engine.mode.ModeOverworld;
import com.volmit.iris.engine.mode.ModeSuperFlat;
import com.volmit.iris.engine.object.annotations.Desc;
import java.util.function.Function;

@Desc("The type of dimension this is")
public enum IrisDimensionModeType {
   @Desc("Typical dimensions. Has a fluid height, and all features of a biome based world")
   OVERWORLD(ModeOverworld::new),
   @Desc("Ultra fast, but very limited in features. Only supports terrain & biomes. No decorations, mobs, objects, or anything of the sort!")
   SUPERFLAT(ModeSuperFlat::new),
   @Desc("Like the nether, a ceiling & floor carved out")
   ENCLOSURE(ModeEnclosure::new),
   @Desc("Floating islands of terrain")
   ISLANDS(ModeIslands::new);

   private final Function<Engine, EngineMode> factory;

   private IrisDimensionModeType(Function<Engine, EngineMode> factory) {
      this.factory = var3;
   }

   public EngineMode create(Engine e) {
      return (EngineMode)this.factory.apply(var1);
   }

   // $FF: synthetic method
   private static IrisDimensionModeType[] $values() {
      return new IrisDimensionModeType[]{OVERWORLD, SUPERFLAT, ENCLOSURE, ISLANDS};
   }
}
