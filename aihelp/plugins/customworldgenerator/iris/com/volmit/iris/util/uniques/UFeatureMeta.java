package com.volmit.iris.util.uniques;

import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.noise.CNG;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;

public class UFeatureMeta {
   private KMap<String, UFeatureMeta.UFeatureMetaInterpolator> interpolators;
   private KMap<String, UFeatureMeta.UFeatureMetaGenerator> generators;
   private String feature;

   public void registerInterpolator(String key, InterpolationMethod method, double radius) {
      if (this.interpolators == null) {
         this.interpolators = new KMap();
      }

      this.interpolators.put(var1, new UFeatureMeta.UFeatureMetaInterpolator(var2, var3));
   }

   public void registerGenerator(String key, CNG cng) {
      if (this.generators == null) {
         this.generators = new KMap();
      }

      this.generators.put(var1, this.buildGenerator(var2));
   }

   public UFeatureMeta.UFeatureMetaGenerator buildGenerator(CNG cng) {
      UFeatureMeta.UFeatureMetaGenerator var2 = new UFeatureMeta.UFeatureMetaGenerator();
      var2.setScale(var1.getScale());
      var2.setOctaves(var1.getOct());
      if (var1.getFracture() != null) {
         var2.setFracture(this.buildGenerator(var1.getFracture()));
         var2.setFractureMultiplier(var1.getFscale());
      }

      if (var1.getChildren() != null && var1.getChildren().isNotEmpty()) {
         var2.setChildren(new KList());
         Iterator var3 = var1.getChildren().iterator();

         while(var3.hasNext()) {
            CNG var4 = (CNG)var3.next();
            var2.getChildren().add(this.buildGenerator(var4));
         }
      }

      if (var1.getInjector() == CNG.ADD) {
         var2.setParentInject("add");
      } else if (var1.getInjector() == CNG.SRC_SUBTRACT) {
         var2.setParentInject("src_subtract");
      } else if (var1.getInjector() == CNG.DST_SUBTRACT) {
         var2.setParentInject("dst_subtract");
      } else if (var1.getInjector() == CNG.MULTIPLY) {
         var2.setParentInject("multiply");
      } else if (var1.getInjector() == CNG.MAX) {
         var2.setParentInject("max");
      } else if (var1.getInjector() == CNG.MIN) {
         var2.setParentInject("min");
      } else if (var1.getInjector() == CNG.SRC_MOD) {
         var2.setParentInject("src_mod");
      } else if (var1.getInjector() == CNG.SRC_POW) {
         var2.setParentInject("src_pow");
      } else if (var1.getInjector() == CNG.DST_MOD) {
         var2.setParentInject("dst_mod");
      } else if (var1.getInjector() == CNG.DST_POW) {
         var2.setParentInject("dst_pow");
      }

      return var2;
   }

   public boolean isEmpty() {
      return (this.interpolators == null || this.interpolators.isEmpty()) && (this.generators == null || this.generators.isEmpty());
   }

   @Generated
   public KMap<String, UFeatureMeta.UFeatureMetaInterpolator> getInterpolators() {
      return this.interpolators;
   }

   @Generated
   public KMap<String, UFeatureMeta.UFeatureMetaGenerator> getGenerators() {
      return this.generators;
   }

   @Generated
   public String getFeature() {
      return this.feature;
   }

   @Generated
   public void setInterpolators(final KMap<String, UFeatureMeta.UFeatureMetaInterpolator> interpolators) {
      this.interpolators = var1;
   }

   @Generated
   public void setGenerators(final KMap<String, UFeatureMeta.UFeatureMetaGenerator> generators) {
      this.generators = var1;
   }

