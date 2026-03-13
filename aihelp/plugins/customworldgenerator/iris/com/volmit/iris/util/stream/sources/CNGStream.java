package com.volmit.iris.util.stream.sources;

import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.stream.BasicLayer;
import com.volmit.iris.util.stream.ProceduralStream;

public class CNGStream extends BasicLayer implements ProceduralStream<Double> {
   private final CNG cng;

   public CNGStream(CNG cng) {
      this.cng = var1;
   }

   public CNGStream(CNG cng, double zoom, double offsetX, double offsetY, double offsetZ) {
      super(1337L, var2, var4, var6, var8);
      this.cng = var1;
   }

   public CNGStream(CNG cng, double zoom) {
      super(1337L, var2);
      this.cng = var1;
   }

   public double toDouble(Double t) {
      return var1;
   }

   public Double fromDouble(double d) {
      return var1;
   }

   public ProceduralStream<Double> getTypedSource() {
      return null;
   }

   public ProceduralStream<?> getSource() {
      return null;
   }

   public Double get(double x, double z) {
      return this.cng.noise((var1 + this.getOffsetX()) / this.getZoom(), (var3 + this.getOffsetZ()) / this.getZoom());
   }

   public Double get(double x, double y, double z) {
      return this.cng.noise((var1 + this.getOffsetX()) / this.getZoom(), (var3 + this.getOffsetY()) / this.getZoom(), (var5 + this.getOffsetZ()) * this.getZoom());
   }
}
