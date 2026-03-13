package github.nighter.smartspawner.libs.mariadb.client.result;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.Column;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.result.rowdecoder.BinaryRowDecoder;
import github.nighter.smartspawner.libs.mariadb.client.result.rowdecoder.RowDecoder;
import github.nighter.smartspawner.libs.mariadb.client.result.rowdecoder.TextRowDecoder;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.client.util.MutableInt;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.message.server.ErrorPacket;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import github.nighter.smartspawner.libs.mariadb.plugin.array.FloatArray;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.BigDecimalCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.BigIntegerCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.BlobCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.ByteArrayCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.ClobCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.FloatArrayCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.ReaderCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.StreamCodec;
import github.nighter.smartspawner.libs.mariadb.plugin.codec.StringCodec;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class Result implements ResultSet, Completion {
   public static final int NULL_LENGTH = -1;
   private static final BinaryRowDecoder BINARY_ROW_DECODER = new BinaryRowDecoder();
   private static final TextRowDecoder TEXT_ROW_DECODER = new TextRowDecoder();
   protected final int resultSetType;
   protected final ExceptionFactory exceptionFactory;
   protected final Reader reader;
   protected final Context context;
   protected final ColumnDecoder[] metadataList;
   protected final RowDecoder rowDecoder;
   protected final StandardReadableByteBuf rowBuf = new StandardReadableByteBuf((byte[])null, 0);
   protected final boolean traceEnable;
   private final int maxIndex;
   private final MutableInt fieldLength = new MutableInt(0);
   private final boolean forceAlias;
   private final byte[] nullBitmap;
   protected int dataSize = 0;
   protected byte[][] data;
   protected MutableInt fieldIndex = new MutableInt();
   protected boolean loaded;
   protected boolean outputParameter;
   protected int rowPointer = -1;
   protected boolean closed;
   protected Statement statement;
   protected long maxRows;
   private boolean closeOnCompletion;
   private Map<String, Integer> mapper = null;
   private int fetchSize;

   public Result(github.nighter.smartspawner.libs.mariadb.Statement stmt, boolean binaryProtocol, long maxRows, ColumnDecoder[] metadataList, Reader reader, Context context, int resultSetType, boolean closeOnCompletion, boolean traceEnable, boolean forceAlias, int fetchSize) {
      this.maxRows = maxRows;
      this.statement = stmt;
      this.closeOnCompletion = closeOnCompletion;
      this.metadataList = metadataList;
      this.maxIndex = this.metadataList.length;
      this.reader = reader;
      this.exceptionFactory = context.getExceptionFactory();
      this.context = context;
      this.resultSetType = resultSetType;
      this.traceEnable = traceEnable;
      this.forceAlias = forceAlias;
      this.fetchSize = fetchSize;
      if (binaryProtocol) {
         this.rowDecoder = BINARY_ROW_DECODER;
         this.nullBitmap = new byte[(this.maxIndex + 9) / 8];
      } else {
         this.rowDecoder = TEXT_ROW_DECODER;
         this.nullBitmap = null;
      }

   }

   protected Result(ColumnDecoder[] metadataList, Result prev) {
      this.maxRows = prev.maxRows;
      this.statement = prev.statement;
      this.closeOnCompletion = prev.closeOnCompletion;
      this.metadataList = metadataList;
      this.maxIndex = metadataList.length;
      this.reader = prev.reader;
      this.exceptionFactory = prev.exceptionFactory;
      this.context = prev.context;
      this.resultSetType = prev.resultSetType;
      this.traceEnable = prev.traceEnable;
      this.forceAlias = true;
      this.rowDecoder = prev.rowDecoder;
      this.nullBitmap = prev.nullBitmap;
      this.data = prev.data;
      this.dataSize = prev.dataSize;
      this.fetchSize = prev.fetchSize;
      this.loaded = prev.loaded;
      this.outputParameter = prev.outputParameter;
   }

   public Result(ColumnDecoder[] metadataList, byte[][] data, Context context, int resultSetType) {
      this.metadataList = metadataList;
      this.maxIndex = this.metadataList.length;
      this.reader = null;
      this.loaded = true;
      this.exceptionFactory = context.getExceptionFactory();
      this.context = context;
      this.data = data;
      this.dataSize = data.length;
      this.statement = null;
      this.resultSetType = resultSetType;
      this.closeOnCompletion = false;
      this.traceEnable = false;
      this.rowDecoder = TEXT_ROW_DECODER;
      this.nullBitmap = null;
      this.forceAlias = false;
   }

   protected boolean readNext(byte[] buf) throws IOException, SQLException {
      switch(buf[0]) {
      case -2:
         if (this.context.isEofDeprecated() && buf.length < 16777215 || !this.context.isEofDeprecated() && buf.length < 8) {
            ReadableByteBuf readBuf = this.reader.readableBufFromArray(buf);
            readBuf.skip();
            int serverStatus;
            int warnings;
            if (!this.context.isEofDeprecated()) {
               warnings = readBuf.readUnsignedShort();
               serverStatus = readBuf.readUnsignedShort();
            } else {
               readBuf.readLongLengthEncodedNotNull();
               readBuf.readLongLengthEncodedNotNull();
               serverStatus = readBuf.readUnsignedShort();
               warnings = readBuf.readUnsignedShort();
            }

            this.outputParameter = (serverStatus & 4096) != 0;
            if ((serverStatus & 8) == 0) {
               this.setBulkResult();
            }

            this.context.setServerStatus(serverStatus);
            this.context.setWarning(warnings);
            this.loaded = true;
            return false;
         }
      default:
         if (this.dataSize + 1 > this.data.length) {
            this.growDataArray();
         }

         this.data[this.dataSize++] = buf;
         return true;
      case -1:
         this.loaded = true;
         ErrorPacket errorPacket = new ErrorPacket(this.reader.readableBufFromArray(buf), this.context);
         throw this.exceptionFactory.create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
      }
   }

   public abstract void setBulkResult();

   public void closeOnCompletion() throws SQLException {
      this.closeOnCompletion = true;
   }

   protected void skipRemaining() throws IOException, SQLException {
      while(true) {
         ReadableByteBuf buf = this.reader.readReusablePacket(this.traceEnable);
         switch(buf.getUnsignedByte()) {
         case 254:
            if (this.context.isEofDeprecated() && buf.readableBytes() < 16777215 || !this.context.isEofDeprecated() && buf.readableBytes() < 8) {
               buf.skip();
               int serverStatus;
               int warnings;
               if (!this.context.isEofDeprecated()) {
                  warnings = buf.readUnsignedShort();
                  serverStatus = buf.readUnsignedShort();
               } else {
                  buf.readLongLengthEncodedNotNull();
                  buf.readLongLengthEncodedNotNull();
                  serverStatus = buf.readUnsignedShort();
                  warnings = buf.readUnsignedShort();
               }

               this.outputParameter = (serverStatus & 4096) != 0;
               this.context.setServerStatus(serverStatus);
               this.context.setWarning(warnings);
               this.loaded = true;
               return;
            }
            break;
         case 255:
            this.loaded = true;
            ErrorPacket errorPacket = new ErrorPacket(buf, this.context);
            throw this.exceptionFactory.create(errorPacket.getMessage(), errorPacket.getSqlState(), errorPacket.getErrorCode());
         }
      }
   }

   private void growDataArray() {
      int newCapacity = Math.max(10, this.data.length << 1);
      byte[][] newData = new byte[newCapacity][];
      System.arraycopy(this.data, 0, newData, 0, this.data.length);
      this.data = newData;
   }

   public abstract boolean next() throws SQLException;

   public abstract boolean streaming();

   public abstract boolean isBulkResult();

   public abstract void fetchRemaining() throws SQLException;

   public boolean loaded() {
      return this.loaded;
   }

   public boolean isOutputParameter() {
      return this.outputParameter;
   }

   public void close() throws SQLException {
      if (!this.loaded) {
         try {
            this.skipRemaining();
         } catch (IOException var2) {
            throw this.exceptionFactory.create("Error while streaming resultSet data", "08000", var2);
         }
      }

      this.closed = true;
      if (this.closeOnCompletion && (this.context.getServerStatus() & 8) == 0) {
         this.statement.close();
      }

   }

   public void closeFromStmtClose(ClosableLock lock) throws SQLException {
      ClosableLock ignore = lock.closeableLock();

      try {
         this.fetchRemaining();
         this.closed = true;
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

   public void abort() {
      this.closed = true;
   }

   protected byte[] getCurrentRowData() {
      return this.data[0];
   }

   protected void addRowData(byte[] buf) {
      if (this.dataSize + 1 > this.data.length) {
         this.growDataArray();
      }

      this.data[this.dataSize++] = buf;
   }

   protected void updateRowData(byte[] rawData) {
      this.data[this.rowPointer] = rawData;
      if (rawData == null) {
         this.setNullRowBuf();
      } else {
         this.setRow(rawData);
         this.fieldIndex.set(-1);
      }

   }

   private void checkIndex(int index) throws SQLException {
      if (index >= 1 && index <= this.maxIndex) {
         if (this.rowBuf.buf == null) {
            throw new SQLDataException("wrong row position", "22023");
         }
      } else {
         throw new SQLException(String.format("Wrong index position. Is %s but must be in 1-%s range", index, this.maxIndex));
      }
   }

   public boolean wasNull() {
      return this.rowDecoder.wasNull(this.nullBitmap, this.fieldIndex, this.fieldLength);
   }

   public String getString(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : this.rowDecoder.decodeString(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, this.context);
   }

   public boolean getBoolean(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? false : this.rowDecoder.decodeBoolean(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength);
   }

   public byte getByte(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? 0 : this.rowDecoder.decodeByte(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength);
   }

   public short getShort(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? 0 : this.rowDecoder.decodeShort(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength);
   }

   public int getInt(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? 0 : this.rowDecoder.decodeInt(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength);
   }

   public long getLong(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? 0L : this.rowDecoder.decodeLong(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength);
   }

   public BigInteger getBigInteger(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (BigInteger)this.rowDecoder.decode(BigIntegerCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public BigInteger getBigInteger(String columnLabel) throws SQLException {
      return this.getBigInteger(this.findColumn(columnLabel));
   }

   public float getFloat(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? 0.0F : this.rowDecoder.decodeFloat(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength);
   }

   public double getDouble(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? 0.0D : this.rowDecoder.decodeDouble(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength);
   }

   /** @deprecated */
   @Deprecated
   public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      if (this.fieldLength.get() == -1) {
         return null;
      } else {
         BigDecimal d = (BigDecimal)this.rowDecoder.decode(BigDecimalCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
         return d == null ? null : d.setScale(scale, RoundingMode.HALF_DOWN);
      }
   }

   public byte[] getBytes(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (byte[])this.rowDecoder.decode(ByteArrayCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public Date getDate(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : this.rowDecoder.decodeDate(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, (Calendar)null, this.context);
   }

   public Time getTime(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : this.rowDecoder.decodeTime(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, (Calendar)null, this.context);
   }

   public Timestamp getTimestamp(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : this.rowDecoder.decodeTimestamp(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, (Calendar)null, this.context);
   }

   public InputStream getAsciiStream(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (InputStream)this.rowDecoder.decode(StreamCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   /** @deprecated */
   @Deprecated
   public InputStream getUnicodeStream(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (InputStream)this.rowDecoder.decode(StreamCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public InputStream getBinaryStream(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (InputStream)this.rowDecoder.decode(StreamCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public String getString(String columnLabel) throws SQLException {
      return this.getString(this.findColumn(columnLabel));
   }

   public boolean getBoolean(String columnLabel) throws SQLException {
      return this.getBoolean(this.findColumn(columnLabel));
   }

   public byte getByte(String columnLabel) throws SQLException {
      return this.getByte(this.findColumn(columnLabel));
   }

   public short getShort(String columnLabel) throws SQLException {
      return this.getShort(this.findColumn(columnLabel));
   }

   public int getInt(String columnLabel) throws SQLException {
      return this.getInt(this.findColumn(columnLabel));
   }

   public long getLong(String columnLabel) throws SQLException {
      return this.getLong(this.findColumn(columnLabel));
   }

   public float getFloat(String columnLabel) throws SQLException {
      return this.getFloat(this.findColumn(columnLabel));
   }

   public double getDouble(String columnLabel) throws SQLException {
      return this.getDouble(this.findColumn(columnLabel));
   }

   /** @deprecated */
   @Deprecated
   public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
      return this.getBigDecimal(this.findColumn(columnLabel), scale);
   }

   public byte[] getBytes(String columnLabel) throws SQLException {
      return this.getBytes(this.findColumn(columnLabel));
   }

   public Date getDate(String columnLabel) throws SQLException {
      return this.getDate(this.findColumn(columnLabel));
   }

   public Time getTime(String columnLabel) throws SQLException {
      return this.getTime(this.findColumn(columnLabel));
   }

   public Timestamp getTimestamp(String columnLabel) throws SQLException {
      return this.getTimestamp(this.findColumn(columnLabel));
   }

   public InputStream getAsciiStream(String columnLabel) throws SQLException {
      return this.getAsciiStream(this.findColumn(columnLabel));
   }

   /** @deprecated */
   @Deprecated
   public InputStream getUnicodeStream(String columnLabel) throws SQLException {
      return this.getUnicodeStream(this.findColumn(columnLabel));
   }

   public InputStream getBinaryStream(String columnLabel) throws SQLException {
      return this.getBinaryStream(this.findColumn(columnLabel));
   }

   public SQLWarning getWarnings() throws SQLException {
      return this.statement == null ? null : this.statement.getWarnings();
   }

   public void clearWarnings() throws SQLException {
      if (this.statement != null) {
         this.statement.clearWarnings();
      }

   }

   public String getCursorName() throws SQLException {
      throw this.exceptionFactory.notSupported("Cursors are not supported");
   }

   public ResultSetMetaData getMetaData() {
      return new ResultSetMetaData(this.exceptionFactory, this.metadataList, this.context.getConf(), this.forceAlias);
   }

   public Object getObject(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : this.rowDecoder.defaultDecode(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, this.context);
   }

   public Object getObject(String columnLabel) throws SQLException {
      return this.getObject(this.findColumn(columnLabel));
   }

   public java.io.Reader getCharacterStream(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (java.io.Reader)this.rowDecoder.decode(ReaderCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public java.io.Reader getCharacterStream(String columnLabel) throws SQLException {
      return this.getCharacterStream(this.findColumn(columnLabel));
   }

   public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (BigDecimal)this.rowDecoder.decode(BigDecimalCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
      return this.getBigDecimal(this.findColumn(columnLabel));
   }

   protected void checkClose() throws SQLException {
      if (this.closed) {
         throw this.exceptionFactory.create("Operation not permit on a closed resultSet", "HY000");
      }
   }

   protected void checkNotForwardOnly() throws SQLException {
      if (this.resultSetType == 1003) {
         throw this.exceptionFactory.create("Operation not permit on TYPE_FORWARD_ONLY resultSet", "HY000");
      }
   }

   public boolean isBeforeFirst() throws SQLException {
      this.checkClose();
      return this.rowPointer == -1 && this.dataSize > 0;
   }

   public abstract boolean isAfterLast() throws SQLException;

   public abstract boolean isFirst() throws SQLException;

   public abstract boolean isLast() throws SQLException;

   public abstract void beforeFirst() throws SQLException;

   public abstract void afterLast() throws SQLException;

   public abstract boolean first() throws SQLException;

   public abstract boolean last() throws SQLException;

   public abstract int getRow() throws SQLException;

   public void setRow(byte[] row) {
      this.rowBuf.buf(row, row.length, 0);
      this.fieldIndex.set(-1);
   }

   public abstract boolean absolute(int var1) throws SQLException;

   public abstract boolean relative(int var1) throws SQLException;

   public abstract boolean previous() throws SQLException;

   public int getFetchDirection() {
      return 1002;
   }

   public void setFetchDirection(int direction) throws SQLException {
      if (direction == 1001) {
         throw this.exceptionFactory.create("Invalid operation. Allowed direction are ResultSet.FETCH_FORWARD and ResultSet.FETCH_UNKNOWN");
      }
   }

   public int getType() {
      return this.resultSetType;
   }

   public int getConcurrency() {
      return 1007;
   }

   public boolean rowUpdated() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public boolean rowInserted() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public boolean rowDeleted() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNull(int columnIndex) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBoolean(int columnIndex, boolean x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateByte(int columnIndex, byte x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateShort(int columnIndex, short x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateInt(int columnIndex, int x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateLong(int columnIndex, long x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateFloat(int columnIndex, float x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateDouble(int columnIndex, double x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateString(int columnIndex, String x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBytes(int columnIndex, byte[] x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateDate(int columnIndex, Date x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateTime(int columnIndex, Time x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateObject(int columnIndex, Object x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNull(String columnLabel) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBoolean(String columnLabel, boolean x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateByte(String columnLabel, byte x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateShort(String columnLabel, short x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateInt(String columnLabel, int x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateLong(String columnLabel, long x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateFloat(String columnLabel, float x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateDouble(String columnLabel, double x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateString(String columnLabel, String x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBytes(String columnLabel, byte[] x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateDate(String columnLabel, Date x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateTime(String columnLabel, Time x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateCharacterStream(String columnLabel, java.io.Reader reader, int length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateObject(String columnLabel, Object x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void insertRow() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateRow() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void deleteRow() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void refreshRow() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void cancelRowUpdates() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void moveToInsertRow() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void moveToCurrentRow() throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public Statement getStatement() {
      return this.statement;
   }

   public void setStatement(Statement stmt) {
      this.statement = stmt;
   }

   public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
      if (map != null && !map.isEmpty()) {
         throw this.exceptionFactory.notSupported("Method ResultSet.getObject(int columnIndex, Map<String, Class<?>> map) not supported for non empty map");
      } else {
         return this.getObject(columnIndex);
      }
   }

   public Ref getRef(int columnIndex) throws SQLException {
      throw this.exceptionFactory.notSupported("Method ResultSet.getRef not supported");
   }

   public Blob getBlob(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (Blob)this.rowDecoder.decode(BlobCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public Clob getClob(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (Clob)this.rowDecoder.decode(ClobCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public Array getArray(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      if (this.fieldLength.get() == -1) {
         return null;
      } else {
         float[] val = (float[])this.rowDecoder.decode(FloatArrayCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
         return new FloatArray(val, this.context);
      }
   }

   public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
      if (map != null && !map.isEmpty()) {
         throw this.exceptionFactory.notSupported("Method ResultSet.getObject(String columnLabel, Map<String, Class<?>> map) not supported");
      } else {
         return this.getObject(columnLabel);
      }
   }

   public Ref getRef(String columnLabel) throws SQLException {
      throw this.exceptionFactory.notSupported("Method ResultSet.getRef not supported");
   }

   public Blob getBlob(String columnLabel) throws SQLException {
      return this.getBlob(this.findColumn(columnLabel));
   }

   public Clob getClob(String columnLabel) throws SQLException {
      return this.getClob(this.findColumn(columnLabel));
   }

   public Array getArray(String columnLabel) throws SQLException {
      return this.getArray(this.findColumn(columnLabel));
   }

   public Date getDate(int columnIndex, Calendar cal) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : this.rowDecoder.decodeDate(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, cal, this.context);
   }

   public Date getDate(String columnLabel, Calendar cal) throws SQLException {
      return this.getDate(this.findColumn(columnLabel), cal);
   }

   public Time getTime(int columnIndex, Calendar cal) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : this.rowDecoder.decodeTime(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, cal, this.context);
   }

   public Time getTime(String columnLabel, Calendar cal) throws SQLException {
      return this.getTime(this.findColumn(columnLabel), cal);
   }

   public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : this.rowDecoder.decodeTimestamp(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, cal, this.context);
   }

   public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
      return this.getTimestamp(this.findColumn(columnLabel), cal);
   }

   public URL getURL(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      if (this.fieldLength.get() == -1) {
         return null;
      } else {
         String s = (String)this.rowDecoder.decode(StringCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
         if (s == null) {
            return null;
         } else {
            try {
               return (new URI(s)).toURL();
            } catch (Exception var4) {
               throw this.exceptionFactory.create(String.format("Could not parse '%s' as URL", s));
            }
         }
      }
   }

   public URL getURL(String columnLabel) throws SQLException {
      return this.getURL(this.findColumn(columnLabel));
   }

   public void updateRef(int columnIndex, Ref x) throws SQLException {
      throw this.exceptionFactory.notSupported("Method ResultSet.updateRef not supported");
   }

   public void updateRef(String columnLabel, Ref x) throws SQLException {
      throw this.exceptionFactory.notSupported("Method ResultSet.updateRef not supported");
   }

   public void updateBlob(int columnIndex, Blob x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBlob(String columnLabel, Blob x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateClob(int columnIndex, Clob x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateClob(String columnLabel, Clob x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateArray(int columnIndex, Array x) throws SQLException {
      throw this.exceptionFactory.notSupported("Array are not supported");
   }

   public void updateArray(String columnLabel, Array x) throws SQLException {
      this.updateArray(this.findColumn(columnLabel), x);
   }

   public RowId getRowId(int columnIndex) throws SQLException {
      throw this.exceptionFactory.notSupported("RowId are not supported");
   }

   public RowId getRowId(String columnLabel) throws SQLException {
      throw this.exceptionFactory.notSupported("RowId are not supported");
   }

   public void updateRowId(int columnIndex, RowId x) throws SQLException {
      throw this.exceptionFactory.notSupported("RowId are not supported");
   }

   public void updateRowId(String columnLabel, RowId x) throws SQLException {
      throw this.exceptionFactory.notSupported("RowId are not supported");
   }

   public int getHoldability() {
      return 1;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public void updateNString(int columnIndex, String nString) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNString(String columnLabel, String nString) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public NClob getNClob(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (NClob)this.rowDecoder.decode(ClobCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public NClob getNClob(String columnLabel) throws SQLException {
      return this.getNClob(this.findColumn(columnLabel));
   }

   public SQLXML getSQLXML(int columnIndex) throws SQLException {
      throw this.exceptionFactory.notSupported("Method ResultSet.getSQLXML not supported");
   }

   public SQLXML getSQLXML(String columnLabel) throws SQLException {
      throw this.exceptionFactory.notSupported("Method ResultSet.getSQLXML not supported");
   }

   public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
      throw this.exceptionFactory.notSupported("Method ResultSet.updateSQLXML not supported");
   }

   public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
      throw this.exceptionFactory.notSupported("Method ResultSet.updateSQLXML not supported");
   }

   public String getNString(int columnIndex) throws SQLException {
      return this.getString(columnIndex);
   }

   public String getNString(String columnLabel) throws SQLException {
      return this.getString(columnLabel);
   }

   public java.io.Reader getNCharacterStream(int columnIndex) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      return this.fieldLength.get() == -1 ? null : (java.io.Reader)this.rowDecoder.decode(ReaderCodec.INSTANCE, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
   }

   public java.io.Reader getNCharacterStream(String columnLabel) throws SQLException {
      return this.getNCharacterStream(this.findColumn(columnLabel));
   }

   public void updateNCharacterStream(int columnIndex, java.io.Reader x, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNCharacterStream(String columnLabel, java.io.Reader reader, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateCharacterStream(int columnIndex, java.io.Reader x, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateCharacterStream(String columnLabel, java.io.Reader reader, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateClob(int columnIndex, java.io.Reader reader, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateClob(String columnLabel, java.io.Reader reader, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNClob(int columnIndex, java.io.Reader reader, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNClob(String columnLabel, java.io.Reader reader, long length) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNCharacterStream(int columnIndex, java.io.Reader x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNCharacterStream(String columnLabel, java.io.Reader reader) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateCharacterStream(int columnIndex, java.io.Reader x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateCharacterStream(String columnLabel, java.io.Reader reader) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateClob(int columnIndex, java.io.Reader reader) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateClob(String columnLabel, java.io.Reader reader) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNClob(int columnIndex, java.io.Reader reader) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateNClob(String columnLabel, java.io.Reader reader) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
      this.checkIndex(columnIndex);
      this.fieldLength.set(this.rowDecoder.setPosition(columnIndex - 1, this.fieldIndex, this.maxIndex, this.rowBuf, this.nullBitmap, this.metadataList));
      if (this.wasNull()) {
         if (type.isPrimitive()) {
            throw new SQLException(String.format("Cannot return null for primitive %s", type.getName()));
         } else {
            return null;
         }
      } else {
         ColumnDecoder column = this.metadataList[columnIndex - 1];
         if (!Object.class.equals(type) && type != null) {
            Configuration conf = this.context.getConf();
            Codec[] var5 = conf.codecs();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Codec<?> codec = var5[var7];
               if (codec.canDecode(column, type)) {
                  return this.rowDecoder.decode(codec, (Calendar)null, this.rowBuf, this.fieldLength, this.metadataList, this.fieldIndex, this.context);
               }
            }

            this.rowBuf.skip(this.fieldLength.get());
            throw new SQLException(String.format("Type %s not supported type for %s type", type, column.getType().name()));
         } else {
            return this.rowDecoder.defaultDecode(this.metadataList, this.fieldIndex, this.rowBuf, this.fieldLength, this.context);
         }
      }
   }

   public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
      return this.getObject(this.findColumn(columnLabel), type);
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

   public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
      throw this.exceptionFactory.notSupported("Not supported when using CONCUR_READ_ONLY concurrency");
   }

   protected void setNullRowBuf() {
      this.rowBuf.buf((byte[])null, 0, 0);
   }

   public int findColumn(String label) throws SQLException {
      if (label == null) {
         throw new SQLException("null is not a valid label value");
      } else {
         if (this.mapper == null) {
            this.mapper = new HashMap((int)((double)this.maxIndex * 2.5D));

            for(int i = 0; i < this.maxIndex; ++i) {
               Column ci = this.metadataList[i];
               String columnAlias = ci.getColumnAlias();
               if (columnAlias != null) {
                  columnAlias = columnAlias.toLowerCase(Locale.ROOT);
                  this.mapper.putIfAbsent(columnAlias, i + 1);
                  String tableAlias = ci.getTableAlias();
                  String tableLabel = tableAlias != null ? tableAlias : ci.getTable();
                  this.mapper.putIfAbsent(tableLabel.toLowerCase(Locale.ROOT) + "." + columnAlias, i + 1);
               }
            }
         }

         Integer ind = (Integer)this.mapper.get(label.toLowerCase(Locale.ROOT));
         if (ind == null) {
            String keys = Arrays.toString(this.mapper.keySet().toArray(new String[0]));
            throw new SQLException(String.format("Unknown label '%s'. Possible value %s", label, keys));
         } else {
            return ind;
         }
      }
   }

   public int getFetchSize() throws SQLException {
      return this.fetchSize;
   }

   public void setFetchSize(int fetchSize) throws SQLException {
      if (fetchSize < 0) {
         throw this.exceptionFactory.create(String.format("invalid fetch size %s", fetchSize));
      } else {
         this.fetchSize = fetchSize;
      }
   }
}
