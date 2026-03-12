package ac.grim.grimac.shaded.slf4j.spi;

import ac.grim.grimac.shaded.slf4j.Logger;
import ac.grim.grimac.shaded.slf4j.Marker;
import ac.grim.grimac.shaded.slf4j.event.DefaultLoggingEvent;
import ac.grim.grimac.shaded.slf4j.event.KeyValuePair;
import ac.grim.grimac.shaded.slf4j.event.Level;
import ac.grim.grimac.shaded.slf4j.event.LoggingEvent;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class DefaultLoggingEventBuilder implements LoggingEventBuilder, CallerBoundaryAware {
   static String DLEB_FQCN = DefaultLoggingEventBuilder.class.getName();
   protected DefaultLoggingEvent loggingEvent;
   protected Logger logger;

   public DefaultLoggingEventBuilder(Logger logger, Level level) {
      this.logger = logger;
      this.loggingEvent = new DefaultLoggingEvent(level, logger);
   }

   public LoggingEventBuilder addMarker(Marker marker) {
      this.loggingEvent.addMarker(marker);
      return this;
   }

   public LoggingEventBuilder setCause(Throwable t) {
      this.loggingEvent.setThrowable(t);
      return this;
   }

   public LoggingEventBuilder addArgument(Object p) {
      this.loggingEvent.addArgument(p);
      return this;
   }

   public LoggingEventBuilder addArgument(Supplier<?> objectSupplier) {
      this.loggingEvent.addArgument(objectSupplier.get());
      return this;
   }

   public LoggingEventBuilder addKeyValue(String key, Object value) {
      this.loggingEvent.addKeyValue(key, value);
      return this;
   }

   public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) {
      this.loggingEvent.addKeyValue(key, value.get());
      return this;
   }

   public void setCallerBoundary(String fqcn) {
      this.loggingEvent.setCallerBoundary(fqcn);
   }

   public void log() {
      this.log((LoggingEvent)this.loggingEvent);
   }

   public LoggingEventBuilder setMessage(String message) {
      this.loggingEvent.setMessage(message);
      return this;
   }

   public LoggingEventBuilder setMessage(Supplier<String> messageSupplier) {
      this.loggingEvent.setMessage((String)messageSupplier.get());
      return this;
   }

   public void log(String message) {
      this.loggingEvent.setMessage(message);
      this.log((LoggingEvent)this.loggingEvent);
   }

   public void log(String message, Object arg) {
      this.loggingEvent.setMessage(message);
      this.loggingEvent.addArgument(arg);
      this.log((LoggingEvent)this.loggingEvent);
   }

   public void log(String message, Object arg0, Object arg1) {
      this.loggingEvent.setMessage(message);
      this.loggingEvent.addArgument(arg0);
      this.loggingEvent.addArgument(arg1);
      this.log((LoggingEvent)this.loggingEvent);
   }

   public void log(String message, Object... args) {
      this.loggingEvent.setMessage(message);
      this.loggingEvent.addArguments(args);
      this.log((LoggingEvent)this.loggingEvent);
   }

   public void log(Supplier<String> messageSupplier) {
      if (messageSupplier == null) {
         this.log((String)null);
      } else {
         this.log((String)messageSupplier.get());
      }

   }

   protected void log(LoggingEvent aLoggingEvent) {
      if (aLoggingEvent.getCallerBoundary() == null) {
         this.setCallerBoundary(DLEB_FQCN);
      }

      if (this.logger instanceof LoggingEventAware) {
         ((LoggingEventAware)this.logger).log(aLoggingEvent);
      } else if (this.logger instanceof LocationAwareLogger) {
         this.logViaLocationAwareLoggerAPI((LocationAwareLogger)this.logger, aLoggingEvent);
      } else {
         this.logViaPublicSLF4JLoggerAPI(aLoggingEvent);
      }

   }

   private void logViaLocationAwareLoggerAPI(LocationAwareLogger locationAwareLogger, LoggingEvent aLoggingEvent) {
      String msg = aLoggingEvent.getMessage();
      List<Marker> markerList = aLoggingEvent.getMarkers();
      String mergedMessage = this.mergeMarkersAndKeyValuePairsAndMessage(aLoggingEvent);
      locationAwareLogger.log((Marker)null, aLoggingEvent.getCallerBoundary(), aLoggingEvent.getLevel().toInt(), mergedMessage, aLoggingEvent.getArgumentArray(), aLoggingEvent.getThrowable());
   }

   private void logViaPublicSLF4JLoggerAPI(LoggingEvent aLoggingEvent) {
      Object[] argArray = aLoggingEvent.getArgumentArray();
      int argLen = argArray == null ? 0 : argArray.length;
      Throwable t = aLoggingEvent.getThrowable();
      int tLen = t == null ? 0 : 1;
      Object[] combinedArguments = new Object[argLen + tLen];
      if (argArray != null) {
         System.arraycopy(argArray, 0, combinedArguments, 0, argLen);
      }

      if (t != null) {
         combinedArguments[argLen] = t;
      }

      String mergedMessage = this.mergeMarkersAndKeyValuePairsAndMessage(aLoggingEvent);
      switch(aLoggingEvent.getLevel()) {
      case TRACE:
         this.logger.trace(mergedMessage, combinedArguments);
         break;
      case DEBUG:
         this.logger.debug(mergedMessage, combinedArguments);
         break;
      case INFO:
         this.logger.info(mergedMessage, combinedArguments);
         break;
      case WARN:
         this.logger.warn(mergedMessage, combinedArguments);
         break;
      case ERROR:
         this.logger.error(mergedMessage, combinedArguments);
      }

   }

   private String mergeMarkersAndKeyValuePairsAndMessage(LoggingEvent aLoggingEvent) {
      StringBuilder sb = this.mergeMarkers(aLoggingEvent.getMarkers(), (StringBuilder)null);
      sb = this.mergeKeyValuePairs(aLoggingEvent.getKeyValuePairs(), sb);
      String mergedMessage = this.mergeMessage(aLoggingEvent.getMessage(), sb);
      return mergedMessage;
   }

   private StringBuilder mergeMarkers(List<Marker> markerList, StringBuilder sb) {
      if (markerList != null && !markerList.isEmpty()) {
         if (sb == null) {
            sb = new StringBuilder();
         }

         Iterator var3 = markerList.iterator();

         while(var3.hasNext()) {
            Marker marker = (Marker)var3.next();
            sb.append(marker);
            sb.append(' ');
         }

         return sb;
      } else {
         return sb;
      }
   }

   private StringBuilder mergeKeyValuePairs(List<KeyValuePair> keyValuePairList, StringBuilder sb) {
      if (keyValuePairList != null && !keyValuePairList.isEmpty()) {
         if (sb == null) {
            sb = new StringBuilder();
         }

         Iterator var3 = keyValuePairList.iterator();

         while(var3.hasNext()) {
            KeyValuePair kvp = (KeyValuePair)var3.next();
            sb.append(kvp.key);
            sb.append('=');
            sb.append(kvp.value);
            sb.append(' ');
         }

         return sb;
      } else {
         return sb;
      }
   }

   private String mergeMessage(String msg, StringBuilder sb) {
      if (sb != null) {
         sb.append(msg);
         return sb.toString();
      } else {
         return msg;
      }
   }
}
