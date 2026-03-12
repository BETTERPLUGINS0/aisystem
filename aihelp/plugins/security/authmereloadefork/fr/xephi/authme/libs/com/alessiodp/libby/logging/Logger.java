package fr.xephi.authme.libs.com.alessiodp.libby.logging;

import fr.xephi.authme.libs.com.alessiodp.libby.logging.adapters.LogAdapter;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Logger {
   private final LogAdapter adapter;
   private LogLevel level;

   public Logger(@NotNull LogAdapter adapter) {
      this.level = LogLevel.INFO;
      this.adapter = (LogAdapter)Objects.requireNonNull(adapter, "adapter");
   }

   @NotNull
   public LogLevel getLevel() {
      return this.level;
   }

   public void setLevel(@NotNull LogLevel level) {
      this.level = (LogLevel)Objects.requireNonNull(level, "level");
   }

   private boolean canLog(@NotNull LogLevel level) {
      return ((LogLevel)Objects.requireNonNull(level, "level")).compareTo(this.level) >= 0;
   }

   public void log(@NotNull LogLevel level, @Nullable String message) {
      if (this.canLog(level)) {
         this.adapter.log(level, message);
      }

   }

   public void log(@NotNull LogLevel level, @Nullable String message, @Nullable Throwable throwable) {
      if (this.canLog(level)) {
         this.adapter.log(level, message, throwable);
      }

   }

   public void debug(String message) {
      this.log(LogLevel.DEBUG, message);
   }

   public void debug(@Nullable String message, @Nullable Throwable throwable) {
      this.log(LogLevel.DEBUG, message, throwable);
   }

   public void info(@Nullable String message) {
      this.log(LogLevel.INFO, message);
   }

   public void info(@Nullable String message, @Nullable Throwable throwable) {
      this.log(LogLevel.INFO, message, throwable);
   }

   public void warn(@Nullable String message) {
      this.log(LogLevel.WARN, message);
   }

   public void warn(@Nullable String message, @Nullable Throwable throwable) {
      this.log(LogLevel.WARN, message, throwable);
   }

   public void error(@Nullable String message) {
      this.log(LogLevel.ERROR, message);
   }

   public void error(@Nullable String message, @Nullable Throwable throwable) {
      this.log(LogLevel.ERROR, message, throwable);
   }
}
