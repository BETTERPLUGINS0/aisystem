package com.volmit.iris.util.noise;

public class CloverNoise implements NoiseGenerator {
   private final CloverNoise.Noise2D n2;
   private final CloverNoise.Noise3D n3;

   public CloverNoise(long seed) {
      this.n2 = new CloverNoise.Noise2D(var1);
      this.n3 = new CloverNoise.Noise3D(var1);
   }

   public double noise(double x) {
      return this.n2.noise(var1, 0.0D);
   }

   public double noise(double x, double z) {
      return this.n2.noise(var1, var3);
   }

   public double noise(double x, double y, double z) {
      return var5 == 0.0D ? this.n2.noise(var1, var3) : this.n3.noise(var1, var3, var5);
   }

   public static class Noise2D {
      private static final long HASH_A = 25214903917L;
      private static final long HASH_C = 11L;
      private static final long HASH_M = 281474976710656L;
      private static final double POINT_SPREAD = 0.3D;
      private static final double CURL_DX = 1.0E-4D;
      private final long seed;

      public Noise2D(long seed) {
         this.seed = var1;
      }

      public Noise2D() {
         this(System.currentTimeMillis());
      }

      private long doHash(long input, long seed) {
         var1 += var3;
         if (var1 < 0L) {
            var1 += 281474976710656L;
         }

         var1 *= 25214903917L;
         var1 += 11L;
         var1 %= 281474976710656L;
         return var1;
      }

      private double hash(CloverNoise.Vector2 position) {
         long var2 = this.doHash(this.seed, (long)Math.floor(var1.getX()));
         var2 = this.doHash(var2, (long)Math.floor(var1.getY()));
         var2 = this.doHash(var2, var2 * (long)Math.floor(var1.getX() + var1.getY()));
         if (var2 < 0L) {
            var2 += 281474976710656L;
         }

         return (double)var2 / 2.81474976710656E14D;
      }

      private CloverNoise.Vector2 offset(CloverNoise.Vector2 position) {
         double var2 = this.hash(var1);
         double var4 = Math.floor(var2 * 50.0D + 1.0D) / 100.0D;
         CloverNoise.Vector2 var6 = (new CloverNoise.Vector2(Math.sin(var2 * 3.141592653589793D * 100.0D), Math.cos(var2 * 3.141592653589793D * 100.0D))).mult(var4).add(0.5D);
         return var1.add(var6.mult(0.6D)).add(0.2D);
      }

