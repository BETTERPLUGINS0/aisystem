package advancedplugins.pm2.cv.models.api.model.rpc.animation.rootmotion;

import org.bukkit.util.Vector;

public record RootMotionDelta(Vector delta, boolean onGround) {
   public RootMotionDelta(Vector delta, boolean onGround) {
      this.delta = var1;
      this.onGround = var2;
   }

   public Vector delta() {
      return this.delta;
   }

   public boolean onGround() {
      return this.onGround;
   }
}
