package fr.xephi.authme.libs.org.postgresql.copy;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.Encoding;
import fr.xephi.authme.libs.org.postgresql.core.QueryExecutor;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;

public class CopyManager {
   static final int DEFAULT_BUFFER_SIZE = 65536;
   private final Encoding encoding;
   private final QueryExecutor queryExecutor;
   private final BaseConnection connection;

   public CopyManager(BaseConnection connection) throws SQLException {
      this.encoding = connection.getEncoding();
      this.queryExecutor = connection.getQueryExecutor();
      this.connection = connection;
   }

   public CopyIn copyIn(String sql) throws SQLException {
      CopyOperation op = this.queryExecutor.startCopy(sql, this.connection.getAutoCommit());
      if (op != null && !(op instanceof CopyIn)) {
         op.cancelCopy();
         throw new PSQLException(GT.tr("Requested CopyIn but got {0}", op.getClass().getName()), PSQLState.WRONG_OBJECT_TYPE);
      } else {
         return (CopyIn)op;
      }
   }

   public CopyOut copyOut(String sql) throws SQLException {
      CopyOperation op = this.queryExecutor.startCopy(sql, this.connection.getAutoCommit());
      if (op != null && !(op instanceof CopyOut)) {
         op.cancelCopy();
         throw new PSQLException(GT.tr("Requested CopyOut but got {0}", op.getClass().getName()), PSQLState.WRONG_OBJECT_TYPE);
      } else {
         return (CopyOut)op;
      }
   }

   public CopyDual copyDual(String sql) throws SQLException {
      CopyOperation op = this.queryExecutor.startCopy(sql, this.connection.getAutoCommit());
      if (op != null && !(op instanceof CopyDual)) {
         op.cancelCopy();
         throw new PSQLException(GT.tr("Requested CopyDual but got {0}", op.getClass().getName()), PSQLState.WRONG_OBJECT_TYPE);
      } else {
         return (CopyDual)op;
      }
   }

   public long copyOut(String sql, Writer to) throws SQLException, IOException {
      CopyOut cp = this.copyOut(sql);

      long var5;
      try {
         byte[] buf;
         while((buf = cp.readFromCopy()) != null) {
            to.write(this.encoding.decode(buf));
         }

         var5 = cp.getHandledRowCount();
      } catch (IOException var12) {
         if (cp.isActive()) {
            cp.cancelCopy();
         }

         try {
            while(true) {
               if (cp.readFromCopy() != null) {
                  continue;
               }
            }
         } catch (SQLException var11) {
         }

         throw var12;
      } finally {
         if (cp.isActive()) {
            cp.cancelCopy();
         }

      }

      return var5;
   }

   public long copyOut(String sql, OutputStream to) throws SQLException, IOException {
      CopyOut cp = this.copyOut(sql);

      long var5;
      try {
         byte[] buf;
         while((buf = cp.readFromCopy()) != null) {
            to.write(buf);
         }

         var5 = cp.getHandledRowCount();
      } catch (IOException var12) {
         if (cp.isActive()) {
            cp.cancelCopy();
         }

         try {
            while(true) {
               if (cp.readFromCopy() != null) {
                  continue;
               }
            }
         } catch (SQLException var11) {
         }

         throw var12;
      } finally {
         if (cp.isActive()) {
            cp.cancelCopy();
         }

      }

      return var5;
   }

   public long copyIn(String sql, Reader from) throws SQLException, IOException {
      return this.copyIn(sql, from, 65536);
   }

   public long copyIn(String sql, Reader from, int bufferSize) throws SQLException, IOException {
      char[] cbuf = new char[bufferSize];
      CopyIn cp = this.copyIn(sql);

      long var12;
      try {
         int len;
         while((len = from.read(cbuf)) >= 0) {
            if (len > 0) {
               byte[] buf = this.encoding.encode(new String(cbuf, 0, len));
               cp.writeToCopy(buf, 0, buf.length);
            }
         }

         var12 = cp.endCopy();
      } finally {
         if (cp.isActive()) {
            cp.cancelCopy();
         }

      }

      return var12;
   }

   public long copyIn(String sql, InputStream from) throws SQLException, IOException {
      return this.copyIn(sql, from, 65536);
   }

   public long copyIn(String sql, InputStream from, int bufferSize) throws SQLException, IOException {
      byte[] buf = new byte[bufferSize];
      CopyIn cp = this.copyIn(sql);

      long var7;
      try {
         int len;
         while((len = from.read(buf)) >= 0) {
            if (len > 0) {
               cp.writeToCopy(buf, 0, len);
            }
         }

         var7 = cp.endCopy();
      } finally {
         if (cp.isActive()) {
            cp.cancelCopy();
         }

      }

      return var7;
   }

   public long copyIn(String sql, ByteStreamWriter from) throws SQLException, IOException {
      CopyIn cp = this.copyIn(sql);

      long var4;
      try {
         cp.writeToCopy(from);
         var4 = cp.endCopy();
      } finally {
         if (cp.isActive()) {
            cp.cancelCopy();
         }

      }

      return var4;
   }
}
