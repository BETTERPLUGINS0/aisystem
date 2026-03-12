package fr.xephi.authme.libs.org.postgresql.geometric;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PGtokenizer;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.Serializable;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGline extends PGobject implements Serializable, Cloneable {
   public double a;
   public double b;
   public double c;
   private boolean isNull;

   public PGline(double a, double b, double c) {
      this();
      this.a = a;
      this.b = b;
      this.c = c;
   }

   public PGline(double x1, double y1, double x2, double y2) {
      this();
      this.setValue(x1, y1, x2, y2);
   }

   public PGline(@Nullable PGpoint p1, @Nullable PGpoint p2) {
      this();
      this.setValue(p1, p2);
   }

   public PGline(@Nullable PGlseg lseg) {
      this();
      if (lseg == null) {
         this.isNull = true;
      } else {
         PGpoint[] point = lseg.point;
         if (point == null) {
            this.isNull = true;
         } else {
            this.setValue(point[0], point[1]);
         }
      }
   }

   private void setValue(@Nullable PGpoint p1, @Nullable PGpoint p2) {
      if (p1 != null && p2 != null) {
         this.setValue(p1.x, p1.y, p2.x, p2.y);
      } else {
         this.isNull = true;
      }

   }

   private void setValue(double x1, double y1, double x2, double y2) {
      if (x1 == x2) {
         this.a = -1.0D;
         this.b = 0.0D;
      } else {
         this.a = (y2 - y1) / (x2 - x1);
         this.b = -1.0D;
      }

      this.c = y1 - this.a * x1;
   }

   public PGline(String s) throws SQLException {
      this();
      this.setValue(s);
   }

   public PGline() {
      this.type = "line";
   }

   public void setValue(@Nullable String s) throws SQLException {
      this.isNull = s == null;
      if (s != null) {
         PGtokenizer t;
         if (s.trim().startsWith("{")) {
            t = new PGtokenizer(PGtokenizer.removeCurlyBrace(s), ',');
            if (t.getSize() != 3) {
               throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", this.type, s), PSQLState.DATA_TYPE_MISMATCH);
            }

            this.a = Double.parseDouble(t.getToken(0));
            this.b = Double.parseDouble(t.getToken(1));
            this.c = Double.parseDouble(t.getToken(2));
         } else if (s.trim().startsWith("[")) {
            t = new PGtokenizer(PGtokenizer.removeBox(s), ',');
            if (t.getSize() != 2) {
               throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", this.type, s), PSQLState.DATA_TYPE_MISMATCH);
            }

            PGpoint point1 = new PGpoint(t.getToken(0));
            PGpoint point2 = new PGpoint(t.getToken(1));
            this.a = point2.x - point1.x;
            this.b = point2.y - point1.y;
            this.c = point1.y;
         }

      }
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         if (!super.equals(obj)) {
            return false;
         } else {
            PGline pGline = (PGline)obj;
            if (this.isNull) {
               return pGline.isNull;
            } else if (pGline.isNull) {
               return false;
            } else {
               return Double.compare(pGline.a, this.a) == 0 && Double.compare(pGline.b, this.b) == 0 && Double.compare(pGline.c, this.c) == 0;
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.isNull) {
         return 0;
      } else {
         int result = super.hashCode();
         long temp = Double.doubleToLongBits(this.a);
         result = 31 * result + (int)(temp ^ temp >>> 32);
         temp = Double.doubleToLongBits(this.b);
         result = 31 * result + (int)(temp ^ temp >>> 32);
         temp = Double.doubleToLongBits(this.c);
         result = 31 * result + (int)(temp ^ temp >>> 32);
         return result;
      }
   }

   @Nullable
   public String getValue() {
      return this.isNull ? null : "{" + this.a + "," + this.b + "," + this.c + "}";
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
