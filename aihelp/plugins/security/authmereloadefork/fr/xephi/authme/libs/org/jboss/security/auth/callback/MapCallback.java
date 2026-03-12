package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.Callback;

public class MapCallback implements Callback {
   private Map<String, Object> info = new HashMap();

   public Object getInfo(String key) {
      return this.info.get(key);
   }

   public void setInfo(String key, Object value) {
      this.info.put(key, value);
   }
}
