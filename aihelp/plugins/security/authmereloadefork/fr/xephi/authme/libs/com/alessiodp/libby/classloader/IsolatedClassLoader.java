package fr.xephi.authme.libs.com.alessiodp.libby.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class IsolatedClassLoader extends URLClassLoader {
   public IsolatedClassLoader(@NotNull URL... urls) {
      super((URL[])Objects.requireNonNull(urls, "urls"), ClassLoader.getSystemClassLoader().getParent());
   }

   public void addURL(@NotNull URL url) {
      super.addURL(url);
   }

   public void addPath(@NotNull Path path) {
      try {
         this.addURL(((Path)Objects.requireNonNull(path, "path")).toUri().toURL());
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   public Class<?> defineClass(@NotNull String name, @NotNull InputStream classBytes) throws IOException, ClassFormatError {
      byte[] bytes = readAllBytes(classBytes);
      return super.defineClass(name, bytes, 0, bytes.length);
   }

   private static byte[] readAllBytes(@NotNull InputStream inputStream) throws IOException {
      int bufLen = true;
      byte[] buf = new byte[4096];
      IOException exception = null;

      byte[] var6;
      try {
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

         try {
            int readLen;
            while((readLen = inputStream.read(buf, 0, 4096)) != -1) {
               outputStream.write(buf, 0, readLen);
            }

            var6 = outputStream.toByteArray();
         } catch (Throwable var18) {
            try {
               outputStream.close();
            } catch (Throwable var17) {
               var18.addSuppressed(var17);
            }

            throw var18;
         }

         outputStream.close();
      } catch (IOException var19) {
         exception = var19;
         throw var19;
      } finally {
         if (exception == null) {
            inputStream.close();
         } else {
            try {
               inputStream.close();
            } catch (IOException var16) {
               exception.addSuppressed(var16);
            }
         }

      }

      return var6;
   }

   static {
      ClassLoader.registerAsParallelCapable();
   }
}
