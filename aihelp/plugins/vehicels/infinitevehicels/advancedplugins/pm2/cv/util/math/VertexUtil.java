package advancedplugins.pm2.cv.util.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public final class VertexUtil {
   public static List<Vector3D> rotateVerticesAroundPivot(List<Vector3D> vertices, Vector3D pivot, Matrix4... rotations) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Vector3D var5 = (Vector3D)var4.next();
         Vector3D var6 = var5;
         Vector3D var7 = var1;
         Matrix4[] var8 = var2;
         int var9 = var2.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Matrix4 var11 = var8[var10];
            var6 = TransformUtil.transform(var6, var11);
            var7 = TransformUtil.transform(var7, var11);
         }

         var3.add(var1.add(var6.subtract(var7)));
      }

      return var3;
   }

   public static List<Vector3D> rotateVerticesAroundPivot(List<Vector3D> vertices, Vector3D pivot, List<Matrix4> rotations) {
      return rotateVerticesAroundPivot(var0, var1, (Matrix4[])var2.toArray(new Matrix4[0]));
   }
}
