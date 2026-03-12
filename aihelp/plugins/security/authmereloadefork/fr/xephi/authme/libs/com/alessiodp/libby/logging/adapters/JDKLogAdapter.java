package fr.xephi.authme.libs.com.alessiodp.libby.logging.adapters;

import fr.xephi.authme.libs.com.alessiodp.libby.logging.LogLevel;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JDKLogAdapter implements LogAdapter {
   private final Logger logger;

   public JDKLogAdapter(@NotNull Logger logger) {
      this.logger = (Logger)Objects.requireNonNull(logger, "logger");
   }

   public void log(@NotNull LogLevel level, @Nullable String message) {
      switch((LogLevel)Objects.requireNonNull(level, "level")) {
      case DEBUG:
         this.logger.log(Level.FINE, message);
         break;
      case INFO:
         this.logger.log(Level.INFO, message);
         break;
      case WARN:
         this.logger.log(Level.WARNING, message);
         break;
      case ERROR:
         this.logger.log(Level.SEVERE, message);
      }

   }

   public void log(@NotNull LogLevel level, @Nullable String message, @Nullable Throwable throwable) {
      switch((LogLevel)Objects.requireNonNull(level, "level")) {
      case DEBUG:
         this.logger.log(Level.FINE, message, throwable);
         break;
      case INFO:
         this.logger.log(Level.INFO, message, throwable);
         break;
      case WARN:
         this.logger.log(Level.WARNING, message, throwable);
         break;
      case ERROR:
         this.logger.log(Level.SEVERE, message, throwable);
      }

   }
}
