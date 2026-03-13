package com.volmit.iris.core.scripting.kotlin.runner.resolver;

import java.io.File;
import java.util.List;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.script.experimental.api.ResultWithDiagnostics;
import kotlin.script.experimental.api.SourceCode.LocationWithId;
import kotlin.script.experimental.dependencies.ArtifactWithLocation;
import kotlin.script.experimental.dependencies.ExternalDependenciesResolver;
import kotlin.script.experimental.dependencies.ExternalDependenciesResolver.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006À\u0006\u0003"},
   d2 = {"Lcom/volmit/iris/core/scripting/kotlin/runner/resolver/DependenciesResolver;", "Lkotlin/script/experimental/dependencies/ExternalDependenciesResolver;", "addPack", "", "directory", "Ljava/io/File;", "core"}
)
public interface DependenciesResolver extends ExternalDependenciesResolver {
   void addPack(@NotNull File var1);

   @Metadata(
      mv = {2, 2, 0},
      k = 3,
      xi = 48
   )
   public static final class DefaultImpls {
      /** @deprecated */
      @Deprecated
      @Nullable
      public static Object resolve(@NotNull DependenciesResolver var0, @NotNull String var1, @NotNull Options var2, @Nullable LocationWithId var3, @NotNull Continuation<? super ResultWithDiagnostics<? extends List<? extends File>>> var4) {
         return var0.resolve(var1, var2, var3, var4);
      }

      /** @deprecated */
      @Deprecated
      @Nullable
      public static Object resolve(@NotNull DependenciesResolver var0, @NotNull List<ArtifactWithLocation> var1, @NotNull Options var2, @NotNull Continuation<? super ResultWithDiagnostics<? extends List<? extends File>>> var3) {
         return var0.resolve(var1, var2, var3);
      }
   }
}
