package com.volmit.iris.core.safeguard.task;

import com.volmit.iris.util.format.C;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001:\u0001\u001fB'\b\u0007\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0004\b\b\u0010\tJ\u001c\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u0013H\u0007J\u001a\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u0013J\b\u0010\u0016\u001a\u00020\u0005H\u0016J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0007HÆ\u0003J)\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÆ\u0001J\u0013\u0010\u001b\u001a\u00020\u00132\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006 "},
   d2 = {"Lcom/volmit/iris/core/safeguard/task/Diagnostic;", "", "logger", "Lcom/volmit/iris/core/safeguard/task/Diagnostic$Logger;", "message", "", "exception", "", "<init>", "(Lcom/volmit/iris/core/safeguard/task/Diagnostic$Logger;Ljava/lang/String;Ljava/lang/Throwable;)V", "getLogger", "()Lcom/volmit/iris/core/safeguard/task/Diagnostic$Logger;", "getMessage", "()Ljava/lang/String;", "getException", "()Ljava/lang/Throwable;", "log", "", "withException", "", "withStackTrace", "render", "toString", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "Logger", "core"}
)
public final class Diagnostic {
   @NotNull
   private final Diagnostic.Logger logger;
   @NotNull
   private final String message;
   @Nullable
   private final Throwable exception;

   @JvmOverloads
   public Diagnostic(@NotNull Diagnostic.Logger var1, @NotNull String var2, @Nullable Throwable var3) {
      Intrinsics.checkNotNullParameter(var1, "logger");
      Intrinsics.checkNotNullParameter(var2, "message");
      super();
      this.logger = var1;
      this.message = var2;
      this.exception = var3;
   }

   // $FF: synthetic method
   public Diagnostic(Diagnostic.Logger var1, String var2, Throwable var3, int var4, DefaultConstructorMarker var5) {
      if ((var4 & 1) != 0) {
         var1 = Diagnostic.Logger.ERROR;
      }

      if ((var4 & 4) != 0) {
         var3 = null;
      }

      this(var1, var2, var3);
   }

   @NotNull
   public final Diagnostic.Logger getLogger() {
      return this.logger;
   }

   @NotNull
   public final String getMessage() {
      return this.message;
   }

   @Nullable
   public final Throwable getException() {
      return this.exception;
   }

   @JvmOverloads
   public final void log(boolean var1, boolean var2) {
      this.logger.print(this.render(var1, var2));
   }

   // $FF: synthetic method
   public static void log$default(Diagnostic var0, boolean var1, boolean var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = true;
      }

      if ((var3 & 2) != 0) {
         var2 = false;
      }

