package advancedplugins.pm2.cv.models.api.model.rpc.animation;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator.KeyframeInterpolator;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeType;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.AbstractKeyframe;
import com.google.common.collect.Maps;
import java.util.Map;

public class Timeline {
   private final BlueprintAnimation animation;
   private final boolean globalRotation;
   private final Map<KeyframeType<?, ?>, KeyframeInterpolator<?, ?>> interpolators = Maps.newConcurrentMap();

   public Timeline(BlueprintAnimation var1, boolean var2) {
      this.animation = var1;
      this.globalRotation = var2;
   }

   public boolean hasInterpolator(KeyframeType<?, ?> var1) {
      return this.interpolators.containsKey(var1);
   }

   public <KEY extends AbstractKeyframe<DATA>, DATA> KeyframeInterpolator<KEY, DATA> getInterpolator(KeyframeType<KEY, DATA> var1) {
      return (KeyframeInterpolator)this.interpolators.computeIfAbsent(var1, (var1x) -> {
         return var1x.createInterpolator(this);
      });
   }

   public <KEY extends AbstractKeyframe<DATA>, DATA> KEY getKeyframe(float var1, KeyframeType<KEY, DATA> var2) {
      KeyframeInterpolator var3 = this.getInterpolator(var2);
      return (AbstractKeyframe)var3.computeIfAbsent(var1, (var1x) -> {
         return var2.createKeyframe();
      });
   }

   public BlueprintAnimation getAnimation() {
      return this.animation;
   }

   public boolean isGlobalRotation() {
      return this.globalRotation;
   }
}
