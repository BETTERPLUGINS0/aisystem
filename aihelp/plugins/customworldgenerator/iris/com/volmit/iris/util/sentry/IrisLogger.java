package com.volmit.iris.util.sentry;

import com.volmit.iris.Iris;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IrisLogger implements ILogger {
   public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Object... args) {
      Iris.msg(String.format("%s: %s", var1, String.format(var2, var3)));
   }

   public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Throwable throwable) {
      if (var3 == null) {
         this.log(var1, var2);
      } else {
         Iris.msg(String.format("%s: %s\n%s", var1, String.format(var2, var3), this.captureStackTrace(var3)));
      }

   }

   public void log(@NotNull SentryLevel level, @Nullable Throwable throwable, @NotNull String message, @Nullable Object... args) {
      if (var2 == null) {
         this.log(var1, var3, var4);
      } else {
         Iris.msg(String.format("%s: %s\n%s", var1, String.format(var3, var2), this.captureStackTrace(var2)));
      }

   }

   public boolean isEnabled(@Nullable SentryLevel level) {
      return true;
   }

   @NotNull
   private String captureStackTrace(@NotNull Throwable throwable) {
      StringWriter var2 = new StringWriter();
      PrintWriter var3 = new PrintWriter(var2);
      var1.printStackTrace(var3);
      return var2.toString();
   }
}
