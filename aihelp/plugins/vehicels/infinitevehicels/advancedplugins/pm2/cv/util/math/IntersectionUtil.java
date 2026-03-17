package advancedplugins.pm2.cv.util.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public final class IntersectionUtil {
   public static boolean isPointInTriangle(Vector3D point, Vector3D t1, Vector3D t2, Vector3D t3) {
      Vector3D var4 = var1.subtract(var0);
      Vector3D var5 = var2.subtract(var0);
      Vector3D var6 = var3.subtract(var0);
      double var7 = var4.dotProduct(var5);
      double var9 = var4.dotProduct(var6);
      double var11 = var5.dotProduct(var6);
      double var13 = var6.dotProduct(var6);
      if (var11 * var9 - var13 * var7 < 0.0D) {
         return false;
      } else {
         double var15 = var5.dotProduct(var5);
         return !(var7 * var11 - var9 * var15 < 0.0D);
      }
   }

   public static Vector3D intersectRayTriangle(Ray ray, Vector3D t1, Vector3D t2, Vector3D t3) {
      Vector3D var4 = var2.subtract(var1);
      Vector3D var5 = var3.subtract(var1);
      Vector3D var6 = var0.direction.crossProduct(var5);
      double var7 = var4.dotProduct(var6);
      if (FloatRoundingUtil.isZero(var7)) {
         Plane var17 = new Plane();
         var17.set(var1, var2, var3);
         return var17.testPoint(var0.origin) == Plane.PlaneSide.OnPlane && isPointInTriangle(var0.origin, var1, var2, var3) ? var0.origin : null;
      } else {
         var7 = 1.0D / var7;
         Vector3D var9 = var0.origin.subtract(var1);
         double var10 = var9.dotProduct(var6) * var7;
         if (!(var10 < 0.0D) && !(var10 > 1.0D)) {
            Vector3D var12 = var9.crossProduct(var4);
            double var13 = var0.direction.dotProduct(var12) * var7;
            if (!(var13 < 0.0D) && !(var10 + var13 > 1.0D)) {
               double var15 = var5.dotProduct(var12) * var7;
               if (var15 < 0.0D) {
                  return null;
               } else {
                  return var15 <= 9.999999974752427E-7D ? var0.origin : var0.getEndPoint(var15);
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public static Vector3D intersectRayTriangles(Ray ray, List<Vector3D> triangles) {
      Vector3D var2 = null;
      double var3 = 3.4028234663852886E38D;
      if (var1.size() % 3 != 0) {
         throw new RuntimeException("triangle list size is not a multiple of 3");
      } else {
         for(int var5 = 0; var5 < var1.size(); var5 += 3) {
            Vector3D var6 = intersectRayTriangle(var0, (Vector3D)var1.get(var5), (Vector3D)var1.get(var5 + 1), (Vector3D)var1.get(var5 + 2));
            if (var6 != null) {
               double var7 = var0.origin.distanceSq(var6);
               if (var7 < var3) {
                  var3 = var7;
                  var2 = var6;
               }
            }
         }

         return var2;
      }
   }

   public static Vector3D intersectRayConvexPolyhedralBounds(Ray ray, ConvexPolyhedralBounds bounds) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.getTriangles().iterator();

      while(var3.hasNext()) {
         Triangle var4 = (Triangle)var3.next();
         var2.add(var4.getVertex(0));
         var2.add(var4.getVertex(1));
         var2.add(var4.getVertex(2));
      }

      return intersectRayTriangles(var0, var2);
   }
}
