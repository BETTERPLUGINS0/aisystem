package fr.xephi.authme.libs.org.jboss.security.authorization.modules.web;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.AuthorizationModuleDelegate;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import javax.security.auth.Subject;

public class WebPolicyModuleDelegate extends AuthorizationModuleDelegate {
   public int authorize(Resource resource, Subject subject, RoleGroup role) {
      return 1;
   }
}
