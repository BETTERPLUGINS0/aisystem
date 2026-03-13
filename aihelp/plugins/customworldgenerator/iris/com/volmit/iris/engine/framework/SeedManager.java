package com.volmit.iris.engine.framework;

import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import lombok.Generated;

public class SeedManager {
   private static final String IRIS_SIGNATURE = "Iris World Generator";
   private static final long IRIS_TERRAIN_VERSION = 1L;
   private final RNG rlock;
   private final CNG soup;
   private final long seed;
   private final long complex;
   private final long complexStreams;
   private final long basic;
   private final long height;
   private final long component;
   private final long script;
   private final long mantle;
   private final long entity;
   private final long biome;
   private final long decorator;
   private final long terrain;
   private final long spawn;
   private final long jigsaw;
   private final long carve;
   private final long deposit;
   private final long post;
   private final long bodies;
   private final long mode;
   private long fullMixedSeed;

   public SeedManager(long seed) {
      this.soup = this.createSoup(var1);
      this.rlock = new RNG(Double.doubleToLongBits(this.soup.fitDouble(Double.MIN_VALUE, Double.MAX_VALUE, (double)(var1 + 1337L), (double)(var1 * 69L), (double)var1)));
      this.seed = var1;
      this.complex = this.of("complex");
      this.complexStreams = this.of("complex_streams");
      this.basic = this.of("basic");
      this.height = this.of("height");
      this.component = this.of("component");
      this.script = this.of("script");
      this.mantle = this.of("mantle");
      this.entity = this.of("entity");
      this.biome = this.of("biome");
      this.decorator = this.of("decorator");
      this.terrain = this.of("terrain");
      this.spawn = this.of("spawn");
      this.jigsaw = this.of("jigsaw");
      this.carve = this.of("carve");
      this.deposit = this.of("deposit");
      this.post = this.of("post");
      this.bodies = this.of("bodies");
      this.mode = this.of("mode");
   }

   private long of(String name) {
      RNG var2 = new RNG(var1 + "Iris World Generator::1" + (this.seed + (long)this.rlock.imax()) * this.rlock.lmax());
      long var3 = (long)this.rlock.imax() * (long)(this.rlock.chance(0.5D) ? 1 : -1) * ((long)var1.hashCode() + Double.doubleToLongBits(this.soup.fitDouble(Double.MIN_VALUE, Double.MAX_VALUE, (double)var2.imax(), (double)var2.imax(), (double)var2.imax())));
      this.fullMixedSeed += var3 * (long)this.rlock.imax();
      return var3;
   }

   private CNG createSoup(long seed) {
      RNG var3 = new RNG((var1 - 2043905L) * 4385677888L);
      RNG var4 = new RNG(var1 * -305L + 45858458555L);
      RNG var5 = new RNG(var1 * (var3.lmax() - var4.lmax()) + (long)"Iris World Generator".hashCode());
      RNG var6 = new RNG(var1 - var5.lmax() * -1L + 1L);
      RNG var7 = new RNG("42Iris World Generator");
      double var8 = 0.0D;
      int var10 = var3.i(1000, 10000);
      String var10000 = var3.s(4);
      char[] var11 = (var10000 + var4.s(4) + var5.s(4) + var6.s(4) + var7.s(4)).toCharArray();
      int var12 = var11.length;

      for(int var13 = 0; var13 < var12; ++var13) {
         char var15 = var11[var13];
         var8 += (double)var10 * var4.d(3.0D, 3.141592653589793D) / var5.d(10.0D, 48.92907291226281D) + 6549.0D;
         var8 *= var6.d(90.5D, 1234567.0D);
         var8 += var7.d(39.95D, 99.25D);
      }

      return NoiseStyle.STATIC.create(new RNG(4966866L * Double.doubleToLongBits((var8 * (double)var3.imax() + (double)var4.imax() + (double)var5.lmax() + (double)var6.lmax()) * (double)var7.lmax())));
   }

   @Generated
   public RNG getRlock() {
      return this.rlock;
   }

   @Generated
   public CNG getSoup() {
      return this.soup;
   }

   @Generated
   public long getSeed() {
      return this.seed;
   }

   @Generated
   public long getComplex() {
      return this.complex;
   }

   @Generated
   public long getComplexStreams() {
      return this.complexStreams;
   }

   @Generated
   public long getBasic() {
      return this.basic;
   }

   @Generated
   public long getHeight() {
      return this.height;
   }

   @Generated
   public long getComponent() {
      return this.component;
   }

   @Generated
   public long getScript() {
      return this.script;
   }

   @Generated
   public long getMantle() {
      return this.mantle;
   }

   @Generated
   public long getEntity() {
      return this.entity;
   }

   @Generated
   public long getBiome() {
      return this.biome;
   }

   @Generated
   public long getDecorator() {
      return this.decorator;
   }

   @Generated
   public long getTerrain() {
      return this.terrain;
   }

   @Generated
   public long getSpawn() {
      return this.spawn;
   }

   @Generated
   public long getJigsaw() {
      return this.jigsaw;
   }

   @Generated
   public long getCarve() {
      return this.carve;
   }

   @Generated
   public long getDeposit() {
      return this.deposit;
   }

   @Generated
   public long getPost() {
      return this.post;
   }

   @Generated
   public long getBodies() {
      return this.bodies;
   }

