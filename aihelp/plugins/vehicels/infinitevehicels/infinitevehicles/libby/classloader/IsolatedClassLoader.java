package me.PM2.infinitevehicles.libby.classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;

public class IsolatedClassLoader extends URLClassLoader {
   public IsolatedClassLoader(URL... urls) {
      super((URL[])Objects.requireNonNull(var1, "urls"), ClassLoader.getSystemClassLoader().getParent());
   }

   public void addURL(URL url) {
      super.addURL(var1);
   }

   public void addPath(Path path) {
      try {
         this.addURL(((Path)Objects.requireNonNull(var1, "path")).toUri().toURL());
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   static {
      ClassLoader.registerAsParallelCapable();
   }
}
