package advancedplugins.pm2.cv.models.api.nms.entity.wrapper;

import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;

public interface MovementOverride {
   void updateMovement(MoveController var1, IModelContainer var2);

   void updateDirection(LookController var1, IModelContainer var2);
}
