package fr.xephi.authme.libs.org.jboss.security.authorization.modules;

import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.ejb.EJBXACMLPolicyModuleDelegate;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.web.WebXACMLPolicyModuleDelegate;

public class XACMLAuthorizationModule extends AbstractAuthorizationModule {
   public XACMLAuthorizationModule() {
      this.delegateMap.put(ResourceType.WEB, WebXACMLPolicyModuleDelegate.class.getName());
      this.delegateMap.put(ResourceType.EJB, EJBXACMLPolicyModuleDelegate.class.getName());
   }

   public int authorize(Resource resource) {
      return this.invokeDelegate(resource);
   }
}
