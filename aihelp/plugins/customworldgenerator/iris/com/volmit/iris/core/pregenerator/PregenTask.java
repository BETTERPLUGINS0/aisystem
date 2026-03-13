package com.volmit.iris.core.pregenerator;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.Spiraled;
import com.volmit.iris.util.math.Spiraler;
import java.util.Comparator;
import java.util.Iterator;
import lombok.Generated;

public class PregenTask {
   private static final Position2 ZERO = new Position2(0, 0);
   private static final KMap<Position2, KList<Position2>> ORDERS = new KMap();
   private final boolean gui;
   private final Position2 center;
   private final int radiusX;
   private final int radiusZ;
   private final PregenTask.Bounds bounds = new PregenTask.Bounds();

   protected PregenTask(boolean gui, Position2 center, int radiusX, int radiusZ) {
      this.gui = var1;
      this.center = new PregenTask.ProxiedPos(var2);
      this.radiusX = var3;
      this.radiusZ = var4;
      this.bounds.update();
   }

   public static void iterateRegion(int xr, int zr, Spiraled s, Position2 pull) {
      Iterator var4 = ((KList)ORDERS.computeIfAbsent(var3, PregenTask::computeOrder)).iterator();

      while(var4.hasNext()) {
         Position2 var5 = (Position2)var4.next();
         var2.on(var5.getX() + (var0 << 5), var5.getZ() + (var1 << 5));
      }

   }

   public static void iterateRegion(int xr, int zr, Spiraled s) {
      iterateRegion(var0, var1, var2, new Position2(-(var0 << 5), -(var1 << 5)));
   }

   private static KList<Position2> computeOrder(Position2 pull) {
      KList var1 = new KList();
      (new Spiraler(33, 33, (var1x, var2) -> {
         int var3 = var1x + 15;
         int var4 = var2 + 15;
         if (var3 >= 0 && var3 <= 31 && var4 >= 0 && var4 <= 31) {
            var1.add((Object)(new Position2(var3, var4)));
         }
      })).drain();
      var1.sort(Comparator.comparing((var1x) -> {
         return var1x.distance(var0);
      }));
      return var1;
   }

   public void iterateRegions(Spiraled s) {
      PregenTask.Bound var2 = this.bounds.region();
      (new Spiraler(var2.sizeX, var2.sizeZ, (var2x, var3) -> {
         if (var2.check(var2x, var3)) {
            var1.on(var2x, var3);
         }

      })).setOffset(this.center.getX() >> 9, this.center.getZ() >> 9).drain();
   }

   public void iterateChunks(int rX, int rZ, Spiraled s) {
      PregenTask.Bound var4 = this.bounds.chunk();
      iterateRegion(var1, var2, (var2x, var3x) -> {
         if (var4.check(var2x, var3x)) {
            var3.on(var2x, var3x);
         }

      });
   }

   public void iterateAllChunks(Spiraled s) {
      this.iterateRegions((var2, var3) -> {
         this.iterateChunks(var2, var3, var1);
      });
   }

   @Generated
   private static boolean $default$gui() {
      return false;
   }

   @Generated
   private static Position2 $default$center() {
      return new Position2(0, 0);
   }

   @Generated
   private static int $default$radiusX() {
      return 1;
   }

   @Generated
   private static int $default$radiusZ() {
      return 1;
   }

   @Generated
   public static PregenTask.PregenTaskBuilder builder() {
      return new PregenTask.PregenTaskBuilder();
   }

   @Generated
   public boolean isGui() {
      return this.gui;
   }

   @Generated
   public Position2 getCenter() {
      return this.center;
   }

   @Generated
   public int getRadiusX() {
      return this.radiusX;
   }

   @Generated
   public int getRadiusZ() {
      return this.radiusZ;
   }

