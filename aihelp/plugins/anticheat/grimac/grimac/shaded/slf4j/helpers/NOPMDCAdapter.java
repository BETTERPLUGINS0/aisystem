package ac.grim.grimac.shaded.slf4j.helpers;

import ac.grim.grimac.shaded.slf4j.spi.MDCAdapter;
import java.util.Deque;
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

   public void pushByKey(String key, String value) {
   }

   public String popByKey(String key) {
      return null;
   }

   public Deque<String> getCopyOfDequeByKey(String key) {
      return null;
   }

   public void clearDequeByKey(String key) {
   }
}
