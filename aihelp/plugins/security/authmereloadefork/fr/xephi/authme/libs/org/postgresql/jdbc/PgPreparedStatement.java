package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.CachedQuery;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.core.ResultHandler;
import fr.xephi.authme.libs.org.postgresql.core.ServerVersion;
import fr.xephi.authme.libs.org.postgresql.core.TypeInfo;
import fr.xephi.authme.libs.org.postgresql.core.v3.BatchedQuery;
import fr.xephi.authme.libs.org.postgresql.largeobject.LargeObject;
import fr.xephi.authme.libs.org.postgresql.largeobject.LargeObjectManager;
import fr.xephi.authme.libs.org.postgresql.util.ByteConverter;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.HStoreConverter;
import fr.xephi.authme.libs.org.postgresql.util.PGBinaryObject;
import fr.xephi.authme.libs.org.postgresql.util.PGTime;
import fr.xephi.authme.libs.org.postgresql.util.PGTimestamp;
import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.ReaderInputStream;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.value.qual.IntRange;

class PgPreparedStatement extends PgStatement implements PreparedStatement {
   protected final CachedQuery preparedQuery;
   protected final ParameterList preparedParameters;
   @Nullable
   private TimeZone defaultTimeZone;

   PgPreparedStatement(PgConnection connection, String sql, int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
      this(connection, connection.borrowQuery(sql), rsType, rsConcurrency, rsHoldability);
   }

