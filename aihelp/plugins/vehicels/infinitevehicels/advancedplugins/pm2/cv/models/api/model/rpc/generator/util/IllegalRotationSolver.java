package advancedplugins.pm2.cv.models.api.model.rpc.generator.util;

import advancedplugins.pm2.cv.models.api.utils.data.Triple;
import advancedplugins.pm2.cv.models.api.utils.math.Axis;
import advancedplugins.pm2.cv.models.api.utils.math.Direction;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.processed.ProcessedJoint;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class IllegalRotationSolver {
   private static final List<Vector3d> AXES;

   public static ProcessedJoint.Cube solve(ProcessedJoint.Cube var0) {
      if (isLegal(var0)) {
         return var0;
      } else {
         Quaterniond var1 = var0.getQuaternion();
         ConcurrentHashMap var2 = new ConcurrentHashMap();
         Iterator var3 = var0.getFaces().entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            Direction var5 = (Direction)var4.getKey();
            ProcessedJoint.Face var6 = (ProcessedJoint.Face)var4.getValue();
            var2.put(var5, new Triple(var5.getNormal().rotate(var1), var5.getUvUp().rotate(var1), var6));
         }

         List var24 = var0.getCorners();
         var24.forEach((var2x) -> {
            var2x.sub(var0.getOrigin()).rotate(var1).add(var0.getOrigin());
         });
         Vector3d var25 = (new Vector3d(0.0D, 0.0D, 1.0D)).rotate(var1);
         Vector3d var26 = closestAxis(var25);
         double var27 = var25.dot(var26);
         Vector3d var8;
         Quaterniond var9;
         if (MathUtils.isSimilar(var27, 1.0D)) {
            var25 = (new Vector3d(0.0D, 1.0D, 0.0D)).rotate(var1);
            var26 = closestAxis(var25);
            var27 = var25.dot(var26);
            if (MathUtils.isSimilar(var27, 1.0D)) {
               var9 = new Quaterniond();
            } else {
               var8 = var25.cross(var26, new Vector3d()).normalize();
               var9 = (new Quaterniond()).rotateAxis(Math.acos(var27), var8);
            }
         } else {
            var8 = var25.cross(var26, new Vector3d()).normalize();
            var9 = (new Quaterniond()).rotateAxis(Math.acos(var27), var8);
         }

         ConcurrentHashMap var10 = new ConcurrentHashMap();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Triple var13 = (Triple)var12.getValue();
            Vector3d var14 = ((Vector3d)var13.getFirst()).rotate(var9);
            Direction var15 = Direction.fromNormal(var14);
            if (var15 != null) {
               Vector3d var16 = var15.getUvUp();
               Vector3d var17 = ((Vector3d)var13.getSecond()).rotate(var9);
               int var18 = 0;
               double var19 = var17.dot(var16);
               if (MathUtils.isSimilar(var19, -1.0D)) {
                  var18 = 180;
               } else if (MathUtils.isSimilar(var19, 0.0D)) {
                  var8 = var17.cross(var16, new Vector3d());
                  var18 = var8.dot(var14) > 0.0D ? 90 : 270;
               }

               ProcessedJoint.Face var21 = (ProcessedJoint.Face)var13.getThird();
               ProcessedJoint.UV var22 = var21.uv();
               ProcessedJoint.Face var23 = new ProcessedJoint.Face(new ProcessedJoint.UV(var22.u1(), var22.v1(), var22.u2(), var22.v2(), (var22.rotation() + var18) % 360), var21.texture());
               var10.put(var15, var23);
            }
         }

         Vector3d var28 = new Vector3d(2.147483647E9D);
         Vector3d var29 = new Vector3d(-2.147483648E9D);
         var24.forEach((var4x) -> {
            Vector3d var5 = var4x.sub(var0.getOrigin()).rotate(var9).add(var0.getOrigin());
            var28.set(Math.min(var28.x, var5.x), Math.min(var28.y, var5.y), Math.min(var28.z, var5.z));
            var29.set(Math.max(var29.x, var5.x), Math.max(var29.y, var5.y), Math.max(var29.z, var5.z));
         });
         Vector3d var30 = MathUtils.fixEuler(MathUtils.toEulerZYX(var9.invert()));
         return new ProcessedJoint.Cube(var0.getName(), var0.getOrigin(), var30, var28, var29, var10, var0.getInflate());
      }
   }

   private static Vector3d closestAxis(Vector3d var0) {
      if (MathUtils.isSimilar(var0.lengthSquared(), 0.0D)) {
         return new Vector3d((Vector3dc)AXES.get(2));
      } else {
         Vector3d var1 = null;
         double var2 = 0.0D;
         Iterator var4 = AXES.iterator();

         while(var4.hasNext()) {
            Vector3d var5 = (Vector3d)var4.next();
            double var6 = Math.abs(var0.dot(var5));
            if (var6 > var2) {
               var2 = var6;
               var1 = var5;
            }
         }

         if (var1 == null) {
            return new Vector3d((Vector3dc)AXES.get(2));
         } else {
            return var1.dot(var0) > 0.0D ? new Vector3d(var1) : var1.negate(new Vector3d());
         }
      }
   }

   private static boolean isLegal(ProcessedJoint.Cube var0) {
      Vector3d var1 = var0.getRotation();
      return MathUtils.isAlmostBetween(var1.x, -45.0D, 45.0D) && MathUtils.isInterval(var1.x, 22.5D) && MathUtils.isSimilar(var1.y, 0.0D) && MathUtils.isSimilar(var1.z, 0.0D) || MathUtils.isAlmostBetween(var1.y, -45.0D, 45.0D) && MathUtils.isInterval(var1.y, 22.5D) && MathUtils.isSimilar(var1.x, 0.0D) && MathUtils.isSimilar(var1.z, 0.0D) || MathUtils.isAlmostBetween(var1.z, -45.0D, 45.0D) && MathUtils.isInterval(var1.z, 22.5D) && MathUtils.isSimilar(var1.x, 0.0D) && MathUtils.isSimilar(var1.y, 0.0D);
   }

   static {
      AXES = List.of(Axis.X.getVector(), Axis.Y.getVector(), Axis.Z.getVector());
   }
}
