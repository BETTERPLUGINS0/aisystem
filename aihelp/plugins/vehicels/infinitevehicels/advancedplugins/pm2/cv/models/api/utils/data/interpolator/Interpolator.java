package advancedplugins.pm2.cv.models.api.utils.data.interpolator;

import java.util.TreeMap;
import org.jetbrains.annotations.Nullable;

public class Interpolator<IN, OUT> extends TreeMap<Float, IN> {
   protected Interpolator.Interpolation<IN> interpolateFunc;
   protected Interpolator.Parse<IN, OUT> parseFunc;
   protected OUT defaultValue = null;

   public Interpolator<IN, OUT> setInterpolateFunc(Interpolator.Interpolation<IN> var1) {
      this.interpolateFunc = var1;
      return this;
   }

   public Interpolator<IN, OUT> setParseFunc(Interpolator.Parse<IN, OUT> var1) {
      this.parseFunc = var1;
      return this;
   }

   public Interpolator<IN, OUT> setDefaultValue(OUT var1) {
      this.defaultValue = var1;
      return this;
   }

   @Nullable
   public OUT interpolate(float var1) {
      if (this.isEmpty()) {
         return this.defaultValue;
      } else if (this.containsKey(var1)) {
         return this.parseFunc.parse(this.get(var1));
      } else {
         float var2 = this.getHigherKey(var1);
         float var3 = this.getLowerKey(var1);
         if (var2 == var3) {
            return this.parseFunc.parse(this.get(var3));
         } else {
            float var4 = (var1 - var3) / (var2 - var3);
            Object var5 = this.get(var2);
            Object var6 = this.get(var3);
            return this.parseFunc.parse(this.interpolateFunc.interpolate(new Interpolator.Context(var3, var2), var6, var5, var4));
         }
      }
   }

   public float getHigherKey(float var1) {
      Float var2 = (Float)this.higherKey(var1);
      return var2 == null ? (Float)this.lastKey() : var2;
   }

   public float getLowerKey(float var1) {
      Float var2 = (Float)this.lowerKey(var1);
      return var2 == null ? (Float)this.firstKey() : var2;
   }

   @FunctionalInterface
   public interface Interpolation<IN> {
      IN interpolate(Interpolator.Context var1, IN var2, IN var3, float var4);
   }

   @FunctionalInterface
   public interface Parse<IN, OUT> {
      OUT parse(IN var1);
   }

   public static class Context {
      public final float prevKey;
      public final float nextKey;

      public Context(float var1, float var2) {
         this.prevKey = var1;
         this.nextKey = var2;
      }
   }
}