   @Generated
   public void setFeature(final String feature) {
      this.feature = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof UFeatureMeta)) {
         return false;
      } else {
         UFeatureMeta var2 = (UFeatureMeta)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label47: {
               KMap var3 = this.getInterpolators();
               KMap var4 = var2.getInterpolators();
               if (var3 == null) {
                  if (var4 == null) {
                     break label47;
                  }
               } else if (var3.equals(var4)) {
                  break label47;
               }

               return false;
            }

            KMap var5 = this.getGenerators();
            KMap var6 = var2.getGenerators();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            String var7 = this.getFeature();
            String var8 = var2.getFeature();
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
      return var1 instanceof UFeatureMeta;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KMap var3 = this.getInterpolators();
      int var6 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KMap var4 = this.getGenerators();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getFeature();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getInterpolators());
      return "UFeatureMeta(interpolators=" + var10000 + ", generators=" + String.valueOf(this.getGenerators()) + ", feature=" + this.getFeature() + ")";
   }

   static class UFeatureMetaInterpolator {
      private InterpolationMethod interpolator;
      private double radius;

      @Generated
      public InterpolationMethod getInterpolator() {
         return this.interpolator;
      }

      @Generated
      public double getRadius() {
         return this.radius;
      }

      @Generated
      public void setInterpolator(final InterpolationMethod interpolator) {
         this.interpolator = var1;
      }

      @Generated
      public void setRadius(final double radius) {
         this.radius = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof UFeatureMeta.UFeatureMetaInterpolator)) {
            return false;
         } else {
            UFeatureMeta.UFeatureMetaInterpolator var2 = (UFeatureMeta.UFeatureMetaInterpolator)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (Double.compare(this.getRadius(), var2.getRadius()) != 0) {
               return false;
            } else {
               InterpolationMethod var3 = this.getInterpolator();
               InterpolationMethod var4 = var2.getInterpolator();
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
         return var1 instanceof UFeatureMeta.UFeatureMetaInterpolator;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         long var3 = Double.doubleToLongBits(this.getRadius());
         int var6 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
         InterpolationMethod var5 = this.getInterpolator();
         var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
         return var6;
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.getInterpolator());
         return "UFeatureMeta.UFeatureMetaInterpolator(interpolator=" + var10000 + ", radius=" + this.getRadius() + ")";
      }

      @Generated
      public UFeatureMetaInterpolator() {
      }

      @Generated
      public UFeatureMetaInterpolator(final InterpolationMethod interpolator, final double radius) {
         this.interpolator = var1;
         this.radius = var2;
      }
   }

   static class UFeatureMetaGenerator {
      private NoiseStyle style;
      private int octaves = 1;
      private double scale = 1.0D;
      private String parentInject;
      private UFeatureMeta.UFeatureMetaGenerator fracture;
      private Double fractureMultiplier;
      private List<UFeatureMeta.UFeatureMetaGenerator> children;

      @Generated
      public NoiseStyle getStyle() {
         return this.style;
      }

      @Generated
      public int getOctaves() {
         return this.octaves;
      }

      @Generated
      public double getScale() {
         return this.scale;
      }

      @Generated
      public String getParentInject() {
         return this.parentInject;
      }

      @Generated
      public UFeatureMeta.UFeatureMetaGenerator getFracture() {
         return this.fracture;
      }

      @Generated
      public Double getFractureMultiplier() {
         return this.fractureMultiplier;
      }

      @Generated
      public List<UFeatureMeta.UFeatureMetaGenerator> getChildren() {
         return this.children;
      }

      @Generated
      public void setStyle(final NoiseStyle style) {
         this.style = var1;
      }

      @Generated
      public void setOctaves(final int octaves) {
         this.octaves = var1;
      }

      @Generated
      public void setScale(final double scale) {
         this.scale = var1;
      }

      @Generated
      public void setParentInject(final String parentInject) {
         this.parentInject = var1;
      }

      @Generated
      public void setFracture(final UFeatureMeta.UFeatureMetaGenerator fracture) {
         this.fracture = var1;
      }

      @Generated
      public void setFractureMultiplier(final Double fractureMultiplier) {
         this.fractureMultiplier = var1;
      }

      @Generated
      public void setChildren(final List<UFeatureMeta.UFeatureMetaGenerator> children) {
         this.children = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof UFeatureMeta.UFeatureMetaGenerator)) {
            return false;
         } else {
            UFeatureMeta.UFeatureMetaGenerator var2 = (UFeatureMeta.UFeatureMetaGenerator)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getOctaves() != var2.getOctaves()) {
               return false;
            } else if (Double.compare(this.getScale(), var2.getScale()) != 0) {
               return false;
            } else {
               label76: {
                  Double var3 = this.getFractureMultiplier();
                  Double var4 = var2.getFractureMultiplier();
                  if (var3 == null) {
                     if (var4 == null) {
                        break label76;
                     }
                  } else if (var3.equals(var4)) {
                     break label76;
                  }

                  return false;
               }

               NoiseStyle var5 = this.getStyle();
               NoiseStyle var6 = var2.getStyle();
               if (var5 == null) {
                  if (var6 != null) {
                     return false;
                  }
               } else if (!var5.equals(var6)) {
                  return false;
               }

               label62: {
                  String var7 = this.getParentInject();
                  String var8 = var2.getParentInject();
                  if (var7 == null) {
                     if (var8 == null) {
                        break label62;
                     }
                  } else if (var7.equals(var8)) {
                     break label62;
                  }

                  return false;
               }

               label55: {
                  UFeatureMeta.UFeatureMetaGenerator var9 = this.getFracture();
                  UFeatureMeta.UFeatureMetaGenerator var10 = var2.getFracture();
                  if (var9 == null) {
                     if (var10 == null) {
                        break label55;
                     }
                  } else if (var9.equals(var10)) {
                     break label55;
                  }

                  return false;
               }

               List var11 = this.getChildren();
               List var12 = var2.getChildren();
               if (var11 == null) {
                  if (var12 != null) {
                     return false;
                  }
               } else if (!var11.equals(var12)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof UFeatureMeta.UFeatureMetaGenerator;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var10 = var2 * 59 + this.getOctaves();
         long var3 = Double.doubleToLongBits(this.getScale());
         var10 = var10 * 59 + (int)(var3 >>> 32 ^ var3);
         Double var5 = this.getFractureMultiplier();
         var10 = var10 * 59 + (var5 == null ? 43 : var5.hashCode());
         NoiseStyle var6 = this.getStyle();
         var10 = var10 * 59 + (var6 == null ? 43 : var6.hashCode());
         String var7 = this.getParentInject();
         var10 = var10 * 59 + (var7 == null ? 43 : var7.hashCode());
         UFeatureMeta.UFeatureMetaGenerator var8 = this.getFracture();
         var10 = var10 * 59 + (var8 == null ? 43 : var8.hashCode());
         List var9 = this.getChildren();
         var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
         return var10;
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.getStyle());
         return "UFeatureMeta.UFeatureMetaGenerator(style=" + var10000 + ", octaves=" + this.getOctaves() + ", scale=" + this.getScale() + ", parentInject=" + this.getParentInject() + ", fracture=" + String.valueOf(this.getFracture()) + ", fractureMultiplier=" + this.getFractureMultiplier() + ", children=" + String.valueOf(this.getChildren()) + ")";
      }

      @Generated
      public UFeatureMetaGenerator() {
      }
   }
}
