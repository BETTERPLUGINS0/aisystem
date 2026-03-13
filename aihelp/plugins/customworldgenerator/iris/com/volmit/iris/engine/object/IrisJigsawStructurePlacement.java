package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;
import lombok.Generated;

@Snippet("jigsaw-structure-placement")
@Desc("Represents a jigsaw structure placer")
public class IrisJigsawStructurePlacement implements IRare {
   @RegistryListResource(IrisJigsawStructure.class)
   @Required
   @Desc("The structure to place")
   private String structure;
   @Required
   @Desc("The 1 in X chance rarity applies when generating multiple structures at once")
   private int rarity = 100;
   @Required
   @DependsOn({"spacing", "separation"})
   @Desc("The salt to use when generating the structure (to differentiate structures)")
   @MinNumber(-9.223372036854776E18D)
   @MaxNumber(9.223372036854776E18D)
   private long salt = 0L;
   @Required
   @MinNumber(0.0D)
   @DependsOn({"salt", "separation"})
   @Desc("Average distance in chunks between two neighboring generation attempts")
   private int spacing = -1;
   @Required
   @MinNumber(0.0D)
   @DependsOn({"salt", "spacing"})
   @Desc("Minimum distance in chunks between two neighboring generation attempts\nThe maximum distance of two neighboring generation attempts is 2*spacing - separation")
   private int separation = -1;
   @Desc("The method used to spread the structure")
   private IrisJigsawStructurePlacement.SpreadType spreadType;
   @DependsOn({"spreadType"})
   @Desc("The noise style to use when spreadType is set to 'NOISE'\nThis ignores the spacing and separation parameters")
   private IrisGeneratorStyle style;
   @DependsOn({"spreadType", "style"})
   @Desc("Threshold for noise style")
   private double threshold;
   @ArrayType(
      type = IrisJigsawMinDistance.class
   )
   @Desc("List of minimum distances to check for")
   private KList<IrisJigsawMinDistance> minDistances;

   public KMap<String, Integer> collectMinDistances() {
      KMap var1 = new KMap();
      Iterator var2 = this.minDistances.iterator();

      while(var2.hasNext()) {
         IrisJigsawMinDistance var3 = (IrisJigsawMinDistance)var2.next();
         var1.compute(var3.getStructure(), (var2x, var3x) -> {
            return var3x != null ? Math.min(this.toChunks(var3.getDistance()), var3x) : this.toChunks(var3.getDistance());
         });
      }

      return var1;
   }

   private int toChunks(int blocks) {
      return (int)Math.ceil((double)var1 / 16.0D);
   }

   private void calculateMissing(double divisor, long seed) {
      if (this.salt == 0L || this.separation <= 0 || this.spacing <= 0) {
         var3 *= (long)this.structure.hashCode() * (long)this.rarity;
         if (this.salt == 0L) {
            this.salt = (new RNG(var3)).l(-2147483648L, 2147483647L);
         }

         if (this.separation == -1 || this.spacing == -1) {
            this.separation = (int)Math.round((double)this.rarity / var1);
            this.spacing = (new RNG(var3)).i(this.separation, this.separation * 2);
         }

      }
   }

   @ChunkCoordinates
   public boolean shouldPlace(IrisData data, double divisor, long seed, int x, int z) {
      this.calculateMissing(var2, var4);
      if (this.spreadType != IrisJigsawStructurePlacement.SpreadType.NOISE) {
         return this.shouldPlaceSpread(var4, var6, var7);
      } else {
         return this.style.create(new RNG(var4 + this.salt), var1).noise((double)var6, (double)var7) > this.threshold;
      }
   }

   private boolean shouldPlaceSpread(long seed, int x, int z) {
      if (this.separation > this.spacing) {
         this.separation = this.spacing;
         Iris.warn("JigsawStructurePlacement: separation must be less than or equal to spacing");
      }

      int var5 = Math.floorDiv(var3, this.spacing);
      int var6 = Math.floorDiv(var4, this.spacing);
      RNG var7 = new RNG((long)var5 * 341873128712L + (long)var6 * 132897987541L + var1 + this.salt);
      int var8 = this.spacing - this.separation;
      int var9 = this.spreadType.apply(var7, var8);
      int var10 = this.spreadType.apply(var7, var8);
      return var5 * this.spacing + var9 == var3 && var6 * this.spacing + var10 == var4;
   }

   @Generated
   public IrisJigsawStructurePlacement() {
      this.spreadType = IrisJigsawStructurePlacement.SpreadType.LINEAR;
      this.style = new IrisGeneratorStyle();
      this.threshold = 0.5D;
      this.minDistances = new KList();
   }

   @Generated
   public IrisJigsawStructurePlacement(final String structure, final int rarity, final long salt, final int spacing, final int separation, final IrisJigsawStructurePlacement.SpreadType spreadType, final IrisGeneratorStyle style, final double threshold, final KList<IrisJigsawMinDistance> minDistances) {
      this.spreadType = IrisJigsawStructurePlacement.SpreadType.LINEAR;
      this.style = new IrisGeneratorStyle();
      this.threshold = 0.5D;
      this.minDistances = new KList();
      this.structure = var1;
      this.rarity = var2;
      this.salt = var3;
      this.spacing = var5;
      this.separation = var6;
      this.spreadType = var7;
      this.style = var8;
      this.threshold = var9;
      this.minDistances = var11;
   }

