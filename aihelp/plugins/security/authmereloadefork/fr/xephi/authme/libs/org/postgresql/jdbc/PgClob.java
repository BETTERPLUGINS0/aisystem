package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.largeobject.LargeObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.sql.SQLException;

public class PgClob extends AbstractBlobClob implements Clob {
   public PgClob(BaseConnection conn, long oid) throws SQLException {
      super(conn, oid);
   }

   public Reader getCharacterStream(long pos, long length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         throw Driver.notImplemented(this.getClass(), "getCharacterStream(long, long)");
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
   }

   public int setString(long pos, String str) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         throw Driver.notImplemented(this.getClass(), "setString(long,str)");
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }
   }

   public int setString(long pos, String str, int offset, int len) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         throw Driver.notImplemented(this.getClass(), "setString(long,String,int,int)");
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
   }

   public OutputStream setAsciiStream(long pos) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         throw Driver.notImplemented(this.getClass(), "setAsciiStream(long)");
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }
   }

   public Writer setCharacterStream(long pos) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         throw Driver.notImplemented(this.getClass(), "setCharacterStream(long)");
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }
   }

   public InputStream getAsciiStream() throws SQLException {
      return this.getBinaryStream();
   }

   public Reader getCharacterStream() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      InputStreamReader var3;
      try {
         Charset connectionCharset = Charset.forName(this.conn.getEncoding().name());
         var3 = new InputStreamReader(this.getBinaryStream(), connectionCharset);
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   public String getSubString(long i, int j) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      String var6;
      try {
         this.assertPosition(i, (long)j);
         LargeObject lo = this.getLo(false);
         lo.seek((int)i - 1);
         var6 = new String(lo.read(j));
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var6;
   }

   public long position(String pattern, long start) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         throw Driver.notImplemented(this.getClass(), "position(String,long)");
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }
   }

   public long position(Clob pattern, long start) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         throw Driver.notImplemented(this.getClass(), "position(Clob,start)");
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }
   }
}
