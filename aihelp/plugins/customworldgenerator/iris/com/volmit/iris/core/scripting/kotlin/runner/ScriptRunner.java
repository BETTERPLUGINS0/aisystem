package com.volmit.iris.core.scripting.kotlin.runner;

import com.volmit.iris.core.scripting.kotlin.base.SimpleScript;
import com.volmit.iris.core.scripting.kotlin.runner.resolver.CompoundDependenciesResolver;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.script.experimental.annotations.KotlinScript;
import kotlin.script.experimental.api.CompiledScript;
import kotlin.script.experimental.api.KotlinType;
import kotlin.script.experimental.api.ResultWithDiagnostics;
import kotlin.script.experimental.api.ScriptCompilationConfiguration;
import kotlin.script.experimental.api.ScriptCompilationConfigurationKeys;
import kotlin.script.experimental.api.ScriptCompiler;
import kotlin.script.experimental.api.ScriptEvaluator;
import kotlin.script.experimental.api.SourceCode;
import kotlin.script.experimental.api.ScriptCompilationConfiguration.Builder;
import kotlin.script.experimental.host.ConfigurationFromTemplateKt;
import kotlin.script.experimental.host.FileScriptSource;
import kotlin.script.experimental.host.HostConfigurationKt;
import kotlin.script.experimental.host.ScriptHostUtilKt;
import kotlin.script.experimental.host.ScriptingHostConfiguration;
import kotlin.script.experimental.jvm.JvmScriptCompilationConfigurationBuilder;
import kotlin.script.experimental.jvm.JvmScriptCompilationKt;
import kotlin.script.experimental.jvm.JvmScriptingHostConfigurationKt;
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost;
import kotlin.script.experimental.jvmhost.JvmScriptCompiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0000\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ,\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\r2\u0006\u0010\u0019\u001a\u00020\u001a2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001aJ,\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\r2\u0006\u0010\u001c\u001a\u00020\u00032\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u001aJ\"\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0014\u0010 \u001a\u00020\u000e2\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\rH\u0002R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\r\u0012\u0004\u0012\u00020\u000e0\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006!"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;", "", "baseDir", "Ljava/io/File;", "parent", "host", "Lkotlin/script/experimental/jvmhost/BasicJvmScriptingHost;", "<init>", "(Ljava/io/File;Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;Lkotlin/script/experimental/jvmhost/BasicJvmScriptingHost;)V", "getBaseDir", "()Ljava/io/File;", "configs", "Ljava/util/concurrent/ConcurrentHashMap;", "Lkotlin/reflect/KClass;", "Lkotlin/script/experimental/api/ScriptCompilationConfiguration;", "hostConfig", "Lkotlin/script/experimental/host/ScriptingHostConfiguration;", "sharedClassLoader", "Lcom/volmit/iris/core/scripting/kotlin/runner/SharedClassLoader;", "resolver", "Lcom/volmit/iris/core/scripting/kotlin/runner/resolver/CompoundDependenciesResolver;", "compile", "Lkotlin/script/experimental/api/ResultWithDiagnostics;", "Lcom/volmit/iris/core/scripting/kotlin/runner/Script;", "type", "raw", "", "name", "file", "preloaded", "code", "Lkotlin/script/experimental/api/SourceCode;", "createConfig", "core"}
)
public final class ScriptRunner {
   @NotNull
   private final File baseDir;
   @NotNull
   private final BasicJvmScriptingHost host;
   @NotNull
   private final ConcurrentHashMap<KClass<?>, ScriptCompilationConfiguration> configs;
   @NotNull
   private final ScriptingHostConfiguration hostConfig;
   @NotNull
   private final SharedClassLoader sharedClassLoader;
   @NotNull
   private final CompoundDependenciesResolver resolver;

   public ScriptRunner(@NotNull File var1, @Nullable ScriptRunner var2, @NotNull BasicJvmScriptingHost var3) {
      SharedClassLoader var10001;
      label11: {
         Intrinsics.checkNotNullParameter(var1, "baseDir");
         Intrinsics.checkNotNullParameter(var3, "host");
         super();
         this.baseDir = var1;
         this.host = var3;
         this.configs = new ConcurrentHashMap();
         this.hostConfig = HostConfigurationKt.withDefaultsFrom(this.host.getBaseHostConfiguration(), JvmScriptingHostConfigurationKt.getDefaultJvmScriptingHostConfiguration());
         if (var2 != null) {
            var10001 = var2.sharedClassLoader;
            if (var10001 != null) {
               break label11;
            }
         }

         var10001 = new SharedClassLoader((ClassLoader)null, 1, (DefaultConstructorMarker)null);
      }

      this.sharedClassLoader = var10001;
      this.resolver = UtilsKt.createResolver(this.baseDir);
   }

   // $FF: synthetic method
   public ScriptRunner(File var1, ScriptRunner var2, BasicJvmScriptingHost var3, int var4, DefaultConstructorMarker var5) {
      if ((var4 & 2) != 0) {
         var2 = null;
      }

      if ((var4 & 4) != 0) {
         var3 = new BasicJvmScriptingHost((ScriptingHostConfiguration)null, (JvmScriptCompiler)null, (ScriptEvaluator)null, 7, (DefaultConstructorMarker)null);
      }

      this(var1, var2, var3);
   }

   @NotNull
   public final File getBaseDir() {
      return this.baseDir;
   }

