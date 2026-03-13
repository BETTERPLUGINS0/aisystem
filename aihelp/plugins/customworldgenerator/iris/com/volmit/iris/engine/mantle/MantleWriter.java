package com.volmit.iris.engine.mantle;

import com.google.common.collect.ImmutableList;
import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IObjectPlacer;
import com.volmit.iris.engine.object.IrisGeneratorStyle;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.engine.object.TileData;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.function.Function3;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.Matter;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.matter.TileWrapper;
import com.volmit.iris.util.noise.CNG;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

public class MantleWriter implements IObjectPlacer, AutoCloseable {
   private final EngineMantle engineMantle;
   private final Mantle mantle;
   private final Map<Long, MantleChunk> cachedChunks;
   private final int radius;
   private final int x;
   private final int z;

   public MantleWriter(EngineMantle engineMantle, Mantle mantle, int x, int z, int radius, boolean multicore) {
      this.engineMantle = var1;
      this.mantle = var2;
      this.radius = var5 * 2;
      int var7 = this.radius + 1;
      this.cachedChunks = (Map)(var6 ? new KMap(var7 * var7, 0.75F, Math.max(32, Runtime.getRuntime().availableProcessors() * 4)) : new Long2ObjectOpenHashMap(var7 * var7));
      this.x = var3;
      this.z = var4;
      int var8 = var6 ? Runtime.getRuntime().availableProcessors() / 2 : 4;
      Object var9 = var6 ? this.cachedChunks : new KMap(var7 * var7, 1.0F, var8);
      var2.getChunks(var3 - var5, var3 + var5, var4 - var5, var4 + var5, var8, (var1x, var2x, var3x) -> {
         var9.put(Cache.key(var1x, var2x), var3x.use());
      });
      if (!var6) {
         this.cachedChunks.putAll((Map)var9);
      }

   }

   private static Set<IrisPosition> getBallooned(Set<IrisPosition> vset, double radius) {
      HashSet var3 = new HashSet();
      int var4 = (int)Math.ceil(var1);
      double var5 = Math.pow(var1, 2.0D);
      Iterator var7 = var0.iterator();

      while(var7.hasNext()) {
         IrisPosition var8 = (IrisPosition)var7.next();
         int var9 = var8.getX();
         int var10 = var8.getY();
         int var11 = var8.getZ();

         for(int var12 = var9 - var4; var12 <= var9 + var4; ++var12) {
            for(int var13 = var10 - var4; var13 <= var10 + var4; ++var13) {
               for(int var14 = var11 - var4; var14 <= var11 + var4; ++var14) {
                  if (hypot((double)(var12 - var9), (double)(var13 - var10), (double)(var14 - var11)) <= var5) {
                     var3.add(new IrisPosition(var12, var13, var14));
                  }
               }
            }
         }
      }

      return var3;
   }

   private static Set<IrisPosition> getHollowed(Set<IrisPosition> vset) {
      KSet var1 = new KSet(new IrisPosition[0]);
      Iterator var2 = var0.iterator();

      while(true) {
         IrisPosition var3;
         double var4;
         double var6;
         double var8;
         do {
            if (!var2.hasNext()) {
               return var1;
            }

            var3 = (IrisPosition)var2.next();
            var4 = (double)var3.getX();
            var6 = (double)var3.getY();
            var8 = (double)var3.getZ();
         } while(var0.contains(new IrisPosition(var4 + 1.0D, var6, var8)) && var0.contains(new IrisPosition(var4 - 1.0D, var6, var8)) && var0.contains(new IrisPosition(var4, var6 + 1.0D, var8)) && var0.contains(new IrisPosition(var4, var6 - 1.0D, var8)) && var0.contains(new IrisPosition(var4, var6, var8 + 1.0D)) && var0.contains(new IrisPosition(var4, var6, var8 - 1.0D)));

         var1.add(var3);
      }
   }

