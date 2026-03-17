package me.PM2.infinitevehicles.libby.classloader;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import me.PM2.infinitevehicles.libby.Library;
import me.PM2.infinitevehicles.libby.LibraryManager;
import sun.misc.Unsafe;

public class URLClassLoaderHelper {
   private static final Unsafe theUnsafe;
   private final URLClassLoader classLoader;
   private MethodHandle addURLMethodHandle = null;

   public URLClassLoaderHelper(URLClassLoader classLoader, LibraryManager libraryManager) {
      Objects.requireNonNull(var2, "libraryManager");
      this.classLoader = (URLClassLoader)Objects.requireNonNull(var1, "classLoader");

      try {
         Method var3 = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);

         try {
            openUrlClassLoaderModule();
         } catch (Exception var7) {
         }

         try {
            var3.setAccessible(true);
         } catch (Exception var9) {
            if (!var9.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException")) {
               throw new RuntimeException("Cannot set accessible URLClassLoader#addURL(URL)", var9);
            }

            if (theUnsafe != null) {
               try {
                  this.addURLMethodHandle = this.getPrivilegedMethodHandle(var3).bindTo(var1);
                  return;
               } catch (Exception var8) {
                  this.addURLMethodHandle = null;
               }
            }

            try {
               this.addOpensWithAgent(var2);
               var3.setAccessible(true);
            } catch (Exception var6) {
               System.err.println("Cannot access URLClassLoader#addURL(URL), if you are using Java 9+ try to add the following option to your java command: --add-opens java.base/java.net=ALL-UNNAMED");
               throw new RuntimeException("Cannot access URLClassLoader#addURL(URL)", var6);
            }
         }

         this.addURLMethodHandle = MethodHandles.lookup().unreflect(var3).bindTo(var1);
      } catch (IllegalAccessException | NoSuchMethodException var10) {
         throw new RuntimeException(var10);
      }
   }

   public void addToClasspath(URL url) {
      try {
         this.addURLMethodHandle.invokeWithArguments(Objects.requireNonNull(var1, "url"));
      } catch (Throwable var3) {
         throw new RuntimeException(var3);
      }
   }

   public void addToClasspath(Path path) {
      try {
         this.addToClasspath(((Path)Objects.requireNonNull(var1, "path")).toUri().toURL());
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   private static void openUrlClassLoaderModule() {
      Class var0 = Class.forName("java.lang.Module");
      Method var1 = Class.class.getMethod("getModule");
      Method var2 = var0.getMethod("addOpens", String.class, var0);
      Object var3 = var1.invoke(URLClassLoader.class);
      Object var4 = var1.invoke(URLClassLoaderHelper.class);
      var2.invoke(var3, URLClassLoader.class.getPackage().getName(), var4);
   }

   private MethodHandle getPrivilegedMethodHandle(Method method) {
      Field[] var2 = Lookup.class.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         if (var5.getType() == Lookup.class && Modifier.isStatic(var5.getModifiers()) && !var5.isSynthetic()) {
            try {
               Lookup var6 = (Lookup)theUnsafe.getObject(theUnsafe.staticFieldBase(var5), theUnsafe.staticFieldOffset(var5));
               return var6.unreflect(var1);
            } catch (Exception var7) {
            }
         }
      }

      throw new RuntimeException("Cannot get privileged method handle.");
   }

   private void addOpensWithAgent(LibraryManager libraryManager) {
      IsolatedClassLoader var2 = new IsolatedClassLoader(new URL[0]);

      try {
         var2.addPath(var1.downloadLibrary(Library.builder().groupId("net.bytebuddy").artifactId("byte-buddy-agent").version("1.12.1").checksum("mcCtBT9cljUEniB5ESpPDYZMfVxEs1JRPllOiWTP+bM=").repository("https://repo1.maven.org/maven2/").build()));
         Class var3 = var2.loadClass("net.bytebuddy.agent.ByteBuddyAgent");
         Object var4 = var3.getDeclaredMethod("install").invoke((Object)null);
         Class var5 = Class.forName("java.lang.instrument.Instrumentation");
         Method var6 = var5.getDeclaredMethod("redefineModule", Class.forName("java.lang.Module"), Set.class, Map.class, Map.class, Set.class, Map.class);
         Method var7 = Class.class.getDeclaredMethod("getModule");
         Map var8 = Collections.singletonMap("java.net", Collections.singleton(var7.invoke(this.getClass())));
         var6.invoke(var4, var7.invoke(URLClassLoader.class), Collections.emptySet(), Collections.emptyMap(), var8, Collections.emptySet(), Collections.emptyMap());
      } finally {
         try {
            var2.close();
         } catch (Exception var14) {
         }

      }

   }

   static {
      Unsafe var0 = null;
      Field[] var1 = Unsafe.class.getDeclaredFields();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field var4 = var1[var3];

         try {
            if (var4.getType() == Unsafe.class && Modifier.isStatic(var4.getModifiers())) {
               var4.setAccessible(true);
               var0 = (Unsafe)var4.get((Object)null);
            }
         } catch (Exception var6) {
         }
      }

      theUnsafe = var0;
   }
}
