package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.data;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;

public class DoubleData implements IKeyframeData {
   private final double data;

   public DoubleData(double var1) {
      this.data = var1;
   }

   public double getValue(IAnimationProperty var1) {
      return this.data;
   }
}