   private static double hypot(double... pars) {
      double var1 = 0.0D;
      double[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         double var6 = var3[var5];
         var1 += Math.pow(var6, 2.0D);
      }

      return var1;
   }

   private static double lengthSq(double x, double y, double z) {
      return var0 * var0 + var2 * var2 + var4 * var4;
   }

   private static double lengthSq(double x, double z) {
      return var0 * var0 + var2 * var2;
   }

   public <T> void setDataWarped(int x, int y, int z, T t, RNG rng, IrisData data, IrisGeneratorStyle style) {
      this.setData((int)Math.round(var7.warp(var5, var6, (double)var1, (double)var1, (double)var2, (double)(-var3))), (int)Math.round(var7.warp(var5, var6, (double)var2, (double)var3, (double)(-var1), (double)var2)), (int)Math.round(var7.warp(var5, var6, (double)var3, (double)(-var2), (double)var3, (double)var1)), var4);
   }

   public <T> void setData(int x, int y, int z, T t) {
      if (var4 != null) {
         int var5 = var1 >> 4;
         int var6 = var3 >> 4;
         if (var2 >= 0 && var2 < this.mantle.getWorldHeight()) {
            MantleChunk var7 = this.acquireChunk(var5, var6);
            if (var7 != null) {
               Matter var8 = var7.getOrCreate(var2 >> 4);
               var8.slice(var8.getClass(var4)).set(var1 & 15, var2 & 15, var3 & 15, var4);
            }
         }
      }
   }

   public <T> T getData(int x, int y, int z, Class<T> type) {
      int var5 = var1 >> 4;
      int var6 = var3 >> 4;
      if (var2 >= 0 && var2 < this.mantle.getWorldHeight()) {
         MantleChunk var7 = this.acquireChunk(var5, var6);
         return var7 == null ? null : var7.getOrCreate(var2 >> 4).slice(var4).get(var1 & 15, var2 & 15, var3 & 15);
      } else {
         return null;
      }
   }

   @ChunkCoordinates
   public MantleChunk acquireChunk(int cx, int cz) {
      if (var1 >= this.x - this.radius && var1 <= this.x + this.radius && var2 >= this.z - this.radius && var2 <= this.z + this.radius) {
         Long var3 = Cache.key(var1, var2);
         MantleChunk var4 = (MantleChunk)this.cachedChunks.get(var3);
         if (var4 == null) {
            var4 = this.mantle.getChunk(var1, var2).use();
            MantleChunk var5 = (MantleChunk)this.cachedChunks.put(var3, var4);
            if (var5 != null) {
               var5.release();
            }
         }

         return var4;
      } else {
         Iris.error("Mantle Writer Accessed chunk out of bounds" + var1 + "," + var2);
         return null;
      }
   }

   public int getHighest(int x, int z, IrisData data) {
      return this.engineMantle.getHighest(var1, var2, var3);
   }

   public int getHighest(int x, int z, IrisData data, boolean ignoreFluid) {
      return this.engineMantle.getHighest(var1, var2, var3, var4);
   }

   public void set(int x, int y, int z, BlockData d) {
      if (var4 instanceof IrisCustomData) {
         IrisCustomData var5 = (IrisCustomData)var4;
         this.setData(var1, var2, var3, var5.getBase());
         this.setData(var1, var2, var3, var5.getCustom());
      } else {
         this.setData(var1, var2, var3, var4);
      }

   }

   public BlockData get(int x, int y, int z) {
      BlockData var4 = (BlockData)this.getData(var1, var2, var3, BlockData.class);
      return var4 == null ? EngineMantle.AIR : var4;
   }

   public boolean isPreventingDecay() {
      return this.getEngineMantle().isPreventingDecay();
   }

   public boolean isCarved(int x, int y, int z) {
      return this.getData(var1, var2, var3, MatterCavern.class) != null;
   }

   public boolean isSolid(int x, int y, int z) {
      return B.isSolid(this.get(var1, var2, var3));
   }

