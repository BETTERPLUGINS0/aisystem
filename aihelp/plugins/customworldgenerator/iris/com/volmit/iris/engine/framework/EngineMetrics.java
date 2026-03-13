package com.volmit.iris.engine.framework;

import com.volmit.iris.util.atomics.AtomicRollingSequence;
import com.volmit.iris.util.collection.KMap;
import lombok.Generated;

public class EngineMetrics {
   private final AtomicRollingSequence total;
   private final AtomicRollingSequence updates;
   private final AtomicRollingSequence terrain;
   private final AtomicRollingSequence biome;
   private final AtomicRollingSequence parallax;
   private final AtomicRollingSequence parallaxInsert;
   private final AtomicRollingSequence post;
   private final AtomicRollingSequence perfection;
   private final AtomicRollingSequence api;
   private final AtomicRollingSequence decoration;
   private final AtomicRollingSequence cave;
   private final AtomicRollingSequence ravine;
   private final AtomicRollingSequence deposit;

   public EngineMetrics(int mem) {
      this.total = new AtomicRollingSequence(var1);
      this.terrain = new AtomicRollingSequence(var1);
      this.api = new AtomicRollingSequence(var1);
      this.biome = new AtomicRollingSequence(var1);
      this.perfection = new AtomicRollingSequence(var1);
      this.parallax = new AtomicRollingSequence(var1);
      this.parallaxInsert = new AtomicRollingSequence(var1);
      this.post = new AtomicRollingSequence(var1);
      this.decoration = new AtomicRollingSequence(var1);
      this.updates = new AtomicRollingSequence(var1);
      this.cave = new AtomicRollingSequence(var1);
      this.ravine = new AtomicRollingSequence(var1);
      this.deposit = new AtomicRollingSequence(var1);
   }

   public KMap<String, Double> pull() {
      KMap var1 = new KMap();
      var1.put("total", this.total.getAverage());
      var1.put("terrain", this.terrain.getAverage());
      var1.put("biome", this.biome.getAverage());
      var1.put("parallax", this.parallax.getAverage());
      var1.put("parallax.insert", this.parallaxInsert.getAverage());
      var1.put("post", this.post.getAverage());
      var1.put("perfection", this.perfection.getAverage());
      var1.put("decoration", this.decoration.getAverage());
      var1.put("api", this.api.getAverage());
      var1.put("updates", this.updates.getAverage());
      var1.put("cave", this.cave.getAverage());
      var1.put("ravine", this.ravine.getAverage());
      var1.put("deposit", this.deposit.getAverage());
      return var1;
   }

   @Generated
   public AtomicRollingSequence getTotal() {
      return this.total;
   }

   @Generated
   public AtomicRollingSequence getUpdates() {
      return this.updates;
   }

   @Generated
   public AtomicRollingSequence getTerrain() {
      return this.terrain;
   }

   @Generated
   public AtomicRollingSequence getBiome() {
      return this.biome;
   }

   @Generated
   public AtomicRollingSequence getParallax() {
      return this.parallax;
   }

   @Generated
   public AtomicRollingSequence getParallaxInsert() {
      return this.parallaxInsert;
   }

   @Generated
   public AtomicRollingSequence getPost() {
      return this.post;
   }

   @Generated
   public AtomicRollingSequence getPerfection() {
      return this.perfection;
   }

   @Generated
   public AtomicRollingSequence getApi() {
      return this.api;
   }

   @Generated
   public AtomicRollingSequence getDecoration() {
      return this.decoration;
   }

   @Generated
   public AtomicRollingSequence getCave() {
      return this.cave;
   }

   @Generated
   public AtomicRollingSequence getRavine() {
      return this.ravine;
   }

