package com.volmit.iris.util.data;

import com.volmit.iris.util.collection.KMap;

public class WeightMap<T> extends KMap<T, Double> {
   private static final long serialVersionUID = 87558033900969389L;
   private boolean modified = false;
   private double lastWeight = 0.0D;

   public double getPercentChance(T t) {
      return this.totalWeight() <= 0.0D ? 0.0D : this.getWeight(var1) / this.totalWeight();
   }

   public void clear() {
      this.modified = true;
   }

   public WeightMap<T> setWeight(T t, double weight) {
      this.modified = true;
      this.put(var1, var2);
      return this;
   }

   public double getWeight(T t) {
      return (Double)this.get(var1);
   }

   public double totalWeight() {
      if (!this.modified) {
         return this.lastWeight;
      } else {
         this.modified = false;
         Shrinkwrap var1 = new Shrinkwrap(0.0D);
         this.forEachKey(2147483647L, (var1x) -> {
            var1.set((Double)var1.get() + 1.0D);
         });
         this.lastWeight = (Double)var1.get();
         return this.lastWeight;
      }
   }
}