   public boolean isUnderwater(int x, int z) {
      return this.getEngineMantle().isUnderwater(var1, var2);
   }

   public int getFluidHeight() {
      return this.getEngineMantle().getFluidHeight();
   }

   public boolean isDebugSmartBore() {
      return this.getEngineMantle().isDebugSmartBore();
   }

   public void setTile(int xx, int yy, int zz, TileData tile) {
      this.setData(var1, var2, var3, new TileWrapper(var4));
   }

   public Engine getEngine() {
      return this.getEngineMantle().getEngine();
   }

   public <T> void setSphere(int cx, int cy, int cz, double radius, boolean fill, T data) {
      this.setElipsoid(var1, var2, var3, var4, var4, var4, var6, var7);
   }

   public <T> void setElipsoid(int cx, int cy, int cz, double rx, double ry, double rz, boolean fill, T data) {
      this.setElipsoidFunction(var1, var2, var3, var4, var6, var8, var10, (var1x, var2x, var3x) -> {
         return var11;
      });
   }

   public <T> void setElipsoidWarped(int cx, int cy, int cz, double rx, double ry, double rz, boolean fill, T data, RNG rng, IrisData idata, IrisGeneratorStyle style) {
      this.setElipsoidFunctionWarped(var1, var2, var3, var4, var6, var8, var10, (var1x, var2x, var3x) -> {
         return var11;
      }, var12, var13, var14);
   }

   public <T> void setElipsoidFunction(int cx, int cy, int cz, double rx, double ry, double rz, boolean fill, Function3<Integer, Integer, Integer, T> data) {
      var4 += 0.5D;
      var6 += 0.5D;
      var8 += 0.5D;
      double var12 = 1.0D / var4;
      double var14 = 1.0D / var6;
      double var16 = 1.0D / var8;
      int var18 = (int)Math.ceil(var4);
      int var19 = (int)Math.ceil(var6);
      int var20 = (int)Math.ceil(var8);
      double var21 = 0.0D;

      label50:
      for(int var23 = 0; var23 <= var18; ++var23) {
         double var24 = var21;
         var21 = (double)(var23 + 1) * var12;
         double var26 = 0.0D;

         for(int var28 = 0; var28 <= var19; ++var28) {
            double var29 = var26;
            var26 = (double)(var28 + 1) * var14;
            double var31 = 0.0D;

            for(int var33 = 0; var33 <= var20; ++var33) {
               double var34 = var31;
               var31 = (double)(var33 + 1) * var16;
               double var36 = lengthSq(var24, var29, var34);
               if (var36 > 1.0D) {
                  if (var33 == 0) {
                     if (var28 == 0) {
                        return;
                     }
                     continue label50;
                  }
                  break;
               }

               if (var10 || !(lengthSq(var21, var29, var34) <= 1.0D) || !(lengthSq(var24, var26, var34) <= 1.0D) || !(lengthSq(var24, var29, var31) <= 1.0D)) {
                  this.setData(var23 + var1, var28 + var2, var33 + var3, var11.apply(var23 + var1, var28 + var2, var33 + var3));
                  this.setData(-var23 + var1, var28 + var2, var33 + var3, var11.apply(-var23 + var1, var28 + var2, var33 + var3));
                  this.setData(var23 + var1, -var28 + var2, var33 + var3, var11.apply(var23 + var1, -var28 + var2, var33 + var3));
                  this.setData(var23 + var1, var28 + var2, -var33 + var3, var11.apply(var23 + var1, var28 + var2, -var33 + var3));
                  this.setData(-var23 + var1, var28 + var2, -var33 + var3, var11.apply(-var23 + var1, var28 + var2, -var33 + var3));
                  this.setData(-var23 + var1, -var28 + var2, var33 + var3, var11.apply(-var23 + var1, -var28 + var2, var33 + var3));
                  this.setData(var23 + var1, -var28 + var2, -var33 + var3, var11.apply(var23 + var1, -var28 + var2, -var33 + var3));
                  this.setData(-var23 + var1, var28 + var2, -var33 + var3, var11.apply(-var23 + var1, var28 + var2, -var33 + var3));
                  this.setData(-var23 + var1, -var28 + var2, -var33 + var3, var11.apply(-var23 + var1, -var28 + var2, -var33 + var3));
               }
            }
         }
      }

   }

