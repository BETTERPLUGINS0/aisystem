package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import org.jetbrains.annotations.Nullable;

public enum DefaultRenderType implements IRenderType {
   ANY,
   NONE,
   MODEL;

   private static DefaultRenderType[] $values() {
      return new DefaultRenderType[]{ANY, NONE, MODEL};
   }

   @Nullable
   public BehaviorRenderer createBehaviorRenderer(IVisualModel var1) {
      return null;
   }

   // $FF: synthetic method
   private static DefaultRenderType[] $values$() {
      return new DefaultRenderType[]{ANY, NONE, MODEL};
   }
}
