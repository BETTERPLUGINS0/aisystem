package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.largeobject.LargeObject;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class PgBlob extends AbstractBlobClob implements Blob {
   public PgBlob(BaseConnection conn, long oid) throws SQLException {
      super(conn, oid);
   }

   public InputStream getBinaryStream(long pos, long length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      InputStream var7;
      try {
         this.checkFreed();
         LargeObject subLO = this.getLo(false).copy();
         this.addSubLO(subLO);
         if (pos > 2147483647L) {
            subLO.seek64(pos - 1L, 0);
         } else {
            subLO.seek((int)pos - 1, 0);
         }

         var7 = subLO.getInputStream(length);
      } catch (Throwable var9) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var7;
   }

   public int setBytes(long pos, byte[] bytes) throws SQLException {
      return this.setBytes(pos, bytes, 0, bytes.length);
   }

   public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      int var7;
      try {
         this.assertPosition(pos);
         this.getLo(true).seek((int)(pos - 1L));
         this.getLo(true).write(bytes, offset, len);
         var7 = len;
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

      return var7;
   }
}