      public double noise(CloverNoise.Vector2 p) {
         CloverNoise.Vector2 var2 = var1.floor();
         CloverNoise.Vector2 var3 = this.offset(var2);
         CloverNoise.Vector2 var4 = this.offset(var2.add(0.0D, -1.0D));
         CloverNoise.Vector2 var5 = this.offset(var2.add(0.0D, 1.0D));
         CloverNoise.Vector2 var6 = this.offset(var2.add(-1.0D, 0.0D));
         CloverNoise.Vector2 var7 = this.offset(var2.add(1.0D, 0.0D));
         CloverNoise.Vector2 var8 = var1.sub(var3).yx();
         CloverNoise.Vector2 var9 = var8.mult(var3);
         double var10 = var9.sub(var8.mult(var6)).ymx();
         double var12 = var9.sub(var8.mult(var7)).ymx();
         CloverNoise.Vector2 var14;
         CloverNoise.Vector2 var15;
         CloverNoise.Vector2 var16;
         double var17;
         if (var10 < 0.0D && var1.x < var3.x || var12 > 0.0D && var1.x > var3.x) {
            var17 = var9.sub(var8.mult(var5)).ymx();
            if (var17 > 0.0D) {
               var14 = var5;
               var15 = var6;
               var16 = new CloverNoise.Vector2(-1.0D, 1.0D);
            } else {
               var14 = var7;
               var15 = var5;
               var16 = new CloverNoise.Vector2(1.0D, 1.0D);
            }
         } else {
            var17 = var9.sub(var8.mult(var4)).ymx();
            if (var17 > 0.0D) {
               var14 = var4;
               var15 = var7;
               var16 = new CloverNoise.Vector2(1.0D, -1.0D);
            } else {
               var14 = var6;
               var15 = var4;
               var16 = new CloverNoise.Vector2(-1.0D, -1.0D);
            }
         }

         var16 = this.offset(var2.add(var16));
         CloverNoise.Vector2 var41 = var14;
         CloverNoise.Vector2 var18 = var15;
         CloverNoise.Vector2 var19 = var16;
         CloverNoise.Vector2 var20 = var14.sub(var15);
         CloverNoise.Vector2 var21 = var3.sub(var16);
         CloverNoise.Vector2 var22;
         if (var20.x * var20.x + var20.y * var20.y < var21.x * var21.x + var21.y * var21.y) {
            var22 = var1.sub(var14);
            if (var22.x * var20.y - var22.y * var20.x > 0.0D) {
               var19 = var3;
            }
         } else {
            var22 = var1.sub(var3);
            if (var22.x * var21.y - var22.y * var21.x > 0.0D) {
               var41 = var3;
            } else {
               var18 = var3;
            }
         }

         var22 = var18.sub(var41);
         CloverNoise.Vector2 var23 = var19.sub(var41);
         CloverNoise.Vector2 var24 = var1.sub(var41);
         double var25 = 1.0D / (var22.x * var23.y - var23.x * var22.y);
         double var27 = (var24.x * var23.y - var23.x * var24.y) * var25;
         double var29 = (var22.x * var24.y - var24.x * var22.y) * var25;
         double var31 = 1.0D - var27 - var29;
         var27 = var27 * var27 * var27;
         var29 = var29 * var29 * var29;
         var31 = var31 * var31 * var31;
         double var33 = 1.0D / (var31 + var27 + var29);
         var27 *= var33;
         var29 *= var33;
         var31 *= var33;
         double var35 = this.hash(var41.floor());
         double var37 = this.hash(var18.floor());
         double var39 = this.hash(var19.floor());
         return var31 * var35 + var27 * var37 + var29 * var39;
      }

      public double noise(double x, double y) {
         return this.noise(new CloverNoise.Vector2(var1, var3));
      }

      public double fractalNoise(CloverNoise.Vector2 p, int iterations) {
         double var3 = 0.0D;
         double var5 = 1.0D;
         double var7 = 0.0D;

         for(int var9 = 0; var9 < var2; ++var9) {
            var3 += this.noise(var1.mult(1.0D / var5)) * var5;
            var7 += var5;
            var5 *= 0.4D;
         }

         return var3 / var7;
      }

      public double fractalNoise(double x, double y, int iterations) {
         return this.fractalNoise(new CloverNoise.Vector2(var1, var3), var5);
      }

      public CloverNoise.Vector2 curlNoise(CloverNoise.Vector2 p) {
         double var2 = this.noise(var1);
         double var4 = this.noise(var1.add(1.0E-4D, 0.0D));
         double var6 = this.noise(var1.add(0.0D, 1.0E-4D));
         return (new CloverNoise.Vector2(var2 - var4, var2 - var6)).normalize();
      }

      public CloverNoise.Vector2 curlNoise(double x, double y) {
         return this.curlNoise(new CloverNoise.Vector2(var1, var3));
      }

      public CloverNoise.Vector2 fractalCurlNoise(CloverNoise.Vector2 p, int iterations) {
         double var3 = this.fractalNoise(var1, var2);
         double var5 = this.fractalNoise(var1.add(1.0E-4D, 0.0D), var2);
         double var7 = this.fractalNoise(var1.add(0.0D, 1.0E-4D), var2);
         return (new CloverNoise.Vector2(var3 - var5, var3 - var7)).normalize();
      }

      public CloverNoise.Vector2 fractalCurlNoise(double x, double y, int iterations) {
         return this.fractalCurlNoise(new CloverNoise.Vector2(var1, var3), var5);
      }

      public double frostNoise(CloverNoise.Vector2 p) {
         CloverNoise.Vector2 var2 = this.fractalCurlNoise(var1, 3).mult(this.fractalNoise(var1, 2) * 0.4D + 0.3D);
         CloverNoise.Vector2 var3 = var1.add(var2);
         CloverNoise.Vector2 var4 = this.fractalCurlNoise(var3, 4).mult(this.fractalNoise(var3, 3) * 0.1D + 0.05D);
         CloverNoise.Vector2 var5 = var3.add(var4);
         return this.fractalNoise(var5, 5) - this.fractalNoise(var3, 3) * 0.5D + this.fractalNoise(var1, 2) * 0.3D;
      }

