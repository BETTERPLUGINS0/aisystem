package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import org.jetbrains.annotations.Nullable;

public interface IRenderType {
   @Nullable
   BehaviorRenderer createBehaviorRenderer(IVisualModel var1);
}
