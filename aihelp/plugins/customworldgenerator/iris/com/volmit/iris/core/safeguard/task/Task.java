package com.volmit.iris.core.safeguard.task;

import com.volmit.iris.core.safeguard.Mode;
import com.volmit.iris.util.format.Form;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.properties.PropertyDelegateProvider;
import kotlin.properties.ReadOnlyProperty;
import kotlin.reflect.KProperty;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH&R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u000e"},
   d2 = {"Lcom/volmit/iris/core/safeguard/task/Task;", "", "id", "", "name", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "getName", "run", "Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "Lcom/volmit/iris/core/safeguard/Mode;", "Companion", "core"}
)
public abstract class Task {
   @NotNull
   public static final Task.Companion Companion = new Task.Companion((DefaultConstructorMarker)null);
   @NotNull
   private final String id;
   @NotNull
   private final String name;

   public Task(@NotNull String var1, @NotNull String var2) {
      Intrinsics.checkNotNullParameter(var1, "id");
      Intrinsics.checkNotNullParameter(var2, "name");
      super();
      this.id = var1;
      this.name = var2;
   }

   // $FF: synthetic method
   public Task(String var1, String var2, int var3, DefaultConstructorMarker var4) {
      if ((var3 & 2) != 0) {
         String var10000 = StringsKt.replace$default(var1, " ", "_", false, 4, (Object)null).toLowerCase(Locale.ROOT);
         Intrinsics.checkNotNullExpressionValue(var10000, "toLowerCase(...)");
         var10000 = Form.capitalizeWords(var10000);
         Intrinsics.checkNotNullExpressionValue(var10000, "capitalizeWords(...)");
         var2 = var10000;
      }

      this(var1, var2);
   }

   @NotNull
   public final String getId() {
      return this.id;
   }

   @NotNull
   public final String getName() {
      return this.name;
   }

   @NotNull
   public abstract ValueWithDiagnostics<Mode> run();

   @Metadata(
      mv = {2, 2, 0},
      k = 1,
      xi = 48,
      d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J,\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\u0012\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nJ\"\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0012\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nJ6\u0010\r\u001a\u001e\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0012\u0012\u0012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0012\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n¨\u0006\u0010"},
      d2 = {"Lcom/volmit/iris/core/safeguard/task/Task$Companion;", "", "<init>", "()V", "of", "Lcom/volmit/iris/core/safeguard/task/Task;", "id", "", "name", "action", "Lkotlin/Function0;", "Lcom/volmit/iris/core/safeguard/task/ValueWithDiagnostics;", "Lcom/volmit/iris/core/safeguard/Mode;", "task", "Lkotlin/properties/PropertyDelegateProvider;", "Lkotlin/properties/ReadOnlyProperty;", "core"}
   )
   public static final class Companion {
      private Companion() {
      }

      @NotNull
      public final Task of(@NotNull String var1, @NotNull String var2, @NotNull final Function0<? extends ValueWithDiagnostics<? extends Mode>> var3) {
         Intrinsics.checkNotNullParameter(var1, "id");
         Intrinsics.checkNotNullParameter(var2, "name");
         Intrinsics.checkNotNullParameter(var3, "action");
         return (Task)(new Task(var1, var2) {
            public ValueWithDiagnostics<Mode> run() {
               return (ValueWithDiagnostics)var3.invoke();
            }
         });
      }

      // $FF: synthetic method
      public static Task of$default(Task.Companion var0, String var1, String var2, Function0 var3, int var4, Object var5) {
         if ((var4 & 2) != 0) {
            var2 = var1;
         }

         return var0.of(var1, var2, var3);
      }

      @NotNull
      public final Task of(@NotNull String var1, @NotNull final Function0<? extends ValueWithDiagnostics<? extends Mode>> var2) {
         Intrinsics.checkNotNullParameter(var1, "id");
         Intrinsics.checkNotNullParameter(var2, "action");
         return (Task)(new Task(var1) {
            public ValueWithDiagnostics<Mode> run() {
               return (ValueWithDiagnostics)var2.invoke();
            }
         });
      }

      @NotNull
      public final PropertyDelegateProvider<Object, ReadOnlyProperty<Object, Task>> task(@NotNull Function0<? extends ValueWithDiagnostics<? extends Mode>> var1) {
         Intrinsics.checkNotNullParameter(var1, "action");
         return Task.Companion::task$lambda$1;
      }

      private static final Task task$lambda$1$lambda$0(Function0 var0, Object var1, KProperty var2) {
         Intrinsics.checkNotNullParameter(var2, "property");
         return Task.Companion.of(var2.getName(), var0);
      }

      private static final ReadOnlyProperty task$lambda$1(Function0 var0, Object var1, KProperty var2) {
         Intrinsics.checkNotNullParameter(var2, "<unused var>");
         return Task.Companion::task$lambda$1$lambda$0;
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