      public double frostNoise(double x, double y) {
         return this.frostNoise(new CloverNoise.Vector2(var1, var3));
      }

      public double marbleNoise(CloverNoise.Vector2 p) {
         CloverNoise.Vector2 var2 = var1.mult(0.6D);
         double var3 = Math.max(1.0D - Math.abs(this.fractalNoise(var2.add(100.0D), 4) - this.fractalNoise(var2.add(200.0D), 3)) * 3.0D, 0.0D);
         var3 = var3 * var3 * var3;
         var3 *= this.fractalNoise(var2.add(300.0D), 2) * 0.3D;
         CloverNoise.Vector2 var5 = this.fractalCurlNoise(var1.add(400.0D), 3).mult(this.fractalNoise(var1.add(500.0D), 2) * 0.05D);
         CloverNoise.Vector2 var6 = var1.mult(1.2D);
         double var7 = Math.max(1.0D - Math.abs(this.fractalNoise(var6.add(var5).add(600.0D), 4) - this.fractalNoise(var6.add(var5).add(700.0D), 3)) * 2.0D, 0.0D);
         var7 = var7 * var7 * var7;
         var7 *= this.fractalNoise(var6.add(800.0D), 2) * 0.5D;
         double var9 = 1.0D - this.fractalNoise(var1.add(900.0D), 5);
         var9 = 1.0D - var9 * var9 * var9;
         return Math.min(Math.max(var9 - var3 - var7, 0.0D), 1.0D);
      }

      public double marbleNoise(double x, double y) {
         return this.marbleNoise(new CloverNoise.Vector2(var1, var3));
      }
   }

   public static class Noise3D {
      private static final long HASH_A = 25214903917L;
      private static final long HASH_C = 11L;
      private static final long HASH_M = 281474976710656L;
      private static final double POINT_SPREAD = 0.2D;
      private static final double CURL_DX = 1.0E-4D;
      private final long seed;

      public Noise3D(long seed) {
         this.seed = var1;
      }

      public Noise3D() {
         this(System.currentTimeMillis());
      }

      private long doHash(long input, long seed) {
         var1 += var3;
         if (var1 < 0L) {
            var1 += 281474976710656L;
         }

         var1 *= 25214903917L;
         var1 += 11L;
         var1 %= 281474976710656L;
         return var1;
      }

      private double hash(CloverNoise.Vector3 position) {
         long var2 = this.doHash(this.seed, (long)Math.floor(var1.getX()));
         var2 = this.doHash(var2, (long)Math.floor(var1.getY()));
         var2 = this.doHash(var2, (long)Math.floor(var1.getZ()));
         var2 = this.doHash(var2, var2 * (long)Math.floor(var1.getX() + var1.getY() + var1.getZ()));
         if (var2 < 0L) {
            var2 += 281474976710656L;
         }

         return (double)var2 / 2.81474976710656E14D;
      }

      private CloverNoise.Vector3 offset(CloverNoise.Vector3 position) {
         double var2 = this.hash(var1);
         double var4 = var2 * 3.141592653589793D * 2000.0D;
         double var6 = ((Math.floor(var2 * 1000.0D) + 0.5D) / 100.0D % 1.0D - 0.5D) * 3.141592653589793D / 2.0D;
         double var8 = Math.floor(var2 * 10.0D + 1.0D) / 10.0D;
         CloverNoise.Vector3 var10 = (new CloverNoise.Vector3(Math.sin(var4) * Math.cos(var6), Math.sin(var6), Math.cos(var4) * Math.cos(var6))).mult(var8).add(0.5D);
         return var1.add(var10.mult(0.4D).add(0.3D));
      }

