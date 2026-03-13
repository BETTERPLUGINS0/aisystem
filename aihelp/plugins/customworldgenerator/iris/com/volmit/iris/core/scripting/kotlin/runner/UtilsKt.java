package com.volmit.iris.core.scripting.kotlin.runner;

import com.volmit.iris.core.scripting.kotlin.runner.resolver.CompoundDependenciesResolver;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.Result.Companion;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.reflect.KClass;
import kotlin.reflect.KProperty;
import kotlin.script.experimental.api.ErrorHandlingKt;
import kotlin.script.experimental.api.EvaluationResult;
import kotlin.script.experimental.api.RefineConfigurationBuilder;
import kotlin.script.experimental.api.ResultValue;
import kotlin.script.experimental.api.ResultWithDiagnostics;
import kotlin.script.experimental.api.ScriptCollectedData;
import kotlin.script.experimental.api.ScriptCollectedDataKeys;
import kotlin.script.experimental.api.ScriptCompilationConfiguration;
import kotlin.script.experimental.api.ScriptCompilationConfigurationKeys;
import kotlin.script.experimental.api.ScriptCompilationKt;
import kotlin.script.experimental.api.ScriptConfigurationRefinementContext;
import kotlin.script.experimental.api.ScriptDataKt;
import kotlin.script.experimental.api.ScriptDependency;
import kotlin.script.experimental.api.ScriptDiagnostic;
import kotlin.script.experimental.api.ScriptSourceAnnotation;
import kotlin.script.experimental.api.ResultValue.Error;
import kotlin.script.experimental.api.ResultValue.Value;
import kotlin.script.experimental.api.ResultWithDiagnostics.Failure;
import kotlin.script.experimental.api.ResultWithDiagnostics.Success;
import kotlin.script.experimental.api.ScriptCompilationConfiguration.Builder;
import kotlin.script.experimental.api.ScriptDiagnostic.Severity;
import kotlin.script.experimental.api.SourceCode.Location;
import kotlin.script.experimental.api.SourceCode.LocationWithId;
import kotlin.script.experimental.api.SourceCode.Position;
import kotlin.script.experimental.dependencies.DependsOn;
import kotlin.script.experimental.dependencies.ExternalDependenciesResolver;
import kotlin.script.experimental.dependencies.Repository;
import kotlin.script.experimental.dependencies.ExternalDependenciesResolver.Options;
import kotlin.script.experimental.jvm.JvmDependency;
import kotlin.script.experimental.jvm.JvmDependencyFromClassLoader;
import kotlin.script.experimental.jvm.JvmScriptCompilationKt;
import kotlin.script.experimental.jvm.util.JvmClasspathUtilKt;
import kotlin.script.experimental.util.PropertiesCollection;
import kotlin.script.experimental.util.PropertiesCollection.Key;
import kotlin.script.experimental.util.PropertiesCollection.PropertyKeyDelegate;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 2,
   xi = 48,
   d1 = {"\u0000 \u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a8\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0003\"\u0004\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00030\u00012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00020\u0005H\u0000\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0007*\u00020\bH\u0000\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0007*\u00020\tH\u0000\u001a\u0012\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000bH\u0000\u001a\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u00012\u0006\u0010\u0014\u001a\u00020\u0015H\u0002\u001aB\u0010\u0016\u001a\u0016\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00190\u0018\u0018\u00010\u0017*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00190\u00180\u001a2\u000e\u0010\u001b\u001a\n\u0012\u0004\u0012\u00020\u001c\u0018\u00010\u001aH\u0002\u001aD\u0010\u001d\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00190\u00180\u00170\u0001*\u00020\u001e2\u0010\u0010\u001f\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030!0 2\u0006\u0010\"\u001a\u00020\u0019H\u0082@¢\u0006\u0002\u0010#\u001a%\u0010-\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010.\u001a\u00020/H\u0000¢\u0006\u0002\u00100\u001a\u0014\u0010?\u001a\u00020@*\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\rH\u0002\u001a,\u0010A\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010B\u001a\b\u0012\u0004\u0012\u00020C0\u001aH\u0002\u001a\f\u0010D\u001a\u00020@*\u00020EH\u0000\"\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u000f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000\"\u001a\u0010$\u001a\u0004\u0018\u00010\u0019*\u00020%8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b&\u0010'\"\u001a\u0010\"\u001a\u0004\u0018\u00010\u0019*\u00020%8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b(\u0010'\"\u001e\u0010)\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0017*\u00020*8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b+\u0010,\"%\u00101\u001a\b\u0012\u0004\u0012\u00020\r02*\u0002038@X\u0080\u0084\u0002¢\u0006\f\n\u0004\b6\u00107\u001a\u0004\b4\u00105\"%\u00108\u001a\b\u0012\u0004\u0012\u00020\u000b02*\u0002038@X\u0080\u0084\u0002¢\u0006\f\n\u0004\b:\u00107\u001a\u0004\b9\u00105\"%\u0010;\u001a\b\u0012\u0004\u0012\u00020\u001102*\u0002038@X\u0080\u0084\u0002¢\u0006\f\n\u0004\b=\u00107\u001a\u0004\b<\u00105\"%\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u001902*\u0002038@X\u0080\u0084\u0002¢\u0006\f\n\u0004\b>\u00107\u001a\u0004\b(\u00105¨\u0006F"},
   d2 = {"map", "Lkotlin/script/experimental/api/ResultWithDiagnostics;", "R", "T", "transformer", "Lkotlin/Function1;", "value", "", "Lkotlin/script/experimental/api/EvaluationResult;", "Lkotlin/script/experimental/api/ResultValue;", "workDir", "Ljava/io/File;", "createResolver", "Lcom/volmit/iris/core/scripting/kotlin/runner/resolver/CompoundDependenciesResolver;", "baseDir", "resolver", "loader", "Lcom/volmit/iris/core/scripting/kotlin/runner/SharedClassLoader;", "configureMavenDepsOnAnnotations", "Lkotlin/script/experimental/api/ScriptCompilationConfiguration;", "context", "Lkotlin/script/experimental/api/ScriptConfigurationRefinementContext;", "filterNewClasspath", "", "Lkotlin/Pair;", "", "", "known", "Lkotlin/script/experimental/api/ScriptDependency;", "resolveDependencies", "Lkotlin/script/experimental/dependencies/ExternalDependenciesResolver;", "annotations", "", "Lkotlin/script/experimental/api/ScriptSourceAnnotation;", "server", "(Lkotlin/script/experimental/dependencies/ExternalDependenciesResolver;Ljava/lang/Iterable;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "shared", "Lkotlin/script/experimental/dependencies/ExternalDependenciesResolver$Options;", "getShared", "(Lkotlin/script/experimental/dependencies/ExternalDependenciesResolver$Options;)Ljava/lang/Boolean;", "getServer", "classpath", "Ljava/lang/ClassLoader;", "getClasspath", "(Ljava/lang/ClassLoader;)Ljava/util/List;", "valueOrThrow", "message", "", "(Lkotlin/script/experimental/api/ResultWithDiagnostics;Ljava/lang/CharSequence;)Ljava/lang/Object;", "dependencyResolver", "Lkotlin/script/experimental/util/PropertiesCollection$Key;", "Lkotlin/script/experimental/api/ScriptCompilationConfigurationKeys;", "getDependencyResolver", "(Lkotlin/script/experimental/api/ScriptCompilationConfigurationKeys;)Lkotlin/script/experimental/util/PropertiesCollection$Key;", "dependencyResolver$delegate", "Lkotlin/script/experimental/util/PropertiesCollection$PropertyKeyDelegate;", "packDirectory", "getPackDirectory", "packDirectory$delegate", "sharedClassloader", "getSharedClassloader", "sharedClassloader$delegate", "server$delegate", "addPack", "", "appendReports", "reports", "Lkotlin/script/experimental/api/ScriptDiagnostic;", "configure", "Lkotlin/script/experimental/api/ScriptCompilationConfiguration$Builder;", "core"}
)
@SourceDebugExtension({"SMAP\nUtils.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Utils.kt\ncom/volmit/iris/core/scripting/kotlin/runner/UtilsKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 errorHandling.kt\nkotlin/script/experimental/api/ErrorHandlingKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 filterByAnnotationType.kt\nkotlin/script/experimental/util/FilterByAnnotationTypeKt\n*L\n1#1,217:1\n1#2:218\n1#2:314\n139#3,4:219\n296#3,4:232\n296#3,4:236\n171#3,9:252\n139#3,2:261\n171#3,20:263\n141#3,2:283\n180#3,4:285\n172#3,2:289\n186#3,5:291\n296#3,4:296\n1460#4,5:223\n827#4:228\n855#4,2:229\n1869#4:231\n1870#4:240\n774#4:242\n865#4,2:243\n1563#4:246\n1634#4,2:247\n1636#4:250\n1563#4:300\n1634#4,3:301\n1617#4,9:304\n1869#4:313\n1870#4:315\n1626#4:316\n1563#4:317\n1634#4,3:318\n1563#4:321\n1634#4,3:322\n11#5:241\n12#5:245\n14#5:249\n15#5:251\n*S KotlinDebug\n*F\n+ 1 Utils.kt\ncom/volmit/iris/core/scripting/kotlin/runner/UtilsKt\n*L\n108#1:314\n99#1:219,4\n138#1:232,4\n143#1:236,4\n158#1:252,9\n162#1:261,2\n167#1:263,20\n162#1:283,2\n158#1:285,4\n158#1:289,2\n158#1:291,5\n178#1:296,4\n122#1:223,5\n126#1:228\n126#1:229,2\n134#1:231\n134#1:240\n157#1:242\n157#1:243,2\n157#1:246\n157#1:247,2\n157#1:250\n102#1:300\n102#1:301,3\n108#1:304,9\n108#1:313\n108#1:315\n108#1:316\n112#1:317\n112#1:318,3\n169#1:321\n169#1:322,3\n157#1:241\n157#1:245\n157#1:249\n157#1:251\n*E\n"})
public final class UtilsKt {
   // $FF: synthetic field
   static final KProperty<Object>[] $$delegatedProperties;
   @NotNull
   private static final File workDir;
   @NotNull
   private static final CompoundDependenciesResolver resolver;
   @NotNull
   private static final SharedClassLoader loader;
   @NotNull
   private static final PropertyKeyDelegate dependencyResolver$delegate;
   @NotNull
   private static final PropertyKeyDelegate packDirectory$delegate;
   @NotNull
   private static final PropertyKeyDelegate sharedClassloader$delegate;
   @NotNull
   private static final PropertyKeyDelegate server$delegate;

