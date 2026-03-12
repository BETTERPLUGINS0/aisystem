package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

public interface AuthorizationManager extends BaseSecurityManager {
   int authorize(Resource var1) throws AuthorizationException;

   int authorize(Resource var1, Subject var2) throws AuthorizationException;

   int authorize(Resource var1, Subject var2, RoleGroup var3) throws AuthorizationException;

   int authorize(Resource var1, Subject var2, Group var3) throws AuthorizationException;

   boolean doesUserHaveRole(Principal var1, Set<Principal> var2);

   RoleGroup getSubjectRoles(Subject var1, CallbackHandler var2);

   Set<Principal> getUserRoles(Principal var1);

   Group getTargetRoles(Principal var1, Map<String, Object> var2);
}