      private boolean boundary(CloverNoise.Vector3 p, CloverNoise.Vector3 c_00, CloverNoise.Vector3 c_10, CloverNoise.Vector3 c_20, CloverNoise.Vector3 c_01, CloverNoise.Vector3 c_11, CloverNoise.Vector3 c_21, CloverNoise.Vector3 c_02, CloverNoise.Vector3 c_12, CloverNoise.Vector3 c_22) {
         CloverNoise.Vector2 var11 = var1.yx().sub(var6.yx());
         CloverNoise.Vector2 var12 = var11.mult(var6.xy());
         double var13 = var12.sub(var11.mult(var5.xy())).ymx();
         double var15 = var12.sub(var11.mult(var7.xy())).ymx();
         CloverNoise.Vector3 var17;
         CloverNoise.Vector3 var18;
         CloverNoise.Vector3 var19;
         CloverNoise.Vector3 var20;
         double var21;
         if (var13 < 0.0D && var1.x < var6.x || var15 > 0.0D && var1.x >= var6.x) {
            var21 = var12.sub(var11.mult(var9.xy())).ymx();
            if (var21 > 0.0D) {
               var17 = var5;
               var18 = var8;
               var19 = var9;
               var20 = var6;
            } else {
               var17 = var6;
               var18 = var9;
               var19 = var10;
               var20 = var7;
            }
         } else {
            var21 = var12.sub(var11.mult(var3.xy())).ymx();
            if (var21 > 0.0D) {
               var17 = var3;
               var18 = var6;
               var19 = var7;
               var20 = var4;
            } else {
               var17 = var2;
               var18 = var5;
               var19 = var6;
               var20 = var3;
            }
         }

         CloverNoise.Vector3 var23 = var20;
         CloverNoise.Vector3 var24 = var17.sub(var19);
         CloverNoise.Vector3 var25 = var1.sub(var17);
         if (var25.x * var24.y - var25.y * var24.x > 0.0D) {
            var23 = var18;
         }

         CloverNoise.Vector2 var26 = var19.xy().sub(var17.xy());
         CloverNoise.Vector2 var27 = var23.xy().sub(var17.xy());
         CloverNoise.Vector2 var28 = var1.xy().sub(var17.xy());
         double var29 = 1.0D / (var26.x * var27.y - var27.x * var26.y);
         double var31 = (var28.x * var27.y - var27.x * var28.y) * var29;
         double var33 = (var26.x * var28.y - var28.x * var26.y) * var29;
         double var35 = 1.0D - var31 - var33;
         return var1.z < var35 * var17.z + var31 * var19.z + var33 * var23.z;
      }

