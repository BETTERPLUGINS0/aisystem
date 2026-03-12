package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import java.util.Map;
import java.util.Set;

public interface ACLProvider {
   void initialize(Map<String, Object> var1, Map<String, Object> var2);

   <T> Set<T> getEntitlements(Class<T> var1, Resource var2, Identity var3) throws AuthorizationException;

   boolean isAccessGranted(Resource var1, Identity var2, ACLPermission var3) throws AuthorizationException;

   ACLPersistenceStrategy getPersistenceStrategy();

   void setPersistenceStrategy(ACLPersistenceStrategy var1);

   boolean tearDown();
}
