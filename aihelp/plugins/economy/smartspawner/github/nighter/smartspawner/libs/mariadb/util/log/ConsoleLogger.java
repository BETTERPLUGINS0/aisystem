package github.nighter.smartspawner.libs.mariadb.util.log;

import java.io.PrintStream;
import java.util.regex.Matcher;

public class ConsoleLogger implements Logger {
   private final String name;
   private final PrintStream err;
   private final PrintStream log;
   private final boolean logDebugLvl;

   public ConsoleLogger(String name, PrintStream log, PrintStream err, boolean logDebugLvl) {
      this.name = name;
      this.log = log;
      this.err = err;
      this.logDebugLvl = logDebugLvl;
   }

   ConsoleLogger(String name, boolean logDebugLvl) {
      this(name, System.out, System.err, logDebugLvl);
   }

   public String getName() {
      return this.name;
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
            computed = computed.replaceFirst("\\{\\}", Matcher.quoteReplacement(String.valueOf(argument)));
         }

         return computed;
      }
   }

   public boolean isTraceEnabled() {
      return this.logDebugLvl;
   }

   public synchronized void trace(String msg) {
      if (this.logDebugLvl) {
         this.log.format("[TRACE] (%s) %s\n", Thread.currentThread().getName(), msg);
      }
   }

   public synchronized void trace(String format, Object... arguments) {
      if (this.logDebugLvl) {
         this.log.format("[TRACE] (%s) %s\n", Thread.currentThread().getName(), this.format(format, arguments));
      }
   }

   public synchronized void trace(String msg, Throwable t) {
      if (this.logDebugLvl) {
         this.log.format("[TRACE] (%s) %s - %s\n", Thread.currentThread().getName(), msg, t);
         t.printStackTrace(this.log);
      }
   }

   public boolean isDebugEnabled() {
      return this.logDebugLvl;
   }

   public synchronized void debug(String msg) {
      if (this.logDebugLvl) {
         this.log.format("[DEBUG] (%s) %s\n", Thread.currentThread().getName(), msg);
      }
   }

   public synchronized void debug(String format, Object... arguments) {
      if (this.logDebugLvl) {
         this.log.format("[DEBUG] (%s) %s\n", Thread.currentThread().getName(), this.format(format, arguments));
      }
   }

   public synchronized void debug(String msg, Throwable t) {
      if (this.logDebugLvl) {
         this.log.format("[DEBUG] (%s) %s - %s\n", Thread.currentThread().getName(), msg, t);
         t.printStackTrace(this.log);
      }
   }

   public boolean isInfoEnabled() {
      return true;
   }

   public synchronized void info(String msg) {
      this.log.format("[ INFO] (%s) %s\n", Thread.currentThread().getName(), msg);
   }

   public synchronized void info(String format, Object... arguments) {
      this.log.format("[ INFO] (%s) %s\n", Thread.currentThread().getName(), this.format(format, arguments));
   }

   public synchronized void info(String msg, Throwable t) {
      this.log.format("[ INFO] (%s) %s - %s\n", Thread.currentThread().getName(), msg, t);
      t.printStackTrace(this.log);
   }

   public boolean isWarnEnabled() {
      return true;
   }

   public synchronized void warn(String msg) {
      this.err.format("[ WARN] (%s) %s\n", Thread.currentThread().getName(), msg);
   }

   public synchronized void warn(String format, Object... arguments) {
      this.err.format("[ WARN] (%s) %s\n", Thread.currentThread().getName(), this.format(format, arguments));
   }

   public synchronized void warn(String msg, Throwable t) {
      this.err.format("[ WARN] (%s) %s - %s\n", Thread.currentThread().getName(), msg, t);
      t.printStackTrace(this.err);
   }

   public boolean isErrorEnabled() {
      return true;
   }

   public synchronized void error(String msg) {
      this.err.format("[ERROR] (%s) %s\n", Thread.currentThread().getName(), msg);
   }

   public synchronized void error(String format, Object... arguments) {
      this.err.format("[ERROR] (%s) %s\n", Thread.currentThread().getName(), this.format(format, arguments));
   }

   public synchronized void error(String msg, Throwable t) {
      this.err.format("[ERROR] (%s) %s - %s\n", Thread.currentThread().getName(), msg, t);
      t.printStackTrace(this.err);
   }
}
