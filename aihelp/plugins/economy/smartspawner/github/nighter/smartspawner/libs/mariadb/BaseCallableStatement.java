package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.client.result.Result;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.codec.Parameter;
import github.nighter.smartspawner.libs.mariadb.util.ParameterList;
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
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseCallableStatement extends ServerPreparedStatement implements CallableStatement {
   protected final String databaseName;
   protected final String procedureName;
   protected final Set<Integer> outputParameters = new HashSet();
   protected CallableParameterMetaData parameterMetaData = null;
   private Result outputResult = null;

   protected BaseCallableStatement(String sql, Connection con, ClosableLock lock, String databaseName, String procedureName, int resultSetType, int resultSetConcurrency, int defaultFetchSize) throws SQLException {
      super(sql, con, lock, 1, resultSetType, resultSetConcurrency, defaultFetchSize);
      this.databaseName = databaseName;
      this.procedureName = procedureName;
   }

   public abstract boolean isFunction();

   protected void outputResultFromRes(int i) throws SQLException {
      this.outputResult = (Result)this.results.remove(this.results.size() - i);
      this.outputResult.next();
   }

   public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
      this.checkIndex(parameterIndex);
      this.outputParameters.add(parameterIndex);
      if (!this.parameters.containsKey(parameterIndex - 1)) {
         this.parameters.set(parameterIndex - 1, Parameter.NULL_PARAMETER);
      }

   }

   private void checkIndex(int index) throws SQLException {
      if (index <= 0 || this.prepareResult != null && index > this.prepareResult.getParameters().length + (this.isFunction() ? 1 : 0) || this.prepareResult == null && this.parameterMetaData != null && index > this.parameterMetaData.getParameterCount()) {
         throw this.exceptionFactory().create(String.format("wrong parameter index %s", index));
      }
   }

   public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
      this.registerOutParameter(parameterIndex, sqlType);
   }

   public void clearParameters() throws SQLException {
      this.checkNotClosed();
      this.parameters = new ParameterList();
      this.outputParameters.stream().forEach((index) -> {
         this.parameters.set(index - 1, Parameter.NULL_PARAMETER);
      });
   }

   public boolean wasNull() throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.wasNull();
   }

   private int idxToOutIdx(int idx) throws SQLException {
      int outputIndex = 1;
      if (idx < 1) {
         throw this.exceptionFactory().create(String.format("wrong index %s", idx));
      } else if (!this.outputParameters.contains(idx)) {
         throw this.exceptionFactory().create(String.format("index %s not declared as output", idx));
      } else {
         for(int i = 1; i < idx; ++i) {
            if (this.outputParameters.contains(i)) {
               ++outputIndex;
            }
         }

         return outputIndex;
      }
   }

   protected void checkOutputResult() throws SQLException {
      if (this.outputResult == null) {
         throw this.exceptionFactory().create("No output result");
      }
   }

   public String getString(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getString(this.idxToOutIdx(parameterIndex));
   }

   public boolean getBoolean(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getBoolean(this.idxToOutIdx(parameterIndex));
   }

   public byte getByte(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getByte(this.idxToOutIdx(parameterIndex));
   }

   public short getShort(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getShort(this.idxToOutIdx(parameterIndex));
   }

   public int getInt(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getInt(this.idxToOutIdx(parameterIndex));
   }

   public long getLong(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getLong(this.idxToOutIdx(parameterIndex));
   }

   public float getFloat(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getFloat(this.idxToOutIdx(parameterIndex));
   }

   public double getDouble(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getDouble(this.idxToOutIdx(parameterIndex));
   }

   /** @deprecated */
   @Deprecated
   public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getBigDecimal(this.idxToOutIdx(parameterIndex), scale);
   }

   public byte[] getBytes(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getBytes(this.idxToOutIdx(parameterIndex));
   }

   public Date getDate(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getDate(this.idxToOutIdx(parameterIndex));
   }

   public Time getTime(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getTime(this.idxToOutIdx(parameterIndex));
   }

   public Timestamp getTimestamp(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getTimestamp(this.idxToOutIdx(parameterIndex));
   }

   public Object getObject(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getObject(this.idxToOutIdx(parameterIndex));
   }

   public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getBigDecimal(this.idxToOutIdx(parameterIndex));
   }

   public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getObject(this.idxToOutIdx(parameterIndex), map);
   }

   public Ref getRef(int parameterIndex) throws SQLException {
      throw this.exceptionFactory().notSupported("Method ResultSet.getRef not supported");
   }

   public Blob getBlob(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getBlob(this.idxToOutIdx(parameterIndex));
   }

   public Clob getClob(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getClob(this.idxToOutIdx(parameterIndex));
   }

   public Array getArray(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      throw this.exceptionFactory().notSupported("Method ResultSet.getArray not supported");
   }

   public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getDate(this.idxToOutIdx(parameterIndex));
   }

   public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getTime(this.idxToOutIdx(parameterIndex));
   }

   public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getTimestamp(this.idxToOutIdx(parameterIndex));
   }

   public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
      this.registerOutParameter(parameterIndex, sqlType);
   }

   public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
      this.checkNotClosed();
      this.registerOutParameter(this.nameToIndex(parameterName), sqlType);
   }

   private int nameToIndex(String parameterName) throws SQLException {
      if (parameterName == null) {
         throw this.exceptionFactory().create("parameter name cannot be null");
      } else {
         if (this.parameterMetaData == null) {
            this.getParameterMetaData();
         }

         int count = this.parameterMetaData.getParameterCount();

         for(int i = 1; i <= count; ++i) {
            String name = this.parameterMetaData.getParameterName(i);
            if (name != null && name.equalsIgnoreCase(parameterName)) {
               return i;
            }
         }

         throw this.exceptionFactory().create(String.format("parameter name %s not found", parameterName));
      }
   }

   public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
      this.registerOutParameter(parameterName, sqlType);
   }

   public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
      this.registerOutParameter(parameterName, sqlType);
   }

   public URL getURL(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getURL(this.idxToOutIdx(parameterIndex));
   }

   public void setURL(String parameterName, URL val) throws SQLException {
      this.setURL(this.nameToIndex(parameterName), val);
   }

   public void setNull(String parameterName, int sqlType) throws SQLException {
      this.setNull(this.nameToIndex(parameterName), sqlType);
   }

   public void setBoolean(String parameterName, boolean x) throws SQLException {
      this.setBoolean(this.nameToIndex(parameterName), x);
   }

   public void setByte(String parameterName, byte x) throws SQLException {
      this.setByte(this.nameToIndex(parameterName), x);
   }

   public void setShort(String parameterName, short x) throws SQLException {
      this.setShort(this.nameToIndex(parameterName), x);
   }

   public void setInt(String parameterName, int x) throws SQLException {
      this.setInt(this.nameToIndex(parameterName), x);
   }

   public void setLong(String parameterName, long x) throws SQLException {
      this.setLong(this.nameToIndex(parameterName), x);
   }

   public void setFloat(String parameterName, float x) throws SQLException {
      this.setFloat(this.nameToIndex(parameterName), x);
   }

   public void setDouble(String parameterName, double x) throws SQLException {
      this.setDouble(this.nameToIndex(parameterName), x);
   }

   public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
      this.setBigDecimal(this.nameToIndex(parameterName), x);
   }

   public void setString(String parameterName, String x) throws SQLException {
      this.setString(this.nameToIndex(parameterName), x);
   }

   public void setBytes(String parameterName, byte[] x) throws SQLException {
      this.setBytes(this.nameToIndex(parameterName), x);
   }

   public void setDate(String parameterName, Date x) throws SQLException {
      this.setDate(this.nameToIndex(parameterName), x);
   }

   public void setTime(String parameterName, Time x) throws SQLException {
      this.setTime(this.nameToIndex(parameterName), x);
   }

   public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
      this.setTimestamp(this.nameToIndex(parameterName), x);
   }

   public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
      this.setAsciiStream(this.nameToIndex(parameterName), x);
   }

   public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
      this.setBinaryStream(this.nameToIndex(parameterName), x, length);
   }

   public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
      this.setObject(this.nameToIndex(parameterName), x);
   }

   public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
      this.setObject(this.nameToIndex(parameterName), x);
   }

   public void setObject(String parameterName, Object x) throws SQLException {
      this.setObject(this.nameToIndex(parameterName), x);
   }

   public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
      this.setCharacterStream(this.nameToIndex(parameterName), reader, length);
   }

   public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
      this.setDate(this.nameToIndex(parameterName), x, cal);
   }

   public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
      this.setTime(this.nameToIndex(parameterName), x, cal);
   }

   public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
      this.setTimestamp(this.nameToIndex(parameterName), x, cal);
   }

   public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
      this.setNull(this.nameToIndex(parameterName), sqlType, typeName);
   }

   public String getString(String parameterName) throws SQLException {
      return this.outputResult.getString(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public boolean getBoolean(String parameterName) throws SQLException {
      return this.outputResult.getBoolean(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public byte getByte(String parameterName) throws SQLException {
      return this.outputResult.getByte(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public short getShort(String parameterName) throws SQLException {
      return this.outputResult.getShort(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public int getInt(String parameterName) throws SQLException {
      return this.outputResult.getInt(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public long getLong(String parameterName) throws SQLException {
      return this.outputResult.getLong(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public float getFloat(String parameterName) throws SQLException {
      return this.outputResult.getFloat(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public double getDouble(String parameterName) throws SQLException {
      return this.outputResult.getDouble(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public byte[] getBytes(String parameterName) throws SQLException {
      return this.outputResult.getBytes(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public Date getDate(String parameterName) throws SQLException {
      return this.outputResult.getDate(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public Time getTime(String parameterName) throws SQLException {
      return this.outputResult.getTime(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public Timestamp getTimestamp(String parameterName) throws SQLException {
      return this.outputResult.getTimestamp(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public Object getObject(String parameterName) throws SQLException {
      return this.outputResult.getObject(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public BigDecimal getBigDecimal(String parameterName) throws SQLException {
      return this.outputResult.getBigDecimal(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
      return this.outputResult.getObject(this.idxToOutIdx(this.nameToIndex(parameterName)), map);
   }

   public Ref getRef(String parameterName) throws SQLException {
      throw this.exceptionFactory().notSupported("Method ResultSet.getRef not supported");
   }

   public Blob getBlob(String parameterName) throws SQLException {
      return this.outputResult.getBlob(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public Clob getClob(String parameterName) throws SQLException {
      return this.outputResult.getClob(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public Array getArray(String parameterName) throws SQLException {
      throw this.exceptionFactory().notSupported("Method ResultSet.getArray not supported");
   }

   public Date getDate(String parameterName, Calendar cal) throws SQLException {
      return this.outputResult.getDate(this.idxToOutIdx(this.nameToIndex(parameterName)), cal);
   }

   public Time getTime(String parameterName, Calendar cal) throws SQLException {
      return this.outputResult.getTime(this.idxToOutIdx(this.nameToIndex(parameterName)), cal);
   }

   public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
      return this.outputResult.getTimestamp(this.idxToOutIdx(this.nameToIndex(parameterName)), cal);
   }

   public URL getURL(String parameterName) throws SQLException {
      return this.outputResult.getURL(this.idxToOutIdx(this.nameToIndex(parameterName)));
   }

   public RowId getRowId(int parameterIndex) throws SQLException {
      throw this.exceptionFactory().notSupported("RowId are not supported");
   }

   public RowId getRowId(String parameterName) throws SQLException {
      throw this.exceptionFactory().notSupported("RowId are not supported");
   }

   public void setRowId(String parameterName, RowId x) throws SQLException {
      throw this.exceptionFactory().notSupported("RowId parameter are not supported");
   }

   public void setNString(String parameterName, String value) throws SQLException {
      this.setNString(this.nameToIndex(parameterName), value);
   }

   public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
      this.setNCharacterStream(this.nameToIndex(parameterName), value, length);
   }

   public void setNClob(String parameterName, NClob value) throws SQLException {
      this.setNClob(this.nameToIndex(parameterName), value);
   }

   public void setClob(String parameterName, Reader reader, long length) throws SQLException {
      this.setClob(this.nameToIndex(parameterName), reader, length);
   }

   public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
      this.setBlob(this.nameToIndex(parameterName), inputStream, length);
   }

   public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
      this.setNClob(this.nameToIndex(parameterName), reader, length);
   }

   public NClob getNClob(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getNClob(this.idxToOutIdx(parameterIndex));
   }

   public NClob getNClob(String parameterName) throws SQLException {
      return this.getNClob(this.nameToIndex(parameterName));
   }

   public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
      throw this.exceptionFactory().notSupported("SQLXML parameter are not supported");
   }

   public SQLXML getSQLXML(int parameterIndex) throws SQLException {
      throw this.exceptionFactory().notSupported("SQLXML are not supported");
   }

   public SQLXML getSQLXML(String parameterName) throws SQLException {
      throw this.exceptionFactory().notSupported("SQLXML are not supported");
   }

   public String getNString(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getNString(this.idxToOutIdx(parameterIndex));
   }

   public String getNString(String parameterName) throws SQLException {
      return this.getNString(this.nameToIndex(parameterName));
   }

   public Reader getNCharacterStream(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getNCharacterStream(this.idxToOutIdx(parameterIndex));
   }

   public Reader getNCharacterStream(String parameterName) throws SQLException {
      return this.getNCharacterStream(this.nameToIndex(parameterName));
   }

   public Reader getCharacterStream(int parameterIndex) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getCharacterStream(this.idxToOutIdx(parameterIndex));
   }

   public Reader getCharacterStream(String parameterName) throws SQLException {
      return this.getNCharacterStream(this.nameToIndex(parameterName));
   }

   public void setBlob(String parameterName, Blob x) throws SQLException {
      this.setBlob(this.nameToIndex(parameterName), x);
   }

   public void setClob(String parameterName, Clob x) throws SQLException {
      this.setClob(this.nameToIndex(parameterName), x);
   }

   public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
      this.setAsciiStream(this.nameToIndex(parameterName), x, length);
   }

   public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
      this.setBinaryStream(this.nameToIndex(parameterName), x, length);
   }

   public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
      this.setCharacterStream(this.nameToIndex(parameterName), reader, length);
   }

   public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
      this.setAsciiStream(this.nameToIndex(parameterName), x);
   }

   public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
      this.setBinaryStream(this.nameToIndex(parameterName), x);
   }

   public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
      this.setCharacterStream(this.nameToIndex(parameterName), reader);
   }

   public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
      this.setNCharacterStream(this.nameToIndex(parameterName), value);
   }

   public void setClob(String parameterName, Reader reader) throws SQLException {
      this.setClob(this.nameToIndex(parameterName), reader);
   }

   public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
      this.setBlob(this.nameToIndex(parameterName), inputStream);
   }

   public void setNClob(String parameterName, Reader reader) throws SQLException {
      this.setNClob(this.nameToIndex(parameterName), reader);
   }

   public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
      this.checkNotClosed();
      this.checkOutputResult();
      return this.outputResult.getObject(this.idxToOutIdx(parameterIndex), type);
   }

   public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
      return this.getObject(this.nameToIndex(parameterName), type);
   }

   public void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
      this.setObject(this.nameToIndex(parameterName), x, targetSqlType, scaleOrLength);
   }

   public void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException {
      this.setObject(this.nameToIndex(parameterName), x, targetSqlType);
   }

   public void registerOutParameter(int parameterIndex, SQLType sqlType) throws SQLException {
      this.registerOutParameter(parameterIndex, 0);
   }

   public void registerOutParameter(int parameterIndex, SQLType sqlType, int scale) throws SQLException {
      this.registerOutParameter(parameterIndex, sqlType);
   }

   public void registerOutParameter(int parameterIndex, SQLType sqlType, String typeName) throws SQLException {
      this.registerOutParameter(parameterIndex, sqlType);
   }

   public void registerOutParameter(String parameterName, SQLType sqlType) throws SQLException {
      this.registerOutParameter(this.nameToIndex(parameterName), sqlType);
   }

   public void registerOutParameter(String parameterName, SQLType sqlType, int scale) throws SQLException {
      this.registerOutParameter(this.nameToIndex(parameterName), sqlType);
   }

   public void registerOutParameter(String parameterName, SQLType sqlType, String typeName) throws SQLException {
      this.registerOutParameter(this.nameToIndex(parameterName), sqlType);
   }

   public CallableParameterMetaData getParameterMetaData() throws SQLException {
      if (this.parameterMetaData == null) {
         PreparedStatement prep = new ClientPreparedStatement("SELECT * from information_schema.PARAMETERS WHERE SPECIFIC_NAME = ? AND SPECIFIC_SCHEMA = ? ORDER BY ORDINAL_POSITION", this.con, this.lock, 2, 1003, 1007, 0);
         prep.setString(1, this.procedureName);
         prep.setString(2, this.databaseName);
         ResultSet rs = prep.executeQuery();
         this.parameterMetaData = new CallableParameterMetaData(rs, this.isFunction());
      }

      return this.parameterMetaData;
   }
}