      var0.log(var1, var2);
   }

   @NotNull
   public final String render(boolean var1, boolean var2) {
      StringBuilder var3 = new StringBuilder();
      StringBuilder var4 = var3;
      boolean var5 = false;
      var3.append(this.message);
      if (var1 && this.exception != null) {
         var3.append(": ");
         var3.append(this.exception);
         if (var2) {
            Closeable var6 = (Closeable)(new ByteArrayOutputStream());
            Throwable var7 = null;

            try {
               ByteArrayOutputStream var8 = (ByteArrayOutputStream)var6;
               boolean var9 = false;
               PrintStream var10 = new PrintStream((OutputStream)var8);
               this.exception.printStackTrace(var10);
               var10.flush();
               var4.append("\n");
               var4.append(var8.toString());
               Unit var15 = Unit.INSTANCE;
            } catch (Throwable var13) {
               var7 = var13;
               throw var13;
            } finally {
               CloseableKt.closeFinally(var6, var7);
            }
         }
      }

      return var3.toString();
   }

   // $FF: synthetic method
   public static String render$default(Diagnostic var0, boolean var1, boolean var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = true;
      }

      if ((var3 & 2) != 0) {
         var2 = false;
      }

      return var0.render(var1, var2);
   }

   @NotNull
   public String toString() {
      String var10000 = C.strip(render$default(this, false, false, 3, (Object)null));
      Intrinsics.checkNotNullExpressionValue(var10000, "strip(...)");
      return var10000;
   }

   @NotNull
   public final Diagnostic.Logger component1() {
      return this.logger;
   }

   @NotNull
   public final String component2() {
      return this.message;
   }

   @Nullable
   public final Throwable component3() {
      return this.exception;
   }

   @NotNull
   public final Diagnostic copy(@NotNull Diagnostic.Logger var1, @NotNull String var2, @Nullable Throwable var3) {
      Intrinsics.checkNotNullParameter(var1, "logger");
      Intrinsics.checkNotNullParameter(var2, "message");
      return new Diagnostic(var1, var2, var3);
   }

   // $FF: synthetic method
   public static Diagnostic copy$default(Diagnostic var0, Diagnostic.Logger var1, String var2, Throwable var3, int var4, Object var5) {
      if ((var4 & 1) != 0) {
         var1 = var0.logger;
      }

      if ((var4 & 2) != 0) {
         var2 = var0.message;
      }

      if ((var4 & 4) != 0) {
         var3 = var0.exception;
      }

      return var0.copy(var1, var2, var3);
   }

   public int hashCode() {
      int var1 = this.logger.hashCode();
      var1 = var1 * 31 + this.message.hashCode();
      var1 = var1 * 31 + (this.exception == null ? 0 : this.exception.hashCode());
      return var1;
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Diagnostic)) {
         return false;
      } else {
         Diagnostic var2 = (Diagnostic)var1;
         if (this.logger != var2.logger) {
            return false;
         } else if (!Intrinsics.areEqual(this.message, var2.message)) {
            return false;
         } else {
            return Intrinsics.areEqual(this.exception, var2.exception);
         }
      }
   }

   @JvmOverloads
   public Diagnostic(@NotNull Diagnostic.Logger var1, @NotNull String var2) {
      Intrinsics.checkNotNullParameter(var1, "logger");
      Intrinsics.checkNotNullParameter(var2, "message");
      this(var1, var2, (Throwable)null, 4, (DefaultConstructorMarker)null);
   }

   @JvmOverloads
   public Diagnostic(@NotNull String var1) {
      Intrinsics.checkNotNullParameter(var1, "message");
      this((Diagnostic.Logger)null, var1, (Throwable)null, 5, (DefaultConstructorMarker)null);
   }

   @JvmOverloads
   public final void log(boolean var1) {
      log$default(this, var1, false, 2, (Object)null);
   }

   @JvmOverloads
   public final void log() {
      log$default(this, false, false, 3, (Object)null);
   }

   @Metadata(
      mv = {2, 2, 0},
      k = 1,
      xi = 48,
      d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u001d\b\u0002\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0004J\u001a\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\u00042\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004¢\u0006\u0002\n\u0000j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f¨\u0006\u0013"},
      d2 = {"Lcom/volmit/iris/core/safeguard/task/Diagnostic$Logger;", "", "logger", "Lkotlin/Function1;", "", "", "<init>", "(Ljava/lang/String;ILkotlin/jvm/functions/Function1;)V", "DEBUG", "RAW", "INFO", "WARN", "ERROR", "print", "message", "create", "Lcom/volmit/iris/core/safeguard/task/Diagnostic;", "exception", "", "core"}
   )
   @SourceDebugExtension({"SMAP\nValueWithDiagnostics.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ValueWithDiagnostics.kt\ncom/volmit/iris/core/safeguard/task/Diagnostic$Logger\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,74:1\n1869#2,2:75\n*S KotlinDebug\n*F\n+ 1 ValueWithDiagnostics.kt\ncom/volmit/iris/core/safeguard/task/Diagnostic$Logger\n*L\n38#1:75,2\n*E\n"})
   public static enum Logger {
      @NotNull
      private final Function1<String, Unit> logger;
      DEBUG((Function1)null.INSTANCE),
      RAW((Function1)null.INSTANCE),
      INFO((Function1)null.INSTANCE),
      WARN((Function1)null.INSTANCE),
      ERROR((Function1)null.INSTANCE);

      // $FF: synthetic field
      private static final EnumEntries $ENTRIES = EnumEntriesKt.enumEntries((Enum[])$VALUES);

      private Logger(Function1<? super String, Unit> var3) {
         this.logger = var3;
      }

      public final void print(@NotNull String var1) {
         Intrinsics.checkNotNullParameter(var1, "message");
         CharSequence var10000 = (CharSequence)var1;
         char[] var2 = new char[]{'\n'};
         Iterable var7 = (Iterable)StringsKt.split$default(var10000, var2, false, 0, 6, (Object)null);
         Function1 var3 = this.logger;
         boolean var4 = false;
         Iterator var5 = var7.iterator();

         while(var5.hasNext()) {
            Object var6 = var5.next();
            var3.invoke(var6);
         }

      }

      @NotNull
      public final Diagnostic create(@NotNull String var1, @Nullable Throwable var2) {
         Intrinsics.checkNotNullParameter(var1, "message");
         return new Diagnostic(this, var1, var2);
      }

      // $FF: synthetic method
      public static Diagnostic create$default(Diagnostic.Logger var0, String var1, Throwable var2, int var3, Object var4) {
         if (var4 != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: create");
         } else {
            if ((var3 & 2) != 0) {
               var2 = null;
            }

            return var0.create(var1, var2);
         }
      }

      @NotNull
      public static EnumEntries<Diagnostic.Logger> getEntries() {
         return $ENTRIES;
      }

      // $FF: synthetic method
      private static final Diagnostic.Logger[] $values() {
         Diagnostic.Logger[] var0 = new Diagnostic.Logger[]{DEBUG, RAW, INFO, WARN, ERROR};
         return var0;
      }
   }
}
