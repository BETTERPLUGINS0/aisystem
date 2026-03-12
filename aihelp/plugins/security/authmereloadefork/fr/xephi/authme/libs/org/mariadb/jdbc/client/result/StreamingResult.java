package fr.xephi.authme.libs.org.mariadb.jdbc.client.result;

import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class StreamingResult extends Result {
   private static final int MAX_FETCH_SIZE = 16384;
   private final ReentrantLock lock;
   private int dataFetchTime;
   private int requestedFetchSize;

   public StreamingResult(Statement stmt, boolean binaryProtocol, long maxRows, ColumnDecoder[] metadataList, Reader reader, Context context, int fetchSize, ReentrantLock lock, int resultSetType, boolean closeOnCompletion, boolean traceEnable) throws SQLException {
      super(stmt, binaryProtocol, maxRows, metadataList, reader, context, resultSetType, closeOnCompletion, traceEnable, false, fetchSize);
      this.lock = lock;
      this.dataFetchTime = 0;
      this.requestedFetchSize = fetchSize;
      this.data = new byte[Math.min(16384, Math.max(fetchSize, 10))][];
      this.addStreamingValue();
   }

   public boolean streaming() {
      return true;
   }

   private void nextStreamingValue() throws SQLException {
      if (this.resultSetType == 1003) {
         this.rowPointer = 0;
         this.dataSize = 0;
      }

      this.addStreamingValue();
   }

   private void addStreamingValue() throws SQLException {
      this.lock.lock();

      try {
         int fetchSizeTmp = this.maxRows <= 0L ? super.getFetchSize() : Math.min(super.getFetchSize(), Math.max(0, (int)(this.maxRows - (long)(this.dataFetchTime * super.getFetchSize()))));

         do {
            byte[] buf = this.reader.readPacket(this.traceEnable);
            this.readNext(buf);
            --fetchSizeTmp;
         } while(fetchSizeTmp > 0 && !this.loaded);

         ++this.dataFetchTime;
         if (this.maxRows > 0L && (long)this.dataFetchTime * (long)super.getFetchSize() >= this.maxRows && !this.loaded) {
            this.skipRemaining();
         }
      } catch (IOException var6) {
         throw this.exceptionFactory.create("Error while streaming resultSet data", "08000", var6);
      } finally {
         this.lock.unlock();
      }

   }

   public void fetchRemaining() throws SQLException {
      if (!this.loaded) {
         while(true) {
            if (this.loaded) {
               ++this.dataFetchTime;
               break;
            }

            this.addStreamingValue();
         }
      }

   }

   public boolean next() throws SQLException {
      this.checkClose();
      if (this.rowPointer < this.dataSize - 1) {
         ++this.rowPointer;
         this.setRow(this.data[this.rowPointer]);
         return true;
      } else if (!this.loaded) {
         this.lock.lock();

         try {
            if (!this.loaded) {
               this.nextStreamingValue();
            }
         } finally {
            this.lock.unlock();
         }

         if (this.resultSetType == 1003) {
            this.rowPointer = 0;
            if (this.dataSize > 0) {
               this.setRow(this.data[this.rowPointer]);
               return true;
            }
         } else {
            ++this.rowPointer;
            if (this.dataSize > this.rowPointer) {
               this.setRow(this.data[this.rowPointer]);
               return true;
            }
         }

         this.setNullRowBuf();
         return false;
      } else {
         this.rowPointer = this.dataSize;
         this.setNullRowBuf();
         return false;
      }
   }

   public boolean isAfterLast() throws SQLException {
      this.checkClose();
      if (this.rowPointer < this.dataSize) {
         return false;
      } else {
         return this.dataSize > 0 || this.dataFetchTime > 1;
      }
   }

   public boolean isFirst() throws SQLException {
      this.checkClose();
      if (this.resultSetType == 1003) {
         return this.rowPointer == 0 && this.dataSize > 0 && this.dataFetchTime == 1;
      } else {
         return this.rowPointer == 0 && this.dataSize > 0;
      }
   }

   public boolean isLast() throws SQLException {
      this.checkClose();
      if (this.rowPointer < this.dataSize - 1) {
         return false;
      } else if (!this.loaded) {
         this.addStreamingValue();
         if (this.loaded) {
            return this.rowPointer == this.dataSize - 1;
         } else {
            return false;
         }
      } else {
         return this.rowPointer == this.dataSize - 1 && this.dataSize > 0;
      }
   }

   public void beforeFirst() throws SQLException {
      this.checkClose();
      this.checkNotForwardOnly();
      this.setNullRowBuf();
      this.rowPointer = -1;
   }

   public void afterLast() throws SQLException {
      this.checkClose();
      this.checkNotForwardOnly();
      this.fetchRemaining();
      this.setNullRowBuf();
      this.rowPointer = this.dataSize;
   }

   public boolean first() throws SQLException {
      this.checkClose();
      this.checkNotForwardOnly();
      this.rowPointer = 0;
      if (this.dataSize > 0) {
         this.setRow(this.data[this.rowPointer]);
         return true;
      } else {
         this.setNullRowBuf();
         return false;
      }
   }

   public boolean last() throws SQLException {
      this.checkClose();
      this.fetchRemaining();
      this.rowPointer = this.dataSize - 1;
      if (this.dataSize > 0) {
         this.setRow(this.data[this.rowPointer]);
         return true;
      } else {
         this.setNullRowBuf();
         return false;
      }
   }

   public int getRow() throws SQLException {
      this.checkClose();
      return this.resultSetType == 1003 ? 0 : this.rowPointer + 1;
   }

   public boolean absolute(int idx) throws SQLException {
      this.checkClose();
      this.checkNotForwardOnly();
      if (idx == 0) {
         this.rowPointer = -1;
         this.setNullRowBuf();
         return false;
      } else if (idx > 0 && idx <= this.dataSize) {
         this.rowPointer = idx - 1;
         this.setRow(this.data[this.rowPointer]);
         return true;
      } else {
         this.fetchRemaining();
         if (idx > 0) {
            if (idx <= this.dataSize) {
               this.rowPointer = idx - 1;
               this.setRow(this.data[this.rowPointer]);
               return true;
            }

            this.rowPointer = this.dataSize;
            this.setNullRowBuf();
         } else {
            if (this.dataSize + idx >= 0) {
               this.rowPointer = this.dataSize + idx;
               this.setRow(this.data[this.rowPointer]);
               return true;
            }

            this.setNullRowBuf();
            this.rowPointer = -1;
         }

         return false;
      }
   }

   public boolean relative(int rows) throws SQLException {
      this.checkClose();
      int newPos = this.rowPointer + rows;
      if (newPos <= -1) {
         this.checkNotForwardOnly();
         this.rowPointer = -1;
         this.setNullRowBuf();
         return false;
      } else {
         while(newPos >= this.dataSize) {
            if (this.loaded) {
               this.rowPointer = this.dataSize;
               this.setNullRowBuf();
               return false;
            }

            this.addStreamingValue();
         }

         this.rowPointer = newPos;
         this.setRow(this.data[this.rowPointer]);
         return true;
      }
   }

   public boolean previous() throws SQLException {
      this.checkClose();
      this.checkNotForwardOnly();
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
      return this.requestedFetchSize;
   }

   public void setFetchSize(int fetchSize) throws SQLException {
      super.setFetchSize(Math.min(16384, fetchSize));
      this.requestedFetchSize = fetchSize;
      this.checkClose();
      if (fetchSize == 0) {
         while(!this.loaded) {
            this.addStreamingValue();
         }
      }

   }
}
