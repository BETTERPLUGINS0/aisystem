package fr.xephi.authme.libs.com.alessiodp.libby.classloader;

import fr.xephi.authme.libs.com.alessiodp.libby.LibraryManager;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class URLClassLoaderHelper extends ClassLoaderHelper {
   private MethodHandle addURLMethodHandle = null;

   public URLClassLoaderHelper(@NotNull URLClassLoader classLoader, @NotNull LibraryManager libraryManager) {
      super(classLoader);
      Objects.requireNonNull(libraryManager, "libraryManager");

      try {
         Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
         this.setMethodAccessible(libraryManager, addURLMethod, "URLClassLoader#addURL(URL)", (methodHandle) -> {
            this.addURLMethodHandle = methodHandle;
         }, (instrumentation) -> {
            this.addOpensWithAgent(instrumentation);
            addURLMethod.setAccessible(true);
         });
         if (this.addURLMethodHandle == null) {
            this.addURLMethodHandle = MethodHandles.lookup().unreflect(addURLMethod).bindTo(classLoader);
         }

      } catch (Exception var4) {
         throw new RuntimeException("Couldn't initialize URLClassLoaderHelper", var4);
      }
   }

   public void addToClasspath(@NotNull URL url) {
      try {
         this.addURLMethodHandle.invokeWithArguments(Objects.requireNonNull(url, "url"));
      } catch (Throwable var3) {
         throw new RuntimeException(var3);
      }
   }

   private void addOpensWithAgent(@NotNull Instrumentation instrumentation) {
      try {
         Method redefineModule = Instrumentation.class.getMethod("redefineModule", Class.forName("java.lang.Module"), Set.class, Map.class, Map.class, Set.class, Map.class);
         Method getModule = Class.class.getMethod("getModule");
         Map<String, Set<?>> toOpen = Collections.singletonMap("java.net", Collections.singleton(getModule.invoke(this.getClass())));
         redefineModule.invoke(instrumentation, getModule.invoke(URLClassLoader.class), Collections.emptySet(), Collections.emptyMap(), toOpen, Collections.emptySet(), Collections.emptyMap());
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }
}
