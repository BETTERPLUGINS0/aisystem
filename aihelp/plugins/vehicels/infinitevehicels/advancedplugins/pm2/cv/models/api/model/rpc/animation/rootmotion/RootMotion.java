package advancedplugins.pm2.cv.models.api.model.rpc.animation.rootmotion;

import org.bukkit.util.Vector;
import org.joml.Vector3f;

public record RootMotion(Vector3f velocity, float yaw, boolean jointOnGround) {
   public RootMotion(Vector3f velocity, float yaw, boolean jointOnGround) {
      this.velocity = var1;
      this.yaw = var2;
      this.jointOnGround = var3;
   }

   public Vector delta() {
      return Vector.fromJOML(this.velocity).rotateAroundY((double)((180.0F - this.yaw) * 0.017453292F));
   }

   public Vector3f velocity() {
      return this.velocity;
   }

   public float yaw() {
      return this.yaw;
   }

   public boolean jointOnGround() {
      return this.jointOnGround;
   }
}