   @NotNull
   public final ResultWithDiagnostics<Script> compile(@NotNull KClass<?> var1, @NotNull String var2, @Nullable String var3) {
      Intrinsics.checkNotNullParameter(var1, "type");
      Intrinsics.checkNotNullParameter(var2, "raw");
      return this.compile(var1, ScriptHostUtilKt.toScriptSource(var2, var3));
   }

   // $FF: synthetic method
   public static ResultWithDiagnostics compile$default(ScriptRunner var0, KClass var1, String var2, String var3, int var4, Object var5) {
      if ((var4 & 4) != 0) {
         var3 = null;
      }

      return var0.compile(var1, var2, var3);
   }

   @NotNull
   public final ResultWithDiagnostics<Script> compile(@NotNull KClass<?> var1, @NotNull File var2, @Nullable String var3) {
      Intrinsics.checkNotNullParameter(var1, "type");
      Intrinsics.checkNotNullParameter(var2, "file");
      return this.compile(var1, (SourceCode)(new FileScriptSource(var2, var3)));
   }

   // $FF: synthetic method
   public static ResultWithDiagnostics compile$default(ScriptRunner var0, KClass var1, File var2, String var3, int var4, Object var5) {
      if ((var4 & 4) != 0) {
         var3 = null;
      }

      return var0.compile(var1, var2, var3);
   }

   private final ResultWithDiagnostics<Script> compile(final KClass<?> var1, final SourceCode var2) {
      return (ResultWithDiagnostics)this.host.runInCoroutineContext((Function1)(new Function1<Continuation<? super ResultWithDiagnostics<? extends CachedScript>>, Object>((Continuation)null) {
         int label;

         public final Object invokeSuspend(Object var1x) {
            Object var2x = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            Object var10000;
            switch(this.label) {
            case 0:
               ResultKt.throwOnFailure(var1x);
               ScriptCompiler var3 = ScriptRunner.this.host.getCompiler();
               SourceCode var10001 = var2;
               Object var10002 = ScriptRunner.this.configs.computeIfAbsent(var1, <undefinedtype>::invokeSuspend$lambda$0);
               Intrinsics.checkNotNullExpressionValue(var10002, "computeIfAbsent(...)");
               ScriptCompilationConfiguration var4 = (ScriptCompilationConfiguration)var10002;
               Continuation var10003 = (Continuation)this;
               this.label = 1;
               var10000 = var3.invoke(var10001, var4, var10003);
               if (var10000 == var2x) {
                  return var2x;
               }
               break;
            case 1:
               ResultKt.throwOnFailure(var1x);
               var10000 = var1x;
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            return UtilsKt.map((ResultWithDiagnostics)var10000, <undefinedtype>::invokeSuspend$lambda$1);
         }

         public final Continuation<Unit> create(Continuation<?> var1x) {
            return (Continuation)(new <anonymous constructor>(var1x));
         }

         public final Object invoke(Continuation<? super ResultWithDiagnostics<CachedScript>> var1x) {
            return ((<undefinedtype>)this.create(var1x)).invokeSuspend(Unit.INSTANCE);
         }

         private static final ScriptCompilationConfiguration invokeSuspend$lambda$0(Function1 var0, Object var1x) {
            return (ScriptCompilationConfiguration)var0.invoke(var1x);
         }

         private static final CachedScript invokeSuspend$lambda$1(ScriptRunner var0, CompiledScript var1x) {
            return new CachedScript(var1x, var0.host, var0.hostConfig);
         }
      }));
   }

   private final ScriptCompilationConfiguration createConfig(KClass<?> var1) {
      return ConfigurationFromTemplateKt.createCompilationConfigurationFromTemplate(new KotlinType(var1, false, 2, (DefaultConstructorMarker)null), this.hostConfig, var1, ScriptRunner::createConfig$lambda$1);
   }

   private static final Unit createConfig$lambda$1$lambda$0(KClass var0, JvmScriptCompilationConfigurationBuilder var1) {
      Intrinsics.checkNotNullParameter(var1, "$this$jvm");
      JvmScriptCompilationKt.dependenciesFromClassContext(var1, var0, new String[0], true);
      JvmScriptCompilationKt.dependenciesFromClassContext(var1, Reflection.getOrCreateKotlinClass(var1.getClass()), new String[0], true);
      JvmScriptCompilationKt.dependenciesFromClassContext(var1, Reflection.getOrCreateKotlinClass(KotlinScript.class), new String[0], true);
      return Unit.INSTANCE;
   }

   private static final Unit createConfig$lambda$1(ScriptRunner var0, KClass var1, Builder var2) {
      Intrinsics.checkNotNullParameter(var2, "$this$createCompilationConfigurationFromTemplate");
      var2.invoke(UtilsKt.getDependencyResolver((ScriptCompilationConfigurationKeys)var2), var0.resolver);
      var2.invoke(UtilsKt.getPackDirectory((ScriptCompilationConfigurationKeys)var2), var0.baseDir);
      var2.invoke(UtilsKt.getSharedClassloader((ScriptCompilationConfigurationKeys)var2), var0.sharedClassLoader);
      var2.invoke(UtilsKt.getServer((ScriptCompilationConfigurationKeys)var2), true);
      if (SimpleScript.class.isAssignableFrom(JvmClassMappingKt.getJavaClass(var1))) {
         return Unit.INSTANCE;
      } else {
         var2.invoke((kotlin.script.experimental.util.PropertiesCollection.Builder)JvmScriptCompilationKt.getJvm((ScriptCompilationConfigurationKeys)var2), ScriptRunner::createConfig$lambda$1$lambda$0);
         UtilsKt.configure(var2);
         return Unit.INSTANCE;
      }
   }
}
