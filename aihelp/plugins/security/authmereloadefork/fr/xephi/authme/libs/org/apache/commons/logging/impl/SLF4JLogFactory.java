package fr.xephi.authme.libs.org.apache.commons.logging.impl;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogConfigurationException;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.org.slf4j.spi.LocationAwareLogger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SLF4JLogFactory extends LogFactory {
   ConcurrentMap<String, Log> loggerMap = new ConcurrentHashMap();
   public static final String LOG_PROPERTY = "fr.xephi.authme.libs.org.apache.commons.logging.Log";
   protected Hashtable attributes = new Hashtable();

   public Object getAttribute(String name) {
      return this.attributes.get(name);
   }

   public String[] getAttributeNames() {
      List<String> names = new ArrayList();
      Enumeration keys = this.attributes.keys();

      while(keys.hasMoreElements()) {
         names.add((String)keys.nextElement());
      }

      String[] results = new String[names.size()];

      for(int i = 0; i < results.length; ++i) {
         results[i] = (String)names.get(i);
      }

      return results;
   }

   public Log getInstance(Class clazz) throws LogConfigurationException {
      return this.getInstance(clazz.getName());
   }

   public Log getInstance(String name) throws LogConfigurationException {
      Log instance = (Log)this.loggerMap.get(name);
      if (instance != null) {
         return instance;
      } else {
         Logger slf4jLogger = LoggerFactory.getLogger(name);
         Object newInstance;
         if (slf4jLogger instanceof LocationAwareLogger) {
            newInstance = new SLF4JLocationAwareLog((LocationAwareLogger)slf4jLogger);
         } else {
            newInstance = new SLF4JLog(slf4jLogger);
         }

         Log oldInstance = (Log)this.loggerMap.putIfAbsent(name, newInstance);
         return (Log)(oldInstance == null ? newInstance : oldInstance);
      }
   }

   public void release() {
      System.out.println("WARN: The method " + SLF4JLogFactory.class + "#release() was invoked.");
      System.out.println("WARN: Please see http://www.slf4j.org/codes.html#release for an explanation.");
      System.out.flush();
   }

   public void removeAttribute(String name) {
      this.attributes.remove(name);
   }

   public void setAttribute(String name, Object value) {
      if (value == null) {
         this.attributes.remove(name);
      } else {
         this.attributes.put(name, value);
      }

   }
}
