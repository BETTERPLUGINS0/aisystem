package fr.xephi.authme.libs.org.jboss.security.mapping;

import fr.xephi.authme.libs.org.jboss.security.BaseSecurityManager;

public interface MappingManager extends BaseSecurityManager {
   /** @deprecated */
   <T> MappingContext<T> getMappingContext(Class<T> var1);

   <T> MappingContext<T> getMappingContext(String var1);
}
