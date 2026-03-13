package com.volmit.iris.util.math;

import java.util.Iterator;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public class Spiral implements Iterable<Position2> {
   private final Position2 start;
   private final long max;

   public static Position2 next(Position2 p) {
      int var1 = var0.getX();
      int var2 = var0.getZ();
      int var3 = Math.abs(var1);
      int var4 = Math.abs(var2);
      if (var1 == 0 && var2 == 0) {
         return var0.add(1, 0);
      } else {
         if (var3 == var4) {
            if (var1 > 0 && var2 > 0) {
               return left(var0);
            }

            if (var1 < 0 && var2 > 0) {
               return down(var0);
            }

            if (var1 < 0 && var2 < 0) {
               return right(var0);
            }

            if (var1 > 0 && var2 < 0) {
               return up(var0);
            }
         } else {
            if (var1 > var2 && var3 > var4) {
               return up(var0);
            }

            if (var1 < var2 && var3 < var4) {
               return left(var0);
            }

            if (var1 < var2 && var3 > var4) {
               return down(var0);
            }

            if (var1 > var2 && var3 < var4) {
               return right(var0);
            }
         }

         return var0;
      }
   }

   public static Spiral from(Position2 p, long iterations) {
      return new Spiral(var0, var1);
   }

   private static Position2 down(Position2 p) {
      return var0.add(0, -1);
   }

   private static Position2 up(Position2 p) {
      return var0.add(0, 1);
   }

   private static Position2 left(Position2 p) {
      return var0.add(-1, 0);
   }

   private static Position2 right(Position2 p) {
      return var0.add(1, 0);
   }

   @NotNull
   public Iterator<Position2> iterator() {
      return new Spiral.SpiralIterator(this, 0L, this.start);
   }

   @Generated
   public Position2 getStart() {
      return this.start;
   }

   @Generated
   public long getMax() {
      return this.max;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Spiral)) {
         return false;
      } else {
         Spiral var2 = (Spiral)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMax() != var2.getMax()) {
            return false;
         } else {
            Position2 var3 = this.getStart();
            Position2 var4 = var2.getStart();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof Spiral;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getMax();
      int var6 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      Position2 var5 = this.getStart();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getStart());
      return "Spiral(start=" + var10000 + ", max=" + this.getMax() + ")";
   }

   @Generated
   public Spiral(final Position2 start, final long max) {
      this.start = var1;
      this.max = var2;
   }

   static class SpiralIterator implements Iterator<Position2> {
      private final Spiral s;
      private long itr = 0L;
      private Position2 cursor;

      public boolean hasNext() {
         return this.itr < this.s.getMax();
      }

      public Position2 next() {
         Position2 var1 = this.cursor;
         this.cursor = Spiral.next(this.cursor);
         ++this.itr;
         return var1;
      }

      @Generated
      public SpiralIterator(final Spiral s, final long itr, final Position2 cursor) {
         this.s = var1;
         this.itr = var2;
         this.cursor = var4;
      }
   }
}
