package fr.xephi.authme.libs.org.jboss.security.authorization.modules;

import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import javax.security.auth.Subject;

public abstract class AuthorizationModuleDelegate {
   protected PolicyRegistration policyRegistration = null;

   public abstract int authorize(Resource var1, Subject var2, RoleGroup var3);

   public void setPolicyRegistrationManager(PolicyRegistration pm) {
      this.policyRegistration = pm;
   }
}
