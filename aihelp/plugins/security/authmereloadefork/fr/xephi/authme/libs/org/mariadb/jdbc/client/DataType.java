package fr.xephi.authme.libs.org.mariadb.jdbc.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.BigDecimalColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.BitColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.BlobColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.DateColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.DoubleColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.FloatColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.GeometryColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.JsonColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.SignedBigIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.SignedIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.SignedMediumIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.SignedSmallIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.SignedTinyIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.StringColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.TimeColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.TimestampColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.UnsignedBigIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.UnsignedIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.UnsignedMediumIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.UnsignedSmallIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.UnsignedTinyIntColumn;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.column.YearColumn;

public enum DataType {
   OLDDECIMAL(0, BigDecimalColumn::new, BigDecimalColumn::new),
   TINYINT(1, SignedTinyIntColumn::new, UnsignedTinyIntColumn::new),
   SMALLINT(2, SignedSmallIntColumn::new, UnsignedSmallIntColumn::new),
   INTEGER(3, SignedIntColumn::new, UnsignedIntColumn::new),
   FLOAT(4, FloatColumn::new, FloatColumn::new),
   DOUBLE(5, DoubleColumn::new, DoubleColumn::new),
   NULL(6, StringColumn::new, StringColumn::new),
   TIMESTAMP(7, TimestampColumn::new, TimestampColumn::new),
   BIGINT(8, SignedBigIntColumn::new, UnsignedBigIntColumn::new),
   MEDIUMINT(9, SignedMediumIntColumn::new, UnsignedMediumIntColumn::new),
   DATE(10, DateColumn::new, DateColumn::new),
   TIME(11, TimeColumn::new, TimeColumn::new),
   DATETIME(12, TimestampColumn::new, TimestampColumn::new),
   YEAR(13, YearColumn::new, YearColumn::new),
   NEWDATE(14, DateColumn::new, DateColumn::new),
   VARCHAR(15, StringColumn::new, StringColumn::new),
   BIT(16, BitColumn::new, BitColumn::new),
   JSON(245, JsonColumn::new, JsonColumn::new),
   DECIMAL(246, BigDecimalColumn::new, BigDecimalColumn::new),
   ENUM(247, StringColumn::new, StringColumn::new),
   SET(248, StringColumn::new, StringColumn::new),
   TINYBLOB(249, BlobColumn::new, BlobColumn::new),
   MEDIUMBLOB(250, BlobColumn::new, BlobColumn::new),
   LONGBLOB(251, BlobColumn::new, BlobColumn::new),
   BLOB(252, BlobColumn::new, BlobColumn::new),
   VARSTRING(253, StringColumn::new, StringColumn::new),
   STRING(254, StringColumn::new, StringColumn::new),
   GEOMETRY(255, GeometryColumn::new, GeometryColumn::new);

   static final DataType[] typeMap = new DataType[256];
   private final int mariadbType;
   private final DataType.ColumnConstructor columnConstructor;
   private final DataType.ColumnConstructor unsignedColumnConstructor;

   private DataType(int mariadbType, DataType.ColumnConstructor columnConstructor, DataType.ColumnConstructor unsignedColumnConstructor) {
      this.mariadbType = mariadbType;
      this.columnConstructor = columnConstructor;
      this.unsignedColumnConstructor = unsignedColumnConstructor;
   }

   public static DataType of(int typeValue) {
      return typeMap[typeValue];
   }

   public int get() {
      return this.mariadbType;
   }

   public DataType.ColumnConstructor getColumnConstructor() {
      return this.columnConstructor;
   }

   public DataType.ColumnConstructor getUnsignedColumnConstructor() {
      return this.unsignedColumnConstructor;
   }

   static {
      DataType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         DataType v = var0[var2];
         typeMap[v.mariadbType] = v;
      }

   }

   @FunctionalInterface
   public interface ColumnConstructor {
      ColumnDecoder create(ReadableByteBuf var1, int var2, long var3, DataType var5, byte var6, int var7, int[] var8, String var9, String var10);
   }
}
