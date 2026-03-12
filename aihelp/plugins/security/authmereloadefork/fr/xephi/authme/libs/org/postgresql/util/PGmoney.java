package fr.xephi.authme.libs.org.postgresql.util;

import java.io.Serializable;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGmoney extends PGobject implements Serializable, Cloneable {
   public double val;
   public boolean isNull;

   public PGmoney(double value) {
      this();
      this.val = value;
   }

   public PGmoney(String value) throws SQLException {
      this();
      this.setValue(value);
   }

   public PGmoney() {
      this.type = "money";
   }

   public void setValue(@Nullable String s) throws SQLException {
      this.isNull = s == null;
      if (s != null) {
         try {
            boolean negative = s.charAt(0) == '(';
            String s1 = PGtokenizer.removePara(s).substring(1);

            for(int pos = s1.indexOf(44); pos != -1; pos = s1.indexOf(44)) {
               s1 = s1.substring(0, pos) + s1.substring(pos + 1);
            }

            this.val = Double.parseDouble(s1);
            this.val = negative ? -this.val : this.val;
         } catch (NumberFormatException var5) {
            throw new PSQLException(GT.tr("Conversion of money failed."), PSQLState.NUMERIC_CONSTANT_OUT_OF_RANGE, var5);
         }
      }
   }

   public int hashCode() {
      if (this.isNull) {
         return 0;
      } else {
         int prime = true;
         int result = super.hashCode();
         long temp = Double.doubleToLongBits(this.val);
         result = 31 * result + (int)(temp ^ temp >>> 32);
         return result;
      }
   }

   public boolean equals(@Nullable Object obj) {
      if (obj instanceof PGmoney) {
         PGmoney p = (PGmoney)obj;
         if (this.isNull) {
            return p.isNull;
         } else if (p.isNull) {
            return false;
         } else {
            return this.val == p.val;
         }
      } else {
         return false;
      }
   }

   @Nullable
   public String getValue() {
      if (this.isNull) {
         return null;
      } else {
         return this.val < 0.0D ? "-$" + -this.val : "$" + this.val;
      }
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
