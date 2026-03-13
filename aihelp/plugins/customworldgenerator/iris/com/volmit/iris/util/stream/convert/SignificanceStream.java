package com.volmit.iris.util.stream.convert;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.stream.ArraySignificance;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;
import com.volmit.iris.util.stream.Significance;

public class SignificanceStream<K extends Significance<T>, T> extends BasicStream<K> {
   private final ProceduralStream<T> stream;
   private final double radius;
   private final int checks;

   public SignificanceStream(ProceduralStream<T> stream, double radius, int checks) {
      this.stream = var1;
      this.radius = var2;
      this.checks = var4;
   }

   public double toDouble(K t) {
      return 0.0D;
   }

   public K fromDouble(double d) {
      return null;
   }

   public K get(double x, double z) {
      KList var5 = new KList(8);
      KList var6 = new KList(8);
      double var7 = 360.0D / (double)this.checks;
      double var9 = 0.0D;

      int var11;
      for(var11 = 0; var11 < 360; var11 = (int)((double)var11 + var7)) {
         double var12 = Math.sin(Math.toRadians((double)var11));
         double var14 = Math.cos(Math.toRadians((double)var11));
         double var16 = var1 + (this.radius * var14 - this.radius * var12);
         double var18 = var3 + this.radius * var12 + this.radius * var14;
         Object var20 = this.stream.get(var16, var18);
         if (var5.addIfMissing(var20)) {
            var6.add((Object)1.0D);
            ++var9;
         } else {
            int var21 = var5.indexOf(var20);
            var6.set(var21, (Double)var6.get(var21) + 1.0D);
         }
      }

      for(var11 = 0; var11 < var6.size(); ++var11) {
         var6.set(var11, (Double)var6.get(var11) / var9);
      }

      return new ArraySignificance(var5, var6);
   }

   public K get(double x, double y, double z) {
      return this.get(var1, var5);
   }
}
