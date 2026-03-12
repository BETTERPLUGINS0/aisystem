package fr.xephi.authme.libs.org.jboss.security.authorization.modules;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.ejb.EJBJACCPolicyModuleDelegate;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.web.WebJACCPolicyModuleDelegate;

public class JACCAuthorizationModule extends AbstractAuthorizationModule {
   public JACCAuthorizationModule() {
      this.delegateMap.put(ResourceType.WEB, WebJACCPolicyModuleDelegate.class.getName());
      this.delegateMap.put(ResourceType.EJB, EJBJACCPolicyModuleDelegate.class.getName());
   }

   public int authorize(Resource resource) {
      return this.invokeDelegate(resource);
   }
}
