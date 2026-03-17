package me.PM2.infinitevehicles.libby.logging;

import java.util.Objects;
import me.PM2.infinitevehicles.libby.logging.adapters.LogAdapter;

public class Logger {
   private final LogAdapter adapter;
   private LogLevel level;

   public Logger(LogAdapter adapter) {
      this.level = LogLevel.INFO;
      this.adapter = (LogAdapter)Objects.requireNonNull(var1, "adapter");
   }

   public LogLevel getLevel() {
      return this.level;
   }

   public void setLevel(LogLevel level) {
      this.level = (LogLevel)Objects.requireNonNull(var1, "level");
   }

   private boolean canLog(LogLevel level) {
      return ((LogLevel)Objects.requireNonNull(var1, "level")).compareTo(this.level) >= 0;
   }

   public void log(LogLevel level, String message) {
      if (this.canLog(var1)) {
         this.adapter.log(var1, var2);
      }

   }

   public void log(LogLevel level, String message, Throwable throwable) {
      if (this.canLog(var1)) {
         this.adapter.log(var1, var2, var3);
      }

   }

   public void debug(String message) {
      this.log(LogLevel.DEBUG, var1);
   }

   public void debug(String message, Throwable throwable) {
      this.log(LogLevel.DEBUG, var1, var2);
   }

   public void info(String message) {
      this.log(LogLevel.INFO, var1);
   }

   public void info(String message, Throwable throwable) {
      this.log(LogLevel.INFO, var1, var2);
   }

   public void warn(String message) {
      this.log(LogLevel.WARN, var1);
   }

   public void warn(String message, Throwable throwable) {
      this.log(LogLevel.WARN, var1, var2);
   }

   public void error(String message) {
      this.log(LogLevel.ERROR, var1);
   }

   public void error(String message, Throwable throwable) {
      this.log(LogLevel.ERROR, var1, var2);
   }
}
