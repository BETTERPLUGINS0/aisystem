package com.volmit.iris.core.scripting.kotlin.environment;

import com.volmit.iris.Iris;
import com.volmit.iris.core.scripting.environment.SimpleEnvironment;
import com.volmit.iris.core.scripting.kotlin.base.SimpleScript;
import com.volmit.iris.core.scripting.kotlin.runner.FileComponents;
import com.volmit.iris.core.scripting.kotlin.runner.Script;
import com.volmit.iris.core.scripting.kotlin.runner.ScriptRunner;
import com.volmit.iris.core.scripting.kotlin.runner.UtilsKt;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.format.C;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import kotlin.reflect.KClass;
import kotlin.script.experimental.annotations.KotlinScript;
import kotlin.script.experimental.api.EvaluationResult;
import kotlin.script.experimental.api.ResultWithDiagnostics;
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\b\b\b\u0016\u0018\u0000 #2\u00020\u0001:\u0001#B\u001b\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007B\u0013\b\u0017\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0006\u0010\bJ\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000bH\u0016J4\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000b2\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\u00192\u0016\u0010\u001a\u001a\u0012\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u00010\u001bH\u0016J\u0012\u0010\u001d\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0017\u001a\u00020\u000bH\u0016J6\u0010\u001d\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0017\u001a\u00020\u000b2\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\u00192\u0016\u0010\u001a\u001a\u0012\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u00010\u001bH\u0016J\u001c\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000b2\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\rH\u0014J8\u0010\u001f\u001a\u0004\u0018\u00010\u001c2\u0006\u0010 \u001a\u00020\u000b2\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\r2\u0018\b\u0002\u0010!\u001a\u0012\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u00010\u001bH\u0002J\b\u0010\"\u001a\u00020\u0016H\u0016R6\u0010\t\u001a$\u0012\u0004\u0012\u00020\u000b\u0012\u001a\u0012\u0018\u0012\b\u0012\u0006\u0012\u0002\b\u00030\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\f0\nX\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u0005X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006$"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/environment/IrisSimpleExecutionEnvironment;", "Lcom/volmit/iris/core/scripting/environment/SimpleEnvironment;", "baseDir", "Ljava/io/File;", "parent", "Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;", "<init>", "(Ljava/io/File;Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;)V", "(Ljava/io/File;)V", "compileCache", "Lcom/volmit/iris/util/data/KCache;", "", "Lcom/volmit/iris/util/collection/KMap;", "Lkotlin/reflect/KClass;", "Lkotlin/script/experimental/api/ResultWithDiagnostics;", "Lcom/volmit/iris/core/scripting/kotlin/runner/Script;", "getCompileCache", "()Lcom/volmit/iris/util/data/KCache;", "runner", "getRunner", "()Lcom/volmit/iris/core/scripting/kotlin/runner/ScriptRunner;", "execute", "", "script", "type", "Ljava/lang/Class;", "vars", "", "", "evaluate", "compile", "evaluate0", "name", "properties", "configureProject", "Companion", "core"}
)
@SourceDebugExtension({"SMAP\nIrisSimpleExecutionEnvironment.kt\nKotlin\n*S Kotlin\n*F\n+ 1 IrisSimpleExecutionEnvironment.kt\ncom/volmit/iris/core/scripting/kotlin/environment/IrisSimpleExecutionEnvironment\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,187:1\n1374#2:188\n1460#2,5:189\n1056#2:194\n*S KotlinDebug\n*F\n+ 1 IrisSimpleExecutionEnvironment.kt\ncom/volmit/iris/core/scripting/kotlin/environment/IrisSimpleExecutionEnvironment\n*L\n82#1:188\n82#1:189,5\n83#1:194\n*E\n"})
public class IrisSimpleExecutionEnvironment implements SimpleEnvironment {
   @NotNull
   public static final IrisSimpleExecutionEnvironment.Companion Companion = new IrisSimpleExecutionEnvironment.Companion((DefaultConstructorMarker)null);
   @NotNull
   private final KCache<String, KMap<KClass<?>, ResultWithDiagnostics<Script>>> compileCache;
   @NotNull
   private final ScriptRunner runner;
   @NotNull
   private static final String CLASSPATH = "val classpath = mapOf(";
   @NotNull
   private static final String ARTIFACT_ID = "local:${it.substringBeforeLast(\".jar\")}:1.0.0";
   @NotNull
   private static final List<String> BASE_GRADLE;

