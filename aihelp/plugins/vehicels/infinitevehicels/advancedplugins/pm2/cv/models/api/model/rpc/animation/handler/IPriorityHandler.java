package advancedplugins.pm2.cv.models.api.model.rpc.animation.handler;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.ModelState;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import java.util.function.BiConsumer;

public interface IPriorityHandler extends AnimationHandler {
   void forEachProperty(BiConsumer<String, IAnimationProperty> var1);

   void playState(ModelState var1);

   default String getId() {
      return "priority";
   }
}
