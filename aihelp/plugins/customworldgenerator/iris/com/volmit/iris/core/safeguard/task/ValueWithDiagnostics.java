package com.volmit.iris.core.safeguard.task;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002B\u001d\u0012\u0006\u0010\u0003\u001a\u00028\u0000\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0004\b\u0007\u0010\bB%\b\u0016\u0012\u0006\u0010\u0003\u001a\u00028\u0000\u0012\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\t\"\u00020\u0006¢\u0006\u0004\b\u0007\u0010\nJ\u001c\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u0013H\u0007J\u000e\u0010\u0015\u001a\u00028\u0000HÆ\u0003¢\u0006\u0002\u0010\fJ\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0003J.\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00028\u00002\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0001¢\u0006\u0002\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u00132\b\u0010\u001a\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u001b\u001a\u00020\u001cHÖ\u0001J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001R\u0013\u0010\u0003\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001f"},
   d2 = {"Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "T", "", "value", "diagnostics", "", "Lcom/volmit/iris/core/safeguard/task/Diagnostic;", "<init>", "(Ljava/lang/Object;Ljava/util/List;)V", "", "(Ljava/lang/Object;[Lcom/volmit/iris/core/safeguard/task/Diagnostic;)V", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getDiagnostics", "()Ljava/util/List;", "log", "", "withException", "", "withStackTrace", "component1", "component2", "copy", "(Ljava/lang/Object;Ljava/util/List;)Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "equals", "other", "hashCode", "", "toString", "", "core"}
)
@SourceDebugExtension({"SMAP\nValueWithDiagnostics.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ValueWithDiagnostics.kt\ncom/volmit/iris/core/safeguard/task/ValueWithDiagnostics\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,74:1\n1869#2,2:75\n*S KotlinDebug\n*F\n+ 1 ValueWithDiagnostics.kt\ncom/volmit/iris/core/safeguard/task/ValueWithDiagnostics\n*L\n19#1:75,2\n*E\n"})
public final class ValueWithDiagnostics<T> {
   private final T value;
   @NotNull
   private final List<Diagnostic> diagnostics;

   public ValueWithDiagnostics(T var1, @NotNull List<Diagnostic> var2) {
      Intrinsics.checkNotNullParameter(var2, "diagnostics");
      super();
      this.value = var1;
      this.diagnostics = var2;
   }

   public final T getValue() {
      return this.value;
   }

   @NotNull
   public final List<Diagnostic> getDiagnostics() {
      return this.diagnostics;
   }

   public ValueWithDiagnostics(T var1, @NotNull Diagnostic... var2) {
      Intrinsics.checkNotNullParameter(var2, "diagnostics");
      this(var1, ArraysKt.toList(var2));
   }

   @JvmOverloads
   public final void log(boolean var1, boolean var2) {
      Iterable var3 = (Iterable)this.diagnostics;
      boolean var4 = false;
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         Diagnostic var7 = (Diagnostic)var6;
         boolean var8 = false;
         var7.log(var1, var2);
      }

   }

   // $FF: synthetic method
   public static void log$default(ValueWithDiagnostics var0, boolean var1, boolean var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = true;
      }

      if ((var3 & 2) != 0) {
         var2 = false;
      }

      var0.log(var1, var2);
   }

   public final T component1() {
      return this.value;
   }

   @NotNull
   public final List<Diagnostic> component2() {
      return this.diagnostics;
   }

   @NotNull
   public final ValueWithDiagnostics<T> copy(T var1, @NotNull List<Diagnostic> var2) {
      Intrinsics.checkNotNullParameter(var2, "diagnostics");
      return new ValueWithDiagnostics(var1, var2);
   }

   // $FF: synthetic method
   public static ValueWithDiagnostics copy$default(ValueWithDiagnostics var0, Object var1, List var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = var0.value;
      }

      if ((var3 & 2) != 0) {
         var2 = var0.diagnostics;
      }

      return var0.copy(var1, var2);
   }

   @NotNull
   public String toString() {
      return "ValueWithDiagnostics(value=" + this.value + ", diagnostics=" + this.diagnostics + ")";
   }

   public int hashCode() {
      int var1 = this.value == null ? 0 : this.value.hashCode();
      var1 = var1 * 31 + this.diagnostics.hashCode();
      return var1;
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ValueWithDiagnostics)) {
         return false;
      } else {
         ValueWithDiagnostics var2 = (ValueWithDiagnostics)var1;
         if (!Intrinsics.areEqual(this.value, var2.value)) {
            return false;
         } else {
            return Intrinsics.areEqual(this.diagnostics, var2.diagnostics);
         }
      }
   }

   @JvmOverloads
   public final void log(boolean var1) {
      log$default(this, var1, false, 2, (Object)null);
   }

   @JvmOverloads
   public final void log() {
      log$default(this, false, false, 3, (Object)null);
   }
}
