package fr.xephi.authme.libs.org.postgresql.copy;

import fr.xephi.authme.libs.org.postgresql.PGConnection;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGCopyOutputStream extends OutputStream implements CopyIn {
   @Nullable
   private CopyIn op;
   private final byte[] copyBuffer;
   private final byte[] singleByteBuffer;
   private int at;

   public PGCopyOutputStream(PGConnection connection, String sql) throws SQLException {
      this(connection, sql, 65536);
   }

   public PGCopyOutputStream(PGConnection connection, String sql, int bufferSize) throws SQLException {
      this(connection.getCopyAPI().copyIn(sql), bufferSize);
   }

   public PGCopyOutputStream(CopyIn op) {
      this(op, 65536);
   }

   public PGCopyOutputStream(CopyIn op, int bufferSize) {
      this.singleByteBuffer = new byte[1];
      this.op = op;
      this.copyBuffer = new byte[bufferSize];
   }

   private CopyIn getOp() {
      return (CopyIn)Nullness.castNonNull(this.op);
   }

   public void write(int b) throws IOException {
      this.checkClosed();
      if (b >= 0 && b <= 255) {
         this.singleByteBuffer[0] = (byte)b;
         this.write(this.singleByteBuffer, 0, 1);
      } else {
         throw new IOException(GT.tr("Cannot write to copy a byte of value {0}", b));
      }
   }

   public void write(byte[] buf) throws IOException {
      this.write(buf, 0, buf.length);
   }

   public void write(byte[] buf, int off, int siz) throws IOException {
      this.checkClosed();

      try {
         this.writeToCopy(buf, off, siz);
      } catch (SQLException var5) {
         throw new IOException("Write to copy failed.", var5);
      }
   }

   private void checkClosed() throws IOException {
      if (this.op == null) {
         throw new IOException(GT.tr("This copy stream is closed."));
      }
   }

   public void close() throws IOException {
      CopyIn op = this.op;
      if (op != null) {
         if (op.isActive()) {
            try {
               this.endCopy();
            } catch (SQLException var3) {
               throw new IOException("Ending write to copy failed.", var3);
            }
         }

         this.op = null;
      }
   }

   public void flush() throws IOException {
      this.checkClosed();

      try {
         this.getOp().writeToCopy(this.copyBuffer, 0, this.at);
         this.at = 0;
         this.getOp().flushCopy();
      } catch (SQLException var2) {
         throw new IOException("Unable to flush stream", var2);
      }
   }

   public void writeToCopy(byte[] buf, int off, int siz) throws SQLException {
      if (this.at > 0 && siz > this.copyBuffer.length - this.at) {
         this.getOp().writeToCopy(this.copyBuffer, 0, this.at);
         this.at = 0;
      }

      if (siz > this.copyBuffer.length) {
         this.getOp().writeToCopy(buf, off, siz);
      } else {
         System.arraycopy(buf, off, this.copyBuffer, this.at, siz);
         this.at += siz;
      }

   }

   public void writeToCopy(ByteStreamWriter from) throws SQLException {
      if (this.at > 0) {
         this.getOp().writeToCopy(this.copyBuffer, 0, this.at);
         this.at = 0;
      }

      this.getOp().writeToCopy(from);
   }

   public int getFormat() {
      return this.getOp().getFormat();
   }

   public int getFieldFormat(int field) {
      return this.getOp().getFieldFormat(field);
   }

   public void cancelCopy() throws SQLException {
      this.getOp().cancelCopy();
   }

   public int getFieldCount() {
      return this.getOp().getFieldCount();
   }

   public boolean isActive() {
      return this.op != null && this.getOp().isActive();
   }

   public void flushCopy() throws SQLException {
      this.getOp().flushCopy();
   }

   public long endCopy() throws SQLException {
      if (this.at > 0) {
         this.getOp().writeToCopy(this.copyBuffer, 0, this.at);
      }

      this.getOp().endCopy();
      return this.getHandledRowCount();
   }

   public long getHandledRowCount() {
      return this.getOp().getHandledRowCount();
   }
}
