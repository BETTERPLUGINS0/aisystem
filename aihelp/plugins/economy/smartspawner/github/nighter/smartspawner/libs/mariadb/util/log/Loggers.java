package github.nighter.smartspawner.libs.mariadb.util.log;

import java.util.HashMap;

public final class Loggers {
   public static final String FALLBACK_PROPERTY = "mariadb.logging.fallback";
   public static final String CONSOLE_DEBUG_PROPERTY = "mariadb.logging.fallback.console.debug";
   public static final String TEST_ENABLE_SLF4J = "mariadb.logging.slf4j.enable";
   public static final String NO_LOGGER_PROPERTY = "mariadb.logging.disable";
   private static Loggers.LoggerFactory LOGGER_FACTORY;

   public static Logger getLogger(String name) {
      return LOGGER_FACTORY.getLogger(name);
   }

   public static Logger getLogger(Class<?> cls) {
      return LOGGER_FACTORY.getLogger(cls.getName());
   }

   public static void init() {
      String name = Loggers.LoggerFactory.class.getName();
      Loggers.LoggerFactory loggerFactory = null;
      if (Boolean.parseBoolean(System.getProperty("mariadb.logging.disable", "false"))) {
         loggerFactory = new Loggers.NoLoggerFactory();
      } else {
         try {
            if (Boolean.parseBoolean(System.getProperty("mariadb.logging.slf4j.enable", "true"))) {
               Class.forName("org.slf4j.LoggerFactory");
               loggerFactory = new Loggers.Slf4JLoggerFactory();
            }
         } catch (ClassNotFoundException var4) {
         }

         if (loggerFactory == null) {
            if ("JDK".equalsIgnoreCase(System.getProperty("mariadb.logging.fallback"))) {
               loggerFactory = new Loggers.JdkLoggerFactory();
            } else {
               loggerFactory = new Loggers.ConsoleLoggerFactory();
            }
         }

         try {
            ((Loggers.LoggerFactory)loggerFactory).getLogger(name);
         } catch (Throwable var3) {
         }
      }

      LOGGER_FACTORY = (Loggers.LoggerFactory)loggerFactory;
   }

   static {
      init();
   }

   private interface LoggerFactory {
      Logger getLogger(String var1);
   }

   private static class NoLoggerFactory implements Loggers.LoggerFactory {
      private NoLoggerFactory() {
      }

      public Logger getLogger(String name) {
         return new NoLogger();
      }

      // $FF: synthetic method
      NoLoggerFactory(Object x0) {
         this();
      }
   }

   private static class Slf4JLoggerFactory implements Loggers.LoggerFactory {
      private Slf4JLoggerFactory() {
      }

      public Logger getLogger(String name) {
         return new Slf4JLogger(org.slf4j.LoggerFactory.getLogger(name));
      }

      // $FF: synthetic method
      Slf4JLoggerFactory(Object x0) {
         this();
      }
   }

   private static class JdkLoggerFactory implements Loggers.LoggerFactory {
      private JdkLoggerFactory() {
      }

      public Logger getLogger(String name) {
         return new JdkLogger(java.util.logging.Logger.getLogger(name));
      }

      // $FF: synthetic method
      JdkLoggerFactory(Object x0) {
         this();
      }
   }

   private static final class ConsoleLoggerFactory implements Loggers.LoggerFactory {
      private static final HashMap<String, Logger> consoleLoggers = new HashMap();

      private ConsoleLoggerFactory() {
      }

      public Logger getLogger(String name) {
         return (Logger)consoleLoggers.computeIfAbsent(name, (n) -> {
            return new ConsoleLogger(n, System.getProperty("mariadb.logging.fallback.console.debug") != null);
         });
      }

      // $FF: synthetic method
      ConsoleLoggerFactory(Object x0) {
         this();
      }
   }
}
