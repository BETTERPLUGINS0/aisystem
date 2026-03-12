package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.Nullable;

class PgCallableStatement extends PgPreparedStatement implements CallableStatement {
   private final boolean isFunction;
   @Nullable
   private int[] functionReturnType;
   @Nullable
   private int[] testReturn;
   private boolean returnTypeSet;
   @Nullable
   protected Object[] callResult;
   private int lastIndex;

   PgCallableStatement(PgConnection connection, String sql, int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
      super(connection, connection.borrowCallableQuery(sql), rsType, rsConcurrency, rsHoldability);
      this.isFunction = this.preparedQuery.isFunction;
      if (this.isFunction) {
         int inParamCount = this.preparedParameters.getInParameterCount() + 1;
         this.testReturn = new int[inParamCount];
         this.functionReturnType = new int[inParamCount];
      }

   }

   public int executeUpdate() throws SQLException {
      if (this.isFunction) {
         this.executeWithFlags(0);
         return 0;
      } else {
         return super.executeUpdate();
      }
   }

   @Nullable
   public Object getObject(@Positive int i, @Nullable Map<String, Class<?>> map) throws SQLException {
      return this.getObjectImpl(i, map);
   }

   @Nullable
   public Object getObject(String s, @Nullable Map<String, Class<?>> map) throws SQLException {
      return this.getObjectImpl(s, map);
   }

