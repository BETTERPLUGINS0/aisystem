package advancedplugins.pm2.cv.models.api.model.rpc.animation;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.utils.archive.AbstractArchive;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import java.util.function.BiFunction;

public class AnimationPropertyArchive extends AbstractArchive<BiFunction<AnimationHandler, SavedData, IAnimationProperty>> {
   public IAnimationProperty createAnimationProperty(AnimationHandler var1, SavedData var2) {
      String var3 = var2.getString("id");
      return var3 != null ? (IAnimationProperty)((BiFunction)this.get(var3)).apply(var1, var2) : null;
   }
}
