package fr.xephi.authme.libs.org.mariadb.jdbc;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Locale;

public class CallableParameterMetaData implements java.sql.ParameterMetaData {
   private final ResultSet rs;
   private final int parameterCount;
   private final boolean isFunction;

   public CallableParameterMetaData(ResultSet rs, boolean isFunction) throws SQLException {
      this.rs = rs;

      int count;
      for(count = 0; rs.next(); ++count) {
      }

      this.parameterCount = count;
      this.isFunction = isFunction;
   }

   public int getParameterCount() {
      return this.parameterCount;
   }

   public int isNullable(int index) throws SQLException {
      this.setIndex(index);
      return 2;
   }

   private void setIndex(int index) throws SQLException {
      if (index >= 1 && index <= this.parameterCount) {
         this.rs.absolute(index);
      } else {
         throw new SQLException("invalid parameter index " + index);
      }
   }

   public boolean isSigned(int index) throws SQLException {
      this.setIndex(index);
      String paramDetail = this.rs.getString("DTD_IDENTIFIER");
      return !paramDetail.contains(" unsigned");
   }

   public int getPrecision(int index) throws SQLException {
      this.setIndex(index);
      int characterMaxLength = this.rs.getInt("CHARACTER_MAXIMUM_LENGTH");
      int numericPrecision = this.rs.getInt("NUMERIC_PRECISION");
      return numericPrecision > 0 ? numericPrecision : characterMaxLength;
   }

   public int getScale(int index) throws SQLException {
      this.setIndex(index);
      return this.rs.getInt("NUMERIC_SCALE");
   }

   public String getParameterName(int index) throws SQLException {
      this.setIndex(index);
      return this.rs.getString("PARAMETER_NAME");
   }