      public double noise(CloverNoise.Vector3 p) {
         CloverNoise.Vector3 var2 = var1.floor();
         CloverNoise.Vector3 var3 = this.offset(var2);
         CloverNoise.Vector3 var4 = this.offset(var2.add(0.0D, -1.0D, -1.0D));
         CloverNoise.Vector3 var5 = this.offset(var2.add(-1.0D, 0.0D, -1.0D));
         CloverNoise.Vector3 var6 = this.offset(var2.add(0.0D, 0.0D, -1.0D));
         CloverNoise.Vector3 var7 = this.offset(var2.add(1.0D, 0.0D, -1.0D));
         CloverNoise.Vector3 var8 = this.offset(var2.add(0.0D, 1.0D, -1.0D));
         CloverNoise.Vector3 var9 = this.offset(var2.add(-1.0D, -1.0D, 0.0D));
         CloverNoise.Vector3 var10 = this.offset(var2.add(0.0D, -1.0D, 0.0D));
         CloverNoise.Vector3 var11 = this.offset(var2.add(1.0D, -1.0D, 0.0D));
         CloverNoise.Vector3 var12 = this.offset(var2.add(-1.0D, 0.0D, 0.0D));
         CloverNoise.Vector3 var13 = this.offset(var2.add(1.0D, 0.0D, 0.0D));
         CloverNoise.Vector3 var14 = this.offset(var2.add(-1.0D, 1.0D, 0.0D));
         CloverNoise.Vector3 var15 = this.offset(var2.add(0.0D, 1.0D, 0.0D));
         CloverNoise.Vector3 var16 = this.offset(var2.add(1.0D, 1.0D, 0.0D));
         CloverNoise.Vector3 var17 = this.offset(var2.add(0.0D, -1.0D, 1.0D));
         CloverNoise.Vector3 var18 = this.offset(var2.add(-1.0D, 0.0D, 1.0D));
         CloverNoise.Vector3 var19 = this.offset(var2.add(0.0D, 0.0D, 1.0D));
         CloverNoise.Vector3 var20 = this.offset(var2.add(1.0D, 0.0D, 1.0D));
         CloverNoise.Vector3 var21 = this.offset(var2.add(0.0D, 1.0D, 1.0D));
         boolean var22 = this.boundary(var1.yzx(), var4.yzx(), var6.yzx(), var8.yzx(), var10.yzx(), var3.yzx(), var15.yzx(), var17.yzx(), var19.yzx(), var21.yzx());
         boolean var23 = this.boundary(var1.xzy(), var5.xzy(), var6.xzy(), var7.xzy(), var12.xzy(), var3.xzy(), var13.xzy(), var18.xzy(), var19.xzy(), var20.xzy());
         boolean var24 = this.boundary(var1, var9, var10, var11, var12, var3, var13, var14, var15, var16);
         CloverNoise.Vector3 var25;
         CloverNoise.Vector3 var26;
         CloverNoise.Vector3 var27;
         CloverNoise.Vector3 var28;
         CloverNoise.Vector3 var29;
         CloverNoise.Vector3 var30;
         CloverNoise.Vector3 var31;
         CloverNoise.Vector3 var32;
         if (var22) {
            if (var23) {
               if (var24) {
                  var25 = this.offset(var2.add(-1.0D, -1.0D, -1.0D));
                  var26 = var9;
                  var27 = var5;
                  var28 = var12;
                  var29 = var4;
                  var30 = var10;
                  var31 = var6;
                  var32 = var3;
               } else {
                  var25 = var9;
                  var26 = this.offset(var2.add(-1.0D, -1.0D, 1.0D));
                  var27 = var12;
                  var28 = var18;
                  var29 = var10;
                  var30 = var17;
                  var31 = var3;
                  var32 = var19;
               }
            } else if (var24) {
               var25 = var5;
               var26 = var12;
               var27 = this.offset(var2.add(-1.0D, 1.0D, -1.0D));
               var28 = var14;
               var29 = var6;
               var30 = var3;
               var31 = var8;
               var32 = var15;
            } else {
               var25 = var12;
               var26 = var18;
               var27 = var14;
               var28 = this.offset(var2.add(-1.0D, 1.0D, 1.0D));
               var29 = var3;
               var30 = var19;
               var31 = var15;
               var32 = var21;
            }
         } else if (var23) {
            if (var24) {
               var25 = var4;
               var26 = var10;
               var27 = var6;
               var28 = var3;
               var29 = this.offset(var2.add(1.0D, -1.0D, -1.0D));
               var30 = var11;
               var31 = var7;
               var32 = var13;
            } else {
               var25 = var10;
               var26 = var17;
               var27 = var3;
               var28 = var19;
               var29 = var11;
               var30 = this.offset(var2.add(1.0D, -1.0D, 1.0D));
               var31 = var13;
               var32 = var20;
            }
         } else if (var24) {
            var25 = var6;
            var26 = var3;
            var27 = var8;
            var28 = var15;
            var29 = var7;
            var30 = var13;
            var31 = this.offset(var2.add(1.0D, 1.0D, -1.0D));
            var32 = var16;
         } else {
            var25 = var3;
            var26 = var19;
            var27 = var15;
            var28 = var21;
            var29 = var13;
            var30 = var20;
            var31 = var16;
            var32 = this.offset(var2.add(1.0D, 1.0D, 1.0D));
         }

         CloverNoise.Vector3 var33 = var25.sub(var32);
         CloverNoise.Vector3 var34 = var1.sub(var25);
         double var35 = var33.cross(var26.sub(var32)).mult(var34).xpypz();
         double var37 = var33.cross(var27.sub(var32)).mult(var34).xpypz();
         double var39 = var33.cross(var28.sub(var32)).mult(var34).xpypz();
         double var41 = var33.cross(var29.sub(var32)).mult(var34).xpypz();
         double var43 = var33.cross(var30.sub(var32)).mult(var34).xpypz();
         double var45 = var33.cross(var31.sub(var32)).mult(var34).xpypz();
         CloverNoise.Vector3 var49;
         CloverNoise.Vector3 var50;
         if (var35 > 0.0D && var39 <= 0.0D) {
            var49 = var26;
            var50 = var28;
         } else if (var39 > 0.0D && var37 <= 0.0D) {
            var49 = var28;
            var50 = var27;
         } else if (var37 > 0.0D && var45 <= 0.0D) {
            var49 = var27;
            var50 = var31;
         } else if (var45 > 0.0D && var41 <= 0.0D) {
            var49 = var31;
            var50 = var29;
         } else if (var41 > 0.0D && var43 <= 0.0D) {
            var49 = var29;
            var50 = var30;
         } else {
            var49 = var30;
            var50 = var26;
         }

         CloverNoise.Vector3 var51 = var1.sub(var25);
         CloverNoise.Vector3 var52 = var1.sub(var32);
         CloverNoise.Vector3 var53 = var32.sub(var25);
         CloverNoise.Vector3 var54 = var49.sub(var25);
         CloverNoise.Vector3 var55 = var50.sub(var25);
         CloverNoise.Vector3 var56 = var49.sub(var32);
         CloverNoise.Vector3 var57 = var50.sub(var32);
         double var58 = var52.mult(var57.cross(var56)).xpypz();
         double var60 = var51.mult(var54.cross(var55)).xpypz();
         double var62 = var51.mult(var55.cross(var53)).xpypz();
         double var64 = var51.mult(var53.cross(var54)).xpypz();
         double var66 = 1.0D / var53.mult(var54.cross(var55)).xpypz();
         double var68 = var58 * var66;
         double var70 = var60 * var66;
         double var72 = var62 * var66;
         double var74 = var64 * var66;
         double var76 = var74 * var74 * var74 * (1.0D - var68 * var70 * var72);
         double var78 = var68 * var68 * var68 * (1.0D - var74 * var70 * var72);
         double var80 = var70 * var70 * var70 * (1.0D - var68 * var74 * var72);
         double var82 = var72 * var72 * var72 * (1.0D - var68 * var70 * var74);
         double var84 = var76 + var78 + var80 + var82;
         var76 /= var84;
         var78 /= var84;
         var80 /= var84;
         var82 /= var84;
         double var86 = this.hash(var25.floor());
         double var88 = this.hash(var32.floor());
         double var90 = this.hash(var49.floor());
         double var92 = this.hash(var50.floor());
         return var78 * var86 + var80 * var88 + var82 * var90 + var76 * var92;
      }

