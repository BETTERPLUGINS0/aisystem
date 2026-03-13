package com.volmit.iris.util.matter;

import com.volmit.iris.util.mantle.Mantle;
import java.util.Iterator;

public interface MatterPlacer {
   int getHeight(int x, int z, boolean ignoreFluid);

   Mantle getMantle();

   default <T> void set(int x, int y, int z, T t) {
      this.getMantle().set(x, y, z, t);
   }

   default <T> T get(int x, int y, int z, Class<T> t) {
      return this.getMantle().get(x, y, z, t);
   }

   default void set(int x, int y, int z, Matter matter) {
      Iterator var5 = matter.getSliceMap().values().iterator();

      while(var5.hasNext()) {
         MatterSlice<?> i = (MatterSlice)var5.next();
         this.set(x, y, z, i);
      }

   }

   default <T> void set(int x, int y, int z, MatterSlice<T> slice) {
      this.getMantle().set(x, y, z, slice);
   }

   default int getHeight(int x, int z) {
      return this.getHeight(x, z, true);
   }

   default int getHeightOrFluid(int x, int z) {
      return this.getHeight(x, z, false);
   }
}
