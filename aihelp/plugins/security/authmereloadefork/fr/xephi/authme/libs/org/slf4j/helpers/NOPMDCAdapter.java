package fr.xephi.authme.libs.org.slf4j.helpers;

import fr.xephi.authme.libs.org.slf4j.spi.MDCAdapter;
import java.util.Map;

public class NOPMDCAdapter implements MDCAdapter {
   public void clear() {
   }

   public String get(String key) {
      return null;
   }

   public void put(String key, String val) {
   }

   public void remove(String key) {
   }

   public Map<String, String> getCopyOfContextMap() {
      return null;
   }

   public void setContextMap(Map<String, String> contextMap) {
   }
}
