package fr.xephi.authme.libs.org.jboss.security.audit;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AuditEvent {
   private String auditLevel;
   private Map<String, Object> contextMap;
   private Exception underlyingException;

   public AuditEvent(String level) {
      this.auditLevel = "Info";
      this.contextMap = new HashMap();
      this.underlyingException = null;
      this.auditLevel = level;
   }

   public AuditEvent(String level, Map<String, Object> map) {
      this(level);
      this.contextMap = map;
   }

   public AuditEvent(String level, Map<String, Object> map, Exception ex) {
      this(level, map);
      this.underlyingException = ex;
   }

   public String getAuditLevel() {
      return this.auditLevel;
   }

   public Map<String, Object> getContextMap() {
      return this.contextMap;
   }

   public void setContextMap(Map<String, Object> cmap) {
      this.contextMap = cmap;
   }

   public Exception getUnderlyingException() {
      return this.underlyingException;
   }

   public void setUnderlyingException(Exception underlyingException) {
      this.underlyingException = underlyingException;
   }

   public String toString() {
      StringBuilder sbu = new StringBuilder();
      sbu.append("[").append(this.auditLevel).append("]");
      sbu.append(this.dissectContextMap());
      return sbu.toString();
   }

   private String dissectContextMap() {
      StringBuilder sbu = new StringBuilder();
      if (this.contextMap != null) {
         Iterator i$ = this.contextMap.keySet().iterator();

         while(true) {
            while(i$.hasNext()) {
               String key = (String)i$.next();
               sbu.append(key).append("=");
               Object obj = this.contextMap.get(key);
               if (obj instanceof Object[]) {
                  Object[] arr = (Object[])((Object[])obj);
                  obj = Arrays.asList(arr);
               }

               if (obj instanceof Collection) {
                  Collection<Object> coll = (Collection)obj;
                  Iterator i$ = coll.iterator();

                  while(i$.hasNext()) {
                     Object o = i$.next();
                     sbu.append(o).append(";");
                  }
               } else {
                  sbu.append(obj).append(";");
               }
            }

            return sbu.toString();
         }
      } else {
         return sbu.toString();
      }
   }
}