   @Generated
   public PregenTask.Bounds getBounds() {
      return this.bounds;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PregenTask)) {
         return false;
      } else {
         PregenTask var2 = (PregenTask)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isGui() != var2.isGui()) {
            return false;
         } else if (this.getRadiusX() != var2.getRadiusX()) {
            return false;
         } else if (this.getRadiusZ() != var2.getRadiusZ()) {
            return false;
         } else {
            Position2 var3 = this.getCenter();
            Position2 var4 = var2.getCenter();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            PregenTask.Bounds var5 = this.getBounds();
            PregenTask.Bounds var6 = var2.getBounds();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof PregenTask;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + (this.isGui() ? 79 : 97);
      var5 = var5 * 59 + this.getRadiusX();
      var5 = var5 * 59 + this.getRadiusZ();
      Position2 var3 = this.getCenter();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      PregenTask.Bounds var4 = this.getBounds();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      boolean var10000 = this.isGui();
      return "PregenTask(gui=" + var10000 + ", center=" + String.valueOf(this.getCenter()) + ", radiusX=" + this.getRadiusX() + ", radiusZ=" + this.getRadiusZ() + ", bounds=" + String.valueOf(this.getBounds()) + ")";
   }

   private class Bounds {
      private PregenTask.Bound chunk = null;
      private PregenTask.Bound region = null;

      public void update() {
         int var1 = PregenTask.this.center.getX() + PregenTask.this.radiusX;
         int var2 = PregenTask.this.center.getZ() + PregenTask.this.radiusZ;
         int var3 = PregenTask.this.center.getX() - PregenTask.this.radiusX;
         int var4 = PregenTask.this.center.getZ() - PregenTask.this.radiusZ;
         this.chunk = new PregenTask.Bound(var3 >> 4, var4 >> 4, Math.ceilDiv(var1, 16), Math.ceilDiv(var2, 16));
         this.region = new PregenTask.Bound(var3 >> 9, var4 >> 9, Math.ceilDiv(var1, 512), Math.ceilDiv(var2, 512));
      }

      public PregenTask.Bound chunk() {
         if (this.chunk == null) {
            this.update();
         }

         return this.chunk;
      }

      public PregenTask.Bound region() {
         if (this.region == null) {
            this.update();
         }

         return this.region;
      }
   }

   private static class ProxiedPos extends Position2 {
      public ProxiedPos(Position2 p) {
         super(var1.getX(), var1.getZ());
      }

      public void setX(int x) {
         throw new IllegalStateException("This Position2 may not be modified");
      }

      public void setZ(int z) {
         throw new IllegalStateException("This Position2 may not be modified");
      }
   }

   private static record Bound(int minX, int maxX, int minZ, int maxZ, int sizeX, int sizeZ) {
      private Bound(int minX, int minZ, int maxX, int maxZ) {
         this(var1, var3, var2, var4, var4 - var2 + 1, var4 - var2 + 1);
      }

      private Bound(int minX, int maxX, int minZ, int maxZ, int sizeX, int sizeZ) {
         this.minX = var1;
         this.maxX = var2;
         this.minZ = var3;
         this.maxZ = var4;
         this.sizeX = var5;
         this.sizeZ = var6;
      }

      boolean check(int x, int z) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minZ && var2 <= this.maxZ;
      }

      public int minX() {
         return this.minX;
      }

      public int maxX() {
         return this.maxX;
      }

      public int minZ() {
         return this.minZ;
      }

      public int maxZ() {
         return this.maxZ;
      }

      public int sizeX() {
         return this.sizeX;
      }

      public int sizeZ() {
         return this.sizeZ;
      }
   }

   @Generated
   public static class PregenTaskBuilder {
      @Generated
      private boolean gui$set;
      @Generated
      private boolean gui$value;
      @Generated
      private boolean center$set;
      @Generated
      private Position2 center$value;
      @Generated
      private boolean radiusX$set;
      @Generated
      private int radiusX$value;
      @Generated
      private boolean radiusZ$set;
      @Generated
      private int radiusZ$value;

      @Generated
      PregenTaskBuilder() {
      }

      @Generated
      public PregenTask.PregenTaskBuilder gui(final boolean gui) {
         this.gui$value = var1;
         this.gui$set = true;
         return this;
      }

      @Generated
      public PregenTask.PregenTaskBuilder center(final Position2 center) {
         this.center$value = var1;
         this.center$set = true;
         return this;
      }

      @Generated
      public PregenTask.PregenTaskBuilder radiusX(final int radiusX) {
         this.radiusX$value = var1;
         this.radiusX$set = true;
         return this;
      }

      @Generated
      public PregenTask.PregenTaskBuilder radiusZ(final int radiusZ) {
         this.radiusZ$value = var1;
         this.radiusZ$set = true;
         return this;
      }

      @Generated
      public PregenTask build() {
         boolean var1 = this.gui$value;
         if (!this.gui$set) {
            var1 = PregenTask.$default$gui();
         }

         Position2 var2 = this.center$value;
         if (!this.center$set) {
            var2 = PregenTask.$default$center();
         }

         int var3 = this.radiusX$value;
         if (!this.radiusX$set) {
            var3 = PregenTask.$default$radiusX();
         }

         int var4 = this.radiusZ$value;
         if (!this.radiusZ$set) {
            var4 = PregenTask.$default$radiusZ();
         }

         return new PregenTask(var1, var2, var3, var4);
      }

      @Generated
      public String toString() {
         boolean var10000 = this.gui$value;
         return "PregenTask.PregenTaskBuilder(gui$value=" + var10000 + ", center$value=" + String.valueOf(this.center$value) + ", radiusX$value=" + this.radiusX$value + ", radiusZ$value=" + this.radiusZ$value + ")";
      }
   }
}