   public <T> void setElipsoidFunctionWarped(int cx, int cy, int cz, double rx, double ry, double rz, boolean fill, Function3<Integer, Integer, Integer, T> data, RNG rng, IrisData idata, IrisGeneratorStyle style) {
      var4 += 0.5D;
      var6 += 0.5D;
      var8 += 0.5D;
      double var15 = 1.0D / var4;
      double var17 = 1.0D / var6;
      double var19 = 1.0D / var8;
      int var21 = (int)Math.ceil(var4);
      int var22 = (int)Math.ceil(var6);
      int var23 = (int)Math.ceil(var8);
      double var24 = 0.0D;

      label50:
      for(int var26 = 0; var26 <= var21; ++var26) {
         double var27 = var24;
         var24 = (double)(var26 + 1) * var15;
         double var29 = 0.0D;

         for(int var31 = 0; var31 <= var22; ++var31) {
            double var32 = var29;
            var29 = (double)(var31 + 1) * var17;
            double var34 = 0.0D;

            for(int var36 = 0; var36 <= var23; ++var36) {
               double var37 = var34;
               var34 = (double)(var36 + 1) * var19;
               double var39 = lengthSq(var27, var32, var37);
               if (var39 > 1.0D) {
                  if (var36 == 0) {
                     if (var31 == 0) {
                        return;
                     }
                     continue label50;
                  }
                  break;
               }

               if (var10 || !(lengthSq(var24, var32, var37) <= 1.0D) || !(lengthSq(var27, var29, var37) <= 1.0D) || !(lengthSq(var27, var32, var34) <= 1.0D)) {
                  this.setDataWarped(var26 + var1, var31 + var2, var36 + var3, var11.apply(var26 + var1, var31 + var2, var36 + var3), var12, var13, var14);
                  this.setDataWarped(-var26 + var1, var31 + var2, var36 + var3, var11.apply(-var26 + var1, var31 + var2, var36 + var3), var12, var13, var14);
                  this.setDataWarped(var26 + var1, -var31 + var2, var36 + var3, var11.apply(var26 + var1, -var31 + var2, var36 + var3), var12, var13, var14);
                  this.setDataWarped(var26 + var1, var31 + var2, -var36 + var3, var11.apply(var26 + var1, var31 + var2, -var36 + var3), var12, var13, var14);
                  this.setDataWarped(-var26 + var1, var31 + var2, -var36 + var3, var11.apply(-var26 + var1, var31 + var2, -var36 + var3), var12, var13, var14);
                  this.setDataWarped(-var26 + var1, -var31 + var2, var36 + var3, var11.apply(-var26 + var1, -var31 + var2, var36 + var3), var12, var13, var14);
                  this.setDataWarped(var26 + var1, -var31 + var2, -var36 + var3, var11.apply(var26 + var1, -var31 + var2, -var36 + var3), var12, var13, var14);
                  this.setDataWarped(-var26 + var1, var31 + var2, -var36 + var3, var11.apply(-var26 + var1, var31 + var2, -var36 + var3), var12, var13, var14);
                  this.setDataWarped(-var26 + var1, -var31 + var2, -var36 + var3, var11.apply(-var26 + var1, -var31 + var2, -var36 + var3), var12, var13, var14);
               }
            }
         }
      }

   }

   public <T> void setCuboid(int x1, int y1, int z1, int x2, int y2, int z2, T data) {
      for(int var10 = var1; var10 <= var4; ++var10) {
         for(int var8 = var1; var8 <= var4; ++var8) {
            for(int var9 = var1; var9 <= var4; ++var9) {
               this.setData(var10, var8, var9, var7);
            }
         }
      }

   }

