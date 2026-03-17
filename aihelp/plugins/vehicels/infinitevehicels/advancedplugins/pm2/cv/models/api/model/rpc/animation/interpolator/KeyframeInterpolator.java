package advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.AbstractKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import java.util.TreeMap;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class KeyframeInterpolator<IN extends AbstractKeyframe<OUT>, OUT> extends TreeMap<Float, IN> {
   protected KeyframeInterpolator.Interpolation<IN, OUT> interpolateFunc;
   protected Supplier<OUT> defaultValue = null;

   public KeyframeInterpolator<IN, OUT> setInterpolateFunc(KeyframeInterpolator.Interpolation<IN, OUT> var1) {
      this.interpolateFunc = var1;
      return this;
   }

   public KeyframeInterpolator<IN, OUT> setDefaultValue(Supplier<OUT> var1) {
      this.defaultValue = var1;
      return this;
   }

   @Nullable
   public OUT interpolate(IJoint var1, IAnimationProperty var2) {
      if (this.isEmpty()) {
         return this.defaultValue.get();
      } else {
         float var3 = (float)var2.getTime();
         if (this.containsKey(var3)) {
            return ((AbstractKeyframe)this.get(var3)).getValue(0, var2);
         } else {
            float var4 = this.getHigherKey(var3);
            float var5 = this.getLowerKey(var3);
            if (var4 == var5) {
               return ((AbstractKeyframe)this.get(var5)).getValue(0, var2);
            } else {
               float var6 = (var3 - var5) / (var4 - var5);
               Object var7 = ((AbstractKeyframe)this.get(var4)).getValue(0, var2);
               Object var8 = ((AbstractKeyframe)this.get(var5)).getValue(0, var2);
               return this.interpolateFunc.interpolate(new KeyframeInterpolator.Context(var5, var4, var2, var1, this), var8, var7, var6);
            }
         }
      }
   }

   public OUT interpolateAndDerive(IJoint var1, IAnimationProperty var2) {
      throw new UnsupportedOperationException();
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
   public interface Interpolation<IN extends AbstractKeyframe<OUT>, OUT> {
      OUT interpolate(KeyframeInterpolator.Context<IN, OUT> var1, OUT var2, OUT var3, float var4);
   }

   public static class Context<IN extends AbstractKeyframe<OUT>, OUT> {
      public final float prevKey;
      public final float nextKey;
      public final IAnimationProperty property;
      public final IJoint joint;
      public final KeyframeInterpolator<IN, OUT> interpolator;

      public Context(float var1, float var2, IAnimationProperty var3, IJoint var4, KeyframeInterpolator<IN, OUT> var5) {
         this.prevKey = var1;
         this.nextKey = var2;
         this.property = var3;
         this.joint = var4;
         this.interpolator = var5;
      }
   }
}
