package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.UUID;
import org.joml.Vector3f;

public interface LeashRenderer extends BehaviorRenderer, RenderQueues<LeashRenderer.Leash> {
   public interface Leash {
      int getPivotId();

      UUID getPivotUUID();

      int getLeashId();

      UUID getLeastUUID();

      DataTracker<Vector3f> getPosition();

      DataTracker<Integer> getConnected();
   }
}
