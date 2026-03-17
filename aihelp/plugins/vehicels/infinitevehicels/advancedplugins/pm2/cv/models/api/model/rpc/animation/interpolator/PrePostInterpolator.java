package advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.AbstractKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class PrePostInterpolator<IN extends AbstractKeyframe<OUT>, OUT> extends KeyframeInterpolator<IN, OUT> {
   private final BiFunction<OUT, OUT, OUT> derivativeFunc;
   private final BiConsumer<KeyframeInterpolator.Context<IN, OUT>, IN> finalizerFunc;

   public PrePostInterpolator(KeyframeInterpolator.Interpolation<IN, OUT> var1, BiConsumer<KeyframeInterpolator.Context<IN, OUT>, IN> var2, BiFunction<OUT, OUT, OUT> var3) {
      this(var1, var2, var3, () -> {
         return null;
      });
   }

   public PrePostInterpolator(KeyframeInterpolator.Interpolation<IN, OUT> var1, BiConsumer<KeyframeInterpolator.Context<IN, OUT>, IN> var2, BiFunction<OUT, OUT, OUT> var3, Supplier<OUT> var4) {
      this.setInterpolateFunc(var1);
      this.derivativeFunc = var3;
      this.finalizerFunc = var2;
      this.setDefaultValue(var4);
   }

   @Nullable
   public OUT interpolate(IJoint var1, IAnimationProperty var2) {
      return this.interpolate(var1, var2, (float)var2.getTime());
   }

   public OUT interpolateAndDerive(IJoint var1, IAnimationProperty var2) {
      float var3 = (float)var2.getTime();
      float var4 = (float)MathUtils.clamp(var2.getLastTime(), 0.0D, var2.getTime());
      Object var5 = this.interpolate(var1, var2, var3);
      if (MathUtils.isSimilar(var3, var4)) {
         return this.derivativeFunc.apply(var5, var5);
      } else {
         Object var6 = this.interpolate(var1, var2, var4);
         return this.derivativeFunc.apply(var6, var5);
      }
   }

   @Nullable
   private OUT interpolate(IJoint var1, IAnimationProperty var2, float var3) {
      if (this.isEmpty()) {
         return this.defaultValue.get();
      } else if (this.containsKey(var3)) {
         AbstractKeyframe var9 = (AbstractKeyframe)this.get(var3);
         this.finalizerFunc.accept(new KeyframeInterpolator.Context(var3, var3, var2, var1, this), var9);
         return var9.getValue(0, var2);
      } else {
         float var4 = this.getHigherKey(var3);
         float var5 = this.getLowerKey(var3);
         if (var4 == var5) {
            AbstractKeyframe var10 = (AbstractKeyframe)this.get(var5);
            this.finalizerFunc.accept(new KeyframeInterpolator.Context(var5, var4, var2, var1, this), var10);
            return var10.getValue(0, var2);
         } else {
            float var6 = (var3 - var5) / (var4 - var5);
            Object var7 = ((AbstractKeyframe)this.get(var4)).getValue(0, var2);
            Object var8 = ((AbstractKeyframe)this.get(var5)).getValue(1, var2);
            return this.interpolateFunc.interpolate(new KeyframeInterpolator.Context(var5, var4, var2, var1, this), var8, var7, var6);
         }
      }
   }
}
