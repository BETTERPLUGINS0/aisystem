package com.volmit.iris.core.safeguard.task;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 2,
   xi = 48,
   d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\u001a/\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0002H\u00022\u0012\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u0004\"\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a)\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0002H\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007¢\u0006\u0002\u0010\b¨\u0006\t"},
   d2 = {"withDiagnostics", "Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "T", "diagnostics", "", "Lcom/volmit/iris/core/safeguard/task/Diagnostic;", "(Ljava/lang/Object;[Lcom/volmit/iris/core/safeguard/task/Diagnostic;)Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "", "(Ljava/lang/Object;Ljava/util/List;)Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "core"}
)
public final class ValueWithDiagnosticsKt {
   @NotNull
   public static final <T> ValueWithDiagnostics<T> withDiagnostics(T var0, @NotNull Diagnostic... var1) {
      Intrinsics.checkNotNullParameter(var1, "diagnostics");
      return new ValueWithDiagnostics(var0, ArraysKt.toList(var1));
   }

   @NotNull
   public static final <T> ValueWithDiagnostics<T> withDiagnostics(T var0, @NotNull List<Diagnostic> var1) {
      Intrinsics.checkNotNullParameter(var1, "diagnostics");
      return new ValueWithDiagnostics(var0, var1);
   }
}
