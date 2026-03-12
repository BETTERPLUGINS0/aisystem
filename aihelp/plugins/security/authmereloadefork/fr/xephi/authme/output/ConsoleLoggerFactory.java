package fr.xephi.authme.output;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.settings.Settings;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ConsoleLoggerFactory {
   private static final Map<String, ConsoleLogger> consoleLoggers = new ConcurrentHashMap();
   private static Settings settings;

   private ConsoleLoggerFactory() {
   }

   public static ConsoleLogger get(Class<?> owningClass) {
      String name = owningClass.getCanonicalName();
      return (ConsoleLogger)consoleLoggers.computeIfAbsent(name, ConsoleLoggerFactory::createLogger);
   }

   public static void reloadSettings(Settings settings) {
      ConsoleLoggerFactory.settings = settings;
      ConsoleLogger.initializeSharedSettings(settings);
      consoleLoggers.values().forEach((logger) -> {
         logger.initializeSettings(settings);
      });
   }

   public static int getTotalLoggers() {
      return consoleLoggers.size();
   }

   private static ConsoleLogger createLogger(String name) {
      ConsoleLogger logger = new ConsoleLogger(name);
      if (settings != null) {
         logger.initializeSettings(settings);
      }

      return logger;
   }
}