   @Generated
   public long getMode() {
      return this.mode;
   }

   @Generated
   public long getFullMixedSeed() {
      return this.fullMixedSeed;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof SeedManager)) {
         return false;
      } else {
         SeedManager var2 = (SeedManager)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getSeed() != var2.getSeed()) {
            return false;
         } else if (this.getComplex() != var2.getComplex()) {
            return false;
         } else if (this.getComplexStreams() != var2.getComplexStreams()) {
            return false;
         } else if (this.getBasic() != var2.getBasic()) {
            return false;
         } else if (this.getHeight() != var2.getHeight()) {
            return false;
         } else if (this.getComponent() != var2.getComponent()) {
            return false;
         } else if (this.getScript() != var2.getScript()) {
            return false;
         } else if (this.getMantle() != var2.getMantle()) {
            return false;
         } else if (this.getEntity() != var2.getEntity()) {
            return false;
         } else if (this.getBiome() != var2.getBiome()) {
            return false;
         } else if (this.getDecorator() != var2.getDecorator()) {
            return false;
         } else if (this.getTerrain() != var2.getTerrain()) {
            return false;
         } else if (this.getSpawn() != var2.getSpawn()) {
            return false;
         } else if (this.getJigsaw() != var2.getJigsaw()) {
            return false;
         } else if (this.getCarve() != var2.getCarve()) {
            return false;
         } else if (this.getDeposit() != var2.getDeposit()) {
            return false;
         } else if (this.getPost() != var2.getPost()) {
            return false;
         } else if (this.getBodies() != var2.getBodies()) {
            return false;
         } else if (this.getMode() != var2.getMode()) {
            return false;
         } else if (this.getFullMixedSeed() != var2.getFullMixedSeed()) {
            return false;
         } else {
            RNG var3 = this.getRlock();
            RNG var4 = var2.getRlock();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            CNG var5 = this.getSoup();
            CNG var6 = var2.getSoup();
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
      return var1 instanceof SeedManager;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getSeed();
      int var45 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = this.getComplex();
      var45 = var45 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = this.getComplexStreams();
      var45 = var45 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = this.getBasic();
      var45 = var45 * 59 + (int)(var9 >>> 32 ^ var9);
      long var11 = this.getHeight();
      var45 = var45 * 59 + (int)(var11 >>> 32 ^ var11);
      long var13 = this.getComponent();
      var45 = var45 * 59 + (int)(var13 >>> 32 ^ var13);
      long var15 = this.getScript();
      var45 = var45 * 59 + (int)(var15 >>> 32 ^ var15);
      long var17 = this.getMantle();
      var45 = var45 * 59 + (int)(var17 >>> 32 ^ var17);
      long var19 = this.getEntity();
      var45 = var45 * 59 + (int)(var19 >>> 32 ^ var19);
      long var21 = this.getBiome();
      var45 = var45 * 59 + (int)(var21 >>> 32 ^ var21);
      long var23 = this.getDecorator();
      var45 = var45 * 59 + (int)(var23 >>> 32 ^ var23);
      long var25 = this.getTerrain();
      var45 = var45 * 59 + (int)(var25 >>> 32 ^ var25);
      long var27 = this.getSpawn();
      var45 = var45 * 59 + (int)(var27 >>> 32 ^ var27);
      long var29 = this.getJigsaw();
      var45 = var45 * 59 + (int)(var29 >>> 32 ^ var29);
      long var31 = this.getCarve();
      var45 = var45 * 59 + (int)(var31 >>> 32 ^ var31);
      long var33 = this.getDeposit();
      var45 = var45 * 59 + (int)(var33 >>> 32 ^ var33);
      long var35 = this.getPost();
      var45 = var45 * 59 + (int)(var35 >>> 32 ^ var35);
      long var37 = this.getBodies();
      var45 = var45 * 59 + (int)(var37 >>> 32 ^ var37);
      long var39 = this.getMode();
      var45 = var45 * 59 + (int)(var39 >>> 32 ^ var39);
      long var41 = this.getFullMixedSeed();
      var45 = var45 * 59 + (int)(var41 >>> 32 ^ var41);
      RNG var43 = this.getRlock();
      var45 = var45 * 59 + (var43 == null ? 43 : var43.hashCode());
      CNG var44 = this.getSoup();
      var45 = var45 * 59 + (var44 == null ? 43 : var44.hashCode());
      return var45;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getRlock());
      return "SeedManager(rlock=" + var10000 + ", soup=" + String.valueOf(this.getSoup()) + ", seed=" + this.getSeed() + ", complex=" + this.getComplex() + ", complexStreams=" + this.getComplexStreams() + ", basic=" + this.getBasic() + ", height=" + this.getHeight() + ", component=" + this.getComponent() + ", script=" + this.getScript() + ", mantle=" + this.getMantle() + ", entity=" + this.getEntity() + ", biome=" + this.getBiome() + ", decorator=" + this.getDecorator() + ", terrain=" + this.getTerrain() + ", spawn=" + this.getSpawn() + ", jigsaw=" + this.getJigsaw() + ", carve=" + this.getCarve() + ", deposit=" + this.getDeposit() + ", post=" + this.getPost() + ", bodies=" + this.getBodies() + ", mode=" + this.getMode() + ", fullMixedSeed=" + this.getFullMixedSeed() + ")";
   }
}
