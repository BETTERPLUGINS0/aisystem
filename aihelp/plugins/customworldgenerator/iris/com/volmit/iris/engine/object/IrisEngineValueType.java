package com.volmit.iris.engine.object;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.Desc;
import java.util.function.Function;

@Desc("Represents a value from the engine")
public enum IrisEngineValueType {
   @Desc("Represents actual height of the engine")
   ENGINE_HEIGHT((var0) -> {
      return (double)var0.getHeight();
   }),
   @Desc("Represents virtual bottom of the engine in the compound. If this engine is on top of another engine, it's min height would be at the maxHeight of the previous engine + 1")
   ENGINE_MIN_HEIGHT((var0) -> {
      return (double)var0.getMinHeight();
   }),
   @Desc("Represents virtual top of the engine in the compound. If this engine is below another engine, it's max height would be at the minHeight of the next engine - 1")
   ENGINE_MAX_HEIGHT((var0) -> {
      return (double)var0.getWorld().maxHeight();
   }),
   @Desc("The fluid height defined in the dimension file")
   FLUID_HEIGHT((var0) -> {
      return var0.getComplex().getFluidHeight();
   });

   private final Function<Engine, Double> getter;

   private IrisEngineValueType(Function<Engine, Double> getter) {
      this.getter = var3;
   }

   public Double get(Engine engine) {
      return (Double)this.getter.apply(var1);
   }

   // $FF: synthetic method
   private static IrisEngineValueType[] $values() {
      return new IrisEngineValueType[]{ENGINE_HEIGHT, ENGINE_MIN_HEIGHT, ENGINE_MAX_HEIGHT, FLUID_HEIGHT};
   }
}