   public boolean executeWithFlags(int flags) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      label101: {
         boolean var5;
         try {
            boolean hasResultSet = super.executeWithFlags(flags);
            int[] functionReturnType = this.functionReturnType;
            if (this.isFunction && this.returnTypeSet && functionReturnType != null) {
               if (!hasResultSet) {
                  throw new PSQLException(GT.tr("A CallableStatement was executed with nothing returned."), PSQLState.NO_DATA);
               }

               ResultSet rs = (ResultSet)Nullness.castNonNull(this.getResultSet());
               if (!rs.next()) {
                  throw new PSQLException(GT.tr("A CallableStatement was executed with nothing returned."), PSQLState.NO_DATA);
               }

               int cols = rs.getMetaData().getColumnCount();
               int outParameterCount = this.preparedParameters.getOutParameterCount();
               if (cols != outParameterCount) {
                  throw new PSQLException(GT.tr("A CallableStatement was executed with an invalid number of parameters"), PSQLState.SYNTAX_ERROR);
               }

               this.lastIndex = 0;
               Object[] callResult = new Object[this.preparedParameters.getParameterCount() + 1];
               this.callResult = callResult;
               int i = 0;

               for(int j = 0; i < cols; ++j) {
                  while(j < functionReturnType.length && functionReturnType[j] == 0) {
                     ++j;
                  }

                  callResult[j] = rs.getObject(i + 1);
                  int columnType = rs.getMetaData().getColumnType(i + 1);
                  if (columnType != functionReturnType[j]) {
                     if (columnType == 8 && functionReturnType[j] == 7) {
                        Object result = callResult[j];
                        if (result != null) {
                           callResult[j] = ((Double)result).floatValue();
                        }
                     } else if (columnType != 2012 || functionReturnType[j] != 1111) {
                        throw new PSQLException(GT.tr("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", i + 1, "java.sql.Types=" + columnType, "java.sql.Types=" + functionReturnType[j]), PSQLState.DATA_TYPE_MISMATCH);
                     }
                  }

                  ++i;
               }

               rs.close();
               this.result = null;
               break label101;
            }

            var5 = hasResultSet;
         } catch (Throwable var14) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var13) {
                  var14.addSuppressed(var13);
               }
            }

            throw var14;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return false;
   }

   public void registerOutParameter(@Positive int parameterIndex, int sqlType) throws SQLException {
      this.checkClosed();
      switch(sqlType) {
      case -6:
         sqlType = 5;
      case -5:
      case -2:
      case 0:
      case 1:
      case 2:
      case 4:
      case 5:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      default:
         break;
      case -4:
      case -3:
         sqlType = -2;
         break;
      case -1:
         sqlType = 12;
         break;
      case 3:
         sqlType = 2;
         break;
      case 6:
         sqlType = 8;
         break;
      case 16:
         sqlType = -7;
      }

      int[] functionReturnType = this.functionReturnType;
      int[] testReturn = this.testReturn;
      if (this.isFunction && functionReturnType != null && testReturn != null) {
         this.preparedParameters.registerOutParameter(parameterIndex, sqlType);
         functionReturnType[parameterIndex - 1] = sqlType;
         testReturn[parameterIndex - 1] = sqlType;
         if (functionReturnType[parameterIndex - 1] != 1 && functionReturnType[parameterIndex - 1] != -1) {
            if (functionReturnType[parameterIndex - 1] == 6) {
               testReturn[parameterIndex - 1] = 7;
            }
         } else {
            testReturn[parameterIndex - 1] = 12;
         }

         this.returnTypeSet = true;
      } else {
         throw new PSQLException(GT.tr("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one."), PSQLState.STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL);
      }
   }

   public boolean wasNull() throws SQLException {
      if (this.lastIndex != 0 && this.callResult != null) {
         return this.callResult[this.lastIndex - 1] == null;
      } else {
         throw new PSQLException(GT.tr("wasNull cannot be call before fetching a result."), PSQLState.OBJECT_NOT_IN_STATE);
      }
   }

   @Nullable
   public String getString(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 12, "String");
      return (String)result;
   }

   public boolean getBoolean(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, -7, "Boolean");
      return result == null ? false : BooleanTypeUtil.castToBoolean(result);
   }

   public byte getByte(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 5, "Byte");
      return result == null ? 0 : ((Integer)result).byteValue();
   }

   public short getShort(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 5, "Short");
      return result == null ? 0 : ((Integer)result).shortValue();
   }

   public int getInt(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 4, "Int");
      return result == null ? 0 : (Integer)result;
   }

   public long getLong(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, -5, "Long");
      return result == null ? 0L : (Long)result;
   }

   public float getFloat(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 7, "Float");
      return result == null ? 0.0F : (Float)result;
   }

   public double getDouble(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 8, "Double");
      return result == null ? 0.0D : (Double)result;
   }

   public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 2, "BigDecimal");
      return (BigDecimal)result;
   }

   public byte[] getBytes(int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, -3, -2, "Bytes");
      return (byte[])result;
   }

   public Date getDate(int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 91, "Date");
      return (Date)result;
   }

   public Time getTime(int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 92, "Time");
      return (Time)result;
   }

   public Timestamp getTimestamp(int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 93, "Timestamp");
      return (Timestamp)result;
   }

   @Nullable
   public Object getObject(@Positive int parameterIndex) throws SQLException {
      return this.getCallResult(parameterIndex);
   }

   @Nullable
   protected Object checkIndex(@Positive int parameterIndex, int type1, int type2, String getName) throws SQLException {
      Object result = this.getCallResult(parameterIndex);
      int testReturn = this.testReturn != null ? this.testReturn[parameterIndex - 1] : -1;
      if (type1 != testReturn && type2 != testReturn) {
         throw new PSQLException(GT.tr("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "java.sql.Types=" + testReturn, getName, "java.sql.Types=" + type1), PSQLState.MOST_SPECIFIC_TYPE_DOES_NOT_MATCH);
      } else {
         return result;
      }
   }

   @Nullable
   protected Object checkIndex(@Positive int parameterIndex, int type, String getName) throws SQLException {
      Object result = this.getCallResult(parameterIndex);
      int testReturn = this.testReturn != null ? this.testReturn[parameterIndex - 1] : -1;
      if (type != testReturn) {
         throw new PSQLException(GT.tr("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "java.sql.Types=" + testReturn, getName, "java.sql.Types=" + type), PSQLState.MOST_SPECIFIC_TYPE_DOES_NOT_MATCH);
      } else {
         return result;
      }
   }

   private Object getCallResult(int parameterIndex) throws SQLException {
      this.checkClosed();
      if (!this.isFunction) {
         throw new PSQLException(GT.tr("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made."), PSQLState.STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL);
      } else if (!this.returnTypeSet) {
         throw new PSQLException(GT.tr("No function outputs were registered."), PSQLState.OBJECT_NOT_IN_STATE);
      } else {
         Object[] callResult = this.callResult;
         if (callResult == null) {
            throw new PSQLException(GT.tr("Results cannot be retrieved from a CallableStatement before it is executed."), PSQLState.NO_DATA);
         } else {
            this.lastIndex = parameterIndex;
            return callResult[parameterIndex - 1];
         }
      }
   }

   protected BatchResultHandler createBatchHandler(Query[] queries, ParameterList[] parameterLists) {
      return new CallableBatchResultHandler(this, queries, parameterLists);
   }

   @Nullable
   public Array getArray(int i) throws SQLException {
      Object result = this.checkIndex(i, 2003, "Array");
      return (Array)result;
   }

   @Nullable
   public BigDecimal getBigDecimal(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 2, "BigDecimal");
      return (BigDecimal)result;
   }

   @Nullable
   public Blob getBlob(int i) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getBlob(int)");
   }

   @Nullable
   public Clob getClob(int i) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getClob(int)");
   }

   @Nullable
   public Object getObjectImpl(int i, @Nullable Map<String, Class<?>> map) throws SQLException {
      if (map != null && !map.isEmpty()) {
         throw Driver.notImplemented(this.getClass(), "getObjectImpl(int,Map)");
      } else {
         return this.getObject(i);
      }
   }

   @Nullable
   public Ref getRef(int i) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getRef(int)");
   }

   @Nullable
   public Date getDate(int i, @Nullable Calendar cal) throws SQLException {
      Object result = this.checkIndex(i, 91, "Date");
      if (result == null) {
         return null;
      } else {
         String value = result.toString();
         return this.getTimestampUtils().toDate(cal, value);
      }
   }

   @Nullable
   public Time getTime(int i, @Nullable Calendar cal) throws SQLException {
      Object result = this.checkIndex(i, 92, "Time");
      if (result == null) {
         return null;
      } else {
         String value = result.toString();
         return this.getTimestampUtils().toTime(cal, value);
      }
   }

   @Nullable
   public Timestamp getTimestamp(int i, @Nullable Calendar cal) throws SQLException {
      Object result = this.checkIndex(i, 93, "Timestamp");
      if (result == null) {
         return null;
      } else {
         String value = result.toString();
         return this.getTimestampUtils().toTimestamp(cal, value);
      }
   }

   public void registerOutParameter(@Positive int parameterIndex, int sqlType, String typeName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter(int,int,String)");
   }

   public void setObject(String parameterName, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setObject");
   }

   public void setObject(String parameterName, @Nullable Object x, SQLType targetSqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setObject");
   }

   public void registerOutParameter(@Positive int parameterIndex, SQLType sqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter");
   }

   public void registerOutParameter(@Positive int parameterIndex, SQLType sqlType, int scale) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter");
   }

   public void registerOutParameter(@Positive int parameterIndex, SQLType sqlType, String typeName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter");
   }

   public void registerOutParameter(String parameterName, SQLType sqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter");
   }

   public void registerOutParameter(String parameterName, SQLType sqlType, int scale) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter");
   }

   public void registerOutParameter(String parameterName, SQLType sqlType, String typeName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter");
   }

   @Nullable
   public RowId getRowId(@Positive int parameterIndex) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getRowId(int)");
   }

   @Nullable
   public RowId getRowId(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getRowId(String)");
   }

   public void setRowId(String parameterName, @Nullable RowId x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setRowId(String, RowId)");
   }

   public void setNString(String parameterName, @Nullable String value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNString(String, String)");
   }

   public void setNCharacterStream(String parameterName, @Nullable Reader value, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNCharacterStream(String, Reader, long)");
   }

   public void setNCharacterStream(String parameterName, @Nullable Reader value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNCharacterStream(String, Reader)");
   }

   public void setCharacterStream(String parameterName, @Nullable Reader value, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setCharacterStream(String, Reader, long)");
   }

   public void setCharacterStream(String parameterName, @Nullable Reader value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setCharacterStream(String, Reader)");
   }

   public void setBinaryStream(String parameterName, @Nullable InputStream value, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBinaryStream(String, InputStream, long)");
   }

   public void setBinaryStream(String parameterName, @Nullable InputStream value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBinaryStream(String, InputStream)");
   }

   public void setAsciiStream(String parameterName, @Nullable InputStream value, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setAsciiStream(String, InputStream, long)");
   }

   public void setAsciiStream(String parameterName, @Nullable InputStream value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setAsciiStream(String, InputStream)");
   }

   public void setNClob(String parameterName, @Nullable NClob value) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNClob(String, NClob)");
   }

   public void setClob(String parameterName, @Nullable Reader reader, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setClob(String, Reader, long)");
   }

   public void setClob(String parameterName, @Nullable Reader reader) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setClob(String, Reader)");
   }

   public void setBlob(String parameterName, @Nullable InputStream inputStream, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBlob(String, InputStream, long)");
   }

   public void setBlob(String parameterName, @Nullable InputStream inputStream) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBlob(String, InputStream)");
   }

   public void setBlob(String parameterName, @Nullable Blob x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBlob(String, Blob)");
   }

   public void setClob(String parameterName, @Nullable Clob x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setClob(String, Clob)");
   }

   public void setNClob(String parameterName, @Nullable Reader reader, long length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNClob(String, Reader, long)");
   }

   public void setNClob(String parameterName, @Nullable Reader reader) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNClob(String, Reader)");
   }

   @Nullable
   public NClob getNClob(@Positive int parameterIndex) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getNClob(int)");
   }

   @Nullable
   public NClob getNClob(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getNClob(String)");
   }

   public void setSQLXML(String parameterName, @Nullable SQLXML xmlObject) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setSQLXML(String, SQLXML)");
   }

   @Nullable
   public SQLXML getSQLXML(@Positive int parameterIndex) throws SQLException {
      Object result = this.checkIndex(parameterIndex, 2009, "SQLXML");
      return (SQLXML)result;
   }

   @Nullable
   public SQLXML getSQLXML(String parameterIndex) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getSQLXML(String)");
   }

   public String getNString(@Positive int parameterIndex) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getNString(int)");
   }

   @Nullable
   public String getNString(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getNString(String)");
   }

   @Nullable
   public Reader getNCharacterStream(@Positive int parameterIndex) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getNCharacterStream(int)");
   }

   @Nullable
   public Reader getNCharacterStream(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getNCharacterStream(String)");
   }

   @Nullable
   public Reader getCharacterStream(@Positive int parameterIndex) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getCharacterStream(int)");
   }

   @Nullable
   public Reader getCharacterStream(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getCharacterStream(String)");
   }

   @Nullable
   public <T> T getObject(@Positive int parameterIndex, Class<T> type) throws SQLException {
      if (type == ResultSet.class) {
         return type.cast(this.getObject(parameterIndex));
      } else {
         throw new PSQLException(GT.tr("Unsupported type conversion to {1}.", type), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   @Nullable
   public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getObject(String, Class<T>)");
   }

   public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter(String,int)");
   }

   public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter(String,int,int)");
   }

   public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "registerOutParameter(String,int,String)");
   }

   @Nullable
   public URL getURL(@Positive int parameterIndex) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getURL(String)");
   }

   public void setURL(String parameterName, @Nullable URL val) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setURL(String,URL)");
   }

   public void setNull(String parameterName, int sqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNull(String,int)");
   }

   public void setBoolean(String parameterName, boolean x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBoolean(String,boolean)");
   }

   public void setByte(String parameterName, byte x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setByte(String,byte)");
   }

   public void setShort(String parameterName, short x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setShort(String,short)");
   }

   public void setInt(String parameterName, int x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setInt(String,int)");
   }

   public void setLong(String parameterName, long x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setLong(String,long)");
   }

   public void setFloat(String parameterName, float x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setFloat(String,float)");
   }

   public void setDouble(String parameterName, double x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setDouble(String,double)");
   }

   public void setBigDecimal(String parameterName, @Nullable BigDecimal x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBigDecimal(String,BigDecimal)");
   }

   public void setString(String parameterName, @Nullable String x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setString(String,String)");
   }

   public void setBytes(String parameterName, @Nullable byte[] x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBytes(String,byte)");
   }

   public void setDate(String parameterName, @Nullable Date x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setDate(String,Date)");
   }

   public void setTime(String parameterName, @Nullable Time x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setTime(String,Time)");
   }

   public void setTimestamp(String parameterName, @Nullable Timestamp x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setTimestamp(String,Timestamp)");
   }

   public void setAsciiStream(String parameterName, @Nullable InputStream x, int length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setAsciiStream(String,InputStream,int)");
   }

   public void setBinaryStream(String parameterName, @Nullable InputStream x, int length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setBinaryStream(String,InputStream,int)");
   }

   public void setObject(String parameterName, @Nullable Object x, int targetSqlType, int scale) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setObject(String,Object,int,int)");
   }

   public void setObject(String parameterName, @Nullable Object x, int targetSqlType) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setObject(String,Object,int)");
   }

   public void setObject(String parameterName, @Nullable Object x) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setObject(String,Object)");
   }

   public void setCharacterStream(String parameterName, @Nullable Reader reader, int length) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setCharacterStream(String,Reader,int)");
   }

   public void setDate(String parameterName, @Nullable Date x, @Nullable Calendar cal) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setDate(String,Date,Calendar)");
   }

   public void setTime(String parameterName, @Nullable Time x, @Nullable Calendar cal) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setTime(String,Time,Calendar)");
   }

   public void setTimestamp(String parameterName, @Nullable Timestamp x, @Nullable Calendar cal) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setTimestamp(String,Timestamp,Calendar)");
   }

   public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setNull(String,int,String)");
   }

   @Nullable
   public String getString(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getString(String)");
   }

   public boolean getBoolean(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getBoolean(String)");
   }

   public byte getByte(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getByte(String)");
   }

   public short getShort(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getShort(String)");
   }

   public int getInt(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getInt(String)");
   }

   public long getLong(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getLong(String)");
   }

   public float getFloat(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getFloat(String)");
   }

   public double getDouble(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getDouble(String)");
   }

   @Nullable
   public byte[] getBytes(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getBytes(String)");
   }

   @Nullable
   public Date getDate(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getDate(String)");
   }

   public Time getTime(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getTime(String)");
   }

   @Nullable
   public Timestamp getTimestamp(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getTimestamp(String)");
   }

   @Nullable
   public Object getObject(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getObject(String)");
   }

   @Nullable
   public BigDecimal getBigDecimal(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getBigDecimal(String)");
   }

   @Nullable
   public Object getObjectImpl(String parameterName, @Nullable Map<String, Class<?>> map) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getObject(String,Map)");
   }

   @Nullable
   public Ref getRef(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getRef(String)");
   }

   @Nullable
   public Blob getBlob(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getBlob(String)");
   }

   @Nullable
   public Clob getClob(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getClob(String)");
   }

   @Nullable
   public Array getArray(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getArray(String)");
   }

   @Nullable
   public Date getDate(String parameterName, @Nullable Calendar cal) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getDate(String,Calendar)");
   }

   @Nullable
   public Time getTime(String parameterName, @Nullable Calendar cal) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getTime(String,Calendar)");
   }

   @Nullable
   public Timestamp getTimestamp(String parameterName, @Nullable Calendar cal) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getTimestamp(String,Calendar)");
   }

   @Nullable
   public URL getURL(String parameterName) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getURL(String)");
   }

   public void registerOutParameter(@Positive int parameterIndex, int sqlType, int scale) throws SQLException {
      this.registerOutParameter(parameterIndex, sqlType);
   }
}