      public double noise(double x, double y, double z) {
         return this.noise(new CloverNoise.Vector3(var1, var3, var5));
      }

      public double fractalNoise(CloverNoise.Vector3 p, int iterations) {
         double var3 = 0.0D;
         double var5 = 1.0D;
         double var7 = 0.0D;

         for(int var9 = 0; var9 < var2; ++var9) {
            var3 += this.noise(var1.mult(1.0D / var5)) * var5;
            var7 += var5;
            var5 *= 0.4D;
         }

         return var3 / var7;
      }

      public double fractalNoise(double x, double y, double z, int iterations) {
         return this.fractalNoise(new CloverNoise.Vector3(var1, var3, var5), var7);
      }

      public CloverNoise.Vector3 curlNoise(CloverNoise.Vector3 p) {
         double var2 = this.noise(var1);
         double var4 = this.noise(var1.add(1.0E-4D, 0.0D, 0.0D));
         double var6 = this.noise(var1.add(0.0D, 1.0E-4D, 0.0D));
         double var8 = this.noise(var1.add(0.0D, 0.0D, 1.0E-4D));
         return (new CloverNoise.Vector3(var2 - var4, var2 - var6, var2 - var8)).normalize();
      }

      public CloverNoise.Vector3 curlNoise(double x, double y, double z) {
         return this.curlNoise(new CloverNoise.Vector3(var1, var3, var5));
      }

