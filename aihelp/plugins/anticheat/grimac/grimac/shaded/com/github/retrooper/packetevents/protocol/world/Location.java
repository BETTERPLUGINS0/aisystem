package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;

public class Location {
   private Vector3d position;
   private float yaw;
   private float pitch;

   public Location(Vector3d position, float yaw, float pitch) {
      this.position = position;
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public Location(double x, double y, double z, float yaw, float pitch) {
      this(new Vector3d(x, y, z), yaw, pitch);
   }

   public Vector3d getPosition() {
      return this.position;
   }

   public double getX() {
      return this.position.getX();
   }

   public double getY() {
      return this.position.getY();
   }

   public double getZ() {
      return this.position.getZ();
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public Vector3f getDirection() {
      double rotX = (double)this.getYaw();
      double rotY = (double)this.getPitch();
      float y = (float)(-Math.sin(Math.toRadians(rotY)));
      double xz = Math.cos(Math.toRadians(rotY));
      float x = (float)(-xz * Math.sin(Math.toRadians(rotX)));
      float z = (float)(xz * Math.cos(Math.toRadians(rotX)));
      return new Vector3f(x, y, z);
   }

   public void setDirection(Vector3f vector) {
      double _2PI = 6.283185307179586D;
      double x = (double)vector.getX();
      double z = (double)vector.getZ();
      if (x == 0.0D && z == 0.0D) {
         this.pitch = (double)vector.getY() > 0.0D ? -90.0F : 90.0F;
      } else {
         double theta = Math.atan2(-x, z);
         this.yaw = (float)Math.toDegrees((theta + 6.283185307179586D) % 6.283185307179586D);
         double x2 = x * x;
         double z2 = z * z;
         double xz = Math.sqrt(x2 + z2);
         this.pitch = (float)Math.toDegrees(Math.atan((double)(-vector.getY()) / xz));
      }

   }

   public Location clone() {
      return new Location(this.position, this.yaw, this.pitch);
   }

   public String toString() {
      return "Location {[" + this.position.toString() + "], yaw: " + this.yaw + ", pitch: " + this.pitch + "}";
   }
}
