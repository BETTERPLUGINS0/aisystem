package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.paralithic.functions.dynamic.Context;
import com.volmit.iris.util.paralithic.functions.dynamic.DynamicFunction;
import com.volmit.iris.util.paralithic.node.Statefulness;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.Objects;
import lombok.Generated;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Snippet("expression-function")
@Desc("Represents a function to use in your expression. Do not set the name to x, y, or z, also don't duplicate names.")
public class IrisExpressionFunction implements DynamicFunction {
   @Required
   @Desc("The function to assign this value to. Do not set the name to x, y, or z")
   private String name;
   @Desc("If defined, this variable will use a generator style as it's value")
   private IrisGeneratorStyle styleValue = null;
   @Desc("If defined, iris will use an internal stream from the engine as it's value")
   private IrisEngineStreamType engineStreamValue = null;
   @MinNumber(2.0D)
   @Desc("Number of arguments for the function")
   private int args = 2;
   private final transient KMap<IrisExpressionFunction.FunctionContext, IrisExpressionFunction.Provider> cache = new KMap();
   private transient IrisData data;

   public boolean isValid() {
      return this.styleValue != null || this.engineStreamValue != null;
   }

   public int getArgNumber() {
      return this.engineStreamValue != null ? 2 : Math.max(this.args, 2);
   }

   @NotNull
   public Statefulness statefulness() {
      return Statefulness.STATEFUL;
   }

   public double eval(double... doubles) {
      return 0.0D;
   }

   public double eval(@Nullable Context raw, double... args) {
      return ((IrisExpressionFunction.Provider)this.cache.computeIfAbsent((IrisExpressionFunction.FunctionContext)var1, (var1x) -> {
         assert var1x != null;

         if (this.engineStreamValue != null) {
            ProceduralStream var2 = this.engineStreamValue.get(this.data.getEngine());
            return (var1) -> {
               return (Double)var2.get(var1[0], var1[1]);
            };
         } else if (this.styleValue != null) {
            CNG var10000 = this.styleValue.createNoCache(var1x.rng, this.data);
            Objects.requireNonNull(var10000);
            return var10000::noise;
         } else {
            return (var0) -> {
               return Double.NaN;
            };
         }
      })).eval(var2);
   }

   @Generated
   public IrisExpressionFunction() {
   }

   @Generated
   public IrisExpressionFunction(final String name, final IrisGeneratorStyle styleValue, final IrisEngineStreamType engineStreamValue, final int args, final IrisData data) {
      this.name = var1;
      this.styleValue = var2;
      this.engineStreamValue = var3;
      this.args = var4;
      this.data = var5;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public IrisGeneratorStyle getStyleValue() {
      return this.styleValue;
   }

   @Generated
   public IrisEngineStreamType getEngineStreamValue() {
      return this.engineStreamValue;
   }

   @Generated
   public int getArgs() {
      return this.args;
   }

   @Generated
   public IrisData getData() {
      return this.data;
   }

   @Generated
   public IrisExpressionFunction setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisExpressionFunction setStyleValue(final IrisGeneratorStyle styleValue) {
      this.styleValue = var1;
      return this;
   }

   @Generated
   public IrisExpressionFunction setEngineStreamValue(final IrisEngineStreamType engineStreamValue) {
      this.engineStreamValue = var1;
      return this;
   }

   @Generated
   public IrisExpressionFunction setArgs(final int args) {
      this.args = var1;
      return this;
   }

   @Generated
   public IrisExpressionFunction setData(final IrisData data) {
      this.data = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = this.getName();
      return "IrisExpressionFunction(name=" + var10000 + ", styleValue=" + String.valueOf(this.getStyleValue()) + ", engineStreamValue=" + String.valueOf(this.getEngineStreamValue()) + ", args=" + this.getArgs() + ", cache=" + String.valueOf(this.cache) + ", data=" + String.valueOf(this.getData()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisExpressionFunction)) {
         return false;
      } else {
         IrisExpressionFunction var2 = (IrisExpressionFunction)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getArgs() != var2.getArgs()) {
            return false;
         } else {
            label49: {
               String var3 = this.getName();
               String var4 = var2.getName();
               if (var3 == null) {
                  if (var4 == null) {
                     break label49;
                  }
               } else if (var3.equals(var4)) {
                  break label49;
               }

               return false;
            }

            IrisGeneratorStyle var5 = this.getStyleValue();
            IrisGeneratorStyle var6 = var2.getStyleValue();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisEngineStreamType var7 = this.getEngineStreamValue();
            IrisEngineStreamType var8 = var2.getEngineStreamValue();
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
      return var1 instanceof IrisExpressionFunction;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var6 = var2 * 59 + this.getArgs();
      String var3 = this.getName();
      var6 = var6 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisGeneratorStyle var4 = this.getStyleValue();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisEngineStreamType var5 = this.getEngineStreamValue();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   public static record FunctionContext(@NonNull RNG rng) implements Context {
      @Generated
      public FunctionContext(@NonNull RNG rng) {
         if (var1 == null) {
            throw new NullPointerException("rng is marked non-null but is null");
         } else {
            this.rng = var1;
         }
      }

      public boolean equals(Object o) {
         if (var1 != null && this.getClass() == var1.getClass()) {
            IrisExpressionFunction.FunctionContext var2 = (IrisExpressionFunction.FunctionContext)var1;
            return this.rng.getSeed() == var2.rng.getSeed();
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Long.hashCode(this.rng.getSeed());
      }

      @NonNull
      public RNG rng() {
         return this.rng;
      }
   }

   @FunctionalInterface
   private interface Provider {
      double eval(double... args);
   }
}
