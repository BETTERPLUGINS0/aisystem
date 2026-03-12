package fr.xephi.authme.libs.org.postgresql.copy;

import fr.xephi.authme.libs.org.postgresql.PGConnection;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGCopyInputStream extends InputStream implements CopyOut {
   @Nullable
   private CopyOut op;
   @Nullable
   private byte[] buf;
   private int at;
   private int len;

   public PGCopyInputStream(PGConnection connection, String sql) throws SQLException {
      this(connection.getCopyAPI().copyOut(sql));
   }

   public PGCopyInputStream(CopyOut op) {
      this.op = op;
   }

   private CopyOut getOp() {
      return (CopyOut)Nullness.castNonNull(this.op);
   }

   @Nullable
   private byte[] fillBuffer() throws IOException {
      if (this.at >= this.len) {
         try {
            this.buf = this.getOp().readFromCopy();
         } catch (SQLException var2) {
            throw new IOException(GT.tr("Copying from database failed: {0}", var2.getMessage()), var2);
         }

         if (this.buf == null) {
            this.at = -1;
         } else {
            this.at = 0;
            this.len = this.buf.length;
         }
      }

      return this.buf;
   }

   private void checkClosed() throws IOException {
      if (this.op == null) {
         throw new IOException(GT.tr("This copy stream is closed."));
      }
   }

   public int available() throws IOException {
      this.checkClosed();
      return this.buf != null ? this.len - this.at : 0;
   }

   public int read() throws IOException {
      this.checkClosed();
      byte[] buf = this.fillBuffer();
      return buf != null ? buf[this.at++] & 255 : -1;
   }

   public int read(byte[] buf) throws IOException {
      return this.read(buf, 0, buf.length);
   }

   public int read(byte[] buf, int off, int siz) throws IOException {
      this.checkClosed();
      int got = 0;

      byte[] data;
      for(data = this.fillBuffer(); got < siz && data != null; data = this.fillBuffer()) {
         int length = Math.min(siz - got, this.len - this.at);
         System.arraycopy(data, this.at, buf, off + got, length);
         this.at += length;
         got += length;
      }

      return got == 0 && data == null ? -1 : got;
   }

   @Nullable
   public byte[] readFromCopy() throws SQLException {
      byte[] result = null;

      try {
         byte[] buf = this.fillBuffer();
         if (buf != null) {
            if (this.at <= 0 && this.len >= buf.length) {
               result = buf;
            } else {
               result = Arrays.copyOfRange(buf, this.at, this.len);
            }

            this.at = this.len;
         }

         return result;
      } catch (IOException var3) {
         throw new PSQLException(GT.tr("Read from copy failed."), PSQLState.CONNECTION_FAILURE, var3);
      }
   }

   @Nullable
   public byte[] readFromCopy(boolean block) throws SQLException {
      return this.readFromCopy();
   }

   public void close() throws IOException {
      CopyOut op = this.op;
      if (op != null) {
         if (op.isActive()) {
            try {
               op.cancelCopy();
            } catch (SQLException var3) {
               throw new IOException("Failed to close copy reader.", var3);
            }
         }

         this.op = null;
      }
   }

   public void cancelCopy() throws SQLException {
      this.getOp().cancelCopy();
   }

   public int getFormat() {
      return this.getOp().getFormat();
   }

   public int getFieldFormat(int field) {
      return this.getOp().getFieldFormat(field);
   }

   public int getFieldCount() {
      return this.getOp().getFieldCount();
   }

   public boolean isActive() {
      return this.op != null && this.op.isActive();
   }

   public long getHandledRowCount() {
      return this.getOp().getHandledRowCount();
   }
}
