package fr.xephi.authme.libs.org.mariadb.jdbc.type;

import java.util.Arrays;

public class MultiPoint implements Geometry {
   private final Point[] points;

   public MultiPoint(Point[] points) {
      this.points = points;
   }

   public Point[] getPoints() {
      return this.points;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("MULTIPOINT(");
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
      } else {
         return o != null && o instanceof MultiPoint ? this.toString().equals(o.toString()) : false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.points);
   }
}
