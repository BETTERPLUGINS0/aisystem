package advancedplugins.pm2.cv.models.api.nms.entity;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import java.util.UUID;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public interface HitboxEntity {
   IJoint getJoint();

   SubHitbox getSubHitbox();

   int getEntityId();

   UUID getUniqueId();

   void queueLocation(Vector3f var1);

   Location getLocation();

   @Nullable
   OrientedBoundingBox getOrientedBoundingBox();

   void markRemoved();
}
