package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class Closeables {
   @VisibleForTesting
   static final Logger logger = Logger.getLogger(Closeables.class.getName());

   private Closeables() {
   }

   public static void close(@CheckForNull Closeable closeable, boolean swallowIOException) throws IOException {
      if (closeable != null) {
         try {
            closeable.close();
         } catch (IOException var3) {
            if (!swallowIOException) {
               throw var3;
            }

            logger.log(Level.WARNING, "IOException thrown while closing Closeable.", var3);
         }

      }
   }

   public static void closeQuietly(@CheckForNull InputStream inputStream) {
      try {
         close(inputStream, true);
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }
   }

   public static void closeQuietly(@CheckForNull Reader reader) {
      try {
         close(reader, true);
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }
   }
}