      public CloverNoise.Vector3 fractalCurlNoise(CloverNoise.Vector3 p, int iterations) {
         double var3 = this.fractalNoise(var1, var2);
         double var5 = this.fractalNoise(var1.add(1.0E-4D, 0.0D, 0.0D), var2);
         double var7 = this.fractalNoise(var1.add(0.0D, 1.0E-4D, 0.0D), var2);
         double var9 = this.fractalNoise(var1.add(0.0D, 0.0D, 1.0E-4D), var2);
         return (new CloverNoise.Vector3(var3 - var5, var3 - var7, var3 - var9)).normalize();
      }

      public CloverNoise.Vector3 fractalCurlNoise(double x, double y, double z, int iterations) {
         return this.fractalCurlNoise(new CloverNoise.Vector3(var1, var3, var5), var7);
      }

      public double frostNoise(CloverNoise.Vector3 p) {
         CloverNoise.Vector3 var2 = this.fractalCurlNoise(var1, 3).mult(this.fractalNoise(var1, 2) * 0.4D + 0.3D);
         CloverNoise.Vector3 var3 = var1.add(var2);
         CloverNoise.Vector3 var4 = this.fractalCurlNoise(var3, 4).mult(this.fractalNoise(var3, 3) * 0.1D + 0.05D);
         CloverNoise.Vector3 var5 = var3.add(var4);
         return this.fractalNoise(var5, 5) - this.fractalNoise(var3, 3) * 0.5D + this.fractalNoise(var1, 2) * 0.3D;
      }

      public double frostNoise(double x, double y, double z) {
         return this.frostNoise(new CloverNoise.Vector3(var1, var3, var5));
      }

      public double marbleNoise(CloverNoise.Vector3 p) {
         CloverNoise.Vector3 var2 = var1.mult(0.6D);
         double var3 = Math.max(1.0D - Math.abs(this.fractalNoise(var2.add(100.0D), 4) - this.fractalNoise(var2.add(200.0D), 3)) * 3.0D, 0.0D);
         var3 = var3 * var3 * var3;
         var3 *= this.fractalNoise(var2.add(300.0D), 2) * 0.3D;
         CloverNoise.Vector3 var5 = this.fractalCurlNoise(var1.add(400.0D), 3).mult(this.fractalNoise(var1.add(500.0D), 2) * 0.05D);
         CloverNoise.Vector3 var6 = var1.mult(1.2D);
         double var7 = Math.max(1.0D - Math.abs(this.fractalNoise(var6.add(var5).add(600.0D), 4) - this.fractalNoise(var6.add(var5).add(700.0D), 3)) * 2.0D, 0.0D);
         var7 = var7 * var7 * var7;
         var7 *= this.fractalNoise(var6.add(800.0D), 2) * 0.5D;
         double var9 = 1.0D - this.fractalNoise(var1.add(900.0D), 5);
         var9 = 1.0D - var9 * var9 * var9;
         return Math.min(Math.max(var9 - var3 - var7, 0.0D), 1.0D);
      }

      public double marbleNoise(double x, double y, double z) {
         return this.marbleNoise(new CloverNoise.Vector3(var1, var3, var5));
      }
   }

   public static class Vector3 {
      double x;
      double y;
      double z;

      public Vector3(double x, double y, double z) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
      }

      public Vector3() {
         this(0.0D, 0.0D, 0.0D);
      }

      public double getX() {
         return this.x;
      }

      public void setX(double x) {
         this.x = var1;
      }

      public double getY() {
         return this.y;
      }

      public void setY(double y) {
         this.y = var1;
      }

      public double getZ() {
         return this.z;
      }

      public void setZ(double z) {
         this.z = var1;
      }

      public CloverNoise.Vector2 xy() {
         return new CloverNoise.Vector2(this.x, this.y);
      }

      public CloverNoise.Vector2 yx() {
         return new CloverNoise.Vector2(this.y, this.x);
      }

      public CloverNoise.Vector3 yzx() {
         return new CloverNoise.Vector3(this.y, this.z, this.x);
      }

      public CloverNoise.Vector3 xzy() {
         return new CloverNoise.Vector3(this.x, this.z, this.y);
      }

      public double xpypz() {
         return this.x + this.y + this.z;
      }

      public CloverNoise.Vector3 negate() {
         return new CloverNoise.Vector3(-this.x, -this.y, -this.z);
      }

      public CloverNoise.Vector3 floor() {
         return new CloverNoise.Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
      }

