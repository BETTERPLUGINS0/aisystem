package com.volmit.iris.util.data;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KeyPair;
import java.util.Random;

public class WeightedRandom<T> {
   private final KList<KeyPair<T, Integer>> weightedObjects = new KList();
   private final Random random;
   private int totalWeight = 0;

   public WeightedRandom(Random random) {
      this.random = var1;
   }

   public WeightedRandom() {
      this.random = new Random();
   }

   public void put(T object, int weight) {
      this.weightedObjects.add((Object)(new KeyPair(var1, var2)));
      this.totalWeight += var2;
   }

   public WeightedRandom<T> merge(WeightedRandom<T> other) {
      this.weightedObjects.addAll(var1.weightedObjects);
      this.totalWeight += var1.totalWeight;
      return this;
   }

   public T pullRandom() {
      int var1 = this.random.nextInt(this.totalWeight);

      int var2;
      for(var2 = 0; var1 > 0; ++var2) {
         var1 -= (Integer)((KeyPair)this.weightedObjects.get(var2)).getV();
         if (var1 <= 0) {
            break;
         }
      }

      return ((KeyPair)this.weightedObjects.get(var2)).getK();
   }

   public int getSize() {
      return this.weightedObjects.size();
   }

   public void shuffle() {
      this.weightedObjects.shuffle(this.random);
   }
}
