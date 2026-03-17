package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.UUID;
import org.joml.Vector3f;

public interface SubHitboxRenderer extends BehaviorRenderer, RenderQueues<SubHitboxRenderer.SubHitbox> {
   public interface SubHitbox {
      int getPivotId();

      int getHitboxId();

      UUID getPivotUuid();

      UUID getHitboxUuid();

      DataTracker<Vector3f> getPosition();

      DataTracker<Float> getWidth();

      DataTracker<Float> getHeight();

      default boolean isDirty() {
         return this.getWidth().isDirty() || this.getHeight().isDirty();
      }

      default void clearDirty() {
         this.getWidth().clearDirty();
         this.getHeight().clearDirty();
      }
   }
}
