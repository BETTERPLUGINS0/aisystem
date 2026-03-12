package ac.grim.grimac.utils.data;

public record HeadRotation(float yaw, float pitch) {
   public HeadRotation(float yaw, float pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public float yaw() {
      return this.yaw;
   }

   public float pitch() {
      return this.pitch;
   }
}
