package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.PGRefCursorResultSet;
import fr.xephi.authme.libs.org.postgresql.PGResultSetMetaData;
import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.BaseStatement;
import fr.xephi.authme.libs.org.postgresql.core.Encoding;
import fr.xephi.authme.libs.org.postgresql.core.Field;
import fr.xephi.authme.libs.org.postgresql.core.Oid;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.core.ResultCursor;
import fr.xephi.authme.libs.org.postgresql.core.ResultHandlerBase;
import fr.xephi.authme.libs.org.postgresql.core.TransactionState;
import fr.xephi.authme.libs.org.postgresql.core.Tuple;
import fr.xephi.authme.libs.org.postgresql.core.TypeInfo;
import fr.xephi.authme.libs.org.postgresql.core.Utils;
import fr.xephi.authme.libs.org.postgresql.util.ByteConverter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.HStoreConverter;
import fr.xephi.authme.libs.org.postgresql.util.JdbcBlackHole;
import fr.xephi.authme.libs.org.postgresql.util.NumberParser;
import fr.xephi.authme.libs.org.postgresql.util.PGbytea;
import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PGtokenizer;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.checkerframework.dataflow.qual.Pure;

public class PgResultSet implements ResultSet, PGRefCursorResultSet {
   private boolean updateable;
   private boolean doingUpdates;
   @Nullable
   private HashMap<String, Object> updateValues;
   private boolean usingOID;
   @Nullable
   private List<PgResultSet.PrimaryKey> primaryKeys;
   private boolean singleTable;
   private String onlyTable = "";
   @Nullable
   private String tableName;
   @Nullable
   private PreparedStatement deleteStatement;
   private final int resultsettype;
   private final int resultsetconcurrency;
   private int fetchdirection = 1002;
   @Nullable
   private TimeZone defaultTimeZone;
   protected final BaseConnection connection;
   protected final BaseStatement statement;
   protected final Field[] fields;
   @Nullable
   protected final Query originalQuery;
   @Nullable
   private TimestampUtils timestampUtils;
   protected final int maxRows;
   protected final int maxFieldSize;
   @Nullable
   protected List<Tuple> rows;
   protected int currentRow = -1;
   protected int rowOffset;
   @Nullable
   protected Tuple thisRow;
   @Nullable
   protected SQLWarning warnings;
   protected boolean wasNullFlag;
   protected boolean onInsertRow;
   @Nullable
   private Tuple rowBuffer;
   protected int fetchSize;
   protected int lastUsedFetchSize;
   protected boolean adaptiveFetch;
   @Nullable
   protected ResultCursor cursor;
   @Nullable
   private Map<String, Integer> columnNameIndexMap;
   @Nullable
   private ResultSetMetaData rsMetaData;
   private final ResourceLock lock = new ResourceLock();
   private static final LocalDate LOCAL_DATE_EPOCH = LocalDate.of(1970, 1, 1);
   @Nullable
   private String refCursorName;
   private static final BigInteger BYTEMAX = new BigInteger(Byte.toString((byte)127));
   private static final BigInteger BYTEMIN = new BigInteger(Byte.toString((byte)-128));
   private static final NumberFormatException FAST_NUMBER_FAILED = new NumberFormatException() {
      public Throwable fillInStackTrace() {
         return this;
      }
   };
   private static final BigInteger SHORTMAX = new BigInteger(Short.toString((short)32767));
   private static final BigInteger SHORTMIN = new BigInteger(Short.toString((short)-32768));
   private static final BigInteger INTMAX = new BigInteger(Integer.toString(Integer.MAX_VALUE));
   private static final BigInteger INTMIN = new BigInteger(Integer.toString(Integer.MIN_VALUE));
   private static final BigInteger LONGMAX = new BigInteger(Long.toString(Long.MAX_VALUE));
   private static final BigInteger LONGMIN = new BigInteger(Long.toString(Long.MIN_VALUE));
   private static final float LONG_MAX_FLOAT = StrictMath.nextDown(9.223372E18F);
   private static final float LONG_MIN_FLOAT = StrictMath.nextUp(-9.223372E18F);
   private static final double LONG_MAX_DOUBLE = StrictMath.nextDown(9.223372036854776E18D);
   private static final double LONG_MIN_DOUBLE = StrictMath.nextUp(-9.223372036854776E18D);

   protected ResultSetMetaData createMetaData() throws SQLException {
      return new PgResultSetMetaData(this.connection, this.fields);
   }

   public ResultSetMetaData getMetaData() throws SQLException {
      this.checkClosed();
      if (this.rsMetaData == null) {
         this.rsMetaData = this.createMetaData();
      }

      return this.rsMetaData;
   }

   PgResultSet(@Nullable Query originalQuery, BaseStatement statement, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor, int maxRows, int maxFieldSize, int rsType, int rsConcurrency, int rsHoldability, boolean adaptiveFetch) throws SQLException {
      if (tuples == null) {
         throw new NullPointerException("tuples must be non-null");
      } else if (fields == null) {
         throw new NullPointerException("fields must be non-null");
      } else {
         this.originalQuery = originalQuery;
         this.connection = (BaseConnection)statement.getConnection();
         this.statement = statement;
         this.fields = fields;
         this.rows = tuples;
         this.cursor = cursor;
         this.maxRows = maxRows;
         this.maxFieldSize = maxFieldSize;
         this.resultsettype = rsType;
         this.resultsetconcurrency = rsConcurrency;
         this.adaptiveFetch = adaptiveFetch;
         this.lastUsedFetchSize = tuples.size();
      }
   }

   public URL getURL(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getURL columnIndex: {0}", columnIndex);
      this.checkClosed();
      throw Driver.notImplemented(this.getClass(), "getURL(int)");
   }

   public URL getURL(String columnName) throws SQLException {
      return this.getURL(this.findColumn(columnName));
   }

   @RequiresNonNull({"thisRow"})
   @Nullable
   protected Object internalGetObject(@Positive int columnIndex, Field field) throws SQLException {
      Nullness.castNonNull(this.thisRow, "thisRow");
      switch(this.getSQLType(columnIndex)) {
      case -7:
      case 16:
         if (field.getOID() == 16) {
            return this.getBoolean(columnIndex);
         } else {
            if (field.getOID() == 1560) {
               byte[] data = this.getRawValue(columnIndex);
               if (data == null || data.length == 1) {
                  return this.getBoolean(columnIndex);
               }
            }

            return null;
         }
      case -6:
      case 4:
      case 5:
         return this.getInt(columnIndex);
      case -5:
         return this.getLong(columnIndex);
      case -4:
      case -3:
      case -2:
         return this.getBytes(columnIndex);
      case -1:
      case 1:
      case 12:
         return this.getString(columnIndex);
      case 2:
      case 3:
         return this.getNumeric(columnIndex, field.getMod() == -1 ? -1 : field.getMod() - 4 & '\uffff', true);
      case 6:
      case 8:
         return this.getDouble(columnIndex);
      case 7:
         return this.getFloat(columnIndex);
      case 91:
         return this.getDate(columnIndex);
      case 92:
         return this.getTime(columnIndex);
      case 93:
         return this.getTimestamp(columnIndex, (Calendar)null);
      case 2003:
         return this.getArray(columnIndex);
      case 2004:
         return this.getBlob(columnIndex);
      case 2005:
         return this.getClob(columnIndex);
      case 2009:
         return this.getSQLXML(columnIndex);
      default:
         String type = this.getPGType(columnIndex);
         if ("unknown".equals(type)) {
            return this.getString(columnIndex);
         } else if ("uuid".equals(type)) {
            return this.isBinary(columnIndex) ? this.getUUID((byte[])Nullness.castNonNull(this.thisRow.get(columnIndex - 1))) : this.getUUID((String)Nullness.castNonNull(this.getString(columnIndex)));
         } else if ("refcursor".equals(type)) {
            String cursorName = (String)Nullness.castNonNull(this.getString(columnIndex));
            StringBuilder sb = new StringBuilder("FETCH ALL IN ");
            Utils.escapeIdentifier(sb, cursorName);
            ResultSet rs = this.connection.execSQLQuery(sb.toString(), this.resultsettype, 1007);
            ((PgResultSet)rs).setRefCursor(cursorName);
            ((PgResultSet)rs).closeRefCursor();
            return rs;
         } else if ("hstore".equals(type)) {
            return this.isBinary(columnIndex) ? HStoreConverter.fromBytes((byte[])Nullness.castNonNull(this.thisRow.get(columnIndex - 1)), this.connection.getEncoding()) : HStoreConverter.fromString((String)Nullness.castNonNull(this.getString(columnIndex)));
         } else {
            return null;
         }
      }
   }

   @Pure
   @EnsuresNonNull({"rows"})
   private void checkScrollable() throws SQLException {
      this.checkClosed();
      if (this.resultsettype == 1003) {
         throw new PSQLException(GT.tr("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY."), PSQLState.INVALID_CURSOR_STATE);
      }
   }

   public boolean absolute(int index) throws SQLException {
      this.checkScrollable();
      if (index == 0) {
         this.beforeFirst();
         return false;
      } else {
         int rowsSize = this.rows.size();
         int internalIndex;
         if (index < 0) {
            if (index < -rowsSize) {
               this.beforeFirst();
               return false;
            }

            internalIndex = rowsSize + index;
         } else {
            if (index > rowsSize) {
               this.afterLast();
               return false;
            }

            internalIndex = index - 1;
         }

         this.currentRow = internalIndex;
         this.initRowBuffer();
         this.onInsertRow = false;
         return true;
      }
   }

   public void afterLast() throws SQLException {
      this.checkScrollable();
      int rowsSize = this.rows.size();
      if (rowsSize > 0) {
         this.currentRow = rowsSize;
      }

      this.onInsertRow = false;
      this.thisRow = null;
      this.rowBuffer = null;
   }

   public void beforeFirst() throws SQLException {
      this.checkScrollable();
      if (!this.rows.isEmpty()) {
         this.currentRow = -1;
      }

      this.onInsertRow = false;
      this.thisRow = null;
      this.rowBuffer = null;
   }

   public boolean first() throws SQLException {
      this.checkScrollable();
      if (this.rows.size() <= 0) {
         return false;
      } else {
         this.currentRow = 0;
         this.initRowBuffer();
         this.onInsertRow = false;
         return true;
      }
   }

   @Nullable
   public Array getArray(String colName) throws SQLException {
      return this.getArray(this.findColumn(colName));
   }

   protected Array makeArray(int oid, byte[] value) throws SQLException {
      return new PgArray(this.connection, oid, value);
   }

   protected Array makeArray(int oid, String value) throws SQLException {
      return new PgArray(this.connection, oid, value);
   }

