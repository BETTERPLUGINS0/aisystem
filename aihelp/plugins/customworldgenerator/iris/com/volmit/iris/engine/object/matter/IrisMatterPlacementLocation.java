package com.volmit.iris.engine.object.matter;

import com.volmit.iris.engine.IrisEngine;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.util.function.Function3;

@Desc("WHERE THINGS PLACE")
public enum IrisMatterPlacementLocation {
   SURFACE((var0, var1, var2) -> {
      return var0.getHeight(var1, var2, true);
   }),
   SURFACE_ON_FLUID((var0, var1, var2) -> {
      return var0.getHeight(var1, var2, false);
   }),
   BEDROCK((var0, var1, var2) -> {
      return 0;
   }),
   SKY((var0, var1, var2) -> {
      return var0.getHeight();
   });

   private final Function3<IrisEngine, Integer, Integer, Integer> computer;

   private IrisMatterPlacementLocation(Function3<IrisEngine, Integer, Integer, Integer> computer) {
      this.computer = var3;
   }

   public int at(IrisEngine engine, int x, int z) {
      return (Integer)this.computer.apply(var1, var2, var3);
   }

   // $FF: synthetic method
   private static IrisMatterPlacementLocation[] $values() {
      return new IrisMatterPlacementLocation[]{SURFACE, SURFACE_ON_FLUID, BEDROCK, SKY};
   }
}
