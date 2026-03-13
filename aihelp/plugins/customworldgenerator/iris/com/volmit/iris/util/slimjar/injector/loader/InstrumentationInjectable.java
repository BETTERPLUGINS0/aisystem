package com.volmit.iris.util.slimjar.injector.loader;

import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import com.volmit.iris.util.slimjar.injector.agent.ByteBuddyInstrumentationFactory;
import com.volmit.iris.util.slimjar.injector.agent.InstrumentationFactory;
import com.volmit.iris.util.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import com.volmit.iris.util.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.jar.JarFile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class InstrumentationInjectable implements Injectable {
   @NotNull
   private final Instrumentation instrumentation;

   public InstrumentationInjectable(@NotNull Instrumentation var1) {
      this.instrumentation = var1;
   }

   public void inject(@NotNull URL var1) {
      try {
         this.instrumentation.appendToSystemClassLoaderSearch(new JarFile(new File(var1.toURI())));
      } catch (URISyntaxException | IOException var3) {
         throw new InjectorException("Failed to inject %s into classLoader.".formatted(new Object[]{var1}), var3);
      }
   }

   public boolean isThreadSafe() {
      return true;
   }

   @NotNull
   public ClassLoader getClassLoader() {
      return ClassLoader.getSystemClassLoader();
   }

   @NotNull
   public static Injectable create(@NotNull Path var0, @NotNull Collection<Repository> var1) {
      JarRelocatorFacadeFactory var2;
      try {
         var2 = ReflectiveJarRelocatorFacadeFactory.create(var0, var1);
      } catch (RelocatorException var4) {
         throw new InjectorException("Failed to create relocator facade factory.", var4);
      }

      return create(new ByteBuddyInstrumentationFactory(var2));
   }

   @Contract("_ -> new")
   @NotNull
   public static Injectable create(@NotNull InstrumentationFactory var0) {
      return new InstrumentationInjectable(var0.create());
   }
}
