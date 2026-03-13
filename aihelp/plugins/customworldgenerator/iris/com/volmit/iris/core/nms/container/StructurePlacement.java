package com.volmit.iris.core.nms.container;

import com.google.gson.JsonObject;
import com.volmit.iris.engine.object.IrisJigsawStructurePlacement;
import java.util.List;
import lombok.Generated;
import org.apache.commons.math3.fraction.Fraction;

public abstract class StructurePlacement {
   private final int salt;
   private final float frequency;
   private final List<StructurePlacement.Structure> structures;

   public abstract JsonObject toJson(String structure);

   protected JsonObject createBase(String structure) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("structure", var1);
      var2.addProperty("salt", this.salt);
      return var2;
   }

   public int frequencyToSpacing() {
      Fraction var1 = new Fraction((double)Math.max(Math.min(this.frequency, 1.0F), 1.0E-9F));
      return (int)Math.round(Math.sqrt((double)var1.getDenominator() / (double)var1.getNumerator()));
   }

   @Generated
   protected StructurePlacement(final StructurePlacement.StructurePlacementBuilder<?, ?> b) {
      this.salt = var1.salt;
      this.frequency = var1.frequency;
      this.structures = var1.structures;
   }

   @Generated
   public int salt() {
      return this.salt;
   }

   @Generated
   public float frequency() {
      return this.frequency;
   }

   @Generated
   public List<StructurePlacement.Structure> structures() {
      return this.structures;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof StructurePlacement)) {
         return false;
      } else {
         StructurePlacement var2 = (StructurePlacement)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.salt() != var2.salt()) {
            return false;
         } else if (Float.compare(this.frequency(), var2.frequency()) != 0) {
            return false;
         } else {
            List var3 = this.structures();
            List var4 = var2.structures();
            if (var3 == null) {
               if (var4 == null) {
                  return true;
               }
            } else if (var3.equals(var4)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof StructurePlacement;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.salt();
      var4 = var4 * 59 + Float.floatToIntBits(this.frequency());
      List var3 = this.structures();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      int var10000 = this.salt();
      return "StructurePlacement(salt=" + var10000 + ", frequency=" + this.frequency() + ", structures=" + String.valueOf(this.structures()) + ")";
   }

   @Generated
   public abstract static class StructurePlacementBuilder<C extends StructurePlacement, B extends StructurePlacement.StructurePlacementBuilder<C, B>> {
      @Generated
      private int salt;
      @Generated
      private float frequency;
      @Generated
      private List<StructurePlacement.Structure> structures;

      @Generated
      public B salt(final int salt) {
         this.salt = var1;
         return this.self();
      }

      @Generated
      public B frequency(final float frequency) {
         this.frequency = var1;
         return this.self();
      }

      @Generated
      public B structures(final List<StructurePlacement.Structure> structures) {
         this.structures = var1;
         return this.self();
      }

      @Generated
      protected abstract B self();

      @Generated
      public abstract C build();

      @Generated
      public String toString() {
         int var10000 = this.salt;
         return "StructurePlacement.StructurePlacementBuilder(salt=" + var10000 + ", frequency=" + this.frequency + ", structures=" + String.valueOf(this.structures) + ")";
      }
   }

   public static record Structure(int weight, String key, List<String> tags) {
      public Structure(int weight, String key, List<String> tags) {
         this.weight = var1;
         this.key = var2;
         this.tags = var3;
      }

      public boolean isValid() {
         return this.weight > 0 && this.key != null;
      }

      public int weight() {
         return this.weight;
      }

      public String key() {
         return this.key;
      }

      public List<String> tags() {
         return this.tags;
      }
   }

   public static class ConcentricRings extends StructurePlacement {
      private final int distance;
      private final int spread;
      private final int count;

      public JsonObject toJson(String structure) {
         return null;
      }

      @Generated
      protected ConcentricRings(final StructurePlacement.ConcentricRings.ConcentricRingsBuilder<?, ?> b) {
         super(var1);
         this.distance = var1.distance;
         this.spread = var1.spread;
         this.count = var1.count;
      }

      @Generated
      public static StructurePlacement.ConcentricRings.ConcentricRingsBuilder<?, ?> builder() {
         return new StructurePlacement.ConcentricRings.ConcentricRingsBuilderImpl();
      }

      @Generated
      public int distance() {
         return this.distance;
      }

      @Generated
      public int spread() {
         return this.spread;
      }

      @Generated
      public int count() {
         return this.count;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof StructurePlacement.ConcentricRings)) {
            return false;
         } else {
            StructurePlacement.ConcentricRings var2 = (StructurePlacement.ConcentricRings)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (!super.equals(var1)) {
               return false;
            } else if (this.distance() != var2.distance()) {
               return false;
            } else if (this.spread() != var2.spread()) {
               return false;
            } else {
               return this.count() == var2.count();
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof StructurePlacement.ConcentricRings;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         int var2 = super.hashCode();
         var2 = var2 * 59 + this.distance();
         var2 = var2 * 59 + this.spread();
         var2 = var2 * 59 + this.count();
         return var2;
      }

      @Generated
      public abstract static class ConcentricRingsBuilder<C extends StructurePlacement.ConcentricRings, B extends StructurePlacement.ConcentricRings.ConcentricRingsBuilder<C, B>> extends StructurePlacement.StructurePlacementBuilder<C, B> {
         @Generated
         private int distance;
         @Generated
         private int spread;
         @Generated
         private int count;

         @Generated
         public B distance(final int distance) {
            this.distance = var1;
            return this.self();
         }

         @Generated
         public B spread(final int spread) {
            this.spread = var1;
            return this.self();
         }

         @Generated
         public B count(final int count) {
            this.count = var1;
            return this.self();
         }

         @Generated
         protected abstract B self();

         @Generated
         public abstract C build();

         @Generated
         public String toString() {
            String var10000 = super.toString();
            return "StructurePlacement.ConcentricRings.ConcentricRingsBuilder(super=" + var10000 + ", distance=" + this.distance + ", spread=" + this.spread + ", count=" + this.count + ")";
         }
      }

      @Generated
      private static final class ConcentricRingsBuilderImpl extends StructurePlacement.ConcentricRings.ConcentricRingsBuilder<StructurePlacement.ConcentricRings, StructurePlacement.ConcentricRings.ConcentricRingsBuilderImpl> {
         @Generated
         protected StructurePlacement.ConcentricRings.ConcentricRingsBuilderImpl self() {
            return this;
         }

         @Generated
         public StructurePlacement.ConcentricRings build() {
            return new StructurePlacement.ConcentricRings(this);
         }
      }
   }

   public static class RandomSpread extends StructurePlacement {
      private final int spacing;
      private final int separation;
      private final IrisJigsawStructurePlacement.SpreadType spreadType;

      public JsonObject toJson(String structure) {
         JsonObject var2 = this.createBase(var1);
         var2.addProperty("spacing", Math.max(this.spacing, this.frequencyToSpacing()));
         var2.addProperty("separation", this.separation);
         var2.addProperty("spreadType", this.spreadType.name());
         return var2;
      }

      @Generated
      protected RandomSpread(final StructurePlacement.RandomSpread.RandomSpreadBuilder<?, ?> b) {
         super(var1);
         this.spacing = var1.spacing;
         this.separation = var1.separation;
         this.spreadType = var1.spreadType;
      }

      @Generated
      public static StructurePlacement.RandomSpread.RandomSpreadBuilder<?, ?> builder() {
         return new StructurePlacement.RandomSpread.RandomSpreadBuilderImpl();
      }

      @Generated
      public int spacing() {
         return this.spacing;
      }

      @Generated
      public int separation() {
         return this.separation;
      }

      @Generated
      public IrisJigsawStructurePlacement.SpreadType spreadType() {
         return this.spreadType;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof StructurePlacement.RandomSpread)) {
            return false;
         } else {
            StructurePlacement.RandomSpread var2 = (StructurePlacement.RandomSpread)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (!super.equals(var1)) {
               return false;
            } else if (this.spacing() != var2.spacing()) {
               return false;
            } else if (this.separation() != var2.separation()) {
               return false;
            } else {
               IrisJigsawStructurePlacement.SpreadType var3 = this.spreadType();
               IrisJigsawStructurePlacement.SpreadType var4 = var2.spreadType();
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
         return var1 instanceof StructurePlacement.RandomSpread;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         int var2 = super.hashCode();
         var2 = var2 * 59 + this.spacing();
         var2 = var2 * 59 + this.separation();
         IrisJigsawStructurePlacement.SpreadType var3 = this.spreadType();
         var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
         return var2;
      }

      @Generated
      public abstract static class RandomSpreadBuilder<C extends StructurePlacement.RandomSpread, B extends StructurePlacement.RandomSpread.RandomSpreadBuilder<C, B>> extends StructurePlacement.StructurePlacementBuilder<C, B> {
         @Generated
         private int spacing;
         @Generated
         private int separation;
         @Generated
         private IrisJigsawStructurePlacement.SpreadType spreadType;

         @Generated
         public B spacing(final int spacing) {
            this.spacing = var1;
            return this.self();
         }

         @Generated
         public B separation(final int separation) {
            this.separation = var1;
            return this.self();
         }

         @Generated
         public B spreadType(final IrisJigsawStructurePlacement.SpreadType spreadType) {
            this.spreadType = var1;
            return this.self();
         }

         @Generated
         protected abstract B self();

         @Generated
         public abstract C build();

         @Generated
         public String toString() {
            String var10000 = super.toString();
            return "StructurePlacement.RandomSpread.RandomSpreadBuilder(super=" + var10000 + ", spacing=" + this.spacing + ", separation=" + this.separation + ", spreadType=" + String.valueOf(this.spreadType) + ")";
         }
      }

      @Generated
      private static final class RandomSpreadBuilderImpl extends StructurePlacement.RandomSpread.RandomSpreadBuilder<StructurePlacement.RandomSpread, StructurePlacement.RandomSpread.RandomSpreadBuilderImpl> {
         @Generated
         protected StructurePlacement.RandomSpread.RandomSpreadBuilderImpl self() {
            return this;
         }

         @Generated
         public StructurePlacement.RandomSpread build() {
            return new StructurePlacement.RandomSpread(this);
         }
      }
   }
}
