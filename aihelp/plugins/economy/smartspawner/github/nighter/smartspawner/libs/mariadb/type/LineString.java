package github.nighter.smartspawner.libs.mariadb.type;

import java.util.Arrays;
import java.util.Objects;

public class LineString implements Geometry {
   private final Point[] points;
   private final boolean open;

   public LineString(Point[] points, boolean open) {
      this.points = points;
      this.open = open;
   }

   public Point[] getPoints() {
      return this.points;
   }

   public boolean isOpen() {
      return this.open;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("LINESTRING(");
      int index = 0;
      Point[] var3 = this.points;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Point pt = var3[var5];
         if (index++ > 0) {
            sb.append(",");
         }

         sb.append(pt.getX()).append(" ").append(pt.getY());
      }

      sb.append(")");
      return sb.toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && o instanceof LineString) {
         return this.open == ((LineString)o).isOpen() && this.toString().equals(o.toString());
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = Objects.hash(new Object[]{this.open});
      result = 31 * result + Arrays.hashCode(this.points);
      return result;
   }
}