   @Pure
   @Nullable
   public Array getArray(int i) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         int oid = this.fields[i - 1].getOID();
         return this.isBinary(i) ? this.makeArray(oid, value) : this.makeArray(oid, (String)Nullness.castNonNull(this.getFixedString(i)));
      }
   }

   @Nullable
   public BigDecimal getBigDecimal(@Positive int columnIndex) throws SQLException {
      return this.getBigDecimal(columnIndex, -1);
   }

   @Nullable
   public BigDecimal getBigDecimal(String columnName) throws SQLException {
      return this.getBigDecimal(this.findColumn(columnName));
   }

   @Nullable
   public Blob getBlob(String columnName) throws SQLException {
      return this.getBlob(this.findColumn(columnName));
   }

   protected Blob makeBlob(long oid) throws SQLException {
      return new PgBlob(this.connection, oid);
   }

   @Pure
   @Nullable
   public Blob getBlob(int i) throws SQLException {
      byte[] value = this.getRawValue(i);
      return value == null ? null : this.makeBlob(this.getLong(i));
   }

   @Nullable
   public Reader getCharacterStream(String columnName) throws SQLException {
      return this.getCharacterStream(this.findColumn(columnName));
   }

   @Nullable
   public Reader getCharacterStream(int i) throws SQLException {
      String value = this.getString(i);
      return value == null ? null : new CharArrayReader(value.toCharArray());
   }

   @Nullable
   public Clob getClob(String columnName) throws SQLException {
      return this.getClob(this.findColumn(columnName));
   }

   protected Clob makeClob(long oid) throws SQLException {
      return new PgClob(this.connection, oid);
   }

   @Pure
   @Nullable
   public Clob getClob(int i) throws SQLException {
      byte[] value = this.getRawValue(i);
      return value == null ? null : this.makeClob(this.getLong(i));
   }

   public int getConcurrency() throws SQLException {
      this.checkClosed();
      return this.resultsetconcurrency;
   }

   @Nullable
   public Date getDate(int i, @Nullable Calendar cal) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         if (cal == null) {
            cal = this.getDefaultCalendar();
         }

         if (this.isBinary(i)) {
            int col = i - 1;
            int oid = this.fields[col].getOID();
            TimeZone tz = cal.getTimeZone();
            if (oid == 1082) {
               return this.getTimestampUtils().toDateBin(tz, value);
            } else if (oid != 1114 && oid != 1184) {
               throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), "date"), PSQLState.DATA_TYPE_MISMATCH);
            } else {
               Timestamp timestamp = (Timestamp)Nullness.castNonNull(this.getTimestamp(i, cal));
               return this.getTimestampUtils().convertToDate(timestamp.getTime(), tz);
            }
         } else {
            return this.getTimestampUtils().toDate(cal, (String)Nullness.castNonNull(this.getString(i)));
         }
      }
   }

   @Nullable
   public Time getTime(int i, @Nullable Calendar cal) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         if (cal == null) {
            cal = this.getDefaultCalendar();
         }

         if (this.isBinary(i)) {
            int col = i - 1;
            int oid = this.fields[col].getOID();
            TimeZone tz = cal.getTimeZone();
            if (oid != 1083 && oid != 1266) {
               if (oid != 1114 && oid != 1184) {
                  throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), "time"), PSQLState.DATA_TYPE_MISMATCH);
               } else {
                  Timestamp timestamp = this.getTimestamp(i, cal);
                  if (timestamp == null) {
                     return null;
                  } else {
                     long timeMillis = timestamp.getTime();
                     return oid == 1184 ? new Time(timeMillis % TimeUnit.DAYS.toMillis(1L)) : this.getTimestampUtils().convertToTime(timeMillis, tz);
                  }
               }
            } else {
               return this.getTimestampUtils().toTimeBin(tz, value);
            }
         } else {
            String string = this.getString(i);
            return this.getTimestampUtils().toTime(cal, string);
         }
      }
   }

   @Pure
   @Nullable
   public Timestamp getTimestamp(int i, @Nullable Calendar cal) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         if (cal == null) {
            cal = this.getDefaultCalendar();
         }

         int col = i - 1;
         int oid = this.fields[col].getOID();
         Timestamp tsWithMicros;
         Timestamp tsUnixEpochDate;
         if (this.isBinary(i)) {
            byte[] row = ((Tuple)Nullness.castNonNull(this.thisRow)).get(col);
            if (oid == 1184 || oid == 1114) {
               boolean hasTimeZone = oid == 1184;
               TimeZone tz = cal.getTimeZone();
               return this.getTimestampUtils().toTimestampBin(tz, (byte[])Nullness.castNonNull(row), hasTimeZone);
            }

            if (oid == 1083) {
               tsWithMicros = this.getTimestampUtils().toTimestampBin(cal.getTimeZone(), (byte[])Nullness.castNonNull(row), false);
               tsUnixEpochDate = new Timestamp(((Time)Nullness.castNonNull(this.getTime(i, cal))).getTime());
               tsUnixEpochDate.setNanos(tsWithMicros.getNanos());
               return tsUnixEpochDate;
            }

            if (oid == 1266) {
               TimeZone tz = cal.getTimeZone();
               byte[] timeBytesWithoutTimeZone = Arrays.copyOfRange((byte[])Nullness.castNonNull(row), 0, 8);
               Timestamp tsWithMicros = this.getTimestampUtils().toTimestampBin(tz, timeBytesWithoutTimeZone, false);
               Timestamp tsUnixEpochDate = new Timestamp(((Time)Nullness.castNonNull(this.getTime(i, cal))).getTime());
               tsUnixEpochDate.setNanos(tsWithMicros.getNanos());
               return tsUnixEpochDate;
            }

            if (oid != 1082) {
               throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), "timestamp"), PSQLState.DATA_TYPE_MISMATCH);
            }

            new Timestamp(((Date)Nullness.castNonNull(this.getDate(i, cal))).getTime());
         }

         String string = (String)Nullness.castNonNull(this.getString(i));
         if (oid != 1083 && oid != 1266) {
            return this.getTimestampUtils().toTimestamp(cal, string);
         } else {
            tsWithMicros = this.getTimestampUtils().toTimestamp(cal, string);
            tsUnixEpochDate = new Timestamp(this.getTimestampUtils().toTime(cal, string).getTime());
            tsUnixEpochDate.setNanos(tsWithMicros.getNanos());
            return tsUnixEpochDate;
         }
      }
   }

   @Nullable
   private OffsetDateTime getOffsetDateTime(int i) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         int col = i - 1;
         int oid = this.fields[col].getOID();
         if (this.isBinary(i)) {
            if (oid == 1184 || oid == 1114) {
               return this.getTimestampUtils().toOffsetDateTimeBin(value);
            }

            if (oid == 1266) {
               return this.getTimestampUtils().toOffsetTimeBin(value).atDate(LOCAL_DATE_EPOCH);
            }
         } else {
            if (oid == 1184 || oid == 1114) {
               OffsetDateTime offsetDateTime = this.getTimestampUtils().toOffsetDateTime((String)Nullness.castNonNull(this.getString(i)));
               if (offsetDateTime != OffsetDateTime.MAX && offsetDateTime != OffsetDateTime.MIN) {
                  return offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC);
               }

               return offsetDateTime;
            }

            if (oid == 1266) {
               return this.getTimestampUtils().toOffsetDateTime((String)Nullness.castNonNull(this.getString(i)));
            }
         }

         throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), "java.time.OffsetDateTime"), PSQLState.DATA_TYPE_MISMATCH);
      }
   }

   @Nullable
   private OffsetTime getOffsetTime(int i) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         int col = i - 1;
         int oid = this.fields[col].getOID();
         if (oid == 1266) {
            return this.isBinary(i) ? this.getTimestampUtils().toOffsetTimeBin(value) : this.getTimestampUtils().toOffsetTime((String)Nullness.castNonNull(this.getString(i)));
         } else {
            throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), "java.time.OffsetTime"), PSQLState.DATA_TYPE_MISMATCH);
         }
      }
   }

   @Nullable
   private LocalDateTime getLocalDateTime(int i) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         int col = i - 1;
         int oid = this.fields[col].getOID();
         if (oid == 1114) {
            return this.isBinary(i) ? this.getTimestampUtils().toLocalDateTimeBin(value) : this.getTimestampUtils().toLocalDateTime((String)Nullness.castNonNull(this.getString(i)));
         } else {
            throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), "java.time.LocalDateTime"), PSQLState.DATA_TYPE_MISMATCH);
         }
      }
   }

   @Nullable
   private LocalDate getLocalDate(int i) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         int col = i - 1;
         int oid = this.fields[col].getOID();
         if (this.isBinary(i)) {
            if (oid == 1082) {
               return this.getTimestampUtils().toLocalDateBin(value);
            }

            if (oid == 1114) {
               return this.getTimestampUtils().toLocalDateTimeBin(value).toLocalDate();
            }
         } else if (oid == 1082 || oid == 1114) {
            return this.getTimestampUtils().toLocalDateTime((String)Nullness.castNonNull(this.getString(i))).toLocalDate();
         }

         throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), "java.time.LocalDate"), PSQLState.DATA_TYPE_MISMATCH);
      }
   }

   @Nullable
   private LocalTime getLocalTime(int i) throws SQLException {
      byte[] value = this.getRawValue(i);
      if (value == null) {
         return null;
      } else {
         int col = i - 1;
         int oid = this.fields[col].getOID();
         if (oid == 1083) {
            return this.isBinary(i) ? this.getTimestampUtils().toLocalTimeBin(value) : this.getTimestampUtils().toLocalTime(this.getString(i));
         } else {
            throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), "java.time.LocalTime"), PSQLState.DATA_TYPE_MISMATCH);
         }
      }
   }

   @Nullable
   public Date getDate(String c, @Nullable Calendar cal) throws SQLException {
      return this.getDate(this.findColumn(c), cal);
   }

   @Nullable
   public Time getTime(String c, @Nullable Calendar cal) throws SQLException {
      return this.getTime(this.findColumn(c), cal);
   }

   @Nullable
   public Timestamp getTimestamp(String c, @Nullable Calendar cal) throws SQLException {
      return this.getTimestamp(this.findColumn(c), cal);
   }

   public int getFetchDirection() throws SQLException {
      this.checkClosed();
      return this.fetchdirection;
   }

   @Nullable
   public Object getObjectImpl(String columnName, @Nullable Map<String, Class<?>> map) throws SQLException {
      return this.getObjectImpl(this.findColumn(columnName), map);
   }

   @Nullable
   public Object getObjectImpl(int i, @Nullable Map<String, Class<?>> map) throws SQLException {
      this.checkClosed();
      if (map != null && !map.isEmpty()) {
         throw Driver.notImplemented(this.getClass(), "getObjectImpl(int,Map)");
      } else {
         return this.getObject(i);
      }
   }

   @Nullable
   public Ref getRef(String columnName) throws SQLException {
      return this.getRef(this.findColumn(columnName));
   }

   @Nullable
   public Ref getRef(int i) throws SQLException {
      this.checkClosed();
      throw Driver.notImplemented(this.getClass(), "getRef(int)");
   }

   public int getRow() throws SQLException {
      this.checkClosed();
      if (this.onInsertRow) {
         return 0;
      } else {
         int rowsSize = this.rows.size();
         return this.currentRow >= 0 && this.currentRow < rowsSize ? this.rowOffset + this.currentRow + 1 : 0;
      }
   }

   public Statement getStatement() throws SQLException {
      this.checkClosed();
      return this.statement;
   }

   public int getType() throws SQLException {
      this.checkClosed();
      return this.resultsettype;
   }

   @Pure
   public boolean isAfterLast() throws SQLException {
      this.checkClosed();
      if (this.onInsertRow) {
         return false;
      } else {
         Nullness.castNonNull(this.rows, "rows");
         int rowsSize = this.rows.size();
         if (this.rowOffset + rowsSize == 0) {
            return false;
         } else {
            return this.currentRow >= rowsSize;
         }
      }
   }

   @Pure
   public boolean isBeforeFirst() throws SQLException {
      this.checkClosed();
      if (this.onInsertRow) {
         return false;
      } else {
         return this.rowOffset + this.currentRow < 0 && !((List)Nullness.castNonNull(this.rows, "rows")).isEmpty();
      }
   }

   public boolean isFirst() throws SQLException {
      this.checkClosed();
      if (this.onInsertRow) {
         return false;
      } else {
         int rowsSize = this.rows.size();
         if (this.rowOffset + rowsSize == 0) {
            return false;
         } else {
            return this.rowOffset + this.currentRow == 0;
         }
      }
   }

   public boolean isLast() throws SQLException {
      this.checkClosed();
      if (this.onInsertRow) {
         return false;
      } else {
         List<Tuple> rows = (List)Nullness.castNonNull(this.rows, "rows");
         int rowsSize = rows.size();
         if (rowsSize == 0) {
            return false;
         } else if (this.currentRow != rowsSize - 1) {
            return false;
         } else {
            ResultCursor cursor = this.cursor;
            if (cursor == null) {
               return true;
            } else if (this.maxRows > 0 && this.rowOffset + this.currentRow == this.maxRows) {
               return true;
            } else {
               this.rowOffset += rowsSize - 1;
               int fetchRows = this.fetchSize;
               int adaptiveFetchRows = this.connection.getQueryExecutor().getAdaptiveFetchSize(this.adaptiveFetch, cursor);
               if (adaptiveFetchRows != -1) {
                  fetchRows = adaptiveFetchRows;
               }

               if (this.maxRows != 0 && (fetchRows == 0 || this.rowOffset + fetchRows > this.maxRows)) {
                  fetchRows = this.maxRows - this.rowOffset;
               }

               this.connection.getQueryExecutor().fetch(cursor, new PgResultSet.CursorResultHandler(), fetchRows, this.adaptiveFetch);
               this.lastUsedFetchSize = fetchRows;
               rows = (List)Nullness.castNonNull(this.rows, "rows");
               rows.add(0, (Tuple)Nullness.castNonNull(this.thisRow));
               this.currentRow = 0;
               return rows.size() == 1;
            }
         }
      }
   }

   public boolean last() throws SQLException {
      this.checkScrollable();
      List<Tuple> rows = (List)Nullness.castNonNull(this.rows, "rows");
      int rowsSize = rows.size();
      if (rowsSize <= 0) {
         return false;
      } else {
         this.currentRow = rowsSize - 1;
         this.initRowBuffer();
         this.onInsertRow = false;
         return true;
      }
   }

   public boolean previous() throws SQLException {
      this.checkScrollable();
      if (this.onInsertRow) {
         throw new PSQLException(GT.tr("Can''t use relative move methods while on the insert row."), PSQLState.INVALID_CURSOR_STATE);
      } else if (this.currentRow - 1 < 0) {
         this.currentRow = -1;
         this.thisRow = null;
         this.rowBuffer = null;
         return false;
      } else {
         --this.currentRow;
         this.initRowBuffer();
         return true;
      }
   }

   public boolean relative(int rows) throws SQLException {
      this.checkScrollable();
      if (this.onInsertRow) {
         throw new PSQLException(GT.tr("Can''t use relative move methods while on the insert row."), PSQLState.INVALID_CURSOR_STATE);
      } else {
         int index = this.currentRow + 1 + rows;
         if (index < 0) {
            this.beforeFirst();
            return false;
         } else {
            return this.absolute(index);
         }
      }
   }

   public void setFetchDirection(int direction) throws SQLException {
      this.checkClosed();
      switch(direction) {
      case 1001:
      case 1002:
         this.checkScrollable();
      case 1000:
         this.fetchdirection = direction;
         return;
      default:
         throw new PSQLException(GT.tr("Invalid fetch direction constant: {0}.", direction), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   public void cancelRowUpdates() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkClosed();
         if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Cannot call cancelRowUpdates() when on the insert row."), PSQLState.INVALID_CURSOR_STATE);
         }

         if (this.doingUpdates) {
            this.doingUpdates = false;
            this.clearRowBuffer(true);
         }
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void deleteRow() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkUpdateable();
         if (this.onInsertRow) {
            throw new PSQLException(GT.tr("Cannot call deleteRow() when on the insert row."), PSQLState.INVALID_CURSOR_STATE);
         }

         if (this.isBeforeFirst()) {
            throw new PSQLException(GT.tr("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here."), PSQLState.INVALID_CURSOR_STATE);
         }

         if (this.isAfterLast()) {
            throw new PSQLException(GT.tr("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here."), PSQLState.INVALID_CURSOR_STATE);
         }

         List<Tuple> rows = (List)Nullness.castNonNull(this.rows, "rows");
         if (rows.isEmpty()) {
            throw new PSQLException(GT.tr("There are no rows in this ResultSet."), PSQLState.INVALID_CURSOR_STATE);
         }

         List<PgResultSet.PrimaryKey> primaryKeys = (List)Nullness.castNonNull(this.primaryKeys, "primaryKeys");
         int numKeys = primaryKeys.size();
         PreparedStatement deleteStatement = this.deleteStatement;
         if (deleteStatement == null) {
            StringBuilder deleteSQL = (new StringBuilder("DELETE FROM ")).append(this.onlyTable).append(this.tableName).append(" where ");
            int i = 0;

            while(true) {
               if (i >= numKeys) {
                  this.deleteStatement = deleteStatement = this.connection.prepareStatement(deleteSQL.toString());
                  break;
               }

               Utils.escapeIdentifier(deleteSQL, ((PgResultSet.PrimaryKey)primaryKeys.get(i)).name);
               deleteSQL.append(" = ?");
               if (i < numKeys - 1) {
                  deleteSQL.append(" and ");
               }

               ++i;
            }
         }

         deleteStatement.clearParameters();
         int i = 0;

         while(true) {
            if (i >= numKeys) {
               deleteStatement.executeUpdate();
               rows.remove(this.currentRow);
               --this.currentRow;
               this.moveToCurrentRow();
               break;
            }

            deleteStatement.setObject(i + 1, ((PgResultSet.PrimaryKey)primaryKeys.get(i)).getValue());
            ++i;
         }
      } catch (Throwable var9) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void insertRow() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkUpdateable();
         Nullness.castNonNull(this.rows, "rows");
         if (!this.onInsertRow) {
            throw new PSQLException(GT.tr("Not on the insert row."), PSQLState.INVALID_CURSOR_STATE);
         }

         HashMap<String, Object> updateValues = this.updateValues;
         if (updateValues == null || updateValues.isEmpty()) {
            throw new PSQLException(GT.tr("You must specify at least one column value to insert a row."), PSQLState.INVALID_PARAMETER_VALUE);
         }

         StringBuilder insertSQL = (new StringBuilder("INSERT INTO ")).append(this.tableName).append(" (");
         StringBuilder paramSQL = new StringBuilder(") values (");
         Iterator<String> columnNames = updateValues.keySet().iterator();
         int numColumns = updateValues.size();
         int i = 0;

         while(true) {
            if (!columnNames.hasNext()) {
               insertSQL.append(paramSQL.toString());
               PreparedStatement insertStatement = null;
               Tuple rowBuffer = (Tuple)Nullness.castNonNull(this.rowBuffer);

               try {
                  insertStatement = this.connection.prepareStatement(insertSQL.toString(), 1);
                  Iterator<Object> values = updateValues.values().iterator();
                  int i = 1;

                  while(true) {
                     if (!values.hasNext()) {
                        insertStatement.executeUpdate();
                        if (this.usingOID) {
                           long insertedOID = ((PgStatement)insertStatement).getLastOID();
                           updateValues.put("oid", insertedOID);
                        }

                        this.updateRowBuffer(insertStatement, rowBuffer, (HashMap)Nullness.castNonNull(updateValues));
                        break;
                     }

                     insertStatement.setObject(i, values.next());
                     ++i;
                  }
               } finally {
                  JdbcBlackHole.close((Statement)insertStatement);
               }

               ((List)Nullness.castNonNull(this.rows)).add(rowBuffer);
               this.thisRow = rowBuffer;
               this.clearRowBuffer(false);
               break;
            }

            String columnName = (String)columnNames.next();
            Utils.escapeIdentifier(insertSQL, columnName);
            if (i < numColumns - 1) {
               insertSQL.append(", ");
               paramSQL.append("?,");
            } else {
               paramSQL.append("?)");
            }

            ++i;
         }
      } catch (Throwable var18) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var16) {
               var18.addSuppressed(var16);
            }
         }

         throw var18;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void moveToCurrentRow() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkUpdateable();
         Nullness.castNonNull(this.rows, "rows");
         if (this.currentRow >= 0 && this.currentRow < this.rows.size()) {
            this.initRowBuffer();
         } else {
            this.thisRow = null;
            this.rowBuffer = null;
         }

         this.onInsertRow = false;
         this.doingUpdates = false;
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void moveToInsertRow() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkUpdateable();
         this.clearRowBuffer(false);
         this.onInsertRow = true;
         this.doingUpdates = false;
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   private void clearRowBuffer(boolean copyCurrentRow) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (copyCurrentRow) {
            this.rowBuffer = ((Tuple)Nullness.castNonNull(this.thisRow, "thisRow")).updateableCopy();
         } else {
            this.rowBuffer = new Tuple(this.fields.length);
         }

         HashMap<String, Object> updateValues = this.updateValues;
         if (updateValues != null) {
            updateValues.clear();
         }
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public boolean rowDeleted() throws SQLException {
      this.checkClosed();
      return false;
   }

   public boolean rowInserted() throws SQLException {
      this.checkClosed();
      return false;
   }

   public boolean rowUpdated() throws SQLException {
      this.checkClosed();
      return false;
   }

   public void updateAsciiStream(@Positive int columnIndex, @Nullable InputStream x, int length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      label58: {
         try {
            if (x == null) {
               this.updateNull(columnIndex);
               break label58;
            }

            try {
               InputStreamReader reader = new InputStreamReader(x, StandardCharsets.US_ASCII);
               char[] data = new char[length];
               int numRead = 0;

               while(true) {
                  int n = reader.read(data, numRead, length - numRead);
                  if (n != -1) {
                     numRead += n;
                     if (numRead != length) {
                        continue;
                     }
                  }

                  this.updateString(columnIndex, new String(data, 0, numRead));
                  break;
               }
            } catch (IOException var10) {
               throw new PSQLException(GT.tr("Provided InputStream failed."), (PSQLState)null, var10);
            }
         } catch (Throwable var11) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var9) {
                  var11.addSuppressed(var9);
               }
            }

            throw var11;
         }

         if (ignore != null) {
            ignore.close();
         }

         return;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateBigDecimal(@Positive int columnIndex, @Nullable BigDecimal x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateBinaryStream(@Positive int columnIndex, @Nullable InputStream x, int length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      label61: {
         try {
            if (x == null) {
               this.updateNull(columnIndex);
               break label61;
            }

            byte[] data = new byte[length];
            int numRead = 0;

            try {
               do {
                  int n = x.read(data, numRead, length - numRead);
                  if (n == -1) {
                     break;
                  }

                  numRead += n;
               } while(numRead != length);
            } catch (IOException var9) {
               throw new PSQLException(GT.tr("Provided InputStream failed."), (PSQLState)null, var9);
            }

            if (numRead == length) {
               this.updateBytes(columnIndex, data);
            } else {
               byte[] data2 = new byte[numRead];
               System.arraycopy(data, 0, data2, 0, numRead);
               this.updateBytes(columnIndex, data2);
            }
         } catch (Throwable var10) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var8) {
                  var10.addSuppressed(var8);
               }
            }

            throw var10;
         }

         if (ignore != null) {
            ignore.close();
         }

         return;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateBoolean(@Positive int columnIndex, boolean x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateByte(@Positive int columnIndex, byte x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, String.valueOf(x));
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateBytes(@Positive int columnIndex, @Nullable byte[] x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateCharacterStream(@Positive int columnIndex, @Nullable Reader x, int length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      label58: {
         try {
            if (x == null) {
               this.updateNull(columnIndex);
               break label58;
            }

            try {
               char[] data = new char[length];
               int numRead = 0;

               while(true) {
                  int n = x.read(data, numRead, length - numRead);
                  if (n != -1) {
                     numRead += n;
                     if (numRead != length) {
                        continue;
                     }
                  }

                  this.updateString(columnIndex, new String(data, 0, numRead));
                  break;
               }
            } catch (IOException var9) {
               throw new PSQLException(GT.tr("Provided Reader failed."), (PSQLState)null, var9);
            }
         } catch (Throwable var10) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var8) {
                  var10.addSuppressed(var8);
               }
            }

            throw var10;
         }

         if (ignore != null) {
            ignore.close();
         }

         return;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateDate(@Positive int columnIndex, @Nullable Date x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateDouble(@Positive int columnIndex, double x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateFloat(@Positive int columnIndex, float x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateInt(@Positive int columnIndex, int x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateLong(@Positive int columnIndex, long x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateNull(@Positive int columnIndex) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkColumnIndex(columnIndex);
         String columnTypeName = this.getPGType(columnIndex);
         this.updateValue(columnIndex, new PgResultSet.NullObject(columnTypeName));
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateObject(int columnIndex, @Nullable Object x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateObject(int columnIndex, @Nullable Object x, int scale) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateObject(columnIndex, x);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void refreshRow() throws SQLException {
      this.checkUpdateable();
      if (this.onInsertRow) {
         throw new PSQLException(GT.tr("Can''t refresh the insert row."), PSQLState.INVALID_CURSOR_STATE);
      } else if (!this.isBeforeFirst() && !this.isAfterLast() && !((List)Nullness.castNonNull(this.rows, "rows")).isEmpty()) {
         StringBuilder selectSQL = new StringBuilder("select ");
         ResultSetMetaData rsmd = this.getMetaData();
         PGResultSetMetaData pgmd = (PGResultSetMetaData)rsmd;

         for(int i = 1; i <= rsmd.getColumnCount(); ++i) {
            if (i > 1) {
               selectSQL.append(", ");
            }

            Utils.escapeIdentifier(selectSQL, pgmd.getBaseColumnName(i));
         }

         selectSQL.append(" from ").append(this.onlyTable).append(this.tableName).append(" where ");
         List<PgResultSet.PrimaryKey> primaryKeys = (List)Nullness.castNonNull(this.primaryKeys, "primaryKeys");
         int numKeys = primaryKeys.size();

         for(int i = 0; i < numKeys; ++i) {
            PgResultSet.PrimaryKey primaryKey = (PgResultSet.PrimaryKey)primaryKeys.get(i);
            Utils.escapeIdentifier(selectSQL, primaryKey.name);
            selectSQL.append(" = ?");
            if (i < numKeys - 1) {
               selectSQL.append(" and ");
            }
         }

         String sqlText = selectSQL.toString();
         if (this.connection.getLogger().isLoggable(Level.FINE)) {
            this.connection.getLogger().log(Level.FINE, "selecting {0}", sqlText);
         }

         PreparedStatement selectStatement = null;

         try {
            selectStatement = this.connection.prepareStatement(sqlText, 1004, 1008);

            for(int i = 0; i < numKeys; ++i) {
               selectStatement.setObject(i + 1, ((PgResultSet.PrimaryKey)primaryKeys.get(i)).getValue());
            }

            PgResultSet rs = (PgResultSet)selectStatement.executeQuery();
            if (rs.next()) {
               if (rs.thisRow == null) {
                  this.rowBuffer = null;
               } else {
                  this.rowBuffer = ((Tuple)Nullness.castNonNull(rs.thisRow)).updateableCopy();
               }
            }

            ((List)Nullness.castNonNull(this.rows)).set(this.currentRow, (Tuple)Nullness.castNonNull(this.rowBuffer));
            this.thisRow = this.rowBuffer;
            this.connection.getLogger().log(Level.FINE, "done updates");
            rs.close();
         } finally {
            JdbcBlackHole.close((Statement)selectStatement);
         }

      }
   }

   public void updateRow() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      label221: {
         try {
            this.checkUpdateable();
            if (this.onInsertRow) {
               throw new PSQLException(GT.tr("Cannot call updateRow() when on the insert row."), PSQLState.INVALID_CURSOR_STATE);
            }

            List<Tuple> rows = (List)Nullness.castNonNull(this.rows, "rows");
            if (this.isBeforeFirst() || this.isAfterLast() || rows.isEmpty()) {
               throw new PSQLException(GT.tr("Cannot update the ResultSet because it is either before the start or after the end of the results."), PSQLState.INVALID_CURSOR_STATE);
            }

            if (!this.doingUpdates) {
               break label221;
            }

            StringBuilder updateSQL = new StringBuilder("UPDATE " + this.onlyTable + this.tableName + " SET  ");
            HashMap<String, Object> updateValues = (HashMap)Nullness.castNonNull(this.updateValues);
            int numColumns = updateValues.size();
            Iterator<String> columns = updateValues.keySet().iterator();

            for(int i = 0; columns.hasNext(); ++i) {
               String column = (String)columns.next();
               Utils.escapeIdentifier(updateSQL, column);
               updateSQL.append(" = ?");
               if (i < numColumns - 1) {
                  updateSQL.append(", ");
               }
            }

            updateSQL.append(" WHERE ");
            List<PgResultSet.PrimaryKey> primaryKeys = (List)Nullness.castNonNull(this.primaryKeys, "primaryKeys");
            int numKeys = primaryKeys.size();

            for(int i = 0; i < numKeys; ++i) {
               PgResultSet.PrimaryKey primaryKey = (PgResultSet.PrimaryKey)primaryKeys.get(i);
               Utils.escapeIdentifier(updateSQL, primaryKey.name);
               updateSQL.append(" = ?");
               if (i < numKeys - 1) {
                  updateSQL.append(" and ");
               }
            }

            String sqlText = updateSQL.toString();
            if (this.connection.getLogger().isLoggable(Level.FINE)) {
               this.connection.getLogger().log(Level.FINE, "updating {0}", sqlText);
            }

            PreparedStatement updateStatement = null;

            try {
               updateStatement = this.connection.prepareStatement(sqlText);
               int i = 0;

               for(Iterator iterator = updateValues.values().iterator(); iterator.hasNext(); ++i) {
                  Object o = iterator.next();
                  updateStatement.setObject(i + 1, o);
               }

               int j = 0;

               while(true) {
                  if (j >= numKeys) {
                     updateStatement.executeUpdate();
                     break;
                  }

                  updateStatement.setObject(i + 1, ((PgResultSet.PrimaryKey)primaryKeys.get(j)).getValue());
                  ++j;
                  ++i;
               }
            } finally {
               JdbcBlackHole.close((Statement)updateStatement);
            }

            Tuple rowBuffer = (Tuple)Nullness.castNonNull(this.rowBuffer, "rowBuffer");
            this.updateRowBuffer((PreparedStatement)null, rowBuffer, updateValues);
            this.connection.getLogger().log(Level.FINE, "copying data");
            this.thisRow = rowBuffer.readOnlyCopy();
            rows.set(this.currentRow, rowBuffer);
            this.connection.getLogger().log(Level.FINE, "done updates");
            updateValues.clear();
            this.doingUpdates = false;
         } catch (Throwable var20) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var18) {
                  var20.addSuppressed(var18);
               }
            }

            throw var20;
         }

         if (ignore != null) {
            ignore.close();
         }

         return;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateShort(@Positive int columnIndex, short x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateString(@Positive int columnIndex, @Nullable String x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateTime(@Positive int columnIndex, @Nullable Time x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateTimestamp(int columnIndex, @Nullable Timestamp x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateValue(columnIndex, x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateNull(String columnName) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateNull(this.findColumn(columnName));
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateBoolean(String columnName, boolean x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateBoolean(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateByte(String columnName, byte x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateByte(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateShort(String columnName, short x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateShort(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateInt(String columnName, int x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateInt(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateLong(String columnName, long x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateLong(this.findColumn(columnName), x);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateFloat(String columnName, float x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateFloat(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateDouble(String columnName, double x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateDouble(this.findColumn(columnName), x);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateBigDecimal(String columnName, @Nullable BigDecimal x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateBigDecimal(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateString(String columnName, @Nullable String x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateString(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateBytes(String columnName, @Nullable byte[] x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateBytes(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateDate(String columnName, @Nullable Date x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateDate(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateTime(String columnName, @Nullable Time x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateTime(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateTimestamp(String columnName, @Nullable Timestamp x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateTimestamp(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateAsciiStream(String columnName, @Nullable InputStream x, int length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateAsciiStream(this.findColumn(columnName), x, length);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateBinaryStream(String columnName, @Nullable InputStream x, int length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateBinaryStream(this.findColumn(columnName), x, length);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateCharacterStream(String columnName, @Nullable Reader reader, int length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateCharacterStream(this.findColumn(columnName), reader, length);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateObject(String columnName, @Nullable Object x, int scale) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateObject(this.findColumn(columnName), x);
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void updateObject(String columnName, @Nullable Object x) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.updateObject(this.findColumn(columnName), x);
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   boolean isUpdateable() throws SQLException {
      this.checkClosed();
      if (this.resultsetconcurrency == 1007) {
         throw new PSQLException(GT.tr("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated."), PSQLState.INVALID_CURSOR_STATE);
      } else if (this.updateable) {
         return true;
      } else {
         this.connection.getLogger().log(Level.FINE, "checking if rs is updateable");
         this.parseQuery();
         if (this.tableName == null) {
            this.connection.getLogger().log(Level.FINE, "tableName is not found");
            return false;
         } else if (!this.singleTable) {
            this.connection.getLogger().log(Level.FINE, "not a single table");
            return false;
         } else {
            this.usingOID = false;
            this.connection.getLogger().log(Level.FINE, "getting primary keys");
            List<PgResultSet.PrimaryKey> primaryKeys = new ArrayList();
            this.primaryKeys = primaryKeys;
            int i = 0;
            int numPKcolumns = 0;
            String[] s = quotelessTableName((String)Nullness.castNonNull(this.tableName));
            String quotelessTableName = (String)Nullness.castNonNull(s[0]);
            String quotelessSchemaName = s[1];
            ResultSet rs = ((PgDatabaseMetaData)this.connection.getMetaData()).getPrimaryUniqueKeys("", quotelessSchemaName, quotelessTableName);
            String lastConstraintName = null;

            while(rs.next()) {
               String constraintName = (String)Nullness.castNonNull(rs.getString(6));
               if (lastConstraintName == null || !lastConstraintName.equals(constraintName)) {
                  if (lastConstraintName != null) {
                     if (i == numPKcolumns && numPKcolumns > 0) {
                        break;
                     }

                     this.connection.getLogger().log(Level.FINE, "no of keys={0} from constraint {1}", new Object[]{i, lastConstraintName});
                  }

                  i = 0;
                  numPKcolumns = 0;
                  primaryKeys.clear();
                  lastConstraintName = constraintName;
               }

               ++numPKcolumns;
               boolean isNotNull = rs.getBoolean("IS_NOT_NULL");
               if (isNotNull) {
                  String columnName = (String)Nullness.castNonNull(rs.getString(4));
                  int index = this.findColumnIndex(columnName);
                  if (index > 0) {
                     ++i;
                     primaryKeys.add(new PgResultSet.PrimaryKey(index, columnName));
                  }
               }
            }

            rs.close();
            this.connection.getLogger().log(Level.FINE, "no of keys={0} from constraint {1}", new Object[]{i, lastConstraintName});
            this.updateable = i == numPKcolumns && numPKcolumns > 0;
            this.connection.getLogger().log(Level.FINE, "checking primary key {0}", this.updateable);
            if (!this.updateable) {
               int oidIndex = this.findColumnIndex("oid");
               if (oidIndex > 0) {
                  primaryKeys.add(new PgResultSet.PrimaryKey(oidIndex, "oid"));
                  this.usingOID = true;
                  this.updateable = true;
               }
            }

            if (!this.updateable) {
               throw new PSQLException(GT.tr("No eligible primary or unique key found for table {0}.", this.tableName), PSQLState.INVALID_CURSOR_STATE);
            } else {
               return this.updateable;
            }
         }
      }
   }

   public void setAdaptiveFetch(boolean adaptiveFetch) throws SQLException {
      this.checkClosed();
      this.updateQueryInsideAdaptiveFetchCache(adaptiveFetch);
      this.adaptiveFetch = adaptiveFetch;
   }

   private void updateQueryInsideAdaptiveFetchCache(boolean newAdaptiveFetch) {
      if (Objects.nonNull(this.cursor)) {
         ResultCursor resultCursor = this.cursor;
         if (!this.adaptiveFetch && newAdaptiveFetch) {
            this.connection.getQueryExecutor().addQueryToAdaptiveFetchCache(true, resultCursor);
         }

         if (this.adaptiveFetch && !newAdaptiveFetch && Objects.nonNull(this.cursor)) {
            this.connection.getQueryExecutor().removeQueryFromAdaptiveFetchCache(true, resultCursor);
         }
      }

   }

   public boolean getAdaptiveFetch() throws SQLException {
      this.checkClosed();
      return this.adaptiveFetch;
   }

   public static String[] quotelessTableName(String fullname) {
      String[] parts = new String[]{null, ""};
      StringBuilder acc = new StringBuilder();
      boolean betweenQuotes = false;

      for(int i = 0; i < fullname.length(); ++i) {
         char c = fullname.charAt(i);
         switch(c) {
         case '"':
            if (i < fullname.length() - 1 && fullname.charAt(i + 1) == '"') {
               ++i;
               acc.append(c);
               break;
            }

            betweenQuotes = !betweenQuotes;
            break;
         case '.':
            if (betweenQuotes) {
               acc.append(c);
            } else {
               parts[1] = acc.toString();
               acc = new StringBuilder();
            }
            break;
         default:
            acc.append(betweenQuotes ? c : Character.toLowerCase(c));
         }
      }

      parts[0] = acc.toString();
      return parts;
   }

   private void parseQuery() {
      Query originalQuery = this.originalQuery;
      if (originalQuery != null) {
         String sql = originalQuery.toString((ParameterList)null);
         StringTokenizer st = new StringTokenizer(sql, " \r\t\n");
         boolean tableFound = false;
         boolean tablesChecked = false;
         String name = "";
         this.singleTable = true;

         while(!tableFound && !tablesChecked && st.hasMoreTokens()) {
            name = st.nextToken();
            if ("from".equalsIgnoreCase(name)) {
               this.tableName = st.nextToken();
               if ("only".equalsIgnoreCase(this.tableName)) {
                  this.tableName = st.nextToken();
                  this.onlyTable = "ONLY ";
               }

               tableFound = true;
            }
         }

      }
   }

   private void setRowBufferColumn(Tuple rowBuffer, int columnIndex, @Nullable Object valueObject) throws SQLException {
      if (valueObject instanceof PGobject) {
         String value = ((PGobject)valueObject).getValue();
         rowBuffer.set(columnIndex, value == null ? null : this.connection.encodeString(value));
      } else {
         if (valueObject == null) {
            rowBuffer.set(columnIndex, (byte[])null);
            return;
         }

         switch(this.getSQLType(columnIndex + 1)) {
         case -7:
         case 16:
            rowBuffer.set(columnIndex, this.connection.encodeString((Boolean)valueObject ? "t" : "f"));
            break;
         case -4:
         case -3:
         case -2:
            if (this.isBinary(columnIndex + 1)) {
               rowBuffer.set(columnIndex, (byte[])valueObject);
            } else {
               try {
                  rowBuffer.set(columnIndex, PGbytea.toPGString((byte[])valueObject).getBytes(this.connection.getEncoding().name()));
               } catch (UnsupportedEncodingException var5) {
                  throw new PSQLException(GT.tr("The JVM claims not to support the encoding: {0}", this.connection.getEncoding().name()), PSQLState.UNEXPECTED_ERROR, var5);
               }
            }
         case 0:
            break;
         case 91:
            rowBuffer.set(columnIndex, this.connection.encodeString(this.getTimestampUtils().toString(this.getDefaultCalendar(), (Date)valueObject)));
            break;
         case 92:
            rowBuffer.set(columnIndex, this.connection.encodeString(this.getTimestampUtils().toString(this.getDefaultCalendar(), (Time)valueObject)));
            break;
         case 93:
            rowBuffer.set(columnIndex, this.connection.encodeString(this.getTimestampUtils().toString(this.getDefaultCalendar(), (Timestamp)valueObject)));
            break;
         default:
            rowBuffer.set(columnIndex, this.connection.encodeString(String.valueOf(valueObject)));
         }
      }

   }

   private void updateRowBuffer(@Nullable PreparedStatement insertStatement, Tuple rowBuffer, HashMap<String, Object> updateValues) throws SQLException {
      Iterator var4 = updateValues.entrySet().iterator();

      int numKeys;
      while(var4.hasNext()) {
         Entry<String, Object> entry = (Entry)var4.next();
         numKeys = this.findColumn((String)entry.getKey()) - 1;
         Object valueObject = entry.getValue();
         this.setRowBufferColumn(rowBuffer, numKeys, valueObject);
      }

      if (insertStatement != null) {
         ResultSet generatedKeys = insertStatement.getGeneratedKeys();

         try {
            generatedKeys.next();
            List<PgResultSet.PrimaryKey> primaryKeys = (List)Nullness.castNonNull(this.primaryKeys);
            numKeys = primaryKeys.size();

            for(int i = 0; i < numKeys; ++i) {
               PgResultSet.PrimaryKey key = (PgResultSet.PrimaryKey)primaryKeys.get(i);
               int columnIndex = key.index - 1;
               Object valueObject = generatedKeys.getObject(key.name);
               this.setRowBufferColumn(rowBuffer, columnIndex, valueObject);
            }
         } finally {
            generatedKeys.close();
         }

      }
   }

   public BaseStatement getPGStatement() {
      return this.statement;
   }

   @Nullable
   public String getRefCursor() {
      return this.refCursorName;
   }

   private void setRefCursor(String refCursorName) {
      this.refCursorName = refCursorName;
   }

   public void setFetchSize(int rows) throws SQLException {
      this.checkClosed();
      if (rows < 0) {
         throw new PSQLException(GT.tr("Fetch size must be a value greater to or equal to 0."), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         this.fetchSize = rows;
      }
   }

   public int getFetchSize() throws SQLException {
      this.checkClosed();
      return this.adaptiveFetch ? this.lastUsedFetchSize : this.fetchSize;
   }

   public int getLastUsedFetchSize() throws SQLException {
      this.checkClosed();
      return this.lastUsedFetchSize;
   }

   public boolean next() throws SQLException {
      this.checkClosed();
      Nullness.castNonNull(this.rows, "rows");
      if (this.onInsertRow) {
         throw new PSQLException(GT.tr("Can''t use relative move methods while on the insert row."), PSQLState.INVALID_CURSOR_STATE);
      } else {
         if (this.currentRow + 1 < this.rows.size()) {
            ++this.currentRow;
         } else {
            ResultCursor cursor = this.cursor;
            if (cursor == null || this.maxRows > 0 && this.rowOffset + this.rows.size() >= this.maxRows) {
               this.currentRow = this.rows.size();
               this.thisRow = null;
               this.rowBuffer = null;
               return false;
            }

            this.rowOffset += this.rows.size();
            int fetchRows = this.fetchSize;
            int adaptiveFetchRows = this.connection.getQueryExecutor().getAdaptiveFetchSize(this.adaptiveFetch, cursor);
            if (adaptiveFetchRows != -1) {
               fetchRows = adaptiveFetchRows;
            }

            if (this.maxRows != 0 && (fetchRows == 0 || this.rowOffset + fetchRows > this.maxRows)) {
               fetchRows = this.maxRows - this.rowOffset;
            }

            this.connection.getQueryExecutor().fetch(cursor, new PgResultSet.CursorResultHandler(), fetchRows, this.adaptiveFetch);
            this.closeRefCursor();
            this.lastUsedFetchSize = fetchRows;
            this.currentRow = 0;
            if (this.rows == null || this.rows.isEmpty()) {
               this.thisRow = null;
               this.rowBuffer = null;
               return false;
            }
         }

         this.initRowBuffer();
         return true;
      }
   }

   public void close() throws SQLException {
      try {
         this.closeInternally();
      } finally {
         ((PgStatement)this.statement).checkCompletion();
      }

   }

   protected void closeInternally() throws SQLException {
      this.rows = null;
      JdbcBlackHole.close((Statement)this.deleteStatement);
      this.deleteStatement = null;
      if (this.cursor != null) {
         this.cursor.close();
         this.cursor = null;
      }

      this.closeRefCursor();
   }

   private void closeRefCursor() throws SQLException {
      String refCursorName = this.refCursorName;
      if (refCursorName != null && this.cursor == null) {
         try {
            if (this.connection.getTransactionState() == TransactionState.OPEN) {
               StringBuilder sb = new StringBuilder("CLOSE ");
               Utils.escapeIdentifier(sb, refCursorName);
               this.connection.execSQLUpdate(sb.toString());
            }
         } finally {
            this.refCursorName = null;
         }

      }
   }

   public boolean wasNull() throws SQLException {
      this.checkClosed();
      return this.wasNullFlag;
   }

   @Pure
   @Nullable
   public String getString(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getString columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return null;
      } else if (this.isBinary(columnIndex) && this.getSQLType(columnIndex) != 12) {
         Field field = this.fields[columnIndex - 1];
         TimestampUtils ts = this.getTimestampUtils();
         switch(field.getOID()) {
         case 1082:
            return ts.toString(ts.toLocalDateBin(value));
         case 1083:
            return ts.toString(ts.toLocalTimeBin(value));
         case 1114:
            return ts.toString(ts.toLocalDateTimeBin(value));
         case 1184:
            return ts.toStringOffsetDateTime(value);
         case 1266:
            return ts.toStringOffsetTimeBin(value);
         default:
            Nullness.castNonNull(this.thisRow, "thisRow");
            Object obj = this.internalGetObject(columnIndex, field);
            if (obj == null) {
               obj = this.getObject(columnIndex);
               return obj == null ? null : obj.toString();
            } else {
               return "hstore".equals(this.getPGType(columnIndex)) ? HStoreConverter.toString((Map)obj) : this.trimString(columnIndex, obj.toString());
            }
         }
      } else {
         Encoding encoding = this.connection.getEncoding();

         try {
            return this.trimString(columnIndex, encoding.decode(value));
         } catch (IOException var6) {
            throw new PSQLException(GT.tr("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database."), PSQLState.DATA_ERROR, var6);
         }
      }
   }

   @Pure
   public boolean getBoolean(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getBoolean columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return false;
      } else {
         int col = columnIndex - 1;
         if (16 != this.fields[col].getOID()) {
            if (this.isBinary(columnIndex)) {
               return BooleanTypeUtil.castToBoolean(this.readDoubleValue(value, this.fields[col].getOID(), "boolean"));
            } else {
               String stringValue = (String)Nullness.castNonNull(this.getString(columnIndex));
               return BooleanTypeUtil.castToBoolean(stringValue);
            }
         } else {
            return 1 == value.length && (116 == value[0] && !this.isBinary(columnIndex) || 1 == value[0] && this.isBinary(columnIndex));
         }
      }
   }

   public byte getByte(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getByte columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return 0;
      } else if (this.isBinary(columnIndex)) {
         int col = columnIndex - 1;
         return (byte)((int)this.readLongValue(value, this.fields[col].getOID(), -128L, 127L, "byte"));
      } else {
         Encoding encoding = this.connection.getEncoding();
         if (encoding.hasAsciiNumbers()) {
            try {
               return (byte)((int)NumberParser.getFastLong(value, -128L, 127L));
            } catch (NumberFormatException var12) {
            }
         }

         String s = this.getString(columnIndex);
         if (s != null) {
            s = s.trim();
            if (s.isEmpty()) {
               return 0;
            } else {
               try {
                  return Byte.parseByte(s);
               } catch (NumberFormatException var11) {
                  try {
                     BigDecimal n = new BigDecimal(s);
                     BigInteger i = n.toBigInteger();
                     int gt = i.compareTo(BYTEMAX);
                     int lt = i.compareTo(BYTEMIN);
                     if (gt <= 0 && lt >= 0) {
                        return i.byteValue();
                     } else {
                        throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "byte", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                     }
                  } catch (NumberFormatException var10) {
                     throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "byte", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                  }
               }
            }
         } else {
            return 0;
         }
      }
   }

   public short getShort(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getShort columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return 0;
      } else if (this.isBinary(columnIndex)) {
         int col = columnIndex - 1;
         int oid = this.fields[col].getOID();
         return oid == 21 ? ByteConverter.int2(value, 0) : (short)((int)this.readLongValue(value, oid, -32768L, 32767L, "short"));
      } else {
         Encoding encoding = this.connection.getEncoding();
         if (encoding.hasAsciiNumbers()) {
            try {
               return (short)((int)NumberParser.getFastLong(value, -32768L, 32767L));
            } catch (NumberFormatException var5) {
            }
         }

         return toShort(this.getFixedString(columnIndex));
      }
   }

   @Pure
   public int getInt(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getInt columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return 0;
      } else if (this.isBinary(columnIndex)) {
         int col = columnIndex - 1;
         int oid = this.fields[col].getOID();
         return oid == 23 ? ByteConverter.int4(value, 0) : (int)this.readLongValue(value, oid, -2147483648L, 2147483647L, "int");
      } else {
         Encoding encoding = this.connection.getEncoding();
         if (encoding.hasAsciiNumbers()) {
            try {
               return (int)NumberParser.getFastLong(value, -2147483648L, 2147483647L);
            } catch (NumberFormatException var5) {
            }
         }

         return toInt(this.getFixedString(columnIndex));
      }
   }

   @Pure
   public long getLong(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getLong columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return 0L;
      } else if (this.isBinary(columnIndex)) {
         int col = columnIndex - 1;
         int oid = this.fields[col].getOID();
         return oid == 20 ? ByteConverter.int8(value, 0) : this.readLongValue(value, oid, Long.MIN_VALUE, Long.MAX_VALUE, "long");
      } else {
         Encoding encoding = this.connection.getEncoding();
         if (encoding.hasAsciiNumbers()) {
            try {
               return NumberParser.getFastLong(value, Long.MIN_VALUE, Long.MAX_VALUE);
            } catch (NumberFormatException var5) {
            }
         }

         return toLong(this.getFixedString(columnIndex));
      }
   }

   private BigDecimal getFastBigDecimal(byte[] bytes) throws NumberFormatException {
      if (bytes.length == 0) {
         throw FAST_NUMBER_FAILED;
      } else {
         int scale = 0;
         long val = 0L;
         int start;
         boolean neg;
         if (bytes[0] == 45) {
            neg = true;
            start = 1;
            if (bytes.length == 1 || bytes.length > 19) {
               throw FAST_NUMBER_FAILED;
            }
         } else {
            start = 0;
            neg = false;
            if (bytes.length > 18) {
               throw FAST_NUMBER_FAILED;
            }
         }

         int periodsSeen = 0;

         while(true) {
            while(start < bytes.length) {
               byte b = bytes[start++];
               if (b < 48 || b > 57) {
                  if (b != 46 || periodsSeen != 0) {
                     throw FAST_NUMBER_FAILED;
                  }

                  scale = bytes.length - start;
                  ++periodsSeen;
               } else {
                  val *= 10L;
                  val += (long)(b - 48);
               }
            }

            int numNonSignChars = neg ? bytes.length - 1 : bytes.length;
            if (periodsSeen <= 1 && periodsSeen != numNonSignChars) {
               if (neg) {
                  val = -val;
               }

               return BigDecimal.valueOf(val, scale);
            }

            throw FAST_NUMBER_FAILED;
         }
      }
   }

   @Pure
   public float getFloat(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getFloat columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return 0.0F;
      } else if (this.isBinary(columnIndex)) {
         int col = columnIndex - 1;
         int oid = this.fields[col].getOID();
         return oid == 700 ? ByteConverter.float4(value, 0) : (float)this.readDoubleValue(value, oid, "float");
      } else {
         return toFloat(this.getFixedString(columnIndex));
      }
   }

   @Pure
   public double getDouble(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getDouble columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return 0.0D;
      } else if (this.isBinary(columnIndex)) {
         int col = columnIndex - 1;
         int oid = this.fields[col].getOID();
         return oid == 701 ? ByteConverter.float8(value, 0) : this.readDoubleValue(value, oid, "double");
      } else {
         return toDouble(this.getFixedString(columnIndex));
      }
   }

   @Nullable
   public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getBigDecimal columnIndex: {0}", columnIndex);
      return (BigDecimal)this.getNumeric(columnIndex, scale, false);
   }

   @Pure
   @Nullable
   private Number getNumeric(int columnIndex, int scale, boolean allowNaN) throws SQLException {
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return null;
      } else if (this.isBinary(columnIndex)) {
         int sqlType = this.getSQLType(columnIndex);
         if (sqlType != 2 && sqlType != 3) {
            Object obj = this.internalGetObject(columnIndex, this.fields[columnIndex - 1]);
            if (obj == null) {
               return null;
            } else if (!(obj instanceof Long) && !(obj instanceof Integer) && !(obj instanceof Byte)) {
               return this.toBigDecimal(this.trimMoney(String.valueOf(obj)), scale);
            } else {
               BigDecimal res = BigDecimal.valueOf(((Number)obj).longValue());
               res = this.scaleBigDecimal(res, scale);
               return res;
            }
         } else {
            Number num = ByteConverter.numeric(value);
            return (Number)(allowNaN && Double.isNaN(num.doubleValue()) ? Double.NaN : num);
         }
      } else {
         Encoding encoding = this.connection.getEncoding();
         if (encoding.hasAsciiNumbers()) {
            try {
               BigDecimal res = this.getFastBigDecimal(value);
               res = this.scaleBigDecimal(res, scale);
               return res;
            } catch (NumberFormatException var8) {
            }
         }

         String stringValue = this.getFixedString(columnIndex);
         return (Number)(allowNaN && "NaN".equalsIgnoreCase(stringValue) ? Double.NaN : this.toBigDecimal(stringValue, scale));
      }
   }

   @Pure
   @Nullable
   public byte[] getBytes(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getBytes columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return null;
      } else if (this.isBinary(columnIndex)) {
         return value;
      } else {
         return this.fields[columnIndex - 1].getOID() == 17 ? this.trimBytes(columnIndex, PGbytea.toBytes(value)) : this.trimBytes(columnIndex, value);
      }
   }

   @Pure
   @Nullable
   public Date getDate(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getDate columnIndex: {0}", columnIndex);
      return this.getDate(columnIndex, (Calendar)null);
   }

   @Pure
   @Nullable
   public Time getTime(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getTime columnIndex: {0}", columnIndex);
      return this.getTime(columnIndex, (Calendar)null);
   }

   @Pure
   @Nullable
   public Timestamp getTimestamp(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getTimestamp columnIndex: {0}", columnIndex);
      return this.getTimestamp(columnIndex, (Calendar)null);
   }

   @Pure
   @Nullable
   public InputStream getAsciiStream(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getAsciiStream columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return null;
      } else {
         String stringValue = (String)Nullness.castNonNull(this.getString(columnIndex));
         return new ByteArrayInputStream(stringValue.getBytes(StandardCharsets.US_ASCII));
      }
   }

   @Pure
   @Nullable
   public InputStream getUnicodeStream(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getUnicodeStream columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return null;
      } else {
         String stringValue = (String)Nullness.castNonNull(this.getString(columnIndex));
         return new ByteArrayInputStream(stringValue.getBytes(StandardCharsets.UTF_8));
      }
   }

   @Pure
   @Nullable
   public InputStream getBinaryStream(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getBinaryStream columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return null;
      } else {
         byte[] b = this.getBytes(columnIndex);
         return b != null ? new ByteArrayInputStream(b) : null;
      }
   }

   @Pure
   @Nullable
   public String getString(String columnName) throws SQLException {
      return this.getString(this.findColumn(columnName));
   }

   @Pure
   public boolean getBoolean(String columnName) throws SQLException {
      return this.getBoolean(this.findColumn(columnName));
   }

   @Pure
   public byte getByte(String columnName) throws SQLException {
      return this.getByte(this.findColumn(columnName));
   }

   @Pure
   public short getShort(String columnName) throws SQLException {
      return this.getShort(this.findColumn(columnName));
   }

   @Pure
   public int getInt(String columnName) throws SQLException {
      return this.getInt(this.findColumn(columnName));
   }

   @Pure
   public long getLong(String columnName) throws SQLException {
      return this.getLong(this.findColumn(columnName));
   }

   @Pure
   public float getFloat(String columnName) throws SQLException {
      return this.getFloat(this.findColumn(columnName));
   }

   @Pure
   public double getDouble(String columnName) throws SQLException {
      return this.getDouble(this.findColumn(columnName));
   }

   @Pure
   @Nullable
   public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
      return this.getBigDecimal(this.findColumn(columnName), scale);
   }

   @Pure
   @Nullable
   public byte[] getBytes(String columnName) throws SQLException {
      return this.getBytes(this.findColumn(columnName));
   }

   @Pure
   @Nullable
   public Date getDate(String columnName) throws SQLException {
      return this.getDate(this.findColumn(columnName), (Calendar)null);
   }

   @Pure
   @Nullable
   public Time getTime(String columnName) throws SQLException {
      return this.getTime(this.findColumn(columnName), (Calendar)null);
   }

   @Pure
   @Nullable
   public Timestamp getTimestamp(String columnName) throws SQLException {
      return this.getTimestamp(this.findColumn(columnName), (Calendar)null);
   }

   @Pure
   @Nullable
   public InputStream getAsciiStream(String columnName) throws SQLException {
      return this.getAsciiStream(this.findColumn(columnName));
   }

   @Pure
   @Nullable
   public InputStream getUnicodeStream(String columnName) throws SQLException {
      return this.getUnicodeStream(this.findColumn(columnName));
   }

   @Pure
   @Nullable
   public InputStream getBinaryStream(String columnName) throws SQLException {
      return this.getBinaryStream(this.findColumn(columnName));
   }

   @Pure
   @Nullable
   public SQLWarning getWarnings() throws SQLException {
      this.checkClosed();
      return this.warnings;
   }

   public void clearWarnings() throws SQLException {
      this.checkClosed();
      this.warnings = null;
   }

   protected void addWarning(SQLWarning warnings) {
      if (this.warnings != null) {
         this.warnings.setNextWarning(warnings);
      } else {
         this.warnings = warnings;
      }

   }

   @Nullable
   public String getCursorName() throws SQLException {
      this.checkClosed();
      return null;
   }

   @Nullable
   public Object getObject(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getObject columnIndex: {0}", columnIndex);
      byte[] value = this.getRawValue(columnIndex);
      if (value == null) {
         return null;
      } else {
         Field field = this.fields[columnIndex - 1];
         if (field == null) {
            this.wasNullFlag = true;
            return null;
         } else {
            Object result = this.internalGetObject(columnIndex, field);
            if (result != null) {
               return result;
            } else if (this.isBinary(columnIndex)) {
               return this.connection.getObject(this.getPGType(columnIndex), (String)null, value);
            } else {
               String stringValue = (String)Nullness.castNonNull(this.getString(columnIndex));
               return this.connection.getObject(this.getPGType(columnIndex), stringValue, (byte[])null);
            }
         }
      }
   }

   @Nullable
   public Object getObject(String columnName) throws SQLException {
      return this.getObject(this.findColumn(columnName));
   }

   @NonNegative
   public int findColumn(String columnName) throws SQLException {
      this.checkClosed();
      int col = this.findColumnIndex(columnName);
      if (col == 0) {
         throw new PSQLException(GT.tr("The column name {0} was not found in this ResultSet.", columnName), PSQLState.UNDEFINED_COLUMN);
      } else {
         return col;
      }
   }

   public static Map<String, Integer> createColumnNameIndexMap(Field[] fields, boolean isSanitiserDisabled) {
      Map<String, Integer> columnNameIndexMap = new HashMap(fields.length * 2);

      for(int i = fields.length - 1; i >= 0; --i) {
         String columnLabel = fields[i].getColumnLabel();
         if (isSanitiserDisabled) {
            columnNameIndexMap.put(columnLabel, i + 1);
         } else {
            columnNameIndexMap.put(columnLabel.toLowerCase(Locale.US), i + 1);
         }
      }

      return columnNameIndexMap;
   }

   @NonNegative
   private int findColumnIndex(String columnName) {
      if (this.columnNameIndexMap == null) {
         if (this.originalQuery != null) {
            this.columnNameIndexMap = this.originalQuery.getResultSetColumnNameIndexMap();
         }

         if (this.columnNameIndexMap == null) {
            this.columnNameIndexMap = createColumnNameIndexMap(this.fields, this.connection.isColumnSanitiserDisabled());
         }
      }

      Integer index = (Integer)this.columnNameIndexMap.get(columnName);
      if (index != null) {
         return index;
      } else {
         index = (Integer)this.columnNameIndexMap.get(columnName.toLowerCase(Locale.US));
         if (index != null) {
            this.columnNameIndexMap.put(columnName, index);
            return index;
         } else {
            index = (Integer)this.columnNameIndexMap.get(columnName.toUpperCase(Locale.US));
            if (index != null) {
               this.columnNameIndexMap.put(columnName, index);
               return index;
            } else {
               return 0;
            }
         }
      }
   }

   public int getColumnOID(int field) {
      return this.fields[field - 1].getOID();
   }

   @Nullable
   public String getFixedString(int col) throws SQLException {
      String stringValue = (String)Nullness.castNonNull(this.getString(col));
      return this.trimMoney(stringValue);
   }

   @PolyNull
   private String trimMoney(@PolyNull String s) {
      if (s == null) {
         return null;
      } else if (s.length() < 2) {
         return s;
      } else {
         char ch = s.charAt(0);
         if (ch > '-') {
            return s;
         } else {
            if (ch == '(') {
               s = "-" + PGtokenizer.removePara(s).substring(1);
            } else if (ch == '$') {
               s = s.substring(1);
            } else if (ch == '-' && s.charAt(1) == '$') {
               s = "-" + s.substring(2);
            }

            return s;
         }
      }
   }

   @Pure
   protected String getPGType(@Positive int column) throws SQLException {
      Field field = this.fields[column - 1];
      this.initSqlType(field);
      return field.getPGType();
   }

   @Pure
   protected int getSQLType(@Positive int column) throws SQLException {
      Field field = this.fields[column - 1];
      this.initSqlType(field);
      return field.getSQLType();
   }

   @Pure
   private void initSqlType(Field field) throws SQLException {
      if (!field.isTypeInitialized()) {
         TypeInfo typeInfo = this.connection.getTypeInfo();
         int oid = field.getOID();
         String pgType = (String)Nullness.castNonNull(typeInfo.getPGType(oid));
         int sqlType = typeInfo.getSQLType(pgType);
         field.setSQLType(sqlType);
         field.setPGType(pgType);
      }
   }

   @EnsuresNonNull({"updateValues", "rows"})
   private void checkUpdateable() throws SQLException {
      this.checkClosed();
      if (!this.isUpdateable()) {
         throw new PSQLException(GT.tr("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details."), PSQLState.INVALID_CURSOR_STATE);
      } else {
         if (this.updateValues == null) {
            this.updateValues = new HashMap((int)((double)this.fields.length / 0.75D), 0.75F);
         }

         Nullness.castNonNull(this.updateValues, "updateValues");
         Nullness.castNonNull(this.rows, "rows");
      }
   }

   @Pure
   @EnsuresNonNull({"rows"})
   protected void checkClosed() throws SQLException {
      if (this.rows == null) {
         throw new PSQLException(GT.tr("This ResultSet is closed."), PSQLState.OBJECT_NOT_IN_STATE);
      }
   }

   protected boolean isResultSetClosed() {
      return this.rows == null;
   }

   @Pure
   protected void checkColumnIndex(@Positive int column) throws SQLException {
      if (column < 1 || column > this.fields.length) {
         throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", column, this.fields.length), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   @EnsuresNonNull({"thisRow"})
   @Nullable
   protected byte[] getRawValue(@Positive int column) throws SQLException {
      this.checkClosed();
      if (this.thisRow == null) {
         throw new PSQLException(GT.tr("ResultSet not positioned properly, perhaps you need to call next."), PSQLState.INVALID_CURSOR_STATE);
      } else {
         this.checkColumnIndex(column);
         byte[] bytes = this.thisRow.get(column - 1);
         this.wasNullFlag = bytes == null;
         return bytes;
      }
   }

   @Pure
   protected boolean isBinary(@Positive int column) {
      return this.fields[column - 1].getFormat() == 1;
   }

   public static short toShort(@Nullable String s) throws SQLException {
      if (s != null) {
         try {
            s = s.trim();
            return Short.parseShort(s);
         } catch (NumberFormatException var7) {
            try {
               BigDecimal n = new BigDecimal(s);
               BigInteger i = n.toBigInteger();
               int gt = i.compareTo(SHORTMAX);
               int lt = i.compareTo(SHORTMIN);
               if (gt <= 0 && lt >= 0) {
                  return i.shortValue();
               } else {
                  throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "short", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
               }
            } catch (NumberFormatException var6) {
               throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "short", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
         }
      } else {
         return 0;
      }
   }

   public static int toInt(@Nullable String s) throws SQLException {
      if (s != null) {
         try {
            s = s.trim();
            return Integer.parseInt(s);
         } catch (NumberFormatException var7) {
            try {
               BigDecimal n = new BigDecimal(s);
               BigInteger i = n.toBigInteger();
               int gt = i.compareTo(INTMAX);
               int lt = i.compareTo(INTMIN);
               if (gt <= 0 && lt >= 0) {
                  return i.intValue();
               } else {
                  throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "int", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
               }
            } catch (NumberFormatException var6) {
               throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "int", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
         }
      } else {
         return 0;
      }
   }

   public static long toLong(@Nullable String s) throws SQLException {
      if (s != null) {
         try {
            s = s.trim();
            return Long.parseLong(s);
         } catch (NumberFormatException var7) {
            try {
               BigDecimal n = new BigDecimal(s);
               BigInteger i = n.toBigInteger();
               int gt = i.compareTo(LONGMAX);
               int lt = i.compareTo(LONGMIN);
               if (gt <= 0 && lt >= 0) {
                  return i.longValue();
               } else {
                  throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "long", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
               }
            } catch (NumberFormatException var6) {
               throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "long", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
         }
      } else {
         return 0L;
      }
   }

   @PolyNull
   public static BigDecimal toBigDecimal(@PolyNull String s) throws SQLException {
      if (s == null) {
         return null;
      } else {
         try {
            s = s.trim();
            return new BigDecimal(s);
         } catch (NumberFormatException var2) {
            throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "BigDecimal", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
         }
      }
   }

   @PolyNull
   public BigDecimal toBigDecimal(@PolyNull String s, int scale) throws SQLException {
      if (s == null) {
         return null;
      } else {
         BigDecimal val = toBigDecimal(s);
         return this.scaleBigDecimal(val, scale);
      }
   }

   private BigDecimal scaleBigDecimal(BigDecimal val, int scale) throws PSQLException {
      if (scale == -1) {
         return val;
      } else {
         try {
            return val.setScale(scale);
         } catch (ArithmeticException var4) {
            throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "BigDecimal", val), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
         }
      }
   }

   public static float toFloat(@Nullable String s) throws SQLException {
      if (s != null) {
         try {
            s = s.trim();
            return Float.parseFloat(s);
         } catch (NumberFormatException var2) {
            throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "float", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
         }
      } else {
         return 0.0F;
      }
   }

   public static double toDouble(@Nullable String s) throws SQLException {
      if (s != null) {
         try {
            s = s.trim();
            return Double.parseDouble(s);
         } catch (NumberFormatException var2) {
            throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "double", s), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
         }
      } else {
         return 0.0D;
      }
   }

   @RequiresNonNull({"rows"})
   private void initRowBuffer() {
      this.thisRow = (Tuple)((List)Nullness.castNonNull(this.rows, "rows")).get(this.currentRow);
      if (this.resultsetconcurrency == 1008) {
         this.rowBuffer = this.thisRow.updateableCopy();
      } else {
         this.rowBuffer = null;
      }

   }

   private boolean isColumnTrimmable(@Positive int columnIndex) throws SQLException {
      switch(this.getSQLType(columnIndex)) {
      case -4:
      case -3:
      case -2:
      case -1:
      case 1:
      case 12:
         return true;
      case 0:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      default:
         return false;
      }
   }

   private byte[] trimBytes(@Positive int columnIndex, byte[] bytes) throws SQLException {
      if (this.maxFieldSize > 0 && bytes.length > this.maxFieldSize && this.isColumnTrimmable(columnIndex)) {
         byte[] newBytes = new byte[this.maxFieldSize];
         System.arraycopy(bytes, 0, newBytes, 0, this.maxFieldSize);
         return newBytes;
      } else {
         return bytes;
      }
   }

   private String trimString(@Positive int columnIndex, String string) throws SQLException {
      return this.maxFieldSize > 0 && string.length() > this.maxFieldSize && this.isColumnTrimmable(columnIndex) ? string.substring(0, this.maxFieldSize) : string;
   }

   private double readDoubleValue(byte[] bytes, int oid, String targetType) throws PSQLException {
      switch(oid) {
      case 20:
         return (double)ByteConverter.int8(bytes, 0);
      case 21:
         return (double)ByteConverter.int2(bytes, 0);
      case 23:
         return (double)ByteConverter.int4(bytes, 0);
      case 700:
         return (double)ByteConverter.float4(bytes, 0);
      case 701:
         return ByteConverter.float8(bytes, 0);
      case 1700:
         return ByteConverter.numeric(bytes).doubleValue();
      default:
         throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), targetType), PSQLState.DATA_TYPE_MISMATCH);
      }
   }

   @Pure
   private long readLongValue(byte[] bytes, int oid, long minVal, long maxVal, String targetType) throws PSQLException {
      long val;
      switch(oid) {
      case 20:
         val = ByteConverter.int8(bytes, 0);
         break;
      case 21:
         val = (long)ByteConverter.int2(bytes, 0);
         break;
      case 23:
         val = (long)ByteConverter.int4(bytes, 0);
         break;
      case 700:
         float f = ByteConverter.float4(bytes, 0);
         if (!(f <= LONG_MAX_FLOAT) || !(f >= LONG_MIN_FLOAT)) {
            throw new PSQLException(GT.tr("Bad value for type {0} : {1}", targetType, f), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
         }

         val = (long)f;
         break;
      case 701:
         double d = ByteConverter.float8(bytes, 0);
         if (d <= LONG_MAX_DOUBLE && d >= LONG_MIN_DOUBLE) {
            val = (long)d;
            break;
         }

         throw new PSQLException(GT.tr("Bad value for type {0} : {1}", targetType, d), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
      case 1700:
         Number num = ByteConverter.numeric(bytes);
         BigInteger i = ((BigDecimal)num).toBigInteger();
         int gt = i.compareTo(LONGMAX);
         int lt = i.compareTo(LONGMIN);
         if (gt <= 0 && lt >= 0) {
            val = num.longValue();
            break;
         }

         throw new PSQLException(GT.tr("Bad value for type {0} : {1}", "long", num), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
      default:
         throw new PSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", Oid.toString(oid), targetType), PSQLState.DATA_TYPE_MISMATCH);
      }

      if (val >= minVal && val <= maxVal) {
         return val;
      } else {
         throw new PSQLException(GT.tr("Bad value for type {0} : {1}", targetType, val), PSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
      }
   }

   protected void updateValue(@Positive int columnIndex, @Nullable Object value) throws SQLException {
      this.checkUpdateable();
      if (!this.onInsertRow && (this.isBeforeFirst() || this.isAfterLast() || ((List)Nullness.castNonNull(this.rows, "rows")).isEmpty())) {
         throw new PSQLException(GT.tr("Cannot update the ResultSet because it is either before the start or after the end of the results."), PSQLState.INVALID_CURSOR_STATE);
      } else {
         this.checkColumnIndex(columnIndex);
         this.doingUpdates = !this.onInsertRow;
         if (value == null) {
            this.updateNull(columnIndex);
         } else {
            PGResultSetMetaData md = (PGResultSetMetaData)this.getMetaData();
            ((HashMap)Nullness.castNonNull(this.updateValues, "updateValues")).put(md.getBaseColumnName(columnIndex), value);
         }

      }
   }

   @Pure
   protected Object getUUID(String data) throws SQLException {
      try {
         UUID uuid = UUID.fromString(data);
         return uuid;
      } catch (IllegalArgumentException var4) {
         throw new PSQLException(GT.tr("Invalid UUID data."), PSQLState.INVALID_PARAMETER_VALUE, var4);
      }
   }

   @Pure
   protected Object getUUID(byte[] data) throws SQLException {
      return new UUID(ByteConverter.int8(data, 0), ByteConverter.int8(data, 8));
   }

   void addRows(List<Tuple> tuples) {
      ((List)Nullness.castNonNull(this.rows, "rows")).addAll(tuples);
   }

   public void updateRef(@Positive int columnIndex, @Nullable Ref x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateRef(int,Ref)");
   }

   public void updateRef(String columnName, @Nullable Ref x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateRef(String,Ref)");
   }

   public void updateBlob(@Positive int columnIndex, @Nullable Blob x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateBlob(int,Blob)");
   }

   public void updateBlob(String columnName, @Nullable Blob x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateBlob(String,Blob)");
   }

   public void updateClob(@Positive int columnIndex, @Nullable Clob x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateClob(int,Clob)");
   }

   public void updateClob(String columnName, @Nullable Clob x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateClob(String,Clob)");
   }

   public void updateArray(@Positive int columnIndex, @Nullable Array x) throws SQLException {
      this.updateObject(columnIndex, x);
   }

   public void updateArray(String columnName, @Nullable Array x) throws SQLException {
      this.updateArray(this.findColumn(columnName), x);
   }

   @Nullable
   public <T> T getObject(@Positive int columnIndex, Class<T> type) throws SQLException {
      if (type == null) {
         throw new SQLException("type is null");
      } else {
         int sqlType = this.getSQLType(columnIndex);
         if (type == BigDecimal.class) {
            if (sqlType != 2 && sqlType != 3) {
               throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
            } else {
               return type.cast(this.getBigDecimal(columnIndex));
            }
         } else if (type == String.class) {
            if (sqlType != 1 && sqlType != 12) {
               throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
            } else {
               return type.cast(this.getString(columnIndex));
            }
         } else if (type == Boolean.class) {
            if (sqlType != 16 && sqlType != -7) {
               throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
            } else {
               boolean booleanValue = this.getBoolean(columnIndex);
               return this.wasNull() ? null : type.cast(booleanValue);
            }
         } else if (type == Short.class) {
            if (sqlType == 5) {
               short shortValue = this.getShort(columnIndex);
               return this.wasNull() ? null : type.cast(shortValue);
            } else {
               throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
            }
         } else if (type == Integer.class) {
            if (sqlType != 4 && sqlType != 5) {
               throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
            } else {
               int intValue = this.getInt(columnIndex);
               return this.wasNull() ? null : type.cast(intValue);
            }
         } else {
            long longValue;
            if (type == Long.class) {
               if (sqlType == -5) {
                  longValue = this.getLong(columnIndex);
                  return this.wasNull() ? null : type.cast(longValue);
               } else {
                  throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
               }
            } else if (type == BigInteger.class) {
               if (sqlType == -5) {
                  longValue = this.getLong(columnIndex);
                  return this.wasNull() ? null : type.cast(BigInteger.valueOf(longValue));
               } else {
                  throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
               }
            } else if (type == Float.class) {
               if (sqlType == 7) {
                  float floatValue = this.getFloat(columnIndex);
                  return this.wasNull() ? null : type.cast(floatValue);
               } else {
                  throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
               }
            } else if (type == Double.class) {
               if (sqlType != 6 && sqlType != 8) {
                  throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
               } else {
                  double doubleValue = this.getDouble(columnIndex);
                  return this.wasNull() ? null : type.cast(doubleValue);
               }
            } else if (type == Date.class) {
               if (sqlType == 91) {
                  return type.cast(this.getDate(columnIndex));
               } else {
                  throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
               }
            } else if (type == Time.class) {
               if (sqlType == 92) {
                  return type.cast(this.getTime(columnIndex));
               } else {
                  throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
               }
            } else if (type == Timestamp.class) {
               if (sqlType != 93 && sqlType != 2014) {
                  throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
               } else {
                  return type.cast(this.getTimestamp(columnIndex));
               }
            } else {
               Timestamp timestamp;
               if (type == Calendar.class) {
                  if (sqlType != 93 && sqlType != 2014) {
                     throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
                  } else {
                     timestamp = this.getTimestamp(columnIndex);
                     if (timestamp == null) {
                        return null;
                     } else {
                        Calendar calendar = Calendar.getInstance(this.getDefaultCalendar().getTimeZone());
                        calendar.setTimeInMillis(timestamp.getTime());
                        return type.cast(calendar);
                     }
                  }
               } else if (type == Blob.class) {
                  if (sqlType != 2004 && sqlType != -2 && sqlType != -5) {
                     throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
                  } else {
                     return type.cast(this.getBlob(columnIndex));
                  }
               } else if (type == Clob.class) {
                  if (sqlType != 2005 && sqlType != -5) {
                     throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
                  } else {
                     return type.cast(this.getClob(columnIndex));
                  }
               } else if (type == java.util.Date.class) {
                  if (sqlType == 93) {
                     timestamp = this.getTimestamp(columnIndex);
                     return timestamp == null ? null : type.cast(new java.util.Date(timestamp.getTime()));
                  } else {
                     throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
                  }
               } else if (type == Array.class) {
                  if (sqlType == 2003) {
                     return type.cast(this.getArray(columnIndex));
                  } else {
                     throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
                  }
               } else if (type == SQLXML.class) {
                  if (sqlType == 2009) {
                     return type.cast(this.getSQLXML(columnIndex));
                  } else {
                     throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
                  }
               } else if (type == UUID.class) {
                  return type.cast(this.getObject(columnIndex));
               } else if (type == InetAddress.class) {
                  String inetText = this.getString(columnIndex);
                  if (inetText == null) {
                     return null;
                  } else {
                     int slash = inetText.indexOf("/");

                     try {
                        return type.cast(InetAddress.getByName(slash < 0 ? inetText : inetText.substring(0, slash)));
                     } catch (UnknownHostException var7) {
                        throw new PSQLException(GT.tr("Invalid Inet data."), PSQLState.INVALID_PARAMETER_VALUE, var7);
                     }
                  }
               } else if (type == LocalDate.class) {
                  return type.cast(this.getLocalDate(columnIndex));
               } else if (type == LocalTime.class) {
                  return type.cast(this.getLocalTime(columnIndex));
               } else if (type == LocalDateTime.class) {
                  return type.cast(this.getLocalDateTime(columnIndex));
               } else if (type == OffsetDateTime.class) {
                  return type.cast(this.getOffsetDateTime(columnIndex));
               } else if (type == OffsetTime.class) {
                  return type.cast(this.getOffsetTime(columnIndex));
               } else if (PGobject.class.isAssignableFrom(type)) {
                  Object object;
                  if (this.isBinary(columnIndex)) {
                     byte[] byteValue = ((Tuple)Nullness.castNonNull(this.thisRow, "thisRow")).get(columnIndex - 1);
                     object = this.connection.getObject(this.getPGType(columnIndex), (String)null, byteValue);
                  } else {
                     object = this.connection.getObject(this.getPGType(columnIndex), this.getString(columnIndex), (byte[])null);
                  }

                  return type.cast(object);
               } else {
                  throw new PSQLException(GT.tr("conversion to {0} from {1} not supported", type, this.getPGType(columnIndex)), PSQLState.INVALID_PARAMETER_VALUE);
               }
            }
         }
      }
   }

   @Nullable
   public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
      return this.getObject(this.findColumn(columnLabel), type);
   }

   @Nullable
   public Object getObject(String s, @Nullable Map<String, Class<?>> map) throws SQLException {
      return this.getObjectImpl(s, map);
   }

   @Nullable
   public Object getObject(@Positive int i, @Nullable Map<String, Class<?>> map) throws SQLException {
      return this.getObjectImpl(i, map);
   }

   public void updateObject(@Positive int columnIndex, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateObject");
   }

   public void updateObject(String columnLabel, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateObject");
   }

   public void updateObject(@Positive int columnIndex, @Nullable Object x, SQLType targetSqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateObject");
   }

   public void updateObject(String columnLabel, @Nullable Object x, SQLType targetSqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateObject");
   }

   @Nullable
   public RowId getRowId(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getRowId columnIndex: {0}", columnIndex);
      throw Driver.notImplemented(this.getClass(), "getRowId(int)");
   }

   @Nullable
   public RowId getRowId(String columnName) throws SQLException {
      return this.getRowId(this.findColumn(columnName));
   }

   public void updateRowId(@Positive int columnIndex, @Nullable RowId x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateRowId(int, RowId)");
   }

   public void updateRowId(String columnName, @Nullable RowId x) throws SQLException {
      this.updateRowId(this.findColumn(columnName), x);
   }

   public int getHoldability() throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getHoldability()");
   }

   public boolean isClosed() throws SQLException {
      return this.rows == null;
   }

   public void updateNString(@Positive int columnIndex, @Nullable String nString) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateNString(int, String)");
   }

   public void updateNString(String columnName, @Nullable String nString) throws SQLException {
      this.updateNString(this.findColumn(columnName), nString);
   }

   public void updateNClob(@Positive int columnIndex, @Nullable NClob nClob) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateNClob(int, NClob)");
   }

   public void updateNClob(String columnName, @Nullable NClob nClob) throws SQLException {
      this.updateNClob(this.findColumn(columnName), nClob);
   }

   public void updateNClob(@Positive int columnIndex, @Nullable Reader reader) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateNClob(int, Reader)");
   }

   public void updateNClob(String columnName, @Nullable Reader reader) throws SQLException {
      this.updateNClob(this.findColumn(columnName), reader);
   }

   public void updateNClob(@Positive int columnIndex, @Nullable Reader reader, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateNClob(int, Reader, long)");
   }

   public void updateNClob(String columnName, @Nullable Reader reader, long length) throws SQLException {
      this.updateNClob(this.findColumn(columnName), reader, length);
   }

   @Nullable
   public NClob getNClob(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getNClob columnIndex: {0}", columnIndex);
      throw Driver.notImplemented(this.getClass(), "getNClob(int)");
   }

   @Nullable
   public NClob getNClob(String columnName) throws SQLException {
      return this.getNClob(this.findColumn(columnName));
   }

   public void updateBlob(@Positive int columnIndex, @Nullable InputStream inputStream, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateBlob(int, InputStream, long)");
   }

   public void updateBlob(String columnName, @Nullable InputStream inputStream, long length) throws SQLException {
      this.updateBlob(this.findColumn(columnName), inputStream, length);
   }

   public void updateBlob(@Positive int columnIndex, @Nullable InputStream inputStream) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateBlob(int, InputStream)");
   }

   public void updateBlob(String columnName, @Nullable InputStream inputStream) throws SQLException {
      this.updateBlob(this.findColumn(columnName), inputStream);
   }

   public void updateClob(@Positive int columnIndex, @Nullable Reader reader, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateClob(int, Reader, long)");
   }

   public void updateClob(String columnName, @Nullable Reader reader, long length) throws SQLException {
      this.updateClob(this.findColumn(columnName), reader, length);
   }

   public void updateClob(@Positive int columnIndex, @Nullable Reader reader) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateClob(int, Reader)");
   }

   public void updateClob(String columnName, @Nullable Reader reader) throws SQLException {
      this.updateClob(this.findColumn(columnName), reader);
   }

   @Pure
   @Nullable
   public SQLXML getSQLXML(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getSQLXML columnIndex: {0}", columnIndex);
      String data = this.getString(columnIndex);
      return data == null ? null : new PgSQLXML(this.connection, data);
   }

   @Nullable
   public SQLXML getSQLXML(String columnName) throws SQLException {
      return this.getSQLXML(this.findColumn(columnName));
   }

   public void updateSQLXML(@Positive int columnIndex, @Nullable SQLXML xmlObject) throws SQLException {
      this.updateValue(columnIndex, xmlObject);
   }

   public void updateSQLXML(String columnName, @Nullable SQLXML xmlObject) throws SQLException {
      this.updateSQLXML(this.findColumn(columnName), xmlObject);
   }

   @Nullable
   public String getNString(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getNString columnIndex: {0}", columnIndex);
      throw Driver.notImplemented(this.getClass(), "getNString(int)");
   }

   @Nullable
   public String getNString(String columnName) throws SQLException {
      return this.getNString(this.findColumn(columnName));
   }

   @Nullable
   public Reader getNCharacterStream(@Positive int columnIndex) throws SQLException {
      this.connection.getLogger().log(Level.FINEST, "  getNCharacterStream columnIndex: {0}", columnIndex);
      throw Driver.notImplemented(this.getClass(), "getNCharacterStream(int)");
   }

   @Nullable
   public Reader getNCharacterStream(String columnName) throws SQLException {
      return this.getNCharacterStream(this.findColumn(columnName));
   }

   public void updateNCharacterStream(@Positive int columnIndex, @Nullable Reader x, int length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader, int)");
   }

   public void updateNCharacterStream(String columnName, @Nullable Reader x, int length) throws SQLException {
      this.updateNCharacterStream(this.findColumn(columnName), x, length);
   }

   public void updateNCharacterStream(@Positive int columnIndex, @Nullable Reader x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader)");
   }

   public void updateNCharacterStream(String columnName, @Nullable Reader x) throws SQLException {
      this.updateNCharacterStream(this.findColumn(columnName), x);
   }

   public void updateNCharacterStream(@Positive int columnIndex, @Nullable Reader x, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader, long)");
   }

   public void updateNCharacterStream(String columnName, @Nullable Reader x, long length) throws SQLException {
      this.updateNCharacterStream(this.findColumn(columnName), x, length);
   }

   public void updateCharacterStream(@Positive int columnIndex, @Nullable Reader reader, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateCharacterStream(int, Reader, long)");
   }

   public void updateCharacterStream(String columnName, @Nullable Reader reader, long length) throws SQLException {
      this.updateCharacterStream(this.findColumn(columnName), reader, length);
   }

   public void updateCharacterStream(@Positive int columnIndex, @Nullable Reader reader) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateCharacterStream(int, Reader)");
   }

   public void updateCharacterStream(String columnName, @Nullable Reader reader) throws SQLException {
      this.updateCharacterStream(this.findColumn(columnName), reader);
   }

   public void updateBinaryStream(@Positive int columnIndex, @Nullable InputStream inputStream, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateBinaryStream(int, InputStream, long)");
   }

   public void updateBinaryStream(String columnName, @Nullable InputStream inputStream, long length) throws SQLException {
      this.updateBinaryStream(this.findColumn(columnName), inputStream, length);
   }

   public void updateBinaryStream(@Positive int columnIndex, @Nullable InputStream inputStream) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateBinaryStream(int, InputStream)");
   }

   public void updateBinaryStream(String columnName, @Nullable InputStream inputStream) throws SQLException {
      this.updateBinaryStream(this.findColumn(columnName), inputStream);
   }

   public void updateAsciiStream(@Positive int columnIndex, @Nullable InputStream inputStream, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateAsciiStream(int, InputStream, long)");
   }

   public void updateAsciiStream(String columnName, @Nullable InputStream inputStream, long length) throws SQLException {
      this.updateAsciiStream(this.findColumn(columnName), inputStream, length);
   }

   public void updateAsciiStream(@Positive int columnIndex, @Nullable InputStream inputStream) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "updateAsciiStream(int, InputStream)");
   }

   public void updateAsciiStream(String columnName, @Nullable InputStream inputStream) throws SQLException {
      this.updateAsciiStream(this.findColumn(columnName), inputStream);
   }

   public boolean isWrapperFor(Class<?> iface) throws SQLException {
      return iface.isAssignableFrom(this.getClass());
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (iface.isAssignableFrom(this.getClass())) {
         return iface.cast(this);
      } else {
         throw new SQLException("Cannot unwrap to " + iface.getName());
      }
   }

   private Calendar getDefaultCalendar() {
      if (this.getTimestampUtils().hasFastDefaultTimeZone()) {
         return this.getTimestampUtils().getSharedCalendar((TimeZone)null);
      } else {
         Calendar sharedCalendar = this.getTimestampUtils().getSharedCalendar(this.defaultTimeZone);
         if (this.defaultTimeZone == null) {
            this.defaultTimeZone = sharedCalendar.getTimeZone();
         }

         return sharedCalendar;
      }
   }

   private TimestampUtils getTimestampUtils() {
      if (this.timestampUtils == null) {
         this.timestampUtils = new TimestampUtils(!this.connection.getQueryExecutor().getIntegerDateTimes(), new QueryExecutorTimeZoneProvider(this.connection.getQueryExecutor()));
      }

      return this.timestampUtils;
   }

   protected PgResultSet upperCaseFieldLabels() {
      Field[] var1 = this.fields;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field field = var1[var3];
         field.upperCaseLabel();
      }

      return this;
   }

   public class CursorResultHandler extends ResultHandlerBase {
      public void handleResultRows(Query fromQuery, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor) {
         PgResultSet.this.rows = tuples;
         PgResultSet.this.cursor = cursor;
      }

      public void handleCommandStatus(String status, long updateCount, long insertOID) {
         this.handleError(new PSQLException(GT.tr("Unexpected command status: {0}.", status), PSQLState.PROTOCOL_VIOLATION));
      }

      public void handleCompletion() throws SQLException {
         SQLWarning warning = this.getWarning();
         if (warning != null) {
            PgResultSet.this.addWarning(warning);
         }

         super.handleCompletion();
      }
   }

   private class PrimaryKey {
      int index;
      String name;

      PrimaryKey(int index, String name) {
         this.index = index;
         this.name = name;
      }

      @Nullable
      Object getValue() throws SQLException {
         return PgResultSet.this.getObject(this.index);
      }
   }

   static class NullObject extends PGobject {
      NullObject(String type) {
         this.type = type;
      }

      @Nullable
      public String getValue() {
         return null;
      }
   }
}
