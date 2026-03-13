package github.nighter.smartspawner.libs.mariadb.type;

import java.util.Arrays;

public class MultiPolygon implements Geometry {
   private final Polygon[] polygons;

   public MultiPolygon(Polygon[] polygons) {
      this.polygons = polygons;
   }

   public Polygon[] getPolygons() {
      return this.polygons;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("MULTIPOLYGON(");
      int indexpoly = 0;
      Polygon[] var3 = this.polygons;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Polygon poly = var3[var5];
         if (indexpoly++ > 0) {
            sb.append(",");
         }

         sb.append("(");
         int indexLine = 0;
         LineString[] var8 = poly.getLines();
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            LineString ls = var8[var10];
            if (indexLine++ > 0) {
               sb.append(",");
            }

            sb.append("(");
            int index = 0;
            Point[] var13 = ls.getPoints();
            int var14 = var13.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               Point pt = var13[var15];
               if (index++ > 0) {
                  sb.append(",");
               }

               sb.append(pt.getX()).append(" ").append(pt.getY());
            }

            sb.append(")");
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
         return o != null && o instanceof MultiPolygon ? this.toString().equals(o.toString()) : false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.polygons);
   }
}
