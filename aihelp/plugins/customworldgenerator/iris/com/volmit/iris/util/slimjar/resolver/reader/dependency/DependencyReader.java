package com.volmit.iris.util.slimjar.resolver.reader.dependency;

import com.volmit.iris.util.slimjar.exceptions.ResolutionException;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DependencyReader {
   DependencyReader DEFAULT = (inputStream) -> {
      try {
         DataInputStream in = new DataInputStream(inputStream);

         DependencyData var2;
         try {
            var2 = DependencyData.read(in);
         } catch (Throwable var5) {
            try {
               in.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         in.close();
         return var2;
      } catch (IOException var6) {
         throw new ResolutionException("Failed to read dependency file", var6);
      }
   };

   @NotNull
   DependencyData read(@NotNull InputStream var1) throws ResolutionException;
}