   public <T> void setPyramid(int cx, int cy, int cz, T data, int size, boolean filled) {
      int var7 = var5;

      for(int var8 = 0; var8 <= var7; ++var8) {
         --var5;

         for(int var9 = 0; var9 <= var5; ++var9) {
            for(int var10 = 0; var10 <= var5; ++var10) {
               if (var6 && var10 <= var5 && var9 <= var5 || var10 == var5 || var9 == var5) {
                  this.setData(var9 + var1, var8 + var2, var10 + var3, var4);
                  this.setData(-var9 + var1, var8 + var2, var10 + var3, var4);
                  this.setData(var9 + var1, var8 + var2, -var10 + var3, var4);
                  this.setData(-var9 + var1, var8 + var2, -var10 + var3, var4);
               }
            }
         }
      }

   }

   public <T> void setLine(IrisPosition a, IrisPosition b, double radius, boolean filled, T data) {
      this.setLine(ImmutableList.of(var1, var2), var3, var5, var6);
   }

   public <T> void setLine(List<IrisPosition> vectors, double radius, boolean filled, T data) {
      this.setLineConsumer(var1, var2, var4, (var1x, var2x, var3) -> {
         return var5;
      });
   }

   public <T> void setLineConsumer(List<IrisPosition> vectors, double radius, boolean filled, Function3<Integer, Integer, Integer, T> data) {
      Set var6 = cleanup(var1);
      var6 = getBallooned(var6, var2);
      if (!var4) {
         var6 = getHollowed(var6);
      }

      this.setConsumer(var6, var5);
   }

   public <T> void setNoiseMasked(List<IrisPosition> vectors, double radius, double threshold, CNG shape, Set<IrisPosition> masks, boolean filled, Function3<Integer, Integer, Integer, T> data) {
      Set var10 = cleanup(var1);
      var10 = var7 == null ? getBallooned(var10, var2) : getMasked(var10, var7, var2);
      var10.removeIf((var3) -> {
         return var6.noise((double)var3.getX(), (double)var3.getY(), (double)var3.getZ()) < var4;
      });
      if (!var8) {
         var10 = getHollowed(var10);
      }

      this.setConsumer(var10, var9);
   }

   private static Set<IrisPosition> getMasked(Set<IrisPosition> vectors, Set<IrisPosition> masks, double radius) {
      KSet var4 = new KSet(new IrisPosition[0]);
      int var5 = (int)Math.ceil(var2);
      double var6 = Math.pow(var2, 2.0D);
      Iterator var8 = var0.iterator();

      while(var8.hasNext()) {
         IrisPosition var9 = (IrisPosition)var8.next();
         int var10 = var9.getX();
         int var11 = var9.getY();
         int var12 = var9.getZ();

         for(int var13 = -var5; var13 <= var5; ++var13) {
            for(int var14 = -var5; var14 <= var5; ++var14) {
               for(int var15 = -var5; var15 <= var5; ++var15) {
                  if (!(hypot((double)var13, (double)var14, (double)var15) > var6) && var1.contains(new IrisPosition(var13, var14, var15))) {
                     var4.add(new IrisPosition(var10 + var13, var11 + var14, var12 + var15));
                  }
               }
            }
         }
      }

      return var4;
   }

