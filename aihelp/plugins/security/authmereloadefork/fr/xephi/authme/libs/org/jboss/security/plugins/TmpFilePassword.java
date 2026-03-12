package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class TmpFilePassword {
   private File passwordFile;

   public TmpFilePassword(String file) {
      this.passwordFile = new File(file);
   }

   public char[] toCharArray() throws IOException {
      while(true) {
         if (!this.passwordFile.exists()) {
            try {
               Thread.sleep(10000L);
               continue;
            } catch (InterruptedException var27) {
            }
         }

         FileInputStream fis = null;
         CharArrayWriter writer = null;

         int b;
         try {
            fis = new FileInputStream(this.passwordFile);
            writer = new CharArrayWriter();

            while((b = fis.read()) >= 0) {
               if (b != 13 && b != 10) {
                  writer.write(b);
               }
            }
         } finally {
            this.safeClose((InputStream)fis);
         }

         try {
            fis = new FileInputStream(this.passwordFile);
            writer = new CharArrayWriter();

            while((b = fis.read()) >= 0) {
               if (b != 13 && b != 10) {
                  writer.write(b);
               }
            }
         } finally {
            this.safeClose((InputStream)fis);
         }

         char[] password = writer.toCharArray();
         writer.reset();

         for(int n = 0; n < password.length; ++n) {
            writer.write(0);
         }

         RandomAccessFile raf = null;

         try {
            raf = new RandomAccessFile(this.passwordFile, "rws");

            for(int i = 0; i < 10; ++i) {
               raf.seek(0L);

               for(int j = 0; j < password.length; ++j) {
                  raf.write(j);
               }
            }

            raf.close();
         } catch (Exception var23) {
            PicketBoxLogger.LOGGER.debugIgnoredException(var23);
         } finally {
            this.safeClose(raf);
         }

         return password;
      }
   }

   private void safeClose(InputStream is) {
      try {
         if (is != null) {
            is.close();
         }
      } catch (Exception var3) {
      }

   }

   private void safeClose(RandomAccessFile f) {
      try {
         if (f != null) {
            f.close();
         }
      } catch (Exception var3) {
      }

   }
}
