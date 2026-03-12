package fr.xephi.authme.libs.org.jboss.security.authorization;

import java.util.Map;

public interface Resource {
   ResourceType getLayer();

   Map<String, Object> getMap();

   void add(String var1, Object var2);
}