   private static Set<IrisPosition> cleanup(List<IrisPosition> vectors) {
      KSet var1 = new KSet(new IrisPosition[0]);

      for(int var2 = 0; var0.size() != 0 && var2 < var0.size() - 1; ++var2) {
         IrisPosition var3 = (IrisPosition)var0.get(var2);
         IrisPosition var4 = (IrisPosition)var0.get(var2 + 1);
         int var5 = var3.getX();
         int var6 = var3.getY();
         int var7 = var3.getZ();
         int var8 = var4.getX();
         int var9 = var4.getY();
         int var10 = var4.getZ();
         int var14 = Math.abs(var8 - var5);
         int var15 = Math.abs(var9 - var6);
         int var16 = Math.abs(var10 - var7);
         if (var14 + var15 + var16 == 0) {
            var1.add(new IrisPosition(var5, var6, var7));
         } else {
            int var17 = Math.max(Math.max(var14, var15), var16);
            int var11;
            int var12;
            int var13;
            int var18;
            if (var17 == var14) {
               for(var18 = 0; var18 <= var14; ++var18) {
                  var11 = var5 + var18 * (var8 - var5 > 0 ? 1 : -1);
                  var12 = (int)Math.round((double)var6 + (double)var18 * (double)var15 / (double)var14 * (double)(var9 - var6 > 0 ? 1 : -1));
                  var13 = (int)Math.round((double)var7 + (double)var18 * (double)var16 / (double)var14 * (double)(var10 - var7 > 0 ? 1 : -1));
                  var1.add(new IrisPosition(var11, var12, var13));
               }
            } else if (var17 == var15) {
               for(var18 = 0; var18 <= var15; ++var18) {
                  var12 = var6 + var18 * (var9 - var6 > 0 ? 1 : -1);
                  var11 = (int)Math.round((double)var5 + (double)var18 * (double)var14 / (double)var15 * (double)(var8 - var5 > 0 ? 1 : -1));
                  var13 = (int)Math.round((double)var7 + (double)var18 * (double)var16 / (double)var15 * (double)(var10 - var7 > 0 ? 1 : -1));
                  var1.add(new IrisPosition(var11, var12, var13));
               }
            } else {
               for(var18 = 0; var18 <= var16; ++var18) {
                  var13 = var7 + var18 * (var10 - var7 > 0 ? 1 : -1);
                  var12 = (int)Math.round((double)var6 + (double)var18 * (double)var15 / (double)var16 * (double)(var9 - var6 > 0 ? 1 : -1));
                  var11 = (int)Math.round((double)var5 + (double)var18 * (double)var14 / (double)var16 * (double)(var8 - var5 > 0 ? 1 : -1));
                  var1.add(new IrisPosition(var11, var12, var13));
               }
            }
         }
      }

      return var1;
   }

   public <T> void setCylinder(int cx, int cy, int cz, T data, double radius, int height, boolean filled) {
      this.setCylinder(var1, var2, var3, var4, var5, var5, var7, var8);
   }

   public <T> void setCylinder(int cx, int cy, int cz, T data, double radiusX, double radiusZ, int height, boolean filled) {
      boolean var11 = false;
      var5 += 0.5D;
      var7 += 0.5D;
      if (var9 != 0) {
         if (var9 < 0) {
            var9 = -var9;
            var2 -= var9;
         }

         if (var2 < 0) {
            var2 = 0;
         } else if (var2 + var9 - 1 > this.getMantle().getWorldHeight()) {
            var9 = this.getMantle().getWorldHeight() - var2 + 1;
         }

         double var12 = 1.0D / var5;
         double var14 = 1.0D / var7;
         int var16 = (int)Math.ceil(var5);
         int var17 = (int)Math.ceil(var7);
         double var18 = 0.0D;

         for(int var20 = 0; var20 <= var16; ++var20) {
            double var21 = var18;
            var18 = (double)(var20 + 1) * var12;
            double var23 = 0.0D;

            for(int var25 = 0; var25 <= var17; ++var25) {
               double var26 = var23;
               var23 = (double)(var25 + 1) * var14;
               double var28 = lengthSq(var21, var26);
               if (var28 > 1.0D) {
                  if (var25 == 0) {
                     return;
                  }
                  break;
               }

               if (var10 || !(lengthSq(var18, var26) <= 1.0D) || !(lengthSq(var21, var23) <= 1.0D)) {
                  for(int var30 = 0; var30 < var9; ++var30) {
                     this.setData(var1 + var20, var2 + var30, var3 + var25, var4);
                     this.setData(var1 + -var20, var2 + var30, var3 + var25, var4);
                     this.setData(var1 + var20, var2 + var30, var3 + -var25, var4);
                     this.setData(var1 + -var20, var2 + var30, var3 + -var25, var4);
                  }
               }
            }
         }

      }
   }

