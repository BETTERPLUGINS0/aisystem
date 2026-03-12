package fr.xephi.authme.libs.org.postgresql.geometric;

import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PGtokenizer;
import java.io.Serializable;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGpolygon extends PGobject implements Serializable, Cloneable {
   @Nullable
   public PGpoint[] points;

   public PGpolygon(PGpoint[] points) {
      this();
      this.points = points;
   }

   public PGpolygon(String s) throws SQLException {
      this();
      this.setValue(s);
   }

   public PGpolygon() {
      this.type = "polygon";
   }

   public void setValue(@Nullable String s) throws SQLException {
      if (s == null) {
         this.points = null;
      } else {
         PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(s), ',');
         int npoints = t.getSize();
         PGpoint[] points = this.points;
         if (points == null || points.length != npoints) {
            this.points = points = new PGpoint[npoints];
         }

         for(int p = 0; p < npoints; ++p) {
            points[p] = new PGpoint(t.getToken(p));
         }

      }
   }

   public boolean equals(@Nullable Object obj) {
      if (obj instanceof PGpolygon) {
         PGpolygon p = (PGpolygon)obj;
         PGpoint[] points = this.points;
         PGpoint[] pPoints = p.points;
         if (points == null) {
            return pPoints == null;
         } else if (pPoints == null) {
            return false;
         } else if (pPoints.length != points.length) {
            return false;
         } else {
            for(int i = 0; i < points.length; ++i) {
               if (!points[i].equals(pPoints[i])) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int hash = 0;
      PGpoint[] points = this.points;
      if (points == null) {
         return hash;
      } else {
         for(int i = 0; i < points.length && i < 5; ++i) {
            hash = hash * 31 + points[i].hashCode();
         }

         return hash;
      }
   }

   public Object clone() throws CloneNotSupportedException {
      PGpolygon newPGpolygon = (PGpolygon)super.clone();
      if (newPGpolygon.points != null) {
         PGpoint[] newPoints = (PGpoint[])newPGpolygon.points.clone();
         newPGpolygon.points = newPoints;

         for(int i = 0; i < newPGpolygon.points.length; ++i) {
            if (newPGpolygon.points[i] != null) {
               newPoints[i] = (PGpoint)newPGpolygon.points[i].clone();
            }
         }
      }

      return newPGpolygon;
   }

   @Nullable
   public String getValue() {
      PGpoint[] points = this.points;
      if (points == null) {
         return null;
      } else {
         StringBuilder b = new StringBuilder();
         b.append("(");

         for(int p = 0; p < points.length; ++p) {
            if (p > 0) {
               b.append(",");
            }

            b.append(points[p].toString());
         }

         b.append(")");
         return b.toString();
      }
   }
}
