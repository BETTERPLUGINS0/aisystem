package com.volmit.iris.util.noise;

import com.volmit.iris.engine.object.IRare;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;

public class RarityCellGenerator<T extends IRare> extends CellGenerator {
   public RarityCellGenerator(RNG rng) {
      super(var1);
   }

   public T get(double x, double z, KList<T> b) {
      if (var5.size() == 0) {
         return null;
      } else if (var5.size() == 1) {
         return (IRare)var5.get(0);
      } else {
         KList var6 = new KList();
         boolean var7 = false;
         int var8 = 1;
         Iterator var9 = var5.iterator();

         IRare var10;
         while(var9.hasNext()) {
            var10 = (IRare)var9.next();
            if (var10.getRarity() > var8) {
               var8 = var10.getRarity();
            }
         }

         ++var8;
         var9 = var5.iterator();

         while(var9.hasNext()) {
            var10 = (IRare)var9.next();

            for(int var11 = 0; var11 < var8 - var10.getRarity(); ++var11) {
               if (var7 = !var7) {
                  var6.add((Object)var10);
               } else {
                  var6.add(0, var10);
               }
            }
         }

         if (var6.size() == 1) {
            return (IRare)var6.get(0);
         } else if (var6.isEmpty()) {
            throw new RuntimeException("BAD RARITY MAP! RELATED TO: " + var5.toString(", or possibly "));
         } else {
            return (IRare)var6.get(this.getIndex(var1, var3, var6.size()));
         }
      }
   }
}