   @NotNull
   public static final <T, R> ResultWithDiagnostics<R> map(@NotNull ResultWithDiagnostics<? extends T> var0, @NotNull Function1<? super T, ? extends R> var1) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "transformer");
      ResultWithDiagnostics var10000;
      if (var0 instanceof Success) {
         var10000 = (ResultWithDiagnostics)(new Success(var1.invoke(((Success)var0).getValue()), ((Success)var0).getReports()));
      } else {
         if (!(var0 instanceof Failure)) {
            throw new NoWhenBranchMatchedException();
         }

         var10000 = var0;
      }

      return var10000;
   }

   @Nullable
   public static final Object value(@NotNull EvaluationResult var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      return value(var0.getReturnValue());
   }

   @Nullable
   public static final Object value(@NotNull ResultValue var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Object var10000;
      if (var0 instanceof Value) {
         var10000 = ((Value)var0).getValue();
      } else {
         if (var0 instanceof Error) {
            throw ((Error)var0).getError();
         }

         var10000 = null;
      }

      return var10000;
   }

   @NotNull
   public static final CompoundDependenciesResolver createResolver(@NotNull File var0) {
      Intrinsics.checkNotNullParameter(var0, "baseDir");
      return new CompoundDependenciesResolver(var0);
   }

   // $FF: synthetic method
   public static CompoundDependenciesResolver createResolver$default(File var0, int var1, Object var2) {
      if ((var1 & 1) != 0) {
         var0 = workDir;
      }

      return createResolver(var0);
   }

   private static final ResultWithDiagnostics<ScriptCompilationConfiguration> configureMavenDepsOnAnnotations(ScriptConfigurationRefinementContext var0) {
      Companion var10000;
      try {
         var10000 = Result.Companion;
         boolean var21 = false;
         ScriptCollectedData var31 = var0.getCollectedData();
         if (var31 != null) {
            List var32 = (List)var31.get(ScriptDataKt.getCollectedAnnotations((ScriptCollectedDataKeys)ScriptCollectedData.Companion));
            if (var32 != null) {
               List var22 = var32;
               boolean var26 = false;
               var32 = !((Collection)var22).isEmpty() ? var22 : null;
               if (var32 != null) {
                  final List var5 = var32;
                  List var6 = (List)(new ArrayList());
                  SharedClassLoader var7 = (SharedClassLoader)var0.getCompilationConfiguration().get(getSharedClassloader((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion));
                  CompoundDependenciesResolver var34 = (CompoundDependenciesResolver)var0.getCompilationConfiguration().get(getDependencyResolver((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion));
                  if (var34 == null) {
                     var34 = resolver;
                  }

                  final CompoundDependenciesResolver var8 = var34;
                  Boolean var35 = (Boolean)var0.getCompilationConfiguration().get(getServer((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion));
                  final boolean var23 = var35 != null ? var35 : false;
                  File var24 = (File)var0.getCompilationConfiguration().get(getPackDirectory((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion));
                  if (var24 != null) {
                     addPack(var24, var8);
                  } else {
                     String var9 = var0.getScript().getLocationId();
                     if (var9 != null) {
                        boolean var11 = false;
                        File var10 = new File(var9);
                        boolean var13 = false;
                        File var14 = var10.exists() ? var10 : null;
                        if (var14 != null) {
                           var13 = false;
                           String var10002 = var0.getScript().getLocationId();
                           Intrinsics.checkNotNull(var10002);
                           LocationWithId var15 = new LocationWithId(var10002, new Location(new Position(0, 0, (Integer)null, 4, (DefaultConstructorMarker)null), (Position)null, 2, (DefaultConstructorMarker)null));
                           String var36 = FilesKt.normalize(var14).getAbsolutePath();
                           Intrinsics.checkNotNullExpressionValue(var36, "getAbsolutePath(...)");
                           CharSequence var37 = (CharSequence)var36;
                           char[] var16 = new char[]{File.separatorChar};
                           List var17 = StringsKt.split$default(var37, var16, false, 0, 6, (Object)null);

                           for(int var29 = var17.size() - 1; 0 < var29; --var29) {
                              if (Intrinsics.areEqual(var17.get(var29), "scripts")) {
                                 Iterable var33 = (Iterable)var17.subList(0, var29);
                                 String var10003 = File.separator;
                                 Intrinsics.checkNotNullExpressionValue(var10003, "separator");
                                 File var18 = new File(CollectionsKt.joinToString$default(var33, (CharSequence)var10003, (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null));
                                 var10003 = File.separator;
                                 if ((new File(var18, "dimensions" + var10003 + var17.get(var29 - 1) + ".json")).exists()) {
                                    addPack(var18, var8);
                                    var6.add(new ScriptDiagnostic(0, "Adding pack \"" + var18 + "\"", Severity.INFO, var15, (Throwable)null, 16, (DefaultConstructorMarker)null));
                                 }
                              }
                           }
                        }
                     }
                  }

                  ResultWithDiagnostics var25 = (ResultWithDiagnostics)BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)(new Function2<CoroutineScope, Continuation<? super ResultWithDiagnostics<? extends List<? extends Pair<? extends File, ? extends Boolean>>>>, Object>((Continuation)null) {
                     int label;

                     public final Object invokeSuspend(Object var1) {
                        Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        Object var10000;
                        switch(this.label) {
                        case 0:
                           ResultKt.throwOnFailure(var1);
                           ExternalDependenciesResolver var3 = (ExternalDependenciesResolver)var8;
                           Iterable var10001 = (Iterable)var5;
                           boolean var10002 = var23;
                           Continuation var10003 = (Continuation)this;
                           this.label = 1;
                           var10000 = UtilsKt.resolveDependencies(var3, var10001, var10002, var10003);
                           if (var10000 == var2) {
                              return var2;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(var1);
                           var10000 = var1;
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        return var10000;
                     }

                     public final Continuation<Unit> create(Object var1, Continuation<?> var2) {
                        return (Continuation)(new <anonymous constructor>(var2));
                     }

                     public final Object invoke(CoroutineScope var1, Continuation<? super ResultWithDiagnostics<? extends List<? extends Pair<? extends File, Boolean>>>> var2) {
                        return ((<undefinedtype>)this.create(var1, var2)).invokeSuspend(Unit.INSTANCE);
                     }
                  }), 1, (Object)null);
                  var26 = false;
                  ResultWithDiagnostics var38;
                  if (var25 instanceof Success) {
                     var32 = ((Success)var25).getReports();
                     List var12 = (List)((Success)var25).getValue();
                     List var19 = var32;
                     boolean var27 = false;
                     var38 = ErrorHandlingKt.plus(var19, (ResultWithDiagnostics)ErrorHandlingKt.asSuccess$default(ScriptCompilationKt.with(var0.getCompilationConfiguration(), UtilsKt::configureMavenDepsOnAnnotations$lambda$10$lambda$9$lambda$8), (List)null, 1, (Object)null));
                  } else {
                     if (!(var25 instanceof Failure)) {
                        throw new NoWhenBranchMatchedException();
                     }

                     var38 = var25;
                  }

                  return appendReports(var38, (Collection)var6);
               }
            }
         }

         return (ResultWithDiagnostics)ErrorHandlingKt.asSuccess$default(var0.getCompilationConfiguration(), (List)null, 1, (Object)null);
      } catch (Throwable var20) {
         var10000 = Result.Companion;
         Object var1 = Result.constructor-impl(ResultKt.createFailure(var20));
         Throwable var28 = Result.exceptionOrNull-impl(var1);
         Object var30;
         if (var28 == null) {
            var30 = var1;
         } else {
            Throwable var2 = var28;
            boolean var3 = false;
            ScriptDiagnostic[] var4 = new ScriptDiagnostic[]{ErrorHandlingKt.asDiagnostics$default(var2, 0, (String)null, (String)null, (Location)null, (Severity)null, 31, (Object)null)};
            var30 = new Failure(var4);
         }

         return (ResultWithDiagnostics)var30;
      }
   }

   private static final List<Pair<File, Boolean>> filterNewClasspath(Collection<? extends Pair<? extends File, Boolean>> var0, Collection<? extends ScriptDependency> var1) {
      if (var0.isEmpty()) {
         return null;
      } else {
         Iterable var3;
         boolean var5;
         HashSet var20;
         if (var1 != null) {
            var3 = (Iterable)var1;
            Collection var4 = (Collection)(new HashSet());
            var5 = false;
            Iterator var6 = var3.iterator();

            while(var6.hasNext()) {
               List var19;
               label54: {
                  Object var7 = var6.next();
                  ScriptDependency var8 = (ScriptDependency)var7;
                  boolean var9 = false;
                  JvmDependency var10000 = var8 instanceof JvmDependency ? (JvmDependency)var8 : null;
                  if ((var8 instanceof JvmDependency ? (JvmDependency)var8 : null) != null) {
                     var19 = var10000.getClasspath();
                     if (var19 != null) {
                        break label54;
                     }
                  }

                  var19 = CollectionsKt.emptyList();
               }

               Iterable var16 = (Iterable)var19;
               CollectionsKt.addAll(var4, var16);
            }

            var20 = (HashSet)var4;
         } else {
            var20 = null;
         }

         HashSet var2 = var20;
         var3 = (Iterable)var0;
         boolean var13 = false;
         Collection var14 = (Collection)(new ArrayList());
         boolean var15 = false;
         Iterator var17 = var3.iterator();

         while(var17.hasNext()) {
            Object var18 = var17.next();
            Pair var10 = (Pair)var18;
            boolean var11 = false;
            if (!(var2 != null ? var2.contains(var10.getFirst()) : false)) {
               var14.add(var18);
            }
         }

         List var12 = (List)var14;
         var5 = false;
         return !((Collection)var12).isEmpty() ? var12 : null;
      }
   }

   private static final Object resolveDependencies(ExternalDependenciesResolver param0, Iterable<? extends ScriptSourceAnnotation<?>> param1, boolean param2, Continuation<? super ResultWithDiagnostics<? extends List<? extends Pair<? extends File, Boolean>>>> param3) {
      // $FF: Couldn't be decompiled
   }

   private static final Boolean getShared(Options var0) {
      return var0.flag("shared");
   }

   private static final Boolean getServer(Options var0) {
      return var0.flag("server");
   }

   @NotNull
   public static final List<File> getClasspath(@NotNull ClassLoader var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      List var10000 = JvmClasspathUtilKt.classpathFromClassloader$default(var0, false, 2, (Object)null);
      if (var10000 == null) {
         var10000 = CollectionsKt.emptyList();
      }

      return var10000;
   }

   public static final <R> R valueOrThrow(@NotNull ResultWithDiagnostics<? extends R> var0, @NotNull CharSequence var1) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      Intrinsics.checkNotNullParameter(var1, "message");
      boolean var3 = false;
      if (var0 instanceof Success) {
         return ((Success)var0).getValue();
      } else if (var0 instanceof Failure) {
         Failure var5 = (Failure)var0;
         boolean var6 = false;
         throw new RuntimeException(CollectionsKt.joinToString$default((Iterable)var5.getReports(), (CharSequence)"\n", (CharSequence)(var1 + "\n"), (CharSequence)null, 0, (CharSequence)null, UtilsKt::valueOrThrow$lambda$25$lambda$24, 28, (Object)null));
      } else {
         throw new NoWhenBranchMatchedException();
      }
   }

   @NotNull
   public static final Key<CompoundDependenciesResolver> getDependencyResolver(@NotNull ScriptCompilationConfigurationKeys var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      return dependencyResolver$delegate.getValue(var0, $$delegatedProperties[0]);
   }

   @NotNull
   public static final Key<File> getPackDirectory(@NotNull ScriptCompilationConfigurationKeys var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      return packDirectory$delegate.getValue(var0, $$delegatedProperties[1]);
   }

   @NotNull
   public static final Key<SharedClassLoader> getSharedClassloader(@NotNull ScriptCompilationConfigurationKeys var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      return sharedClassloader$delegate.getValue(var0, $$delegatedProperties[2]);
   }

   @NotNull
   public static final Key<Boolean> getServer(@NotNull ScriptCompilationConfigurationKeys var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      return server$delegate.getValue(var0, $$delegatedProperties[3]);
   }

   private static final void addPack(File var0, CompoundDependenciesResolver var1) {
      var1.addPack(var0);
   }

   private static final <R> ResultWithDiagnostics<R> appendReports(ResultWithDiagnostics<? extends R> var0, Collection<ScriptDiagnostic> var1) {
      ResultWithDiagnostics var10000;
      if (var1.isEmpty()) {
         var10000 = var0;
      } else if (var0 instanceof Success) {
         var10000 = (ResultWithDiagnostics)(new Success(((Success)var0).getValue(), CollectionsKt.plus((Collection)((Success)var0).getReports(), (Iterable)var1)));
      } else {
         if (!(var0 instanceof Failure)) {
            throw new NoWhenBranchMatchedException();
         }

         var10000 = (ResultWithDiagnostics)(new Failure(CollectionsKt.plus((Collection)((Failure)var0).getReports(), (Iterable)var1)));
      }

      return var10000;
   }

   public static final void configure(@NotNull Builder var0) {
      Intrinsics.checkNotNullParameter(var0, "<this>");
      var0.invoke((kotlin.script.experimental.util.PropertiesCollection.Builder)ScriptCompilationKt.getRefineConfiguration(var0), UtilsKt::configure$lambda$28);
   }

   private static final Unit configureMavenDepsOnAnnotations$lambda$10$lambda$9$lambda$8(boolean var0, List var1, SharedClassLoader var2, Builder var3) {
      Intrinsics.checkNotNullParameter(var3, "$this$with");
      boolean var8;
      boolean var12;
      if (!var0) {
         Iterable var26 = (Iterable)var1;
         boolean var27 = false;
         Collection var31 = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault(var26, 10)));
         var8 = false;
         Iterator var34 = var26.iterator();

         while(var34.hasNext()) {
            Object var36 = var34.next();
            Pair var37 = (Pair)var36;
            var12 = false;
            var31.add((File)var37.getFirst());
         }

         JvmScriptCompilationKt.updateClasspath(var3, (Collection)((List)var31));
         return Unit.INSTANCE;
      } else {
         List var10000 = filterNewClasspath((Collection)var1, (Collection)var3.get(ScriptCompilationKt.getDependencies((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion)));
         if (var10000 == null) {
            return Unit.INSTANCE;
         } else {
            List var4 = var10000;
            Iterable var6 = (Iterable)var1;
            boolean var7 = false;
            Collection var9 = (Collection)(new ArrayList());
            boolean var10 = false;
            var12 = false;
            Iterator var13 = var6.iterator();

            while(var13.hasNext()) {
               Object var14 = var13.next();
               boolean var16 = false;
               Pair var17 = (Pair)var14;
               boolean var18 = false;
               Object var19 = var17.getFirst();
               File var20 = (File)var19;
               boolean var21 = false;
               File var41 = (File)((Boolean)var17.getSecond() ? var19 : null);
               if (var41 != null) {
                  File var22 = var41;
                  boolean var23 = false;
                  var9.add(var22);
               }
            }

            List var5 = (List)var9;
            if (!((Collection)var5).isEmpty()) {
               Intrinsics.checkNotNull(var2);
               var2.addFiles(var5);
            }

            Iterable var29 = (Iterable)var4;
            var8 = false;
            Collection var35 = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault(var29, 10)));
            boolean var11 = false;
            Iterator var38 = var29.iterator();

            while(var38.hasNext()) {
               Object var39 = var38.next();
               Pair var40 = (Pair)var39;
               boolean var15 = false;
               var35.add((File)var40.getFirst());
            }

            List var32 = (List)var35;
            boolean var33 = false;
            JvmDependency var28 = new JvmDependency(var32);
            Key var10001 = ScriptCompilationKt.getDependencies((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion);
            JvmDependency[] var30 = new JvmDependency[]{var28};
            var3.append(var10001, var30);
            return Unit.INSTANCE;
         }
      }
   }

   private static final List resolveDependencies$lambda$23$lambda$22$lambda$21(Options var0, List var1) {
      Intrinsics.checkNotNullParameter(var1, "files");
      Iterable var2 = (Iterable)var1;
      boolean var3 = false;
      Collection var5 = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault(var2, 10)));
      boolean var6 = false;
      Iterator var7 = var2.iterator();

      while(var7.hasNext()) {
         Object var8 = var7.next();
         File var9 = (File)var8;
         boolean var10 = false;
         Boolean var10001 = getShared(var0);
         var5.add(TuplesKt.to(var9, var10001 != null ? var10001 : false));
      }

      return (List)var5;
   }

   private static final CharSequence valueOrThrow$lambda$25$lambda$24(ScriptDiagnostic var0) {
      Intrinsics.checkNotNullParameter(var0, "r");
      return (CharSequence)ScriptDiagnostic.render$default(var0, false, false, false, true, 7, (Object)null);
   }

   private static final Unit configure$lambda$28$lambda$27$lambda$26(ScriptConfigurationRefinementContext var0, Builder var1) {
      Intrinsics.checkNotNullParameter(var1, "$this$with");
      Boolean var10000 = (Boolean)var0.getCompilationConfiguration().get(getServer((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion));
      if (var10000 != null ? var10000 : false) {
         Key var10001 = ScriptCompilationKt.getDependencies((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion);
         JvmDependencyFromClassLoader[] var2 = new JvmDependencyFromClassLoader[1];
         Object var10004 = var1.get(getSharedClassloader((ScriptCompilationConfigurationKeys)ScriptCompilationConfiguration.Companion));
         Intrinsics.checkNotNull(var10004);
         var2[0] = ((SharedClassLoader)var10004).getDependency();
         var1.append(var10001, var2);
      }

      return Unit.INSTANCE;
   }

   private static final ResultWithDiagnostics configure$lambda$28$lambda$27(ScriptConfigurationRefinementContext var0) {
      Intrinsics.checkNotNullParameter(var0, "context");

      ResultWithDiagnostics var1;
      try {
         var1 = (ResultWithDiagnostics)ErrorHandlingKt.asSuccess$default(ScriptCompilationKt.with(var0.getCompilationConfiguration(), UtilsKt::configure$lambda$28$lambda$27$lambda$26), (List)null, 1, (Object)null);
      } catch (Throwable var4) {
         ScriptDiagnostic[] var3 = new ScriptDiagnostic[]{ErrorHandlingKt.asDiagnostics$default(var4, 0, (String)null, (String)null, (Location)null, (Severity)null, 31, (Object)null)};
         var1 = (ResultWithDiagnostics)(new Failure(var3));
      }

      return var1;
   }

   private static final Unit configure$lambda$28(RefineConfigurationBuilder var0) {
      Intrinsics.checkNotNullParameter(var0, "$this$refineConfiguration");
      var0.beforeParsing(UtilsKt::configure$lambda$28$lambda$27);
      KClass[] var1 = new KClass[]{Reflection.getOrCreateKotlinClass(DependsOn.class), Reflection.getOrCreateKotlinClass(Repository.class)};
      var0.onAnnotations(var1, (Function1)null.INSTANCE);
      return Unit.INSTANCE;
   }

   static {
      KProperty[] var0 = new KProperty[]{Reflection.property1((PropertyReference1)(new PropertyReference1Impl(UtilsKt.class, "dependencyResolver", "getDependencyResolver(Lkotlin/script/experimental/api/ScriptCompilationConfigurationKeys;)Lkotlin/script/experimental/util/PropertiesCollection$Key;", 1))), Reflection.property1((PropertyReference1)(new PropertyReference1Impl(UtilsKt.class, "packDirectory", "getPackDirectory(Lkotlin/script/experimental/api/ScriptCompilationConfigurationKeys;)Lkotlin/script/experimental/util/PropertiesCollection$Key;", 1))), Reflection.property1((PropertyReference1)(new PropertyReference1Impl(UtilsKt.class, "sharedClassloader", "getSharedClassloader(Lkotlin/script/experimental/api/ScriptCompilationConfigurationKeys;)Lkotlin/script/experimental/util/PropertiesCollection$Key;", 1))), Reflection.property1((PropertyReference1)(new PropertyReference1Impl(UtilsKt.class, "server", "getServer(Lkotlin/script/experimental/api/ScriptCompilationConfigurationKeys;)Lkotlin/script/experimental/util/PropertiesCollection$Key;", 1)))};
      $$delegatedProperties = var0;
      workDir = FilesKt.normalize(new File("."));
      resolver = createResolver$default((File)null, 1, (Object)null);
      loader = new SharedClassLoader((ClassLoader)null, 1, (DefaultConstructorMarker)null);
      dependencyResolver$delegate = PropertiesCollection.Companion.key(resolver, true);
      packDirectory$delegate = PropertiesCollection.Companion.key((Object)null, true);
      sharedClassloader$delegate = PropertiesCollection.Companion.key((Object)null, true);
      server$delegate = PropertiesCollection.Companion.key(false, true);
   }
}
