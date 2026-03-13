package com.volmit.iris.util.slimjar.injector.agent;

import com.volmit.iris.util.slimjar.app.builder.ApplicationBuilder;
import com.volmit.iris.util.slimjar.app.builder.InjectingApplicationBuilder;
import com.volmit.iris.util.slimjar.app.module.ModuleExtractor;
import com.volmit.iris.util.slimjar.app.module.TemporaryModuleExtractor;
import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import com.volmit.iris.util.slimjar.injector.loader.InstrumentationInjectable;
import com.volmit.iris.util.slimjar.injector.loader.IsolatedInjectableClassLoader;
import com.volmit.iris.util.slimjar.injector.loader.manifest.JarManifestGenerator;
import com.volmit.iris.util.slimjar.relocation.JarFileRelocator;
import com.volmit.iris.util.slimjar.relocation.PassthroughRelocator;
import com.volmit.iris.util.slimjar.relocation.RelocationRule;
import com.volmit.iris.util.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.util.Packages;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ByteBuddyInstrumentationFactory implements InstrumentationFactory {
   @NotNull
   public static final String AGENT_JAR = "loader-agent.isolated-jar";
   @NotNull
   private static final String AGENT_PACKAGE = "io#github#slimjar#injector#agent";
   @NotNull
   private static final String AGENT_CLASS = "ClassLoaderAgent";
   @NotNull
   private static final String BYTE_BUDDY_AGENT_CLASS = "net#bytebuddy#agent#ByteBuddyAgent";
   @NotNull
   private final URL agentJarUrl;
   @NotNull
   private final ModuleExtractor extractor;
   @NotNull
   private final JarRelocatorFacadeFactory relocatorFacadeFactory;

   public ByteBuddyInstrumentationFactory(@NotNull JarRelocatorFacadeFactory var1) {
      this((URL)Objects.requireNonNull(InstrumentationInjectable.class.getClassLoader().getResource("loader-agent.isolated-jar")), new TemporaryModuleExtractor(), var1);
   }

   @Contract(
      pure = true
   )
   public ByteBuddyInstrumentationFactory(@NotNull URL var1, @NotNull ModuleExtractor var2, @NotNull JarRelocatorFacadeFactory var3) {
      this.agentJarUrl = var1;
      this.extractor = var2;
      this.relocatorFacadeFactory = var3;
   }

   @NotNull
   public Instrumentation create() {
      URL var1 = this.extractor.extractModule(this.agentJarUrl, "loader-agent");
      String var2 = generatePattern();
      String var3 = String.format("%s.%s", var2, "ClassLoaderAgent");
      RelocationRule var4 = new RelocationRule(Packages.fix("io#github#slimjar#injector#agent"), var2, Collections.emptySet(), Collections.emptySet());
      JarFileRelocator var5 = new JarFileRelocator(Collections.singleton(var4), this.relocatorFacadeFactory);

      File var6;
      File var7;
      try {
         var6 = new File(var1.toURI());
         var7 = File.createTempFile("slimjar-agent", ".jar");
      } catch (IOException | URISyntaxException var19) {
         throw new InjectorException("Failed to create temporary file for relocated agent", var19);
      }

      IsolatedInjectableClassLoader var8 = new IsolatedInjectableClassLoader();
      var5.relocate(var6, var7);
      JarManifestGenerator.with(var7.toURI()).attribute("Manifest-Version", "1.0").attribute("Agent-Class", var3).generate();
      ((InjectingApplicationBuilder)((InjectingApplicationBuilder)((InjectingApplicationBuilder)ApplicationBuilder.injecting("SlimJar-Agent", var8).dataProviderFactory((var0) -> {
         return ByteBuddyInstrumentationFactory::getDependency;
      })).relocatorFactory((var0) -> {
         return new PassthroughRelocator();
      })).relocationHelperFactory((var0) -> {
         return (var0x, var1) -> {
            return var1;
         };
      })).build();

      try {
         Class var9 = Class.forName(Packages.fix("net#bytebuddy#agent#ByteBuddyAgent"), true, var8);
         Method var10 = var9.getMethod("attach", File.class, String.class, String.class);
         Class var11 = Class.forName("java.lang.ProcessHandle");
         Method var12 = var11.getMethod("current");
         Method var13 = var11.getMethod("pid");
         Object var14 = var12.invoke(var11);
         Long var15 = (Long)var13.invoke(var14);
         var10.invoke((Object)null, var7, String.valueOf(var15), "");
         Class var16 = Class.forName(var3, true, ClassLoader.getSystemClassLoader());
         Method var17 = var16.getMethod("getInstrumentation");
         return (Instrumentation)var17.invoke((Object)null);
      } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException var18) {
         throw new InjectorException("Encountered exception while creating ByteBuddy instructions.", var18);
      }
   }

   @NotNull
   private static DependencyData getDependency() {
      Dependency var0 = new Dependency("net.bytebuddy", "byte-buddy-agent", "1.11.0", (String)null, Collections.emptyList());
      return new DependencyData(Collections.emptySet(), Collections.singleton(Repository.central()), Collections.singleton(var0), Collections.emptyList());
   }

   @Contract(
      pure = true
   )
   @NotNull
   private static String generatePattern() {
      return "slimjar.%s".formatted(new Object[]{UUID.randomUUID()});
   }
}
