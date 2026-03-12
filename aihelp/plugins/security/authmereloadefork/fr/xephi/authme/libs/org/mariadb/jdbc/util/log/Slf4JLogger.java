package fr.xephi.authme.libs.org.mariadb.jdbc.util.log;

public class Slf4JLogger implements Logger {
   private final fr.xephi.authme.libs.org.slf4j.Logger logger;

   public Slf4JLogger(fr.xephi.authme.libs.org.slf4j.Logger logger) {
      this.logger = logger;
   }

   public String getName() {
      return this.logger.getName();
   }

   public boolean isTraceEnabled() {
      return this.logger.isTraceEnabled();
   }

   public void trace(String msg) {
      this.logger.trace(msg);
   }

   public void trace(String format, Object... arguments) {
      this.logger.trace(format, arguments);
   }

   public void trace(String msg, Throwable t) {
      this.logger.trace(msg, t);
   }

   public boolean isDebugEnabled() {
      return this.logger.isDebugEnabled();
   }

   public void debug(String msg) {
      this.logger.debug(msg);
   }

   public void debug(String format, Object... arguments) {
      this.logger.debug(format, arguments);
   }

   public void debug(String msg, Throwable t) {
      this.logger.debug(msg, t);
   }

   public boolean isInfoEnabled() {
      return this.logger.isInfoEnabled();
   }

   public void info(String msg) {
      this.logger.info(msg);
   }

   public void info(String format, Object... arguments) {
      this.logger.info(format, arguments);
   }

   public void info(String msg, Throwable t) {
      this.logger.info(msg, t);
   }

   public boolean isWarnEnabled() {
      return this.logger.isWarnEnabled();
   }

   public void warn(String msg) {
      this.logger.warn(msg);
   }

   public void warn(String format, Object... arguments) {
      this.logger.warn(format, arguments);
   }

   public void warn(String msg, Throwable t) {
      this.logger.warn(msg, t);
   }

   public boolean isErrorEnabled() {
      return this.logger.isErrorEnabled();
   }

   public void error(String msg) {
      this.logger.error(msg);
   }

   public void error(String format, Object... arguments) {
      this.logger.error(format, arguments);
   }

   public void error(String msg, Throwable t) {
      this.logger.error(msg, t);
   }
}
