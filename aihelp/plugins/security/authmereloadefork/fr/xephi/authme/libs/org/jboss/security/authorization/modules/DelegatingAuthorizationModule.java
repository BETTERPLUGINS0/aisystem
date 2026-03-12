package fr.xephi.authme.libs.org.jboss.security.authorization.modules;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.ejb.EJBPolicyModuleDelegate;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.web.WebPolicyModuleDelegate;

public class DelegatingAuthorizationModule extends AbstractAuthorizationModule {
   public DelegatingAuthorizationModule() {
      this.delegateMap.put(ResourceType.WEB, WebPolicyModuleDelegate.class.getName());
      this.delegateMap.put(ResourceType.EJB, EJBPolicyModuleDelegate.class.getName());
   }

   public int authorize(Resource resource) {
      return this.invokeDelegate(resource);
   }
}
