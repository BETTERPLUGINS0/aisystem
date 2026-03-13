package com.volmit.iris.core.scripting.kotlin.runner.resolver;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.script.experimental.api.ErrorHandlingKt;
import kotlin.script.experimental.api.ResultWithDiagnostics;
import kotlin.script.experimental.api.ResultWithDiagnostics.Success;
import kotlin.script.experimental.api.SourceCode.LocationWithId;
import kotlin.script.experimental.dependencies.ArtifactWithLocation;
import kotlin.script.experimental.dependencies.RepositoryCoordinates;
import kotlin.script.experimental.dependencies.ExternalDependenciesResolver.Options;
import kotlin.script.experimental.dependencies.impl.ResolverUtilsKt;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\n\u001a\u0004\u0018\u00010\u0003*\u00020\u000bH\u0002J\u000e\u0010\f\u001a\u0004\u0018\u00010\u0003*\u00020\rH\u0002J(\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J4\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00160\u000f2\u0006\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0096@¢\u0006\u0002\u0010\u0018J\u0010\u0010\u0019\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\u000bH\u0016J\u0010\u0010\u001a\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\rH\u0016J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000RN\u0010\u0006\u001aB\u0012\f\u0012\n \b*\u0004\u0018\u00010\u00030\u0003\u0012\f\u0012\n \b*\u0004\u0018\u00010\t0\t \b* \u0012\f\u0012\n \b*\u0004\u0018\u00010\u00030\u0003\u0012\f\u0012\n \b*\u0004\u0018\u00010\t0\t\u0018\u00010\u00070\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/runner/resolver/FileDependenciesResolver;", "Lcom/volmit/iris/core/scripting/kotlin/runner/resolver/DependenciesResolver;", "baseDir", "Ljava/io/File;", "<init>", "(Ljava/io/File;)V", "localRepos", "Ljava/util/concurrent/ConcurrentHashMap$KeySetView;", "kotlin.jvm.PlatformType", "", "toRepositoryFileOrNull", "", "toFilePath", "Lkotlin/script/experimental/dependencies/RepositoryCoordinates;", "addRepository", "Lkotlin/script/experimental/api/ResultWithDiagnostics;", "repositoryCoordinates", "options", "Lkotlin/script/experimental/dependencies/ExternalDependenciesResolver$Options;", "sourceCodeLocation", "Lkotlin/script/experimental/api/SourceCode$LocationWithId;", "resolve", "", "artifactCoordinates", "(Ljava/lang/String;Lkotlin/script/experimental/dependencies/ExternalDependenciesResolver$Options;Lkotlin/script/experimental/api/SourceCode$LocationWithId;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "acceptsArtifact", "acceptsRepository", "addPack", "", "directory", "core"}
)
@SourceDebugExtension({"SMAP\nFileDependenciesResolver.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileDependenciesResolver.kt\ncom/volmit/iris/core/scripting/kotlin/runner/resolver/FileDependenciesResolver\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,69:1\n1#2:70\n*E\n"})
public final class FileDependenciesResolver implements DependenciesResolver {
   @NotNull
   private final File baseDir;
   private final KeySetView<File, Boolean> localRepos;

   public FileDependenciesResolver(@NotNull File var1) {
      Intrinsics.checkNotNullParameter(var1, "baseDir");
      super();
      this.baseDir = var1;
      KeySetView var2 = ConcurrentHashMap.newKeySet(1);
      boolean var4 = false;
      var2.add(this.baseDir);
      this.localRepos = var2;
   }

   private final File toRepositoryFileOrNull(String var1) {
      File var2 = new File(this.baseDir, var1);
      boolean var4 = false;
      return var2.exists() && var2.isDirectory() ? var2 : null;
   }

   private final File toFilePath(RepositoryCoordinates var1) {
      FileDependenciesResolver var10000 = this;
      URL var10001 = ResolverUtilsKt.toRepositoryUrlOrNull(var1);
      String var7;
      if (var10001 != null) {
         URL var2 = var10001;
         boolean var4 = false;
         boolean var6 = Intrinsics.areEqual(var2.getProtocol(), "file");
         var10000 = this;
         var10001 = var6 ? var2 : null;
         if ((var6 ? var2 : null) != null) {
            var7 = var10001.getPath();
            if (var7 != null) {
               return var10000.toRepositoryFileOrNull(var7);
            }
         }
      }

      var7 = var1.getString();
      return var10000.toRepositoryFileOrNull(var7);
   }

   @NotNull
   public ResultWithDiagnostics<Boolean> addRepository(@NotNull RepositoryCoordinates var1, @NotNull Options var2, @Nullable LocationWithId var3) {
      Intrinsics.checkNotNullParameter(var1, "repositoryCoordinates");
      Intrinsics.checkNotNullParameter(var2, "options");
      if (!this.acceptsRepository(var1)) {
         return (ResultWithDiagnostics)ErrorHandlingKt.asSuccess$default(false, (List)null, 1, (Object)null);
      } else {
         File var10000 = this.toFilePath(var1);
         if (var10000 == null) {
            return (ResultWithDiagnostics)ResolverUtilsKt.makeResolveFailureResult("Invalid repository location: '" + var1 + "'", var3);
         } else {
            File var4 = var10000;
            this.localRepos.add(var4);
            return (ResultWithDiagnostics)ErrorHandlingKt.asSuccess$default(true, (List)null, 1, (Object)null);
         }
      }
   }

   @Nullable
   public Object resolve(@NotNull String var1, @NotNull Options var2, @Nullable LocationWithId var3, @NotNull Continuation<? super ResultWithDiagnostics<? extends List<? extends File>>> var4) {
      if (!this.acceptsArtifact(var1)) {
         throw new IllegalArgumentException("Path is invalid");
      } else {
         List var5 = (List)(new ArrayList());
         Iterator var10000 = this.localRepos.iterator();
         Intrinsics.checkNotNullExpressionValue(var10000, "iterator(...)");
         Iterator var6 = var10000;

         while(var6.hasNext()) {
            File var7 = (File)var6.next();
            File var8 = new File(var7, var1);
            if (!var8.exists()) {
               var5.add("File '" + var8 + "' not found");
            } else {
               if (var8.isFile() || var8.isDirectory()) {
                  return new Success(CollectionsKt.listOf(var8), (List)null, 2, (DefaultConstructorMarker)null);
               }

               var5.add("Path '" + var8 + "' is neither file nor directory");
            }
         }

         return ResolverUtilsKt.makeResolveFailureResult(CollectionsKt.joinToString$default((Iterable)var5, (CharSequence)"; ", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null), var3);
      }
   }

   public boolean acceptsArtifact(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "artifactCoordinates");
      return !StringsKt.isBlank((CharSequence)var1);
   }

   public boolean acceptsRepository(@NotNull RepositoryCoordinates var1) {
      Intrinsics.checkNotNullParameter(var1, "repositoryCoordinates");
      return this.toFilePath(var1) != null;
   }

   public void addPack(@NotNull File var1) {
      Intrinsics.checkNotNullParameter(var1, "directory");
      this.localRepos.add(var1);
   }

   @Nullable
   public Object resolve(@NotNull List<ArtifactWithLocation> var1, @NotNull Options var2, @NotNull Continuation<? super ResultWithDiagnostics<? extends List<? extends File>>> var3) {
      return DependenciesResolver.super.resolve(var1, var2, var3);
   }
}