   public IrisSimpleExecutionEnvironment(@NotNull File var1, @Nullable ScriptRunner var2) {
      Intrinsics.checkNotNullParameter(var1, "baseDir");
      super();
      this.compileCache = new KCache(IrisSimpleExecutionEnvironment::compileCache$lambda$0, 1024L);
      this.runner = new ScriptRunner(var1, var2, (BasicJvmScriptingHost)null, 4, (DefaultConstructorMarker)null);
   }

   @JvmOverloads
   public IrisSimpleExecutionEnvironment(@NotNull File var1) {
      Intrinsics.checkNotNullParameter(var1, "baseDir");
      this(var1, (ScriptRunner)null);
   }

   // $FF: synthetic method
   public IrisSimpleExecutionEnvironment(File var1, int var2, DefaultConstructorMarker var3) {
      if ((var2 & 1) != 0) {
         File var10000 = (new File(".")).getAbsoluteFile();
         Intrinsics.checkNotNullExpressionValue(var10000, "getAbsoluteFile(...)");
         var1 = var10000;
      }

      this(var1);
   }

   @NotNull
   protected final KCache<String, KMap<KClass<?>, ResultWithDiagnostics<Script>>> getCompileCache() {
      return this.compileCache;
   }

   @NotNull
   protected final ScriptRunner getRunner() {
      return this.runner;
   }

