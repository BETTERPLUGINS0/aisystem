package fr.xephi.authme.libs.org.jboss.security.mapping;

import java.util.Map;

public interface MappingProvider<T> {
   void init(Map<String, Object> var1);

   void performMapping(Map<String, Object> var1, T var2);

   void setMappingResult(MappingResult<T> var1);

   boolean supports(Class<?> var1);
}
