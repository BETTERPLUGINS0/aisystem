package fr.xephi.authme.libs.org.postgresql.util;

import fr.xephi.authme.libs.org.postgresql.jdbc.ResourceLock;
import java.io.Writer;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LogWriterHandler extends Handler {
   @Nullable
   private Writer writer;
   private final ResourceLock lock = new ResourceLock();

   public LogWriterHandler(Writer inWriter) {
      this.setLevel(Level.INFO);
      this.setFilter((Filter)null);
      this.setFormatter(new SimpleFormatter());
      this.setWriter(inWriter);
   }

   public void publish(LogRecord record) {
      Formatter formatter = this.getFormatter();

      String formatted;
      try {
         formatted = formatter.format(record);
      } catch (Exception var8) {
         this.reportError("Error Formatting record", var8, 5);
         return;
      }

      if (formatted.length() != 0) {
         try {
            ResourceLock ignore = this.lock.obtain();

            try {
               Writer writer = this.writer;
               if (writer != null) {
                  writer.write(formatted);
               }
            } catch (Throwable var9) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var7) {
                     var9.addSuppressed(var7);
                  }
               }

               throw var9;
            }

            if (ignore != null) {
               ignore.close();
            }
         } catch (Exception var10) {
            this.reportError("Error writing message", var10, 1);
         }

      }
   }

   public void flush() {
      try {
         ResourceLock ignore = this.lock.obtain();

         try {
            Writer writer = this.writer;
            if (writer != null) {
               writer.flush();
            }
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
      } catch (Exception var6) {
         this.reportError("Error on flush", var6, 1);
      }

   }

   public void close() throws SecurityException {
      try {
         ResourceLock ignore = this.lock.obtain();

         try {
            Writer writer = this.writer;
            if (writer != null) {
               writer.close();
            }
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
      } catch (Exception var6) {
         this.reportError("Error closing writer", var6, 1);
      }

   }

   private void setWriter(Writer writer) throws IllegalArgumentException {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (writer == null) {
            throw new IllegalArgumentException("Writer cannot be null");
         }

         this.writer = writer;

         try {
            writer.write(this.getFormatter().getHead(this));
         } catch (Exception var6) {
            this.reportError("Error writing head section", var6, 1);
         }
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var7.addSuppressed(var5);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }
}
