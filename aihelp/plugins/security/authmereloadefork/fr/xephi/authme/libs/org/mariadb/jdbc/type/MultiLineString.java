package fr.xephi.authme.libs.org.mariadb.jdbc.type;

import java.util.Arrays;

public class MultiLineString implements Geometry {
   private final LineString[] lines;

   public MultiLineString(LineString[] lines) {
      this.lines = lines;
   }

   public LineString[] getLines() {
      return this.lines;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("MULTILINESTRING(");
      int indexLine = 0;
      LineString[] var3 = this.lines;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         LineString ls = var3[var5];
         if (indexLine++ > 0) {
            sb.append(",");
         }

         sb.append("(");
         int index = 0;
         Point[] var8 = ls.getPoints();
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Point pt = var8[var10];
            if (index++ > 0) {
               sb.append(",");
            }

            sb.append(pt.getX()).append(" ").append(pt.getY());
         }

         sb.append(")");
      }

      sb.append(")");
      return sb.toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o != null && o instanceof MultiLineString ? this.toString().equals(o.toString()) : false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.lines);
   }
}
