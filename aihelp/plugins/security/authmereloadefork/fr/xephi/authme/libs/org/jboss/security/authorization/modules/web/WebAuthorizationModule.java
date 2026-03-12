package fr.xephi.authme.libs.org.jboss.security.authorization.modules.web;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.AbstractAuthorizationModule;

public class WebAuthorizationModule extends AbstractAuthorizationModule {
   public int authorize(Resource resource) {
      return 1;
   }
}
