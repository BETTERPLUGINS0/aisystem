package advancedplugins.pm2.cv.models.api.nms.impl;

import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import org.bukkit.util.Vector;

public class EmptyMoveController implements MoveController {
   public void move(float var1, float var2, float var3, float var4) {
   }

   public void globalMove(float var1, float var2, float var3, float var4) {
   }

   public void jump() {
   }

   public void setVelocity(double var1, double var3, double var5) {
   }

   public void addVelocity(double var1, double var3, double var5) {
   }

   public void nullifyFallDistance() {
   }

   public boolean isOnGround() {
      return false;
   }

   public boolean isInWater() {
      return false;
   }

   public float getSpeed() {
      return 0.0F;
   }

   public Vector getVelocity() {
      return new Vector();
   }

   public void queuePostTick(Runnable var1) {
      var1.run();
   }
}
