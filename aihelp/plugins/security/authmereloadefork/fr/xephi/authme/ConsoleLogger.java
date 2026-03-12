package fr.xephi.authme;

import fr.xephi.authme.libs.com.google.common.base.Throwables;
import fr.xephi.authme.output.LogLevel;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.ExceptionUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConsoleLogger {
   private static final String NEW_LINE = System.getProperty("line.separator");
   private static final DateTimeFormatter DATE_FORMAT = (new DateTimeFormatterBuilder()).appendLiteral('[').appendPattern("MM-dd HH:mm:ss").appendLiteral(']').toFormatter();
   private static File logFile;
   private static Logger logger;
   private static OutputStreamWriter fileWriter;
   private final String name;
   private LogLevel logLevel;

   public ConsoleLogger(String name) {
      this.logLevel = LogLevel.INFO;
      this.name = name;
   }

   public static void initialize(Logger logger, File logFile) {
      ConsoleLogger.logger = logger;
      ConsoleLogger.logFile = logFile;
   }

   public static void initializeSharedSettings(Settings settings) {
      boolean useLogging = (Boolean)settings.getProperty(SecuritySettings.USE_LOGGING);
      if (useLogging) {
         initializeFileWriter();
      } else {
         closeFileWriter();
      }

   }

   public void initializeSettings(Settings settings) {
      this.logLevel = (LogLevel)settings.getProperty(PluginSettings.LOG_LEVEL);
   }

   public LogLevel getLogLevel() {
      return this.logLevel;
   }

   public String getName() {
      return this.name;
   }

   public void warning(String message) {
      logger.warning(message);
      writeLog("[WARN] " + message);
   }

   public void logException(String message, Throwable th) {
      this.warning(message + " " + ExceptionUtils.formatException(th));
      writeLog(Throwables.getStackTraceAsString(th));
   }

   public void info(String message) {
      logger.info(message);
      writeLog("[INFO] " + message);
   }

   public void fine(String message) {
      if (this.logLevel.includes(LogLevel.FINE)) {
         logger.info(message);
         writeLog("[INFO:FINE] " + message);
      }

   }

   public void debug(String message) {
      if (this.logLevel.includes(LogLevel.DEBUG)) {
         this.logAndWriteWithDebugPrefix(message);
      }

   }

   public void debug(String message, Object param1) {
      if (this.logLevel.includes(LogLevel.DEBUG)) {
         this.debug(message, param1);
      }

   }

   public void debug(String message, Object param1, Object param2) {
      if (this.logLevel.includes(LogLevel.DEBUG)) {
         this.debug(message, param1, param2);
      }

   }

   public void debug(String message, Object... params) {
      if (this.logLevel.includes(LogLevel.DEBUG)) {
         this.logAndWriteWithDebugPrefix(MessageFormat.format(message, params));
      }

   }

   public void debug(Supplier<String> msgSupplier) {
      if (this.logLevel.includes(LogLevel.DEBUG)) {
         this.logAndWriteWithDebugPrefix((String)msgSupplier.get());
      }

   }

   private void logAndWriteWithDebugPrefix(String message) {
      String debugMessage = "[INFO:DEBUG] " + message;
      logger.info(debugMessage);
      writeLog(debugMessage);
   }

   public static void closeFileWriter() {
      if (fileWriter != null) {
         try {
            fileWriter.flush();
         } catch (IOException var4) {
         } finally {
            closeSafely(fileWriter);
            fileWriter = null;
         }
      }

   }

   private static void writeLog(String message) {
      if (fileWriter != null) {
         String dateTime = DATE_FORMAT.format(LocalDateTime.now());

         try {
            fileWriter.write(dateTime);
            fileWriter.write(": ");
            fileWriter.write(message);
            fileWriter.write(NEW_LINE);
            fileWriter.flush();
         } catch (IOException var3) {
         }
      }

   }

   private static void closeSafely(Closeable closeable) {
      if (closeable != null) {
         try {
            closeable.close();
         } catch (IOException var2) {
            logger.log(Level.SEVERE, "Failed to close resource", var2);
         }
      }

   }

   private static void initializeFileWriter() {
      if (fileWriter == null) {
         FileOutputStream fos = null;

         try {
            fos = new FileOutputStream(logFile, true);
            fileWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
         } catch (Exception var2) {
            closeSafely(fos);
            logger.log(Level.SEVERE, "Failed to create writer to AuthMe log file", var2);
         }
      }

   }
}
