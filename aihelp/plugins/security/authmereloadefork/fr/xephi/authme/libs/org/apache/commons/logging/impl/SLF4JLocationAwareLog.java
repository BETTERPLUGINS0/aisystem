package fr.xephi.authme.libs.org.apache.commons.logging.impl;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.org.slf4j.Marker;
import fr.xephi.authme.libs.org.slf4j.spi.LocationAwareLogger;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class SLF4JLocationAwareLog implements Log, Serializable {
   private static final long serialVersionUID = -2379157579039314822L;
   protected String name;
   private final transient LocationAwareLogger logger;
   private static final String FQCN = SLF4JLocationAwareLog.class.getName();

   public SLF4JLocationAwareLog(LocationAwareLogger logger) {
      this.logger = logger;
      this.name = logger.getName();
   }

   public boolean isTraceEnabled() {
      return this.logger.isTraceEnabled();
   }

   public boolean isDebugEnabled() {
      return this.logger.isDebugEnabled();
   }

   public boolean isInfoEnabled() {
      return this.logger.isInfoEnabled();
   }

   public boolean isWarnEnabled() {
      return this.logger.isWarnEnabled();
   }

   public boolean isErrorEnabled() {
      return this.logger.isErrorEnabled();
   }

   public boolean isFatalEnabled() {
      return this.logger.isErrorEnabled();
   }

   public void trace(Object message) {
      if (this.isTraceEnabled()) {
         this.logger.log((Marker)null, FQCN, 0, String.valueOf(message), (Object[])null, (Throwable)null);
      }

   }

   public void trace(Object message, Throwable t) {
      if (this.isTraceEnabled()) {
         this.logger.log((Marker)null, FQCN, 0, String.valueOf(message), (Object[])null, t);
      }

   }

   public void debug(Object message) {
      if (this.isDebugEnabled()) {
         this.logger.log((Marker)null, FQCN, 10, String.valueOf(message), (Object[])null, (Throwable)null);
      }

   }

   public void debug(Object message, Throwable t) {
      if (this.isDebugEnabled()) {
         this.logger.log((Marker)null, FQCN, 10, String.valueOf(message), (Object[])null, t);
      }

   }

   public void info(Object message) {
      if (this.isInfoEnabled()) {
         this.logger.log((Marker)null, FQCN, 20, String.valueOf(message), (Object[])null, (Throwable)null);
      }

   }

   public void info(Object message, Throwable t) {
      if (this.isInfoEnabled()) {
         this.logger.log((Marker)null, FQCN, 20, String.valueOf(message), (Object[])null, t);
      }

   }

   public void warn(Object message) {
      if (this.isWarnEnabled()) {
         this.logger.log((Marker)null, FQCN, 30, String.valueOf(message), (Object[])null, (Throwable)null);
      }

   }

   public void warn(Object message, Throwable t) {
      if (this.isWarnEnabled()) {
         this.logger.log((Marker)null, FQCN, 30, String.valueOf(message), (Object[])null, t);
      }

   }

   public void error(Object message) {
      if (this.isErrorEnabled()) {
         this.logger.log((Marker)null, FQCN, 40, String.valueOf(message), (Object[])null, (Throwable)null);
      }

   }

   public void error(Object message, Throwable t) {
      if (this.isErrorEnabled()) {
         this.logger.log((Marker)null, FQCN, 40, String.valueOf(message), (Object[])null, t);
      }

   }

   public void fatal(Object message) {
      if (this.isErrorEnabled()) {
         this.logger.log((Marker)null, FQCN, 40, String.valueOf(message), (Object[])null, (Throwable)null);
      }

   }

   public void fatal(Object message, Throwable t) {
      if (this.isErrorEnabled()) {
         this.logger.log((Marker)null, FQCN, 40, String.valueOf(message), (Object[])null, t);
      }

   }

   protected Object readResolve() throws ObjectStreamException {
      Logger logger = LoggerFactory.getLogger(this.name);
      return new SLF4JLocationAwareLog((LocationAwareLogger)logger);
   }
}