   @Generated
   public String getStructure() {
      return this.structure;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public long getSalt() {
      return this.salt;
   }

   @Generated
   public int getSpacing() {
      return this.spacing;
   }

   @Generated
   public int getSeparation() {
      return this.separation;
   }

   @Generated
   public IrisJigsawStructurePlacement.SpreadType getSpreadType() {
      return this.spreadType;
   }

   @Generated
   public IrisGeneratorStyle getStyle() {
      return this.style;
   }

   @Generated
   public double getThreshold() {
      return this.threshold;
   }

   @Generated
   public KList<IrisJigsawMinDistance> getMinDistances() {
      return this.minDistances;
   }

   @Generated
   public IrisJigsawStructurePlacement setStructure(final String structure) {
      this.structure = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructurePlacement setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructurePlacement setSalt(final long salt) {
      this.salt = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructurePlacement setSpacing(final int spacing) {
      this.spacing = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructurePlacement setSeparation(final int separation) {
      this.separation = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructurePlacement setSpreadType(final IrisJigsawStructurePlacement.SpreadType spreadType) {
      this.spreadType = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructurePlacement setStyle(final IrisGeneratorStyle style) {
      this.style = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructurePlacement setThreshold(final double threshold) {
      this.threshold = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructurePlacement setMinDistances(final KList<IrisJigsawMinDistance> minDistances) {
      this.minDistances = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = this.getStructure();
      return "IrisJigsawStructurePlacement(structure=" + var10000 + ", rarity=" + this.getRarity() + ", salt=" + this.getSalt() + ", spacing=" + this.getSpacing() + ", separation=" + this.getSeparation() + ", spreadType=" + String.valueOf(this.getSpreadType()) + ", style=" + String.valueOf(this.getStyle()) + ", threshold=" + this.getThreshold() + ", minDistances=" + String.valueOf(this.getMinDistances()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisJigsawStructurePlacement)) {
         return false;
      } else {
         IrisJigsawStructurePlacement var2 = (IrisJigsawStructurePlacement)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (this.getSalt() != var2.getSalt()) {
            return false;
         } else if (this.getSpacing() != var2.getSpacing()) {
            return false;
         } else if (this.getSeparation() != var2.getSeparation()) {
            return false;
         } else if (Double.compare(this.getThreshold(), var2.getThreshold()) != 0) {
            return false;
         } else {
            label71: {
               String var3 = this.getStructure();
               String var4 = var2.getStructure();
               if (var3 == null) {
                  if (var4 == null) {
                     break label71;
                  }
               } else if (var3.equals(var4)) {
                  break label71;
               }

               return false;
            }

            label64: {
               IrisJigsawStructurePlacement.SpreadType var5 = this.getSpreadType();
               IrisJigsawStructurePlacement.SpreadType var6 = var2.getSpreadType();
               if (var5 == null) {
                  if (var6 == null) {
                     break label64;
                  }
               } else if (var5.equals(var6)) {
                  break label64;
               }

               return false;
            }

            label57: {
               IrisGeneratorStyle var7 = this.getStyle();
               IrisGeneratorStyle var8 = var2.getStyle();
               if (var7 == null) {
                  if (var8 == null) {
                     break label57;
                  }
               } else if (var7.equals(var8)) {
                  break label57;
               }

               return false;
            }

            KList var9 = this.getMinDistances();
            KList var10 = var2.getMinDistances();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisJigsawStructurePlacement;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var11 = var2 * 59 + this.getRarity();
      long var3 = this.getSalt();
      var11 = var11 * 59 + (int)(var3 >>> 32 ^ var3);
      var11 = var11 * 59 + this.getSpacing();
      var11 = var11 * 59 + this.getSeparation();
      long var5 = Double.doubleToLongBits(this.getThreshold());
      var11 = var11 * 59 + (int)(var5 >>> 32 ^ var5);
      String var7 = this.getStructure();
      var11 = var11 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisJigsawStructurePlacement.SpreadType var8 = this.getSpreadType();
      var11 = var11 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisGeneratorStyle var9 = this.getStyle();
      var11 = var11 * 59 + (var9 == null ? 43 : var9.hashCode());
      KList var10 = this.getMinDistances();
      var11 = var11 * 59 + (var10 == null ? 43 : var10.hashCode());
      return var11;
   }

   @Desc("Spread type")
   public static enum SpreadType {
      @Desc("Linear spread")
      LINEAR(RNG::i),
      @Desc("Triangular spread")
      TRIANGULAR((var0, var1) -> {
         return (var0.i(var1) + var0.i(var1)) / 2;
      }),
      @Desc("Noise based spread\nThis ignores the spacing and separation parameters")
      NOISE((var0, var1) -> {
         return 0;
      });

      private final IrisJigsawStructurePlacement.SpreadMethod method;

      private SpreadType(IrisJigsawStructurePlacement.SpreadMethod method) {
         this.method = var3;
      }

      public int apply(RNG rng, int bound) {
         return this.method.apply(var1, var2);
      }

      // $FF: synthetic method
      private static IrisJigsawStructurePlacement.SpreadType[] $values() {
         return new IrisJigsawStructurePlacement.SpreadType[]{LINEAR, TRIANGULAR, NOISE};
      }
   }

   private interface SpreadMethod {
      int apply(RNG rng, int bound);
   }
}
