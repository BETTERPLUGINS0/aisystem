package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.UUID;
import org.joml.Vector3f;

public interface MountRenderer extends BehaviorRenderer, RenderQueues<MountRenderer.Mount> {
   public interface Mount {
      int getPivotId();

      UUID getPivotUuid();

      int getMountId();

      UUID getMountUuid();

      DataTracker<Vector3f> getPosition();

      DataTracker<Byte> getYaw();

      DataTracker<Float> getMaxHealth();

      DataTracker<Float> getHealth();

      CollectionDataTracker<Integer> getPassengers();
   }
}