   public void execute(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "script");
      this.execute(var1, SimpleScript.class, (Map)null);
   }

   public void execute(@NotNull String var1, @NotNull Class<?> var2, @Nullable Map<String, ? extends Object> var3) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "type");
      Iris.debug("Execute Script (void) " + C.DARK_GREEN + var1);
      this.evaluate0(var1, JvmClassMappingKt.getKotlinClass(var2), var3);
   }

   @Nullable
   public Object evaluate(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "script");
      return this.evaluate(var1, SimpleScript.class, (Map)null);
   }

   @Nullable
   public Object evaluate(@NotNull String var1, @NotNull Class<?> var2, @Nullable Map<String, ? extends Object> var3) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "type");
      Iris.debug("Execute Script (for result) " + C.DARK_GREEN + var1);
      return this.evaluate0(var1, JvmClassMappingKt.getKotlinClass(var2), var3);
   }

   @NotNull
   protected Script compile(@NotNull String var1, @NotNull KClass<?> var2) {
      Intrinsics.checkNotNullParameter(var1, "script");
      Intrinsics.checkNotNullParameter(var2, "type");
      Object var10000 = ((KMap)this.compileCache.get(var1)).computeIfAbsent(var2, IrisSimpleExecutionEnvironment::compile$lambda$2);
      Intrinsics.checkNotNullExpressionValue(var10000, "computeIfAbsent(...)");
      return (Script)UtilsKt.valueOrThrow((ResultWithDiagnostics)var10000, (CharSequence)"Failed to compile script");
   }

   private final Object evaluate0(String var1, KClass<?> var2, Map<String, ? extends Object> var3) {
      Thread var4 = Thread.currentThread();
      ClassLoader var5 = var4.getContextClassLoader();
      var4.setContextClassLoader(this.getClass().getClassLoader());

      try {
         return UtilsKt.value((EvaluationResult)UtilsKt.valueOrThrow(this.compile(var1, var2).evaluate(var3), (CharSequence)"Failed to evaluate script"));
      } catch (Throwable var7) {
         var7.printStackTrace();
         var4.setContextClassLoader(var5);
         return null;
      }
   }

   // $FF: synthetic method
   static Object evaluate0$default(IrisSimpleExecutionEnvironment var0, String var1, KClass var2, Map var3, int var4, Object var5) {
      if (var5 != null) {
         throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: evaluate0");
      } else {
         if ((var4 & 4) != 0) {
            var3 = null;
         }

         return var0.evaluate0(var1, var2, var3);
      }
   }

   public void configureProject() {
      this.runner.getBaseDir().mkdirs();
      List[] var2 = new List[2];
      ClassLoader var10002 = this.getClass().getClassLoader();
      Intrinsics.checkNotNullExpressionValue(var10002, "getClassLoader(...)");
      var2[0] = UtilsKt.getClasspath(var10002);
      var10002 = KotlinScript.class.getClassLoader();
      Intrinsics.checkNotNullExpressionValue(var10002, "getClassLoader(...)");
      var2[1] = UtilsKt.getClasspath(var10002);
      Iterable var11 = (Iterable)CollectionsKt.listOf(var2);
      boolean var3 = false;
      Collection var5 = (Collection)(new ArrayList());
      boolean var6 = false;
      Iterator var7 = var11.iterator();

      while(var7.hasNext()) {
         Object var8 = var7.next();
         List var9 = (List)var8;
         boolean var10 = false;
         Iterable var12 = (Iterable)var9;
         CollectionsKt.addAll(var5, var12);
      }

      var11 = (Iterable)((List)var5);
      var3 = false;
      List var1 = CollectionsKt.toMutableList((Collection)CollectionsKt.sortedWith(var11, (Comparator)(new IrisSimpleExecutionEnvironment$configureProject$$inlined$sortedBy$1())));
      Companion.updateClasspath(new File(this.runner.getBaseDir(), "build.gradle.kts"), var1);
   }

   @JvmOverloads
   public IrisSimpleExecutionEnvironment() {
      this((File)null, 1, (DefaultConstructorMarker)null);
   }

   private static final KMap compileCache$lambda$0(String var0) {
      return new KMap();
   }

   private static final ResultWithDiagnostics compile$lambda$1(IrisSimpleExecutionEnvironment var0, KClass var1, String var2, KClass var3) {
      return ScriptRunner.compile$default(var0.runner, var1, (String)var2, (String)null, 4, (Object)null);
   }

   private static final ResultWithDiagnostics compile$lambda$2(Function1 var0, Object var1) {
      return (ResultWithDiagnostics)var0.invoke(var1);
   }

   static {
      CharSequence var10000 = (CharSequence)"val classpath = mapOf()\n\nplugins {\n    kotlin(\"jvm\") version(\"2.2.0\")\n}\n\nrepositories {\n    mavenCentral()\n    flatDir {\n        dirs(classpath.keys)\n    }\n}\n\nval script by configurations.creating\nconfigurations.compileOnly { extendsFrom(script) }\nconfigurations.kotlinScriptDef { extendsFrom(script) }\nconfigurations.kotlinScriptDefExtensions { extendsFrom(script) }\nconfigurations.kotlinCompilerClasspath { extendsFrom(script) }\nconfigurations.kotlinCompilerPluginClasspath { extendsFrom(script) }\n\ndependencies {\n    classpath.values.flatMap { it }.forEach { script(\"local:${it.substringBeforeLast(\".jar\")}:1.0.0\") }\n}";
      String[] var0 = new String[]{"\n"};
      BASE_GRADLE = StringsKt.split$default(var10000, var0, false, 0, 6, (Object)null);
   }

   @Metadata(
      mv = {2, 2, 0},
      k = 1,
      xi = 48,
      d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001a\u0010\u0006\u001a\u00020\u0007*\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nH\u0002J \u0010\u0006\u001a\u00020\u0005*\b\u0012\u0004\u0012\u00020\u00050\n2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u0018\u0010\u000b\u001a\u00020\u0005*\u00020\u00058BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"},
      d2 = {"Lcom/volmit/iris/core/scripting/kotlin/environment/IrisSimpleExecutionEnvironment$Companion;", "", "<init>", "()V", "CLASSPATH", "", "updateClasspath", "", "Ljava/io/File;", "classpath", "", "escapedPath", "getEscapedPath", "(Ljava/lang/String;)Ljava/lang/String;", "ARTIFACT_ID", "BASE_GRADLE", "core"}
   )
   @SourceDebugExtension({"SMAP\nIrisSimpleExecutionEnvironment.kt\nKotlin\n*S Kotlin\n*F\n+ 1 IrisSimpleExecutionEnvironment.kt\ncom/volmit/iris/core/scripting/kotlin/environment/IrisSimpleExecutionEnvironment$Companion\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,187:1\n1869#2,2:188\n1193#2,2:190\n1267#2,2:192\n1869#2,2:195\n1270#2:197\n360#2,7:198\n1#3:194\n*S KotlinDebug\n*F\n+ 1 IrisSimpleExecutionEnvironment.kt\ncom/volmit/iris/core/scripting/kotlin/environment/IrisSimpleExecutionEnvironment$Companion\n*L\n100#1:188,2\n113#1:190,2\n113#1:192,2\n131#1:195,2\n113#1:197\n148#1:198,7\n*E\n"})
   public static final class Companion {
      private Companion() {
      }

      private final void updateClasspath(File var1, List<? extends File> var2) {
         List var3 = var1.exists() ? FilesKt.readLines$default(var1, (Charset)null, 1, (Object)null) : IrisSimpleExecutionEnvironment.BASE_GRADLE;
         FilesKt.writeText$default(var1, this.updateClasspath(var3, var2), (Charset)null, 2, (Object)null);
      }

      private final String updateClasspath(List<String> var1, List<? extends File> var2) {
         LinkedHashMap var3 = new LinkedHashMap();
         Iterable var4 = (Iterable)var2;
         boolean var5 = false;
         Iterator var6 = var4.iterator();

         boolean var9;
         String var13;
         String var10000;
         while(var6.hasNext()) {
            Object var7 = var6.next();
            File var8 = (File)var7;
            var9 = false;
            var10000 = var8.getCanonicalPath();
            Intrinsics.checkNotNullExpressionValue(var10000, "getCanonicalPath(...)");
            CharSequence var43 = (CharSequence)var10000;
            char[] var10 = new char[]{File.separatorChar};
            List var11 = StringsKt.split$default(var43, var10, false, 0, 6, (Object)null);
            if (var11.size() <= 1) {
               Iris.error("Invalid classpath entry: " + var8);
            } else {
               Object var44 = var3.computeIfAbsent(var11.get(0), IrisSimpleExecutionEnvironment.Companion::updateClasspath$lambda$2$lambda$1);
               Intrinsics.checkNotNullExpressionValue(var44, "computeIfAbsent(...)");
               FileComponents var36 = (FileComponents)var44;

               for(Iterator var12 = var11.subList(1, var11.size()).iterator(); var12.hasNext(); var36 = var36.append(var13)) {
                  var13 = (String)var12.next();
               }
            }
         }

         Collection var45 = var3.values();
         Intrinsics.checkNotNullExpressionValue(var45, "<get-values>(...)");
         Iterable var30 = (Iterable)var45;
         boolean var32 = false;
         int var34 = RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(var30, 10)), 16);
         Map var35 = (Map)(new LinkedHashMap(var34));
         boolean var37 = false;
         Iterator var38 = var30.iterator();

         Object var40;
         while(var38.hasNext()) {
            var40 = var38.next();
            FileComponents var14 = (FileComponents)var40;
            boolean var15 = false;
            Object var16 = null;
            Intrinsics.checkNotNull(var14);
            var16 = var14;
            IrisSimpleExecutionEnvironment.Companion var46 = IrisSimpleExecutionEnvironment.Companion;
            StringBuilder var17 = new StringBuilder();
            StringBuilder var18 = var17;
            IrisSimpleExecutionEnvironment.Companion var19 = var46;

            for(boolean var20 = false; ((FileComponents)var16).getChildren().size() == 1; var16 = CollectionsKt.first((Iterable)((FileComponents)var16).getChildren())) {
               var18.append(((FileComponents)var16).getSegment());
               var18.append(File.separatorChar);
            }

            var18.append(((FileComponents)var16).getSegment());
            var18.append(File.separatorChar);
            String var21 = var19.getEscapedPath(var17.toString());
            Set var47 = (Set)(new LinkedHashSet());
            ArrayDeque var48 = new ArrayDeque();
            var48.add(TuplesKt.to((Object)null, ((FileComponents)var16).getChildren()));

            while(!((Collection)var48).isEmpty()) {
               Pair var50;
               label75: {
                  var50 = (Pair)var48.removeFirst();
                  var10000 = (String)var50.getFirst();
                  if (var10000 != null) {
                     String var22 = var10000;
                     boolean var23 = false;
                     var10000 = var22 + File.separatorChar;
                     if (var10000 != null) {
                        break label75;
                     }
                  }

                  var10000 = "";
               }

               String var24 = var10000;
               Iterable var25 = (Iterable)var50.getSecond();
               boolean var26 = false;
               Iterator var27 = var25.iterator();

               while(var27.hasNext()) {
                  Object var51 = var27.next();
                  FileComponents var52 = (FileComponents)var51;
                  boolean var28 = false;
                  String var29 = var24 + var52.getSegment();
                  if (var52.getChildren().isEmpty()) {
                     var47.add(IrisSimpleExecutionEnvironment.Companion.getEscapedPath(var29));
                  } else {
                     var48.add(TuplesKt.to(var29, var52.getChildren()));
                  }
               }
            }

            Pair var41 = TuplesKt.to(var21, var47);
            var35.put(var41.getFirst(), var41.getSecond());
         }

         String var31 = CollectionsKt.joinToString$default((Iterable)var35.entrySet(), (CharSequence)",", (CharSequence)"val classpath = mapOf(", (CharSequence)")", 0, (CharSequence)null, IrisSimpleExecutionEnvironment.Companion::updateClasspath$lambda$8, 24, (Object)null);
         List var33 = CollectionsKt.toMutableList((Collection)var1);
         var9 = false;
         int var39 = 0;
         var38 = var1.iterator();

         int var49;
         while(true) {
            if (!var38.hasNext()) {
               var49 = -1;
               break;
            }

            var40 = var38.next();
            var13 = (String)var40;
            boolean var42 = false;
            if (StringsKt.startsWith$default(var13, "val classpath = mapOf(", false, 2, (Object)null)) {
               var49 = var39;
               break;
            }

            ++var39;
         }

         var34 = var49;
         if (var34 == -1) {
            var33.clear();
            var33.addAll((Collection)IrisSimpleExecutionEnvironment.BASE_GRADLE);
         }

         var33.set(var34 == -1 ? 0 : var34, var31);
         return CollectionsKt.joinToString$default((Iterable)var33, (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null);
      }

      private final String getEscapedPath(String var1) {
         return StringsKt.replace$default(StringsKt.replace$default(var1, "\\", "\\\\", false, 4, (Object)null), "\"", "\\\"", false, 4, (Object)null);
      }

      private static final FileComponents updateClasspath$lambda$2$lambda$0(List var0, String var1) {
         Intrinsics.checkNotNullParameter(var1, "it");
         return new FileComponents((String)var0.get(0), true);
      }

      private static final FileComponents updateClasspath$lambda$2$lambda$1(Function1 var0, Object var1) {
         return (FileComponents)var0.invoke(var1);
      }

      private static final CharSequence updateClasspath$lambda$8$lambda$7(String var0) {
         Intrinsics.checkNotNullParameter(var0, "f");
         return (CharSequence)("\"" + var0 + "\"");
      }

      private static final CharSequence updateClasspath$lambda$8(Entry var0) {
         Intrinsics.checkNotNullParameter(var0, "it");
         Object var10000 = var0.getKey();
         return (CharSequence)("\"" + var10000 + "\" to setOf(" + CollectionsKt.joinToString$default((Iterable)var0.getValue(), (CharSequence)", ", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, IrisSimpleExecutionEnvironment.Companion::updateClasspath$lambda$8$lambda$7, 30, (Object)null) + ")");
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