      public CloverNoise.Vector3 normalize() {
         double var1 = this.x * this.x + this.y * this.y + this.z * this.z;
         double var3 = Math.sqrt(var1);
         return new CloverNoise.Vector3(this.x / var3, this.y / var3, this.z / var3);
      }

      public CloverNoise.Vector3 add(CloverNoise.Vector3 a) {
         return new CloverNoise.Vector3(this.x + var1.x, this.y + var1.y, this.z + var1.z);
      }

      public CloverNoise.Vector3 add(double xa, double ya, double za) {
         return this.add(new CloverNoise.Vector3(var1, var3, var5));
      }

      public CloverNoise.Vector3 add(double a) {
         return this.add(var1, var1, var1);
      }

      public CloverNoise.Vector3 sub(CloverNoise.Vector3 s) {
         return this.add(var1.negate());
      }

      public CloverNoise.Vector3 sub(double xs, double ys, double zs) {
         return this.sub(new CloverNoise.Vector3(var1, var3, var5));
      }

      public CloverNoise.Vector3 sub(double s) {
         return this.sub(var1, var1, var1);
      }

      public CloverNoise.Vector3 mult(CloverNoise.Vector3 m) {
         return new CloverNoise.Vector3(this.x * var1.x, this.y * var1.y, this.z * var1.z);
      }

      public CloverNoise.Vector3 mult(double mx, double my, double mz) {
         return this.mult(new CloverNoise.Vector3(var1, var3, var5));
      }

      public CloverNoise.Vector3 mult(double m) {
         return this.mult(var1, var1, var1);
      }

      public CloverNoise.Vector3 cross(CloverNoise.Vector3 c) {
         return new CloverNoise.Vector3(this.y * var1.z - this.z * var1.y, this.z * var1.x - this.x * var1.z, this.x * var1.y - this.y * var1.x);
      }
   }

   public static class Vector2 {
      private double x;
      private double y;

      public Vector2(double x, double y) {
         this.x = var1;
         this.y = var3;
      }

      public Vector2() {
         this(0.0D, 0.0D);
      }

      public double getX() {
         return this.x;
      }

      public void setX(double x) {
         this.x = var1;
      }

      public double getY() {
         return this.y;
      }

      public void setY(double y) {
         this.y = var1;
      }

      public double ymx() {
         return this.y - this.x;
      }

      public CloverNoise.Vector2 yx() {
         return new CloverNoise.Vector2(this.y, this.x);
      }

      public CloverNoise.Vector2 negate() {
         return new CloverNoise.Vector2(-this.x, -this.y);
      }

      public CloverNoise.Vector2 floor() {
         return new CloverNoise.Vector2(Math.floor(this.x), Math.floor(this.y));
      }

      public CloverNoise.Vector2 normalize() {
         double var1 = this.x * this.x + this.y * this.y;
         double var3 = Math.sqrt(var1);
         return new CloverNoise.Vector2(this.x / var3, this.y / var3);
      }

      public CloverNoise.Vector2 add(CloverNoise.Vector2 a) {
         return new CloverNoise.Vector2(this.x + var1.x, this.y + var1.y);
      }

      public CloverNoise.Vector2 add(double xa, double ya) {
         return this.add(new CloverNoise.Vector2(var1, var3));
      }

      public CloverNoise.Vector2 add(double a) {
         return this.add(var1, var1);
      }

      public CloverNoise.Vector2 sub(CloverNoise.Vector2 s) {
         return this.add(var1.negate());
      }

      public CloverNoise.Vector2 sub(double xs, double ys) {
         return this.sub(new CloverNoise.Vector2(var1, var3));
      }

      public CloverNoise.Vector2 sub(double s) {
         return this.sub(var1, var1);
      }

      public CloverNoise.Vector2 mult(CloverNoise.Vector2 m) {
         return new CloverNoise.Vector2(this.x * var1.x, this.y * var1.y);
      }

      public CloverNoise.Vector2 mult(double xm, double ym) {
         return this.mult(new CloverNoise.Vector2(var1, var3));
      }

      public CloverNoise.Vector2 mult(double m) {
         return this.mult(var1, var1);
      }
   }
}
