package com.volmit.iris.util.slimjar.relocation.facade;

import com.volmit.iris.util.slimjar.app.builder.ApplicationBuilder;
import com.volmit.iris.util.slimjar.app.builder.InjectingApplicationBuilder;
import com.volmit.iris.util.slimjar.exceptions.RelocatorException;
import com.volmit.iris.util.slimjar.injector.loader.InjectableClassLoader;
import com.volmit.iris.util.slimjar.injector.loader.IsolatedInjectableClassLoader;
import com.volmit.iris.util.slimjar.relocation.PassthroughRelocator;
import com.volmit.iris.util.slimjar.relocation.RelocationRule;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.util.Packages;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReflectiveJarRelocatorFacadeFactory implements JarRelocatorFacadeFactory {
   @NotNull
   private static final String JAR_RELOCATOR_PACKAGE = "me#lucko#jarrelocator#JarRelocator";
   @NotNull
   private static final String RELOCATION_PACKAGE = "me#lucko#jarrelocator#Relocation";
   @NotNull
   private final Constructor<?> jarRelocatorConstructor;
   @NotNull
   private final Constructor<?> relocationConstructor;
   @NotNull
   private final Method jarRelocatorRunMethod;

   private ReflectiveJarRelocatorFacadeFactory(@NotNull Constructor<?> var1, @NotNull Constructor<?> var2, @NotNull Method var3) {
      this.jarRelocatorConstructor = var1;
      this.relocationConstructor = var2;
      this.jarRelocatorRunMethod = var3;
   }

   @NotNull
   public JarRelocatorFacade createFacade(@NotNull File var1, @NotNull File var2, @NotNull Collection<RelocationRule> var3) {
      Object var4;
      try {
         Set var5 = (Set)var3.stream().map((var1x) -> {
            return createRelocation(this.relocationConstructor, var1x);
         }).filter(Objects::nonNull).collect(Collectors.toSet());
         var4 = createRelocator(this.jarRelocatorConstructor, var1, var2, var5);
      } catch (InstantiationException | InvocationTargetException | IllegalAccessException var6) {
         throw new RelocatorException("Failed to create JarRelocator", var6);
      }

      return new ReflectiveJarRelocatorFacade(var4, this.jarRelocatorRunMethod);
   }

   @Nullable
   private static Object createRelocation(@NotNull Constructor<?> var0, @NotNull RelocationRule var1) {
      try {
         return var0.newInstance(var1.originalPackagePattern(), var1.relocatedPackagePattern(), var1.exclusions(), var1.inclusions());
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException var3) {
         return null;
      }
   }

   private static Object createRelocator(Constructor<?> var0, File var1, File var2, Collection<Object> var3) {
      return var0.newInstance(var1, var2, var3);
   }

   private static DependencyData getJarRelocatorDependency(Collection<Repository> var0) {
      Dependency var1 = new Dependency(Packages.fix("org#ow2#asm"), "asm", "9.8", (String)null, Collections.emptyList());
      Dependency var2 = new Dependency(Packages.fix("org#ow2#asm"), "asm-commons", "9.8", (String)null, Collections.emptyList());
      Dependency var3 = new Dependency(Packages.fix("de#crazydev22#slimjar"), "jar-relocator", "2.1.5", (String)null, Arrays.asList(var1, var2));
      return new DependencyData(Collections.emptySet(), var0, Collections.singleton(var3), Collections.emptyList());
   }

   public static JarRelocatorFacadeFactory create(Path var0, Collection<Repository> var1) {
      IsolatedInjectableClassLoader var2 = new IsolatedInjectableClassLoader();
      return create(var0, var1, var2);
   }

   @Contract("_, _, _ -> new")
   @NotNull
   public static JarRelocatorFacadeFactory create(@NotNull Path var0, @NotNull Collection<Repository> var1, @NotNull InjectableClassLoader var2) {
      ((InjectingApplicationBuilder)((InjectingApplicationBuilder)((InjectingApplicationBuilder)((InjectingApplicationBuilder)((InjectingApplicationBuilder)ApplicationBuilder.injecting("SlimJar", var2).downloadDirectoryPath(var0)).preResolutionDataProviderFactory((var0x) -> {
         return Collections::emptyMap;
      })).dataProviderFactory((var1x) -> {
         return () -> {
            return getJarRelocatorDependency(var1);
         };
      })).relocatorFactory((var0x) -> {
         return new PassthroughRelocator();
      })).relocationHelperFactory((var0x) -> {
         return (var0, var1) -> {
            return var1;
         };
      })).build();

      Constructor var3;
      Constructor var4;
      Method var5;
      try {
         Class var6 = Class.forName(Packages.fix("me#lucko#jarrelocator#JarRelocator"), true, var2);
         Class var7 = Class.forName(Packages.fix("me#lucko#jarrelocator#Relocation"), true, var2);
         var3 = var6.getConstructor(File.class, File.class, Collection.class);
         var4 = var7.getConstructor(String.class, String.class, Collection.class, Collection.class);
         var5 = var6.getMethod("run");
      } catch (NoSuchMethodException var8) {
         throw new RelocatorException("Failed to find JarRelocator constructor", var8);
      } catch (ClassNotFoundException var9) {
         throw new RelocatorException("Failed to find JarRelocator class", var9);
      }

      return new ReflectiveJarRelocatorFacadeFactory(var3, var4, var5);
   }
}
