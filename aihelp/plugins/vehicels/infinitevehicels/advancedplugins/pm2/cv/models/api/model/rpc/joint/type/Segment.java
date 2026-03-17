package advancedplugins.pm2.cv.models.api.model.rpc.joint.type;

import org.joml.Vector3f;

public interface Segment {
   Vector3f getWorldLocation();

   Vector3f getDirection();

   Vector3f getUp();

   boolean isBounded();

   void setBounded(boolean var1);

   boolean isRollLock();

   void setRollLock(boolean var1);

   float getAngleLimit();

   void setAngleLimit(float var1);

   float getExtendRate();

   void setExtendRate(float var1);
}
