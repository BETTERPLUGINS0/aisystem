package github.nighter.smartspawner.libs.mariadb.client.result;

import github.nighter.smartspawner.libs.mariadb.Statement;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.DataType;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompleteResult extends Result {
   protected static final int BEFORE_FIRST_POS = -1;
   private boolean bulkResult;
   private boolean mightBeBulkResult;

   public CompleteResult(Statement stmt, boolean binaryProtocol, long maxRows, ColumnDecoder[] metadataList, Reader reader, Context context, int resultSetType, boolean closeOnCompletion, boolean traceEnable, boolean mightBeBulkResult) throws IOException, SQLException {
      super(stmt, binaryProtocol, maxRows, metadataList, reader, context, resultSetType, closeOnCompletion, traceEnable, false, 0);
      this.mightBeBulkResult = mightBeBulkResult;
      if (maxRows > 0L) {
         this.data = new byte[10][];

         do {
            this.readNext(reader.readPacket(traceEnable));
         } while(!this.loaded && (long)this.dataSize < maxRows);

         if (!this.loaded) {
            this.skipRemaining();
         }
      } else {
         byte[] buf = reader.readPacket(traceEnable);
         if (buf[0] == -1 || buf[0] == -2 && buf.length < 16777215) {
            this.data = new byte[0][];
            this.readNext(buf);
         } else {
            byte[] buf2 = reader.readPacket(traceEnable);
            if (buf2[0] == -1 || buf2[0] == -2 && buf2.length < 16777215) {
               this.data = new byte[1][];
               this.data[0] = buf;
               this.dataSize = 1;
               this.readNext(buf2);
            } else {
               this.data = new byte[10][];
               this.data[0] = buf;
               this.data[1] = buf2;
               this.dataSize = 2;

               do {
                  this.readNext(reader.readPacket(traceEnable));
               } while(!this.loaded);
            }
         }
      }

   }

   public void setBulkResult() {
      if (this.mightBeBulkResult) {
         this.bulkResult = true;
      }

   }

   private CompleteResult(ColumnDecoder[] metadataList, CompleteResult prev) {
      super(metadataList, prev);
   }

   public CompleteResult(ColumnDecoder[] metadataList, byte[][] data, Context context, int resultSetType) {
      super(metadataList, data, context, resultSetType);
   }

   public static ResultSet createResultSet(String columnName, DataType columnType, String[][] data, Context context, int flags, int resultSetType) {
      return createResultSet(new String[]{columnName}, new DataType[]{columnType}, data, context, flags, resultSetType);
   }

   public static ResultSet createResultSet(String[] columnNames, DataType[] columnTypes, String[][] data, Context context, int flags, int resultSetType) {
      int columnNameLength = columnNames.length;
      ColumnDecoder[] columns = new ColumnDecoder[columnNameLength];

      int estimatedRowSize;
      for(estimatedRowSize = 0; estimatedRowSize < columnNameLength; ++estimatedRowSize) {
         columns[estimatedRowSize] = ColumnDecoder.create(context.getDatabase(), columnNames[estimatedRowSize], columnTypes[estimatedRowSize], flags);
      }

      estimatedRowSize = columnNameLength * 20;
      List<byte[]> rows = new ArrayList(data.length);
      String[][] var10 = data;
      int var11 = data.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         String[] rowData = var10[var12];
         ByteArrayOutputStream baos = new ByteArrayOutputStream(estimatedRowSize);
         String[] var15 = rowData;
         int var16 = rowData.length;

         for(int var17 = 0; var17 < var16; ++var17) {
            String rowDatum = var15[var17];
            if (rowDatum != null) {
               byte[] bb = rowDatum.getBytes(StandardCharsets.UTF_8);
               int len = bb.length;
               if (len < 251) {
                  baos.write((byte)len);
               } else if (len < 65536) {
                  baos.write(-4);
                  baos.write((byte)len);
                  baos.write((byte)(len >>> 8));
               } else if (len < 16777215) {
                  baos.write(-3);
                  baos.write((byte)len);
                  baos.write((byte)(len >>> 8));
                  baos.write((byte)(len >>> 16));
               } else {
                  baos.write(-2);
                  baos.write((byte)len);
                  baos.write((byte)(len >>> 8));
                  baos.write((byte)(len >>> 16));
                  baos.write((byte)(len >>> 24));
                  baos.write((byte)((int)((long)len >>> 32)));
                  baos.write((byte)((int)((long)len >>> 40)));
                  baos.write((byte)((int)((long)len >>> 48)));
                  baos.write((byte)((int)((long)len >>> 56)));
               }

               baos.write(bb, 0, len);
            } else {
               baos.write(-5);
            }
         }

         byte[] bb = baos.toByteArray();
         rows.add(bb);
      }

      return new CompleteResult(columns, (byte[][])rows.toArray(new byte[0][0]), context, resultSetType);
   }

   public CompleteResult newResultsetWithUseAliasAsName() {
      int length = this.metadataList.length;
      ColumnDecoder[] newMeta = new ColumnDecoder[length];

      for(int i = 0; i < length; ++i) {
         newMeta[i] = this.metadataList[i].useAliasAsName();
      }

      return new CompleteResult(newMeta, this);
   }

   public boolean next() throws SQLException {
      if (this.rowPointer < this.dataSize - 1) {
         this.setRow(this.data[++this.rowPointer]);
         return true;
      } else {
         this.setNullRowBuf();
         this.rowPointer = this.dataSize;
         return false;
      }
   }

   public boolean streaming() {
      return false;
   }

   public boolean isBulkResult() {
      return this.bulkResult;
   }

   public void fetchRemaining() {
   }

   public void closeFromStmtClose(ClosableLock lock) {
      this.closed = true;
   }

   public boolean isAfterLast() throws SQLException {
      this.checkClose();
      if (this.rowPointer < this.dataSize) {
         return false;
      } else {
         return this.dataSize > 0;
      }
   }

   public boolean isFirst() throws SQLException {
      this.checkClose();
      return this.rowPointer == 0 && this.dataSize > 0;
   }

   public boolean isLast() throws SQLException {
      this.checkClose();
      return this.rowPointer == this.dataSize - 1 && this.dataSize > 0;
   }

   public void beforeFirst() throws SQLException {
      this.checkClose();
      this.rowPointer = -1;
      this.setNullRowBuf();
   }

   public void afterLast() throws SQLException {
      this.checkClose();
      this.setNullRowBuf();
      this.rowPointer = this.dataSize;
   }

   public boolean first() throws SQLException {
      this.checkClose();
      this.rowPointer = 0;
      if (this.dataSize == 0) {
         this.setNullRowBuf();
         return false;
      } else {
         this.setRow(this.data[this.rowPointer]);
         return true;
      }
   }

   public boolean last() throws SQLException {
      this.checkClose();
      this.rowPointer = this.dataSize - 1;
      if (this.rowPointer == -1) {
         this.setNullRowBuf();
         return false;
      } else {
         this.setRow(this.data[this.rowPointer]);
         return true;
      }
   }

   public int getRow() throws SQLException {
      this.checkClose();
      return this.rowPointer == this.dataSize ? 0 : this.rowPointer + 1;
   }

   public boolean absolute(int idx) throws SQLException {
      this.checkClose();
      if (idx != 0 && idx <= this.dataSize) {
         if (idx > 0) {
            this.rowPointer = idx - 1;
            this.setRow(this.data[this.rowPointer]);
            return true;
         } else if (this.dataSize + idx >= 0) {
            this.rowPointer = this.dataSize + idx;
            this.setRow(this.data[this.rowPointer]);
            return true;
         } else {
            this.rowPointer = -1;
            this.setNullRowBuf();
            return false;
         }
      } else {
         this.rowPointer = idx == 0 ? -1 : this.dataSize;
         this.setNullRowBuf();
         return false;
      }
   }

   public boolean relative(int rows) throws SQLException {
      this.checkClose();
      int newPos = this.rowPointer + rows;
      if (newPos <= -1) {
         this.rowPointer = -1;
         this.setNullRowBuf();
         return false;
      } else if (newPos >= this.dataSize) {
         this.rowPointer = this.dataSize;
         this.setNullRowBuf();
         return false;
      } else {
         this.rowPointer = newPos;
         this.setRow(this.data[this.rowPointer]);
         return true;
      }
   }

   public boolean previous() throws SQLException {
      this.checkClose();
      if (this.rowPointer > -1) {
         --this.rowPointer;
         if (this.rowPointer != -1) {
            this.setRow(this.data[this.rowPointer]);
            return true;
         }
      }

      this.setNullRowBuf();
      return false;
   }

   public int getFetchSize() throws SQLException {
      this.checkClose();
      return super.getFetchSize();
   }

   public void setFetchSize(int rows) throws SQLException {
      this.checkClose();
      super.setFetchSize(rows);
   }
}
