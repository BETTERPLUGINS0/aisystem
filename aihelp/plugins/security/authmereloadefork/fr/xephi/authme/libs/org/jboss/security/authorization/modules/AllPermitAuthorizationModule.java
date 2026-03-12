package fr.xephi.authme.libs.org.jboss.security.authorization.modules;

import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;

public class AllPermitAuthorizationModule extends AbstractAuthorizationModule {
   public int authorize(Resource resource) {
      return 1;
   }

   public boolean abort() throws AuthorizationException {
      return true;
   }

   public boolean commit() throws AuthorizationException {
      return true;
   }
}