   PgPreparedStatement(PgConnection connection, CachedQuery query, int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
      super(connection, rsType, rsConcurrency, rsHoldability);
      this.preparedQuery = query;
      this.preparedParameters = this.preparedQuery.query.createParameterList();
      int parameterCount = this.preparedParameters.getParameterCount();
      int maxSupportedParameters = this.maximumNumberOfParameters();
      if (parameterCount > maxSupportedParameters) {
         throw new PSQLException(GT.tr("PreparedStatement can have at most {0} parameters. Please consider using arrays, or splitting the query in several ones, or using COPY. Given query has {1} parameters", maxSupportedParameters, parameterCount), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         this.setPoolable(true);
      }
   }

   final int maximumNumberOfParameters() {
      return this.connection.getPreferQueryMode() == PreferQueryMode.SIMPLE ? Integer.MAX_VALUE : '\uffff';
   }

   public ResultSet executeQuery(String sql) throws SQLException {
      throw new PSQLException(GT.tr("Can''t use query methods that take a query string on a PreparedStatement."), PSQLState.WRONG_OBJECT_TYPE);
   }

   public ResultSet executeQuery() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      ResultSet var2;
      try {
         if (!this.executeWithFlags(0)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
         }

         var2 = this.getSingleResultSet();
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

      return var2;
   }

   public int executeUpdate(String sql) throws SQLException {
      throw new PSQLException(GT.tr("Can''t use query methods that take a query string on a PreparedStatement."), PSQLState.WRONG_OBJECT_TYPE);
   }

   public int executeUpdate() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      int var2;
      try {
         this.executeWithFlags(4);
         this.checkNoResultUpdate();
         var2 = this.getUpdateCount();
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

      return var2;
   }

   public long executeLargeUpdate() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      long var2;
      try {
         this.executeWithFlags(4);
         this.checkNoResultUpdate();
         var2 = this.getLargeUpdateCount();
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

      return var2;
   }

   public boolean execute(String sql) throws SQLException {
      throw new PSQLException(GT.tr("Can''t use query methods that take a query string on a PreparedStatement."), PSQLState.WRONG_OBJECT_TYPE);
   }

   public boolean execute() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      boolean var2;
      try {
         var2 = this.executeWithFlags(0);
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

      return var2;
   }

   public boolean executeWithFlags(int flags) throws SQLException {
      boolean var3;
      try {
         ResourceLock ignore = this.lock.obtain();

         try {
            this.checkClosed();
            if (this.connection.getPreferQueryMode() == PreferQueryMode.SIMPLE) {
               flags |= 1024;
            }

            this.execute(this.preparedQuery, this.preparedParameters, flags);
            this.checkClosed();
            var3 = this.result != null && this.result.getResultSet() != null;
         } catch (Throwable var10) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (ignore != null) {
            ignore.close();
         }
      } finally {
         this.defaultTimeZone = null;
      }

      return var3;
   }

   protected boolean isOneShotQuery(@Nullable CachedQuery cachedQuery) {
      if (cachedQuery == null) {
         cachedQuery = this.preparedQuery;
      }

      return super.isOneShotQuery(cachedQuery);
   }

   public void closeImpl() throws SQLException {
      if (this.preparedQuery != null) {
         this.connection.releaseQuery(this.preparedQuery);
      }

   }

   public void setNull(int parameterIndex, int sqlType) throws SQLException {
      this.checkClosed();
      if (parameterIndex >= 1 && parameterIndex <= this.preparedParameters.getParameterCount()) {
         int oid;
         switch(sqlType) {
         case -7:
         case 16:
            oid = 16;
            break;
         case -6:
         case 5:
            oid = 21;
            break;
         case -5:
            oid = 20;
            break;
         case -4:
         case -3:
         case -2:
            oid = 17;
            break;
         case -1:
         case 12:
            oid = this.connection.getStringVarcharFlag() ? 1043 : 0;
            break;
         case 0:
         case 1111:
         case 2001:
         case 2002:
         case 2003:
            oid = 0;
            break;
         case 1:
            oid = 1042;
            break;
         case 2:
         case 3:
            oid = 1700;
            break;
         case 4:
            oid = 23;
            break;
         case 6:
         case 8:
            oid = 701;
            break;
         case 7:
            oid = 700;
            break;
         case 91:
            oid = 1082;
            break;
         case 92:
         case 93:
         case 2013:
         case 2014:
            oid = 0;
            break;
         case 2004:
         case 2005:
            oid = 26;
            break;
         case 2009:
            oid = 142;
            break;
         case 2012:
            oid = 1790;
            break;
         default:
            throw new PSQLException(GT.tr("Unknown Types value."), PSQLState.INVALID_PARAMETER_TYPE);
         }

         this.preparedParameters.setNull(parameterIndex, oid);
      } else {
         throw new PSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", parameterIndex, this.preparedParameters.getParameterCount()), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   public void setBoolean(@Positive int parameterIndex, boolean x) throws SQLException {
      this.checkClosed();
      this.bindLiteral(parameterIndex, x ? "TRUE" : "FALSE", 16);
   }

   public void setByte(@Positive int parameterIndex, byte x) throws SQLException {
      this.setShort(parameterIndex, (short)x);
   }

   public void setShort(@Positive int parameterIndex, short x) throws SQLException {
      this.checkClosed();
      if (this.connection.binaryTransferSend(21)) {
         byte[] val = new byte[2];
         ByteConverter.int2(val, 0, x);
         this.bindBytes(parameterIndex, val, 21);
      } else {
         this.bindLiteral(parameterIndex, Integer.toString(x), 21);
      }
   }

   public void setInt(@Positive int parameterIndex, int x) throws SQLException {
      this.checkClosed();
      if (this.connection.binaryTransferSend(23)) {
         byte[] val = new byte[4];
         ByteConverter.int4(val, 0, x);
         this.bindBytes(parameterIndex, val, 23);
      } else {
         this.bindLiteral(parameterIndex, Integer.toString(x), 23);
      }
   }

   public void setLong(@Positive int parameterIndex, long x) throws SQLException {
      this.checkClosed();
      if (this.connection.binaryTransferSend(20)) {
         byte[] val = new byte[8];
         ByteConverter.int8(val, 0, x);
         this.bindBytes(parameterIndex, val, 20);
      } else {
         this.bindLiteral(parameterIndex, Long.toString(x), 20);
      }
   }

   public void setFloat(@Positive int parameterIndex, float x) throws SQLException {
      this.checkClosed();
      if (this.connection.binaryTransferSend(700)) {
         byte[] val = new byte[4];
         ByteConverter.float4(val, 0, x);
         this.bindBytes(parameterIndex, val, 700);
      } else {
         this.bindLiteral(parameterIndex, Float.toString(x), 701);
      }
   }

   public void setDouble(@Positive int parameterIndex, double x) throws SQLException {
      this.checkClosed();
      if (this.connection.binaryTransferSend(701)) {
         byte[] val = new byte[8];
         ByteConverter.float8(val, 0, x);
         this.bindBytes(parameterIndex, val, 701);
      } else {
         this.bindLiteral(parameterIndex, Double.toString(x), 701);
      }
   }

   public void setBigDecimal(@Positive int parameterIndex, @Nullable BigDecimal x) throws SQLException {
      if (x != null && this.connection.binaryTransferSend(1700)) {
         byte[] bytes = ByteConverter.numeric(x);
         this.bindBytes(parameterIndex, bytes, 1700);
      } else {
         this.setNumber(parameterIndex, x);
      }
   }

   public void setString(@Positive int parameterIndex, @Nullable String x) throws SQLException {
      this.checkClosed();
      this.setString(parameterIndex, x, this.getStringType());
   }

   private int getStringType() {
      return this.connection.getStringVarcharFlag() ? 1043 : 0;
   }

   protected void setString(@Positive int parameterIndex, @Nullable String x, int oid) throws SQLException {
      this.checkClosed();
      if (x == null) {
         this.preparedParameters.setNull(parameterIndex, oid);
      } else {
         this.bindString(parameterIndex, x, oid);
      }

   }

   public void setBytes(@Positive int parameterIndex, @Nullable byte[] x) throws SQLException {
      this.checkClosed();
      if (null == x) {
         this.setNull(parameterIndex, -3);
      } else {
         byte[] copy = new byte[x.length];
         System.arraycopy(x, 0, copy, 0, x.length);
         this.preparedParameters.setBytea(parameterIndex, copy, 0, x.length);
      }
   }

   private void setByteStreamWriter(@Positive int parameterIndex, ByteStreamWriter x) throws SQLException {
      this.preparedParameters.setBytea(parameterIndex, x);
   }

   public void setDate(@Positive int parameterIndex, @Nullable Date x) throws SQLException {
      this.setDate(parameterIndex, x, (Calendar)null);
   }

   public void setTime(@Positive int parameterIndex, @Nullable Time x) throws SQLException {
      this.setTime(parameterIndex, x, (Calendar)null);
   }

   public void setTimestamp(@Positive int parameterIndex, @Nullable Timestamp x) throws SQLException {
      this.setTimestamp(parameterIndex, x, (Calendar)null);
   }

   private void setCharacterStreamPost71(@Positive int parameterIndex, @Nullable InputStream x, int length, String encoding) throws SQLException {
      if (x == null) {
         this.setNull(parameterIndex, 12);
      } else if (length < 0) {
         throw new PSQLException(GT.tr("Invalid stream length {0}.", length), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         try {
            InputStreamReader inStream = new InputStreamReader(x, encoding);
            char[] chars = new char[length];
            int charsRead = 0;

            do {
               int n = inStream.read(chars, charsRead, length - charsRead);
               if (n == -1) {
                  break;
               }

               charsRead += n;
            } while(charsRead != length);

            this.setString(parameterIndex, new String(chars, 0, charsRead), 1043);
         } catch (UnsupportedEncodingException var9) {
            throw new PSQLException(GT.tr("The JVM claims not to support the {0} encoding.", encoding), PSQLState.UNEXPECTED_ERROR, var9);
         } catch (IOException var10) {
            throw new PSQLException(GT.tr("Provided InputStream failed."), PSQLState.UNEXPECTED_ERROR, var10);
         }
      }
   }

   public void setAsciiStream(@Positive int parameterIndex, @Nullable InputStream x, @NonNegative int length) throws SQLException {
      this.checkClosed();
      this.setCharacterStreamPost71(parameterIndex, x, length, "ASCII");
   }

   public void setUnicodeStream(@Positive int parameterIndex, @Nullable InputStream x, @NonNegative int length) throws SQLException {
      this.checkClosed();
      this.setCharacterStreamPost71(parameterIndex, x, length, "UTF-8");
   }

   public void setBinaryStream(@Positive int parameterIndex, @Nullable InputStream x, @NonNegative int length) throws SQLException {
      this.checkClosed();
      if (x == null) {
         this.setNull(parameterIndex, -3);
      } else if (length < 0) {
         throw new PSQLException(GT.tr("Invalid stream length {0}.", length), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         this.preparedParameters.setBytea(parameterIndex, x, length);
      }
   }

   public void clearParameters() throws SQLException {
      this.preparedParameters.clear();
   }

   private void setPGobject(@Positive int parameterIndex, PGobject x) throws SQLException {
      String typename = x.getType();
      int oid = this.connection.getTypeInfo().getPGType(typename);
      if (oid == 0) {
         throw new PSQLException(GT.tr("Unknown type {0}.", typename), PSQLState.INVALID_PARAMETER_TYPE);
      } else {
         if (x instanceof PGBinaryObject && this.connection.binaryTransferSend(oid)) {
            PGBinaryObject binObj = (PGBinaryObject)x;
            int length = binObj.lengthInBytes();
            if (length == 0) {
               this.preparedParameters.setNull(parameterIndex, oid);
               return;
            }

            byte[] data = new byte[length];
            binObj.toBytes(data, 0);
            this.bindBytes(parameterIndex, data, oid);
         } else {
            this.setString(parameterIndex, x.getValue(), oid);
         }

      }
   }

   private void setMap(@Positive int parameterIndex, Map<?, ?> x) throws SQLException {
      int oid = this.connection.getTypeInfo().getPGType("hstore");
      if (oid == 0) {
         throw new PSQLException(GT.tr("No hstore extension installed."), PSQLState.INVALID_PARAMETER_TYPE);
      } else {
         if (this.connection.binaryTransferSend(oid)) {
            byte[] data = HStoreConverter.toBytes(x, this.connection.getEncoding());
            this.bindBytes(parameterIndex, data, oid);
         } else {
            this.setString(parameterIndex, HStoreConverter.toString(x), oid);
         }

      }
   }

   private void setNumber(@Positive int parameterIndex, @Nullable Number x) throws SQLException {
      this.checkClosed();
      if (x == null) {
         this.setNull(parameterIndex, 3);
      } else {
         this.bindLiteral(parameterIndex, x.toString(), 1700);
      }

   }

   public void setObject(@Positive int parameterIndex, @Nullable Object in, int targetSqlType, int scale) throws SQLException {
      this.checkClosed();
      if (in == null) {
         this.setNull(parameterIndex, targetSqlType);
      } else if (targetSqlType == 1111 && in instanceof UUID && this.connection.haveMinimumServerVersion(ServerVersion.v8_3)) {
         this.setUuid(parameterIndex, (UUID)in);
      } else {
         switch(targetSqlType) {
         case -7:
         case 16:
            this.setBoolean(parameterIndex, BooleanTypeUtil.castToBoolean(in));
            break;
         case -6:
         case 5:
            this.setShort(parameterIndex, castToShort(in));
            break;
         case -5:
            this.setLong(parameterIndex, castToLong(in));
            break;
         case -4:
         case -3:
         case -2:
            this.setObject(parameterIndex, in);
            break;
         case -1:
            if (in instanceof InputStream) {
               this.preparedParameters.setText(parameterIndex, (InputStream)in);
            } else {
               this.setString(parameterIndex, castToString(in), this.getStringType());
            }
            break;
         case 1:
            this.setString(parameterIndex, castToString(in), 1042);
            break;
         case 2:
         case 3:
            this.setBigDecimal(parameterIndex, castToBigDecimal(in, scale));
            break;
         case 4:
            this.setInt(parameterIndex, castToInt(in));
            break;
         case 6:
         case 8:
            this.setDouble(parameterIndex, castToDouble(in));
            break;
         case 7:
            this.setFloat(parameterIndex, castToFloat(in));
            break;
         case 12:
            this.setString(parameterIndex, castToString(in), this.getStringType());
            break;
         case 91:
            if (in instanceof Date) {
               this.setDate(parameterIndex, (Date)in);
            } else {
               Date tmpd;
               if (in instanceof java.util.Date) {
                  tmpd = new Date(((java.util.Date)in).getTime());
               } else {
                  if (in instanceof LocalDate) {
                     this.setDate(parameterIndex, (LocalDate)in);
                     break;
                  }

                  tmpd = this.getTimestampUtils().toDate(this.getDefaultCalendar(), in.toString());
               }

               this.setDate(parameterIndex, tmpd);
            }
            break;
         case 92:
            if (in instanceof Time) {
               this.setTime(parameterIndex, (Time)in);
            } else {
               Time tmpt;
               if (in instanceof java.util.Date) {
                  tmpt = new Time(((java.util.Date)in).getTime());
               } else {
                  if (in instanceof LocalTime) {
                     this.setTime(parameterIndex, (LocalTime)in);
                     break;
                  }

                  if (in instanceof OffsetTime) {
                     this.setTime(parameterIndex, (OffsetTime)in);
                     break;
                  }

                  tmpt = this.getTimestampUtils().toTime(this.getDefaultCalendar(), in.toString());
               }

               this.setTime(parameterIndex, tmpt);
            }
            break;
         case 93:
            if (in instanceof PGTimestamp) {
               this.setObject(parameterIndex, in);
            } else if (in instanceof Timestamp) {
               this.setTimestamp(parameterIndex, (Timestamp)in);
            } else {
               Timestamp tmpts;
               if (in instanceof java.util.Date) {
                  tmpts = new Timestamp(((java.util.Date)in).getTime());
               } else {
                  if (in instanceof LocalDateTime) {
                     this.setTimestamp(parameterIndex, (LocalDateTime)in);
                     break;
                  }

                  tmpts = this.getTimestampUtils().toTimestamp(this.getDefaultCalendar(), in.toString());
               }

               this.setTimestamp(parameterIndex, tmpts);
            }
            break;
         case 1111:
            if (in instanceof PGobject) {
               this.setPGobject(parameterIndex, (PGobject)in);
            } else if (in instanceof Map) {
               this.setMap(parameterIndex, (Map)in);
            } else {
               this.bindString(parameterIndex, in.toString(), 0);
            }
            break;
         case 2001:
            this.bindString(parameterIndex, in.toString(), 0);
            break;
         case 2003:
            if (in instanceof Array) {
               this.setArray(parameterIndex, (Array)in);
            } else {
               try {
                  this.setObjectArray(parameterIndex, in);
               } catch (Exception var7) {
                  throw new PSQLException(GT.tr("Cannot cast an instance of {0} to type {1}", in.getClass().getName(), "Types.ARRAY"), PSQLState.INVALID_PARAMETER_TYPE, var7);
               }
            }
            break;
         case 2004:
            if (in instanceof Blob) {
               this.setBlob(parameterIndex, (Blob)in);
            } else {
               if (!(in instanceof InputStream)) {
                  throw new PSQLException(GT.tr("Cannot cast an instance of {0} to type {1}", in.getClass().getName(), "Types.BLOB"), PSQLState.INVALID_PARAMETER_TYPE);
               }

               long oid = this.createBlob(parameterIndex, (InputStream)in, Long.MAX_VALUE);
               this.setLong(parameterIndex, oid);
            }
            break;
         case 2005:
            if (!(in instanceof Clob)) {
               throw new PSQLException(GT.tr("Cannot cast an instance of {0} to type {1}", in.getClass().getName(), "Types.CLOB"), PSQLState.INVALID_PARAMETER_TYPE);
            }

            this.setClob(parameterIndex, (Clob)in);
            break;
         case 2009:
            if (in instanceof SQLXML) {
               this.setSQLXML(parameterIndex, (SQLXML)in);
            } else {
               this.setSQLXML(parameterIndex, new PgSQLXML(this.connection, in.toString()));
            }
            break;
         case 2014:
            if (in instanceof OffsetDateTime) {
               this.setTimestamp(parameterIndex, (OffsetDateTime)in);
            } else {
               if (!(in instanceof PGTimestamp)) {
                  throw new PSQLException(GT.tr("Cannot cast an instance of {0} to type {1}", in.getClass().getName(), "Types.TIMESTAMP_WITH_TIMEZONE"), PSQLState.INVALID_PARAMETER_TYPE);
               }

               this.setObject(parameterIndex, in);
            }
            break;
         default:
            throw new PSQLException(GT.tr("Unsupported Types value: {0}", targetSqlType), PSQLState.INVALID_PARAMETER_TYPE);
         }

      }
   }

   private Class<?> getArrayType(Class<?> type) {
      for(Class subType = type.getComponentType(); subType != null; subType = subType.getComponentType()) {
         type = subType;
      }

      return type;
   }

   private <A> void setObjectArray(int parameterIndex, A in) throws SQLException {
      ArrayEncoding.ArrayEncoder<A> arraySupport = ArrayEncoding.getArrayEncoder(in);
      TypeInfo typeInfo = this.connection.getTypeInfo();
      int oid = arraySupport.getDefaultArrayTypeOid();
      if (arraySupport.supportBinaryRepresentation(oid) && this.connection.getPreferQueryMode() != PreferQueryMode.SIMPLE) {
         this.bindBytes(parameterIndex, arraySupport.toBinaryRepresentation(this.connection, in, oid), oid);
      } else {
         if (oid == 0) {
            Class<?> arrayType = this.getArrayType(in.getClass());
            oid = typeInfo.getJavaArrayType(arrayType.getName());
            if (oid == 0) {
               throw new SQLFeatureNotSupportedException();
            }
         }

         int baseOid = typeInfo.getPGArrayElement(oid);
         String baseType = (String)Nullness.castNonNull(typeInfo.getPGType(baseOid));
         Array array = this.getPGConnection().createArrayOf(baseType, in);
         this.setArray(parameterIndex, array);
      }

   }

   private static String asString(Clob in) throws SQLException {
      return in.getSubString(1L, (int)in.length());
   }

   private static int castToInt(Object in) throws SQLException {
      try {
         if (in instanceof String) {
            return Integer.parseInt((String)in);
         }

         if (in instanceof Number) {
            return ((Number)in).intValue();
         }

         if (in instanceof java.util.Date) {
            return (int)((java.util.Date)in).getTime();
         }

         if (in instanceof Boolean) {
            return (Boolean)in ? 1 : 0;
         }

         if (in instanceof Clob) {
            return Integer.parseInt(asString((Clob)in));
         }

         if (in instanceof Character) {
            return Integer.parseInt(in.toString());
         }
      } catch (Exception var2) {
         throw cannotCastException(in.getClass().getName(), "int", var2);
      }

      throw cannotCastException(in.getClass().getName(), "int");
   }

   private static short castToShort(Object in) throws SQLException {
      try {
         if (in instanceof String) {
            return Short.parseShort((String)in);
         }

         if (in instanceof Number) {
            return ((Number)in).shortValue();
         }

         if (in instanceof java.util.Date) {
            return (short)((int)((java.util.Date)in).getTime());
         }

         if (in instanceof Boolean) {
            return (short)((Boolean)in ? 1 : 0);
         }

         if (in instanceof Clob) {
            return Short.parseShort(asString((Clob)in));
         }

         if (in instanceof Character) {
            return Short.parseShort(in.toString());
         }
      } catch (Exception var2) {
         throw cannotCastException(in.getClass().getName(), "short", var2);
      }

      throw cannotCastException(in.getClass().getName(), "short");
   }

   private static long castToLong(Object in) throws SQLException {
      try {
         if (in instanceof String) {
            return Long.parseLong((String)in);
         }

         if (in instanceof Number) {
            return ((Number)in).longValue();
         }

         if (in instanceof java.util.Date) {
            return ((java.util.Date)in).getTime();
         }

         if (in instanceof Boolean) {
            return (Boolean)in ? 1L : 0L;
         }

         if (in instanceof Clob) {
            return Long.parseLong(asString((Clob)in));
         }

         if (in instanceof Character) {
            return Long.parseLong(in.toString());
         }
      } catch (Exception var2) {
         throw cannotCastException(in.getClass().getName(), "long", var2);
      }

      throw cannotCastException(in.getClass().getName(), "long");
   }

   private static float castToFloat(Object in) throws SQLException {
      try {
         if (in instanceof String) {
            return Float.parseFloat((String)in);
         }

         if (in instanceof Number) {
            return ((Number)in).floatValue();
         }

         if (in instanceof java.util.Date) {
            return (float)((java.util.Date)in).getTime();
         }

         if (in instanceof Boolean) {
            return (Boolean)in ? 1.0F : 0.0F;
         }

         if (in instanceof Clob) {
            return Float.parseFloat(asString((Clob)in));
         }

         if (in instanceof Character) {
            return Float.parseFloat(in.toString());
         }
      } catch (Exception var2) {
         throw cannotCastException(in.getClass().getName(), "float", var2);
      }

      throw cannotCastException(in.getClass().getName(), "float");
   }

   private static double castToDouble(Object in) throws SQLException {
      try {
         if (in instanceof String) {
            return Double.parseDouble((String)in);
         }

         if (in instanceof Number) {
            return ((Number)in).doubleValue();
         }

         if (in instanceof java.util.Date) {
            return (double)((java.util.Date)in).getTime();
         }

         if (in instanceof Boolean) {
            return (Boolean)in ? 1.0D : 0.0D;
         }

         if (in instanceof Clob) {
            return Double.parseDouble(asString((Clob)in));
         }

         if (in instanceof Character) {
            return Double.parseDouble(in.toString());
         }
      } catch (Exception var2) {
         throw cannotCastException(in.getClass().getName(), "double", var2);
      }

      throw cannotCastException(in.getClass().getName(), "double");
   }

   private static BigDecimal castToBigDecimal(Object in, int scale) throws SQLException {
      try {
         BigDecimal rc = null;
         if (in instanceof String) {
            rc = new BigDecimal((String)in);
         } else if (in instanceof BigDecimal) {
            rc = (BigDecimal)in;
         } else if (in instanceof BigInteger) {
            rc = new BigDecimal((BigInteger)in);
         } else if (!(in instanceof Long) && !(in instanceof Integer) && !(in instanceof Short) && !(in instanceof Byte)) {
            if (!(in instanceof Double) && !(in instanceof Float)) {
               if (in instanceof java.util.Date) {
                  rc = BigDecimal.valueOf(((java.util.Date)in).getTime());
               } else if (in instanceof Boolean) {
                  rc = (Boolean)in ? BigDecimal.ONE : BigDecimal.ZERO;
               } else if (in instanceof Clob) {
                  rc = new BigDecimal(asString((Clob)in));
               } else if (in instanceof Character) {
                  rc = new BigDecimal(new char[]{(Character)in});
               }
            } else {
               rc = BigDecimal.valueOf(((Number)in).doubleValue());
            }
         } else {
            rc = BigDecimal.valueOf(((Number)in).longValue());
         }

         if (rc != null) {
            if (scale >= 0) {
               rc = rc.setScale(scale, RoundingMode.HALF_UP);
            }

            return rc;
         }
      } catch (Exception var3) {
         throw cannotCastException(in.getClass().getName(), "BigDecimal", var3);
      }

      throw cannotCastException(in.getClass().getName(), "BigDecimal");
   }

   private static String castToString(Object in) throws SQLException {
      try {
         if (in instanceof String) {
            return (String)in;
         } else {
            return in instanceof Clob ? asString((Clob)in) : in.toString();
         }
      } catch (Exception var2) {
         throw cannotCastException(in.getClass().getName(), "String", var2);
      }
   }

   private static PSQLException cannotCastException(String fromType, String toType) {
      return cannotCastException(fromType, toType, (Exception)null);
   }

   private static PSQLException cannotCastException(String fromType, String toType, @Nullable Exception cause) {
      return new PSQLException(GT.tr("Cannot convert an instance of {0} to type {1}", fromType, toType), PSQLState.INVALID_PARAMETER_TYPE, cause);
   }

   public void setObject(@Positive int parameterIndex, @Nullable Object x, int targetSqlType) throws SQLException {
      this.setObject(parameterIndex, x, targetSqlType, -1);
   }

   public void setObject(@Positive int parameterIndex, @Nullable Object x) throws SQLException {
      this.checkClosed();
      if (x == null) {
         this.setNull(parameterIndex, 1111);
      } else if (x instanceof UUID && this.connection.haveMinimumServerVersion(ServerVersion.v8_3)) {
         this.setUuid(parameterIndex, (UUID)x);
      } else if (x instanceof SQLXML) {
         this.setSQLXML(parameterIndex, (SQLXML)x);
      } else if (x instanceof String) {
         this.setString(parameterIndex, (String)x);
      } else if (x instanceof BigDecimal) {
         this.setBigDecimal(parameterIndex, (BigDecimal)x);
      } else if (x instanceof Short) {
         this.setShort(parameterIndex, (Short)x);
      } else if (x instanceof Integer) {
         this.setInt(parameterIndex, (Integer)x);
      } else if (x instanceof Long) {
         this.setLong(parameterIndex, (Long)x);
      } else if (x instanceof Float) {
         this.setFloat(parameterIndex, (Float)x);
      } else if (x instanceof Double) {
         this.setDouble(parameterIndex, (Double)x);
      } else if (x instanceof byte[]) {
         this.setBytes(parameterIndex, (byte[])x);
      } else if (x instanceof ByteStreamWriter) {
         this.setByteStreamWriter(parameterIndex, (ByteStreamWriter)x);
      } else if (x instanceof Date) {
         this.setDate(parameterIndex, (Date)x);
      } else if (x instanceof Time) {
         this.setTime(parameterIndex, (Time)x);
      } else if (x instanceof Timestamp) {
         this.setTimestamp(parameterIndex, (Timestamp)x);
      } else if (x instanceof Boolean) {
         this.setBoolean(parameterIndex, (Boolean)x);
      } else if (x instanceof Byte) {
         this.setByte(parameterIndex, (Byte)x);
      } else if (x instanceof Blob) {
         this.setBlob(parameterIndex, (Blob)x);
      } else if (x instanceof Clob) {
         this.setClob(parameterIndex, (Clob)x);
      } else if (x instanceof Array) {
         this.setArray(parameterIndex, (Array)x);
      } else if (x instanceof PGobject) {
         this.setPGobject(parameterIndex, (PGobject)x);
      } else if (x instanceof Character) {
         this.setString(parameterIndex, ((Character)x).toString());
      } else if (x instanceof LocalDate) {
         this.setDate(parameterIndex, (LocalDate)x);
      } else if (x instanceof LocalTime) {
         this.setTime(parameterIndex, (LocalTime)x);
      } else if (x instanceof OffsetTime) {
         this.setTime(parameterIndex, (OffsetTime)x);
      } else if (x instanceof LocalDateTime) {
         this.setTimestamp(parameterIndex, (LocalDateTime)x);
      } else if (x instanceof OffsetDateTime) {
         this.setTimestamp(parameterIndex, (OffsetDateTime)x);
      } else if (x instanceof Map) {
         this.setMap(parameterIndex, (Map)x);
      } else if (x instanceof Number) {
         this.setNumber(parameterIndex, (Number)x);
      } else {
         if (!x.getClass().isArray()) {
            throw new PSQLException(GT.tr("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", x.getClass().getName()), PSQLState.INVALID_PARAMETER_TYPE);
         }

         try {
            this.setObjectArray(parameterIndex, x);
         } catch (Exception var4) {
            throw new PSQLException(GT.tr("Cannot cast an instance of {0} to type {1}", x.getClass().getName(), "Types.ARRAY"), PSQLState.INVALID_PARAMETER_TYPE, var4);
         }
      }

   }

   public String toString() {
      return this.preparedQuery == null ? super.toString() : this.preparedQuery.query.toString(this.preparedParameters);
   }

   protected void bindLiteral(@Positive int paramIndex, String s, int oid) throws SQLException {
      this.preparedParameters.setLiteralParameter(paramIndex, s, oid);
   }

   protected void bindBytes(@Positive int paramIndex, byte[] b, int oid) throws SQLException {
      this.preparedParameters.setBinaryParameter(paramIndex, b, oid);
   }

   private void bindString(@Positive int paramIndex, String s, int oid) throws SQLException {
      this.preparedParameters.setStringParameter(paramIndex, s, oid);
   }

   public boolean isUseServerPrepare() {
      return this.preparedQuery != null && this.mPrepareThreshold != 0 && this.preparedQuery.getExecuteCount() + 1 >= this.mPrepareThreshold;
   }

   public void addBatch(String sql) throws SQLException {
      this.checkClosed();
      throw new PSQLException(GT.tr("Can''t use query methods that take a query string on a PreparedStatement."), PSQLState.WRONG_OBJECT_TYPE);
   }

   public void addBatch() throws SQLException {
      this.checkClosed();
      ArrayList<Query> batchStatements = this.batchStatements;
      if (batchStatements == null) {
         this.batchStatements = batchStatements = new ArrayList();
      }

      ArrayList<ParameterList> batchParameters = this.batchParameters;
      if (batchParameters == null) {
         this.batchParameters = batchParameters = new ArrayList();
      }

      batchParameters.add(this.preparedParameters.copy());
      Query query = this.preparedQuery.query;
      if (!(query instanceof BatchedQuery) || batchStatements.isEmpty()) {
         batchStatements.add(query);
      }

   }

   @Nullable
   public ResultSetMetaData getMetaData() throws SQLException {
      this.checkClosed();
      ResultSet rs = this.getResultSet();
      if (rs == null || ((PgResultSet)rs).isResultSetClosed()) {
         int flags = 49;
         PgStatement.StatementResultHandler handler = new PgStatement.StatementResultHandler();
         this.connection.getQueryExecutor().execute((Query)this.preparedQuery.query, (ParameterList)this.preparedParameters, (ResultHandler)handler, 0, 0, flags);
         ResultWrapper wrapper = handler.getResults();
         if (wrapper != null) {
            rs = wrapper.getResultSet();
         }
      }

      return rs != null ? rs.getMetaData() : null;
   }

   public void setArray(int i, @Nullable Array x) throws SQLException {
      this.checkClosed();
      if (null == x) {
         this.setNull(i, 2003);
      } else {
         String typename = x.getBaseTypeName();
         int oid = this.connection.getTypeInfo().getPGArrayType(typename);
         if (oid == 0) {
            throw new PSQLException(GT.tr("Unknown type {0}.", typename), PSQLState.INVALID_PARAMETER_TYPE);
         } else {
            if (x instanceof PgArray) {
               PgArray arr = (PgArray)x;
               byte[] bytes = arr.toBytes();
               if (bytes != null) {
                  this.bindBytes(i, bytes, oid);
                  return;
               }
            }

            this.setString(i, x.toString(), oid);
         }
      }
   }

   protected long createBlob(int i, InputStream inputStream, @NonNegative long length) throws SQLException {
      LargeObjectManager lom = this.connection.getLargeObjectAPI();
      long oid = lom.createLO();
      LargeObject lob = lom.open(oid);

      try {
         OutputStream outputStream = lob.getOutputStream();

         try {
            byte[] buf = new byte[(int)Math.min(length, 8192L)];

            int numRead;
            while(length > 0L && (numRead = inputStream.read(buf, 0, (int)Math.min((long)buf.length, length))) >= 0) {
               length -= (long)numRead;
               outputStream.write(buf, 0, numRead);
            }
         } catch (Throwable var13) {
            if (outputStream != null) {
               try {
                  outputStream.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }
            }

            throw var13;
         }

         if (outputStream != null) {
            outputStream.close();
         }

         return oid;
      } catch (IOException var14) {
         throw new PSQLException(GT.tr("Unexpected error writing large object to database."), PSQLState.UNEXPECTED_ERROR, var14);
      }
   }

   public void setBlob(@Positive int i, @Nullable Blob x) throws SQLException {
      this.checkClosed();
      if (x == null) {
         this.setNull(i, 2004);
      } else {
         InputStream inStream = x.getBinaryStream();

         try {
            long oid = this.createBlob(i, inStream, x.length());
            this.setLong(i, oid);
         } finally {
            try {
               inStream.close();
            } catch (Exception var11) {
            }

         }

      }
   }

   private String readerToString(Reader value, int maxLength) throws SQLException {
      try {
         int bufferSize = Math.min(maxLength, 1024);
         StringBuilder v = new StringBuilder(bufferSize);
         char[] buf = new char[bufferSize];
         int nRead = 0;

         while(nRead > -1 && v.length() < maxLength) {
            nRead = value.read(buf, 0, Math.min(bufferSize, maxLength - v.length()));
            if (nRead > 0) {
               v.append(buf, 0, nRead);
            }
         }

         return v.toString();
      } catch (IOException var7) {
         throw new PSQLException(GT.tr("Provided Reader failed."), PSQLState.UNEXPECTED_ERROR, var7);
      }
   }

   public void setCharacterStream(@Positive int i, @Nullable Reader x, @NonNegative int length) throws SQLException {
      this.checkClosed();
      if (x == null) {
         this.setNull(i, 12);
      } else if (length < 0) {
         throw new PSQLException(GT.tr("Invalid stream length {0}.", length), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         this.setString(i, this.readerToString(x, length));
      }
   }

   public void setClob(@Positive int i, @Nullable Clob x) throws SQLException {
      this.checkClosed();
      if (x == null) {
         this.setNull(i, 2005);
      } else {
         Reader inStream = x.getCharacterStream();
         int length = (int)x.length();
         LargeObjectManager lom = this.connection.getLargeObjectAPI();
         long oid = lom.createLO();
         LargeObject lob = lom.open(oid);
         Charset connectionCharset = Charset.forName(this.connection.getEncoding().name());
         OutputStream los = lob.getOutputStream();
         OutputStreamWriter lw = new OutputStreamWriter(los, connectionCharset);

         try {
            int c = inStream.read();
            int p = 0;

            while(true) {
               if (c <= -1 || p >= length) {
                  lw.close();
                  break;
               }

               lw.write(c);
               c = inStream.read();
               ++p;
            }
         } catch (IOException var14) {
            throw new PSQLException(GT.tr("Unexpected error writing large object to database."), PSQLState.UNEXPECTED_ERROR, var14);
         }

         this.setLong(i, oid);
      }
   }

   public void setNull(@Positive int parameterIndex, int t, @Nullable String typeName) throws SQLException {
      if (typeName == null) {
         this.setNull(parameterIndex, t);
      } else {
         this.checkClosed();
         TypeInfo typeInfo = this.connection.getTypeInfo();
         int oid = typeInfo.getPGType(typeName);
         this.preparedParameters.setNull(parameterIndex, oid);
      }
   }

   public void setRef(@Positive int i, @Nullable Ref x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setRef(int,Ref)");
   }

   public void setDate(@Positive int i, @Nullable Date d, @Nullable Calendar cal) throws SQLException {
      this.checkClosed();
      if (d == null) {
         this.setNull(i, 91);
      } else if (this.connection.binaryTransferSend(1082)) {
         byte[] val = new byte[4];
         TimeZone tz = cal != null ? cal.getTimeZone() : null;
         this.getTimestampUtils().toBinDate(tz, val, d);
         this.preparedParameters.setBinaryParameter(i, val, 1082);
      } else {
         if (cal == null) {
            cal = this.getDefaultCalendar();
         }

         this.bindString(i, this.getTimestampUtils().toString(cal, d), 0);
      }
   }

   public void setTime(@Positive int i, @Nullable Time t, @Nullable Calendar cal) throws SQLException {
      this.checkClosed();
      if (t == null) {
         this.setNull(i, 92);
      } else {
         int oid = 0;
         if (t instanceof PGTime) {
            PGTime pgTime = (PGTime)t;
            if (pgTime.getCalendar() == null) {
               oid = 1083;
            } else {
               oid = 1266;
               cal = pgTime.getCalendar();
            }
         }

         if (cal == null) {
            cal = this.getDefaultCalendar();
         }

         this.bindString(i, this.getTimestampUtils().toString(cal, t), oid);
      }
   }

   public void setTimestamp(@Positive int i, @Nullable Timestamp t, @Nullable Calendar cal) throws SQLException {
      this.checkClosed();
      if (t == null) {
         this.setNull(i, 93);
      } else {
         int oid = 0;
         if (t instanceof PGTimestamp) {
            PGTimestamp pgTimestamp = (PGTimestamp)t;
            if (pgTimestamp.getCalendar() == null) {
               oid = 1114;
            } else {
               oid = 1184;
               cal = pgTimestamp.getCalendar();
            }
         }

         if (cal == null) {
            cal = this.getDefaultCalendar();
         }

         this.bindString(i, this.getTimestampUtils().toString(cal, t), oid);
      }
   }

   private void setDate(@Positive int i, LocalDate localDate) throws SQLException {
      int oid = 1082;
      this.bindString(i, this.getTimestampUtils().toString(localDate), oid);
   }

   private void setTime(@Positive int i, LocalTime localTime) throws SQLException {
      int oid = 1083;
      this.bindString(i, this.getTimestampUtils().toString(localTime), oid);
   }

   private void setTime(@Positive int i, OffsetTime offsetTime) throws SQLException {
      int oid = 1266;
      this.bindString(i, this.getTimestampUtils().toString(offsetTime), oid);
   }

   private void setTimestamp(@Positive int i, LocalDateTime localDateTime) throws SQLException {
      int oid = 1114;
      this.bindString(i, this.getTimestampUtils().toString(localDateTime), oid);
   }

   private void setTimestamp(@Positive int i, OffsetDateTime offsetDateTime) throws SQLException {
      int oid = 1184;
      this.bindString(i, this.getTimestampUtils().toString(offsetDateTime), oid);
   }

   public ParameterMetaData createParameterMetaData(BaseConnection conn, int[] oids) throws SQLException {
      return new PgParameterMetaData(conn, oids);
   }

   public void setObject(@Positive int parameterIndex, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setObject");
   }

   public void setObject(@Positive int parameterIndex, @Nullable Object x, SQLType targetSqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setObject");
   }

   public void setRowId(@Positive int parameterIndex, @Nullable RowId x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setRowId(int, RowId)");
   }

   public void setNString(@Positive int parameterIndex, @Nullable String value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNString(int, String)");
   }

   public void setNCharacterStream(@Positive int parameterIndex, @Nullable Reader value, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNCharacterStream(int, Reader, long)");
   }

   public void setNCharacterStream(@Positive int parameterIndex, @Nullable Reader value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNCharacterStream(int, Reader)");
   }

   public void setCharacterStream(@Positive int parameterIndex, @Nullable Reader value, @NonNegative long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setCharacterStream(int, Reader, long)");
   }

   public void setCharacterStream(@Positive int parameterIndex, @Nullable Reader value) throws SQLException {
      if (this.connection.getPreferQueryMode() == PreferQueryMode.SIMPLE) {
         String s = value != null ? this.readerToString(value, Integer.MAX_VALUE) : null;
         this.setString(parameterIndex, s);
      } else {
         InputStream is = value != null ? new ReaderInputStream(value) : null;
         this.setObject(parameterIndex, is, -1);
      }
   }

   public void setBinaryStream(@Positive int parameterIndex, @Nullable InputStream value, @NonNegative @IntRange(from = 0L,to = 2147483647L) long length) throws SQLException {
      if (length > 2147483647L) {
         throw new PSQLException(GT.tr("Object is too large to send over the protocol."), PSQLState.NUMERIC_CONSTANT_OUT_OF_RANGE);
      } else {
         if (value == null) {
            this.preparedParameters.setNull(parameterIndex, 17);
         } else {
            this.preparedParameters.setBytea(parameterIndex, value, (int)length);
         }

      }
   }

   public void setBinaryStream(@Positive int parameterIndex, @Nullable InputStream value) throws SQLException {
      if (value == null) {
         this.preparedParameters.setNull(parameterIndex, 17);
      } else {
         this.preparedParameters.setBytea(parameterIndex, value);
      }

   }

   public void setAsciiStream(@Positive int parameterIndex, @Nullable InputStream value, @NonNegative long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setAsciiStream(int, InputStream, long)");
   }

   public void setAsciiStream(@Positive int parameterIndex, @Nullable InputStream value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setAsciiStream(int, InputStream)");
   }

   public void setNClob(@Positive int parameterIndex, @Nullable NClob value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNClob(int, NClob)");
   }

   public void setClob(@Positive int parameterIndex, @Nullable Reader reader, @NonNegative long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setClob(int, Reader, long)");
   }

   public void setClob(@Positive int parameterIndex, @Nullable Reader reader) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setClob(int, Reader)");
   }

   public void setBlob(@Positive int parameterIndex, @Nullable InputStream inputStream, @NonNegative long length) throws SQLException {
      this.checkClosed();
      if (inputStream == null) {
         this.setNull(parameterIndex, 2004);
      } else if (length < 0L) {
         throw new PSQLException(GT.tr("Invalid stream length {0}.", length), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         long oid = this.createBlob(parameterIndex, inputStream, length);
         this.setLong(parameterIndex, oid);
      }
   }

   public void setBlob(@Positive int parameterIndex, @Nullable InputStream inputStream) throws SQLException {
      this.checkClosed();
      if (inputStream == null) {
         this.setNull(parameterIndex, 2004);
      } else {
         long oid = this.createBlob(parameterIndex, inputStream, Long.MAX_VALUE);
         this.setLong(parameterIndex, oid);
      }
   }

   public void setNClob(@Positive int parameterIndex, @Nullable Reader reader, @NonNegative long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNClob(int, Reader, long)");
   }

   public void setNClob(@Positive int parameterIndex, @Nullable Reader reader) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNClob(int, Reader)");
   }

   public void setSQLXML(@Positive int parameterIndex, @Nullable SQLXML xmlObject) throws SQLException {
      this.checkClosed();
      String stringValue = xmlObject == null ? null : xmlObject.getString();
      if (stringValue == null) {
         this.setNull(parameterIndex, 2009);
      } else {
         this.setString(parameterIndex, stringValue, 142);
      }

   }

   private void setUuid(@Positive int parameterIndex, UUID uuid) throws SQLException {
      if (this.connection.binaryTransferSend(2950)) {
         byte[] val = new byte[16];
         ByteConverter.int8(val, 0, uuid.getMostSignificantBits());
         ByteConverter.int8(val, 8, uuid.getLeastSignificantBits());
         this.bindBytes(parameterIndex, val, 2950);
      } else {
         this.bindLiteral(parameterIndex, uuid.toString(), 2950);
      }

   }

   public void setURL(@Positive int parameterIndex, @Nullable URL x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setURL(int,URL)");
   }

   public int[] executeBatch() throws SQLException {
      int[] var1;
      try {
         if (this.batchParameters != null && this.batchParameters.size() > 1 && this.mPrepareThreshold > 0) {
            this.preparedQuery.increaseExecuteCount(this.mPrepareThreshold);
         }

         var1 = super.executeBatch();
      } finally {
         this.defaultTimeZone = null;
      }

      return var1;
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

   public ParameterMetaData getParameterMetaData() throws SQLException {
      int flags = 49;
      PgStatement.StatementResultHandler handler = new PgStatement.StatementResultHandler();
      this.connection.getQueryExecutor().execute((Query)this.preparedQuery.query, (ParameterList)this.preparedParameters, (ResultHandler)handler, 0, 0, flags);
      int[] oids = this.preparedParameters.getTypeOIDs();
      return this.createParameterMetaData(this.connection, oids);
   }

   protected void transformQueriesAndParameters() throws SQLException {
      ArrayList<ParameterList> batchParameters = this.batchParameters;
      if (batchParameters != null && batchParameters.size() > 1 && this.preparedQuery.query instanceof BatchedQuery) {
         BatchedQuery originalQuery = (BatchedQuery)this.preparedQuery.query;
         int bindCount = originalQuery.getBindCount();
         int highestBlockCount = true;
         int maxValueBlocks = bindCount == 0 ? 1024 : Integer.highestOneBit(Math.min(Math.max(1, this.maximumNumberOfParameters() / bindCount), 128));
         int unprocessedBatchCount = batchParameters.size();
         int fullValueBlocksCount = unprocessedBatchCount / maxValueBlocks;
         int partialValueBlocksCount = Integer.bitCount(unprocessedBatchCount % maxValueBlocks);
         int count = fullValueBlocksCount + partialValueBlocksCount;
         ArrayList<Query> newBatchStatements = new ArrayList(count);
         ArrayList<ParameterList> newBatchParameters = new ArrayList(count);
         int offset = 0;

         for(int i = 0; i < count; ++i) {
            int valueBlock;
            if (unprocessedBatchCount >= maxValueBlocks) {
               valueBlock = maxValueBlocks;
            } else {
               valueBlock = Integer.highestOneBit(unprocessedBatchCount);
            }

            BatchedQuery bq = originalQuery.deriveForMultiBatch(valueBlock);
            ParameterList newPl = bq.createParameterList();

            for(int j = 0; j < valueBlock; ++j) {
               ParameterList pl = (ParameterList)batchParameters.get(offset++);
               if (pl != null) {
                  newPl.appendAll(pl);
               }
            }

            newBatchStatements.add(bq);
            newBatchParameters.add(newPl);
            unprocessedBatchCount -= valueBlock;
         }

         this.batchStatements = newBatchStatements;
         this.batchParameters = newBatchParameters;
      }
   }
}
