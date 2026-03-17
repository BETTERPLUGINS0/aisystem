package me.PM2.infinitevehicles.libby.logging.adapters;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.PM2.infinitevehicles.libby.logging.LogLevel;

public class JDKLogAdapter implements LogAdapter {
   private final Logger logger;

   public JDKLogAdapter(Logger logger) {
      this.logger = (Logger)Objects.requireNonNull(var1, "logger");
   }

   public void log(LogLevel level, String message) {
      switch((LogLevel)Objects.requireNonNull(var1, "level")) {
      case DEBUG:
         this.logger.log(Level.FINE, var2);
         break;
      case INFO:
         this.logger.log(Level.INFO, var2);
         break;
      case WARN:
         this.logger.log(Level.WARNING, var2);
         break;
      case ERROR:
         this.logger.log(Level.SEVERE, var2);
      }

   }

   public void log(LogLevel level, String message, Throwable throwable) {
      switch((LogLevel)Objects.requireNonNull(var1, "level")) {
      case DEBUG:
         this.logger.log(Level.FINE, var2, var3);
         break;
      case INFO:
         this.logger.log(Level.INFO, var2, var3);
         break;
      case WARN:
         this.logger.log(Level.WARNING, var2, var3);
         break;
      case ERROR:
         this.logger.log(Level.SEVERE, var2, var3);
      }

   }
}