   @Generated
   public AtomicRollingSequence getDeposit() {
      return this.deposit;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof EngineMetrics)) {
         return false;
      } else {
         EngineMetrics var2 = (EngineMetrics)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label167: {
               AtomicRollingSequence var3 = this.getTotal();
               AtomicRollingSequence var4 = var2.getTotal();
               if (var3 == null) {
                  if (var4 == null) {
                     break label167;
                  }
               } else if (var3.equals(var4)) {
                  break label167;
               }

               return false;
            }

            AtomicRollingSequence var5 = this.getUpdates();
            AtomicRollingSequence var6 = var2.getUpdates();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label153: {
               AtomicRollingSequence var7 = this.getTerrain();
               AtomicRollingSequence var8 = var2.getTerrain();
               if (var7 == null) {
                  if (var8 == null) {
                     break label153;
                  }
               } else if (var7.equals(var8)) {
                  break label153;
               }

               return false;
            }

            AtomicRollingSequence var9 = this.getBiome();
            AtomicRollingSequence var10 = var2.getBiome();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label139: {
               AtomicRollingSequence var11 = this.getParallax();
               AtomicRollingSequence var12 = var2.getParallax();
               if (var11 == null) {
                  if (var12 == null) {
                     break label139;
                  }
               } else if (var11.equals(var12)) {
                  break label139;
               }

               return false;
            }

            AtomicRollingSequence var13 = this.getParallaxInsert();
            AtomicRollingSequence var14 = var2.getParallaxInsert();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            label125: {
               AtomicRollingSequence var15 = this.getPost();
               AtomicRollingSequence var16 = var2.getPost();
               if (var15 == null) {
                  if (var16 == null) {
                     break label125;
                  }
               } else if (var15.equals(var16)) {
                  break label125;
               }

               return false;
            }

            label118: {
               AtomicRollingSequence var17 = this.getPerfection();
               AtomicRollingSequence var18 = var2.getPerfection();
               if (var17 == null) {
                  if (var18 == null) {
                     break label118;
                  }
               } else if (var17.equals(var18)) {
                  break label118;
               }

               return false;
            }

            AtomicRollingSequence var19 = this.getApi();
            AtomicRollingSequence var20 = var2.getApi();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            label104: {
               AtomicRollingSequence var21 = this.getDecoration();
               AtomicRollingSequence var22 = var2.getDecoration();
               if (var21 == null) {
                  if (var22 == null) {
                     break label104;
                  }
               } else if (var21.equals(var22)) {
                  break label104;
               }

               return false;
            }

            label97: {
               AtomicRollingSequence var23 = this.getCave();
               AtomicRollingSequence var24 = var2.getCave();
               if (var23 == null) {
                  if (var24 == null) {
                     break label97;
                  }
               } else if (var23.equals(var24)) {
                  break label97;
               }

               return false;
            }

            AtomicRollingSequence var25 = this.getRavine();
            AtomicRollingSequence var26 = var2.getRavine();
            if (var25 == null) {
               if (var26 != null) {
                  return false;
               }
            } else if (!var25.equals(var26)) {
               return false;
            }

            AtomicRollingSequence var27 = this.getDeposit();
            AtomicRollingSequence var28 = var2.getDeposit();
            if (var27 == null) {
               if (var28 != null) {
                  return false;
               }
            } else if (!var27.equals(var28)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof EngineMetrics;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      AtomicRollingSequence var3 = this.getTotal();
      int var16 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      AtomicRollingSequence var4 = this.getUpdates();
      var16 = var16 * 59 + (var4 == null ? 43 : var4.hashCode());
      AtomicRollingSequence var5 = this.getTerrain();
      var16 = var16 * 59 + (var5 == null ? 43 : var5.hashCode());
      AtomicRollingSequence var6 = this.getBiome();
      var16 = var16 * 59 + (var6 == null ? 43 : var6.hashCode());
      AtomicRollingSequence var7 = this.getParallax();
      var16 = var16 * 59 + (var7 == null ? 43 : var7.hashCode());
      AtomicRollingSequence var8 = this.getParallaxInsert();
      var16 = var16 * 59 + (var8 == null ? 43 : var8.hashCode());
      AtomicRollingSequence var9 = this.getPost();
      var16 = var16 * 59 + (var9 == null ? 43 : var9.hashCode());
      AtomicRollingSequence var10 = this.getPerfection();
      var16 = var16 * 59 + (var10 == null ? 43 : var10.hashCode());
      AtomicRollingSequence var11 = this.getApi();
      var16 = var16 * 59 + (var11 == null ? 43 : var11.hashCode());
      AtomicRollingSequence var12 = this.getDecoration();
      var16 = var16 * 59 + (var12 == null ? 43 : var12.hashCode());
      AtomicRollingSequence var13 = this.getCave();
      var16 = var16 * 59 + (var13 == null ? 43 : var13.hashCode());
      AtomicRollingSequence var14 = this.getRavine();
      var16 = var16 * 59 + (var14 == null ? 43 : var14.hashCode());
      AtomicRollingSequence var15 = this.getDeposit();
      var16 = var16 * 59 + (var15 == null ? 43 : var15.hashCode());
      return var16;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getTotal());
      return "EngineMetrics(total=" + var10000 + ", updates=" + String.valueOf(this.getUpdates()) + ", terrain=" + String.valueOf(this.getTerrain()) + ", biome=" + String.valueOf(this.getBiome()) + ", parallax=" + String.valueOf(this.getParallax()) + ", parallaxInsert=" + String.valueOf(this.getParallaxInsert()) + ", post=" + String.valueOf(this.getPost()) + ", perfection=" + String.valueOf(this.getPerfection()) + ", api=" + String.valueOf(this.getApi()) + ", decoration=" + String.valueOf(this.getDecoration()) + ", cave=" + String.valueOf(this.getCave()) + ", ravine=" + String.valueOf(this.getRavine()) + ", deposit=" + String.valueOf(this.getDeposit()) + ")";
   }
}
