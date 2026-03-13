package com.volmit.iris.engine.object;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.stream.ProceduralStream;
import com.volmit.iris.util.stream.interpolation.Interpolated;
import java.util.Iterator;
import java.util.List;

public interface IRare {
   static <T extends IRare> ProceduralStream<T> stream(ProceduralStream<Double> noise, List<T> possibilities, boolean legacyRarity) {
      return ProceduralStream.of(legacyRarity ? (x, z) -> {
         return pickLegacy(possibilities, (Double)noise.get(x, z));
      } : (x, z) -> {
         return pick(possibilities, (Double)noise.get(x, z));
      }, legacyRarity ? (x, y, z) -> {
         return pickLegacy(possibilities, (Double)noise.get(x, y, z));
      } : (x, y, z) -> {
         return pick(possibilities, (Double)noise.get(x, y, z));
      }, new Interpolated<T>() {
         public double toDouble(T t) {
            return 0.0D;
         }

         public T fromDouble(double d) {
            return null;
         }
      });
   }

   static <T extends IRare> T pickSlowly(List<T> possibilities, double noiseValue) {
      if (possibilities.isEmpty()) {
         return null;
      } else if (possibilities.size() == 1) {
         return (IRare)possibilities.get(0);
      } else {
         KList<T> rarityTypes = new KList();
         int totalRarity = 0;

         Iterator var5;
         IRare i;
         for(var5 = possibilities.iterator(); var5.hasNext(); totalRarity += get(i)) {
            i = (IRare)var5.next();
         }

         var5 = possibilities.iterator();

         while(var5.hasNext()) {
            i = (IRare)var5.next();
            rarityTypes.addMultiple(i, totalRarity / get(i));
         }

         return (IRare)rarityTypes.get((int)(noiseValue * (double)rarityTypes.last()));
      }
   }

   static <T extends IRare> T pick(List<T> possibilities, double noiseValue) {
      if (possibilities.isEmpty()) {
         return null;
      } else if (possibilities.size() == 1) {
         return (IRare)possibilities.getFirst();
      } else {
         double total = 0.0D;

         IRare i;
         for(Iterator var5 = possibilities.iterator(); var5.hasNext(); total += 1.0D / (double)i.getRarity()) {
            i = (IRare)var5.next();
         }

         double threshold = total * noiseValue;
         double buffer = 0.0D;
         Iterator var9 = possibilities.iterator();

         IRare i;
         do {
            if (!var9.hasNext()) {
               return (IRare)possibilities.getLast();
            }

            i = (IRare)var9.next();
            buffer += 1.0D / (double)i.getRarity();
         } while(!(buffer >= threshold));

         return i;
      }
   }

   static <T extends IRare> T pickLegacy(List<T> possibilities, double noiseValue) {
      if (possibilities.isEmpty()) {
         return null;
      } else if (possibilities.size() == 1) {
         return (IRare)possibilities.get(0);
      } else {
         int totalWeight = 0;
         int buffer = 0;

         IRare i;
         for(Iterator var5 = possibilities.iterator(); var5.hasNext(); totalWeight += i.getRarity()) {
            i = (IRare)var5.next();
         }

         double threshold = (double)(totalWeight * (possibilities.size() - 1)) * noiseValue;
         Iterator var7 = possibilities.iterator();

         IRare i;
         do {
            if (!var7.hasNext()) {
               return (IRare)possibilities.get(possibilities.size() - 1);
            }

            i = (IRare)var7.next();
            buffer += totalWeight - i.getRarity();
         } while(!((double)buffer >= threshold));

         return i;
      }
   }

   static <T extends IRare> T pickOld(List<T> possibilities, double noiseValue) {
      if (possibilities.isEmpty()) {
         return null;
      } else if (possibilities.size() == 1) {
         return (IRare)possibilities.get(0);
      } else {
         double completeWeight = 0.0D;
         double highestWeight = 0.0D;

         double countWeight;
         for(Iterator var7 = possibilities.iterator(); var7.hasNext(); completeWeight += countWeight) {
            T item = (IRare)var7.next();
            countWeight = (double)Math.max(item.getRarity(), 1);
            highestWeight = Math.max(highestWeight, countWeight);
         }

         double r = noiseValue * completeWeight;
         countWeight = 0.0D;
         Iterator var11 = possibilities.iterator();

         IRare item;
         do {
            if (!var11.hasNext()) {
               return (IRare)possibilities.get(possibilities.size() - 1);
            }

            item = (IRare)var11.next();
            double weight = Math.max(highestWeight - (double)Math.max(item.getRarity(), 1), 1.0D);
            countWeight += weight;
         } while(!(countWeight >= r));

         return item;
      }
   }

   static int get(Object v) {
      return v instanceof IRare ? Math.max(1, ((IRare)v).getRarity()) : 1;
   }

   int getRarity();
}
