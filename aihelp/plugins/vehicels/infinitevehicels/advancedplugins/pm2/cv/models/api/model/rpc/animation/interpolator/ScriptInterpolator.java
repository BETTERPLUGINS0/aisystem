package advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.AbstractKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class ScriptInterpolator<IN extends AbstractKeyframe<OUT>, OUT> extends KeyframeInterpolator<IN, OUT> {
   private final Supplier<OUT> supplier;
   private final BiConsumer<OUT, OUT> combiner;

   public ScriptInterpolator(Supplier<OUT> var1, BiConsumer<OUT, OUT> var2) {
      this.supplier = var1;
      this.combiner = var2;
   }

   @Nullable
   public OUT interpolate(IJoint var1, IAnimationProperty var2) {
      if (this.isEmpty()) {
         return null;
      } else {
         Object var3 = this.supplier.get();
         float var4 = (float)var2.getLastTime();
         float var5 = (float)var2.getTime();
         float var6 = var4;
         float var7 = (Float)this.lastKey();
         Object var8;
         if (var4 > var5) {
            while(var6 < var7 && (var6 = this.getHigherKey(var6)) <= var7) {
               var8 = ((AbstractKeyframe)this.get(var6)).getValue(0, var2);
               this.combiner.accept(var3, var8);
            }

            var6 = -1.0F;
         }

         while(var6 < var7 && (var6 = this.getHigherKey(var6)) <= var5) {
            var8 = ((AbstractKeyframe)this.get(var6)).getValue(0, var2);
            this.combiner.accept(var3, var8);
         }

         return var3;
      }
   }
}
