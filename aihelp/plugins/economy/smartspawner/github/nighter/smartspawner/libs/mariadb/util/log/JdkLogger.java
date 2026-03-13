package github.nighter.smartspawner.libs.mariadb.util.log;

import java.util.logging.Level;
import java.util.regex.Matcher;

public class JdkLogger implements Logger {
   private final java.util.logging.Logger logger;

   public JdkLogger(java.util.logging.Logger logger) {
      this.logger = logger;
   }

   public String getName() {
      return this.logger.getName();
   }

   public boolean isTraceEnabled() {
      return this.logger.isLoggable(Level.FINEST);
   }

   public void trace(String msg) {
      this.logger.log(Level.FINEST, msg);
   }

   public void trace(String format, Object... arguments) {
      this.logger.log(Level.FINEST, this.format(format, arguments));
   }

   public void trace(String msg, Throwable t) {
      this.logger.log(Level.FINEST, msg, t);
   }

   public boolean isDebugEnabled() {
      return this.logger.isLoggable(Level.FINE);
   }

   public void debug(String msg) {
      this.logger.log(Level.FINE, msg);
   }

   public void debug(String format, Object... arguments) {
      this.logger.log(Level.FINE, this.format(format, arguments));
   }

   public void debug(String msg, Throwable t) {
      this.logger.log(Level.FINE, msg, t);
   }

   public boolean isInfoEnabled() {
      return this.logger.isLoggable(Level.INFO);
   }

   public void info(String msg) {
      this.logger.log(Level.INFO, msg);
   }

   public void info(String format, Object... arguments) {
      this.logger.log(Level.INFO, this.format(format, arguments));
   }

   public void info(String msg, Throwable t) {
      this.logger.log(Level.INFO, msg, t);
   }

   public boolean isWarnEnabled() {
      return this.logger.isLoggable(Level.WARNING);
   }

   public void warn(String msg) {
      this.logger.log(Level.WARNING, msg);
   }

   public void warn(String format, Object... arguments) {
      this.logger.log(Level.WARNING, this.format(format, arguments));
   }

   public void warn(String msg, Throwable t) {
      this.logger.log(Level.WARNING, msg, t);
   }

   public boolean isErrorEnabled() {
      return this.logger.isLoggable(Level.SEVERE);
   }

   public void error(String msg) {
      this.logger.log(Level.SEVERE, msg);
   }

   public void error(String format, Object... arguments) {
      this.logger.log(Level.SEVERE, this.format(format, arguments));
   }

   public void error(String msg, Throwable t) {
      this.logger.log(Level.SEVERE, msg, t);
   }

   final String format(String from, Object... arguments) {
      if (from == null) {
         return null;
      } else {
         String computed = from;
         Object[] var4 = arguments;
         int var5 = arguments.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Object argument = var4[var6];
            computed = computed.replaceFirst("\\{}", Matcher.quoteReplacement(String.valueOf(argument)));
         }

         return computed;
      }
   }
}
