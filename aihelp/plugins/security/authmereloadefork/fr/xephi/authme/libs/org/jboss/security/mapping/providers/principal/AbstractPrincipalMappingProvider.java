package fr.xephi.authme.libs.org.jboss.security.mapping.providers.principal;

import fr.xephi.authme.libs.org.jboss.security.mapping.MappingProvider;
import java.security.Principal;
import java.security.acl.Group;

public abstract class AbstractPrincipalMappingProvider implements MappingProvider<Principal> {
   public boolean supports(Class<?> p) {
      if (Group.class.isAssignableFrom(p)) {
         return false;
      } else {
         return Principal.class.isAssignableFrom(p);
      }
   }
}
