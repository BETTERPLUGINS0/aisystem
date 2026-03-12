package fr.xephi.authme.libs.org.postgresql.geometric;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PGtokenizer;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.Serializable;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGlseg extends PGobject implements Serializable, Cloneable {
   @Nullable
   public PGpoint[] point;

   public PGlseg(double x1, double y1, double x2, double y2) {
      this(new PGpoint(x1, y1), new PGpoint(x2, y2));
   }

   public PGlseg(PGpoint p1, PGpoint p2) {
      this();
      this.point = new PGpoint[]{p1, p2};
   }

   public PGlseg(String s) throws SQLException {
      this();
      this.setValue(s);
   }

   public PGlseg() {
      this.type = "lseg";
   }

   public void setValue(@Nullable String s) throws SQLException {
      if (s == null) {
         this.point = null;
      } else {
         PGtokenizer t = new PGtokenizer(PGtokenizer.removeBox(s), ',');
         if (t.getSize() != 2) {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", this.type, s), PSQLState.DATA_TYPE_MISMATCH);
         } else {
            PGpoint[] point = this.point;
            if (point == null) {
               this.point = point = new PGpoint[2];
            }

            point[0] = new PGpoint(t.getToken(0));
            point[1] = new PGpoint(t.getToken(1));
         }
      }
   }

   public boolean equals(@Nullable Object obj) {
      if (!(obj instanceof PGlseg)) {
         return false;
      } else {
         PGlseg p = (PGlseg)obj;
         PGpoint[] point = this.point;
         PGpoint[] pPoint = p.point;
         if (point == null) {
            return pPoint == null;
         } else if (pPoint == null) {
            return false;
         } else {
            return pPoint[0].equals(point[0]) && pPoint[1].equals(point[1]) || pPoint[0].equals(point[1]) && pPoint[1].equals(point[0]);
         }
      }
   }

   public int hashCode() {
      PGpoint[] point = this.point;
      return point == null ? 0 : point[0].hashCode() ^ point[1].hashCode();
   }

   public Object clone() throws CloneNotSupportedException {
      PGlseg newPGlseg = (PGlseg)super.clone();
      if (newPGlseg.point != null) {
         newPGlseg.point = (PGpoint[])newPGlseg.point.clone();

         for(int i = 0; i < newPGlseg.point.length; ++i) {
            if (newPGlseg.point[i] != null) {
               newPGlseg.point[i] = (PGpoint)newPGlseg.point[i].clone();
            }
         }
      }

      return newPGlseg;
   }

   @Nullable
   public String getValue() {
      PGpoint[] point = this.point;
      return point == null ? null : "[" + point[0] + "," + point[1] + "]";
   }
}
