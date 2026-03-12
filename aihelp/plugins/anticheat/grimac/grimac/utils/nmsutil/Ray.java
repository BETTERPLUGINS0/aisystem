package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.math.Vector3dm;
import lombok.Generated;

public class Ray implements Cloneable {
   private final Vector3dm origin;
   private final Vector3dm direction;

   public Ray(Vector3dm origin, Vector3dm direction) {
      this.origin = origin;
      this.direction = direction;
   }

   public Ray(GrimPlayer player, double x, double y, double z, float xRot, float yRot) {
      this.origin = new Vector3dm(x, y, z);
      this.direction = calculateDirection(player, xRot, yRot);
   }

   public static Vector3dm calculateDirection(GrimPlayer player, float xRot, float yRot) {
      Vector3dm vector = new Vector3dm();
      float rotX = (float)Math.toRadians((double)xRot);
      float rotY = (float)Math.toRadians((double)yRot);
      vector.setY(-player.trigHandler.sin(rotY));
      double xz = (double)player.trigHandler.cos(rotY);
      vector.setX(-xz * (double)player.trigHandler.sin(rotX));
      vector.setZ(xz * (double)player.trigHandler.cos(rotX));
      return vector;
   }

   public Ray clone() {
      return new Ray(this.origin.clone(), this.direction.clone());
   }

   public String toString() {
      String var10000 = String.valueOf(this.origin);
      return "origin: " + var10000 + " direction: " + String.valueOf(this.direction);
   }

   public Vector3dm getPointAtDistance(double distance) {
      Vector3dm dir = new Vector3dm(this.direction.getX(), this.direction.getY(), this.direction.getZ());
      Vector3dm orig = new Vector3dm(this.origin.getX(), this.origin.getY(), this.origin.getZ());
      return orig.add(dir.multiply(distance));
   }

   public Pair<Vector3dm, Vector3dm> closestPointsBetweenLines(Ray other) {
      Vector3dm n1 = this.direction.clone().crossProduct(other.direction.clone().crossProduct(this.direction));
      Vector3dm n2 = other.direction.clone().crossProduct(this.direction.clone().crossProduct(other.direction));
      Vector3dm c1 = this.origin.clone().add(this.direction.clone().multiply(other.origin.clone().subtract(this.origin).dot(n2) / this.direction.dot(n2)));
      Vector3dm c2 = other.origin.clone().add(other.direction.clone().multiply(this.origin.clone().subtract(other.origin).dot(n1) / other.direction.dot(n1)));
      return new Pair(c1, c2);
   }

   @Generated
   public Vector3dm getOrigin() {
      return this.origin;
   }

   @Generated
   public Vector3dm getDirection() {
      return this.direction;
   }
}
