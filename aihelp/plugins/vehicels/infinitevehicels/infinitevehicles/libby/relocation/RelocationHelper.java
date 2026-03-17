package me.PM2.infinitevehicles.libby.relocation;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import me.PM2.infinitevehicles.libby.Library;
import me.PM2.infinitevehicles.libby.LibraryManager;
import me.PM2.infinitevehicles.libby.classloader.IsolatedClassLoader;

public class RelocationHelper {
   private final Constructor<?> jarRelocatorConstructor;
   private final Method jarRelocatorRunMethod;
   private final Constructor<?> relocationConstructor;

   public RelocationHelper(LibraryManager libraryManager) {
      Objects.requireNonNull(var1, "libraryManager");
      IsolatedClassLoader var2 = new IsolatedClassLoader(new URL[0]);
      var2.addPath(var1.downloadLibrary(Library.builder().groupId("org.ow2.asm").artifactId("asm-commons").version("9.7").checksum("OJvCR5WOBJ/JoECNOYySxtNwwYA1EgOV1Muh2dkwS3o=").repository("https://repo1.maven.org/maven2/").build()));
      var2.addPath(var1.downloadLibrary(Library.builder().groupId("org.ow2.asm").artifactId("asm").version("9.7").checksum("rfRtXjSUC98Ujs3Sap7o7qlElqcgNP9xQQZrPupcTp0=").repository("https://repo1.maven.org/maven2/").build()));
      var2.addPath(var1.downloadLibrary(Library.builder().groupId("me.lucko").artifactId("jar-relocator").version("1.7").checksum("b30RhOF6kHiHl+O5suNLh/+eAr1iOFEFLXhwkHHDu4I=").repository("https://repo1.maven.org/maven2/").build()));

      try {
         Class var3 = var2.loadClass("me.lucko.jarrelocator.JarRelocator");
         Class var4 = var2.loadClass("me.lucko.jarrelocator.Relocation");
         this.jarRelocatorConstructor = var3.getConstructor(File.class, File.class, Collection.class);
         this.jarRelocatorRunMethod = var3.getMethod("run");
         this.relocationConstructor = var4.getConstructor(String.class, String.class, Collection.class, Collection.class);
      } catch (ReflectiveOperationException var5) {
         throw new RuntimeException(var5);
      }
   }

   public void relocate(Path in, Path out, Collection<Relocation> relocations) {
      Objects.requireNonNull(var1, "in");
      Objects.requireNonNull(var2, "out");
      Objects.requireNonNull(var3, "relocations");

      try {
         LinkedList var4 = new LinkedList();
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            Relocation var6 = (Relocation)var5.next();
            var4.add(this.relocationConstructor.newInstance(var6.getPattern(), var6.getRelocatedPattern(), var6.getIncludes(), var6.getExcludes()));
         }

         this.jarRelocatorRunMethod.invoke(this.jarRelocatorConstructor.newInstance(var1.toFile(), var2.toFile(), var4));
      } catch (ReflectiveOperationException var7) {
         throw new RuntimeException(var7);
      }
   }
}
