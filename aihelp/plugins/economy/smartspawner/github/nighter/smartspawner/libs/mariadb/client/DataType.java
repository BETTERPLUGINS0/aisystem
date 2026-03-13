package github.nighter.smartspawner.libs.mariadb.client;

import github.nighter.smartspawner.libs.mariadb.client.column.BigDecimalColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.BitColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.BlobColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.DateColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.DoubleColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.FloatColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.GeometryColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.JsonColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.SignedBigIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.SignedIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.SignedMediumIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.SignedSmallIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.SignedTinyIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.StringColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.TimeColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.TimestampColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.UnsignedBigIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.UnsignedIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.UnsignedMediumIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.UnsignedSmallIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.UnsignedTinyIntColumn;
import github.nighter.smartspawner.libs.mariadb.client.column.YearColumn;

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

   private DataType(int param3, DataType.ColumnConstructor param4, DataType.ColumnConstructor param5) {
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

   // $FF: synthetic method
   private static DataType[] $values() {
      return new DataType[]{OLDDECIMAL, TINYINT, SMALLINT, INTEGER, FLOAT, DOUBLE, NULL, TIMESTAMP, BIGINT, MEDIUMINT, DATE, TIME, DATETIME, YEAR, NEWDATE, VARCHAR, BIT, JSON, DECIMAL, ENUM, SET, TINYBLOB, MEDIUMBLOB, LONGBLOB, BLOB, VARSTRING, STRING, GEOMETRY};
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
