package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.data;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;

public interface IKeyframeData {
   IKeyframeData EMPTY = (property) -> {
      return 0.0D;
   };

   double getValue(IAnimationProperty var1);
}
