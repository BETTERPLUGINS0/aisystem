package fr.xephi.authme.libs.org.mariadb.jdbc.type;

import java.util.Objects;

public class Point implements Geometry {
   private final double x;
   private final double y;

   public Point(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public String toString() {
      return "POINT(" + this.x + " " + this.y + ")";
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o != null && o instanceof Point ? this.toString().equals(o.toString()) : false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.x, this.y});
   }
}
