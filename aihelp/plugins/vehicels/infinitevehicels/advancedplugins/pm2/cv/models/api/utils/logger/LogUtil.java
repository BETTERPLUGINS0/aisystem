package advancedplugins.pm2.cv.models.api.utils.logger;

import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Generated;

public class LogUtil {
   public static Logger logger;

   public static void log() {
      log("");
   }

   public static void log(String var0) {
      log(1, (String)var0);
   }

   public static void log(int var0, String var1) {
      if (ConfigProperty.DEBUG_LEVEL.getInt() >= var0) {
         logger.log(Level.INFO, var1 + String.valueOf(LogUtil.LogColor.RESET));
      }

   }

   public static void log(Object var0) {
      log(var0 == null ? "null" : var0.toString());
   }

   public static <T> void log(Iterable<T> var0) {
      log(1, (Iterable)var0);
   }

   public static <T> void log(Iterable<T> var0, Function<T, String> var1) {
      log(1, (Iterable)var0, var1);
   }

   public static <T> void log(int var0, Iterable<T> var1) {
      log(var0, var1, Object::toString);
   }

   public static <T> void log(int var0, Iterable<T> var1, Function<T, String> var2) {
      String var3 = var1.getClass().getSimpleName();
      StringBuilder var4 = new StringBuilder();
      var4.append(var3).append(":[");
      boolean var5 = true;

      Object var6;
      for(Iterator var7 = var1.iterator(); var7.hasNext(); var4.append((String)var2.apply(var6))) {
         var6 = var7.next();
         if (!var5) {
            var4.append(", ");
         } else {
            var5 = false;
         }
      }

      var4.append("]");
      log(var0, var4.toString());
   }

   public static <T> void log(T[] var0) {
      log(1, (Object[])var0);
   }

   public static <T> void log(T[] var0, Function<T, String> var1) {
      log(1, (Object[])var0, var1);
   }

   public static <T> void log(int var0, T[] var1) {
      log(var0, var1, Objects::toString);
   }

   public static <T> void log(int var0, T[] var1, Function<T, String> var2) {
      String var3 = var1.getClass().getSimpleName();
      StringBuilder var4 = new StringBuilder();
      var4.append(var3).append(":[");
      boolean var5 = true;
      Object[] var6 = var1;
      int var7 = var1.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Object var9 = var6[var8];
         if (!var5) {
            var4.append(", ");
         } else {
            var5 = false;
         }

         var4.append((String)var2.apply(var9));
      }

      var4.append("]");
      log(var0, var4.toString());
   }

   public static void stacktrace() {
      if (isDebugEnabled()) {
         log((Object[])Thread.currentThread().getStackTrace(), (var0) -> {
            return "\n" + String.valueOf(var0);
         });
      }

   }

   public static void debug() {
      if (isDebugEnabled()) {
         log();
      }

   }

   public static void debug(String var0) {
      if (isDebugEnabled()) {
         log(var0);
      }

   }

   public static void debug(int var0, String var1) {
      if (isDebugEnabled()) {
         log(var0, var1);
      }

   }

   public static void debug(Object var0) {
      if (isDebugEnabled()) {
         log(var0);
      }

   }

   public static <T> void debug(Iterable<T> var0) {
      debug(var0, Objects::toString);
   }

   public static <T> void debug(Iterable<T> var0, Function<T, String> var1) {
      if (isDebugEnabled()) {
         log(var0, var1);
      }

   }

   public static <T> void debug(T[] var0) {
      debug(var0, Objects::toString);
   }

   public static <T> void debug(T[] var0, Function<T, String> var1) {
      if (isDebugEnabled()) {
         log(var0, var1);
      }

   }

   private static boolean isDebugEnabled() {
      return ConfigProperty.DEBUG_LEVEL.getInt() == 157;
   }

   public static void warn(String var0) {
      warn(1, var0);
   }

   public static void warn(int var0, String var1) {
      if (ConfigProperty.DEBUG_LEVEL.getInt() >= var0) {
         Logger var10000 = logger;
         Level var10001 = Level.WARNING;
         String var10002 = String.valueOf(LogUtil.LogColor.YELLOW);
         var10000.log(var10001, var10002 + var1 + String.valueOf(LogUtil.LogColor.RESET));
      }

   }

   public static void error(String var0) {
      error(1, var0);
   }

   public static void error(int var0, String var1) {
      if (ConfigProperty.DEBUG_LEVEL.getInt() >= var0) {
         Logger var10000 = logger;
         Level var10001 = Level.SEVERE;
         String var10002 = String.valueOf(LogUtil.LogColor.DARK_RED);
         var10000.log(var10001, var10002 + var1 + String.valueOf(LogUtil.LogColor.RESET));
      }

   }

   public static enum LogColor {
      BLACK("\u001b[30m"),
      RED("\u001b[31m"),
      GREEN("\u001b[32m"),
      YELLOW("\u001b[33m"),
      BLUE("\u001b[34m"),
      PURPLE("\u001b[35m"),
      CYAN("\u001b[36m"),
      WHITE("\u001b[37m"),
      BRIGHT_GREEN("\u001b[38;5;46m"),
      RESET("\u001b[0m"),
      BOLD("\u001b[1m"),
      ITALICS("\u001b[2m"),
      UNDERLINE("\u001b[4m"),
      DARK_RED("\u001b[38;5;124m"),
      DARK_GREEN("\u001b[38;5;46m"),
      GOLD("\u001b[38;5;220m");

      private final String ansiColor;

      private LogColor(String param3) {
         this.ansiColor = var3;
      }

      public String toString() {
         return this.ansiColor;
      }

      @Generated
      public String getAnsiColor() {
         return this.ansiColor;
      }

      // $FF: synthetic method
      private static LogUtil.LogColor[] $values() {
         return new LogUtil.LogColor[]{BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, BRIGHT_GREEN, RESET, BOLD, ITALICS, UNDERLINE, DARK_RED, DARK_GREEN, GOLD};
      }
   }
}
