package advancedplugins.pm2.cv.models.api.model.rpc;

import org.joml.Vector3f;

public interface IPosition {
   Vector3f getXYZ();

   Vector3f getRotation();

   Vector3f getScale();

   default float getYaw() {
      return 0.0F;
   }

   default float getPitch() {
      return 0.0F;
   }

   void setOrigin(Vector3f var1);

   void setOrigin(double var1, double var3, double var5);

   void setXYZ(double var1, double var3, double var5);

   void setXYZ(Vector3f var1);

   void setRotation(double var1, double var3, double var5);

   void setRotation(Vector3f var1);

   void setScale(double var1, double var3, double var5);

   void setScale(Vector3f var1);

   default void setYaw(float yaw) {
   }

   default void setPitch(float pitch) {
   }

   float getYHeadRot();

   float getXHeadRot();

   float getYBodyRot();

   void setYHeadRot(float var1);

   void setXHeadRot(float var1);

   void setYBodyRot(float var1);

   default Vector3f getOrigin() {
      return this.getXYZ();
   }
}