   public int getParameterType(int index) throws SQLException {
      this.setIndex(index);
      String str = this.rs.getString("DATA_TYPE").toUpperCase(Locale.ROOT);
      byte var4 = -1;
      switch(str.hashCode()) {
      case -2034720975:
         if (str.equals("DECIMAL")) {
            var4 = 13;
         }
         break;
      case -1783518776:
         if (str.equals("VARBINARY")) {
            var4 = 24;
         }
         break;
      case -1718637701:
         if (str.equals("DATETIME")) {
            var4 = 22;
         }
         break;
      case -1666320270:
         if (str.equals("GEOMETRY")) {
            var4 = 29;
         }
         break;
      case -1618932450:
         if (str.equals("INTEGER")) {
            var4 = 7;
         }
         break;
      case -1453246218:
         if (str.equals("TIMESTAMP")) {
            var4 = 21;
         }
         break;
      case -1291368423:
         if (str.equals("LONGBLOB")) {
            var4 = 28;
         }
         break;
      case -1290838615:
         if (str.equals("LONGTEXT")) {
            var4 = 32;
         }
         break;
      case -1285035886:
         if (str.equals("MEDIUMBLOB")) {
            var4 = 27;
         }
         break;
      case -1284506078:
         if (str.equals("MEDIUMTEXT")) {
            var4 = 31;
         }
         break;
      case -1247219043:
         if (str.equals("TINYBLOB")) {
            var4 = 25;
         }
         break;
      case -1246689235:
         if (str.equals("TINYTEXT")) {
            var4 = 17;
         }
         break;
      case -594415409:
         if (str.equals("TINYINT")) {
            var4 = 1;
         }
         break;
      case 65773:
         if (str.equals("BIT")) {
            var4 = 0;
         }
         break;
      case 72655:
         if (str.equals("INT")) {
            var4 = 5;
         }
         break;
      case 81986:
         if (str.equals("SET")) {
            var4 = 18;
         }
         break;
      case 2041757:
         if (str.equals("BLOB")) {
            var4 = 26;
         }
         break;
      case 2067286:
         if (str.equals("CHAR")) {
            var4 = 14;
         }
         break;
      case 2090926:
         if (str.equals("DATE")) {
            var4 = 19;
         }
         break;
      case 2133249:
         if (str.equals("ENUM")) {
            var4 = 16;
         }
         break;
      case 2342524:
         if (str.equals("LONG")) {
            var4 = 8;
         }
         break;
      case 2511262:
         if (str.equals("REAL")) {
            var4 = 10;
         }
         break;
      case 2571565:
         if (str.equals("TEXT")) {
            var4 = 30;
         }
         break;
      case 2575053:
         if (str.equals("TIME")) {
            var4 = 20;
         }
         break;
      case 2719805:
         if (str.equals("YEAR")) {
            var4 = 3;
         }
         break;
      case 66988604:
         if (str.equals("FLOAT")) {
            var4 = 12;
         }
         break;
      case 69823057:
         if (str.equals("INT24")) {
            var4 = 6;
         }
         break;
      case 176095624:
         if (str.equals("SMALLINT")) {
            var4 = 2;
         }
         break;
      case 651290682:
         if (str.equals("MEDIUMINT")) {
            var4 = 4;
         }
         break;
      case 954596061:
         if (str.equals("VARCHAR")) {
            var4 = 15;
         }
         break;
      case 1959128815:
         if (str.equals("BIGINT")) {
            var4 = 9;
         }
         break;
      case 1959329793:
         if (str.equals("BINARY")) {
            var4 = 23;
         }
         break;
      case 2022338513:
         if (str.equals("DOUBLE")) {
            var4 = 11;
         }
      }

      switch(var4) {
      case 0:
         return -7;
      case 1:
         return -6;
      case 2:
      case 3:
         return 5;
      case 4:
      case 5:
      case 6:
      case 7:
         return 4;
      case 8:
      case 9:
         return -5;
      case 10:
      case 11:
         return 8;
      case 12:
         return 6;
      case 13:
         return 3;
      case 14:
         return 1;
      case 15:
      case 16:
      case 17:
      case 18:
         return 12;
      case 19:
         return 91;
      case 20:
         return 92;
      case 21:
      case 22:
         return 93;
      case 23:
         return -2;
      case 24:
         return -3;
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
         return 2004;
      case 30:
      case 31:
      case 32:
         return 2005;
      default:
         return 1111;
      }
   }

   public String getParameterTypeName(int index) throws SQLException {
      this.setIndex(index);
      return this.rs.getString("DATA_TYPE").toUpperCase(Locale.ROOT);
   }

