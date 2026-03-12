package fr.xephi.authme.libs.org.postgresql.geometric;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PGBinaryObject;
import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PGtokenizer;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.Serializable;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGbox extends PGobject implements PGBinaryObject, Serializable, Cloneable {
   @Nullable
   public PGpoint[] point;

   public PGbox(double x1, double y1, double x2, double y2) {
      this(new PGpoint(x1, y1), new PGpoint(x2, y2));
   }

   public PGbox(PGpoint p1, PGpoint p2) {
      this();
      this.point = new PGpoint[]{p1, p2};
   }

   public PGbox(String s) throws SQLException {
      this();
      this.setValue(s);
   }

   public PGbox() {
      this.type = "box";
   }

   public void setValue(@Nullable String value) throws SQLException {
      if (value == null) {
         this.point = null;
      } else {
         PGtokenizer t = new PGtokenizer(value, ',');
         if (t.getSize() != 2) {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", this.type, value), PSQLState.DATA_TYPE_MISMATCH);
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

   public void setByteValue(byte[] b, int offset) {
      PGpoint[] point = this.point;
      if (point == null) {
         this.point = point = new PGpoint[2];
      }

      point[0] = new PGpoint();
      point[0].setByteValue(b, offset);
      point[1] = new PGpoint();
      point[1].setByteValue(b, offset + point[0].lengthInBytes());
      this.point = point;
   }

   public boolean equals(@Nullable Object obj) {
      if (obj instanceof PGbox) {
         PGbox p = (PGbox)obj;
         PGpoint[] point = this.point;
         PGpoint[] pPoint = p.point;
         if (point == null) {
            return pPoint == null;
         }

         if (pPoint == null) {
            return false;
         }

         if (pPoint[0].equals(point[0]) && pPoint[1].equals(point[1])) {
            return true;
         }

         if (pPoint[0].equals(point[1]) && pPoint[1].equals(point[0])) {
            return true;
         }

         if (pPoint[0].x == point[0].x && pPoint[0].y == point[1].y && pPoint[1].x == point[1].x && pPoint[1].y == point[0].y) {
            return true;
         }

         if (pPoint[0].x == point[1].x && pPoint[0].y == point[0].y && pPoint[1].x == point[0].x && pPoint[1].y == point[1].y) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      PGpoint[] point = this.point;
      return point == null ? 0 : point[0].hashCode() ^ point[1].hashCode();
   }

   public Object clone() throws CloneNotSupportedException {
      PGbox newPGbox = (PGbox)super.clone();
      if (newPGbox.point != null) {
         newPGbox.point = (PGpoint[])newPGbox.point.clone();

         for(int i = 0; i < newPGbox.point.length; ++i) {
            if (newPGbox.point[i] != null) {
               newPGbox.point[i] = (PGpoint)newPGbox.point[i].clone();
            }
         }
      }

      return newPGbox;
   }

   @Nullable
   public String getValue() {
      PGpoint[] point = this.point;
      return point == null ? null : point[0].toString() + "," + point[1].toString();
   }

   public int lengthInBytes() {
      PGpoint[] point = this.point;
      return point == null ? 0 : point[0].lengthInBytes() + point[1].lengthInBytes();
   }

   public void toBytes(byte[] bytes, int offset) {
      PGpoint[] point = (PGpoint[])Nullness.castNonNull(this.point);
      point[0].toBytes(bytes, offset);
      point[1].toBytes(bytes, offset + point[0].lengthInBytes());
   }
}
