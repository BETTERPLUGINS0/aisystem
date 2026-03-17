package advancedplugins.pm2.cv.util.math;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class BoundingBoxUtil {
   public static Vector getCorner000(BoundingBox bounds) {
      return new Vector(var0.getMinX(), var0.getMinY(), var0.getMinZ());
   }

   public static Vector getCorner001(BoundingBox bounds) {
      return new Vector(var0.getMinX(), var0.getMinY(), var0.getMaxZ());
   }

   public static Vector getCorner010(BoundingBox bounds) {
      return new Vector(var0.getMinX(), var0.getMaxY(), var0.getMinZ());
   }

   public static Vector getCorner011(BoundingBox bounds) {
      return new Vector(var0.getMinX(), var0.getMaxY(), var0.getMaxZ());
   }

   public static Vector getCorner100(BoundingBox bounds) {
      return new Vector(var0.getMaxX(), var0.getMinY(), var0.getMinZ());
   }

   public static Vector getCorner101(BoundingBox bounds) {
      return new Vector(var0.getMaxX(), var0.getMinY(), var0.getMaxZ());
   }

   public static Vector getCorner110(BoundingBox bounds) {
      return new Vector(var0.getMaxX(), var0.getMaxY(), var0.getMinZ());
   }

   public static Vector getCorner111(BoundingBox bounds) {
      return new Vector(var0.getMaxX(), var0.getMaxY(), var0.getMaxZ());
   }

   public static Vector[] getCorners(BoundingBox bounds) {
      return new Vector[]{getCorner000(var0), getCorner001(var0), getCorner010(var0), getCorner011(var0), getCorner100(var0), getCorner101(var0), getCorner110(var0), getCorner111(var0)};
   }
}