   public String getParameterClassName(int index) throws SQLException {
      this.setIndex(index);
      String str = this.rs.getString("DATA_TYPE").toUpperCase(Locale.ROOT);
      byte var4 = -1;
      switch(str.hashCode()) {
      case -2034720975:
         if (str.equals("DECIMAL")) {
            var4 = 14;
         }
         break;
      case -1783518776:
         if (str.equals("VARBINARY")) {
            var4 = 10;
         }
         break;
      case -1718637701:
         if (str.equals("DATETIME")) {
            var4 = 27;
         }
         break;
      case -1666320270:
         if (str.equals("GEOMETRY")) {
            var4 = 9;
         }
         break;
      case -1618932450:
         if (str.equals("INTEGER")) {
            var4 = 6;
         }
         break;
      case -1453246218:
         if (str.equals("TIMESTAMP")) {
            var4 = 26;
         }
         break;
      case -1291368423:
         if (str.equals("LONGBLOB")) {
            var4 = 30;
         }
         break;
      case -1290838615:
         if (str.equals("LONGTEXT")) {
            var4 = 23;
         }
         break;
      case -1285035886:
         if (str.equals("MEDIUMBLOB")) {
            var4 = 29;
         }
         break;
      case -1284506078:
         if (str.equals("MEDIUMTEXT")) {
            var4 = 22;
         }
         break;
      case -1247219043:
         if (str.equals("TINYBLOB")) {
            var4 = 11;
         }
         break;
      case -1246689235:
         if (str.equals("TINYTEXT")) {
            var4 = 20;
         }
         break;
      case -594415409:
         if (str.equals("TINYINT")) {
            var4 = 1;
         }
         break;
      case 65773:
         if (str.equals("BIT")) {
            var4 = 0;
         }
         break;
      case 72655:
         if (str.equals("INT")) {
            var4 = 5;
         }
         break;
      case 81986:
         if (str.equals("SET")) {
            var4 = 8;
         }
         break;
      case 2041757:
         if (str.equals("BLOB")) {
            var4 = 28;
         }
         break;
      case 2067286:
         if (str.equals("CHAR")) {
            var4 = 17;
         }
         break;
      case 2090926:
         if (str.equals("DATE")) {
            var4 = 24;
         }
         break;
      case 2133249:
         if (str.equals("ENUM")) {
            var4 = 19;
         }
         break;
      case 2511262:
         if (str.equals("REAL")) {
            var4 = 15;
         }
         break;
      case 2571565:
         if (str.equals("TEXT")) {
            var4 = 21;
         }
         break;
      case 2575053:
         if (str.equals("TIME")) {
            var4 = 25;
         }
         break;
      case 2719805:
         if (str.equals("YEAR")) {
            var4 = 3;
         }
         break;
      case 66988604:
         if (str.equals("FLOAT")) {
            var4 = 13;
         }
         break;
      case 176095624:
         if (str.equals("SMALLINT")) {
            var4 = 2;
         }
         break;
      case 651290682:
         if (str.equals("MEDIUMINT")) {
            var4 = 4;
         }
         break;
      case 954596061:
         if (str.equals("VARCHAR")) {
            var4 = 18;
         }
         break;
      case 1959128815:
         if (str.equals("BIGINT")) {
            var4 = 12;
         }
         break;
      case 1959329793:
         if (str.equals("BINARY")) {
            var4 = 7;
         }
         break;
      case 2022338513:
         if (str.equals("DOUBLE")) {
            var4 = 16;
         }
      }

      switch(var4) {
      case 0:
         return BitSet.class.getName();
      case 1:
         return Byte.TYPE.getName();
      case 2:
      case 3:
         return Short.TYPE.getName();
      case 4:
      case 5:
      case 6:
         return Integer.TYPE.getName();
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
         return byte[].class.getName();
      case 12:
         return Long.TYPE.getName();
      case 13:
         return Float.TYPE.getName();
      case 14:
         return BigDecimal.class.getName();
      case 15:
      case 16:
         return Double.TYPE.getName();
      case 17:
      case 18:
      case 19:
      case 20:
         return String.class.getName();
      case 21:
      case 22:
      case 23:
         return Clob.class.getName();
      case 24:
         return Date.class.getName();
      case 25:
         return Time.class.getName();
      case 26:
      case 27:
         return Timestamp.class.getName();
      case 28:
      case 29:
      case 30:
         return Blob.class.getName();
      default:
         return Object.class.getName();
      }
   }

   public int getParameterMode(int index) throws SQLException {
      this.setIndex(index);
      if (this.isFunction) {
         return 4;
      } else {
         String str = this.rs.getString("PARAMETER_MODE");
         byte var4 = -1;
         switch(str.hashCode()) {
         case 2341:
            if (str.equals("IN")) {
               var4 = 0;
            }
            break;
         case 78638:
            if (str.equals("OUT")) {
               var4 = 1;
            }
            break;
         case 69819369:
            if (str.equals("INOUT")) {
               var4 = 2;
            }
         }

         switch(var4) {
         case 0:
            return 1;
         case 1:
            return 4;
         case 2:
            return 2;
         default:
            return 0;
         }
      }
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (this.isWrapperFor(iface)) {
         return iface.cast(this);
      } else {
         throw new SQLException("The receiver is not a wrapper for " + iface.getName());
      }
   }

   public boolean isWrapperFor(Class<?> iface) {
      return iface.isInstance(this);
   }
}
