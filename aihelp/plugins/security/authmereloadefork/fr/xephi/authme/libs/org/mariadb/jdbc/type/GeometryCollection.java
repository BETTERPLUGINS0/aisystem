package fr.xephi.authme.libs.org.mariadb.jdbc.type;

import java.util.Arrays;

public class GeometryCollection implements Geometry {
   private final Geometry[] geometries;

   public GeometryCollection(Geometry[] geometries) {
      this.geometries = geometries;
   }

   public Geometry[] getGeometries() {
      return this.geometries;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("GEOMETRYCOLLECTION(");
      int indexpoly = 0;
      Geometry[] var3 = this.geometries;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Geometry geo = var3[var5];
         if (indexpoly++ > 0) {
            sb.append(",");
         }

         sb.append(geo.toString());
      }

      sb.append(")");
      return sb.toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o != null && o instanceof GeometryCollection ? this.toString().equals(o.toString()) : false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.geometries);
   }
}
