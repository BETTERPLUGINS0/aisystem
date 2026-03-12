package fr.xephi.authme.libs.org.mariadb.jdbc.client.result;

import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.DataType;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class CompleteResult extends Result {
   protected static final int BEFORE_FIRST_POS = -1;

   public CompleteResult(Statement stmt, boolean binaryProtocol, long maxRows, ColumnDecoder[] metadataList, Reader reader, Context context, int resultSetType, boolean closeOnCompletion, boolean traceEnable) throws IOException, SQLException {
      super(stmt, binaryProtocol, maxRows, metadataList, reader, context, resultSetType, closeOnCompletion, traceEnable, false, 0);
      this.data = new byte[10][];
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

      for(int i = 0; i < columnNameLength; ++i) {
         columns[i] = ColumnDecoder.create(columnNames[i], columnTypes[i], flags);
      }

      List<byte[]> rows = new ArrayList();
      String[][] var9 = data;
      int var10 = data.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         String[] rowData = var9[var11];
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         String[] var14 = rowData;
         int var15 = rowData.length;

         for(int var16 = 0; var16 < var15; ++var16) {
            String rowDatum = var14[var16];
            if (rowDatum != null) {
               byte[] bb = rowDatum.getBytes();
               int len = bb.length;
               if (len < 251) {
                  baos.write((byte)len);
               } else {
                  baos.write(-4);
                  baos.write((byte)len);
                  baos.write((byte)(len >>> 8));
               }

               baos.write(bb, 0, bb.length);
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
      ColumnDecoder[] newMeta = new ColumnDecoder[this.metadataList.length];

      for(int i = 0; i < this.metadataList.length; ++i) {
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

   public void fetchRemaining() {
   }

   public void closeFromStmtClose(ReentrantLock lock) {
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
