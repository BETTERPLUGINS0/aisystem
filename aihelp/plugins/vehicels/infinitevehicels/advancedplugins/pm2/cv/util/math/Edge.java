package advancedplugins.pm2.cv.util.math;

import com.google.common.base.Preconditions;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;

public class Edge {
   private final Vector3D start;
   private final Vector3D end;

   public Edge(Vector3D start, Vector3D end) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      this.start = var1;
      this.end = var2;
   }

   public Vector3D getStart() {
      return this.start;
   }

   public Vector3D getEnd() {
      return this.end;
   }

   public boolean equals(Object other) {
      if (!(var1 instanceof Edge)) {
         return false;
      } else {
         Edge var2 = (Edge)var1;
         if (this.start.equals(var2.end) && this.end.equals(var2.start)) {
            return true;
         } else {
            return this.end.equals(var2.end) && this.start.equals(var2.start);
         }
      }
   }

   public int hashCode() {
      return this.start.hashCode() ^ this.end.hashCode();
   }

   public String toString() {
      String var10000 = String.valueOf(this.start);
      return "(" + var10000 + "," + String.valueOf(this.end) + ")";
   }

   public Triangle createTriangle(Vector3D vertex) {
      Preconditions.checkNotNull(var1);
      return new Triangle(this.start, this.end, var1);
   }

   public Triangle createTriangle2(Vector3D vertex) {
      Preconditions.checkNotNull(var1);
      return new Triangle(this.start, var1, this.end);
   }
}
