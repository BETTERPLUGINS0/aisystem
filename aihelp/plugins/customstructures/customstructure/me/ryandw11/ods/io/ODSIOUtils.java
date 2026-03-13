package me.ryandw11.ods.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ODSIOUtils {
   public static byte[] toByteArray(InputStream input) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      Throwable var2 = null;

      byte[] var3;
      try {
         copy(input, output, new byte[8192]);
         var3 = output.toByteArray();
      } catch (Throwable var12) {
         var2 = var12;
         throw var12;
      } finally {
         if (output != null) {
            if (var2 != null) {
               try {
                  output.close();
               } catch (Throwable var11) {
                  var2.addSuppressed(var11);
               }
            } else {
               output.close();
            }
         }

      }

      return var3;
   }

   private static void copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
      int n;
      while(-1 != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
      }

   }
}
