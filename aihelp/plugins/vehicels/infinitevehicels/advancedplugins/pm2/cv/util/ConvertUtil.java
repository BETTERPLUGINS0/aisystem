package advancedplugins.pm2.cv.util;

import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class ConvertUtil {
   public static Quaternionf toQuaternionf(Quaterniond quaterniond) {
      return new Quaternionf(var0);
   }

   public static Vector toVector(Vector3D vector3D) {
      return new Vector(var0.getX(), var0.getY(), var0.getZ());
   }

   public static Vector toVector(Vector3f vector3f) {
      return new Vector(var0.x, var0.y, var0.z);
   }

   public static Vector3D toVector3D(Vector vector) {
      return new Vector3D(var0.getX(), var0.getY(), var0.getZ());
   }

   public static Vector3D toVector3D(Location location) {
      return new Vector3D(var0.getX(), var0.getY(), var0.getZ());
   }

   public static Vector3f toVector3f(Vector3D vector3D) {
      return new Vector3f((float)var0.getX(), (float)var0.getY(), (float)var0.getZ());
   }

   public static Vector3f toVector3f(Location location) {
      return new Vector3f((float)var0.getX(), (float)var0.getY(), (float)var0.getZ());
   }

   public static Vector3D toVector3D(Vector3f vector) {
      return var0 == null ? new Vector3D(0.0D, 0.0D, 0.0D) : new Vector3D((double)var0.x, (double)var0.y, (double)var0.z);
   }

   public static Vector3D toVector3D(Vector3d vector) {
      return new Vector3D(var0.x, var0.y, var0.z);
   }

   public static Location toLocation(Vector3D vector3D, World world) {
      return new Location(var1, var0.getX(), var0.getY(), var0.getZ());
   }

   public static Location toLocation(Vector3D vector3d, World world, float yaw, float pitch) {
      return new Location(var1, var0.getX(), var0.getY(), var0.getZ(), var2, var3);
   }

   public static Location toLocation(Vector3f vector3f, World world, float yaw, float pitch) {
      return new Location(var1, (double)var0.x, (double)var0.y, (double)var0.z, var2, var3);
   }

   public static Location toLocation(Vector3f vector3f, World world) {
      return new Location(var1, (double)var0.x, (double)var0.y, (double)var0.z);
   }
}
