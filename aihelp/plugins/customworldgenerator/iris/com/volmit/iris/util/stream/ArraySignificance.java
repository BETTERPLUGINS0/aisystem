package com.volmit.iris.util.stream;

import com.volmit.iris.util.collection.KList;

public class ArraySignificance<T> implements Significance<T> {
   private final KList<T> types;
   private final KList<Double> significance;
   private final T significant;

   public ArraySignificance(KList<T> types, KList<Double> significance, T significant) {
      this.types = var1;
      this.significance = var2;
      this.significant = var3;
   }

   public ArraySignificance(KList<T> types, KList<Double> significance) {
      this.types = var1;
      this.significance = var2;
      double var3 = 0.0D;
      int var5 = 0;

      for(int var6 = 0; var6 < var2.size(); ++var6) {
         if ((Double)var2.get(var6) > var3) {
            var3 = (Double)var2.get(var6);
            var5 = var6;
         }
      }

      this.significant = var1.get(var5);
   }

   public KList<T> getFactorTypes() {
      return this.types;
   }

   public double getSignificance(T t) {
      for(int var2 = 0; var2 < this.types.size(); ++var2) {
         if (this.types.get(var2).equals(var1)) {
            return (Double)this.significance.get(var2);
         }
      }

      return 0.0D;
   }

   public T getMostSignificantType() {
      return this.significant;
   }
}
