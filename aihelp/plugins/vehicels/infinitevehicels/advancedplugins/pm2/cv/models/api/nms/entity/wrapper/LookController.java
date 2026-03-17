package advancedplugins.pm2.cv.models.api.nms.entity.wrapper;

public interface LookController {
   void lookAt(double var1, double var3, double var5);

   void setPitch(float var1);

   void setHeadYaw(float var1);

   void setBodyYaw(float var1);

   default void setMovementOverride(MovementOverride override) {
   }
}
