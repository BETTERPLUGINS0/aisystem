package com.volmit.iris.engine.object;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.function.Function;

@Desc("Represents a stream from the engine")
public enum IrisEngineStreamType {
   @Desc("Represents the given slope at the x, z coordinates")
   SLOPE((var0) -> {
      return var0.getComplex().getSlopeStream();
   }),
   @Desc("Represents the base generator height at the given position. This includes only the biome generators / interpolation and noise features but does not include carving, caves.")
   HEIGHT((var0) -> {
      return var0.getComplex().getHeightStream();
   }),
   @Desc("Represents the base generator height at the given position. This includes only the biome generators / interpolation and noise features but does not include carving, caves. with Max(height, fluidHeight).")
   HEIGHT_OR_FLUID((var0) -> {
      return var0.getComplex().getHeightFluidStream();
   }),
   @Desc("Represents the overlay noise generators summed (dimension setting)")
   OVERLAY_NOISE((var0) -> {
      return var0.getComplex().getOverlayStream();
   }),
   @Desc("Represents the noise style of regions")
   REGION_STYLE((var0) -> {
      return var0.getComplex().getRegionStyleStream();
   }),
   @Desc("Represents the identity of regions. Each region has a unique number (very large numbers)")
   REGION_IDENTITY((var0) -> {
      return var0.getComplex().getRegionIdentityStream();
   });

   private final Function<Engine, ProceduralStream<Double>> getter;

   private IrisEngineStreamType(Function<Engine, ProceduralStream<Double>> getter) {
      this.getter = var3;
   }

   public ProceduralStream<Double> get(Engine engine) {
      return (ProceduralStream)this.getter.apply(var1);
   }

   // $FF: synthetic method
   private static IrisEngineStreamType[] $values() {
      return new IrisEngineStreamType[]{SLOPE, HEIGHT, HEIGHT_OR_FLUID, OVERLAY_NOISE, REGION_STYLE, REGION_IDENTITY};
   }
}