   public <T> void set(IrisPosition pos, T data) {
      try {
         this.setData(var1.getX(), var1.getY(), var1.getZ(), var2);
      } catch (Throwable var4) {
         Iris.error("No set? " + var2.toString() + " for " + var1.toString());
      }

   }

   public <T> void set(List<IrisPosition> positions, T data) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         IrisPosition var4 = (IrisPosition)var3.next();
         this.set(var4, var2);
      }

   }

   public <T> void set(Set<IrisPosition> positions, T data) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         IrisPosition var4 = (IrisPosition)var3.next();
         this.set(var4, var2);
      }

   }

   public <T> void setConsumer(Set<IrisPosition> positions, Function3<Integer, Integer, Integer, T> data) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         IrisPosition var4 = (IrisPosition)var3.next();
         this.set(var4, var2.apply(var4.getX(), var4.getY(), var4.getZ()));
      }

   }

   public boolean isWithin(Vector pos) {
      return this.isWithin(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   public boolean isWithin(int x, int y, int z) {
      int var4 = var1 >> 4;
      int var5 = var3 >> 4;
      if (var2 >= 0 && var2 < this.mantle.getWorldHeight()) {
         return var4 >= this.x - this.radius && var4 <= this.x + this.radius && var5 >= this.z - this.radius && var5 <= this.z + this.radius;
      } else {
         return false;
      }
   }

   public void close() {
      Iterator var1 = this.cachedChunks.values().iterator();

      while(var1.hasNext()) {
         ((MantleChunk)var1.next()).release();
         var1.remove();
      }

   }

   @Generated
   public EngineMantle getEngineMantle() {
      return this.engineMantle;
   }

   @Generated
   public Mantle getMantle() {
      return this.mantle;
   }

   @Generated
   public Map<Long, MantleChunk> getCachedChunks() {
      return this.cachedChunks;
   }

   @Generated
   public int getRadius() {
      return this.radius;
   }

   @Generated
   public int getX() {
      return this.x;
   }

   @Generated
   public int getZ() {
      return this.z;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MantleWriter)) {
         return false;
      } else {
         MantleWriter var2 = (MantleWriter)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRadius() != var2.getRadius()) {
            return false;
         } else if (this.getX() != var2.getX()) {
            return false;
         } else if (this.getZ() != var2.getZ()) {
            return false;
         } else {
            label54: {
               EngineMantle var3 = this.getEngineMantle();
               EngineMantle var4 = var2.getEngineMantle();
               if (var3 == null) {
                  if (var4 == null) {
                     break label54;
                  }
               } else if (var3.equals(var4)) {
                  break label54;
               }

               return false;
            }

            Mantle var5 = this.getMantle();
            Mantle var6 = var2.getMantle();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            Map var7 = this.getCachedChunks();
            Map var8 = var2.getCachedChunks();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof MantleWriter;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var6 = var2 * 59 + this.getRadius();
      var6 = var6 * 59 + this.getX();
      var6 = var6 * 59 + this.getZ();
      EngineMantle var3 = this.getEngineMantle();
      var6 = var6 * 59 + (var3 == null ? 43 : var3.hashCode());
      Mantle var4 = this.getMantle();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      Map var5 = this.getCachedChunks();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getEngineMantle());
      return "MantleWriter(engineMantle=" + var10000 + ", mantle=" + String.valueOf(this.getMantle()) + ", cachedChunks=" + String.valueOf(this.getCachedChunks()) + ", radius=" + this.getRadius() + ", x=" + this.getX() + ", z=" + this.getZ() + ")";
   }
}
