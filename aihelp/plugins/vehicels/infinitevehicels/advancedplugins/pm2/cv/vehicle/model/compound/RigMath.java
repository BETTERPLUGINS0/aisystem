package advancedplugins.pm2.cv.vehicle.model.compound;

import advancedplugins.pm2.cv.util.ConvertUtil;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

final class RigMath {
   static Vector3D mulProject(Vector3D vector3D, Matrix4f matrix4f) {
      return ConvertUtil.toVector3D(ConvertUtil.toVector3f(var0).mulProject(var1));
   }

   static void rotateXYZ(Quaternionf quaternion, double x, double y, double z) {
      var0.rotateXYZ((float)var1, (float)var3, (float)var5);
   }

   static void rotateXYZ(Quaternionf quaternion, Vector3D rotation) {
      rotateXYZ(var0, var1.getX(), var1.getY(), var1.getZ());
   }
}
