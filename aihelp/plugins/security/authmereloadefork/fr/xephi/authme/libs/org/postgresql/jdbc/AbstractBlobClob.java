package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.ServerVersion;
import fr.xephi.authme.libs.org.postgresql.largeobject.LargeObject;
import fr.xephi.authme.libs.org.postgresql.largeobject.LargeObjectManager;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractBlobClob {
   protected BaseConnection conn;
   @Nullable
   private LargeObject currentLo;
   private boolean currentLoIsWriteable;
   private final boolean support64bit;
   @Nullable
   private ArrayList<LargeObject> subLOs = new ArrayList();
   protected final ResourceLock lock = new ResourceLock();
   private final long oid;

   public AbstractBlobClob(BaseConnection conn, long oid) throws SQLException {
      this.conn = conn;
      this.oid = oid;
      this.currentLoIsWriteable = false;
      this.support64bit = conn.haveMinimumServerVersion(90300);
   }

   public void free() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (this.currentLo != null) {
            this.currentLo.close();
            this.currentLo = null;
            this.currentLoIsWriteable = false;
         }

         if (this.subLOs != null) {
            Iterator var2 = this.subLOs.iterator();

            while(var2.hasNext()) {
               LargeObject subLO = (LargeObject)var2.next();
               subLO.close();
            }
         }

         this.subLOs = null;
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

   }

   public void truncate(long len) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkFreed();
         if (!this.conn.haveMinimumServerVersion(ServerVersion.v8_3)) {
            throw new PSQLException(GT.tr("Truncation of large objects is only implemented in 8.3 and later servers."), PSQLState.NOT_IMPLEMENTED);
         }

         if (len < 0L) {
            throw new PSQLException(GT.tr("Cannot truncate LOB to a negative length."), PSQLState.INVALID_PARAMETER_VALUE);
         }

         if (len > 2147483647L) {
            if (!this.support64bit) {
               throw new PSQLException(GT.tr("PostgreSQL LOBs can only index to: {0}", Integer.MAX_VALUE), PSQLState.INVALID_PARAMETER_VALUE);
            }

            this.getLo(true).truncate64(len);
         } else {
            this.getLo(true).truncate((int)len);
         }
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

      if (ignore != null) {
         ignore.close();
      }

   }

   public long length() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      long var2;
      label43: {
         try {
            this.checkFreed();
            if (this.support64bit) {
               var2 = this.getLo(false).size64();
               break label43;
            }

            var2 = (long)this.getLo(false).size();
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

         return var2;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   public byte[] getBytes(long pos, int length) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      byte[] var5;
      try {
         this.assertPosition(pos);
         this.getLo(false).seek((int)(pos - 1L), 0);
         var5 = this.getLo(false).read(length);
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

      return var5;
   }

   public InputStream getBinaryStream() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      InputStream var3;
      try {
         this.checkFreed();
         LargeObject subLO = this.getLo(false).copy();
         this.addSubLO(subLO);
         subLO.seek(0, 0);
         var3 = subLO.getInputStream();
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

   public OutputStream setBinaryStream(long pos) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      OutputStream var5;
      try {
         this.assertPosition(pos);
         LargeObject subLO = this.getLo(true).copy();
         this.addSubLO(subLO);
         subLO.seek((int)(pos - 1L));
         var5 = subLO.getOutputStream();
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

      if (ignore != null) {
         ignore.close();
      }

      return var5;
   }

   public long position(byte[] pattern, long start) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      long var14;
      try {
         this.assertPosition(start, (long)pattern.length);
         int position = 1;
         int patternIdx = 0;
         long result = -1L;
         int tmpPosition = 1;
         AbstractBlobClob.LOIterator i = new AbstractBlobClob.LOIterator(start - 1L);

         while(true) {
            label42: {
               if (i.hasNext()) {
                  byte b = i.next();
                  if (b != pattern[patternIdx]) {
                     patternIdx = 0;
                     break label42;
                  }

                  if (patternIdx == 0) {
                     tmpPosition = position;
                  }

                  ++patternIdx;
                  if (patternIdx != pattern.length) {
                     break label42;
                  }

                  result = (long)tmpPosition;
               }

               var14 = result;
               break;
            }

            ++position;
         }
      } catch (Throwable var13) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var12) {
               var13.addSuppressed(var12);
            }
         }

         throw var13;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var14;
   }

   public long position(Blob pattern, long start) throws SQLException {
      return this.position(pattern.getBytes(1L, (int)pattern.length()), start);
   }

   protected void assertPosition(long pos) throws SQLException {
      this.assertPosition(pos, 0L);
   }

   protected void assertPosition(long pos, long len) throws SQLException {
      this.checkFreed();
      if (pos < 1L) {
         throw new PSQLException(GT.tr("LOB positioning offsets start at 1."), PSQLState.INVALID_PARAMETER_VALUE);
      } else if (pos + len - 1L > 2147483647L) {
         throw new PSQLException(GT.tr("PostgreSQL LOBs can only index to: {0}", Integer.MAX_VALUE), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   protected void checkFreed() throws SQLException {
      if (this.subLOs == null) {
         throw new PSQLException(GT.tr("free() was called on this LOB previously"), PSQLState.OBJECT_NOT_IN_STATE);
      }
   }

   protected LargeObject getLo(boolean forWrite) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      LargeObject var10;
      label56: {
         LargeObject var5;
         try {
            LargeObject currentLo = this.currentLo;
            if (currentLo != null) {
               if (forWrite && !this.currentLoIsWriteable) {
                  int currentPos = currentLo.tell();
                  LargeObjectManager lom = this.conn.getLargeObjectAPI();
                  LargeObject newLo = lom.open(this.oid, 393216);
                  ((ArrayList)Nullness.castNonNull(this.subLOs)).add(currentLo);
                  currentLo = newLo;
                  this.currentLo = newLo;
                  if (currentPos != 0) {
                     newLo.seek(currentPos);
                  }
               }

               var10 = currentLo;
               break label56;
            }

            LargeObjectManager lom = this.conn.getLargeObjectAPI();
            this.currentLo = currentLo = lom.open(this.oid, forWrite ? 393216 : 262144);
            this.currentLoIsWriteable = forWrite;
            var5 = currentLo;
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

         return var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var10;
   }

   protected void addSubLO(LargeObject subLO) {
      ((ArrayList)Nullness.castNonNull(this.subLOs)).add(subLO);
   }

   private class LOIterator {
      private static final int BUFFER_SIZE = 8096;
      private final byte[] buffer = new byte[8096];
      private int idx = 8096;
      private int numBytes = 8096;

      LOIterator(long start) throws SQLException {
         AbstractBlobClob.this.getLo(false).seek((int)start);
      }

      public boolean hasNext() throws SQLException {
         boolean result;
         if (this.idx < this.numBytes) {
            result = true;
         } else {
            this.numBytes = AbstractBlobClob.this.getLo(false).read(this.buffer, 0, 8096);
            this.idx = 0;
            result = this.numBytes > 0;
         }

         return result;
      }

      private byte next() {
         return this.buffer[this.idx++];
      }
   }
}
