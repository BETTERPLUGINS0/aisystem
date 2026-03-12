package fr.xephi.authme.libs.org.picketbox.core.authorization.resources;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class POJOResource implements Resource {
   private Map<String, Object> map = new HashMap();
   private Object pojo = null;

   public POJOResource(Object obj) {
      this.pojo = obj;
   }

   public ResourceType getLayer() {
      return ResourceType.POJO;
   }

   public void add(Map<String, Object> m) {
      this.map.putAll(m);
   }

   public Map<String, Object> getMap() {
      return Collections.unmodifiableMap(this.map);
   }

   public void add(String key, Object value) {
      this.map.put(key, value);
   }
}
