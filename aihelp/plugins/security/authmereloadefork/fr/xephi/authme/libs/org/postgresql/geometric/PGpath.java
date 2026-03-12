package fr.xephi.authme.libs.org.postgresql.geometric;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PGtokenizer;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.Serializable;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGpath extends PGobject implements Serializable, Cloneable {
   public boolean open;
   @Nullable
   public PGpoint[] points;

   public PGpath(@Nullable PGpoint[] points, boolean open) {
      this();
      this.points = points;
      this.open = open;
   }

   public PGpath() {
      this.type = "path";
   }

   public PGpath(String s) throws SQLException {
      this();
      this.setValue(s);
   }

   public void setValue(@Nullable String s) throws SQLException {
      if (s == null) {
         this.points = null;
      } else {
         if (s.startsWith("[") && s.endsWith("]")) {
            this.open = true;
            s = PGtokenizer.removeBox(s);
         } else {
            if (!s.startsWith("(") || !s.endsWith(")")) {
               throw new PSQLException(GT.tr("Cannot tell if path is open or closed: {0}.", s), PSQLState.DATA_TYPE_MISMATCH);
            }

            this.open = false;
            s = PGtokenizer.removePara(s);
         }

         PGtokenizer t = new PGtokenizer(s, ',');
         int npoints = t.getSize();
         PGpoint[] points = new PGpoint[npoints];
         this.points = points;

         for(int p = 0; p < npoints; ++p) {
            points[p] = new PGpoint(t.getToken(p));
         }

      }
   }

   public boolean equals(@Nullable Object obj) {
      if (obj instanceof PGpath) {
         PGpath p = (PGpath)obj;
         PGpoint[] points = this.points;
         PGpoint[] pPoints = p.points;
         if (points == null) {
            return pPoints == null;
         } else if (pPoints == null) {
            return false;
         } else if (p.open != this.open) {
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
      PGpoint[] points = this.points;
      if (points == null) {
         return 0;
      } else {
         int hash = this.open ? 1231 : 1237;

         for(int i = 0; i < points.length && i < 5; ++i) {
            hash = hash * 31 + points[i].hashCode();
         }

         return hash;
      }
   }

   public Object clone() throws CloneNotSupportedException {
      PGpath newPGpath = (PGpath)super.clone();
      if (newPGpath.points != null) {
         PGpoint[] newPoints = (PGpoint[])newPGpath.points.clone();
         newPGpath.points = newPoints;

         for(int i = 0; i < newPGpath.points.length; ++i) {
            newPoints[i] = (PGpoint)newPGpath.points[i].clone();
         }
      }

      return newPGpath;
   }

   @Nullable
   public String getValue() {
      PGpoint[] points = this.points;
      if (points == null) {
         return null;
      } else {
         StringBuilder b = new StringBuilder(this.open ? "[" : "(");

         for(int p = 0; p < points.length; ++p) {
            if (p > 0) {
               b.append(",");
            }

            b.append(points[p].toString());
         }

         b.append(this.open ? "]" : ")");
         return b.toString();
      }
   }

   public boolean isOpen() {
      return this.open && this.points != null;
   }

   public boolean isClosed() {
      return !this.open && this.points != null;
   }

   public void closePath() {
      this.open = false;
   }

   public void openPath() {
      this.open = true;
   }
}
